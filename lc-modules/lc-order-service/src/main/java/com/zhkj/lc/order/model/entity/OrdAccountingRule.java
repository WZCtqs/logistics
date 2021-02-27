package com.zhkj.lc.order.model.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 计费规则附表
 * </p>
 *
 * @author wzc
 * @since 2019-01-11
 */
@Data
@TableName("ord_accounting_rule")
public class OrdAccountingRule extends Model<OrdAccountingRule> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 规则id
     */
    @TableField("rule_id")
    private Integer ruleId;
    /**
     * 界限（<=）
     */
    @TableField("limit_num")
    private Float limitNum;
    /**
     * 价格
     */
    private BigDecimal price;
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

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "OrdAccountingRule{" +
                "id=" + id +
                ", ruleId=" + ruleId +
                ", limitNum=" + limitNum +
                ", price=" + price +
                ", createBy=" + createBy +
                ", createTime=" + createTime +
                ", updateBy=" + updateBy +
                ", updateTime=" + updateTime +
                ", tenantId=" + tenantId +
                "}";
    }
}