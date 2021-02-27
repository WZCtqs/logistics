package com.zhkj.lc.oilcard.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.vo.OilTruckRechargeVO;
import com.zhkj.lc.oilcard.model.OilTruckMonthRecharge;
import com.zhkj.lc.oilcard.model.OilTruckRecharge;
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
public interface OilTruckMonthRechargeMapper extends BaseMapper<OilTruckMonthRecharge> {
    OilTruckMonthRecharge selectByTruckIdWithYearMonth(@Param("truckId") Integer truckId, @Param("tenantId") Integer tenantId,@Param("yearMonth") String yearMonth);
    Integer updateTruckMonthRecharge(OilTruckMonthRecharge oilTruckMonthRecharge);
    Integer insertOilTruckMonthRecharge(OilTruckMonthRecharge oilTruckMonthRecharge);
    List<OilTruckRechargeVO> selectOilTruckRecharge(Query query,OilTruckRechargeVO oilTruckRecharg);
    List<OilTruckRechargeVO> selectOilTruckRechargeByIds(@Param("ids") String[] ids);
    List<OilTruckRechargeVO> selectOilTruckRechargeNoPage(OilTruckRechargeVO oilTruckRecharg);
}
