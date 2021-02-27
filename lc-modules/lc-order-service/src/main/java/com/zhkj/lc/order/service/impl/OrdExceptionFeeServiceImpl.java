package com.zhkj.lc.order.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zhkj.lc.common.constant.CommonConstant;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.util.StringUtils;
import com.zhkj.lc.common.util.UserUtils;
import com.zhkj.lc.common.util.support.Convert;
import com.zhkj.lc.common.vo.SysDictVO;
import com.zhkj.lc.order.dto.OrdCommonTruckVO;
import com.zhkj.lc.order.feign.SystemFeginServer;
import com.zhkj.lc.order.mapper.OrdExceptionFeeMapper;
import com.zhkj.lc.order.mapper.OrdOrderMapper;
import com.zhkj.lc.order.model.entity.NeedPayBill;
import com.zhkj.lc.order.model.entity.OrdCommonTruck;
import com.zhkj.lc.order.model.entity.OrdExceptionFee;
import com.zhkj.lc.order.model.entity.OrdOrder;
import com.zhkj.lc.order.service.INeedPayBillService;
import com.zhkj.lc.order.service.IOrdCommonTruckService;
import com.zhkj.lc.order.service.IOrdExceptionFeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wzc
 * @since 2019-01-11
 */
@Service
public class OrdExceptionFeeServiceImpl extends ServiceImpl<OrdExceptionFeeMapper, OrdExceptionFee> implements IOrdExceptionFeeService {

    @Autowired
    private OrdExceptionFeeMapper ordExceptionFeeMapper;
    @Autowired
    private SystemFeginServer systemFegin;
    @Autowired
    private OrdOrderMapper ordOrderMapper;
    @Autowired
    private INeedPayBillService needPayBillService;
    @Autowired
    private IOrdCommonTruckService commonTruckService;

    @Override
    public OrdExceptionFee getByid(Integer id) {
        OrdExceptionFee exceptionFee = ordExceptionFeeMapper.getByid(id);
        if(null !=exceptionFee.getApplyBy() && ! exceptionFee.getApplyBy().equals("")){
            exceptionFee.setDriverName(exceptionFee.getApplyBy()+"(后台添加）");
        }
        if(null != exceptionFee.getImgUrl() && exceptionFee.getImgUrl() != ""){
            exceptionFee.setImgUrlFile(exceptionFee.getImgUrl().split(","));
        }else{
            exceptionFee.setImgUrlFile(new String[0]);
        }
        return exceptionFee;
    }

    @Override
    public List<OrdExceptionFee> selectOrdExceptionFeeListByIds(String ids) {
        List<OrdExceptionFee> ordExceptionFeeList = ordExceptionFeeMapper.selectOrdExceptionFeeListByIds(Convert.toStrArray(ids));
        return exportTO(ordExceptionFeeList);
    }

    @Override
    public List<OrdExceptionFee> selectOrdExceptionFeeList(OrdExceptionFee ordExceptionFee) {
        List<OrdExceptionFee> ordExceptionFeeList = ordExceptionFeeMapper.selectOrdExceptionFeeList(ordExceptionFee);
        return exportTO(ordExceptionFeeList);
    }

