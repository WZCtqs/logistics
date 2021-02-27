package com.zhkj.lc.oilcard.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zhkj.lc.common.constant.CommonConstant;
import com.zhkj.lc.common.util.DateUtils;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.util.support.Convert;
import com.zhkj.lc.oilcard.mapper.MajorMonthRechargeMapper;
import com.zhkj.lc.oilcard.mapper.OilCashAmountMapper;
import com.zhkj.lc.oilcard.model.MajorMonthRecharge;
import com.zhkj.lc.oilcard.model.OilCardCashAmount;
import com.zhkj.lc.oilcard.model.OilCashAmount;
import com.zhkj.lc.oilcard.service.IMajorMonthRechargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  主卡月充值服务实现类
 * </p>
 *
 * @author ckj
 * @since 2019-02-11
 */
@Service
public class MajorMonthRechargeServiceImpl extends ServiceImpl<MajorMonthRechargeMapper, MajorMonthRecharge> implements IMajorMonthRechargeService {

    @Autowired
    private MajorMonthRechargeMapper majorMonthRechargeMapper;
    @Autowired
    private OilCashAmountMapper oilCashAmountMapper;

    @Override
    public MajorMonthRecharge selectMajorMonthRecharge(Integer majorMonthId) {
        return majorMonthRechargeMapper.selectMajorMonthRecharge(majorMonthId);
    }

    @Override
    public List<MajorMonthRecharge> selectMajorMonthRechargeByIds(String ids) {
        return majorMonthRechargeMapper.selectMajorMonthRechargeByIds(Convert.toStrArray(ids));
    }

    @Override
    public MajorMonthRecharge selectMajorMonthRechargeUpdate(Integer majorId, Integer tenantId, String yearMonth) {
        return majorMonthRechargeMapper.selectMajorMonthRechargeUpdate(majorId, tenantId, yearMonth);
    }

    @Override
    public List<MajorMonthRecharge> selectMajorMonthRechargeList(MajorMonthRecharge majorMonthRecharge) {
        return majorMonthRechargeMapper.selectMajorMonthRechargeList(majorMonthRecharge);
    }

    @Override
    public Page<MajorMonthRecharge> selectMajorMonthRechargeList(Query query, MajorMonthRecharge majorMonthRecharge) {
        List<MajorMonthRecharge> list = majorMonthRechargeMapper.selectMajorMonthRechargeList(query, majorMonthRecharge);
        query.setRecords(list);
        return query;
    }

    @Override
    public List<OilCardCashAmount> selectMajorMonthRechargeCashAmount(String yearMonth, Integer tenantId) {
        return majorMonthRechargeMapper.selectMajorMonthRechargeCashAmount(yearMonth, tenantId);
    }

    @Override
    public Page<OilCardCashAmount> selectMajorMonthRechargeCashAmount(Query query, String yearMonth, Integer tenantId) {
        List<OilCardCashAmount> list = majorMonthRechargeMapper.selectMajorMonthRechargeCashAmount(query, yearMonth, tenantId);
        query.setRecords(list);
        return query;
    }

    @Override
    public boolean updateMajorMonthRecharge(MajorMonthRecharge majorMonthRecharge) {
        Integer result = majorMonthRechargeMapper.updateMajorMonthRecharge(majorMonthRecharge);
        return null != result && result == 1;
    }

    @Override
    public boolean insertMajorMonthRecharge(MajorMonthRecharge majorMonthRecharge) {
        // 重复判断过滤
        MajorMonthRecharge majorMonthRecharge1 = majorMonthRechargeMapper.selectMajorMonthRechargeUpdate(majorMonthRecharge.getMajorId(), majorMonthRecharge.getTenantId(), majorMonthRecharge.getYearMonth());
        Integer result;
        if (null == majorMonthRecharge1){
            MajorMonthRecharge majorMonthRechargeLast = majorMonthRechargeMapper.selectMajorMonthRechargeUpdate(majorMonthRecharge.getMajorId(), majorMonthRecharge.getTenantId(), DateUtils.getLastDateYM());
            if (null != majorMonthRechargeLast){
                majorMonthRecharge1.setLastAmount((majorMonthRechargeLast.getLastAmount().add((majorMonthRechargeLast.getRechargeSum().add(majorMonthRechargeLast.getRebate())))).subtract(majorMonthRechargeLast.getDistributeSum()));
            }
            majorMonthRecharge.setRechargeSum(majorMonthRecharge.getRechargeSum());
            result = majorMonthRechargeMapper.insertMajorMonthRecharge(majorMonthRecharge);
        }else
            result = 0;
        
        return null != result && result == 1;
    }

    @Override
    public boolean deleteMajorMonthRecharge(String ids, String updateBy) {
        Integer result = majorMonthRechargeMapper.deleteMajorMonthRecharge(Convert.toStrArray(ids), updateBy, CommonConstant.STATUS_DEL);
        return null != result && result >= 1;
    }
}
