package com.zhkj.lc.oilcard.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  主卡基础表
 * </p>
 *
 * @author wzc
 * @since 2019-02-11
 */
@ApiModel(description = "主卡基础信息")
@TableName("oil_major")
public class OilMajor extends Model<OilMajor> {

    private static final long serialVersionUID = 1L;

	public OilMajor() {
	}

	public OilMajor(String majorNumber, String majorCompany, Integer tenantId,String majorName) {
		this.majorNumber = majorNumber;
		this.majorCompany = majorCompany;
		this.tenantId = tenantId;
		this.majorName = majorName;
	}

	@TableField(exist = false)
	private List<OilCard> oilCards;

	public List<OilCard> getOilCards() {
		return oilCards;
	}

	public void setOilCards(List<OilCard> oilCards) {
		this.oilCards = oilCards;
	}

	/**
     * 主卡id
     */
    @ApiModelProperty(value = "主卡id")
    @TableId(value = "major_id", type = IdType.AUTO)
    private Integer majorId;
    /**
     * 主卡名
     */
    @ApiModelProperty(value = "主卡名")
    @TableField("major_name")
    private String majorName;
    /**
     * 主卡号
     */
    @ApiModelProperty(value = "主卡号")
    @TableField("major_number")
    private String majorNumber;
    /**
     * 归属公司
     */
    @ApiModelProperty(value = "主卡归属公司")
    @TableField("major_company")
    private String majorCompany;
    /**
     * 主卡下油卡数量
     */
    @ApiModelProperty(value = "附属油卡数量")
    @TableField("card_num")
    private Integer cardNum;
    /**
     * 删除标志（0代表存在 1代表删除）
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
     * 租户id
     */
    @ApiModelProperty(value = "租户id")
    @TableField("tenant_id")
    private Integer tenantId;
    /**
     * 备注
     */
    @ApiModelProperty(hidden = true)
    private String remark;


    public Integer getMajorId() {
        return majorId;
    }

    public void setMajorId(Integer majorId) {
        this.majorId = majorId;
    }

    public String getMajorName() {
        return majorName;
    }

    public void setMajorName(String majorName) {
        this.majorName = majorName;
    }

    public String getMajorNumber() {
        return majorNumber;
    }

    public void setMajorNumber(String majorNumber) {
        this.majorNumber = majorNumber;
    }

    public String getMajorCompany() {
        return majorCompany;
    }

    public void setMajorCompany(String majorCompany) {
        this.majorCompany = majorCompany;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getTenantId() {
        return tenantId;
    }

    public void setTenantId(Integer tenantId) {
        this.tenantId = tenantId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getCardNum() {
        return cardNum;
    }

    public void setCardNum(Integer cardNum) {
        this.cardNum = cardNum;
    }

    @Override
    protected Serializable pkVal() {
        return this.majorId;
    }

    @Override
    public String toString() {
        return "OilMajor{" +
        ", majorId=" + majorId +
        ", majorName=" + majorName +
        ", majorNumber=" + majorNumber +
        ", majorCompany=" + majorCompany +
        ", cardNum=" + cardNum +
        ", delFlag=" + delFlag +
        ", createBy=" + createBy +
        ", createTime=" + createTime +
        ", updateBy=" + updateBy +
        ", updateTime=" + updateTime +
        ", tenantId=" + tenantId +
        ", remark=" + remark +
        "}";
    }
}
