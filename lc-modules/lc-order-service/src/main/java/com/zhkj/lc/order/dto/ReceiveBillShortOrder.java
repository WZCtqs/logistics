package com.zhkj.lc.order.dto;

import com.baomidou.mybatisplus.annotations.TableField;
import com.zhkj.lc.common.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: wchx
 * @Date: 2019/2/19 17:50
 */
@Data
public class ReceiveBillShortOrder {
    //订单编号
    @Excel(name = "订单编号")
    private String orderId;
    //订单编号数组
    @TableField(exist = false)
    private String[] orderIds;
    //订单类型
    @Excel(name = "订单类型")
    private String orderType;
    //结算日期(业务日期)
    @Excel(name = "业务日期")
    private Date orderDate;
    //客户
    @Excel(name = "客户")
    private String customer;
    //托运客户
    @Excel(name = "托运客户")
    private String customerName;
    //品名
    @Excel(name = "品名")
    private String goodsName;
    //空重箱
    @Excel(name = "空重箱")
    private String isHeavy;
    //业务种类
    @Excel(name = "业务种类")
    private String shortType;
    //路线
    @Excel(name = "路线")
    private String transLine;
    //车牌号
    @Excel(name = "车牌号")
    private String plateNumber;
    //短驳次数
    @Excel(name = "短驳次数")
    private int shortTransSum;
    //短驳费
    @Excel(name = "短驳费")
    private BigDecimal recPrice;
    //超时费
    @Excel(name = "超时费")
    private BigDecimal recOutTimeFee;
    //压车费
    @Excel(name = "压车费")
    private BigDecimal recParkingFee;
    //其他费用
    @Excel(name = "其他费用")
    private BigDecimal recOtherFee;
    //订单费用
    @Excel(name = "订单费用")
    private BigDecimal orderFee;
    //结算状态
    @Excel(name = "结算状态")
    private String balanceStatus;
    //利率
    @Excel(name = "利率")
    private BigDecimal rate;
    //总计费用（含发票）
    @Excel(name = "总计费用（含发票）")
    private BigDecimal totalFee;
    //备注
    @Excel(name = "备注")
    private String remark	;

    private Date updateTime;
}
