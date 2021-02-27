package com.zhkj.lc.admin.service;


import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.zhkj.lc.admin.model.entity.SysTanent;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.vo.TanentVo;

import java.util.List;

/**
 * <p>
 * 系统租户信息表 服务类
 * </p>
 *
 * @author cb
 * @since 2018-12-13
 */
public interface SysTanentService extends IService<SysTanent> {

    void insertTanentInfo(SysTanent sysTanent);

    Page selectTanentVoPage(Query query, TanentVo tanentVo);

    SysTanent selectTanentInfo(Integer tenantId);

    List<TanentVo> selectTanentList();

    TanentVo selectByWA(String weixinId);

    Boolean findPhone(String phone);

    Boolean fingByPhone(String phone, Integer tenantId);

    String selectTenantShortName(Integer tenantId);
}
