package com.zhkj.lc.order.model.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Auther: wzc
 * @Date: 2019/1/11 15:13
 * @Description:
 */
@Data
@TableName("ord_accounting_rules")
public class OrdAccountingRules  extends Model<OrdCommonFile> {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 规则名称
     */
    @TableField("rule_name")
    private String ruleName;
    /**
     * 对象类型
     */
    @TableField("obj_type")
    private String objType;
    /**
     * 规则对象id
     */
    @TableField("rule_obj_id")
    private Integer ruleObjId;
    /**
     * 规则类型
     */
    @TableField("rule_type")
    private String ruleType;
    /**
     * 起运量
     */
    @TableField("start_num")
    private Float startNum;
    /**
     * 删除标志
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

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "OrdAccountingRules{" +
                "id=" + id +
                ", ruleName=" + ruleName +
                ", ruleObjId=" + ruleObjId +
                ", ruleType=" + ruleType +
                ", startNum=" + startNum +
                ", delFlag=" + delFlag +
                ", createBy=" + createBy +
                ", createTime=" + createTime +
                ", updateBy=" + updateBy +
                ", updateTime=" + updateTime +
                ", tenantId=" + tenantId +
                "}";
    }
}
