package com.zhkj.lc.oilcard.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zhkj.lc.common.constant.CommonConstant;
import com.zhkj.lc.common.util.support.Convert;
import com.zhkj.lc.oilcard.mapper.OilMajorMapper;
import com.zhkj.lc.oilcard.model.OilMajor;
import com.zhkj.lc.oilcard.service.IOilMajorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wzc
 * @since 2019-02-11
 */
@Service
public class OilMajorServiceImpl extends ServiceImpl<OilMajorMapper, OilMajor> implements IOilMajorService {

    @Autowired
    private OilMajorMapper oilMajorMapper;

    @Override
    public List<OilMajor> selectOilMajorNumber(Integer tenantId) {

        return oilMajorMapper.selectOilMajorNumber(tenantId);
    }

    @Override
    public OilMajor majorNumber2(String majorNumber, Integer tenantId){
        return oilMajorMapper.majorNumber2(majorNumber, tenantId);
    }

    @Override
    public boolean updateOilMajor(OilMajor oilMajor) {
        Integer result =  oilMajorMapper.updateOilMajor(oilMajor);
        return null != result && result == 1;
    }

    @Override
    public boolean insertOilMajor(OilMajor oilMajor) {
        Integer result =  oilMajorMapper.insertOilMajor(oilMajor);
        return null != result && result == 1;
    }

    @Override
    public boolean deleteOilMajor(String ids, String updateBy) {
        Integer result =  oilMajorMapper.deleteOilMajor(Convert.toStrArray(ids), updateBy, CommonConstant.STATUS_DEL);
        return null != result && result >= 1;
    }

    @Override
    public OilMajor findOne(String majorNumber, Integer tenantId) {

        return oilMajorMapper.majorNumber2(majorNumber,tenantId);
    }


}
