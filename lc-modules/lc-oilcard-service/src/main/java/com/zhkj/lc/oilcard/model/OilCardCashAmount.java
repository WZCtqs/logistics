package com.zhkj.lc.oilcard.model;

import com.zhkj.lc.common.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Description 公司油卡现金余额
 * @Author ckj
 * @Date 2019/3/2 14:16
 */
@ApiModel(description = "公司油卡现金金额")
@Data
public class OilCardCashAmount implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 年月份
     */
    @ApiModelProperty(value = "年月")
    private String yearMonth;
    /**
     * 归属公司
     */
    @Excel(name = "公司名称")
    @ApiModelProperty(value = "公司名称")
    private String majorCompany;
    /**
     * 上月余额
     */
    @Excel(name = "上月余额")
    @ApiModelProperty(value = "上月余额")
    private BigDecimal lastAmount;
    /**
     * 本月合计充值
     */
    @Excel(name = "本月合计充值")
    @ApiModelProperty(value = "本月合计充值")
    private BigDecimal rechargeSum;
    /**
     * 本月合计分配
     */
    @Excel(name = "本月合计分配")
    @ApiModelProperty(value = "本月合计分配")
    private BigDecimal distributeSum;
    /**
     * 返利
     */
    @Excel(name = "返利")
    @ApiModelProperty(value = "返利")
    private BigDecimal rebate;
    /**
     * 本月余额
     */
    @Excel(name = "本月余额")
    @ApiModelProperty(value = "本月余额")
    private BigDecimal monthAmount;

    /**
     * 备注
     */
    @Excel(name = "备注")
    @ApiModelProperty(value = "备注")
    private String remark;
}
