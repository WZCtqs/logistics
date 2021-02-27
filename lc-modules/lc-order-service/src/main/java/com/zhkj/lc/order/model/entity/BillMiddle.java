package com.zhkj.lc.order.model.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.xiaoleilu.hutool.date.DateTime;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 应收对账单表
 * </p>
 *
 * @author wzc
 * @since 2019-02-15
 */
@Data
@TableName("billmiddle")
public class BillMiddle extends Model<BillMiddle> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 对账单编号
     */
    @TableField("account_pay_number")
    private String accountPayNumber;
    /**
     * 订单编号
     */
    @TableField("order_number")
    private String orderNumber;

    /**
     * 租户id
     */
    @TableField("tenant_id")
    private Integer tenantId;
    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
