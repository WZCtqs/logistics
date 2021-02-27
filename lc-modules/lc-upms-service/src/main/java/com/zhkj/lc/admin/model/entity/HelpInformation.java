package com.zhkj.lc.admin.model.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author yl
 * @since 2019-02-19
 */
@Data
@TableName("help_information")
public class HelpInformation extends Model<HelpInformation> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "information_id",type = IdType.AUTO)
    private Integer informationId;
    /**
     * 删除标志（0代表存在 1代表删除）
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
    private String remarks;
    /**
     * 租户id
     */
    @TableField("tenant_id")
    private Integer tenantId;
    /**
     * 帮助信息标题
     */
    @TableField("title")
    private String title;
    /**
     * 内容
     */
    @TableField("content")
    private String content;
    /**
     * 类型
     */
    @TableField("type")
    private String type;

    @TableField(exist = false)
    private String[] informationIds;

    @Override
    protected Serializable pkVal() {
        return this.informationId;
    }

    @Override
    public String toString() {
        return "HelpInformation{" +
        ", informationId=" + informationId +
        ", delFlag=" + delFlag +
        ", createBy=" + createBy +
        ", createTime=" + createTime +
        ", updateBy=" + updateBy +
        ", updateTime=" + updateTime +
        ", remarks=" + remarks +
        ", tenantId=" + tenantId +
        ", title=" + title +
        ", content=" + content +
        ", type=" + type +
        "}";
    }
}
