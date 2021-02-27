package com.zhkj.lc.trunk.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.vo.DriverVO;
import com.zhkj.lc.trunk.model.TruDriver;
import com.zhkj.lc.trunk.model.TruTruck;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 司机 服务层
 * 
 * @author zhkj
 * @date 2018-11-20
 */
public interface ITruDriverService extends IService<TruDriver> {

    /**
     * 连表查询车主信息
     * @return
     */
    public Page selectDriverTruck(Query<Object> objectQuery,EntityWrapper<Object> objectEntityWrapper);

    public TruDriver selectDriverByName(TruDriver truDriver);

    public TruDriver selectDriverByPhoneXopenId(String phone,String xopenId);

    public List<TruDriver> selectAllDriver(TruDriver truDriver);

    public boolean deleteDriverByIds(String ids);

    public List<TruDriver> selectDriverPlateNumber(TruDriver truDriver);

    public List<TruDriver> selectByIds(String ids);

    public List<TruDriver> selectDriverByplateId(TruDriver truDriver);

    public int checkDriverTruck(String driverName, String plateNumber, String tenantId);

    public int addDriver(TruDriver truDriver);

    public Page selectDriverDetailList(Query query,TruDriver truDriver);

    List<TruDriver> selectDriverPhone(TruDriver truDriver);

    List<DriverVO> getZFDriverInfo(String zfId);

    TruDriver selectDriverDetailById(TruDriver truDriver);

    //根据openid查询司机信息（gopenId）
    TruDriver selectDriverBygopenId(String gopenId);

    List<TruDriver> selectDriverByWhiteList(TruDriver truDriver);

    Boolean updateDriverStatus(DriverVO driverVO);

    List<TruDriver> selectDriverByTruck(TruDriver truDriver);

    String getDriverPayType(Integer driverId, Integer tenantId);

    List<DriverVO> getDriverTeamType(String teamType,Integer tenantId);

    List<DriverVO> selectDriverByPlateNumber(Integer plateId,Integer tenantId);

    DriverVO selectDriverByDriverId(Integer driverId,Integer tenantId);

    List<TruDriver> selectAllDriverByTenantId(TruDriver truDriver);

    DriverVO selectDriverStatus(Integer driverId, Integer tenantId);

    List<TruDriver> selectDriverStatusByTruckId (Integer truckId);

    List<TruDriver> selectDriverStatusByPlateNumber(String plateNumber);

    TruDriver selectDriverBaseColumn(TruDriver truDriver);

    Boolean findPhone(String phone);
}
