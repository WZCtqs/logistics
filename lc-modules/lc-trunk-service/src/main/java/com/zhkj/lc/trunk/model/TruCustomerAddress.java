package com.zhkj.lc.trunk.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: HP
 * @Date: 2019/2/11 16:50
 * @Description:
 */
@ApiModel(description = "客户收发货地址信息")
@TableName("tru_customer_address")
@Data
public class TruCustomerAddress extends Model<TruCustomerAddress> {

    private static final long serialVersionUID = 1L;

    /**
     * 地址数据id
     */

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 客户id
     */
    @TableField("customer_id")
    private Integer customerId;
    /**
     * 0：收货，1：发货
     */
    @TableField("data_type")
    private String dataType;
    /**
     * 收/发货人姓名
     */
    @TableField("people_name")
    private String peopleName;
    /**
     * 收/发货人电话
     */
    @TableField("people_phone")
    private String peoplePhone;
    /**
     * 收/发货省市区地址
     */
    private String address;
    /**
     * 收/发货详细地址
     */
    @TableField("detaile_address")
    private String detaileAddress;
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
        return null;
    }
}
