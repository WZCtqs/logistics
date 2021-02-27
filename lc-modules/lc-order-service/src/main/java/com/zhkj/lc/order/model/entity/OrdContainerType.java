package com.zhkj.lc.order.model.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/**
 * 订单的应付费用统计
 */
@Data
@TableName("ord_container_type")
@ApiModel(value = "集装箱箱型")
public class OrdContainerType extends Model<OrdContainerType> {


    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("container_type")
    private String containerType;

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
}
