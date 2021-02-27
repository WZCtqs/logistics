package com.zhkj.lc.trunk.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zhkj.lc.trunk.mapper.TruTruckMapper;
import com.zhkj.lc.trunk.mapper.TruTruckfileMapper;
import com.zhkj.lc.trunk.model.TruTruckfile;
import com.zhkj.lc.trunk.service.ITruTruckfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 车辆文件表 服务实现类
 * </p>
 *
 * @author wzc
 * @since 2018-12-07
 */
@Service
public class TruTruckfileServiceImpl extends ServiceImpl<TruTruckfileMapper, TruTruckfile> implements ITruTruckfileService {

    @Autowired
    TruTruckfileMapper truTruckfileMapper;

    @Override
    public boolean insertTruckFile(TruTruckfile truTruckfile) {
        return truTruckfileMapper.insertTruckFile(truTruckfile);
    }
}
