package com.zhkj.lc.trunk.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: wchx
 * @Date: 2019/2/26 8:16
 */
@Data
public class DriverMessage implements Serializable {
    private String platenum; //车牌号
    private String drivername;// /司机
    private String driverphone;// 手机号
}
