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

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zhkj.lc.admin.common.util.TreeUtil;
import com.zhkj.lc.admin.mapper.SysDeptMapper;
import com.zhkj.lc.admin.mapper.SysDeptRelationMapper;
import com.zhkj.lc.admin.model.dto.DeptTree;
import com.zhkj.lc.admin.model.entity.SysDept;
import com.zhkj.lc.admin.model.entity.SysDeptRelation;
import com.zhkj.lc.admin.service.SysDeptService;
import com.zhkj.lc.common.constant.CommonConstant;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 部门管理 服务实现类
 * </p>
 *
 * @author lengleng
 * @since 2018-01-20
 */
@Service
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements SysDeptService {
    @Autowired
    private SysDeptMapper sysDeptMapper;
    @Autowired
    private SysDeptRelationMapper sysDeptRelationMapper;

    /**
     * 添加信息部门
     *
     * @param dept 部门
     * @return
     */
    @Override
    public Boolean insertDept(SysDept dept,Integer tenantId) {
        SysDept sysDept = new SysDept();
        BeanUtils.copyProperties(dept, sysDept);
        sysDept.setTenantId(tenantId);
        //改为能返回自增id的insert
       // sysDeptMapper.insertDeptInfo(sysDept);
        this.insert(sysDept);
        this.insertDeptRelation(sysDept);
        return Boolean.TRUE;
    }

    /**
     * 维护部门关系
     * @param sysDept 部门
     */
    @Override
    public void insertDeptRelation(SysDept sysDept) {
        //增加部门关系表
        SysDeptRelation deptRelation = new SysDeptRelation();
        deptRelation.setDescendant(sysDept.getParentId());
        List<SysDeptRelation> deptRelationList = sysDeptRelationMapper.selectList(new EntityWrapper<>(deptRelation));
        for (SysDeptRelation sysDeptRelation : deptRelationList) {
            sysDeptRelation.setDescendant(sysDept.getDeptId());
            sysDeptRelationMapper.insert(sysDeptRelation);
        }
        //自己也要维护到关系表中
        SysDeptRelation own = new SysDeptRelation();
        own.setDescendant(sysDept.getDeptId());
        own.setAncestor(sysDept.getDeptId());
        own.setTenantId(sysDept.getTenantId());

        sysDeptRelationMapper.insert(own);
    }

    /**
     * 供租户管理新增租户时调用
     * @param sysDept
     */

    @Override
    public void insertDeptInfo(SysDept sysDept) {
        sysDeptMapper.insertDeptInfo(sysDept);
    }

    /**
     * 删除部门
     *
     * @param id 部门 ID
     * @return 成功、失败
     */
    @Override
    public Boolean deleteDeptById(Integer id) {
        SysDept sysDept = new SysDept();
        sysDept.setDeptId(id);
        sysDept.setUpdateTime(new Date());
        sysDept.setDelFlag(CommonConstant.STATUS_DEL);
        this.deleteById(sysDept);
        sysDeptRelationMapper.deleteAllDeptRealtion(id);
        return Boolean.TRUE;
    }

    /**
     * 更新部门
     *
     * @param sysDept 部门信息
     * @return 成功、失败
     */
    @Override
    public Boolean updateDeptById(SysDept sysDept) {
        //更新部门状态
        this.updateById(sysDept);
        //更新部门关系
        SysDeptRelation relation = new SysDeptRelation();
        relation.setAncestor(sysDept.getParentId());
        relation.setDescendant(sysDept.getDeptId());
        sysDeptRelationMapper.updateDeptRealtion(relation);
        return null;
    }

    /**
     * 查询部门树
     *
     * @param sysDeptEntityWrapper
     * @return 树
     */
    @Override
    public List<DeptTree> selectListTree(EntityWrapper<SysDept> sysDeptEntityWrapper) {
        return getDeptTree(this.selectList(sysDeptEntityWrapper), 0);
    }

    /**
     * 构建部门树
     *
     * @param depts 部门
     * @param root  根节点
     * @return
     */
    private List<DeptTree> getDeptTree(List<SysDept> depts, int root) {
        List<DeptTree> trees = new ArrayList<>();
        DeptTree node;
        for (SysDept dept : depts) {
            if (dept.getParentId().equals(dept.getDeptId())) {
                continue;
            }
            node = new DeptTree();
            node.setId(dept.getDeptId());
            node.setParentId(dept.getParentId());
            node.setName(dept.getName());
            trees.add(node);
        }
        return TreeUtil.bulid(trees, root);
    }
}
