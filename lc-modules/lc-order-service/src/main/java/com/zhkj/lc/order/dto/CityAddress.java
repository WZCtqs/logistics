package com.zhkj.lc.order.dto;

import lombok.Data;

import java.util.List;

/**
 * @Auther: HP
 * @Date: 2019/1/23 16:07
 * @Description:
 */
@Data
public class CityAddress {

    private String stauts;
    private String message;
    private List<ResultDetail> result;
    @Data
    public class ResultDetail {
        /*地址名称*/
        private String name;

        private Location location;

        @Data
        public class Location{
            private String lat;
            private String lng;
        }

        /*uid*/
        private String uid;

        /*所属省*/
        private String province;

        /*所属省市*/
        private String city;

        /*所属省市区*/
        private String district;

        /*商业*/
        private String business;

        /*城市code*/
        private String cityid;
    }
}
