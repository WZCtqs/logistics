package com.zhkj.lc.order.model.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 运输跟踪
 * </p>
 *
 * @author wzc
 * @since 2018-12-07
 */
@Data
@TableName("ord_order_logistics")
public class OrdOrderLogistics extends Model<OrdOrderLogistics> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 订单编号
     */
    @TableField("order_id")
    private String orderId;
    /**
     * 订单状态
     */
    @TableField("order_status")
    private String orderStatus;
    /**
     * 运踪信息
     */
    @TableField("logistics_msg")
    private String logisticsMsg;
    /**
     * 运踪地点
     */
    @TableField("logistics_address")
    private String logisticsAddress;
    /**
     * 运踪时间
     */
    @TableField("logistics_time")
    private Date logisticsTime;
    /**
     * 铅封号
     */
    @TableField("seal_number")
    private String sealNumber;
    /**
     * 铅封状态
     */
    @TableField("seal_status")
    private String sealStatus;
    /**
     * 货品状态
     */
    @TableField("goods_status")
    private String goodsStatus;
    /**
     * 车辆状态
     */
    @TableField("truck_status")
    private String truckStatus;


    /**
     * 运踪信息文件路径，（数据库存储，逗号分割）
     */

    private String photos;
    /**
     * 备注
     */
    @TableField(exist = false)
    private String containerNo;

    /**
     * 文件路径数组
     */
    @TableField(exist = false)
    private String[] paths;
    /**
     * 备注
     */
    private String remark;

    /**
     * 租户id
     */
    @TableField("tenant_id")
    private Integer tenantId;

    @ApiModelProperty("当前地址数据id")
    @TableField(exist = false)
    private Integer addId;

    @ApiModelProperty("签收码（送货货需要）")
    @TableField(exist = false)
    private String receiptCode;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "OrdOrderLogistics{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", logisticsMsg=" + logisticsMsg +
                ", logisticsAddress=" + logisticsAddress +
                ", logisticsTime=" + logisticsTime +
                ", sealNumber=" + sealNumber +
                ", sealStatus=" + sealStatus +
                ", goodsStatus=" + goodsStatus +
                ", truckStatus=" + truckStatus +

                ", remark=" + remark +
                "}";
    }
}
