package com.zhkj.lc.order.dto;

import lombok.Data;

/**
 * @Author: wchx
 * @Date: 2019/2/26 8:16
 */
@Data
public class OrderDriverMessage {
    private String platenum; //车牌号
    private String drivername;// /司机
    private String driverphone;// 手机号
}
