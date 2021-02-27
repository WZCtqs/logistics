package com.zhkj.lc.order.dto;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.zhkj.lc.order.model.entity.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 普货订单详细信息，含运踪信息，异常费用，情况，文件
 * @author cb
 * @since 2019-01-05
 */
@Data

public class OrdCommonGoodsVo  {

    private static final long serialVersionUID = 1L;

    private Integer id;
    /**
     * 普货订单编号
     */
    private String morderId;
    /**
     * 订单状态
     */
    private String status;
    /**
     * 客户名称
     */
    private String customerName;

    private Integer customerId;
    /**
     * 发货城市
     */
    private String sendGoodsPlace;


    /**
     * 发货城市数组
     */
    private String[]sendPlaceArray;
    /**
     * 发货时间
     */
    private Date sendGoodsDate;
    /**
     * 到货城市
     */
    private String pickGoodsPlace;


    /**
     * 发货城市数组
     */
    private String[]pickPlaceArray;
    /**
     * 到货时间
     */
    private Date pickGoodsDate;
    /**
     * 提货方式（0：上门提货 1：派车直送）
     */
    private String pickGoodsWay;
    /**
     * 制单员
     */
    private String docOperator;
    /**
     * 发货人
     */
    private String shipper;
    /**
     * 发货人地址（城市）
     */
    private String shipperCity;

    /**
     * 发货人城市数组
     */
    private String[]shipperCityArray;
    /**
     * 发货地址（详细）
     */
    private String shipperPlace;
    /**
     * 发货人电话
     */
    private String shipperPhone;
    /**
     * 收货人
     */
    private String picker;
    /**
     * 收货地址（城市）
     */
    private String pickerCity;

    /**
     * 收货人城市数组
     */
    private String[]pickerCityArray;
    /**
     * 收货人地址（详细）
     */
    private String pickerPlace;
    /**
     * 收货人电话
     */
    private String pickerPhone;
    /**
     * 结算方式
     */
    private String balanceWay;
    /**
     * 计费里程
     */
    private BigDecimal mchargedMileage;
    /**
     * 运输费用
     */
    private BigDecimal mtransportFee;
    /**
     * 提货费用
     */
    private BigDecimal mpickFee;
    /**
     * 装货费用
     */
    private BigDecimal mpackFee;
    /**
     * 卸货费用
     */
    private BigDecimal mreleaseFee;
    /**
     * 保险费用
     */
    private BigDecimal minsuranceFee;
    /**
     * 其他费用
     */
    private BigDecimal motherFee;
    /**
     * 是否开发票（0：否，1：是）
     */
    private String isInvoice;
    /**
     * 签收码
     */
    private String receiptCode;
    /**
     * 是否发送成功
     */
    private String isSend;

    private Date sendTruckDate;
    /**
     * 费用合计
     */
    private BigDecimal mtotalFee;
    /**
     * 删除标志
     */
    private String delFlag;
    /**
     * 创建者
     */
    private String createBy;
    /**
     * 创建事件
     */
    private Date createTime;
    /**
     * 更新者
     */
    private String updateBy;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 租户id
     */
    private Integer mtenantId;


    /**
     * 司机id
     */
    private Integer driverId;
    //普货订单货物基本信息
    private List<CommonGoodsInfo> commonGoodsInfos;

    //普货订单运踪信息
    private List<OrdOrderLogistics> ordOrderLogistics;

    //普货订单汽车调度
    private OrdCommonTruckVO ordCommonTruck;

    //普货订单文件
    private OrdCommonFile ordCommonFile;
    //普货异常情况
    private List<OrdExceptionCondition> exceptionConditions;
    //普货异常费用
    private List<OrdExceptionFee> exceptionFees;

    //提货凭证文件url
    private String[]thImgs;
    //签收凭证文件url
    private String[]qsImgs;

    private BigDecimal sumWeight;
    private BigDecimal sumVolume;
    private String ifEx;

    private String driverAddress;

    /*收货地址信息*/
    private List<OrdPickupArrivalAdd> arrivalAdds;
    /*发货地址信息*/
    private List<OrdPickupArrivalAdd> pickupAdds;
}
