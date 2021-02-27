package com.zhkj.lc.oilcard.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zhkj.lc.common.constant.CommonConstant;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.util.support.Convert;
import com.zhkj.lc.oilcard.mapper.PointAmountMapper;
import com.zhkj.lc.oilcard.model.PointAmount;
import com.zhkj.lc.oilcard.service.IPointAmountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description 公司积分余额 服务实现类
 * @Author ckj
 * @Date 2019/3/5 21:08
 */
@Service
public class PointAmountServiceImpl extends ServiceImpl<PointAmountMapper, PointAmount> implements IPointAmountService {

    @Autowired
    private PointAmountMapper pointAmountMapper;

    @Override
    public PointAmount selectPointAmount(Integer id) {
        return pointAmountMapper.selectPointAmount(id);
    }

    @Override
    public List<PointAmount> selectPointAmountListByIds(String ids) {
        return pointAmountMapper.selectPointAmountListByIds(Convert.toStrArray(ids));
    }

    @Override
    public List<PointAmount> selectPointAmountList(PointAmount pointAmount) {
        return pointAmountMapper.selectPointAmountList(pointAmount);
    }

    @Override
    public Page<PointAmount> selectPointAmountList(Query query, PointAmount pointAmount) {
        List<PointAmount> list = pointAmountMapper.selectPointAmountList(query, pointAmount);
        query.setRecords(list);
        return query;
    }

    @Override
    public PointAmount pointAmount2(Integer tenantId, String yearMonth, String company) {
        return pointAmountMapper.pointAmount2(tenantId, yearMonth, company);
    }

    @Override
    public boolean updatePointAmount(PointAmount pointAmount) {
        Integer result = pointAmountMapper.updatePointAmount(pointAmount);
        return null != result && result == 1;
    }

    @Override
    public boolean insertPointAmount(PointAmount pointAmount) {
        Integer result = pointAmountMapper.insertPointAmount(pointAmount);
        return null != result && result == 1;
    }

    @Override
    public boolean deletePointAmount(String ids, String updateBy) {
        Integer result = pointAmountMapper.deletePointAmount(Convert.toStrArray(ids), updateBy, CommonConstant.STATUS_DEL);
        return null != result && result >= 1;
    }
}
