package com.zhkj.lc.trunk.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author cb
 * @since 2019-02-11
 */
@TableName("tru_truck_fee")
@Data
@ApiModel(value = "车辆费用管理")
public class TruTruckFee extends Model<TruTruckFee> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 费用月份
     */
    @ApiModelProperty(value = "费用的月份")
    @TableField("fee_month")
    @JsonFormat(pattern="yyyy-MM")
    private Date feeMonth;
    /**
     * 车牌号
     */
    @ApiModelProperty(value = "车辆id")
    @TableField("truck_id")
    private Integer truckId;
    /**
     * 二维费
     */
    @ApiModelProperty(value = "二维费")
    @TableField("qrcode_fee")
    private BigDecimal qrcodeFee;
    /**
     * 租车费
     */
    @ApiModelProperty(value = "租车费")
    @TableField("rent_fee")
    private BigDecimal rentFee;
    /**
     * 审车费
     */
    @ApiModelProperty(value = "审车费")
    @TableField("check_fee")
    private BigDecimal checkFee;
    /**
     * 保险费
     */
    @ApiModelProperty(value = "保险费")
    @TableField("insurance_fee")
    private BigDecimal insuranceFee;
    /**
     * 删除标志
     */
    @TableField("del_flag")
    private String delFlag;
    @TableField("create_time")
    private Date createTime;
    @TableField("create_by")
    private String createBy;
    @TableField("update_time")
    private Date updateTime;
    @TableField("update_by")
    private String updateBy;

    @TableField("tenant_id")
    private Integer tenantId;



    /**
     * 车主
     */
    @ApiModelProperty(value = "车主")
    @TableField(exist = false)
    private String truckOwner;

    /**
     * 车主手机号
     */
    @ApiModelProperty(value = "车主手机号")
    @TableField(exist = false)
    private String truckOwnerPhone;

    @ApiModelProperty(value = "车牌号")
    @TableField(exist = false)
    private String plateNumber;

    @ApiModelProperty(value = "司机id")
    @TableField(exist = false)
    private String driverId;
    @ApiModelProperty(value = "司机名")
    @TableField(exist = false)
    private String driverName;
    @ApiModelProperty(value = "司机手机号")
    @TableField(exist = false)
    private String phone;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "TruTruckFee{" +
        ", id=" + id +
        ", feeMonth=" + feeMonth +
        ", qrcodeFee=" + qrcodeFee +
        ", rentFee=" + rentFee +
        ", checkFee=" + checkFee +
        ", insuranceFee=" + insuranceFee +
        ", delFlag=" + delFlag +
        ", createTime=" + createTime +
        ", createBy=" + createBy +
        ", updateTime=" + updateTime +
        ", updateBy=" + updateBy +
        "}";
    }
}
