package com.zhkj.lc.oilcard.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.util.support.Convert;
import com.zhkj.lc.common.vo.DriverVO;
import com.zhkj.lc.common.vo.TruckVO;
import com.zhkj.lc.oilcard.feign.TruckFeign;
import com.zhkj.lc.oilcard.mapper.OilExceptionMapper;
import com.zhkj.lc.oilcard.model.OilException;
import com.zhkj.lc.oilcard.service.IOilExceptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 油卡异常表 服务实现类
 * </p>
 *
 * @author wzc
 * @since 2018-12-07
 */
@Service
public class OilExceptionServiceImpl extends ServiceImpl<OilExceptionMapper, OilException> implements IOilExceptionService {

    @Autowired private OilExceptionMapper oilExceptionMapper;
    @Autowired private TruckFeign truckFeign;
    @Autowired private DictService dictService;

    @Override
    public OilException selectByExceptionId(Integer exceptionId) {
        OilException oilException = oilExceptionMapper.selectByExceptionId(exceptionId);
        if (null != oilException.getTruckId()) {
            TruckVO truckVO = truckFeign.getTruckByid(oilException.getTruckId());
            if (null != truckVO) {
                oilException.setPlateNumber(truckVO.getPlateNumber());
                oilException.setAttribute(truckVO.getAttribute());
                oilException.setTruckOwnerPhone(truckVO.getTruckOwnerPhone());
                oilException.setTruckOwner(truckVO.getTruckOwner());
            }
        }
        if (null != oilException.getOwnerDriverId()) {
            DriverVO driverVO = truckFeign.getDriverByid(oilException.getOwnerDriverId());
            if (null != driverVO) {
                oilException.setDriverPhone(driverVO.getPhone());
                oilException.setDriverName(driverVO.getDriverName());
            }
        }
        return oilException;
    }

    @Override
    public List<OilException> selectListByIds(String ids){
        return listDict(oilExceptionMapper.selectListByIds(Convert.toStrArray(ids)));
    }

    @Override
    public List<OilException> selectPageList(OilException oilException){
        List<OilException> exceptionList = new ArrayList<>();
        Map<Boolean,  String[]> map = dictService.booleanTruckIds(oilException.getTruckId(), oilException.getAttribute());
        Boolean flag = true;
        for (Map.Entry<Boolean,  String[]> entry : map.entrySet()) {
            flag = entry.getKey();
            oilException.setTruckIds(entry.getValue());
        }
        if (flag) {
            exceptionList = listDict(oilExceptionMapper.selectPageList(oilException));
        }
        return exceptionList;
    }

    @Override
    public Page<OilException> selectPageList(Query query, OilException oilException) {

        List<OilException> exceptionList = new ArrayList<>();
        Map<Boolean,  String[]> map = dictService.booleanTruckIds(oilException.getTruckId(), oilException.getAttribute());
        Boolean flag = true;
        for (Map.Entry<Boolean,  String[]> entry : map.entrySet()) {
            flag = entry.getKey();
            oilException.setTruckIds(entry.getValue());
        }
        if (flag) {
            exceptionList = oilExceptionMapper.selectPageList(query, oilException);
            if (null != exceptionList && exceptionList.size() > 0) {
                TruckVO truckVO;
                DriverVO driverVO;
                for (int i = 0; i < exceptionList.size(); i++) {
                    if (null != exceptionList.get(i).getTruckId()) {
                        truckVO = truckFeign.getTruckByid(exceptionList.get(i).getTruckId());
                        if (null != truckVO) {
                            exceptionList.get(i).setPlateNumber(truckVO.getPlateNumber());
                            exceptionList.get(i).setAttribute(truckVO.getAttribute());
                            exceptionList.get(i).setTruckOwnerPhone(truckVO.getTruckOwnerPhone());
                            exceptionList.get(i).setTruckOwner(truckVO.getTruckOwner());
                        }
                    }
                    if (null != exceptionList.get(i).getOwnerDriverId()) {
                        driverVO = truckFeign.getDriverByid(exceptionList.get(i).getOwnerDriverId());
                        if (null != driverVO) {
                            exceptionList.get(i).setDriverPhone(driverVO.getPhone());
                            exceptionList.get(i).setDriverName(driverVO.getDriverName());
                        }
                    }
                }
            }
        }
        query.setRecords(exceptionList);
        return query;
    }

    private List<OilException> listDict(List<OilException> list){
        if (null != list && list.size()>0) {
            TruckVO truckVO;
            DriverVO driverVO;
            for (int i = 0; i < list.size(); i++) {
                if (null != list.get(i).getCardType() && "" != list.get(i).getCardType()) {
                    list.get(i).setCardType(dictService.getLabel("oilcard_type", list.get(i).getCardType(), list.get(i).getTenantId()));
                }
                if (null != list.get(i).getTruckId()) {
                    truckVO = truckFeign.getTruckByid(list.get(i).getTruckId());
                    if (null != truckVO) {
                        list.get(i).setPlateNumber(truckVO.getPlateNumber());
                        if (null != truckVO.getAttribute() && "" != truckVO.getAttribute()) {
                            list.get(i).setAttribute(dictService.getLabel("truck_attribute", truckVO.getAttribute(), list.get(i).getTenantId()));
                        }
                        list.get(i).setTruckOwnerPhone(truckVO.getTruckOwnerPhone());
                        list.get(i).setTruckOwner(truckVO.getTruckOwner());
                    }
                }
                if (null != list.get(i).getOwnerDriverId()) {
                    driverVO = truckFeign.getDriverByid(list.get(i).getOwnerDriverId());
                    if (null != driverVO) {
                        list.get(i).setDriverName(driverVO.getDriverName());
                        list.get(i).setDriverPhone(driverVO.getPhone());
                    }
                }
                list.get(i).setExceptionType(dictService.getLabel("oilexception_type", list.get(i).getExceptionType(), list.get(i).getTenantId()));
                list.get(i).setStatus(dictService.getLabel("oilexception_status", list.get(i).getStatus(), list.get(i).getTenantId()));
            }
        }
        return list;
    }

    /**
     * 根据车主姓名查找(没用到)
     */
    @Override
    public OilException selectByOwner(String truckOwner){
        return oilExceptionMapper.selectByOwner(truckOwner);
    }

    /**
     * 根据司机id查找(没用到)
     */
    @Override
    public OilException selectByDriver(Integer driverId){
        return oilExceptionMapper.selectByDriver(driverId);
    }

    /**
     * 删除油卡异常对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public Boolean deleteExceptionByIds(String ids)
    {
        return oilExceptionMapper.deleteExceptionByIds(Convert.toStrArray(ids));
    }
}
