package com.zhkj.lc.order.model.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 运输跟踪
 * </p>
 *
 * @author wzc
 * @since 2018-12-07
 */
@Data
@ApiModel(description = "首页在途订单司机记录的最新位置")
public class OrdOrderLogisticsVo {

    private static final long serialVersionUID = 1L;

    /**
     * 订单编号
     */
    @TableField("order_id")
    private String orderId;
    /**
     * 运踪信息
     */
    @TableField("logistics_msg")
    private String logisticsMsg;
    /**
     * 运踪地点
     */
    @TableField("logistics_address")
    private String logisticsAddress;
    /**
     * 运踪时间
     */
    @TableField("logistics_time")
    private Date logisticsTime;

    private Integer driverId;

    private String driverName;

    private String plateNumber;

    private String truckTeamName;

    private String orderStatus;
}
