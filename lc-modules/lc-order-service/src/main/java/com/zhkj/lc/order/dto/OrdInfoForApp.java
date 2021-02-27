package com.zhkj.lc.order.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class OrdInfoForApp {
    //订单编号
    private String orderId;

    //订单类型
    private String orderType;

    //货物信息
    private List<String> goodsInfo;

    //服务方式
    private String pickGoodsWay;

    //发货时间
    private Date sendGoodsDate;

}
