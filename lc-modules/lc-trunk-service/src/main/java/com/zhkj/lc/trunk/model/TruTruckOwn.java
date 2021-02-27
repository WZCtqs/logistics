package com.zhkj.lc.trunk.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.zhkj.lc.common.vo.TruckVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 车辆信息表
 * </p>
 *
 * @author lzd
 * @since 2018-12-07
 */
@ApiModel(description = "车主信息实体类")
@TableName("tru_truck_own")
@Data
public class TruTruckOwn extends Model<TruTruckOwn> {

    private static final long serialVersionUID = 1L;

    /**
     * 车主id
     */
    @ApiModelProperty(value = "车主id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 车主姓名
     */
    @ApiModelProperty(value = "车主姓名")
    @TableField(value = "truckown_name")
    private String truckownName;


    @ApiModelProperty(value = "车主手机号")
    @TableField("truckown_phone")
    private String truckownPhone;

    @ApiModelProperty(value = "银行卡号")
    @TableField("bank_number")
    private String bankNumber;

    /**
     * 小程序openid
     */
    @ApiModelProperty(value = "小程序openid")
    @TableField("xopen_id")
    private String xopenId;

    /**
     * 公众号openid
     */
    @ApiModelProperty(value = "公众号openid")
    @TableField("gopen_id")
    private String gopenId;
    /**
     * 删除标志（0代表存在 2代表删除）
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
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;
    /**
     * 租户id
     */
    @ApiModelProperty(hidden = true)
    @TableField("tenant_id")
    private Integer tenantId;

    @TableField(exist = false)
    private int[] truckOwnIds;

    @TableField(exist = false)
    private List<TruckVO> truckList;

    @Override
    protected Serializable pkVal() { return this.id; }

    @Override
    public String toString() {
        return "TruTruckOwn{" +
                "id=" + id +
                ", truckownName=" + truckownName +
                ", truckownPhone='" + truckownPhone + '\'' +
                ", xopenId='" + xopenId + '\'' +
                ", gopenId='" + gopenId + '\'' +
                ", delFlag='" + delFlag + '\'' +
                ", createBy='" + createBy + '\'' +
                ", createTime=" + createTime +
                ", updateBy='" + updateBy + '\'' +
                ", updateTime=" + updateTime +
                ", remark='" + remark + '\'' +
                ", tenantId=" + tenantId +
                '}';
    }


}
