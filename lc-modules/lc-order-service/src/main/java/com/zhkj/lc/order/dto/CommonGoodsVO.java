package com.zhkj.lc.order.dto;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.zhkj.lc.order.model.entity.*;
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
@ApiModel(value = "普货订单管理分页数据实体")
public class CommonGoodsVO implements Serializable {

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

    private Integer customerId;

    private String customerName;
    /**
     * 发货城市
     */

    private String sendGoodsPlace;


    /**
     * 发货时间
     */

    private Date sendGoodsDate;
    /**
     * 到货城市
     */

    private String pickGoodsPlace;
    /**
     * 提货方式
     */
    private String pickGoodsWay;



    /**
     * 到货时间
     */

    private Date pickGoodsDate;


    /**
     * 发货人
     */
    private String shipper;
    /**
     * 发货人地址（城市）
     */

    private String shipperCity;


    /**
     * 收货人
     */
    private String picker;
    /**
     * 收货地址（城市）
     */

    private String pickerCity;


    /**
     * 计费里程
     */

    private BigDecimal mchargedMileage;


    /**
     * 费用合计
     */
    private BigDecimal mtotalFee;
    /**
     * 删除标志
     */
    private String delFlag;

    /**
     * 创建事件
     */
    private Date createTime;


    /**
     * 租户id
     */
    private Integer mtenantId;


    //普货订单货物基本信息
    private List<CommonGoodsInfo> commonGoodsInfos;


    //总重量
    private BigDecimal sumWeight;

    //总体积
    private BigDecimal sumVolume;

    //普货订单汽车调度
    private OrdCommonTruckVO ordCommonTruck;

    private Integer mdriverId;


    private List<OrdOrderLogistics> ordOrderLogistics;

    private List<OrdPickupArrivalAdd> arrivalAdds;

    private List<OrdPickupArrivalAdd> pickupAdds;

}
