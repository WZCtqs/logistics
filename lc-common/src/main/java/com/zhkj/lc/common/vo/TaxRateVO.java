package com.zhkj.lc.common.vo;

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
public class TaxRateVO implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	/** id  */
	private Integer id;
	/** 税率 */
	private BigDecimal taxRate;

	private Date createTime;

	private String createBy;

	private Date updateTime;

	private String updateBy;

	private Integer tenantId;

	private String delFlag;

}
