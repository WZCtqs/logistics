package com.zhkj.lc.trunk.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.trunk.model.TruTruck;
import com.zhkj.lc.trunk.model.TruTruckOwn;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 车主信息 数据层
 * 
 * @author zhkj
 * @date 2018-11-20
 */
public interface TruTruckOwnMapper extends BaseMapper<TruTruckOwn> {

    int deleteByTruTruckOwn(TruTruckOwn truTruckOwn);

    List<TruTruckOwn> allTruTruckOwn(TruTruckOwn truTruckOwn);

    TruTruckOwn selectOwnerByPlateNumber(@Param("plateNumber")String plateNumber, @Param("tenantId")Integer tenantId);

    List<String> getPlateNumbersByTruckOwnId(TruTruckOwn truTruckOwn);

    TruTruckOwn selectTruckOwnBygopenId(@Param("gopenId")String gopenId);

    TruTruckOwn selectTruckOwnByPhoneXopenId(@Param("phone")String phone, @Param("xopenId")String xopenId);

    List<TruTruckOwn> allTruTruckOwn(Query query, TruTruckOwn truTruckOwn);

    List<TruTruck> getCarDataByTruckOwnId(@Param("truckOwnId")Integer truckOwnId);

}