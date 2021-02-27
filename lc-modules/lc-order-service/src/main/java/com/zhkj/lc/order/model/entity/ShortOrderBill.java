package com.zhkj.lc.order.model.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Auther: HP
 * @Date: 2019/7/3 09:18
 * @Description: 
 */
@ApiModel(value="com.zhkj.lc.order.model.entity.ShortOrderBill")
@Data
@TableName("short_order_bill")
public class ShortOrderBill implements Serializable {
    /**
    * id
    */
    @ApiModelProperty(value="id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
    * 盘短对账单编号
    */
    @ApiModelProperty(value="盘短对账单编号")
    @TableField("account_pay_id")
    private String accountPayId;

    /**
    * 班列日期
    */
    @ApiModelProperty(value="班列日期")
    @TableField("class_date")
    private Date classDate;

    /**
    * 应收费用合计
    */
    @ApiModelProperty(value="应收费用合计")
    private BigDecimal receivable;

    /**
    * 应付费用合计
    */
    @ApiModelProperty(value="应付费用合计")
    @TableField("need_pay")
    private BigDecimal needPay;

    /**
    * 对账单状态
    */
    @ApiModelProperty(value="对账单状态")
    @TableField("settlement_status")
    private String settlementStatus;

    /**
    * 备注
    */
    @ApiModelProperty(value="备注")
    private String remark;

    /**
    * 删除标志
    */
    @ApiModelProperty(value="删除标志")
    @TableField("del_flag")
    private String delFlag;

    /**
    * 创建者
    */
    @ApiModelProperty(value="创建者")
    @TableField("create_by")
    private String createBy;

    /**
    * 创建时间
    */
    @ApiModelProperty(value="创建时间")
    @TableField("create_time")
    private Date createTime;

    /**
    * 更新者
    */
    @ApiModelProperty(value="更新者")
    @TableField("update_by")
    private String updateBy;

    /**
    * 更新时间
    */
    @ApiModelProperty(value="更新时间")
    @TableField("update_time")
    private Date updateTime;

    /**
    * 租户id
    */
    @ApiModelProperty(value="租户id")
    @TableField("tenant_id")
    private Integer tenantId;

    @TableField(exist = false)
    private List<OrdShortOrder> orders;

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", accountPayId=").append(accountPayId);
        sb.append(", classDate=").append(classDate);
        sb.append(", receivable=").append(receivable);
        sb.append(", needPay=").append(needPay);
        sb.append(", settlementStatus=").append(settlementStatus);
        sb.append(", remark=").append(remark);
        sb.append(", delFlag=").append(delFlag);
        sb.append(", createBy=").append(createBy);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateBy=").append(updateBy);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", tenantId=").append(tenantId);
        sb.append("]");
        return sb.toString();
    }
}