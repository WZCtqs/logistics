package com.zhkj.lc.oilcard.service.impl;


import com.baomidou.mybatisplus.plugins.Page;
import com.zhkj.lc.common.util.DateUtils;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.util.support.Convert;
import com.zhkj.lc.common.vo.OilCardVO;
import com.zhkj.lc.common.vo.OilTruckRechargeVO;
import com.zhkj.lc.oilcard.mapper.OilCardMapper;
import com.zhkj.lc.oilcard.mapper.OilTruckMonthRechargeMapper;
import com.zhkj.lc.oilcard.model.OilCard;
import com.zhkj.lc.oilcard.model.OilTruckMonthRecharge;
import com.zhkj.lc.oilcard.model.OilTruckRecharge;
import com.zhkj.lc.oilcard.service.IOilTruckMonthRechargeService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author JHM
 * @since 2019-05-29
 */
@Service
public class OilTruckMonthRechargeServiceImpl extends ServiceImpl<OilTruckMonthRechargeMapper, OilTruckMonthRecharge> implements IOilTruckMonthRechargeService {

    @Autowired private OilTruckMonthRechargeMapper oilTruckMonthRechargeMapper;
    @Autowired private OilCardMapper oilCardMapper;
    @Override
    public OilTruckMonthRecharge selectByTruckIdWithYearMonth(Integer truckId, Integer tenantId, String yearMonth) {
        return oilTruckMonthRechargeMapper.selectByTruckIdWithYearMonth(truckId,tenantId,yearMonth);
    }

    @Override
    public boolean updateTruckMonthRecharge(OilTruckMonthRecharge oilTruckMonthRecharge) {
        Integer result = oilTruckMonthRechargeMapper.updateTruckMonthRecharge(oilTruckMonthRecharge);
        return null != result && result == 1;
    }

    @Override
    public Boolean insertTruckMonthRecharge(OilTruckMonthRecharge oilTruckMonthRecharge) {
        OilTruckMonthRecharge oilTruckMonthRechargeLast = oilTruckMonthRechargeMapper.selectByTruckIdWithYearMonth(oilTruckMonthRecharge.getTruckId(), oilTruckMonthRecharge.getTenantId(), DateUtils.getLastDateYM());
        if (null != oilTruckMonthRechargeLast){
            oilTruckMonthRecharge.setLastAmount(oilTruckMonthRechargeLast.getLastAmount().add((oilTruckMonthRechargeLast.getBalance())));
        }

       Integer result =  oilTruckMonthRechargeMapper.insertOilTruckMonthRecharge(oilTruckMonthRecharge);
        return result != null && result ==1;
    }

    @Override
    public Page<OilTruckRechargeVO> selectOilTruckRecharge(Query query, OilTruckRechargeVO oilTruckRecharg) {
       //查找对应车辆的持卡数量
        Map<Object,String> map = new HashMap<>();
        List<OilCardVO> countList = oilCardMapper.countCardNumByTruckId(oilTruckRecharg.getPlateNumber(),oilTruckRecharg.getTenantId());
        for(OilCardVO oilCardVO:countList){
            map.put(oilCardVO.getTruckId(),oilCardVO.getQuantity());
        }
        List<OilTruckRechargeVO> oilTruckRechargeVOList = oilTruckMonthRechargeMapper.selectOilTruckRecharge(query,oilTruckRecharg);
        for(OilTruckRechargeVO oilTruckRechargeVO:oilTruckRechargeVOList){
           Integer truckId = oilTruckRechargeVO.getTruckId();
            oilTruckRechargeVO.setQuantity(map.get(truckId));
        }
        query.setRecords(oilTruckRechargeVOList);
        return query;

    }

    @Override
    public List<OilTruckRechargeVO> selectOilTruckRechargeList(OilTruckRechargeVO oilTruckRecharg) {
        Map<Object,String> map = new HashMap<>();
        List<OilCardVO> countList = oilCardMapper.countCardNumByTruckId(oilTruckRecharg.getPlateNumber(),oilTruckRecharg.getTenantId());
        for(OilCardVO oilCardVO:countList){
            map.put(oilCardVO.getTruckId(),oilCardVO.getQuantity());
        }
        List<OilTruckRechargeVO> oilTruckRechargeVOList = oilTruckMonthRechargeMapper.selectOilTruckRechargeNoPage(oilTruckRecharg);
        for(OilTruckRechargeVO oilTruckRechargeVO:oilTruckRechargeVOList){
            Integer truckId = oilTruckRechargeVO.getTruckId();
            oilTruckRechargeVO.setQuantity(map.get(truckId));
        }
        return oilTruckRechargeVOList;
    }

    @Override
    public List<OilTruckRechargeVO> selectOilTruckRechargeByIds(String ids) {

        return oilTruckMonthRechargeMapper.selectOilTruckRechargeByIds(Convert.toStrArray(ids));
    }
}
