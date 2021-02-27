package com.zhkj.lc.admin.model.entity;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 岗位信息表
 * </p>
 *
 * @author cb
 * @since 2018-12-19
 */
@Data
@TableName("sys_post")
public class SysPost extends Model<SysPost> {

    private static final long serialVersionUID = 1L;

    /**
     * 岗位ID
     */
    @TableId(value = "post_id", type = IdType.AUTO)
    private Integer postId;
    /**
     * 岗位编码
     */
    @TableField("post_code")
    private String postCode;
    /**
     * 岗位名称
     */
    @TableField("post_name")
    private String postName;
    /**
     * 显示顺序
     */
    @TableField("post_sort")
    private Integer postSort;
    /**
     * 状态（0正常 1停用）
     */
    private String status;
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
    /**
     * 删除标志
     */
    @TableField("del_flag")
    private String delFlag;


    @TableField("tenant_id")
    private Integer tenantId;



    @Override
    protected Serializable pkVal() {
        return this.postId;
    }

    @Override
    public String toString() {
        return "SysPost{" +
        ", postId=" + postId +
        ", postCode=" + postCode +
        ", postName=" + postName +
        ", postSort=" + postSort +
        ", status=" + status +
        ", createBy=" + createBy +
        ", createTime=" + createTime +
        ", updateBy=" + updateBy +
        ", updateTime=" + updateTime +
        ", remark=" + remark +
        ", delFlag=" + delFlag +
        "}";
    }
}
