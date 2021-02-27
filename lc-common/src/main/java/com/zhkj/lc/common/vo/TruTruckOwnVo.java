package com.zhkj.lc.common.vo;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
@Data
public class TruTruckOwnVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 车主id
     */
    private Integer id;

    /**
     * 车主姓名
     */
    private String truckownName;


    private String truckownPhone;

    /**
     * 小程序openid
     */
    private String xopenId;

    /**
     * 公众号openid
     */
    private String gopenId;
    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;
    /**
     * 创建者
     */
    private String createBy;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新者
     */
    private String updateBy;
    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;
    /**
     * 租户id
     */
    private Integer tenantId;

    private List<TruckVO> truckList;

}
