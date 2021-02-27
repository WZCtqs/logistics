package com.zhkj.lc.order.model.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import lombok.Data;

import java.util.Date;

@Data
public class GpsOrder {
    private String uuid;
    private String compname;//物流公司
    private String drivername;//司机
    private String driverphone; //手机
    private String proxynum;//委托书编号
    private String platenum;//车牌号
    private String idCard;//司机身份唯一标识
    private String ordernum;//订单号（之前是箱行亚欧的订单号，但是系统也没用到，建议不用传）
    private String boxtype;//箱型
    private String goodname;//货物名字
    private String boxnum;//箱号
    private String getboxtime;//提箱/提货完成时间
    private String gocome;//去回程/(0去1回)
    private String destination;//目的地 去程是陆港，回程是卸货地
    private String topickupgoods;//去程提货地（去程时的目的地）
    private Date getordertime;//订单接单时间（派车时间）
    private String returnboxadress;//还箱地
    private String getboxtimeadress;//提箱/提货完成时间的地址
    private String getgoodtime;//提卸货完成时间
    private String getgoodtimeadress;//提卸货地点
    private String arrivetime;//到达装卸货地时间
    private String arrivetimeadress;//到达装卸货地时间的地址
    private String upstreamId;
    private String destinationtime;//到达陆港/还箱完成时间
    private String backboxadress;//还箱地点
    private String getboxtimeadresslat;//GPS纬度
    private String getboxtimeadresslon; ////GPS经度
    private String arrivetimeadresslat;
    private String arrivetimeadresslon;
    private String getgoodtimeadresslat;
    private String getgoodtimeadresslon;
    private String backboxadresslat;
    private String backboxadresslon;

    private Integer containerNum;//箱量
    private String weight;//货重
    private String size;//体积尺寸
    private String consignor;//发货人
    private String consignorPhone;//发货人联系方式
    private String consignee;//收货人
    private String consigneePhone;//收货人联系方式
    private String scheduleman;//调度员
    private Integer kilometre;//公里
    private String sealNumber;//铅封号
    private String classDate;//班列日期

}
