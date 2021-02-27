package com.zhkj.lc.common.util;

import com.zhkj.lc.common.util.http.HttpClientUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: HP
 * @Date: 2019/1/10 15:07
 * @Description:
 */
public class BaiDuMapUtils {

    /**
     *
     * 功能描述: 百度接口 距离计算
     *
     * @param origin 起始地址信息
     * @param destination 终点地址信息
     * @return float 距离（米）
     * @auther wzc
     * @date 2019/1/10 15:09
     */
    public static float getDistances(String origin, String destination) {
        origin = origin.replaceAll(" ", "");
//        origin = origin.length() > 25 ? origin.substring(0, 25) : origin;
        destination = destination.replaceAll(" ", "");
//        destination = destination.length() > 25 ? destination.substring(0, 25)
//                : destination;
        Map<String, String> params = new HashMap<String, String>();

        String originDouble = HttpClientUtil
                .doGet("http://api.map.baidu.com/geocoder/v2/?output=json&ak=XoWiKlCuvwBwX1GFIo84zF4hQW3P88l8&address="
                        + origin);
        String desDouble = HttpClientUtil
                .doGet("http://api.map.baidu.com/geocoder/v2/?output=json&ak=XoWiKlCuvwBwX1GFIo84zF4hQW3P88l8&address="
                        + destination);
        com.alibaba.fastjson.JSONObject jsonObjectOri = com.alibaba.fastjson.JSONObject
                .parseObject(originDouble);
        com.alibaba.fastjson.JSONObject jsonObjectDes = com.alibaba.fastjson.JSONObject
                .parseObject(desDouble);
        String oriLng = jsonObjectOri.getJSONObject("result")
                .getJSONObject("location").getString("lng");// 经度值ֵ
        String oriLat = jsonObjectOri.getJSONObject("result")
                .getJSONObject("location").getString("lat");// 纬度值ֵ

        String desLng = jsonObjectDes.getJSONObject("result")
                .getJSONObject("location").getString("lng");
        String desLat = jsonObjectDes.getJSONObject("result")
                .getJSONObject("location").getString("lat");
        params.put("output", "json");
        params.put("tactics", "11");
        params.put("ak", "XoWiKlCuvwBwX1GFIo84zF4hQW3P88l8");
        params.put("origins", oriLat + "," + oriLng + "|" + oriLat + ","
                + oriLng);
        params.put("destinations", desLat + "," + desLng + "|" + desLat + ","
                + desLng);

        String result = HttpClientUtil.doGet(
                "http://api.map.baidu.com/routematrix/v2/driving", params);
        com.alibaba.fastjson.JSONArray jsonArray = com.alibaba.fastjson.JSONObject
                .parseObject(result).getJSONArray("result");
//        System.out.println(jsonArray);
        // 获取距离、米
        String distanceString = jsonArray.getJSONObject(0)
                .getJSONObject("distance").getString("value");
        float dis = Float.parseFloat(distanceString);
//        System.out.println(dis);
//        System.out.println(jsonArray.getJSONObject(0)
//                .getJSONObject("distance").getString("text"));
        return dis;
    }

