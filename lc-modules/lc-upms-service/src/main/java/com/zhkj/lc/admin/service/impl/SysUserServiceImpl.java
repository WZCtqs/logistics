/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the lc4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */

package com.zhkj.lc.admin.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zhkj.lc.admin.common.util.PswGenerator;
import com.zhkj.lc.admin.mapper.SysUserMapper;
import com.zhkj.lc.admin.model.dto.UserDTO;
import com.zhkj.lc.admin.model.dto.UserInfo;
import com.zhkj.lc.admin.model.entity.SysDeptRelation;
import com.zhkj.lc.admin.model.entity.SysUser;
import com.zhkj.lc.admin.model.entity.SysUserPost;
import com.zhkj.lc.admin.model.entity.SysUserRole;
import com.zhkj.lc.admin.service.*;
import com.zhkj.lc.common.bean.interceptor.DataScope;
import com.zhkj.lc.common.constant.CommonConstant;
import com.zhkj.lc.common.constant.MqQueueConstant;
import com.zhkj.lc.common.constant.SecurityConstants;
import com.zhkj.lc.common.constant.enums.EnumSmsChannelTemplate;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.util.R;
import com.zhkj.lc.common.util.template.MobileMsgTemplate;
import com.zhkj.lc.common.vo.MenuVO;
import com.zhkj.lc.common.vo.SysRole;
import com.zhkj.lc.common.vo.UserVO;
import com.xiaoleilu.hutool.collection.CollectionUtil;
import com.xiaoleilu.hutool.util.ArrayUtil;
import com.xiaoleilu.hutool.util.RandomUtil;
import com.xiaoleilu.hutool.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
//import org.apache.velocity.runtime.directive.Foreach;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author lengleng
 * @date 2017/10/31
 */
