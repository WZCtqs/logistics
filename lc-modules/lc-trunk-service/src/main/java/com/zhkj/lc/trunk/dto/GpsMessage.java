package com.zhkj.lc.trunk.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Auther: HP
 * @Date: 2019/6/14 11:42
 * @Description:
 */
@Data
public class GpsMessage implements Serializable {

    private DriverMessage driverMessage;

    private PolylinePath polylinePath;
}
