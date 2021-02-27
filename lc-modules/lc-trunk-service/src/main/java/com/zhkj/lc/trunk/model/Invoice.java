package com.zhkj.lc.trunk.model;

import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author cb
 * @since 2019-02-13
 */
@Data
@TableName("invoice")
@ApiModel(value = "发票信息")
public class Invoice extends Model<Invoice> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 发票抬头
     */
    @TableField("invoice_title")
    private String invoiceTitle;
    /**
     * 开票金额
     */
    @TableField("invoice_value")
    private BigDecimal invoiceValue;
    /**
     * 开具类型（0：专票，1：普票）
     */
    @TableField("invoice_type")
    private String invoiceType;
    /**
     * 税务账号
     */
    @TableField("sy_account")
    private String syAccount;
    /**
     * 开户银行
     */
    @TableField("bank_name")
    private String bankName;
    /**
     * 开户银行账号
     */
    @TableField("bank_account")
    private String bankAccount;
    /**
     * 注册地点
     */
    @TableField("regist_place")
    private String registPlace;
    /**
     * 注册电话
     */
    @TableField("regist_phone")
    private String registPhone;
    /**
     * 邮寄地址
     */
    @TableField("post_place")
    private String postPlace;
    /**
     * 快递单号
     */
    @TableField("post_number")
    private String postNumber;
    @TableField("del_flag")
    private String delFlag;
    @TableField("create_time")
    private Date createTime;
    @TableField("create_by")
    private String createBy;
    @TableField("update_time")
    private Date updateTime;
    @TableField("update_by")
    private String updateBy;
    /**
     * 租户id
     */
    @TableField("tenant_id")
    private Integer tenantId;




    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Invoice{" +
        ", id=" + id +
        ", invoiceTitle=" + invoiceTitle +
        ", invoiceValue=" + invoiceValue +
        ", invoiceType=" + invoiceType +
        ", syAccount=" + syAccount +
        ", bankName=" + bankName +
        ", bankAccount=" + bankAccount +
        ", registPlace=" + registPlace +
        ", registPhone=" + registPhone +
        ", postPlace=" + postPlace +
        ", postNumber=" + postNumber +
        ", delFlag=" + delFlag +
        ", createTime=" + createTime +
        ", createBy=" + createBy +
        ", updateTime=" + updateTime +
        ", updateBy=" + updateBy +
        ", tenantId=" + tenantId +
        "}";
    }
}
