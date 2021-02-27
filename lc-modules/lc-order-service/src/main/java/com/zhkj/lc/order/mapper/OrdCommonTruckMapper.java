package com.zhkj.lc.order.mapper;

import com.zhkj.lc.order.dto.OrdCommonTruckVO;
import com.zhkj.lc.order.model.entity.OrdCommonTruck;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author cb
 * @since 2019-01-03
 */
public interface OrdCommonTruckMapper extends BaseMapper<OrdCommonTruck> {

    OrdCommonTruckVO selectCommonTruck(String orderId);

    Boolean updateForYFBillByOrderIds(@Param("orderIds") String[] orderIds);
}
