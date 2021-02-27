package com.zhkj.lc.order.service;

import com.baomidou.mybatisplus.service.IService;
import com.zhkj.lc.order.model.entity.OrdPickupArrivalAdd;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * <p>
 * 运输跟踪 服务类
 * </p>
 *
 * @author wzc
 * @since 2018-12-07
 */
public interface IOrdPickupArrivalAdd extends IService<OrdPickupArrivalAdd> {

    List<OrdPickupArrivalAdd> selectByOrderId(String orderId);

    boolean deleteByOrderId(String orderId);

    List<OrdPickupArrivalAdd>getPickupAdds(List<OrdPickupArrivalAdd> list);

    List<OrdPickupArrivalAdd>getArrivalAdds(List<OrdPickupArrivalAdd> list);

    Map<String, String> selectStartEndAdd(String orderId);

    OrdPickupArrivalAdd selectNowReceivingPhoto(String orderId);

    List<OrdPickupArrivalAdd> selectArrivalByOrderId(String orderId);

    List<OrdPickupArrivalAdd> selectPickupByOrderId(String orderId);
}
