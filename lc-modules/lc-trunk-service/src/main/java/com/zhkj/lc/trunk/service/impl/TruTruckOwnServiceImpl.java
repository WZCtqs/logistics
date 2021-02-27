package com.zhkj.lc.trunk.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.util.UserUtils;
import com.zhkj.lc.trunk.mapper.TruTruckOwnMapper;
import com.zhkj.lc.trunk.model.TruTruck;
import com.zhkj.lc.trunk.model.TruTruckOwn;
import com.zhkj.lc.trunk.service.ITruTruckOwnService;
import lombok.AllArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
;import java.util.Date;
import java.util.List;

/**
 * 车主信息 服务层实现
 * 
 * @author zhkj
 * @date 2018-11-20
 */
@Service
@AllArgsConstructor
public class TruTruckOwnServiceImpl extends ServiceImpl<TruTruckOwnMapper, TruTruckOwn> implements ITruTruckOwnService {

    @Autowired
    private TruTruckOwnMapper truTruckOwnMapper;

    @Override
    public int deleteByTruTruckOwn(TruTruckOwn truTruckOwn) {

            return truTruckOwnMapper.deleteByTruTruckOwn(truTruckOwn);
    }

    @Override
    public List<TruTruckOwn> allTruTruckOwn(TruTruckOwn truTruckOwn) {
        return truTruckOwnMapper.allTruTruckOwn(truTruckOwn);
    }

    @Override
    public TruTruckOwn selectOwnerByPlateNumber(@Param("plateNumber")String plateNumber,@Param("tenantId")Integer tenantId) {
        return truTruckOwnMapper.selectOwnerByPlateNumber(plateNumber,tenantId);
    }

    @Override
    public List<String> getPlateNumbersByTruckOwnId(TruTruckOwn truTruckOwn) {
        return truTruckOwnMapper.getPlateNumbersByTruckOwnId(truTruckOwn);
    }

    @Override
    public TruTruckOwn selectTruckOwnBygopenId(String gopenId) {
        return truTruckOwnMapper.selectTruckOwnBygopenId(gopenId);
    }

    @Override
    public TruTruckOwn selectTruckOwnByPhoneXopenId(@Param("phone")String phone, @Param("xopenId")String xopenId) {
        return truTruckOwnMapper.selectTruckOwnByPhoneXopenId(phone,xopenId);
    }

    @Override
    public Page selectTruTruckOwnByPage(Query query, TruTruckOwn truTruckOwn) {
        List<TruTruckOwn> list = truTruckOwnMapper.allTruTruckOwn(query, truTruckOwn);
        query.setRecords(list);
        return query;
    }

    @Override
    public List<TruTruck> getCarDataByTruckOwnId(Integer truckOwnId) {
        return truTruckOwnMapper.getCarDataByTruckOwnId(truckOwnId);
    }

}
