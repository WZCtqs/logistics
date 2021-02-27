package com.zhkj.lc.oilcard.service.impl;

import com.zhkj.lc.common.vo.OilTruckRechargeVO;
import com.zhkj.lc.oilcard.model.OilTruckRecharge;
import com.zhkj.lc.oilcard.mapper.OilTruckRechargeMapper;
import com.zhkj.lc.oilcard.service.IOilTruckRechargeService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.plugins.Page;
import com.zhkj.lc.common.util.Query;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author JHM
 * @since 2019-05-29
 */
@Service
public class OilTruckRechargeServiceImpl extends ServiceImpl<OilTruckRechargeMapper, OilTruckRecharge> implements IOilTruckRechargeService {

    @Autowired OilTruckRechargeMapper oilTruckRechargeMapper;
    @Override
    public Boolean insertOilTruckRecharge(OilTruckRecharge oilTruckRecharge) {
      Integer result = oilTruckRechargeMapper.insertOilTruckRecharge(oilTruckRecharge);
        return result !=null && result==1 ;
    }

    @Override
    public Page<OilTruckRecharge> findAllTruckRechargeRecordsByTruckId(Query query,Integer truckId, Integer tenantId) {

        List<OilTruckRecharge> oilTruckRechargeList = oilTruckRechargeMapper.findAllTruckRechargeRecordsByTruckId(query,truckId,tenantId);
        query.setRecords(oilTruckRechargeList);
        return query;
    }
}