    @Override
    public Page<OrdExceptionFee> selectOrdExceptionFeeList(Query query, OrdExceptionFee ordExceptionFee) {

        List<OrdExceptionFee> list = ordExceptionFeeMapper.selectOrdExceptionFeeList(query,ordExceptionFee);
        if (null != list) {
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setExceptionFeeType(getLable(list.get(i).getExceptionFeeType(),"exception_fee_type", ordExceptionFee.getTenantId()));
                list.get(i).setAuditing(getLable(list.get(i).getAuditing(),"auditing_type", ordExceptionFee.getTenantId()));
            }
        }
        query.setRecords(list);
        return query;
    }

    @Override
    public boolean insertOrdExceptionFee(OrdExceptionFee ordExceptionFee) {
        Integer result = ordExceptionFeeMapper.insertOrdExceptionFee(ordExceptionFee);
        return null != result && result == 1;
    }

    @Override
    @Transactional
    public boolean updateOrdExceptionFee(OrdExceptionFee ordExceptionFee, OrdOrder ordOrder, OrdCommonTruckVO truckVO) {
        BigDecimal zero = new BigDecimal(0);
        if(ordOrder != null){
            /*判断为单结*/
            if(ordOrder.getSettlement().equals("0")){
                OrdOrder order = new OrdOrder();
                order.setOrderId(ordOrder.getOrderId());
                order.setNeedPayStatus(CommonConstant.WFP);/*未分配状态*/
                order.setPayCash(zero);
                order.setCash(zero);
                order.setIsYFInvoice("0");
                order.setUpdateTime(new Date());
                order.setUpdateBy(UserUtils.getUser());
                order.setEtcFee(zero);
                order.setOilPledge(zero);
                order.setTransOilFee(zero);
                OrdOrder condition = new OrdOrder();
                condition.setOrderId(ordOrder.getOrderId());
                ordOrderMapper.update(order, new EntityWrapper<>(condition));
            }else {
                /*如果已加入对账单*/
                if(ordOrder.getIfAddToYfbill().equals("1")){
                    NeedPayBill needPayBill = needPayBillService.selectBillByOrderId(ordOrder.getOrderId(), ordExceptionFee.getTenantId());
                    if(needPayBill != null && !needPayBill.getSettlementStatus().equals("0")){
                        NeedPayBill need = new NeedPayBill();
                        need.setSettlementStatus(CommonConstant.WFP);
                        need.setEtcFee(zero);
                        need.setPayCash(zero);
                        need.setFreightOilcardFee(zero);
                        need.setOilPledge(zero);
                        need.setNeedPayCash(zero);
                        need.setId(needPayBill.getId());
                        needPayBillService.updateById(need);
                    }
                }
            }
        }else if(truckVO != null){
            /*判断为单结*/
            if(truckVO.getPayType().equals("0")){
                OrdCommonTruck commonTruck = new OrdCommonTruck();
                commonTruck.setOrderId(truckVO.getOrderId());
                commonTruck.setCash(zero);
                commonTruck.setPayCash(zero);
                commonTruck.setNeedPayStatus(CommonConstant.WFP);
                commonTruck.setIsYFInvoice("0");
                commonTruck.setEtcFee(zero);
                commonTruck.setOilPledge(zero);
                commonTruck.setTransOilFee(zero);
                commonTruckService.updateById(commonTruck);
            }else {
                /*如果已加入对账单*/
                if(truckVO.getIfAddToYfbill().equals("1")){
                    NeedPayBill needPayBill = needPayBillService.selectBillByOrderId(truckVO.getOrderId(), ordExceptionFee.getTenantId());
                    if(needPayBill != null && !needPayBill.getSettlementStatus().equals("0")){
                        NeedPayBill need = new NeedPayBill();
                        need.setSettlementStatus(CommonConstant.WFP);
                        need.setEtcFee(zero);
                        need.setPayCash(zero);
                        need.setFreightOilcardFee(zero);
                        need.setOilPledge(zero);
                        need.setNeedPayCash(zero);
                        need.setId(needPayBill.getId());
                        needPayBillService.updateById(need);
                    }
                }
            }
        }

        Integer result = ordExceptionFeeMapper.updateOrdExceptionFee(ordExceptionFee);
        return null != result && result == 1;
    }

    @Override
    public boolean deleteOrdExceptionFeeByIds(Integer tenantId, String updateBy, String ids) {
        Integer result = ordExceptionFeeMapper.deleteOrdExceptionFeeByIds(CommonConstant.STATUS_DEL,tenantId,updateBy,Convert.toStrArray(ids));
        return null != result && result >= 1;
    }

    private List<OrdExceptionFee> exportTO(List<OrdExceptionFee> ordExceptionFeeList){
        if (null != ordExceptionFeeList) {
            for (int i = 0; i < ordExceptionFeeList.size(); i++) {
                ordExceptionFeeList.get(i).setExceptionFeeType(getLable(ordExceptionFeeList.get(i).getExceptionFeeType(), "exception_fee_type",ordExceptionFeeList.get(i).getTenantId()));
                ordExceptionFeeList.get(i).setAuditing(getLable(ordExceptionFeeList.get(i).getAuditing(),"auditing_type",ordExceptionFeeList.get(i).getTenantId()));
                ordExceptionFeeList.get(i).setStartPlace(ordExceptionFeeList.get(i).getStartPlace());
                ordExceptionFeeList.get(i).setEndPlace(ordExceptionFeeList.get(i).getEndPlace());
                if(null != ordExceptionFeeList.get(i).getImgUrl()){
                    ordExceptionFeeList.get(i).setImgUrlFile(Convert.toStrArray(ordExceptionFeeList.get(i).getImgUrl()));
                }
            }
        }
        return ordExceptionFeeList;
    }

    private String getLable(String value, String type, Integer tenantId){
        if (null != value && !value.equals("") && null != systemFegin.findDictByType(type, tenantId)) {
            SysDictVO sysDictVO = new SysDictVO();
            sysDictVO.setType(type);
            sysDictVO.setValue(value);
            sysDictVO.setTenantId(tenantId);
            return systemFegin.selectDict(sysDictVO).getLabel();
        }
        return value;
    }

    /**
     * 根据订单id查询该订单下所有异常费用
     * @param orderId
     * @return
     */
    public List<OrdExceptionFee> selectExFee(String orderId){
        List<OrdExceptionFee> list = ordExceptionFeeMapper.selectExFee(orderId);
        if(list != null && list.size() > 0) {
            List<SysDictVO> dicts = systemFegin.findDictByType("exception_fee_type", list.get(0).getTenantId());
            for (OrdExceptionFee fee : list) {
                for (SysDictVO dictVO : dicts) {
                    if(fee.getExceptionFeeType().equals(dictVO.getValue())){
                        fee.setType(dictVO.getLabel());
                    }
                }
            }
        }
        return list;
    }
}
