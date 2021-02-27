package com.zhkj.lc.trunk.model;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 车辆文件表
 * </p>
 *
 * @author wzc
 * @since 2018-12-07
 */
@Data
@TableName("tru_truckfile")
public class TruTruckfile extends Model<TruTruckfile> {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 车辆文件id
     */
    @TableField("truck_file_id")
    private Integer truckFileId;
    /**
     * 行驶证正副本复印件
     */
    @TableField("driving_license_copy")
    private String drivingLicenseCopy;
    /**
     * 合格证复印件
     */
    @TableField("certificate_copy")
    private String certificateCopy;
    /**
     * 营运证复印件
     */
    @TableField("operation_certificate_copy")
    private String operationCertificateCopy;
    /**
     * 车辆道路经营许可证正本复印件
     */
    @TableField("vehicle_road_original")
    private String vehicleRoadOriginal;


    /**
     * 登记证书原件
     */
    @TableField("enregister_original")
    private String enregisterOriginal;
    /*************路径数组*****************/
    /**
     * 车辆道路经营许可证正本复印件
     */
    @TableField(exist = false)
    private String[] vehicleRoadOriginalArr;
    /**
     * 营运证复印件
     */
    @TableField(exist = false)
    private String [] operationCertificateCopyArr;
    /**
     * 登记证书原件
     */
    @TableField(exist = false)
    private String[] enregisterOriginalArr;
    /**
     * 合格证复印件
     */
    @TableField(exist = false)
    private String [] certificateCopyArr;
    /**
     * 行驶证正副本复印件
     */
    @TableField(exist = false)
    private String [] drivingLicenseCopyArr;


   /* *//**
     * 登记证书复印件
     *//*
    @TableField("enregister_copy")
    private String enregisterCopy;
    *//**
     * 一致性证书复印件
     *//*
    @TableField("consistency_copy")
    private String consistencyCopy;
    *//**
     * 购车发票复印件
     *//*
    @TableField("buy_car_copy")
    private String buyCarCopy;
    *//**
     * 购置税发票复印件
     *//*
    @TableField("purchase_tax_bill_copy")
    private String purchaseTaxBillCopy;
    *//**
     * 购置税正本原件
     *//*
    @TableField("purchase_tax_original")
    private String purchaseTaxOriginal;
    *//**
     * 购置税正本复印件
     *//*
    @TableField("purchase_tax_copy")
    private String purchaseTaxCopy;
    *//**
     * 保险单原件
     *//*
    @TableField("insurance_original")
    private String insuranceOriginal;
    *//**
     * 保险单复印件
     *//*
    @TableField("insurance_copy")
    private String insuranceCopy;
    *//**
     * 挂靠协议原件
     *//*
    @TableField("attachment_agreement_original")
    private String attachmentAgreementOriginal;
    *//**
     * 车辆技评二维记录卡原件
     *//*
    @TableField("vehicle_technical_evaluation_original")
    private String vehicleTechnicalEvaluationOriginal;
    *//**
     * 车辆技评二维记录卡复印件
     *//*
    @TableField("vehicle_technical_evaluation_copy")
    private String vehicleTechnicalEvaluationCopy;
    *//**
     * 监测报告单原件
     *//*
    @TableField("monitoring_report_original")
    private String monitoringReportOriginal;
    *//**
     * GPS安装证明复印件
     *//*
    @TableField("gps_installation_certificate_copy")
    private String gpsInstallationCertificateCopy;
    *//**
     * GPS安装入网证明复印件
     *//*
    @TableField("gps_installation_network_certification_copy")
    private String gpsInstallationNetworkCertificationCopy;

    *//**
     * 车辆道路经营许可证副本复印件
     *//*
    @TableField("vehicle_road_copy")
    private String vehicleRoadCopy;*/
    /**
     * 删除标志（0代表存在 2代表删除）
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





    @Override
    protected Serializable pkVal() {
        return this.truckFileId;
    }

    @Override
    public String toString() {
        return "TruTruckfile{" +
        "truckFileId=" + truckFileId +
        ", certificateCopy=" + certificateCopy +
        ", enregisterOriginal=" + enregisterOriginal +
        ", operationCertificateCopy=" + operationCertificateCopy +
        ", vehicleRoadOriginal=" + vehicleRoadOriginal +
        ", delFlag=" + delFlag +
        ", createBy=" + createBy +
        ", createTime=" + createTime +
        ", updateBy=" + updateBy +
        ", updateTime=" + updateTime +
        ", remark=" + remark +
               /* ", enregisterCopy=" + enregisterCopy +
                ", consistencyCopy=" + consistencyCopy +
                ", buyCarCopy=" + buyCarCopy +
                ", purchaseTaxBillCopy=" + purchaseTaxBillCopy +
                ", purchaseTaxOriginal=" + purchaseTaxOriginal +
                ", purchaseTaxCopy=" + purchaseTaxCopy +
                ", drivingLicenseCopy=" + drivingLicenseCopy +

                ", insuranceOriginal=" + insuranceOriginal +
                ", insuranceCopy=" + insuranceCopy +
                ", attachmentAgreementOriginal=" + attachmentAgreementOriginal +
                ", vehicleTechnicalEvaluationOriginal=" + vehicleTechnicalEvaluationOriginal +
                ", vehicleTechnicalEvaluationCopy=" + vehicleTechnicalEvaluationCopy +
                ", monitoringReportOriginal=" + monitoringReportOriginal +
                ", gpsInstallationCertificateCopy=" + gpsInstallationCertificateCopy +
                ", gpsInstallationNetworkCertificationCopy=" + gpsInstallationNetworkCertificationCopy +

                ", vehicleRoadCopy=" + vehicleRoadCopy +*/
        "}";
    }
}
