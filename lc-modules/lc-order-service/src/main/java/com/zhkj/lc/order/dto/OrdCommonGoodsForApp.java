package com.zhkj.lc.order.dto;

import com.zhkj.lc.order.model.entity.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 普货订单详细信息，含运踪信息，异常费用，情况，文件(小程序端)
 * @author cb
 * @since 2019-01-05
 */
@Data
@ApiModel(description = "小程序端订单详情数据模型")
public class OrdCommonGoodsForApp {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "订单id")
    private Integer id;
    /**
     * 普货订单编号
     */
    @ApiModelProperty(value = "普货订单编号")
    private String morderId;
    /**
     * 订单状态
     */
    @ApiModelProperty(value = "订单状态")
    private String status;
    /**
     * 客户名称
     */
    @ApiModelProperty(value = "客户名称")
    private String customerName;

    private Integer customerId;
    /**
     * 发货城市
     */
    @ApiModelProperty(value = "发货城市")
    private String sendGoodsPlace;


    /**
     * 发货时间
     */
    @ApiModelProperty(value = "发货时间")
    private Date sendGoodsDate;
    /**
     * 到货城市
     */
    @ApiModelProperty(value = "到货城市")
    private String pickGoodsPlace;


    /**
     * 到货时间
     */
    @ApiModelProperty(value = "到货时间")
    private Date pickGoodsDate;
    /**
     * 提货方式（0：上门提货 1：派车直送）
     */
    @ApiModelProperty(value = "提货方式")
    private String pickGoodsWay;

    /**
     * 发货人
     */
    @ApiModelProperty(value = "发货人")
    private String shipper;
    /**
     * 发货人地址（城市）
     */
    //private String shipperCity;

    /**
     * 发货地址（详细）
     */
    @ApiModelProperty(value = "发货地址")
    private String shipperPlace;
    /**
     * 发货人电话
     */
    @ApiModelProperty(value = "发货人电话")
    private String shipperPhone;
    /**
     * 收货人
     */
    @ApiModelProperty(value = "收货人")
    private String picker;
    /**
     * 收货地址（城市）
     */
   // private String pickerCity;


    /**
     * 收货人地址（详细）
     */
    @ApiModelProperty(value = "收货人地址")
    private String pickerPlace;
    /**
     * 收货人电话
     */
    @ApiModelProperty(value = "收货人电话")
    private String pickerPhone;



    //普货订单货物基本信息
    @ApiModelProperty(value = "普货订单货物基本信息")
    private List<CommonGoodsInfo> commonGoodsInfos;

    //普货订单运踪信息
    @ApiModelProperty(value = "普货订单运踪信息")
    private List<OrdOrderLogistics> ordOrderLogistics;


    //普货订单文件
    @ApiModelProperty(value = "普货订单文件")
    private OrdCommonFile ordCommonFile;
    //普货异常情况
    @ApiModelProperty(value = "普货异常情况")
    private List<OrdExceptionCondition> exceptionConditions;
    //普货异常费用
    @ApiModelProperty(value = "普货异常费用")
    private List<OrdExceptionFee> exceptionFees;

    //提货凭证
    @ApiModelProperty(value = "提货凭证")
    private String[] pickGoodsImgList;

    //签收凭证
    @ApiModelProperty(value = "签收凭证")
    private String[] receiptImgList;

    @ApiModelProperty(value = "车辆调度信息")
    private OrdCommonTruckVO ordCommonTruck;

    /*收货地址信息*/
    private List<OrdPickupArrivalAdd> arrivalAdds;
    /*发货地址信息*/
    private List<OrdPickupArrivalAdd> pickupAdds;
}
