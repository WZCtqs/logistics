package com.zhkj.lc.common.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Auther: HP
 * @Date: 2019/2/27 09:49
 * @Description:
 */
@Data
public class GPSVO {

    private DriverMes driverMes;

    List<Gps> polylinePathlist;

    Gps polylinePath;

    @Data
    public class DriverMes{
        private String id;
        private String account;
        private String drivername;
        private String platenum;
        private String state;
        private String bindtime;
        private String bindlongtime;
        private String rbindtime;
        private String rbindlongtime;
        private String imei;
        private String cdBindState;
        private String cpBindState;
        private String cdBindTime;
        private String cpBindTime;
        private String driverphone;
    }

    @Data
    public class Gps{
        private Date receivetime;
        private String lat;
        private String lon;
        private String lng;
        private String speed;
        private String flameoutState;
    }

}
