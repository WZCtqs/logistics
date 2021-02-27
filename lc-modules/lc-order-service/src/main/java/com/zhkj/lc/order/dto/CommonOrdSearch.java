package com.zhkj.lc.order.dto;


import lombok.Data;

/**
 * 普货订单管理Bean
 */
@Data
public class CommonOrdSearch {

    private String orderId;

    private String status;

    private Integer customerId;

    private Integer[] customerIds;

    /*派车日期开始*/
    private String fromDate;
    /*派车日期结束*/
    private String toDate;

    private Integer tenantId;

    /*订单状态数组*/
    private String[] statusArray;

    /*接单时间*/
    private String driverReceiptTime;

    /*司机提货时间*/
    private String sendGoodsTime;

    /*签收码确认时间*/
    private String receivedTime;

    /*发货人手机号*/
    private String shipperPhone;

    /*收货人手机号*/
    private String pickerPhone;

    /*微信公众号openid*/
    private String gopenId;
    /*客户名称*/
    private String customerName;

    /**创建者*/
    private String createBy;
}
