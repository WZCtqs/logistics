package com.zhkj.lc.oilcard.model;

import com.zhkj.lc.common.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Description
 * @Author ckj
 * @Date 2019/3/2 10:43
 */
@ApiModel(description = "油卡充值汇总")
@Data
public class RechargeSum implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 搜索年月
     */
    @Excel(name = "日期")
    @ApiModelProperty(value = "搜索年月")
    private String yearMonth;

    /** 充值汇总类别 */
    @Excel(name = "充值汇总类别")
    @ApiModelProperty(value = "充值汇总类别")
    private String rechrageSort;

    /**
     * 月充值金额
     */
    @Excel(name = "月充值金额")
    @ApiModelProperty(value = "月充值金额")
    private BigDecimal monthRechargeSum;

    /**
     * 备注
     */
    @Excel(name = "备注")
    @ApiModelProperty(value = "备注")
    private String remark;
}
