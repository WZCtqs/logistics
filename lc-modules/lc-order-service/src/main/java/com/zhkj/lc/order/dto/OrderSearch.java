package com.zhkj.lc.order.dto;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.zhkj.lc.common.annotation.Excel;
import com.zhkj.lc.common.vo.CustomerVO;
import com.zhkj.lc.common.vo.DriverVO;
import com.zhkj.lc.order.model.entity.OrdOrderFile;
import com.zhkj.lc.order.model.entity.OrdOrderLogistics;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 订单管理
 * </p>
 *
 * @author wzc
 * @since 2018-12-07
 */
@Data
public class OrderSearch extends Model<OrderSearch> {

    private static final long serialVersionUID = 1L;

    /*订单id数组*/
    private String[] orderIds;

    /*接受前台id数组传值*/
    private String ids;
    /*订单编号*/
    private String orderId;
    /*订单类型*/
    private String type;
    /*订单状态*/
    private String status;
    /*客户名称*/
    private String customerName;
    /*客户ids*/
    private Integer[] customerIds;
    /*班列日期*/
    private Date classDate;
    /*派车日期*/
    private Date sendTruckDate;
    /*派车日期开始*/
    private String fromDate;
    /*派车日期结束*/
    private String toDate;
    /*集装箱号*/
    private String containerNo;
    /*司机id*/
    private Integer driverId;
    /*司机手机号*/
    private String driverPhone;
    /*司机姓名*/
    private String driverName;
    /*车牌号*/
    private String plateNumber;
    /*车辆类型*/
    private String truckAttribute;
    /*租户id*/
    private Integer tenantId;
    /*车牌号集合*/
    private String[] plates;
    /*提货地*/
    private String pickupGoodsPlace;
    /*送货地*/
    private String sendGoodsPlace;

    /*仓位号*/
    private String classOrder;

    /**创建者*/
    private String createBy;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}
