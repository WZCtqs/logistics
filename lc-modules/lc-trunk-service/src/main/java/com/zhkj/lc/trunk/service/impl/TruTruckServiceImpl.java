package com.zhkj.lc.trunk.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.zhkj.lc.common.util.BaiDuMapUtils;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.util.http.HttpClientUtil;
import com.zhkj.lc.common.vo.DriverVO;
import com.zhkj.lc.common.vo.GPSVO;
import com.zhkj.lc.common.vo.TruckVO;
import com.zhkj.lc.trunk.dto.DriverMessage;
import com.zhkj.lc.trunk.dto.GpsMessage;
import com.zhkj.lc.trunk.dto.PolylinePath;
import com.zhkj.lc.trunk.feign.OrderFeign;
import com.zhkj.lc.trunk.mapper.TruTruckMapper;
import com.zhkj.lc.trunk.model.TruTruck;
import com.zhkj.lc.trunk.model.TruTruckfile;
import com.zhkj.lc.trunk.service.ITruTruckService;
import lombok.AllArgsConstructor;
import net.sf.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.rmi.MarshalledObject;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 车辆 服务层实现
 * 
 * @author zhkj
 * @date 2018-11-20
 */
@Service
@AllArgsConstructor
public class TruTruckServiceImpl extends ServiceImpl<TruTruckMapper,TruTruck> implements ITruTruckService {
    @Autowired TruTruckMapper truTruckMapper;

    @Autowired private OrderFeign orderFeign;

    private final RedisTemplate redisTemplate;

    @Override
    public TruTruck selectTruckById(Integer id) {
        return truTruckMapper.selectTruckById(id);
    }

    @Override
    public List<TruTruck> selectPlateNumberList(TruTruck truTruck) {
        return truTruckMapper.selectPlateNumberList(truTruck);
    }

    @Override
    public List<TruTruck> selectTruckList(TruTruck truTruck) {
        return truTruckMapper.selectTruckList(truTruck);
    }

    @Override
    public TruTruck selectTruckByplateNumber(TruTruck truTruck) {
        TruTruck truTruck1 = truTruckMapper.selectTruckByplateNumber(truTruck);
        if (null != truTruck1.getTruTruckfile().getCertificateCopy()) {
            String[] a = new String[1];
            a[0] = truTruck1.getTruTruckfile().getCertificateCopy();
            truTruck1.getTruTruckfile().setCertificateCopyArr(a);
        }
        if (null != truTruck1.getTruTruckfile().getDrivingLicenseCopy()) {
            String[] a = new String[1];
            a[0] = truTruck1.getTruTruckfile().getDrivingLicenseCopy();
            truTruck1.getTruTruckfile().setDrivingLicenseCopyArr(a);
        }
        if (null != truTruck1.getTruTruckfile().getOperationCertificateCopy()) {
            String[] a = new String[1];
            a[0] = truTruck1.getTruTruckfile().getOperationCertificateCopy();
            truTruck1.getTruTruckfile().setOperationCertificateCopyArr(a);
        }
        if (null != truTruck1.getTruTruckfile().getEnregisterOriginal()) {
            String[] a = new String[1];
            a[0] = truTruck1.getTruTruckfile().getEnregisterOriginal();
            truTruck1.getTruTruckfile().setEnregisterOriginalArr(a);
        }
        if (null != truTruck1.getTruTruckfile().getVehicleRoadOriginal()) {
            String[] a = new String[1];
            a[0] = truTruck1.getTruTruckfile().getVehicleRoadOriginal();
            truTruck1.getTruTruckfile().setVehicleRoadOriginalArr(a);
        }
        return truTruck1;
    }

    @Override
    public boolean deleteTruckByIds(String ids, String updateBy) {
        String[] str = ids.split(",");
// 声明一个int类型的数组.数组长度和String类型的数组长度一致
        int[] truckIds = new int[str.length];
// 对String数组进行遍历循环，并转换成int类型的数组
        for (int i = 0; i < str.length; i++) {
            truckIds[i] = Integer.parseInt(str[i]);
        }
        TruTruck truTruck = new TruTruck();
        truTruck.setTruckIds(truckIds);
        truTruck.setUpdateTime(new Date());
        truTruck.setUpdateBy(updateBy);
        Integer result = truTruckMapper.deleteTruckByIds(truTruck);
        return null != result && result >= 1;
    }

