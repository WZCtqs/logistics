package com.zhkj.lc.order.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zhkj.lc.order.mapper.OrdPickupArrivalAddMapper;
import com.zhkj.lc.order.model.entity.OrdPickupArrivalAdd;
import com.zhkj.lc.order.service.IOrdPickupArrivalAdd;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 运输跟踪 服务实现类
 * </p>
 *
 * @author wzc
 * @since 2018-12-07
 */
@Service
public class OrdPickupArrivalAddImpl extends ServiceImpl<OrdPickupArrivalAddMapper, OrdPickupArrivalAdd> implements IOrdPickupArrivalAdd {

    @Resource
    private OrdPickupArrivalAddMapper addMapper;

    @Override
    public List<OrdPickupArrivalAdd> selectByOrderId(String orderId) {
        return addMapper.selectByOrderId(orderId);
    }

    @Override
    public boolean deleteByOrderId(String orderId) {
        return addMapper.deleteByOrderId(orderId);
    }

    @Override
    public List<OrdPickupArrivalAdd> getPickupAdds(List<OrdPickupArrivalAdd> list) {
        List<OrdPickupArrivalAdd> adds = new ArrayList<>();
        if(list != null) {
            for(OrdPickupArrivalAdd add : list){
                if(add.getAddType().equals("0")){
                    adds.add(add);
                }
            }
        }
        return adds;
    }

    @Override
    public List<OrdPickupArrivalAdd> getArrivalAdds(List<OrdPickupArrivalAdd> list) {
        List<OrdPickupArrivalAdd> adds = new ArrayList<>();
        if(list != null) {
            for(OrdPickupArrivalAdd add : list){
                if(add.getAddType().equals("1")){
                    adds.add(add);
                }
            }
        }
        return adds;
    }

    @Override
    public Map<String, String> selectStartEndAdd(String orderId) {
        Map<String, String> map = addMapper.selectStartEndAdd(orderId);
        String start[] = map.get("pickupadd").split("/");
        String end[] = map.get("arrivaladd").split("/");
        map.put("pickupadd",start[1]);
        map.put("arrivaladd",end[1]);
        return map;
    }

    @Override
    public OrdPickupArrivalAdd selectNowReceivingPhoto(String orderId) {
        return addMapper.selectNowReceivingPhoto(orderId);
    }

    @Override
    public List<OrdPickupArrivalAdd> selectArrivalByOrderId(String orderId) {
        return addMapper.selectArrivalByOrderId(orderId);
    }

    @Override
    public List<OrdPickupArrivalAdd> selectPickupByOrderId(String orderId) {
        return addMapper.selectPickupByOrderId(orderId);
    }

}
