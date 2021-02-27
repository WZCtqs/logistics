package com.zhkj.lc.oilcard.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zhkj.lc.common.constant.CommonConstant;
import com.zhkj.lc.common.util.DateUtils;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.util.UserUtils;
import com.zhkj.lc.common.util.support.Convert;
import com.zhkj.lc.oilcard.mapper.OilCashAmountMapper;
import com.zhkj.lc.oilcard.model.OilCashAmount;
import com.zhkj.lc.oilcard.service.IOilCashAmountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description 加油现金余额 服务实现类
 * @Author ckj
 * @Date 2019/3/5 19:52
 */
@Service
public class OilCashAmountServiceImpl extends ServiceImpl<OilCashAmountMapper, OilCashAmount> implements IOilCashAmountService {

    @Autowired
    private OilCashAmountMapper oilCashAmountMapper;

    @Override
    public OilCashAmount selectOilCashAmount(Integer id) {
        return oilCashAmountMapper.selectOilCashAmount(id);
    }

    @Override
    public List<OilCashAmount> selectOilCashAmountListByIds(String ids) {
        return oilCashAmountMapper.selectOilCashAmountListByIds(Convert.toStrArray(ids));
    }

    @Override
    public List<OilCashAmount> selectOilCashAmountList(OilCashAmount oilCashAmount) {
        return oilCashAmountMapper.selectOilCashAmountList(oilCashAmount);
    }

    @Override
    public Page<OilCashAmount> selectOilCashAmountList(Query query, OilCashAmount oilCashAmount) {
        List<OilCashAmount> list = oilCashAmountMapper.selectOilCashAmountList(query, oilCashAmount);
        query.setRecords(list);
        return query;
    }

    @Override
    public OilCashAmount oilCashAmount2(Integer tenantId, String yearMonth, String company) {
        return oilCashAmountMapper.oilCashAmount2(tenantId, yearMonth, company);
    }

    @Override
    public boolean updateOilCashAmount(OilCashAmount oilCashAmount) {
        Integer result = oilCashAmountMapper.updateOilCashAmount(oilCashAmount);
        return null != result && result == 1;
    }

    @Override
    public boolean insertOilCashAmount(OilCashAmount oilCashAmount) {
        OilCashAmount oilCashAmount1 = new OilCashAmount();
        oilCashAmount1.setTenantId(oilCashAmount.getTenantId());
        oilCashAmount1.setYearMonth(DateUtils.getLastDateYM());
        oilCashAmount1.setCompany(oilCashAmount.getCompany());
        List<OilCashAmount> list = oilCashAmountMapper.selectOilCashAmountList(oilCashAmount1);
        if (null != list && list.size() > 0){
            oilCashAmount.setLastCash(list.get(0).getCashAmount());
        }
        oilCashAmount.setYearMonth(DateUtils.getDateYM());
        Integer result = oilCashAmountMapper.insertOilCashAmount(oilCashAmount);
        return null != result && result == 1;
    }

    @Override
    public boolean deleteOilCashAmount(String ids, String updateBy) {
        Integer result = oilCashAmountMapper.deleteOilCashAmount(Convert.toStrArray(ids), updateBy, CommonConstant.STATUS_DEL);
        return null != result && result >= 1;
    }
}
