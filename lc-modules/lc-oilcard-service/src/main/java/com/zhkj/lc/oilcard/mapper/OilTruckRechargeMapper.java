package com.zhkj.lc.oilcard.mapper;

import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.oilcard.model.OilTruckRecharge;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author JHM
 * @since 2019-05-29
 */
public interface OilTruckRechargeMapper extends BaseMapper<OilTruckRecharge> {
    Integer insertOilTruckRecharge(OilTruckRecharge oilTruckRecharge);
    List<OilTruckRecharge> findAllTruckRechargeRecordsByTruckId(Query query,@Param("truckId") Integer truckId, @Param("tenantId") Integer tenantId);
}
