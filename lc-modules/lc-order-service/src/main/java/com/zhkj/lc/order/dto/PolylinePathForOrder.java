package com.zhkj.lc.order.dto;

import lombok.Data;

import java.util.Date;

/**
 * @Author: wchx
 * @Date: 2019/2/22 17:27
 */
@Data
public class PolylinePathForOrder {
    private String receivetime;       //定位时间
    private String lat;            //纬度
    private String lon;            //经度
    private String speed;       //速度
    private Integer flameoutState; //发动机状态(1代表点火 0代表熄火)
}
