package com.zhkj.lc.oilcard.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.zhkj.lc.common.vo.OilTruckRechargeVO;
import com.zhkj.lc.oilcard.model.OilTruckRecharge;
import com.baomidou.mybatisplus.service.IService;

import com.zhkj.lc.common.util.Query;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author JHM
 * @since 2019-05-29
 */
public interface IOilTruckRechargeService extends IService<OilTruckRecharge> {

    Boolean insertOilTruckRecharge(OilTruckRecharge oilTruckRecharge);

    Page<OilTruckRecharge> findAllTruckRechargeRecordsByTruckId(Query query, Integer truckId, Integer tenantId);

}
