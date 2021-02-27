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


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.zhkj.lc.admin.model.entity.SysDict;
import com.zhkj.lc.admin.service.SysDictService;
import com.zhkj.lc.common.constant.CommonConstant;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.util.R;
import com.zhkj.lc.common.util.UserUtils;
import com.zhkj.lc.common.vo.SysDictVO;
import com.zhkj.lc.common.web.BaseController;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 字典表 前端控制器
 * </p>
 *
 * @author lengleng
 * @since 2017-11-19
 */
@AllArgsConstructor
@RestController
@RequestMapping("/dict")
public class DictController extends BaseController {
    @Autowired
    private SysDictService sysDictService;

    private final RedisTemplate redisTemplate;

    /**
     * 通过ID查询字典信息
     *
     * @param id ID
     * @return 字典信息
     */
    @GetMapping("/{id}")
    public SysDict dict(@PathVariable Integer id) {
        return sysDictService.selectById(id);
    }

    /**
     *
     * 功能描述:
     *
     * @param sysDict 字典参数（value值和类型，或者value值和lable）
     * @return 字典单实例
     * @auther wzc
     * @date 2018/12/17 17:17
     */
    @PostMapping("/selectDictByValueAndType")
    public SysDictVO selectDict(@RequestBody SysDictVO sysDict){
        SysDictVO sd = sysDictService.selectDictByValueAndType(sysDict);
        return sd;
    }


    /**
     * 分页查询字典信息
     *
     * @param params 分页对象
     * @return 分页对象
     */
    @RequestMapping("/dictPage")
    public Page dictPage(@RequestParam Map<String, Object> params) {
        params.put(CommonConstant.DEL_FLAG, CommonConstant.STATUS_NORMAL);
        String username = UserUtils.getUser();
        params.put("tenant_id", getTenantId());
        return sysDictService.selectPage(new Query<>(params), new EntityWrapper<>());
    }

    /**
     * 通过字典类型查找字典
     *
     * @param type 类型
     * @return 同类型字典
     */
    @GetMapping("/type/{type}")
    @Cacheable(value = "dict_details", key = "#type")
    public List<SysDict> findDictByType(@PathVariable String type) {
        SysDict condition = new SysDict();
        condition.setDelFlag(CommonConstant.STATUS_NORMAL);
        condition.setType(type);
        condition.setTenantId(getTenantId());
        return sysDictService.selectList(new EntityWrapper<>(condition));
    }

    /**
     * 通过字典类型查找字典
     *
     * @param type 类型
     * @return 同类型字典
     */
    @GetMapping("/fegintype/{type}/{tenantId}")
//    @Cacheable(value = "dict_details", key = "#type")
    public List<SysDict> findDictByTypeForFegin(@PathVariable String type, @PathVariable Integer tenantId) {
        List<SysDict> list = new ArrayList<>();
        Object object = redisTemplate.opsForValue().get("DICT_TYPE_"+type+"_TENANTID_"+tenantId);
        if(object != null){
            list = (List<SysDict>) object;
        }else {
            SysDict condition = new SysDict();
            condition.setDelFlag(CommonConstant.STATUS_NORMAL);
            condition.setType(type);
            condition.setTenantId(tenantId);
            list = sysDictService.selectList(new EntityWrapper<>(condition));
            redisTemplate.opsForValue().set("DICT_TYPE_"+type+"_TENANTID_"+tenantId, list, 2, TimeUnit.HOURS);
        }
        return list;
    }

    /**
     * 小程序端根据字典类型和租户id查找字典
     * @param type
     * @param tenantId
     * @return
     */
    @GetMapping("/wechat/type")
    //@Cacheable(value = "dictWechat_details", key = "#type")
    public List<SysDict> wechatSelectDictByType(@RequestParam("type") String type,@RequestParam("tenantId")Integer tenantId) {
        SysDict condition = new SysDict();
        condition.setDelFlag(CommonConstant.STATUS_NORMAL);
        condition.setType(type);
        condition.setTenantId(tenantId);
        return sysDictService.selectList(new EntityWrapper<>(condition));
    }

    /**
     * 添加字典
     *
     * @param sysDict 字典信息
     * @return success、false
     */
    @PostMapping
    @CacheEvict(value = "dict_details", key = "#sysDict.type")
    public R<Boolean> dict(@RequestBody SysDict sysDict) {
        //新增字典时添加租户id
         sysDict.setTenantId(getTenantId());
         String username = UserUtils.getUser();
        return new R<>(sysDictService.insert(sysDict));
    }

    /**
     * 删除字典，并且清除字典缓存
     *
     * @param id   ID
     * @param type 类型
     * @return R
     */
    @DeleteMapping("/{id}/{type}")
    @CacheEvict(value = "dict_details", key = "#type")
    public R<Boolean> deleteDict(@PathVariable Integer id, @PathVariable String type) {
        SysDict sysDict = new SysDict();
        sysDict.setTenantId(getTenantId());
        sysDict.setDelFlag(CommonConstant.STATUS_DEL);
        sysDict.setUpdateTime(new Date());
        return new R<>(sysDictService.updateById(sysDict));
    }

    /**
     * 修改字典
     *
     * @param sysDict 字典信息
     * @return success/false
     */
    @PutMapping
    @CacheEvict(value = "dict_details", key = "#sysDict.type")
    public R<Boolean> editDict(@RequestBody SysDict sysDict) {
        return new R<>(sysDictService.updateById(sysDict));
    }
}
