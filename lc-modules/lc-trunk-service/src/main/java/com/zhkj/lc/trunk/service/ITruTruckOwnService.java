package com.zhkj.lc.trunk.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.trunk.model.TruTruck;
import com.zhkj.lc.trunk.model.TruTruckOwn;

import java.util.List;

/**
 * 车主信息 服务层
 * 
 * @author zhkj
 * @date 2018-11-20
 */
public interface ITruTruckOwnService extends IService<TruTruckOwn> {

    int deleteByTruTruckOwn(TruTruckOwn truTruckOwn);

    List<TruTruckOwn> allTruTruckOwn(TruTruckOwn truTruckOwn);

    TruTruckOwn selectOwnerByPlateNumber(String plateNumber,Integer tenantId);

    List<String> getPlateNumbersByTruckOwnId(TruTruckOwn truTruckOwn);

    TruTruckOwn selectTruckOwnBygopenId(String gopenId);

    TruTruckOwn selectTruckOwnByPhoneXopenId(String phone, String xopenId);

    Page selectTruTruckOwnByPage(Query query, TruTruckOwn truTruckOwn);

    List<TruTruck> getCarDataByTruckOwnId(Integer truckOwnId);
}
