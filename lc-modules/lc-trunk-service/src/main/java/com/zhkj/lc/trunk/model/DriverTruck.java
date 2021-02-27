package com.zhkj.lc.trunk.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 司机车主关联表
 * </p>
 */
@Data
public class DriverTruck implements Serializable {
    /**
     * 司机id
     */
    @TableId(value = "driver_id", type = IdType.AUTO)
    private Integer driverId;
    /**
     * 司机姓名
     */
    @TableField("driver_name")
    private String driverName;
    /**
     * 司机状态
     */
    private String status;
    /**
     * 是否是车主
     */
    @TableField("is_owner")
    private String isOwner;
    /**
     * 年龄
     */
    @TableField("driver_age")
    private Integer driverAge;
    /**
     * 身份证号
     */
    @TableField("idcard_number")
    private String idcardNumber;
    /**
     * 身份证照片（正）
     */
    @TableField("idcard_photo_up")
    private String idcardPhotoUp;
    /**
     * 身份证照片（反）
     */
    @TableField("idcard_photo_down")
    private String idcardPhotoDown;
    /**
     * 司机手机号
     */
    private String phone;
    /**
     * 驾证级别
     */
    @TableField("license_level")
    private String licenseLevel;
    /**
     * 驾龄
     */
    @TableField("drive_years")
    private Integer driveYears;
    /**
     * 驾证照片
     */
    @TableField("license_photo")
    private String licensePhoto;
    /**
     * 从业资格证
     */
    private String qualification;
    /**
     * 驾驶证审验日期
     */
    @TableField("licenseLevel_time")
    private Date licenseLevelTime;
    /**
     * 从业证审验日期
     */
    @TableField("qualification_time")
    private Date qualificationTime;
    /**
     * 所属银行
     */
    @TableField("deposit_bank")
    private String depositBank;
    /**
     * 银行卡号
     */
    @TableField("bank_number")
    private String bankNumber;
    /**
     * 车辆id
     */
    @TableField("plate_id")
    private Integer plateId;
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
     * 备注
     */
    private String remark;
    /**
     * 租户id
     */
    @TableField("tenant_id")
    private Integer tenantId;

    /**
     * 车辆id
     */
    @TableField(value = "truck_id")
    private Integer truckId;
    /**
     * 车牌号
     */
    @TableField("plate_number")
    private String plateNumber;
    /**
     * 车主
     */
    @TableField("truck_owner")
    private String truckOwner;
    /**
     * 车主手机号
     */
    @TableField("truck_owner_phone")
    private String truckOwnerPhone;

    /**
     * 开始时间区间
     */
    @TableField(exist = false)
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date beginTime;
    /**
     * 截止时间区间
     */
    @TableField(exist = false)
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date endTime;

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
