package com.zhkj.lc.oilcard.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.zhkj.lc.common.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author ckj
 * @since 2019-02-11
 */
@ApiModel(description = "主卡月充值信息")
@Data
@TableName("major_month_recharge")
public class MajorMonthRecharge extends Model<MajorMonthRecharge> {

    private static final long serialVersionUID = 1L;

    /**
     * 主卡月统计id
     */
    @ApiModelProperty(value = "主卡月统计id")
    @TableId(value = "major_month_id", type = IdType.AUTO)
    private Integer majorMonthId;
    /**
     * 主卡id
     */
    @ApiModelProperty(value = "主卡id")
    @TableField("major_id")
    private Integer majorId;
    /**
     * 主卡名
     */
    @Excel(name = "主卡名")
    @ApiModelProperty(value = "主卡名")
    @TableField(exist = false)
    private String majorName;
    /**
     * 主卡号
     */
    @Excel(name = "主卡名")
    @ApiModelProperty(value = "主卡号")
    @TableField(exist = false)
    private String majorNumber;
    /**
     * 归属公司
     */
    @Excel(name = "主卡名")
    @ApiModelProperty(value = "主卡归属公司")
    @TableField(exist = false)
    private String majorCompany;
    /**
     * 主卡下油卡数量
     */
    @ApiModelProperty(value = "主卡下油卡数量")
    @TableField(exist = false)
    private Integer cardNum;
    /**
     * 年月份
     */
    @ApiModelProperty(value = "年月")
    @TableField("year_month")
    private String yearMonth;
    /**
     * 上月余额
     */
    @Excel(name = "上月余额")
    @ApiModelProperty(value = "上月余额")
    @TableField("last_amount")
    private BigDecimal lastAmount;
    /**
     * 本月合计充值
     */
    @Excel(name = "本月合计充值")
    @ApiModelProperty(value = "本月合计充值")
    @TableField("recharge_sum")
    private BigDecimal rechargeSum;
    /**
     * 本月合计分配
     */
    @Excel(name = "本月合计分配")
    @ApiModelProperty(value = "本月合计分配")
    @TableField("distribute_sum")
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
    @TableField(exist = false)
    private BigDecimal monthAmount;
    /**
     * 删除标志（0代表存在 1代表删除）
     */
    @ApiModelProperty(hidden = true)
    @TableField("del_flag")
    private String delFlag;
    /**
     * 创建者
     */
    @ApiModelProperty(hidden = true)
    @TableField("create_by")
    private String createBy;
    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    @TableField("create_time")
    private Date createTime;
    /**
     * 更新者
     */
    @ApiModelProperty(hidden = true)
    @TableField("update_by")
    private String updateBy;
    /**
     * 更新时间
     */
    @ApiModelProperty(hidden = true)
    @TableField("update_time")
    private Date updateTime;
    /**
     * 租户id
     */
    @ApiModelProperty(hidden = true)
    @TableField("tenant_id")
    private Integer tenantId;
    /**
     * 备注
     */
    @Excel(name = "备注")
    @ApiModelProperty(hidden = true)
    private String remark;


    public Integer getMajorMonthId() {
        return majorMonthId;
    }

    public void setMajorMonthId(Integer majorMonthId) {
        this.majorMonthId = majorMonthId;
    }

    public Integer getMajorId() {
        return majorId;
    }

    public void setMajorId(Integer majorId) {
        this.majorId = majorId;
    }

    public BigDecimal getLastAmount() {
        return lastAmount;
    }

    public void setLastAmount(BigDecimal lastAmount) {
        this.lastAmount = lastAmount;
    }

    public BigDecimal getRechargeSum() {
        return rechargeSum;
    }

    public void setRechargeSum(BigDecimal rechargeSum) {
        this.rechargeSum = rechargeSum;
    }

    public BigDecimal getDistributeSum() {
        return distributeSum;
    }

    public void setDistributeSum(BigDecimal distributeSum) {
        this.distributeSum = distributeSum;
    }

    public BigDecimal getRebate() {
        return rebate;
    }

    public void setRebate(BigDecimal rebate) {
        this.rebate = rebate;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getTenantId() {
        return tenantId;
    }

    public void setTenantId(Integer tenantId) {
        this.tenantId = tenantId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    protected Serializable pkVal() {
        return this.majorMonthId;
    }

    @Override
    public String toString() {
        return "MajorMonthRecharge{" +
                "majorMonthId=" + majorMonthId +
                ", majorId=" + majorId +
                ", majorName='" + majorName + '\'' +
                ", majorNumber='" + majorNumber + '\'' +
                ", majorCompany='" + majorCompany + '\'' +
                ", yearMonth='" + yearMonth + '\'' +
                ", lastAmount=" + lastAmount +
                ", rechargeSum=" + rechargeSum +
                ", distributeSum=" + distributeSum +
                ", rebate=" + rebate +
                ", monthAmount=" + monthAmount +
                ", delFlag='" + delFlag + '\'' +
                ", createBy='" + createBy + '\'' +
                ", createTime=" + createTime +
                ", updateBy='" + updateBy + '\'' +
                ", updateTime=" + updateTime +
                ", tenantId=" + tenantId +
                ", remark='" + remark + '\'' +
                '}';
    }
}
