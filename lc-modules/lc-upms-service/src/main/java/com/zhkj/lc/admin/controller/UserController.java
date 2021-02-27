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

package com.zhkj.lc.admin.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.netflix.discovery.converters.Auto;
import com.zhkj.lc.admin.mapper.SysUserMapper;
import com.zhkj.lc.admin.model.dto.UserDTO;
import com.zhkj.lc.admin.model.dto.UserInfo;
import com.zhkj.lc.admin.model.entity.SysUser;
import com.zhkj.lc.admin.model.entity.SysUserPost;
import com.zhkj.lc.admin.model.entity.SysUserRole;
import com.zhkj.lc.admin.service.SysTanentService;
import com.zhkj.lc.admin.service.SysUserService;
import com.zhkj.lc.common.api.FileUtils;
import com.zhkj.lc.common.bean.config.FdfsPropertiesConfig;
import com.zhkj.lc.common.config.Global;
import com.zhkj.lc.common.constant.CommonConstant;
import com.zhkj.lc.common.util.PDFUtils;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.util.R;
import com.zhkj.lc.common.vo.UserVO;
import com.zhkj.lc.common.web.BaseController;
import com.luhuiguo.fastdfs.domain.StorePath;
import com.luhuiguo.fastdfs.service.FastFileStorageClient;
import com.xiaoleilu.hutool.io.FileUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author lengleng
 * @date 2017/10/28
 */
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {
    private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();
    @Autowired
    private FastFileStorageClient fastFileStorageClient;
    @Autowired
    private SysUserService userService;
    @Autowired
    private FdfsPropertiesConfig fdfsPropertiesConfig;
    @Autowired
    private SysTanentService sysTanentService;

    @Value("${uploadfile.ippath}")
    public String url;

    /**
     * 获取当前用户信息（角色、权限）
     * 并且异步初始化用户部门信息
     *
     * @param userVo 当前用户信息
     * @return 用户名
     */
    @ApiOperation(value = "获取当前用户信息", notes = "用户信息")
    @GetMapping("/info")
    public R<UserInfo> user(UserVO userVo) {
        Integer tenantId = getTenantId();
        UserInfo userInfo = userService.findUserInfo(userVo,tenantId);
        String tenantShortName = sysTanentService.selectTenantShortName(tenantId);
        userInfo.setTenantShortName(tenantShortName);
        return new R<>(userInfo);
    }


    /**
     * 通过ID查询当前用户信息
     *
     * @param id ID
     * @return 用户信息
     */
    @ApiOperation(value = "查询用户", notes = "根据ID查询用户")
    @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "int", paramType = "path")
    @GetMapping("/{id}")
    public UserVO user(@PathVariable Integer id) {
        return userService.selectUserVoById(id);
    }

    /**
     * 删除用户信息
     *
     * @param id ID
     * @return R
     */
    @ApiOperation(value = "删除用户", notes = "根据ID删除用户")
    @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "int", paramType = "path")
    @DeleteMapping("/{id}")
    public R<Boolean> userDel(@PathVariable Integer id) {
        SysUser sysUser = userService.selectById(id);
        return new R<>(userService.deleteUserById(sysUser));
    }

    /**
     * 添加用户
     *
     * @param userDto 用户信息
     * @return success/false
     */
    @Transactional
    @PostMapping
    public R<Boolean> user(@RequestBody UserDTO userDto) {
        SysUser sysUser = new SysUser();
        //校验手机号是否存在重复，重复返给前端提示信息
        Boolean f = userService.findPhone(userDto);
        if(!f){
            return new R<>(Boolean.FALSE,"手机号已被注册！");
        }
        UserVO userVO = userService.findUserByUsername(userDto.getUsername());
        if(userVO != null){
            return new R<>(Boolean.FALSE,"该账号已存在！");
        }
        BeanUtils.copyProperties(userDto, sysUser);
        sysUser.setTenantId(getTenantId());
        sysUser.setDelFlag(CommonConstant.STATUS_NORMAL);
        sysUser.setStatus(CommonConstant.TANENT_NORMAL);
        sysUser.setPassword(ENCODER.encode(userDto.getNewpassword1()));
        userService.insert(sysUser);

        //更新用户角色表
        userDto.getRole().forEach(roleId -> {
            SysUserRole userRole = new SysUserRole();
            userRole.setUserId(sysUser.getUserId());
            userRole.setRoleId(roleId);
            userRole.setTenantId(sysUser.getTenantId());
            userRole.insert();
        });

        //更新用户岗位表
        SysUserPost sysUserPost = new SysUserPost();
        sysUserPost.setUserId(sysUser.getUserId());
        sysUserPost.setPostId(userDto.getPostId());
        sysUserPost.setTenantId(sysUser.getTenantId());
        sysUserPost.insert();
        return new R<>(Boolean.TRUE);
    }

    /**
     * 更新用户信息
     *
     * @param userDto 用户信息
     * @return R
     */
    @PutMapping
    public R<Boolean> userUpdate(@RequestBody UserDTO userDto) {
        boolean f = userService.findPhone(userDto);
        if(f) {
            SysUser user = userService.selectById(userDto.getUserId());
            return new R<>(userService.updateUser(userDto, user.getUsername(), getTenantId()));
        }else {

        }return new R<>(Boolean.FALSE,"手机号已被注册！");
    }

    /**
     * 通过用户名查询用户及其角色信息
     *
     * @param username 用户名
     * @return UseVo 对象
     */
    //需改为手机号为用户名登陆
    @GetMapping("/findUserByUsername/{username}")
    public UserVO findUserByUsername(@PathVariable String username) {
        return userService.findUserByUsername(username);
    }

    /**
     * 通过手机号查询用户及其角色信息
     *
     * @param mobile 手机号
     * @return UseVo 对象
     */
    @GetMapping("/findUserByMobile/{mobile}")
    public UserVO findUserByMobile(@PathVariable String mobile) {
        return userService.findUserByMobile(mobile);
    }

    /**
     * 通过OpenId查询
     *
     * @param openId openid
     * @return 对象
     */
    @GetMapping("/findUserByOpenId/{openId}")
    public UserVO findUserByOpenId(@PathVariable String openId) {
        return userService.findUserByOpenId(openId);
    }

    /**
     * 分页查询用户
     *
     * @param params 参数集
     * @param userVO 用户信息
     * @return 用户集合
     */
    @RequestMapping("/userPage")
    public Page userPage(@RequestParam Map<String, Object> params,UserVO userVO) {
        //加入租户id的过滤
        params.put("tenantId", getTenantId());
        return userService.selectWithRolePage(new Query(params),userVO);
    }

    /**
     * 上传用户头像
     * (多机部署有问题，建议使用独立的文件服务器)
     *
     * @param file 资源
     * @return filename map
     */
    @PostMapping("/upload")
    public Map<String, String> upload(@RequestParam("file")MultipartFile file) {
        if(file != null) {
            System.out.println(file.getOriginalFilename());
            String fileExt = FileUtil.extName(file.getOriginalFilename());
            Map<String, String> resultMap = new HashMap<>(1);
            try {
                StorePath storePath = fastFileStorageClient.uploadFile(file.getBytes(), fileExt);
                resultMap.put("filename", fdfsPropertiesConfig.getFileHost() + storePath.getFullPath());
                return resultMap;
            } catch (IOException e) {
                logger.error("文件上传异常", e);
                throw new RuntimeException(e);
            }
        }else {
            return null;
        }
    }

    @PostMapping("/localUpload")
    public Map<String, String> localUpload(@RequestParam("file") MultipartFile file) {
        Map<String, String> resultMap = new HashMap<>();
        if (file.isEmpty()) {
            return null;
        }
        try {
            PDFUtils.createDirectory(Global.FILE_PATH_SYSTEM);
            String filename = UUID.randomUUID().toString().replace("-","") + PDFUtils.getSuffix(file.getOriginalFilename());
            byte[] bytes = file.getBytes();
            Path path = Paths.get(Global.FILE_PATH_SYSTEM + filename);
            Files.write(path, bytes);
            resultMap.put("filename",url + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultMap;
    }
    // 查看本地图片
    @GetMapping("/viewFile")
    public void getImage(
            @RequestParam("imageName") String imageName,
            HttpServletResponse response) {
        try {
            response.setContentType("image/jpg");
            StreamUtils.copy(new FileInputStream(Global.FILE_PATH_SYSTEM + imageName), response.getOutputStream());
        } catch (IOException e) {
            logger.error("本地图片查看失败：" + e.getMessage());
        }
    }
    /*@PostMapping("/upload")
    @ApiOperation(value = "上传用户头像")
    public R<String> uploadImg(@RequestParam("userImg")MultipartFile userImg){
        try {
            String path = FileUtils.saveFile(userImg);
            return new R<>(path);
        }catch (IOException e){
            logger.info("上传用户头像失败！");
            return new R<>("error");
        }
    }*/
    /**
     * 修改个人信息
     *
     * @param userDto userDto
     * @param userVo  登录用户信息
     * @return success/false
     */
    @PutMapping("/editInfo")
    public R<Boolean> editInfo(@RequestBody UserDTO userDto, UserVO userVo) {
        return userService.updateUserInfo(userDto, userVo.getUsername());
    }

    @ApiOperation("重置密码")
    @PutMapping("/updatePassword")
    public R<Boolean> updatePassword(@RequestBody UserVO userDTO){
        return userService.updatePassword(userDTO, userDTO.getUsername());
    }

}
