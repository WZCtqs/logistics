package com.zhkj.lc.trunk.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.util.support.Convert;
import com.zhkj.lc.common.vo.DriverVO;
import com.zhkj.lc.trunk.feign.OilCardFeign;
import com.zhkj.lc.trunk.mapper.TruDriverMapper;
import com.zhkj.lc.trunk.mapper.TruTruckMapper;
import com.zhkj.lc.trunk.mapper.TruTruckTeamMapper;
import com.zhkj.lc.trunk.model.*;
import com.zhkj.lc.trunk.service.ITruDriverService;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 司机 服务层实现
 * 
 * @author zhkj
 * @date 2018-11-20
 */
@Service
public class TruDriverServiceImpl extends ServiceImpl<TruDriverMapper,TruDriver> implements ITruDriverService {

    @Autowired private TruDriverMapper truDriverMapper;
    @Autowired private TruTruckMapper truTruckMapper;
    @Autowired private TruTruckTeamMapper truTruckTeamMapper;
    @Autowired private OilCardFeign oilCardFeign;

    /**
     * 连表查询车主信息
     * @return
     */
    @Override
    public Page selectDriverTruck(Query<Object> objectQuery,EntityWrapper<Object> wrapper) {
        objectQuery.setRecords(truDriverMapper.selectDriverTruck(objectQuery, objectQuery.getCondition()));
        return objectQuery;
    }

    /**
     * 根据司机手机号和小程序密码(xopenId)获取司机信息
     * @param
     */
    @Override
    public TruDriver selectDriverByPhoneXopenId(String phone,String xopenId){
        return truDriverMapper.selectDriverByPhoneXopenId(phone,xopenId);
    }

    /**
     * 根据司机姓名查询司机信息
     * @param
     * @return
     */
    @Override
    public TruDriver selectDriverByName(TruDriver truDriver) {
        return truDriverMapper.selectDriverByName(truDriver);
    }

    @Override
    public List<TruDriver> selectAllDriver(TruDriver truDriver) {
        return truDriverMapper.selectAllDriver(truDriver);
    }

    @Override
    public boolean deleteDriverByIds(String ids) {
        String[] str = ids.split(",");
        // 声明一个int类型的数组.数组长度和String类型的数组长度一致
        int[] driverIds = new int[str.length];
        // 对String数组进行遍历循环，并转换成int类型的数组
        for (int i = 0; i < str.length; i++) {
            driverIds[i] = Integer.parseInt(str[i]);
        }
        TruDriver truDriver = new TruDriver();
        truDriver.setDriverIds(driverIds);
        truDriver.setUpdateTime(new Date());
        /*oilApply.setUpdateBy("");*/
        Integer result = truDriverMapper.deleteDriverByIds(truDriver);
        return null != result && result >= 1;
    }

    @Override
    public List<TruDriver> selectDriverPlateNumber(TruDriver truDriver) {
        return truDriverMapper.selectDriverPlateNumber(truDriver);
    }

    @Override
    public List<TruDriver> selectByIds(String ids) {
        String[] str = ids.split(",");
        // 声明一个int类型的数组.数组长度和String类型的数组长度一致
        int[] driverIds = new int[str.length];
        // 对String数组进行遍历循环，并转换成int类型的数组
        for (int i = 0; i < str.length; i++) {
            driverIds[i] = Integer.parseInt(str[i]);
        }
        TruDriver truDriver = new TruDriver();
        truDriver.setDriverIds(driverIds);
        return truDriverMapper.selectByIds(truDriver);
    }

    @Override
    public List<TruDriver> selectDriverByplateId(TruDriver truDriver) {
        return truDriverMapper.selectDriverByplateId(truDriver);
    }

    @Override
    public int checkDriverTruck(String driverName, String plateNumber, String tenantId) {
        return truDriverMapper.checkDriverTruck(driverName,plateNumber, tenantId);
    }

    @Override
    public int addDriver(TruDriver truDriver) {
        return truDriverMapper.addDriver(truDriver);
    }