@Slf4j
@Service
@AllArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();
    private final SysMenuService sysMenuService;
    private final RedisTemplate redisTemplate;
    private final SysUserMapper sysUserMapper;
    private final RabbitTemplate rabbitTemplate;
    private final SysUserRoleService sysUserRoleService;
    private final SysDeptRelationService sysDeptRelationService;
    private final SysUserPostService sysUserPostService;

    @Override
    public UserInfo findUserInfo(UserVO userVo,Integer tenantId) {
        SysUser condition = new SysUser();
        condition.setUsername(userVo.getUsername());
        //加入租户id的过滤
        condition.setTenantId(tenantId);
        SysUser sysUser = this.selectOne(new EntityWrapper<>(condition));
        UserInfo userInfo = new UserInfo();
        userInfo.setSysUser(sysUser);
        //设置角色列表
        List<String> roleCodes = userVo.getRoleList().stream()
                .filter(sysRole -> !StrUtil.equals(SecurityConstants.BASE_ROLE, sysRole.getRoleCode()))
                .map(SysRole::getRoleCode)
                .collect(Collectors.toList());
        String[] roles = ArrayUtil.toArray(roleCodes, String.class);
        userInfo.setRoles(roles);

        //设置权限列表（menu.permission）
        Set<String> permissions = new HashSet<>();
        Arrays.stream(roles).forEach(role -> {
            List<MenuVO> menuVos = sysMenuService.findMenuByRoleName(role);
            List<String> permissionList = menuVos.stream()
                    .filter(menuVo -> StringUtils.isNotEmpty(menuVo.getPermission()))
                    .map(MenuVO::getPermission).collect(Collectors.toList());
            permissions.addAll(permissionList);
        });
        userInfo.setPermissions(ArrayUtil.toArray(permissions, String.class));
        return userInfo;
    }

   /* @Override
    public UserInfo findUserInfo(UserVO userVo,Integer tenantId) {
        SysUser condition = new SysUser();
        condition.setUsername(userVo.getUsername());
        //加入租户id的过滤
        condition.setTenantId(tenantId);
        SysUser sysUser = this.selectOne(new EntityWrapper<>(condition));
        UserInfo userInfo = new UserInfo();
        userInfo.setSysUser(sysUser);
        //设置角色列表
        List<Integer> rolelist = new ArrayList<>();
        List<SysRole> sysRoles = userVo.getRoleList();
        for (SysRole role :sysRoles) {
            rolelist.add(role.getRoleId());
        }
        Integer[]roleIds = ArrayUtil.toArray(rolelist,Integer.class);
        userInfo.setRoles(roleIds);

        //设置权限列表（menu.permission）
        Set<String> permissions = new HashSet<>();
        Arrays.stream(roleIds).forEach(roleId -> {
            List<MenuVO> menuVos = sysMenuService.findMenuByRoleId(roleId);
            List<String> permissionList = menuVos.stream()
                    .filter(menuVo -> StringUtils.isNotEmpty(menuVo.getPermission()))
                    .map(MenuVO::getPermission).collect(Collectors.toList());
            permissions.addAll(permissionList);
        });
        userInfo.setPermissions(ArrayUtil.toArray(permissions, String.class));
        return userInfo;
    }*/

    @Override
    @Cacheable(value = "user_details", key = "#username")
    public UserVO findUserByUsername(String username) {
        return sysUserMapper.selectUserVoByUsername(username);
    }

    /**
     * 通过手机号查询用户信息
     *
     * @param mobile 手机号
     * @return 用户信息
     */
    @Override
    @Cacheable(value = "user_details_mobile", key = "#mobile")
    public UserVO findUserByMobile(String mobile) {
        return sysUserMapper.selectUserVoByMobile(mobile);
    }

    /**
     * 通过openId查询用户
     *
     * @param openId openId
     * @return 用户信息
     */
    @Override
    @Cacheable(value = "user_details_openid", key = "#openId")
    public UserVO findUserByOpenId(String openId) {
        return sysUserMapper.selectUserVoByOpenId(openId);
    }

    @Override
    public Page selectWithRolePage(Query query, UserVO userVO) {
        DataScope dataScope = new DataScope();
        dataScope.setScopeName("deptId");
        dataScope.setIsOnly(true);
        dataScope.setDeptIds(getChildDepts(userVO));
        Object username = query.getCondition().get("username");
        Object tenantId = query.getCondition().get("tenantId");
        query.setRecords(sysUserMapper.selectUserVoPageDataScope(query, username, dataScope,tenantId));
        return query;
    }

    /**
     * 通过ID查询用户信息
     *
     * @param id 用户ID
     * @return 用户信息
     */
    @Override
    public UserVO selectUserVoById(Integer id) {
        return sysUserMapper.selectUserVoById(id);
    }

    @Override
    public Boolean findPhone(UserDTO userDto) {
       List<UserVO> userVOList = sysUserMapper.findPhone(userDto.getPhone());
       if(userVOList.size()==0){
           //手机号不重复
           return true;
       }else{
           /*修改用户信息时*/
           if(userDto.getUserId() != null){
               if(userVOList!=null && userVOList.get(0).getUserId() != userDto.getUserId()){
                   return false;
               }else {
                   return true;
               }
           }
           //手机号存在重复
           return false;
       }
    }

    /**
     * 保存用户验证码，和randomStr绑定
     *
     * @param randomStr 客户端生成
     * @param imageCode 验证码信息
     */
    @Override
    public void saveImageCode(String randomStr, String imageCode) {
        redisTemplate.opsForValue().set(SecurityConstants.DEFAULT_CODE_KEY + randomStr, imageCode, SecurityConstants.DEFAULT_IMAGE_EXPIRE, TimeUnit.SECONDS);
    }

    /**
     * 发送验证码
     * <p>
     * 1. 先去redis 查询是否 60S内已经发送
     * 2. 未发送： 判断手机号是否存 ? false :产生4位数字  手机号-验证码
     * 3. 发往消息中心-》发送信息
     * 4. 保存redis
     *
     * @param mobile 手机号
     * @return true、false
     */
    @Override
    public R<Boolean> sendSmsCode(String mobile) {
        Object tempCode = redisTemplate.opsForValue().get(SecurityConstants.DEFAULT_CODE_KEY + mobile);
        if (tempCode != null) {
            log.error("用户:{}验证码未失效{}", mobile, tempCode);
            return new R<>(false, "验证码未失效，请失效后再次申请");
        }

        SysUser params = new SysUser();
        params.setPhone(mobile);
        List<SysUser> userList = this.selectList(new EntityWrapper<>(params));

        if (CollectionUtil.isEmpty(userList)) {
            log.error("根据用户手机号{}查询用户为空", mobile);
            return new R<>(false, "手机号不存在");
        }

        String code = RandomUtil.randomNumbers(4);
        JSONObject contextJson = new JSONObject();
        contextJson.put("code", code);
        contextJson.put("product", "lc4Cloud");
        log.info("短信发送请求消息中心 -> 手机号:{} -> 验证码：{}", mobile, code);
        rabbitTemplate.convertAndSend(MqQueueConstant.MOBILE_CODE_QUEUE,
                new MobileMsgTemplate(
                        mobile,
                        contextJson.toJSONString(),
                        CommonConstant.ALIYUN_SMS,
                        EnumSmsChannelTemplate.LOGIN_NAME_LOGIN.getSignName(),
                        EnumSmsChannelTemplate.LOGIN_NAME_LOGIN.getTemplate()
                ));
        redisTemplate.opsForValue().set(SecurityConstants.DEFAULT_CODE_KEY + mobile, code, SecurityConstants.DEFAULT_IMAGE_EXPIRE, TimeUnit.SECONDS);
        return new R<>(true);
    }

    /**
     * 删除用户
     *
     * @param sysUser 用户
     * @return Boolean
     */
    @Override
    @CacheEvict(value = "user_details", key = "#sysUser.username")
    public Boolean deleteUserById(SysUser sysUser) {
        sysUserRoleService.deleteByUserId(sysUser.getUserId());
        this.deleteById(sysUser.getUserId());
        return Boolean.TRUE;
    }

    @Override
    @CacheEvict(value = "user_details", key = "#username")
    public R<Boolean> updateUserInfo(UserDTO userDto, String username) {
        UserVO userVo = this.findUserByUsername(username);
        SysUser sysUser = new SysUser();
        if (StrUtil.isNotBlank(userDto.getPassword())
                && StrUtil.isNotBlank(userDto.getNewpassword1())) {
            if (ENCODER.matches(userDto.getPassword(), userVo.getPassword())) {
                sysUser.setPassword(ENCODER.encode(userDto.getNewpassword1()));
            } else {
                log.warn("原密码错误，修改密码失败:{}", username);
                return new R<>(Boolean.FALSE, "原密码错误，修改失败");
            }
        }
        sysUser.setPhone(userDto.getPhone());
        sysUser.setUserId(userVo.getUserId());
        sysUser.setAvatar(userDto.getAvatar());
        return new R<>(this.updateById(sysUser));
    }

    @Override
    @CacheEvict(value = "user_details", key = "#username")
    public R<Boolean> updatePassword(UserVO userDTO, String username) {
        UserVO userVo = this.findUserByUsername(username);
        String newPassword = PswGenerator.getPswRandom(8);
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userVo.getUserId());
        sysUser.setPhone(userVo.getPhone());
        sysUser.setPassword(ENCODER.encode(newPassword));
        if(this.updateById(sysUser)){
            return new R<>(Boolean.TRUE, newPassword);
        }
        return new R<>(Boolean.FALSE);
    }

    @Override
    @CacheEvict(value = "user_details", key = "#username")
    public Boolean updateUser(UserDTO userDto, String username,Integer tenantId) {
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(userDto, sysUser);
        sysUser.setUpdateTime(new Date());
        this.updateById(sysUser);

        //更新用户角色表
        SysUserRole condition = new SysUserRole();
        condition.setUserId(userDto.getUserId());
        sysUserRoleService.delete(new EntityWrapper<>(condition));
        userDto.getRole().forEach(roleId -> {
            SysUserRole userRole = new SysUserRole();
            userRole.setUserId(sysUser.getUserId());
            userRole.setRoleId(roleId);
            userRole.setTenantId(tenantId);
            userRole.insert();
        });
        //更新用户岗位表
        SysUserPost userPost = new SysUserPost();
        userPost.setUserId(userDto.getUserId());
        sysUserPostService.delete(new EntityWrapper<>(userPost));
        SysUserPost sysUserPost = new SysUserPost();
        sysUserPost.setUserId(sysUser.getUserId());
        sysUserPost.setPostId(userDto.getPostId());
        sysUserPost.setTenantId(tenantId);
        sysUserPost.insert();

        return Boolean.TRUE;
    }

    /**
     * 获取当前用户的子部门信息
     *
     * @param userVO 用户信息
     * @return 子部门列表
     */
    private List<Integer> getChildDepts(UserVO userVO) {
        UserVO userVo = findUserByUsername(userVO.getUsername());
        Integer deptId = userVo.getDeptId();

        //获取当前部门的子部门
        SysDeptRelation deptRelation = new SysDeptRelation();
        deptRelation.setAncestor(deptId);
        List<SysDeptRelation> deptRelationList = sysDeptRelationService.selectList(new EntityWrapper<>(deptRelation));
        List<Integer> deptIds = new ArrayList<>();
        for (SysDeptRelation sysDeptRelation : deptRelationList) {
            deptIds.add(sysDeptRelation.getDescendant());
        }
        return deptIds;
    }

    @Override
    public void insertUserInfo(SysUser sysUser) {
        sysUserMapper.insertUserInfo(sysUser);
    }
}
