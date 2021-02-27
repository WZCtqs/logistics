package com.zhkj.lc.trunk.model;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 司机常跑路线
 * </p>
 *
 * @author wzc
 * @since 2019-01-03
 */
@TableName("freight_route")
@Data
public class FreightRoute extends Model<FreightRoute> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 司机id
     */
    @ApiModelProperty(value = "司机id")
    @TableField("driver_id")
    private Integer driverId;
    /**
     * 出发地
     */
    @ApiModelProperty(value = "出发地")
    private String origin;
    /**
     * 出发地数组
     */
    @ApiModelProperty(value = "出发地数组")
    @TableField(exist = false)
    private String[] originArray;
    /**
     * 目的地
     */
    @ApiModelProperty(value = "目的地")
    private String destination;
    /**
     * 目的地数组
     */
    @ApiModelProperty(value = "目的地数组")
    @TableField(exist = false)
    private String[] destinationArray;
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
     * 备注
     */
    private String remark;


    public String[] getOriginArray() {
        return originArray;
    }

    public void setOriginArray(String[] originArray) {
        this.originArray = originArray;
    }

    public String[] getDestinationArray() {
        return destinationArray;
    }

    public void setDestinationArray(String[] destinationArray) {
        this.destinationArray = destinationArray;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDriverId() {
        return driverId;
    }

    public void setDriverId(Integer driverId) {
        this.driverId = driverId;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "FreightRoute{" +
        ", id=" + id +
        ", driverId=" + driverId +
        ", origin=" + origin +
        ", destination=" + destination +
        ", delFlag=" + delFlag +
        ", createBy=" + createBy +
        ", createTime=" + createTime +
        ", updateBy=" + updateBy +
        ", updateTime=" + updateTime +
        ", remark=" + remark +
        "}";
    }
}