    @Override
    public Page pageSearch(Query query, TruTruck truTruck) {
        List<TruTruck> truTrucks = truTruckMapper.pageSearch(query,truTruck);
        query.setRecords(truTrucks);
        return query;
    }

    @Override
    public Page selectTruckDriverTeamList(Query query, TruTruck truTruck) {
        List<TruTruck> truTrucks = truTruckMapper.selectTruckDriverTeamList(query,truTruck);
        List<TruTruck> truTruckList = new ArrayList<TruTruck>();
        if(truTrucks.size() > 0) {
            for (TruTruck truTruck1 : truTrucks) {
                if (null != truTruck1.getTruTruckfile()) {
                    if (null != truTruck1.getTruTruckfile().getCertificateCopy()) {
                        String[] a = new String[1];
                        a[0] = truTruck1.getTruTruckfile().getCertificateCopy();
                        truTruck1.getTruTruckfile().setCertificateCopyArr(a);
                    }
                    if (null != truTruck1.getTruTruckfile().getDrivingLicenseCopy()) {
                        String[] a = new String[1];
                        a[0] = truTruck1.getTruTruckfile().getDrivingLicenseCopy();
                        truTruck1.getTruTruckfile().setDrivingLicenseCopyArr(a);
                    }
                    if (null != truTruck1.getTruTruckfile().getOperationCertificateCopy()) {
                        String[] a = new String[1];
                        a[0] = truTruck1.getTruTruckfile().getOperationCertificateCopy();
                        truTruck1.getTruTruckfile().setOperationCertificateCopyArr(a);
                    }
                    if (null != truTruck1.getTruTruckfile().getEnregisterOriginal()) {
                        String[] a = new String[1];
                        a[0] = truTruck1.getTruTruckfile().getEnregisterOriginal();
                        truTruck1.getTruTruckfile().setEnregisterOriginalArr(a);
                    }
                    if (null != truTruck1.getTruTruckfile().getVehicleRoadOriginal()) {
                        String[] a = new String[1];
                        a[0] = truTruck1.getTruTruckfile().getVehicleRoadOriginal();
                        truTruck1.getTruTruckfile().setVehicleRoadOriginalArr(a);
                    }
                }
                truTruckList.add(truTruck1);
            }
        }
        query.setRecords(truTruckList);
        return query;
    }

    @Override
    public List<TruTruck> selectPlateNumberByTruckTeamId(TruTruck truTruck) {
        return truTruckMapper.selectPlateNumberByTruckTeamId(truTruck);
    }

    @Override
    public List<TruTruck> selectPlateNumberByAttribute(TruTruck truTruck) {
        return truTruckMapper.selectPlateNumberByAttribute(truTruck);
    }

    @Override
    public void addTruck(TruTruck truTruck) {
        truTruckMapper.addTruck(truTruck);
    }

    @Override
    public Integer countDrivers(Integer truckId) {
        return truTruckMapper.countDrivers(truckId);
    }

    @Override
    public Integer selectTruckIdBy(Integer driverId, String plateNumber) {
        return truTruckMapper.selectTruckIdBy(driverId, plateNumber);
    }

