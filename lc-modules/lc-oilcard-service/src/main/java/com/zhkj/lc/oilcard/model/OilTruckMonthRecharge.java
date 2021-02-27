package com.zhkj.lc.oilcard.model;

import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author JHM
 * @since 2019-05-29
 */
@TableName("oil_truck_month_recharge")
public class OilTruckMonthRecharge extends Model<OilTruckMonthRecharge> {

    private static final long serialVersionUID = 1L;

    /**
     * 主卡月统计id
     */
    @TableId(value = "truck_month_id", type = IdType.AUTO)

    private Integer truckMonthId;
    /**
     * 主卡id
     */
    @TableField("truck_id")
    private Integer truckId;
    /**
     * 本月
     */
    @TableField("year_month")
    private String yearMonth;
    /**
     * 上月余额
     */
    @TableField("last_amount")
    private BigDecimal lastAmount;
    /**
     * 本月合计充值
     */
    @TableField("recharge_sum")
    private BigDecimal rechargeSum;
    /**
     * 本月合计分配
     */
    @TableField("distribute_sum")
    private BigDecimal distributeSum;
    /**
     * 删除标志（0代表存在 1代表删除）
     */
    @TableField("del_flag")
    private String delFlag;
    /**
     * 创建者
     */
    @TableField("create_by")
    private String createBy;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 更新者
     */
    @TableField("update_by")
    private String updateBy;
    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;
    /**
     * 租户id
     */
    @TableField("tenant_id")
    private Integer tenantId;
    /**
     * 备注
     */
    private String remark;
    /**
     * 本月余额
     */
    private BigDecimal balance;


    public Integer getTruckMonthId() {
        return truckMonthId;
    }

    public void setTruckMonthId(Integer truckMonthId) {
        this.truckMonthId = truckMonthId;
    }

    public Integer getTruckId() {
        return truckId;
    }

    public void setTruckId(Integer truckId) {
        this.truckId = truckId;
    }

    public String getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(String yearMonth) {
        this.yearMonth = yearMonth;
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

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    protected Serializable pkVal() {
        return this.truckMonthId;
    }

    @Override
    public String toString() {
        return "OilTruckMonthRecharge{" +
        ", truckMonthId=" + truckMonthId +
        ", truckId=" + truckId +
        ", yearMonth=" + yearMonth +
        ", lastAmount=" + lastAmount +
        ", rechargeSum=" + rechargeSum +
        ", distributeSum=" + distributeSum +
        ", delFlag=" + delFlag +
        ", createBy=" + createBy +
        ", createTime=" + createTime +
        ", updateBy=" + updateBy +
        ", updateTime=" + updateTime +
        ", tenantId=" + tenantId +
        ", remark=" + remark +
        ", balance=" + balance +
        "}";
    }
}
