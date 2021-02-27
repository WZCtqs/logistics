package com.zhkj.lc.order.dto;

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
public class SysSmsTempVO extends Model<SysSmsTempVO> {

    private static final long serialVersionUID = 1L;

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
	private String sendObj;
	/**
	 * 是否发送
	 */
	private String isSend;
	/**
	 * 司机
	 */
	private String isSendDriver;
	/**
	 * 收货人
	 */
	private String isSendReceice;
	/**
	 * 发货人
	 */
	private String isSendPicker;

	private String updateBy;

	private Date updateTime;

	private Integer tenantId;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