    @Override
    public List<Integer> getTruckIdListByProc(Integer tenantId){
        List<DriverVO> list = orderFeign.selectPlateNumberByProc(tenantId);
        List<Integer> listTruckId = new ArrayList<>();
        Integer truckId;
        if (null != list && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                truckId = truTruckMapper.selectTruckIdBy(list.get(i).getDriverId(), list.get(i).getPlateNumber());
                listTruckId.add(truckId);
            }
        }
        return listTruckId;
    }

    /**
     * 全部白名单车辆的gps信息(0车牌号、1司机、2手机号、3状态、4地址、5经度、6纬度)
     *
     * @param tenantId 租户
     * @return
     */
    @Override
    public String[][] allTruckGPS(Integer tenantId) {

        TruTruck truTruck = new TruTruck();
        truTruck.setTenantId(tenantId);
        truTruck.setIsTrust("0");
        /*获取所有车辆信息*/
        List<TruTruck> list = truTruckMapper.selectPlateNumberListGPS(truTruck);
        /*获取在途车辆信息ids*/
        List<Integer> listTruckId = getTruckIdListByProc(tenantId);
        /*返回数据定义*/
        String[][] truckGPS = new String[][]{};
        boolean a = false, b = false;
        if (null != list && list.size() > 0){
            a = true;
            truckGPS = new String[list.size()][7]; // 0车牌号、1司机、2手机号、3状态、4地址、5经度、6纬度
            if (null != listTruckId && listTruckId.size() > 0){
                b = true;
            }
        }
        String[] addressSplit;
        String address;
        if (a){
            for (int i = 0; i < list.size(); i ++){
                truckGPS[i][0] = list.get(i).getPlateNumber();
                truckGPS[i][1] = list.get(i).getDriverName();
                truckGPS[i][2] = list.get(i).getPhone();
                if (b && listTruckId.contains(list.get(i).getTruckId())){
                    truckGPS[i][3] = "在途";
                }else {
                    truckGPS[i][3] = "非在途";
                }
                address = getGPS(list.get(i).getPlateNumber());
                if (null != address && !"null".equals(address)){
                    addressSplit = address.split(",");
                    truckGPS[i][4] = addressSplit[2];
                    truckGPS[i][5] = addressSplit[1];
                    truckGPS[i][6] = addressSplit[0];
                }
            }
        }

        return truckGPS;
    }

    public String getGPS(String plateNumber){
        Object address = redisTemplate.opsForValue().get("GPS_ADD_DATA" + plateNumber);
        if(address != null){
            return address.toString();
        }
        String plate = URLEncoder.encode(plateNumber);
        String url = "http://171.15.132.161:8081/RoadGPS/road/getNewLngLat.do?plateNum="+plateNumber;
        String result = HttpClientUtil.doGet(url);
        System.out.println("调用GPS系统接口获取  "+plateNumber+"  的信息：  " + url);
        System.out.println(result);
        System.out.println("--------------------------------");
        //GSON直接解析成对象
        GPSVO resultBean = new Gson().fromJson(result, GPSVO.class);
        if(null != resultBean && resultBean.getPolylinePath() != null){
            String lat = resultBean.getPolylinePath().getLat();
            String lng = resultBean.getPolylinePath().getLng();
            if (null != lat && null != lng) {
                address = BaiDuMapUtils.getAddressByLatLon(lat + "," + lng);
                redisTemplate.opsForValue().set("GPS_ADD_DATA" + plateNumber, lat + "," + lng + "," + address,1800, TimeUnit.SECONDS);
                return lat + "," + lng + "," + address;
            }
        }
        return null;
    }


    @Override
    public Integer checkTruckDriverStatus(String plateNumbr, Integer tenantId) {
        return truTruckMapper.checkTruckDriverStatus(plateNumbr, tenantId);
    }

    @Override
    public Integer checkPlateNumber(String plateNumber, Integer tenantId) {
        return truTruckMapper.checkPlateNumber(plateNumber,tenantId);
    }
    @Override
    public Integer checkPlateNumberById(String plateNumber, Integer tenantId) {
        return truTruckMapper.checkPlateNumberById(plateNumber,tenantId);
    }

    @Override
    public Map<String, Object> getTruckGPS(String plateNumber) throws Exception {
        Object value = redisTemplate.opsForValue().get("GPS_TRUCK_DATA"+plateNumber);
        Map<String,Object> gps = new HashMap<>();
        if(value != null){
            return (Map)value;
        }
        PolylinePath polylinePath = new PolylinePath();
        DriverMessage driverMessage = new DriverMessage();
        // 要调用的接口方法
        String url = "http://171.15.132.161:8081/RoadGPS/road/getNewLngLat.do?plateNum="+plateNumber;
        String result = HttpClientUtil.doGet(url);
        if(result.equals("") || result == null){
            return null;
        }
        Gson gson = new Gson();
        GpsMessage gpsMessage = gson.fromJson(result, GpsMessage.class);
        if(null != gpsMessage) {
            gps.put("polylinePath",gpsMessage.getPolylinePath());
            gps.put("driverMessage",gpsMessage.getDriverMessage());
            redisTemplate.opsForValue().set("GPS_TRUCK_DATA"+plateNumber, gps, 1800, TimeUnit.SECONDS);
            return gps;
        }
        gps.put("polylinePath",polylinePath);
        gps.put("driverMessage",driverMessage);
        return gps;
    }

    @Override
    public TruTruck selectTruckByPlateNumber(String plateNumber, Integer tenantId) {
        return truTruckMapper.selectByPlateNumber(plateNumber, tenantId);
    }

    @Override
    public Boolean updateTruckStatus(TruckVO truckVO) {
        return truTruckMapper.updateTruckStatus(truckVO);
    }

}
