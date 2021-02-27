package com.zhkj.lc.oilcard.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.zhkj.lc.common.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description TODO
 * @Author ckj
 * @Date 2019/3/2 14:49
 */
@ApiModel(description = "公司积分余额")
@Data
@TableName("point_amount")
public class PointAmount extends Model<PointAmount> {

    @ApiModelProperty(value = "积分id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 年月份
     */
    @ApiModelProperty(value = "年月")
    @TableField(value = "year_month")
    private String yearMonth;
    /**
     * 归属公司
     */
    @Excel(name = "公司名称")
    @ApiModelProperty(value = "公司名称")
    private String company;
    /**
     * 上月余额
     */
    @Excel(name = "上月余额")
    @ApiModelProperty(value = "上月余额")
    @TableField("last_point")
    private BigDecimal lastPoint;
    /**
     * 本月新增
     */
    @Excel(name = "本月新增")
    @ApiModelProperty(value = "本月新增")
    @TableField("add_point")
    private BigDecimal addPoint;
    /**
     * 本月合计分配
     */
    @Excel(name = "公司名称")
    @ApiModelProperty(value = "本月合计分配")
    @TableField("distribute_point")
    private BigDecimal distributePoint;
    /**
     * 本月余额
     */
    @Excel(name = "本月余额")
    @ApiModelProperty(value = "本月余额")
    @TableField(exist = false)
    private BigDecimal pointAmount;

    /**
     * 备注
     */
    @Excel(name = "备注")
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 删除标志（0存在 1删除）
     */
    @ApiModelProperty(hidden = true)
    @TableField("del_flag")
    private String delFlag;
    /**
     * 创建者
     */
    @ApiModelProperty(value = "创建者")
    @TableField("create_by")
    private String createBy;
    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    @TableField("create_time")
    private Date createTime;
    /**
     * 更新者
     */
    @ApiModelProperty(hidden = true)
    @TableField("update_by")
    private String updateBy;
    /**
     * 更新时间
     */
    @ApiModelProperty(hidden = true)
    @TableField("update_time")
    private Date updateTime;
    /**
     * 租户id
     */
    @ApiModelProperty(value = "租户id")
    @TableField("tenant_id")
    private Integer tenantId;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
