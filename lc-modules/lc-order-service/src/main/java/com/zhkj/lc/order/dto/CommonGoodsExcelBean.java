package com.zhkj.lc.order.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/**
 * 普货订单导入bean
 */
@Data
public class CommonGoodsExcelBean {

    //客户订单号
    @Excel(name = "客户订单号")
    private String customerOrdId;
    //客户名称
    @Excel(name = "客户名称")
    private String customerName;

    private Integer customerId;
/*
    //派车日期
    @Excel(name = "派车日期")
    private Date sendTruckDate;*/

    //发货城市
    @Excel(name = "发货城市")
    private String sendGoodsPlace;

    //到货城市
    @Excel(name = "到货城市")
    private String pickGoodsPlace;
    //发货时间
    @Excel(name = "发货时间")
    private Date sendGoodsDate;
    //到货时间
    @Excel(name = "到货时间")
    private Date pickGoodsDate;
    //提货方式
    @Excel(name = "提货方式")
    private String pickGoodsWay;


    /*发货人+“-”+地址（城市+详细）*/
    @Excel(name = "发货人-提货地址1")
    private String pickupAdd1;
    @Excel(name = "发货人联系方式1")
    private String shipperPhone1;

    /*发货人+“-”+地址（城市+详细）*/
    @Excel(name = "发货人-提货地址2")
    private String pickupAdd2;
    @Excel(name = "发货人联系方式2")
    private String shipperPhone2;

    /*发货人+“-”+地址（城市+详细）*/
    @Excel(name = "发货人-提货地址3")
    private String pickupAdd3;
    @Excel(name = "发货人联系方式3")
    private String shipperPhone3;

    /*收货人+“-”+地址（城市+详细）*/
    @Excel(name = "收货人-送货地址1")
    private String arrivalAdd1;
    @Excel(name = "收货人联系方式1")
    private String pickerPhone1;

    /*收货人+“-”+地址（城市+详细）*/
    @Excel(name = "收货人-送货地址2")
    private String arrivalAdd2;
    @Excel(name = "收货人联系方式2")
    private String pickerPhone2;

    /*收货人+“-”+地址（城市+详细）*/
    @Excel(name = "收货人-送货地址3")
    private String arrivalAdd3;
    @Excel(name = "收货人联系方式3")
    private String pickerPhone3;


    //收货人
    //@Excel(name = "收货人")
    private String picker;
    //收货人电话
    //@Excel(name = "收货人电话")
    private String pickerPhone;
    //收货人城市
    //@Excel(name = "收货人城市")
    private String pickerCity;
    //收货人详细地址
    //@Excel(name = "收货人详细地址")
    private String pickerPlace;
    //发货人
    //@Excel(name = "发货人")
    private String shipper;
    //发货人电话
    //@Excel(name = "发货人电话")
    private String shipperPhone;
    //发货人城市
    //@Excel(name = "发货人城市")
    private String shipperCity;
    //发货人详细地址
    //@Excel(name = "发货人详细地址")
    private String shipperPlace;

    //货物名称
    @Excel(name = "货物名称")
    private String goodsName;
    //重量（千克）
    @Excel(name = "重量（吨）")
    //private BigDecimal weight;
    private String weight;
    //体积（立方）
    @Excel(name = "体积（立方）")
    //private BigDecimal volume;
    private String volume;
    //货值
    @Excel(name = "货值")
    //private BigDecimal value;
    private String value;
    //包装方式
    @Excel(name = "包装方式")
    private String packWay;
    //包装数量
    @Excel(name = "包装数量")
   // private Integer packNum;
    private String packNum;
    //货物备注
    @Excel(name = "货物备注")
    private String goodsRemarks;
    //应收结算方式
    @Excel(name = "应收结算方式")
    private String balanceWay;
    //应收计费里程
    @Excel(name = "应收计费里程")
    private BigDecimal mchargedMileage;
    //应收运输费用
    @Excel(name = "应收运输费用")
    private BigDecimal mtransportFee;
    //应收提货费用
    @Excel(name = "应收提货费用")
    private BigDecimal mpickFee;
    //应收装货费用
    @Excel(name = "应收装货费用")
    private BigDecimal mpackFee;
    //应收卸货费用
    @Excel(name = "应收卸货费用")
    private BigDecimal mreleaseFee;
    //应收保险费用
    @Excel(name = "应收保险费用")
    private BigDecimal minsuranceFee;
    //应收其他费用
    @Excel(name = "应收其他费用")
    private BigDecimal motherFee;
    //是否开票
    @Excel(name = "是否开票")
    private String isInvoice;
    //应收费用合计
    @Excel(name = "应收费用合计")
    private BigDecimal mtotalFee;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommonGoodsExcelBean that = (CommonGoodsExcelBean) o;
        return Objects.equals(customerOrdId, that.customerOrdId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(customerOrdId);
    }
}
