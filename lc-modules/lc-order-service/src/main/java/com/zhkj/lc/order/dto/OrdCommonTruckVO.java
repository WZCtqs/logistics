package com.zhkj.lc.order.dto;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.zhkj.lc.common.vo.DriverVO;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 车辆调度查询数据模型
 * </p>
 *
 * @author cb
 * @since 2019-01-03
 */
@Data

public class OrdCommonTruckVO {

    private static final long serialVersionUID = 1L;

    /**
     * 普货订单编号
     */

    private String orderId;
    /**
     * 车辆类型（0：自有车，1：外调车）
     */

    private String truckType;
    /**
     * 承运商id
     */

    private Integer truckTeamId;
    /**
     * 司机id
     */

    private Integer mdriverId;

    /**
     * 司机id
     */

    private Integer sdriverId;

    /**
     * 车牌号
     */
    private String plateNumber;

    /**
     * 应付单价
     */
    private BigDecimal payPrice;
    /**
     * 所属车队利率
     */
    private BigDecimal payRate;
    /**
     * 车型（0：厢车，1：自卸，2：冷藏，3：平板）
     */

    private String vehicleType;
    /**
     * 车高
     */

    private String vehicleLength;
    /**
     * 运输费
     */
    private BigDecimal transportFee;
    /**
     * 装货费
     */
    private BigDecimal packFee;
    /**
     * 卸货费
     */
    private BigDecimal releaseFee;
    /**
     * 保险费
     */
    private BigDecimal insuranceFee;
    /**
     * 其他费用
     */
    private BigDecimal otherFee;
    /**
     * 结算方式（0：按单 1：按月）
     */
    private String payType;
    /**
     * 付款方式（0：现金，1：油卡）
     */
    private BigDecimal oilPayment;
    /**
     * 费用总计
     */
    private BigDecimal totalFee;
    /**
     * 付款金额
     */
    private BigDecimal cashPayment;
    /**
     * 计费里程
     */
    private BigDecimal chargedMileage;
    /**
     * 路桥费
     */
    private BigDecimal luqiaoFee;
    /**
     * 油费
     */
    private BigDecimal oilFee;
    /**
     * 车辆调度备注
     */
    private String remarks;

    private List<DriverVO> driverVOS;
    /**
     * 租户id
     */
    private Integer tenantId;

    private String needPayStatus;

    private String settlement;

    private String ifAddToYfbill;

}
