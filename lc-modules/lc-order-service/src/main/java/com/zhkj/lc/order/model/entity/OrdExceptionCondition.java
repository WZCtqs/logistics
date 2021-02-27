package com.zhkj.lc.order.model.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhkj.lc.common.annotation.Excel;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author cb
 * @since 2019-01-11
 */
@Data
@TableName("ord_exception_condition")
public class OrdExceptionCondition extends Model<OrdExceptionCondition> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 订单编号
     */
    @TableField("order_id")
    @Excel(name = "订单编号")
    private String orderId;
    /**
     * 司机
     */
    @TableField(exist = false)
    @Excel(name = "司机")
    private String driver;

    @TableField(exist = false)
    @Excel(name = "司机手机号")
    private String driverPhone;
    /**
     * 订单路线
     */
    @TableField(exist = false)
    @Excel(name = "路线")
    private String orderRoute;
    /**
     * 提交时间
     */
    @TableField("submit_date")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Excel(name = "提交时间")
    @JsonFormat()
    private Date submitDate;
    /**
     * 备注
     */
    @TableField("ex_remarks")
    @Excel(name = "异常备注")
    private String exRemarks;
    /**
     * 数据库存储图片路径，以逗号分割
     */
    @TableField("oec_file")
    private String oecFile;

    /**
     * 异常情况图片路径
     */
    @TableField(exist = false)
    private String[] paths;
    /**
     * 删除标志
     */
    @TableField("del_flag")
    private String delFlag;

    /**
     * 租户id
     */
    @TableField("tenant_id")
    private Integer tenantId;
    @TableField("order_status")
    private String orderStatus;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "OrdExceptionCondition{" +
        ", id=" + id +
        ", orderId=" + orderId +
        ", orderRoute=" + orderRoute +
        ", submitDate=" + submitDate +
        ", exRemarks=" + exRemarks +
        ", delFlag=" + delFlag +
        "}";
    }
}
