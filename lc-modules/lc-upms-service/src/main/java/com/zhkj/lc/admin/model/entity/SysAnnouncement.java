package com.zhkj.lc.admin.model.entity;

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
import java.util.Date;

/**
 * @Description 公告实体类
 * @Author ckj
 * @Date 2019/1/3 14:52
 */
@ApiModel(description = "公告实体类")
@Data
@TableName("sys_announcement")
public class SysAnnouncement extends Model<SysAnnouncement> {

    private static final long serialVersionUID = 1L;

    /**
     * 公告id
     */
    @ApiModelProperty(value = "公告id(添加的时候不需要填写)")
    @TableId(value = "announcement_id", type = IdType.AUTO)
    private Integer announcementId;
    /**
     * 公告内容
     */
    @ApiModelProperty(value = "公告内容")
    @Excel(name = "公告内容")
    private String content;
    /**
     * 公告类型(0公共，1个人)
     */
    @ApiModelProperty(value = "公告类型(0公共，一定不要driverOwerId；1个人,个人时必须填写driverOwerId)")
    @Excel(name = "公告内容")
    private String type;
    /**
     * 公告接收人的id
     */
    @ApiModelProperty(value = "公告接收人(司机)的id")
    @TableField("driver_ower_id")
    private Integer driverOwerId;

    /**
     * 公告接收人名字
     */
    @ApiModelProperty(value = "公告接收人名字")
    @Excel(name = "公告接收人名字")
    @TableField(exist = false)
    private String driverName;

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
