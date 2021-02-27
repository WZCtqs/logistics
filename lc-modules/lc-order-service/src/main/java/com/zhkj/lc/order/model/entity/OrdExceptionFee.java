package com.zhkj.lc.order.model.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhkj.lc.common.annotation.Excel;
import com.zhkj.lc.common.util.support.Convert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 *  异常费用实体类
 * </p>
 *
 * @author wzc
 * @since 2019-01-11
 */
@ApiModel(description = "异常费用实体类")
@TableName("ord_exception_fee")
public class OrdExceptionFee extends Model<OrdExceptionFee> {

    private static final long serialVersionUID = 1L;

    /**
     * 订单异常主键
     */
    @ApiModelProperty(value = "订单异常id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 订单编号
     */
    @ApiModelProperty(value = "订单编号")
    @Excel(name = "订单编号")
    @TableField("order_id")
    private String orderId;
    /**
     * 司机
     */
    @ApiModelProperty(value = "司机")
    @Excel(name = "司机")
    @TableField(exist = false)
    private String driverName;
    /**
     * 司机电话
     */
    @ApiModelProperty(value = "司机电话")
    @Excel(name = "司机电话")
    @TableField(exist = false)
    private String driverPhone;
    /**
     * 订单起始地
     */
    @ApiModelProperty(value = "订单起始地")
    @Excel(name = "订单起始地")
    @TableField(exist = false)
    private String startPlace;
    /**
     * 订单终止地
     */
    @ApiModelProperty(value = "订单终止地")
    @Excel(name = "订单终止地")
    @TableField(exist = false)
    private String endPlace;
    /**
     * 异常费用类型
     */
    @ApiModelProperty(value = "异常费用类型")
    @Excel(name = "异常费用类型")
    @TableField("exception_fee_type")
    private String exceptionFeeType;

    @TableField(exist = false)
    private String type;

    /**
     * 异常费用 
     */
    @ApiModelProperty(value = "异常费用金额")
    @Excel(name = "异常费用金额")
    @TableField("exception_fee")
    private BigDecimal exceptionFee;
    /**
     * 图片凭证地址
     */
    @ApiModelProperty(value = "图片凭证地址")
    @Excel(name = "图片凭证地址")
    @TableField("img_url")
    private String imgUrl;

    /**
     * 图片文件
     */
    @ApiModelProperty(value = "图片凭证文件url数组")
    @TableField(exist = false)
    private String[] imgUrlFile;

    /**
     * 备注
     */
    @ApiModelProperty(value = "异常费用申请备注")
    @Excel(name = "异常费用申请备注")
    private String remark;

    @ApiModelProperty(value = "申请人")
    @Excel(name = "申请人")
    @TableField("apply_by")
    private String applyBy;
    /**
     * 申请时间
     */
    @ApiModelProperty(value = "申请时间")
    @Excel(name = "申请时间")
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    @TableField("apply_time")
    private Date applyTime;
    /**
     * 审核状态（0：未审核，1：通过；2：拒绝）
     */
    @ApiModelProperty(value = "审核状态（0：未审核，1：通过；2：拒绝）,默认0")
    @Excel(name = "审核状态")
    private String auditing;
    /**
     * 处理人id
     */
    @ApiModelProperty(value = "处理人id")
    private Integer transactor;
    /**
     * 处理人
     */
    @ApiModelProperty(value = "处理人")
    @Excel(name = "处理人")
    @TableField(exist = false)
    private String transactorName;
    /**
     * 处理时间
     */
    @ApiModelProperty(value = "处理时间")
    @Excel(name = "处理时间")
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    @TableField("handle_time")
    private Date handleTime;
    /**
     * 删除标识
     */
    @ApiModelProperty(hidden = true)
    @TableField("del_flag")
    private String delFlag;
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
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    @TableField("update_time")
    private Date updateTime;
    /**
     * 租户id
     */
    @ApiModelProperty(hidden = true)
    @TableField("tenant_id")
    private Integer tenantId;

    @ApiModelProperty(hidden = true)
    @TableField("order_status")
    private String orderStatus;

    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private String[] splitStrings;

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }

    public String getStartPlace() {
        if (null != startPlace){
           splitStrings =  startPlace.split("/");
           for (int i = 0; i < splitStrings.length; i++){
               if (splitStrings[i] != null && splitStrings[i].contains("市")){
                   startPlace = splitStrings[i];
                   break;
               }
           }
        }
        return startPlace;
    }

    public void setStartPlace(String startPlace) {
        this.startPlace = startPlace;
    }

    public String getEndPlace() {
        if (null != endPlace){
            splitStrings =  endPlace.split("/");
            for (int i = 0; i < splitStrings.length; i++){
                if (splitStrings[i] != null && splitStrings[i].contains("市")){
                    endPlace = splitStrings[i];
                    break;
                }
            }
        }
        return endPlace;
    }

    public void setEndPlace(String endPlace) {
        this.endPlace = endPlace;
    }

    public String getExceptionFeeType() {
        return exceptionFeeType;
    }

    public void setExceptionFeeType(String exceptionFeeType) {
        this.exceptionFeeType = exceptionFeeType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getExceptionFee() {
        return exceptionFee;
    }

    public void setExceptionFee(BigDecimal exceptionFee) {
        this.exceptionFee = exceptionFee;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getApplyBy() {
        return applyBy;
    }

    public void setApplyBy(String applyBy) {
        this.applyBy = applyBy;
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    public String getAuditing() {
        return auditing;
    }

    public void setAuditing(String auditing) {
        this.auditing = auditing;
    }

    public Integer getTransactor() {
        return transactor;
    }

    public void setTransactor(Integer transactor) {
        this.transactor = transactor;
    }

    public String getTransactorName() {
        return transactorName;
    }

    public void setTransactorName(String transactorName) {
        this.transactorName = transactorName;
    }

    public Date getHandleTime() {
        return handleTime;
    }

    public void setHandleTime(Date handleTime) {
        this.handleTime = handleTime;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
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

    public String[] getImgUrlFile() {
        if (null != imgUrl && !imgUrl.equals("")){
            imgUrlFile = Convert.toStrArray(imgUrl);
        }
        return imgUrlFile;
    }

    public void setImgUrlFile(String[] imgUrlFile) {
        this.imgUrlFile = imgUrlFile;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "OrdExceptionFee{" +
                "id=" + id +
                ", orderId='" + orderId + '\'' +
                ", driverName='" + driverName + '\'' +
                ", driverPhone='" + driverPhone + '\'' +
                ", startPlace='" + startPlace + '\'' +
                ", endPlace='" + endPlace + '\'' +
                ", exceptionFeeType='" + exceptionFeeType + '\'' +
                ", exceptionFee=" + exceptionFee +
                ", imgUrl='" + imgUrl + '\'' +
                ", imgUrlFile='" + imgUrlFile + '\'' +
                ", remark='" + remark + '\'' +
                ", applyTime=" + applyTime +
                ", auditing='" + auditing + '\'' +
                ", transactor=" + transactor +
                ", transactorName='" + transactorName + '\'' +
                ", handleTime=" + handleTime +
                ", delFlag='" + delFlag + '\'' +
                ", updateBy='" + updateBy + '\'' +
                ", updateTime=" + updateTime +
                ", tenantId=" + tenantId +
                '}';
    }
}
