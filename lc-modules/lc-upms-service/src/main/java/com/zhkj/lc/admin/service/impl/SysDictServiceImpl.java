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

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zhkj.lc.admin.mapper.SysDictMapper;
import com.zhkj.lc.admin.model.entity.SysDict;
import com.zhkj.lc.admin.service.SysDictService;
import com.zhkj.lc.common.vo.SysDictVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 字典表 服务实现类
 * </p>
 *
 * @author lengleng
 * @since 2017-11-19
 */
@Service
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements SysDictService {

    @Autowired
    SysDictMapper sysDictMapper;

    @Override
    public SysDictVO selectDictByValueAndType(SysDictVO sysDict) {
        return sysDictMapper.selectDictByValueAndType(sysDict);
    }

    @Override
    public List<SysDict> selectSystemDict() {
        return sysDictMapper.selectSystemDictData();
    }
}
