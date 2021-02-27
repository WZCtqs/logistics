package com.zhkj.lc.order.dto;

import lombok.Data;

/**
 * @Auther: HP
 * @Date: 2019/3/15 15:32
 * @Description:
 */
@Data
public class UpstreamOrderDriver {

    private String upstreamId;//上游订单id
    private String carrier;//承运商
    private Integer tenantId;//租户id
    private String rsoPlateNum;//车牌号
    private String rsoTownerName;//司机姓名
    private String rsoTownerPhone;//司机手机号
    private String rsoTruckType;//车辆类型
    private String rsoContainerNum;//箱号
    private String rsoSealNum;//铅封号
}
