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

package com.zhkj.lc.admin.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.IService;
import com.zhkj.lc.admin.model.dto.DeptTree;
import com.zhkj.lc.admin.model.entity.SysDept;

import java.util.List;

/**
 * <p>
 * 部门管理 服务类
 * </p>
 *
 * @author lengleng
 * @since 2018-01-20
 */
public interface SysDeptService extends IService<SysDept> {

    /**
     * 查询部门树菜单
     * @param sysDeptEntityWrapper
     * @return 树
     */
    List<DeptTree> selectListTree(EntityWrapper<SysDept> sysDeptEntityWrapper);

    /**
     * 添加信息部门
     * @param sysDept
     * @return
     */
    Boolean insertDept(SysDept sysDept,Integer tenantId);

    //供租户管理模块调用更新deptrelation的方法
    void insertDeptRelation(SysDept sysDept);

    //供租户管理调用
    void insertDeptInfo(SysDept sysDept);
    /**
     * 删除部门
     * @param id 部门 ID
     * @return 成功、失败
     */
    Boolean deleteDeptById(Integer id);

    /**
     * 更新部门
     * @param sysDept 部门信息
     * @return 成功、失败
     */
    Boolean updateDeptById(SysDept sysDept);
}
