package com.zhkj.lc.oilcard.service.impl;

import com.zhkj.lc.common.vo.DriverVO;
import com.zhkj.lc.common.vo.SysDictVO;
import com.zhkj.lc.common.vo.TruckVO;
import com.zhkj.lc.oilcard.feign.DictFeign;
import com.zhkj.lc.oilcard.feign.TruckFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DictService {

    @Autowired private DictFeign dictFeign;
    @Autowired private TruckFeign truckFeign;

    /**
     * 根据字典类型查询字典数据信息
     *
     * @param dictType 字典类型
     * @return 参数键值
     */
    public List<SysDictVO> getType(String dictType, Integer tenantId)
    {
        if (null != dictType && !dictType.equals("")){
            return dictFeign.findDictByType(dictType, tenantId);
        }else {
            return null;
        }
    }

    /**
     * 根据字典类型和字典键值查询字典数据信息 ${@dict.getLabel(a,b)}
     *
     * @param dictType 字典类型
     * @param dictValue 字典键值
     * @return 字典标签
     */
    public String getLabel(String dictType, String dictValue, Integer tenantId)
    {
        String lable = null;
        if (null != dictValue && !dictValue.equals("")){
            SysDictVO sysDictVO = new SysDictVO();
            sysDictVO.setType(dictType);
            sysDictVO.setValue(dictValue);
            sysDictVO.setTenantId(tenantId);
            SysDictVO dictVO = dictFeign.selectDict(sysDictVO);
            if (null != dictVO){
                lable = dictVO.getLabel();
            }
        }
        return lable;
    }

    public Map<Boolean, String[]> booleanTruckIds(Integer truckId, String attribute){
        Map<Boolean, String[]> map = new HashMap();
        boolean flag = true;
        String[] truckIds = new String[]{};
        if (null != truckId){
            if (null != attribute) {
                if (null != truckFeign.getTruckByid(truckId)) {
                    if (!attribute.equals(truckFeign.getTruckByid(truckId).getAttribute())) {
                        flag = false;
                    }
                }
            }
            if (flag) truckIds = new String[]{truckId.toString()};
        }else {
            if (null != attribute) {
                TruckVO truckVO = new TruckVO();
                truckVO.setAttribute(attribute);
                List<TruckVO> listTruck = truckFeign.listTruck(truckVO);
                if (null != listTruck && listTruck.size() > 0){
                    truckIds = new String[listTruck.size()];
                    for (int i = 0; i < listTruck.size(); i++){
                        truckIds[i] = listTruck.get(i).getTruckId().toString();
                    }
                }
            }
        }
        map.put(flag, truckIds);
        return map;
    }

    public String[] getDriverIds(String driverName){
        String[] driverIds = new String[]{};
        if (null != driverName){
            DriverVO driverVO = new DriverVO();
            driverVO.setDriverName(driverName);
            List<DriverVO> list = truckFeign.selectDriverByplateId(driverVO);
            if (null != list && list.size() > 0){
                driverIds = new String[list.size()];
                for (int i = 0; i < list.size(); i++){
                    driverIds[i] = list.get(i).getDriverId().toString();
                }
            }
        }
        return driverIds;
    }
}
