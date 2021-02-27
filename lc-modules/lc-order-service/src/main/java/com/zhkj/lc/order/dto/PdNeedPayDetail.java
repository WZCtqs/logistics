package com.zhkj.lc.order.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 盘短订单应付账单
 */
@Data
public class PdNeedPayDetail {
    /**
     * 车队名称
     */
    private String carrier;


    /**
     * 车队id
     */
    private Integer truckTeamId;
    /**
     * 车牌号
     */
    private String plateNumber;
    /**
     * 订单号
     */
    private String orderId;

    /**
     * 订单类型
     */
    private String orderType;

    /**
     * 业务日期
     */
    private Date orderDate;

    /**
     * 托运客户
     */
    private String customer;

    /**
     * 品名
     */

    private String goodsName;
    /**
     * 空重箱
     */
    private String isHeavy;

    /**
     * 业务种类
     */
    private String shortType;

    /**
     * 路线
     */
    private String transLine;

    /**
     * 短驳次数
     */

    private int shortTransSum;
    /**
     * 短驳费
     */

    /**
     * 超时费
     */
    private BigDecimal payOutTimeFee;

    /**
     * 其他费用
     */

    private BigDecimal payOtherFee;
    /**
     * 油卡押金费
     */
    private BigDecimal oilPledge;

    /**
     * 利率
     */
    private BigDecimal moneyRate;


    /**
     * 应付总额
     */
    private BigDecimal needPay;

    /**
     * 备注
     */

    private String remarks;

    /**
     * 运费油卡
     */
    private BigDecimal transOilFee;

    /**
     * 结算状态（0:未发送，1：已发送，2：司机已确认 3：司机已反馈 4：未结算 5：已结算）
     */
    private String needPayStatus;

    private Date updateTime;


}
