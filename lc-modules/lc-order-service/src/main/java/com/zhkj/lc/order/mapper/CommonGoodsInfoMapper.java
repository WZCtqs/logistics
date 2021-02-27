package com.zhkj.lc.order.mapper;

import com.zhkj.lc.order.dto.OrderBaseInfoForApp;
import com.zhkj.lc.order.model.entity.CommonGoodsInfo;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author cb
 * @since 2019-01-05
 */
public interface CommonGoodsInfoMapper extends BaseMapper<CommonGoodsInfo> {

    List<CommonGoodsInfo> selectCommonGoodsInfoById(@Param("orderId") String orderId);

    void deleteByOrderId(String morderId);

    List<OrderBaseInfoForApp> selectCompletedOrderForApp(@Param("driverId") String driverId);
}
