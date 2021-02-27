package com.zhkj.lc.order.model.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: HP
 * @Date: 2019/5/16 09:05
 * @Description:
 */
@Data
@TableName("ord_pickup_arrival_add")
public class    OrdPickupArrivalAdd extends Model<OrdPickupArrivalAdd> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 订单编号
     */
    @ApiModelProperty("订单编号")
    @TableField("order_id")
    private String orderId;

        /**
     * 地址类型 0提货地址，1收货地址
     */
    @ApiModelProperty("0发货地址，1收货地址")
    @TableField("add_type")
    private String addType;

    /**
     * 订单编号
     */
    @ApiModelProperty("排序")
    private Integer sort;

    /**
     * 签收码
     */
    @ApiModelProperty("签收码")
    @TableField("receipt_code")
    private String receiptCode;

    /**
     * 签收码是否发送成功
     */
    @ApiModelProperty("签收码是否发送成功 0 失败  1 成功")
    @TableField("is_send_ok")
    private String isSendOk;

    /**
     * 计划提送货时间
     */
    @ApiModelProperty("计划提送货时间")
    @TableField("plan_time")
    private Date planTime;
    /**
     * 城市地址
     */
    @ApiModelProperty("城市地址")
    @TableField("address_city")
    private String addressCity;


    /**
     * 省市区数组
     */
    @ApiModelProperty("省市区数组")
    @TableField(exist = false)
    private String[] ssqArray;

    @ApiModelProperty("详细地址")
    @TableField("address_detail_place")
    private String addressDetailPlace;

    @ApiModelProperty("经纬度")
    @TableField("address_lnglat")
    private String addressLnglat;

    @ApiModelProperty("联系人")
    private String contacts;


    @ApiModelProperty("联系方式")
    @TableField("contacts_phone")
    private String contactsPhone;


    @ApiModelProperty("固话联系方式")
    @TableField("contacts_tel")
    private String contactsTel;

    /**
     * 提交凭证时间
     */
    @ApiModelProperty("提交凭证时间")
    @TableField("success_time")
    private Date successTime;
    /**
     * 提交凭证地址信息
     */
    @ApiModelProperty("提交凭证地址信息")
    @TableField("success_add")
    private String successAdd;
    /**
     * 凭证地址
     */
    @ApiModelProperty("凭证地址")
    private String files;

    @ApiModelProperty("备注")
    private String remark;
    /**
     * 凭证地址数组
     */
    @ApiModelProperty("凭证地址数组")
    @TableField(exist = false)
    private String[] fileArray;

    @TableField("tenant_id")
    private Integer tenantId;




    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
