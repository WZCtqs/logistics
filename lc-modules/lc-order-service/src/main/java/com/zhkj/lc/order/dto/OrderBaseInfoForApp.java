package com.zhkj.lc.order.dto;

import com.zhkj.lc.order.model.entity.CommonGoodsInfo;
import com.zhkj.lc.order.model.entity.OrdOrderLogistics;
import com.zhkj.lc.order.model.entity.OrdPickupArrivalAdd;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 小程序端订单数据模型（包含普货和集装箱）
 */
@Data
public class OrderBaseInfoForApp {

    //订单编号
    private String orderId;

    //订单类型
    private String orderType;

    //货品名称
    private String goodsName;

    //重量
    private String weight;

    //货物信息
    private List<String> goodsInfo;

    private List<CommonGoodsInfo> commonGoodsInfos;

    private List<OrdOrderLogistics> logistics;

    //服务方式
    private String pickGoodsWay;

    //发货时间
    private Date sendGoodsDate;

    //发货时间
    private Date returnGoodsDate;

    //订单状态状态
    private String status;

    //订单起始地
    private String ordStart;

    //目的地
    private String ordEnd;

    //订单上次状态的时间
    private Date LastStaTime;

    private String type;
    /*集装箱号*/
    private String containerNo;
    /*铅封号*/
    private String sealNumber;

    /*账单结算状态*/
    private String needPayStatus;

    private OrdPickupArrivalAdd nowAdd;

    /*收货地址信息*/
    private List<OrdPickupArrivalAdd> arrivalAdds;
    /*发货地址信息*/
    private List<OrdPickupArrivalAdd> pickupAdds;
    /**
     * 提箱地
     */
    private String pickupConPlace;
    private String pickupConDetailplace;
    /**
     * 还箱地
     */
    private String returnConPlace;
    private String returnConDetailplace;

}


