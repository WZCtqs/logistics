package com.zhkj.lc.order.model.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 货物详细信息
 * </p>
 *
 * @author cb
 * @since 2019-01-05
 */
@Data
@TableName("common_goods_info")
public class CommonGoodsInfo extends Model<CommonGoodsInfo> {

    private static final long serialVersionUID = 1L;


    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("order_id")
    private String orderId;
    /**
     * 货物名
     */
    @TableField("goods_name")
    private String goodsName;
    /**
     * 重量
     */
    private BigDecimal weight;
    /**
     * 体积
     */
    private BigDecimal volume;
    /**
     * 货值
     */
    private BigDecimal value;
    /**
     * 包装方式
     */
    @TableField("pack_way")
    private String packWay;
    /**
     * 包装数量
     */
    @TableField("pack_num")
    private Integer packNum;
    /**
     * 货物备注
     */
    @TableField("goods_remarks")
    private String goodsRemarks;

    /**
     * 租户id
     */
    @TableField("tenant_id")
    private Integer tenantId;




    @Override
    protected Serializable pkVal() {
        return this.orderId;
    }

    @Override
    public String toString() {
        return "CommonGoodsInfo{" +
        ", orderId=" + orderId +
        ", goodsName=" + goodsName +
        ", weight=" + weight +
        ", volume=" + volume +
        ", value=" + value +
        ", packWay=" + packWay +
        ", packNum=" + packNum +
        ", goodsRemarks=" + goodsRemarks +
        "}";
    }
}
