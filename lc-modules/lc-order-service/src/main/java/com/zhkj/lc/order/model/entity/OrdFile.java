package com.zhkj.lc.order.model.entity;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author cb
 * @since 2019-01-24
 */
@Data
@TableName("ord_file")
public class OrdFile extends Model<OrdFile> {

    private static final long serialVersionUID = 1L;

    /**
     * 订单编号
     */
    @TableId("order_id")
    private String orderId;
    /**
     * 文件类型
     */
    @TableField("file_type")
    private String fileType;
    /**
     * 文件路径
     */
    @TableField("file_url")
    private String fileUrl;
    /**
     * 存储时间
     */
    @TableField("create_time")
    private Date createTime;
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
        return "OrdFile{" +
        ", orderId=" + orderId +
        ", fileType=" + fileType +
        ", fileUrl=" + fileUrl +
        ", createTime=" + createTime +
        ", tenantId=" + tenantId +
        "}";
    }
}
