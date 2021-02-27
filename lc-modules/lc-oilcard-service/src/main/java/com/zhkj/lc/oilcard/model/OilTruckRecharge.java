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
@TableName("oil_truck_recharge")
public class OilTruckRecharge extends Model<OilTruckRecharge> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 车辆id
     */
    @TableField("truck_id")
    private Integer truckId;
    /**
     * 车牌号
     */
    @TableField("plate_number")
    private String plateNumber;
    /**
     * 财务充值金额（应付对账单分配金额）
     */
    private BigDecimal recharge;
    /**
     * 充值时间
     */
    @TableField("recharge_time")
    private Date rechargeTime;
    /**
     * 分配员
     */
    @TableField("create_by")
    private String createBy;
    /**
     * 租户id
     */
    @TableField("tenant_id")
    private Integer tenantId;


    /**
     * 余额
     */
    @TableField("balance")
    private BigDecimal balance;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTruckId() {
        return truckId;
    }

    public void setTruckId(Integer truckId) {
        this.truckId = truckId;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public BigDecimal getRecharge() {
        return recharge;
    }

    public void setRecharge(BigDecimal recharge) {
        this.recharge = recharge;
    }

    public Date getRechargeTime() {
        return rechargeTime;
    }

    public void setRechargeTime(Date rechargeTime) {
        this.rechargeTime = rechargeTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Integer getTenantId() {
        return tenantId;
    }

    public void setTenantId(Integer tenantId) {
        this.tenantId = tenantId;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "OilTruckRecharge{" +
        ", id=" + id +
        ", truckId=" + truckId +
        ", plateNumber=" + plateNumber +
        ", recharge=" + recharge +
        ", rechargeTime=" + rechargeTime +
        ", createBy=" + createBy +
        ", tenantId=" + tenantId +
         ", balance=" + balance +
        "}";
    }
}
