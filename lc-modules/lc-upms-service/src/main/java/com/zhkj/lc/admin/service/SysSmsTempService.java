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

import com.baomidou.mybatisplus.service.IService;
import com.zhkj.lc.admin.model.dto.SmsDTO;
import com.zhkj.lc.admin.model.entity.SysDict;
import com.zhkj.lc.admin.model.entity.SysSmsTemp;
import com.zhkj.lc.common.vo.SysDictVO;

import java.util.List;

/**
 * <p>
 * 短信设置 服务类
 * </p>
 *
 * @author wzc
 * @since 2019-02-21
 */
public interface SysSmsTempService extends IService<SysSmsTemp> {

    List<SysSmsTemp> selectSmsList(Integer tenantId);

    List<SysSmsTemp> selectForTenant();

    boolean edit(SmsDTO smsDTO);

    boolean selectIsSend(Integer tenantId);

    SysSmsTemp selectSmsSetByTplId(Integer tenantId, Integer tpl_id);
}
