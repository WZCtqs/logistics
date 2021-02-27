package com.zhkj.lc.order.model.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: HP
 * @Date: 2019/5/8 14:23
 * @Description:
 */
@Data
@TableName("order_settlement")
public class OrderSettlement extends Model<OrderSettlement> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 订单编号
     */
    @ApiModelProperty(value = "订单编号/对账单编号")
    @TableField("order_id")
    private String orderId;
    /**
     * 结算类型
     */
    @ApiModelProperty(value = "结算类型：oil 油卡，cash 现金")
    @TableField("settlement_type")
    private String settlementType;
    /**
     * 结算时间
     */
    @ApiModelProperty(value = "结算时间")
    @TableField("settlement_time")
    private Date settlementTime;
    /**
     * 操作员
     */
    @ApiModelProperty(value = "操作员")
    @TableField("create_by")
    private String createBy;

    @ApiModelProperty(value = "现金结算状态")
    @TableField("cash_status")
    private Integer cashStatus;

    @ApiModelProperty(value = "现金结算人员")
    @TableField("cash_by")
    private String cashBy;

    @ApiModelProperty(value = "现金结算时间")
    @TableField("cash_time")
    private Date cashTime;

    @ApiModelProperty(value = "油卡结算状态")
    @TableField("oil_status")
    private Integer oilStatus;

    @ApiModelProperty(value = "油卡结算人员")
    @TableField("oil_by")
    private String oilBy;

    @ApiModelProperty(value = "油卡结算时间")
    @TableField("oil_time")
    private Date oilTime;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}
