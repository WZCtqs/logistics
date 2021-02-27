package com.zhkj.lc.common.vo;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.zhkj.lc.common.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class OilTruckRechargeVO {

    private static final long serialVersionUID = 1L;


    private Integer truckMonthId;
     /**
     * 车辆id
     */

    private Integer truckId;

    @Excel(name = "日期")
    private String yearMonth;

    /**
     * 车牌号
     */
    @Excel(name = "车牌号")
    private String plateNumber;
    /**
     * 财务充值金额（应付对账单分配金额）
     */
    private BigDecimal recharge;
    /**
     * 充值时间
     */


    private String rechargeTime;
    /**
     * 分配员
     */

    private String createBy;
    /**
     * 租户id
     */

    private Integer tenantId;



    /**
     * 持卡数量
     */
    @ApiModelProperty(value = "持卡数量")
    @TableField(exist = false)
    @Excel(name = "持卡数量")
    private String quantity;//持卡数量

    /**
     * 车辆类型
     */
    @ApiModelProperty(value = "车辆类型")
    @TableField(exist = false)
    @Excel(name = "车辆类型")
    private String attribute;//车辆类型

    @Excel(name = "上月结余")
    private BigDecimal lastAmount;

    @Excel(name = "财务分配数")
    private BigDecimal rechargeSum;

    @Excel(name = "本月已充值")
    private BigDecimal distributeSum;

    @Excel(name = "本月余额")
    private BigDecimal balance;




}
