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
import com.zhkj.lc.common.web.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 字典表 前端控制器
 * </p>
 *
 * @author lengleng
 * @since 2017-11-19
 */
@Api("盘短订单-业务类型数据维护接口")
@RestController
@RequestMapping("/shortOrderDict")
public class ShortOrderTypeDictController extends BaseController {
    @Autowired
    private SysDictService sysDictService;

    /**
     * 分页查询字典信息
     *
     * @param params 分页对象
     * @return 分页对象
     */
    @ApiOperation("分页查询")
    @PostMapping("/dictPage")
    public Page dictPage(@RequestParam Map<String, Object> params) {
        params.put(CommonConstant.DEL_FLAG, CommonConstant.STATUS_NORMAL);
        params.put("tenant_id", getTenantId());
        params.put("type", "short_order_type");
        return sysDictService.selectPage(new Query<>(params), new EntityWrapper<>());
    }

    /**
     * 通过字典类型查找字典
     *
     * @param type 类型
     * @return 同类型字典
     */
    @ApiOperation("集合查询")
    @ApiImplicitParam(value = "type", name = "short_order_type")
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
     * 添加字典
     *
     * @param sysDict 字典信息
     * @return success、false
     */
    @ApiOperation("添加业务类型")
    @PostMapping
    @CacheEvict(value = "dict_details", key = "#sysDict.type")
    public R<Boolean> dict(@RequestBody SysDict sysDict) {
        //新增字典时添加租户id
         sysDict.setTenantId(getTenantId());
         sysDict.setType("short_order_type");
        return new R<>(sysDictService.insert(sysDict));
    }

    /**
     * 删除字典，并且清除字典缓存
     *
     * @param id   ID
     * @param type 类型
     * @return R
     */
    @ApiOperation("根据id和类型删除业务类型")
    @DeleteMapping("/{id}/{type}")
    @CacheEvict(value = "dict_details", key = "#type")
    public R<Boolean> deleteDict(@PathVariable Integer id, @PathVariable String type) {
        SysDict sysDict = new SysDict();
        sysDict.setTenantId(getTenantId());
        sysDict.setId(id);
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
    @ApiOperation("根据id修改业务类型")
    @PutMapping
    @CacheEvict(value = "dict_details", key = "#sysDict.type")
    public R<Boolean> editDict(@RequestBody SysDict sysDict) {
        return new R<>(sysDictService.updateById(sysDict));
    }
}
