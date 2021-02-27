package com.zhkj.lc.order.dto;

import com.zhkj.lc.order.model.entity.OrdOrderLogistics;
import lombok.Data;

/**
 * @Auther: HP
 * @Date: 2019/3/11 14:57
 * @Description:
 */
@Data
public class OrderForGPSSystem {

    private String destination;//目的地

    private String transitstate;//运输状态

    private String orderId; // 物流系统订单编号

    private String ordernum;//订单号

    private String proxynum;//委托书编号

    private String boxnum;//箱号

    private int gocome;//去回程/(0去1回)

    private String goodname;//货物名字

    private String boxtype;//箱型箱量

    private String getordertime;//接单时间

    private String getboxtime;//提箱/提货完成时间

    private String arrivetime;//到达装卸货地时间

    private String getgoodtime;//提卸货完成时间

    private String destinationtime;//到达陆港/还箱完成时间

    private OrdOrderLogistics logistics;

}
