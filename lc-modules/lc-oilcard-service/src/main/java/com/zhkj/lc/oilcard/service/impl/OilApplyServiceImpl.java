package com.zhkj.lc.oilcard.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zhkj.lc.common.constant.CommonConstant;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.util.support.Convert;
import com.zhkj.lc.common.vo.DriverVO;
import com.zhkj.lc.common.vo.TruckVO;
import com.zhkj.lc.oilcard.feign.TruckFeign;
import com.zhkj.lc.oilcard.mapper.OilApplyMapper;
import com.zhkj.lc.oilcard.model.OilApply;
import com.zhkj.lc.oilcard.service.IOilApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 油卡申请表 服务实现类
 * </p>
 *
 * @author wzc
 * @since 2018-12-07
 */
@Service
public class OilApplyServiceImpl extends ServiceImpl<OilApplyMapper, OilApply> implements IOilApplyService {

    @Autowired private OilApplyMapper oilApplyMapper;
    @Autowired private TruckFeign truckFeign;
    @Autowired private DictService dictService;

    @Override
    public OilApply selectByApplyId(Integer applyId) {
        OilApply oilApply = oilApplyMapper.selectByApplyId(applyId);
        TruckVO truckVO = truckFeign.getTruckByid(oilApply.getTruckId());
        if (null != oilApply.getTruckId()) {
            if (null != truckVO) {
                oilApply.setAttribute(truckVO.getAttribute());
                oilApply.setPlateNumber(truckVO.getPlateNumber());
                oilApply.setTruckOwnerPhone(truckVO.getTruckOwnerPhone());
                oilApply.setTruckOwner(truckVO.getTruckOwner());
            }
        }
        if (null != oilApply.getOwnerDriverId()) {
            DriverVO driverVO = truckFeign.getDriverByid(oilApply.getOwnerDriverId());
            if (null != driverVO) {
                oilApply.setDriverPhone(driverVO.getPhone());
                oilApply.setDriverName(driverVO.getDriverName());
            }
        }
        return oilApply;
    }

    @Override
    public List<OilApply> selectApplyLisIds(String ids) {
        return listDict(oilApplyMapper.selectApplyLisIds(Convert.toStrArray(ids)));
    }

    @Override
    public List<OilApply> selectApplyList(OilApply oilApply) {

        List<OilApply> list = new ArrayList<>();
        Map<Boolean,  String[]> map = dictService.booleanTruckIds(oilApply.getTruckId(), oilApply.getAttribute());
        Boolean flag = true;
        for (Map.Entry<Boolean,  String[]> entry : map.entrySet()) {
            flag = entry.getKey();
            oilApply.setTruckIds(entry.getValue());
        }
        if (flag) {
            list = listDict(oilApplyMapper.selectApplyList(oilApply));
        }
        return list;
    }

    @Override
    public Page<OilApply> selectApplyListPage(Query query, OilApply oilApply) {
        Map<Boolean, String[]> map = dictService.booleanTruckIds(oilApply.getTruckId(), oilApply.getAttribute());
        Boolean flag = true;
        for (Map.Entry<Boolean,  String[]> entry : map.entrySet()) {
            flag = entry.getKey();
            oilApply.setTruckIds(entry.getValue());
        }
        List<OilApply> list = new ArrayList<>();
        if (flag) {
            list = oilApplyMapper.selectApplyList(query, oilApply);
            if (null != list && list.size() > 0) {
                TruckVO truckVO;
                DriverVO driverVO;
                for (int i = 0; i < list.size(); i++) {
                    if (null != list.get(i).getTruckId()) {
                        truckVO = truckFeign.getTruckByid(list.get(i).getTruckId());
                        if (null != truckVO) {
                            list.get(i).setAttribute(truckVO.getAttribute());
                            list.get(i).setPlateNumber(truckVO.getPlateNumber());
                            list.get(i).setTruckOwnerPhone(truckVO.getTruckOwnerPhone());
                            list.get(i).setTruckOwner(truckVO.getTruckOwner());
                        }
                    }
                    if (null != list.get(i).getOwnerDriverId()) {
                        driverVO = truckFeign.getDriverByid(list.get(i).getOwnerDriverId());
                        if (null != driverVO) {
                            list.get(i).setDriverPhone(driverVO.getPhone());
                            list.get(i).setDriverName(driverVO.getDriverName());
                        }
                    }
                }
            }
        }
        query.setRecords(list);
        return query;
    }

    @Override
    public OilApply selectByOilCardNumber(String oilCardNumber, Integer tenantId) {
        return oilApplyMapper.selectByOilCardNumber(oilCardNumber, tenantId);
    }

    @Override
    public List<String> selectOpenCardPlace(Integer tenantId) {
        return oilApplyMapper.selectOpenCardPlace(tenantId);
    }

    @Override
    public boolean updateApply(OilApply oilApply) {
        Integer result = oilApplyMapper.updateApply(oilApply);
        return null != result && result == 1;
    }

    @Override
    public boolean deleteApplyByIds(String ids, String updateBy) {

        Integer result = oilApplyMapper.deleteApplyByIds(CommonConstant.STATUS_DEL, updateBy, Convert.toStrArray(ids));
        return null != result && result >= 1;
    }

    @Override
    public boolean insertOilApply(OilApply oilApply) {
        Integer result = oilApplyMapper.insertOilApply(oilApply);
        return null != result && result == 1;
    }

    private List<OilApply> listDict(List<OilApply> list){
        if (null != list && list.size()>0) {
            TruckVO truckVO;
            DriverVO driverVO;
            for (int i = 0; i < list.size(); i++) {
                if (null != list.get(i).getTruckId()) {
                    truckVO = truckFeign.getTruckByid(list.get(i).getTruckId());
                    if (null != truckVO) {
                        list.get(i).setPlateNumber(truckVO.getPlateNumber());
                        list.get(i).setTruckOwnerPhone(truckVO.getTruckOwnerPhone());
                        list.get(i).setTruckOwner(truckVO.getTruckOwner());
                        list.get(i).setAttribute(dictService.getLabel("truck_attribute", truckVO.getAttribute(), list.get(i).getTenantId()));
                    }
                }

                list.get(i).setApplyCardType(dictService.getLabel("oilcard_type", list.get(i).getApplyCardType(), list.get(i).getTenantId()));
                list.get(i).setIsPassed(dictService.getLabel("auditing_type", list.get(i).getIsPassed(), list.get(i).getTenantId()));
                list.get(i).setGetStatus(dictService.getLabel("auditing_type", list.get(i).getGetStatus(), list.get(i).getTenantId()));
                if (null != list.get(i).getOwnerDriverId()) {
                    driverVO = truckFeign.getDriverByid(list.get(i).getOwnerDriverId());
                    if (null != driverVO) {
                        list.get(i).setDriverName(driverVO.getDriverName());
                        list.get(i).setDriverPhone(driverVO.getPhone());
                    }
                }
            }
        }
        return list;
    }

}
