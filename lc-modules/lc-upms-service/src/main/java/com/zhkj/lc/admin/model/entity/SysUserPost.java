package com.zhkj.lc.admin.model.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 用户与岗位关联表
 * </p>
 *
 * @author cb
 * @since 2018-12-19
 */
@Data
@TableName("sys_user_post")
public class SysUserPost extends Model<SysUserPost> {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @TableId(type = IdType.INPUT)
    private Integer userId;
    /**
     * 岗位ID
     */
    @TableId(type = IdType.INPUT)
    private Integer postId;


    @TableField("tenant_id")
    private Integer tenantId;


    @Override
    protected Serializable pkVal() {
        return this.userId;
    }

    @Override
    public String toString() {
        return "SysUserPost{" +
        ", userId=" + userId +
        ", postId=" + postId +
        "}";
    }
}