    @Override
    public Page selectDriverDetailList(Query query, TruDriver truDriver) {
        List<TruDriver> truDrivers = truDriverMapper.selectDriverDetailList(query,truDriver);
        if(truDrivers.size() > 0){
            List<TruDriver> truDriverList = new ArrayList<TruDriver>();
           for (TruDriver truDriver1:truDrivers) {
               truDriver1.setOilCardNum(oilCardFeign.driversCardNum(truDriver1.getDriverId(),truDriver.getTenantId()));

               if (null != truDriver1.getLicensePhoto()){
                   String[] a = new String[1];
                   a[0] = truDriver1.getLicensePhoto();
                   truDriver1.setLicensePhotoArr(a);
               }
               if (null != truDriver1.getIdcardPhotoUp()){
                   String[] a = new String[1];
                   a[0] = truDriver1.getIdcardPhotoUp();
                   truDriver1.setIdcardPhotoUpArr(a);
               }
               if (null != truDriver1.getIdcardPhotoDown()){
                   String[] a = new String[1];
                   a[0] = truDriver1.getIdcardPhotoDown();
                   truDriver1.setIdcardPhotoDownArr(a);
               }
               if (null != truDriver1.getQualification()){
                   String[] a = new String[1];
                   a[0] = truDriver1.getQualification();
                   truDriver1.setQualificationArr(a);
               }
                if (truDriver1.getFreightRouteList().size() > 0){
                    FreightRoute[] freightRoutes = new FreightRoute[truDriver1.getFreightRouteList().size()];
                   /* truDriver1.getFreightRouteList().toArray(freightRoutes);
                    truDriver1.setFreightRoute(freightRoutes);*/
                    for (int i = 0; i< truDriver1.getFreightRouteList().size(); i++) {
                        if (truDriver1.getFreightRouteList().get(i).getOrigin() != null && ! truDriver1.getFreightRouteList().get(i).getOrigin().equals("")) {
                            truDriver1.getFreightRouteList().get(i).setOriginArray(truDriver1.getFreightRouteList().get(i).getOrigin().split("/"));
                        }
                        if (truDriver1.getFreightRouteList().get(i).getDestination() != null && ! truDriver1.getFreightRouteList().get(i).getDestination().equals("")) {
                            truDriver1.getFreightRouteList().get(i).setDestinationArray(truDriver1.getFreightRouteList().get(i).getDestination().split("/"));
                        }
                        freightRoutes[i] =  truDriver1.getFreightRouteList().get(i);
                    }
                    truDriver1.setFreightRoute( freightRoutes);
            }
               truDriverList.add(truDriver1);
           }
            query.setRecords(truDriverList);
            return query;
        }
        query.setRecords(truDrivers);
        return query;
    }

    @Override
    public List<TruDriver> selectDriverPhone(TruDriver truDriver) {
        return truDriverMapper.selectDriverPhone(truDriver);
    }

    @Override
    public List<DriverVO> getZFDriverInfo(String zfId) {
        String []zfid = Convert.toStrArray(zfId);
        return truDriverMapper.selectZFDriverVo(zfid);
    }

    @Override
    public TruDriver selectDriverDetailById(TruDriver truDriver) {
        return truDriverMapper.selectDriverDetailById(truDriver);
    }

    @Override
    public TruDriver selectDriverBygopenId(String gopenId){
        return truDriverMapper.selectDriverBygopenId(gopenId);
    }

    @Override
    public List<TruDriver> selectDriverByWhiteList(TruDriver truDriver) {
        return truDriverMapper.selectDriverByWhiteList(truDriver);
    }

    @Override
    public Boolean updateDriverStatus(DriverVO driverVO) {
        try {
            truDriverMapper.updateStatus(driverVO);
            return true;
        }catch (Exception e){

            return false;

        }
    }

    @Override
    public List<TruDriver> selectDriverByTruck(TruDriver truDriver) {
        return truDriverMapper.selectDriverByTruck(truDriver);
    }

    @Override
    public String getDriverPayType(Integer driverId, Integer tenantId) {
        return truDriverMapper.getDriverPayType(driverId, tenantId);
    }

    @Override
    public List<DriverVO> getDriverTeamType(String teamType, Integer tenantId) {
        return truDriverMapper.getDriverTeamType(teamType,tenantId);
    }

    @Override
    public List<DriverVO> selectDriverByPlateNumber(Integer plateId, Integer tenantId) {
        return truDriverMapper.selectDriverByPlateNumber(plateId,tenantId);
    }

    @Override
    public DriverVO selectDriverByDriverId(Integer driverId, Integer tenantId) {
        return truDriverMapper.selectDriverByDriverId(driverId,tenantId);
    }

    @Override
    public List<TruDriver> selectAllDriverByTenantId(TruDriver truDriver) {
        return truDriverMapper.selectAllDriverByTenantId(truDriver);
    }

    @Override
    public DriverVO selectDriverStatus(Integer driverId, Integer tenantId){
        return truDriverMapper.selectDriverStatus(driverId, tenantId);
    }

    @Override
    public List<TruDriver> selectDriverStatusByTruckId(Integer truckId) {
        return truDriverMapper.selectDriverStatusByTruckId(truckId);
    }

    @Override
    public List<TruDriver> selectDriverStatusByPlateNumber(String plateNumber) {
        return truDriverMapper.selectDriverStatusByPlateNumber(plateNumber);
    }

    @Override
    public TruDriver selectDriverBaseColumn(TruDriver truDriver) {
        return truDriverMapper.selectDriverBaseColumn(truDriver);
    }

    @Override
    public Boolean findPhone(String phone) {
        List<DriverVO> driverVOList = truDriverMapper.findPhone(phone);
        if(driverVOList.size()==0){
            return true;
        }else {
            return false;
        }

    }
}