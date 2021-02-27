package com.zhkj.lc.trunk.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 税率表 sys_tax_rate
 * 
 * @author tqs
 * @date 2019-05-06
 */
@Data
@TableName(value = "tax_rate")
public class TaxRate implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;
	/**
	 * 税率
	 */
	@TableField("tax_rate")
	private BigDecimal taxRate;

	@TableField("create_time")
	private Date createTime;

	@TableField("create_by")
	private String createBy;

	@TableField("update_time")
	private Date updateTime;

	@TableField("update_by")
	private String updateBy;

	@TableField("tenant_id")
	private Integer tenantId;

	@TableField("del_flag")
	private String delFlag;

	public TaxRate(Integer tenantId) {
		this.tenantId = tenantId;
	}

	public TaxRate() {
	}

	@Override
	public String toString() {
		return "TaxRate{" +
				"id=" + id +
				", taxRate=" + taxRate +
				", createTime=" + createTime +
				", createBy='" + createBy + '\'' +
				", updateTime=" + updateTime +
				", updateBy='" + updateBy + '\'' +
				", tenantId=" + tenantId +
				", delFlag='" + delFlag + '\'' +
				'}';
	}
}