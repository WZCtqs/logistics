package com.zhkj.lc.order.dto;

import com.zhkj.lc.common.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 财务应收应付查询参数实体类
 */
@Data
@ApiModel(value = "财务应收应收应付查询参数实体类")
public class FinanceQueryDTO {

    /**
     * 订单类型
     */
    @ApiModelProperty(value = "订单类型")
    private String orderType;

    /**
     * 订单编号
     */
    @ApiModelProperty(value = "订单编号")
    private String orderId;

    /**
     * 订单编号数组
     */
    @ApiModelProperty(value = "订单编号数组")
    private String[] orderIds;
    /**
     * 车牌号
     */
    @ApiModelProperty(value = "车牌号")
    private String plateNumber;

    @ApiModelProperty(value = "车辆类型")
    private String truckAttribute;
    /**
     * 班列开始日期
     */
    @ApiModelProperty(value = "班列开始日期")
    private String blStartTime;
    /**
     * 班列结束日期
     */
    @ApiModelProperty(value = "班列结束日期")
    private String blEndTime;
    /**
     * 订单下单开始日期
     */
    @ApiModelProperty(value = "订单下单开始日期")
    private String ordStartTime;
    /**
     * 订单下单结束日期
     */
    @ApiModelProperty(value = "订单下单结束日期")
    private String ordEndTime;

    /**
     * 开始日期
     */
    @ApiModelProperty(value = "开始日期")
    private String startTime;
    /**
     * 截止日期
     */
    @ApiModelProperty(value = "截止日期")
    private String endTime;

    /**
     * 租户id
     */
    private Integer tenantId;

    /**
     * 客户id
     */
    @ApiModelProperty(value = "客户id")
    private Integer customerId;

    /**
     * 结算方式
     */
    @ApiModelProperty(value = "结算方式 0：单结 1”月结")
    private String settlement;

    /**
     * 应收结算状态
     */
    @ApiModelProperty(value = "结算状态（0:未发送，1：已发送，2：司机已确认 3：司机已反馈 4：未结算 5：已结算）")
    private String balanceStatus;
    /**
     * 司机id
     */
    @ApiModelProperty(value = "司机id")
    private Integer driverId;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private String createTime;

    /**
     * 对账单ids
     */
    @ApiModelProperty(value = "对账单ids")
    private String ids;

    private String[] idsArray;

    private String accountPayId;

    private String settlementStatus;

    private String[] statusArray;

    private String  ifAddToYfbill;

    private String pickGoodsPlace;
    //车主
    private String truckOwnId;

    //车主
    private String truckownName;



    private String pickCNPlace;//提箱地

    private String returnCNPlace;//换箱地

    private String type;//去回程

    private String classOrder;//舱位号

    private String containerNo;//箱号

    private Date classDate;//班列日期

    private BigDecimal rate;

    /*车牌号集合*/
    private String[] plates;

    /*铅封号查询*/
    private String sealNumber;

    /*现金结算状态查询*/
    private Integer cashStatus;

    /*运输油卡状态查询*/
    private Integer oilStatus;
}

