package com.zhkj.lc.order.model.entity;

import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhkj.lc.order.dto.OrdCommonTruckVO;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author cb
 * @since 2019-01-05
 */
@Data
@TableName("ord_common_goods")
public class OrdCommonGoods extends Model<OrdCommonGoods> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 普货订单编号
     */
    @TableField("order_id")
    private String morderId;

    /**
     * 订单编号数组
     */
    @TableField(exist = false)
    private String[] orderIds;

    /**
     * 订单状态
     */
    private String status;

    /**
     * 结算状态
     */
    @TableField("balance_status")
    private String balanceStatus;

    /**
     * 客户名称
     */
    @TableField("customer_id")
    private Integer customerId;
    /**
     * 发货城市
     */
    @TableField("send_goods_place")
    private String sendGoodsPlace;


    /**
     * 发货城市数组
     */
    @TableField(exist = false)
    private String[]sendPlaceArray;
    /**
     * 发货时间
     */
    @TableField("send_goods_date")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date sendGoodsDate;
    /**
     * 到货城市
     */
    @TableField("pick_goods_place")
    private String pickGoodsPlace;


    /**
     * 发货城市数组
     */
    @TableField(exist = false)
    private String[]pickPlaceArray;
    /**
     * 到货时间
     */
    @TableField("pick_goods_date")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date pickGoodsDate;
    /**
     * 提货方式（0：上门提货 1：派车直送）
     */
    @TableField("pick_goods_way")
    private String pickGoodsWay;
    /**
     * 制单员
     */
    @TableField("doc_operator")
    private String docOperator;
    /**
     * 发货人
     */
    private String shipper;
    /**
     * 发货人地址（城市）
     */
    @TableField("shipper_city")
    private String shipperCity;

    /**
     * 发货人城市数组
     */
   /* @TableField(exist = false)
    private String[]shipperCityArray;*/
    /**
     * 发货地址（详细）
     */
    @TableField("shipper_place")
    private String shipperPlace;
    /**
     * 发货人电话
     */
    @TableField("shipper_phone")
    private String shipperPhone;
    /**
     * 收货人
     */
    private String picker;
    /**
     * 收货地址（城市）
     */
    @TableField("picker_city")
    private String pickerCity;

    /**
     * 收货人城市数组
     */
   /* @TableField(exist = false)
    private String[]pickerCityArray;*/
    /**
     * 收货人地址（详细）
     */
    @TableField("picker_place")
    private String pickerPlace;
    /**
     * 收货人电话
     */
    @TableField("picker_phone")
    private String pickerPhone;
    /**
     * 结算方式
     */
    @TableField("balance_way")
    private String balanceWay;
    /**
     * 计费里程
     */
    @TableField("charged_mileage")
    private int mchargedMileage;
    /**
     * 运输费用
     */
    @TableField("transport_fee")
    private BigDecimal mtransportFee;
    /**
     * 提货费用
     */
    @TableField("pick_fee")
    private BigDecimal mpickFee;
    /**
     * 装货费用
     */
    @TableField("pack_fee")
    private BigDecimal mpackFee;
    /**
     * 卸货费用
     */
    @TableField("release_fee")
    private BigDecimal mreleaseFee;
    /**
     * 保险费用
     */
    @TableField("insurance_fee")
    private BigDecimal minsuranceFee;
    /**
     * 其他费用
     */
    @TableField("other_fee")
    private BigDecimal motherFee;
    /**
     * 是否开发票（0：否，1：是）
     */
    @TableField("is_invoice")
    private String isInvoice;
    /**
     * 签收码
     */
    @TableField("receipt_code")
    private String receiptCode;
    /**
     * 是否发送成功
     */
    @TableField("is_send")
    private String isSend;

    @TableField("send_truck_date")
    private Date sendTruckDate;
    /**
     * 费用合计
     */
    @TableField("total_fee")
    private BigDecimal mtotalFee;
    /**
     * 删除标志
     */
    @TableField("del_flag")
    private String delFlag;
    /**
     * 创建者
     */
    @TableField("create_by")
    private String createBy;
    /**
     * 创建事件
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 更新者
     */
    @TableField("update_by")
    private String updateBy;
    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;
    /**
     * 租户id
     */
    @TableField("tenant_id")
    private Integer mtenantId;

    @TableField("driver_id")
    private Integer driverId;


    /**
     *是否加入对账单
     */
    @TableField("is_add_to_bill")
    private String isAddToBill;


    //普货订单货物基本信息
    @TableField(exist = false)
    private List<CommonGoodsInfo> commonGoodsInfos;

    //普货订单运踪信息
    @TableField(exist = false)
    private List<OrdOrderLogistics> ordOrderLogistics;

    //普货订单汽车调度
    @TableField(exist = false)
    private OrdCommonTruck ordCommonTruck;

    //普货订单文件
    @TableField(exist = false)
    private OrdCommonFile ordCommonFile;
    //普货异常信息
    @TableField(exist = false)
    private List<OrdExceptionCondition> exceptionConditions;
    //普货异常情况
    @TableField(exist = false)
    private List<OrdExceptionFee> exceptionFees;

    @TableField(exist = false)
    private BigDecimal sumWeight;
    @TableField(exist = false)
    private BigDecimal sumVolume;
    @TableField(exist = false)
    private String ifEx;


    /*收货地址信息*/
    @TableField(exist = false)
    private List<OrdPickupArrivalAdd> arrivalAdds;
    /*发货地址信息*/
    @TableField(exist = false)
    private List<OrdPickupArrivalAdd> pickupAdds;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "OrdCommonGoods{" +
                ", id=" + id +
                ", orderId=" + morderId +
                ", status=" + status +
                ", sendGoodsPlace=" + sendGoodsPlace +
                ", sendGoodsDate=" + sendGoodsDate +
                ", pickGoodsPlace=" + pickGoodsPlace +
                ", pickGoodsDate=" + pickGoodsDate +
                ", pickGoodsWay=" + pickGoodsWay +
                ", docOperator=" + docOperator +
                ", shipper=" + shipper +
                ", shipperCity=" + shipperCity +
                ", shipperPlace=" + shipperPlace +
                ", shipperPhone=" + shipperPhone +
                ", picker=" + picker +
                ", pickerCity=" + pickerCity +
                ", pickerPlace=" + pickerPlace +
                ", pickerPhone=" + pickerPhone +
                ", balanceWay=" + balanceWay +
                ", chargedMileage=" + mchargedMileage +
                ", transportFee=" + mtransportFee +
                ", pickFee=" + mpickFee +
                ", packFee=" + mpackFee +
                ", releaseFee=" + mreleaseFee +
                ", insuranceFee=" + minsuranceFee +
                ", otherFee=" + motherFee +
                ", isInvoice=" + isInvoice +
                ", receiptCode=" + receiptCode +
                ", isSend=" + isSend +
                ", totalFee=" + mtotalFee +
                ", delFlag=" + delFlag +
                ", createBy=" + createBy +
                ", createTime=" + createTime +
                ", updateBy=" + updateBy +
                ", updateTime=" + updateTime +

                "}";
    }
}
