package com.zhkj.lc.oilcard.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zhkj.lc.common.constant.CommonConstant;
import com.zhkj.lc.common.util.DateUtils;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.util.support.Convert;
import com.zhkj.lc.oilcard.mapper.OilMajorRechargeMapper;
import com.zhkj.lc.oilcard.model.MajorMonthRecharge;
import com.zhkj.lc.oilcard.model.OilMajorRecharge;
import com.zhkj.lc.oilcard.service.IMajorMonthRechargeService;
import com.zhkj.lc.oilcard.service.IOilMajorRechargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 *  主卡充值服务实现类
 * </p>
 *
 * @author ckj
 * @since 2019-02-11
 */
@Service
public class OilMajorRechargeServiceImpl extends ServiceImpl<OilMajorRechargeMapper, OilMajorRecharge> implements IOilMajorRechargeService {

    @Autowired
    private OilMajorRechargeMapper majorRechargeMapper;
    @Autowired
    private IMajorMonthRechargeService majorMonthRechargeService;

    @Override
    public OilMajorRecharge selectMajorRecharge(Integer majorRechargeId) {
        return majorRechargeMapper.selectMajorRecharge(majorRechargeId);
    }

    @Override
    public List<OilMajorRecharge> selectMajorRechargeByIds(String ids) {
        return majorRechargeMapper.selectMajorRechargeByIds(Convert.toStrArray(ids));
    }

    @Override
    public List<OilMajorRecharge> selectMajorRechargeList(OilMajorRecharge oilMajorRecharge) {
        return majorRechargeMapper.selectMajorRechargeList(oilMajorRecharge);
    }

    @Override
    public Page<OilMajorRecharge> selectMajorRechargeList(Query query, OilMajorRecharge oilMajorRecharge) {
        List<OilMajorRecharge> list = majorRechargeMapper.selectMajorRechargeList(query, oilMajorRecharge);
        query.setRecords(list);
        return query;
    }

    @Override
    public boolean updateMajorRecharge(OilMajorRecharge oilMajorRecharge) {
        Integer result = majorRechargeMapper.updateMajorRecharge(oilMajorRecharge);
        return null != result && result == 1;
    }

    @Override
    public boolean insertMajorRecharge(OilMajorRecharge oilMajorRecharge) {
        Integer result = majorRechargeMapper.insertMajorRecharge(oilMajorRecharge);
        return null != result && result == 1;
    }

    @Override
    public boolean deleteMajorRecharge(String ids, String updateBy) {
        Integer result = majorRechargeMapper.deleteMajorRecharge(Convert.toStrArray(ids),updateBy, CommonConstant.STATUS_DEL);
        return null != result && result >= 1;
    }
}
