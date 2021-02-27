package com.zhkj.lc.order.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhkj.lc.order.model.entity.OrdOrderLogistics;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *普货订单管理bean,不含运踪信息，异常情况，费用
 * @author cb
 * @since 2019-01-05
 */


@Data
@ApiModel(value = "普货订单公众号端数据实体")
public class CommonGoodsForWXPublic implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    /*普货订单编号*/
    private String morderId;
    /*订单状态*/
    private String status;
    /*客户id*/
    private Integer customerId;
    /*发货城市*/
    private String sendGoodsPlace;
    /*发货时间*/
    @JsonFormat(pattern="yyyy/MM/dd",timezone = "GMT+8")
    private Date sendGoodsDate;
    /*到货城市*/
    private String pickGoodsPlace;
    /*到货时间*/
    @JsonFormat(pattern="yyyy/MM/dd",timezone = "GMT+8")
    private Date pickGoodsDate;
    /*提货方式*/
    private String pickGoodsWay;
    /*发货人*/
    private String shipper;
    /*发货人城市*/
    private String shipperCity;
    /*发货人详细地址*/
    private String shipperPlace;
    /*发货人联系电话*/
    private String shipperPhone;
    /*收货人*/
    private String picker;
    /*收货人城市*/
    private String pickerCity;
    /*收货人详细地址*/
    private String pickerPlace;
    /*收货人联系*/
    private String pickerPhone;

    private String balanceWay;
    /**
     * 货物名
     */
    private String goodsName;
    /**
     * 重量
     */
    private BigDecimal weight;
    /**
     * 体积
     */
    private BigDecimal volume;
    /**
     * 货值
     */
    private BigDecimal value;
    /**
     * 包装方式
     */
    private String packWay;
    /**
     * 包装数量
     */
    private Integer packNum;
    /**
     * 货物备注
     */
    private String goodsRemarks;
    /**
     * 删除标志
     */
    private String delFlag;

    /**
     * 创建事件
     */
    private Date createTime;

    private String createBy;

    /*签收码*/
    private String receiptCode;
    private Integer tenantId;
    private Integer mtenantId;
    /*运踪状态*/
    private List<OrdOrderLogistics> logistics;
}
