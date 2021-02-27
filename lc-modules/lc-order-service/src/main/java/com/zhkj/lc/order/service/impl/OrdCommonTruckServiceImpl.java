package com.zhkj.lc.order.service.impl;

import com.zhkj.lc.order.dto.OrdCommonTruckVO;
import com.zhkj.lc.order.model.entity.OrdCommonTruck;
import com.zhkj.lc.order.mapper.OrdCommonTruckMapper;
import com.zhkj.lc.order.service.IOrdCommonTruckService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author cb
 * @since 2019-01-03
 */
@Service
public class OrdCommonTruckServiceImpl extends ServiceImpl<OrdCommonTruckMapper, OrdCommonTruck> implements IOrdCommonTruckService {

    @Autowired
    private OrdCommonTruckMapper commonTruckMapper;

    @Override
    public OrdCommonTruckVO selectCommonTruck(String orderId) {
        return commonTruckMapper.selectCommonTruck(orderId);
    }
}
