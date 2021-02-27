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
 * 菜单权限表
 * </p>
 *
 * @author lengleng
 * @since 2017-11-08
 */
@Data
@TableName("sys_sms_temp")
public class SysSmsTemp extends Model<SysSmsTemp> {

    private static final long serialVersionUID = 1L;

	@TableId(value = "id",type = IdType.AUTO)
    private Integer id;
	/**
	 * 系统数据
	 */
	private String sys;
    /**
     * 菜单ID
     */
	private Integer tpl_id;
	/**
	 * 菜单ID
	 */
	@TableField(value = "tpl_name")
	private String tplName;
    /**
     * 审核状态
     */
	private String check_status;
    /**
     * 模板内内容
     */
	private String tpl_content;
    /**
     * 原因
     */
	private String reason;
	/**
	 * 发送对象
	 */
	@TableField("send_obj")
	private String sendObj;
	/**
	 * 是否发送
	 */
	@TableField("is_send")
	private String isSend;
	/**
	 * 司机
	 */
	@TableField("is_send_driver")
	private String isSendDriver;

	@TableField(exist = false)
	private Boolean isDriver;
	/**
	 * 收货人
	 */
	@TableField("is_send_receice")
	private String isSendReceice;

	@TableField(exist = false)
	private Boolean isReceice;
	/**
	 * 发货人
	 */
	@TableField("is_send_picker")
	private String isSendPicker;

	@TableField(exist = false)
	private Boolean isPicker;
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
