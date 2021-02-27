package com.zhkj.lc.order.dto;

import lombok.Data;

/**
 * <p>
 * 更新重去重回订单
 * </p>
 *
 * @author shy
 * @since 2018-12-07
 */
@Data
public class OrdOrderDTO{

    /**
     * 上游系统订单编号
     */
    private String upstreamId;
    /**
     * 租户id
     */
    private Integer tenantId;
    /**
     * 车牌号
     */
    private String plateNumber;
    /**
     * 是否重去重回
     */
    private String upstreamIsZqzh;
    /**
     * 上游重去重回舱位号
     */
    private String upstreamZqzhClassOrder;

}
