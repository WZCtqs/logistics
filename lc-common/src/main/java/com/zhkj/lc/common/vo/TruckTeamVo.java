package com.zhkj.lc.common.vo;

import com.baomidou.mybatisplus.enums.IdType;

import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.zhkj.lc.common.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * <p>
 * 车队信息表
 * </p>
 *
 * @author wzc
 * @since 2018-12-07
 */

@Data
public class TruckTeamVo implements Serializable{

    private static final long serialVersionUID = 1L;

    /**
     * 车队id
     */


    private Integer truckTeamId;
    /**
     * 车队名字
     */

    private String teamName;
    /**
     * 业务员
     */

    private String saleman;
    /**
     * 联系人
     */

    private String contact;
    /**
     * 职位
     */

    private String contactJob;
    /**
     * 联系电话
     */

    private String phone;
    /**
     * 联系人邮箱
     */

    private String email;
    /**
     * 结算方式(单付/月付)
     */

    private String payWay;
    /**
     * 是否通过（0：通过，1：不通过）
     */

    private String isPassed;
    /**
     * 是否合作（0：合作，1：非合作）
     */

    private String isPartner;
    /**
     * 车队状态
     */

    private String status;
    /**
     * 是否失信（1：失信，0：信任）
     */

    private String isTrust;
    /**
     * 车队类型(0:个体车队，1:运输车队)
     */

    private String teamType;
    /**
     * 备注
     */

    private String remark;
    /**
     * 删除标志
     */

    private String delFlag;
    /**
     * 创建者
     */

    private String createBy;
    /**
     * 创建时间
     */

    //private Date createTime;
    /**
     * 更新者
     */

    private String updateBy;
    /**
     * 更新时间
     */

   // private Date updateTime;
    /**
     * 租户id
     */

    private Integer tenantId;


    private int[] truckTeamIds;

    /**
     * 开始时间区间
     */

 //   private Date beginTime;
    /**
     * 截止时间区间
     */

   // private Date endTime;
    /**
     * 某车队车辆总数
     */

    private Integer truckSum;
    /**
     * 某车队司机总数
     */

    private Integer driverSum;



    @Override
    public String toString() {
        return "TruTruckTeam{" +
                "truckTeamId=" + truckTeamId +
                ", teamName=" + teamName +
                ", saleman=" + saleman +
                ", contact=" + contact +
                ", contactJob=" + contactJob +
                ", phone=" + phone +
                ", email=" + email +
                ", isPassed=" + isPassed +
                ", isPartner=" + isPartner +
                ", status=" + status +
                ", isTrust=" + isTrust +
                ", remark=" + remark +
                ", delFlag=" + delFlag +
                ", createBy=" + createBy +
                ", updateBy=" + updateBy +
                ", tenantId=" + tenantId +
                "}";
    }
}
