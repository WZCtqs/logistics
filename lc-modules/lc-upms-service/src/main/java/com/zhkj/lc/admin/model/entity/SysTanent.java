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
 * 系统租户信息表
 * </p>
 *
 * @author cb
 * @since 2018-12-13
 */
@Data
@TableName("sys_tanent")
public class SysTanent extends Model<SysTanent> {

    private static final long serialVersionUID = 1L;

    /**
     * 租户ids
     */
    @TableId(value = "tenant_id", type = IdType.AUTO)
    private Integer tenantId;
    /**
     * 租户公司名称
     */
    @TableField("tanent_name")
    private String tanentName;
    /**
     * 租户简称
     */
    @TableField("short_name")
    private String shortName;

    /**
     * 初始密码
     */
    @TableField("old_psw")
    private String oldPsw;
    /**
     * 公司地址
     */
    private String address;

    /**
     * 手机号码
     */
    private String phone;
    /**
     * 到期时间
     */
    @TableField("expire_time")
    private Date expireTime;
    /**
     * 开通的功能模块
     */
    @TableField("menuIds")
    private String menuIds;
    /**
     * 微信公众号appid
     */
    @TableField("appid")
    private String appid;
    /**
     * 微信公众号secret
     */
    @TableField("appsecret")
    private String appsecret;
    /**
     * 微信公众号原始id
     */
    @TableField("weixin_id")
    private String weixinId;

    /**
     * 租户账号状态
     */
    private String status;
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
     * 租户对应的角色id，更新角色菜单表使用
     */
    @TableField(exist = false)
    private Integer roleId;

    @TableField(exist = false)
    private Integer isExpried;

    /**
     * 是否是合作方
     */
    @TableField("is_partner")
    private String isPartner;

    @Override
    protected Serializable pkVal() {
        return this.tenantId;
    }

    @Override
    public String toString() {
        return "SysTanent{" +
        ", tenantId=" + tenantId +
        ", tanentName=" + tanentName +
        ", address=" + address +
        ", expireTime=" + expireTime +
        ", menuIds=" + menuIds +
        ", appid=" + appid +
        ", appsecret=" + appsecret +
        ", weixinId=" + weixinId +
        ", delFlag=" + delFlag +
        ", createBy=" + createBy +
        ", createTime=" + createTime +
        ", updateBy=" + updateBy +
        ", updateTime=" + updateTime +
        "}";
    }
}
