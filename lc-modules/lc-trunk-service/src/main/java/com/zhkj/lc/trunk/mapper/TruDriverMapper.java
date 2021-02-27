package com.zhkj.lc.trunk.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.vo.DriverVO;
import com.zhkj.lc.trunk.model.TruDriver;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 司机 数据层
 * 
 * @author zhkj
 * @date 2018-11-20
 */
public interface TruDriverMapper extends BaseMapper<TruDriver> {
    public List<TruDriver> selectTruckDriverByPlateId(@Param("plateId") int truckId);
    /**
     * 连表查询车主信息
     * @return
     */
    public List<Object> selectDriverTruck(Query<Object> query, Map<String, Object> condition);

    public TruDriver selectDriverByName(TruDriver truDriver);

    public TruDriver selectDriverByPhone(String phone);

    public TruDriver selectDriverByPhoneXopenId(@Param("phone")String phone,@Param("xopenId")String xopenId);

    public List<TruDriver> selectAllDriver(TruDriver truDriver);

    public Integer deleteDriverByIds(TruDriver truDriver);

    public TruDriver selectDriverById(Integer driverId);

    public List<TruDriver> selectDriverPlateNumber(TruDriver truDriver);

    public List<TruDriver> selectByIds(TruDriver truDriver);

    public List<TruDriver> selectDriverByplateId(TruDriver truDriver);

    public int checkDriverTruck(@Param("driverName") String driverName,@Param("plateNumber") String plateNumber, @Param("tenantId") String tenantId);

    @Options(useGeneratedKeys = true, keyProperty = "driverId", keyColumn = "driver_id") // id自动增长
    public int addDriver(TruDriver truDriver);

    public List<TruDriver> selectDriverDetailList(Query query, TruDriver truDriver);

    List<TruDriver> selectDriverPhone(TruDriver truDriver);

    List<DriverVO> selectZFDriverVo(@Param("zfid") String[] zfid);

    TruDriver selectDriverDetailById(TruDriver truDriver);

    //根据openid查询司机信息（gopenId）
    TruDriver selectDriverBygopenId(@Param("gopenId")String gopenId);

    List<TruDriver> selectDriverByWhiteList(TruDriver truDriver);

    void updateStatus(DriverVO driverVO);

    List<TruDriver> selectDriverByTruck(TruDriver driverVO);

    String getDriverPayType(@Param("driverId")Integer driverId, @Param("tenantId")Integer tenantId);

    List<DriverVO> getDriverTeamType(@Param("teamType")String teamType, @Param("tenantId")Integer tenantId);

    List<DriverVO> selectDriverByPlateNumber(@Param("plateId")Integer plateId,@Param("tenantId")Integer tenantId);

    DriverVO selectDriverByDriverId(@Param("driverId")Integer driverId,@Param("tenantId")Integer tenantId);

    List<TruDriver> selectAllDriverByTenantId(TruDriver truDriver);

    DriverVO selectDriverStatus(@Param("driverId") Integer driverId, @Param("tenantId") Integer tenantId);

    List<TruDriver> selectDriverStatusByTruckId(Integer truckId);

    List<TruDriver> selectDriverStatusByPlateNumber(String plateNumber);

    TruDriver selectDriverBaseColumn(TruDriver truDriver);

    List<DriverVO> findPhone(String phone);
}