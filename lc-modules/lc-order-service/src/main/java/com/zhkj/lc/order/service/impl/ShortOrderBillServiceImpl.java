package com.zhkj.lc.order.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zhkj.lc.common.constant.CommonConstant;
import com.zhkj.lc.common.util.R;
import com.zhkj.lc.common.util.support.Convert;
import com.zhkj.lc.common.vo.SysDictVO;
import com.zhkj.lc.order.feign.SystemFeginServer;
import com.zhkj.lc.order.mapper.BillMiddleMapper;
import com.zhkj.lc.order.mapper.OrdShortOrderMapper;
import com.zhkj.lc.order.model.entity.BillMiddle;
import com.zhkj.lc.order.model.entity.OrdShortOrder;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.zhkj.lc.order.mapper.ShortOrderBillMapper;
import com.zhkj.lc.order.model.entity.ShortOrderBill;
import com.zhkj.lc.order.service.ShortOrderBillService;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Auther: HP
 * @Date: 2019/7/3 09:18
 * @Description: 
 */
@Service
public class ShortOrderBillServiceImpl extends ServiceImpl<ShortOrderBillMapper, ShortOrderBill> implements ShortOrderBillService{

    @Resource
    private ShortOrderBillMapper shortOrderBillMapper;
    @Resource
    private OrdShortOrderMapper shortOrderMapper;
    @Resource
    private BillMiddleMapper billMiddleMapper;
    @Resource
    private SystemFeginServer systemFeginServer;

    /**
     * 对账单号生成方法
     */
    public String getShortOrderBillNumber(Integer tenantId) {
        String expensespayableNumber = "YF";
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String str = format.format(new Date());
        return expensespayableNumber + String.valueOf(tenantId) + str;
    }

    @Override
    @Transactional
    public R<Boolean> cerateDZD(String ids, String loginName, Integer tenantId) {
        Integer[] idArray = Convert.toIntArray(ids);
        List<OrdShortOrder> ordShortOrderList = shortOrderMapper.selectBatchIds(Arrays.asList(idArray));
        if(ordShortOrderList != null && ordShortOrderList.size() > 0){
            ShortOrderBill bill = new ShortOrderBill();
            /*生成对账单编号*/
            String accountPayId = getShortOrderBillNumber(tenantId);
            bill.setAccountPayId(accountPayId);
            BigDecimal totalNeedPay = new BigDecimal(0);
            BigDecimal totalReceivable = new BigDecimal(0);
            for (OrdShortOrder order : ordShortOrderList) {
                /* 将订单编号和对账单编号保存到中间表 */
                BillMiddle billMiddle = new BillMiddle();
                billMiddle.setAccountPayNumber(accountPayId);
                billMiddle.setOrderNumber(order.getOrderId());
                billMiddle.setTenantId(tenantId);
                billMiddleMapper.insert(billMiddle);
                totalNeedPay = totalNeedPay.add(order.getNeedPay());
                totalReceivable = totalReceivable.add(order.getReceivables());
                bill.setClassDate(order.getClassDate());
            }
            /*修改盘短订单状态改为已加入对账单*/
            shortOrderMapper.updateOrderBillStatus(idArray);
            /*对账单数据处理*/
            bill.setNeedPay(totalNeedPay);
            bill.setReceivable(totalReceivable);
            bill.setTenantId(tenantId);
            bill.setCreateBy(loginName);
            bill.setCreateTime(new Date());
            bill.setUpdateBy(loginName);
            bill.setUpdateTime(new Date());
            bill.setDelFlag(CommonConstant.STATUS_NORMAL);
            return new R<>(shortOrderBillMapper.insert(bill)==1);
        }
        return new R<>(Boolean.FALSE);
    }

    @Override
    @Transactional
    public R<Boolean> deleteBillById(String accountBillId, String loginName, Integer tenantId) {
        /*删除对账单数据*/
        ShortOrderBill bill = new ShortOrderBill();
        bill.setUpdateBy(loginName);
        bill.setUpdateTime(new Date());
        bill.setDelFlag(CommonConstant.STATUS_DEL);
        ShortOrderBill param = new ShortOrderBill();
        param.setTenantId(tenantId);
        param.setAccountPayId(accountBillId);
        shortOrderBillMapper.update(bill, new EntityWrapper<>(param));
        /*恢复订单状态为 ： 未加入对账单*/
        BillMiddle middleParam = new BillMiddle();
        middleParam.setAccountPayNumber(accountBillId);
        middleParam.setTenantId(tenantId);
        List<BillMiddle> middles = billMiddleMapper.selectByAccountPayNumber(middleParam);
        if(middles != null && middles.size() > 0) {
            List<String>orderlist = middles.stream()
                    .map(BillMiddle::getOrderNumber)
                    .distinct()
                    .collect(Collectors.toList());
            shortOrderMapper.removeBill(orderlist.toArray(new String[orderlist.size()]), tenantId);
        }
        /*删除对账单中间表数据*/
        return new R<>(billMiddleMapper.removeAllOrder(middleParam));
    }

    @Override
    public ShortOrderBill selectOrdersByBillId(String accountBillId, Integer tenantId) {
        ShortOrderBill param = new ShortOrderBill();
        param.setAccountPayId(accountBillId);
        param.setTenantId(tenantId);
        ShortOrderBill bill = shortOrderBillMapper.selectOne(param);
        if(bill != null){
            BillMiddle middleParam = new BillMiddle();
            middleParam.setAccountPayNumber(accountBillId);
            middleParam.setTenantId(tenantId);
            List<BillMiddle> middles = billMiddleMapper.selectByAccountPayNumber(middleParam);
            if(middles != null && middles.size() > 0) {
                List<String> orderlist = middles.stream()
                        .map(BillMiddle::getOrderNumber)
                        .distinct()
                        .collect(Collectors.toList());
                List<OrdShortOrder> orderList = shortOrderMapper.selectByOrderIds(orderlist.toArray(new String[orderlist.size()]), tenantId);
                List<SysDictVO>types = systemFeginServer.findDictByType("short_order_type", tenantId);
                orderList.forEach(order -> {
                    order.setShortType(getLabel(types, order.getShortType()));
                });
                bill.setOrders(orderList);
            }
        }
        return bill;
    }

    @Override
    @Transactional
    public R<Boolean> removeShortOrder(String accountBillId, String orderId,String loginName, Integer tenantId) {
        /*移除中间对账单中间表*/
        BillMiddle billMiddle = new BillMiddle();
        billMiddle.setAccountPayNumber(accountBillId);
        billMiddle.setOrderNumber(orderId);
        billMiddleMapper.removeOrder(billMiddle);
        /*修改订单状态*/
        String[] orderIds = {orderId};
        shortOrderMapper.removeBill(orderIds, tenantId);
        /*修改对账单数据*/
        ShortOrderBill bill = shortOrderBillMapper.selectByAccountBillId(accountBillId);
        OrdShortOrder order = shortOrderMapper.selectShortOrderByOrderId(orderId);
        bill.setReceivable(bill.getReceivable().subtract(order.getReceivables()));
        bill.setNeedPay(bill.getNeedPay().subtract(order.getNeedPay()));
        bill.setUpdateTime(new Date());
        bill.setUpdateBy(loginName);
        return new R<>(shortOrderBillMapper.updateByPrimaryKey(bill));
    }

    //获取字典lable
    public String getLabel(List<SysDictVO>dicts, String value){
        if(value==null ||("").equals(value)){
            return null;
        }
        for (SysDictVO type:dicts) {
            if(value.equals(type.getValue())){
                return type.getLabel();
            }
        }
        return null;
    }
}