    /**
     *
     * 功能描述: 百度接口 距离计算
     *
     * @param origin 起始地址信息
     * @param destination 终点地址信息
     * @return float 距离（米）
     * @auther wzc
     * @date 2019/1/10 15:19
     */
    public static String getDistance(String origin, String destination) {
        origin = origin.replaceAll(" ", "");
        destination = destination.replaceAll(" ", "");
        Map<String, String> params = new HashMap<String, String>();

        String originDouble = HttpClientUtil
                .doGet("http://api.map.baidu.com/geocoder/v2/?output=json&ak=XoWiKlCuvwBwX1GFIo84zF4hQW3P88l8&address="
                        + origin);
        String desDouble = HttpClientUtil
                .doGet("http://api.map.baidu.com/geocoder/v2/?output=json&ak=XoWiKlCuvwBwX1GFIo84zF4hQW3P88l8&address="
                        + destination);
        com.alibaba.fastjson.JSONObject jsonObjectOri = com.alibaba.fastjson.JSONObject
                .parseObject(originDouble);
        com.alibaba.fastjson.JSONObject jsonObjectDes = com.alibaba.fastjson.JSONObject
                .parseObject(desDouble);
        String oriLng = jsonObjectOri.getJSONObject("result")
                .getJSONObject("location").getString("lng");// 经度值ֵ
        String oriLat = jsonObjectOri.getJSONObject("result")
                .getJSONObject("location").getString("lat");// 纬度值ֵ

        String desLng = jsonObjectDes.getJSONObject("result")
                .getJSONObject("location").getString("lng");
        String desLat = jsonObjectDes.getJSONObject("result")
                .getJSONObject("location").getString("lat");
        params.put("output", "json");
        params.put("tactics", "11");
        params.put("ak", "XoWiKlCuvwBwX1GFIo84zF4hQW3P88l8");
        params.put("origins", oriLat + "," + oriLng + "|" + oriLat + ","
                + oriLng);
        params.put("destinations", desLat + "," + desLng + "|" + desLat + ","
                + desLng);

        String result = HttpClientUtil.doGet(
                "http://api.map.baidu.com/routematrix/v2/driving", params);
        com.alibaba.fastjson.JSONArray jsonArray = com.alibaba.fastjson.JSONObject
                .parseObject(result).getJSONArray("result");
//        System.out.println(jsonArray);
        // 获取距离、米
        String distanceString = jsonArray.getJSONObject(0)
                .getJSONObject("distance").getString("text");
        return distanceString;
    }

    /**
     * -百度接口，根据地址（中国）计算所属省市
     *
     * @param address
     * @return
     */
    public static String getProvince(String address) {

        address = address.replaceAll("[^\u4E00-\u9FA5]", "");
        Map<String, String> params1 = new HashMap<String, String>();
        String originDouble = HttpClientUtil
                .doGet("http://api.map.baidu.com/geocoder/v2/?output=json&ak=kax5nZswkVyjVqFu5ahELxh8r8ITmnG9&address="
                        + address);
        com.alibaba.fastjson.JSONObject jsonObjectOri = com.alibaba.fastjson.JSONObject.parseObject(originDouble);
        String status = jsonObjectOri.getString("status");
        if (status == "0" || "0".equals(status)) {// 解析的地址不为空时 进行值的获取
            String oriLng = jsonObjectOri.getJSONObject("result").getJSONObject("location").getString("lng");// 经度值
            String oriLat = jsonObjectOri.getJSONObject("result").getJSONObject("location").getString("lat");// 纬度值
            params1.put("output", "json");
            params1.put("ak", "kax5nZswkVyjVqFu5ahELxh8r8ITmnG9");
            params1.put("location", oriLat + "," + oriLng);
            String location = oriLat + "," + oriLng;
            String result = HttpClientUtil.doGet(
                    "http://api.map.baidu.com/geocoder/v2/?output=json&ak=kax5nZswkVyjVqFu5ahELxh8r8ITmnG9&location="
                            + location);
            com.alibaba.fastjson.JSONObject jsonObjectAdds = com.alibaba.fastjson.JSONObject.parseObject(result);
//            System.out.println("jsonObjectAdds == "+jsonObjectAdds);
            String province = jsonObjectAdds.getJSONObject("result").getJSONObject("addressComponent")
                    .getString("province");// 省
            String city = jsonObjectAdds.getJSONObject("result").getJSONObject("addressComponent").getString("city");// 市
            String district = jsonObjectAdds.getJSONObject("result").getJSONObject("addressComponent").getString("district");// 市

            return province+"/"+city+"/"+district;
        }
        return null;
    }

    public static String getAddressByLatLon(String latLon){

        String result = HttpClientUtil.doGet(
                "http://api.map.baidu.com/geocoder/v2/?output=json&ak=kax5nZswkVyjVqFu5ahELxh8r8ITmnG9&location="
                        + latLon);
        com.alibaba.fastjson.JSONObject jsonObjectAdds = com.alibaba.fastjson.JSONObject.parseObject(result);
        String address = jsonObjectAdds.getJSONObject("result").getString("formatted_address");
//        System.out.println(address);
        return address;
    }

    public static void main(String[] args){
        getAddressByLatLon("39.102778,117.572425");
    }

}
