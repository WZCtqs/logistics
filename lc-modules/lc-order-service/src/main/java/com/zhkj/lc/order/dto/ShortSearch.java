package com.zhkj.lc.order.dto;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhkj.lc.common.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 盘短查询
 * </p>
 *
 * @author wzc
 * @since 2018-12-19
 */
@Data
public class ShortSearch extends Model<ShortSearch> {

    private static final long serialVersionUID = 1L;

    private String ids;

    /*订单id数组*/
    private String[] orderIds;

    /*盘短订单id*/
    private String orderId;

    /*盘短类型*/
    private String shortType;

    /*业务日期*/
    private String orderDate;

    private String orderDateTo;

    /*司机id*/
    private Integer driverId;

    /*运单号*/
    private String classOrder;

    /*箱号*/
    private String containerNo;

    /*箱型*/
    private String containerType;

    /*班列日期*/
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date classDate;

    /*运输路线*/
    private String transLine;

    /*车牌号*/
    private String plateNumber;

    /*租户id*/
    private Integer tenantId;

    /*是否加入对账单*/
    private Integer isaddbill;

    @Override
    protected Serializable pkVal() {
        return this.orderId;
    }
}
