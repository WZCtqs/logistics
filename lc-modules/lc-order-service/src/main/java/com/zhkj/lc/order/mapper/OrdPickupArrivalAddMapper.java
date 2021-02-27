package com.zhkj.lc.order.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zhkj.lc.order.model.entity.OrdPickupArrivalAdd;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Auther: HP
 * @Date: 2019/5/16 09:26
 * @Description:
 */
public interface OrdPickupArrivalAddMapper extends BaseMapper<OrdPickupArrivalAdd> {

    /*送货地*/
    List<OrdPickupArrivalAdd> selectArrivalByOrderId(@Param("orderId")String orderId);
    /*提货地*/
    List<OrdPickupArrivalAdd> selectPickupByOrderId(@Param("orderId")String orderId);

    List<OrdPickupArrivalAdd> selectByOrderId(@Param("orderId")String orderId);

    boolean deleteByOrderId(@Param("orderId")String orderId);

    Map<String, String> selectStartEndAdd(@Param("orderId")String orderId);

    OrdPickupArrivalAdd selectNowReceivingPhoto(@Param("orderId")String orderId);
}
