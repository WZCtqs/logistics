package com.zhkj.lc.trunk.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.vo.TruckVO;
import com.zhkj.lc.trunk.model.TruTruck;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 车辆 数据层
 * 
 * @author zhkj
 * @date 2018-11-20
 */
public interface TruTruckMapper extends BaseMapper<TruTruck> {

    TruTruck selectTruckById(@Param("truckId") Integer truckId);

    TruTruck selectTruckByDriverPlateId(@Param("truckId")int plateId);

    public List<TruTruck> selectPlateNumberList(TruTruck truTruck);

    public List<TruTruck> selectTruckList(TruTruck truTruck);

    public TruTruck selectTruckByplateNumber(TruTruck truTruck);

    public Integer deleteTruckByIds(TruTruck truTruck);

    public List<TruTruck> pageSearch(Query query, TruTruck truTruck);

    public List<TruTruck> selectTruckDriverTeamList(Query query, TruTruck truTruck);

    public List<TruTruck> selectPlateNumberByTruckTeamId(TruTruck truTruck);

    public List<TruTruck> selectPlateNumberByAttribute(TruTruck truTruck);

//    @Options(useGeneratedKeys = true, keyProperty = "truckId", keyColumn = "truck_id") // id自动增长
    void addTruck(TruTruck truTruck);

    String selectPayTypeForFegin(String truckId);

    Integer countDrivers(@Param("truckId") Integer truckId);

    List<TruTruck> selectPlateNumberListGPS(TruTruck truTruck);

    Integer selectTruckIdBy(@Param("driverId") Integer driverId, @Param("plateNumber") String plateNumber);

    Integer checkTruckDriverStatus(@Param("plateNumber") String plateNumber,  @Param("tenantId") Integer tenantId);

    Integer checkPlateNumber(@Param("plateNumber")String plateNumber, @Param("tenantId")Integer tenantId);
    Integer checkPlateNumberById(@Param("plateNumber")String plateNumber, @Param("tenantId")Integer tenantId);

    List<TruckVO> selectListByOwnerId(Integer ownerId);

    TruTruck selectByPlateNumber(@Param("plateNumber")String plateNumber, @Param("tenantId")Integer tenantId);

    Boolean updateTruckStatus(TruckVO truckVO);
}