package com.zhkj.lc.trunk.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhkj.lc.common.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description 公告实体类
 * @Author ckj
 * @Date 2019/1/3 14:52
 */
@ApiModel(description = "公告实体类")
@Data
@TableName("announcement")
public class Announcement extends Model<Announcement> {

    private static final long serialVersionUID = 1L;

    /**
     * 公告id
     */
    @ApiModelProperty(value = "公告id(添加的时候不需要填写)")
    @TableId(value = "announcement_id", type = IdType.AUTO)
    private Integer announcementId;
    /**
     * 公告标题
     */
    @ApiModelProperty(value = "公告标题")
    @Excel(name = "公告标题")
    private String title;
    /**
     * 公告内容
     */
    @ApiModelProperty(value = "公告内容")
    @Excel(name = "公告内容")
    private String content;
    /**
     * 公告类型(0个人，1公共，2后台)
     */
    @ApiModelProperty(value = "公告类型(1公共，一定不要driverOwerId(用于小程序公告)；0个人,个人时必须填写driverOwerId(用于小程序公告);2客服(用于后台通知))")
    @Excel(name = "公告类型")
    private String type;
    /**
     * 公告接收人的id
     */
    @ApiModelProperty(value = "公告接收人(司机)的id")
    @TableField("driver_ower_id")
    private Integer driverOwerId;
    /**
     * 公告接收人的id
     */
    @ApiModelProperty(value = "公告接收人(车主)的id")
    @TableField("truck_own_id")
    private Integer truckOwnId;

    /**
     * 公告接收人名字
     */
    @ApiModelProperty(value = "公告接收人名字")
    @Excel(name = "公告接收人名字")
    @TableField(exist = false)
    private String driverName;

    /**
     * 查看状态
     */
    @ApiModelProperty(value = "查看状态(0查看，1未查看)")
    @Excel(name = "查看状态")
    private String checkout;

    /**
     * 删除标志（0存在 1删除）
     */
    @ApiModelProperty(hidden = true)
    @TableField("del_flag")
    private String delFlag;
    /**
     * 创建者
     */
    @ApiModelProperty(hidden = true)
    @TableField("create_by")
    private String createBy;
    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
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
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    @TableField("update_time")
    private Date updateTime;
    /**
     * 备注信息
     */
    @ApiModelProperty(hidden = true)
    private String remarks;
    /**
     * 租户id
     */
    @ApiModelProperty(hidden = true)
    @TableField("tenant_id")
    private Integer tenantId;

    @Override
    protected Serializable pkVal() {
        return this.announcementId;
    }
}
