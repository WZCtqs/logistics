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
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 合同管理
 * </p>
 *
 * @author wzc
 * @since 2018-12-07
 */
@Data
@ApiModel(description = "合同信息实体类")
@TableName("contract")
public class Contract extends Model<Contract> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    /**
     * 合同编号
     */
    @ApiModelProperty(value = "合同编号")
    @Excel(name="合同编号")
    @TableField("contract_number")
    private String contractNumber;
    /**
     * 合同名称
     */
    @ApiModelProperty(value = "合同名称")
    @Excel(name="合同名称")
    @TableField("contract_name")
    private String contractName;
    /**
     * 合同类型
     */
    @ApiModelProperty(value = "合同类型")
    @TableField("contract_type")
    private String contractType;
    /**
     * 客户id
     */
    @ApiModelProperty(value = "客户id")
    @TableField("customer_id")
    private Integer customerId;

    /**
     * 客户名称
     */
    @ApiModelProperty(value = "客户名称")
    @TableField( exist = false)
    private String customerName;

    /**
     * 客户状态
     */
    @ApiModelProperty(value = "客户状态")
    @TableField( exist = false)
    private String customerIsTrust;

    /**
     * 联系人
     */
    @ApiModelProperty(value = "联系人")
    @Excel(name="联系人")
    private String contact;
    /**
     * 联系方式
     */
    @ApiModelProperty(value = "联系方式")
    @Excel(name="联系方式")
    private String phone;
    /**
     * 业务员
     */
    @ApiModelProperty(value = "业务员")
    @Excel(name="业务员")
    private String saleman;
    /**
     * 签订时间
     */
    @ApiModelProperty(value = "签订时间")
    @Excel(name="签订时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @TableField("sign_date")
    private Date signDate;
    /**
     * 到期时间
     */
    @ApiModelProperty(value = "到期时间")
    @Excel(name="到期时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @TableField("expiry_date")
    private Date expiryDate;
    /**
     * 提醒时间
     */
    @ApiModelProperty(value = "提醒时间",hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @TableField("remind_date")
    private Date remindDate;
    /**
     * 合同状态
     */
    @ApiModelProperty(value = "合同状态")
    @Excel(name="合同状态")
    private String status;
    /**
     * 合同文件
     */
    @ApiModelProperty(value = "合同文件")
    @TableField("contract_file")
    private String contractFile;

    @TableField(exist = false)
    private String[] files;

    /**
     * 开始时间区间
     */
    @ApiModelProperty(value = "开始时间区间")
    @TableField(exist = false)
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date beginTime;
    /**
     * 截止时间区间
     */
    @ApiModelProperty(value = "截止时间区间")
    @TableField(exist = false)
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date endTime;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    @Excel(name="备注")
    private String remark;

    /**
     * 删除标志
     */
    @ApiModelProperty(hidden = true)
    @TableField("del_flag")
    private String delFlag;
    /**
     * 创建者
     */
    @ApiModelProperty(value = "创建者")
    @TableField("create_by")
    private String createBy;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
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
     * 租户id
     */
    @ApiModelProperty(hidden = true)
    @TableField("tenant_id")
    private Integer tenantId;

    /**
     * 客户对象
     */
    @ApiModelProperty(value = "客户对象")
    @TableField(exist = false)
    private TruCustomer truCustomer;

    @TableField(exist = false)
    private String[] contractNumbers;

    @Override
    protected Serializable pkVal() {
        return this.contractNumber;
    }

    @Override
    public String toString() {
        return "Contract{" +
        "contractNumber=" + contractNumber +
        ", contractName=" + contractName +
        ", contractType=" + contractType +
        ", customerId=" + customerId +
        ", contact=" + contact +
        ", saleman=" + saleman +
        ", signDate=" + signDate +
        ", expiryDate=" + expiryDate +
        ", remindDate=" + remindDate +
        ", status=" + status +
        ", contractFile=" + contractFile +
        ", remark=" + remark +
        ", delFlag=" + delFlag +
        ", createBy=" + createBy +
        ", createTime=" + createTime +
        ", updateBy=" + updateBy +
        ", updateTime=" + updateTime +
        "}";
    }
}