package com.zhkj.lc.order.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.order.model.entity.OrdExConDTO;
import com.zhkj.lc.order.model.entity.OrdExceptionCondition;
import com.baomidou.mybatisplus.service.IService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author cb
 * @since 2019-01-11
 */
public interface IOrdExceptionConditionService extends IService<OrdExceptionCondition> {


    Page selectExConditionPage(Query objectQuery, OrdExConDTO ordExConDTO);

    Boolean exportCondition(HttpServletRequest request, HttpServletResponse response, String ids, Integer tenantId);

    Boolean deleteByIds(String ids);

    Page selectExPageByOrderId(Query query,String orderId, Integer tenantId);

    OrdExceptionCondition selectExConditionById(Integer id, Integer tenantId);
}
