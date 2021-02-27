package com.zhkj.lc.order.feign.fallback;

import com.zhkj.lc.common.util.R;
import com.zhkj.lc.common.vo.*;
import com.zhkj.lc.order.dto.TruDriver;
import com.zhkj.lc.order.feign.TrunkFeign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author cb
 * @description
 * @date 2018/12/5
 */
@Service
public class TrunkFeignImpl implements TrunkFeign {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public Integer checkDriverTruck(String driverName, String plateNumber, Integer tenantId) {
        return null;
    }

    @Override
    public DriverVO getDriverByid(Integer id) {
        return null;
    }

    @Override
    public List<DriverVO> getDriverList(DriverVO driverVO) {
        return null;
    }

    @Override
    public List<TruckVO> selectTruckList(TruckVO truckVO) {
        return null;
    }

    @Override
    public TruDriver getDriverByName(String driverName, Integer tenantId) {
        return null;
    }

    @Override
    public TruckVO selectTruckByPlateNumber(String plateNumber, Integer tenantId) {
        return null;
    }

    @Override
    public List<DriverVO> getDriverTeamType(String teamType, Integer tenantId) {
        return null;
    }

    @Override
    public List<CustomerVO> getCustomerList(CustomerVO customerVO) {
        return null;
    }

    @Override
    public CustomerVO selectCustomerById(CustomerVO customerVO) {
        return null;
    }

    @Override
    public List<TruckTeamVo> getTruckTeams(TruckTeamVo truckTeamVo) {
        return null;
    }

    @Override
    public List<DriverVO> getZFDriverInfo(String zfId) {
        return null;
    }

    @Override
    public R<Boolean> edit(DriverVO truDriver) {
        return null;
    }

    @Override
    public R<Boolean> add(AnnouncementVO announcementVO) {
        return null;
    }

    @Override
    public List<AnnouncementVO> listAllNoChecked(Integer tenantId) {
        return null;
    }

    @Override
    public R<Boolean> update2CheckedById(String ids) {
        return null;
    }

    @Override
    public List<CustomerVO> selectAllCustomers(CustomerVO customerVO) {
        return null;
    }

    @Override
    public TruckTeamFeeVO selectTruckFeeByDriverLast7days(FeeVO feeVO) {
        return null;
    }

    @Override
    public TruckTeamFeeVO selectTruckFeeByDriverMonthdays(FeeVO feeVO) {
        return null;
    }

    @Override
    public TruckTeamFeeVO selectTruckFeeByDriverCurrentSeason(FeeVO feeVO) {
        return null;
    }

    @Override
    public TruckTeamFeeVO selectTruckFeeByDriverLast6Months(FeeVO feeVO) {
        return null;
    }

    @Override
    public TruckTeamFeeVO selectTruckFeeByDriverSometime(FeeVO feeVO) {
        return null;
    }

    @Override
    public List<DriverVO> selectDriverByPlateNumber(Integer plateId, Integer tenantId) {
        return null;
    }

    @Override
    public DriverVO selectDriverByDriverId(Integer driverId, Integer tenantId) {
        return null;
    }

    @Override
    public List<DriverVO> selectAllDriver(DriverVO driverVO) {
        return null;
    }

    @Override
    public List<CustomerVO> selectAllForFegin(CustomerVO customerVO) {
        return null;
    }

    @Override
    public List<CustomerVO> selectLikeAllForFegin(CustomerVO customerVO) {
        return null;
    }

    @Override
    public Integer checkTruckDriverStatus(String plateNumber, Integer tenantId) {
        return null;
    }

    @Override
    public DriverVO selectDriverStatus(Integer driverId, Integer tenantId) {
        return null;
    }

    @Override
    public List<DriverVO> selectDriverStatusByPlateNumber(String plateNumber) {
        return null;
    }

    @Override
    public TaxRateVO getRate(Integer tenantId) { return null;}

    @Override
    public List<TruTruckOwnVo> getAllTruTruckOwn(TruTruckOwnVo truTruckOwnVo) { return null; }

    @Override
    public List<String> getPlateNumbersByTruckOwnId( Integer truckOwnId, Integer tenantId){ return null; }

    @Override
    public TruTruckOwnVo getTruTruckOwnVoByPlateNumber(String plateNumber, Integer tenantId){ return null; }
}
