package com.zhkj.lc.trunk.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.vo.TruckVO;
import com.zhkj.lc.trunk.model.TruTruck;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 车辆 服务层
 * 
 * @author zhkj
 * @date 2018-11-20
 */
public interface ITruTruckService extends IService<TruTruck> {

    TruTruck selectTruckById(Integer id);

    public List<TruTruck> selectPlateNumberList(TruTruck truTruck);

    public List<TruTruck> selectTruckList(TruTruck truTruck);

    public TruTruck selectTruckByplateNumber(TruTruck truTruck);

    public boolean deleteTruckByIds(String ids, String updateBy);

    public Page pageSearch(Query query, TruTruck truTruck);

    public Page selectTruckDriverTeamList(Query query, TruTruck truTruck);

    public List<TruTruck> selectPlateNumberByTruckTeamId(TruTruck truTruck);

    public List<TruTruck> selectPlateNumberByAttribute(TruTruck truTruck);

    void addTruck(TruTruck truTruck);

    Integer countDrivers(Integer truckId);

    Integer selectTruckIdBy(Integer driverId, String plateNumber);

    List<Integer> getTruckIdListByProc(Integer tenantId);

    String[][] allTruckGPS(Integer tenantId);

    Integer checkTruckDriverStatus(String plateNumber, Integer tenantId);

    Integer checkPlateNumber(String plateNumber, Integer tenantId);
    Integer checkPlateNumberById(String plateNumber, Integer tenantId);

    Map<String,Object> getTruckGPS(String plateNumber) throws Exception;

    TruTruck selectTruckByPlateNumber(String plateNumber, Integer tenantId);

    Boolean updateTruckStatus(TruckVO truckVO);
}
