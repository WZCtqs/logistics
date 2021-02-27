package com.zhkj.lc.trunk.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: wchx
 * @Date: 2019/2/22 17:27
 */
@Data
public class PolylinePath implements Serializable {
    private String lng;            //经度
    private String lat;            //纬度
    private String speed;       //速度
    private Integer flameoutState; //发动机状态(1代表点火 0代表熄火)
}
