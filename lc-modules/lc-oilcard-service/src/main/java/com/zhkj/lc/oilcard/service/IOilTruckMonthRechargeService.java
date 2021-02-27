package com.zhkj.lc.oilcard.service;


import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.vo.OilTruckRechargeVO;
import com.zhkj.lc.oilcard.model.OilTruckMonthRecharge;
import com.zhkj.lc.oilcard.model.OilTruckRecharge;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author JHM
 * @since 2019-05-29
 */
public interface IOilTruckMonthRechargeService extends IService<OilTruckMonthRecharge> {
    OilTruckMonthRecharge selectByTruckIdWithYearMonth(Integer truckId, Integer tenantId, String yearMonth);
    boolean updateTruckMonthRecharge(OilTruckMonthRecharge oilTruckMonthRecharge);
    Boolean insertTruckMonthRecharge(OilTruckMonthRecharge oilTruckMonthRecharge);
    Page<OilTruckRechargeVO> selectOilTruckRecharge(Query query, OilTruckRechargeVO oilTruckRecharg);

    List<OilTruckRechargeVO> selectOilTruckRechargeList(OilTruckRechargeVO oilTruckRecharg);

    List<OilTruckRechargeVO> selectOilTruckRechargeByIds(String ids);
}
