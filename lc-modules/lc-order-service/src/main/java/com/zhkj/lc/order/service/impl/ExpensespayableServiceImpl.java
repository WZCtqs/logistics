package com.zhkj.lc.order.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zhkj.lc.common.constant.CommonConstant;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.util.excel.ExcelUtil;
import com.zhkj.lc.common.vo.CustomerVO;
import com.zhkj.lc.common.vo.DriverVO;
import com.zhkj.lc.common.vo.SysDictVO;
import com.zhkj.lc.common.vo.TruckVO;
import com.zhkj.lc.order.dto.*;
import com.zhkj.lc.order.feign.CommonGoodsTruckFeign;
import com.zhkj.lc.order.feign.SystemFeginServer;
import com.zhkj.lc.order.feign.TrunkFeign;
import com.zhkj.lc.order.mapper.BillMiddleMapper;
import com.zhkj.lc.order.mapper.ExpensespayableMapper;
import com.zhkj.lc.order.mapper.OrdPickupArrivalAddMapper;
import com.zhkj.lc.order.model.entity.BillMiddle;
import com.zhkj.lc.order.model.entity.Expensespayable;
import com.zhkj.lc.order.model.entity.OrdPickupArrivalAdd;
import com.zhkj.lc.order.service.IExpensespayableService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 应收对账单表 服务实现类
 * </p>
 *
 * @author wzc
 * @since 2019-02-15
 */
@Service
public class  ExpensespayableServiceImpl extends ServiceImpl<ExpensespayableMapper, Expensespayable> implements IExpensespayableService {
    @Autowired
    ExpensespayableMapper expensespayableMapper;
    @Autowired
    private TrunkFeign truckFeign;
    @Autowired
    private BillMiddleMapper billMiddleMapper;
    @Autowired
    private OrdOrderServiceImpl orderServiceImpl;
    @Autowired
    private SystemFeginServer systemFeginServer;
    @Autowired
    private OrdPickupArrivalAddMapper addMapper;

    @Override
    public Boolean deleteByIds(String ids) {
        String[] str = ids.split(",");
// 声明一个int类型的数组.数组长度和String类型的数组长度一致
        int[] expensespayableids = new int[str.length];
// 对String数组进行遍历循环，并转换成int类型的数组
        for (int i = 0; i < str.length; i++) {
            expensespayableids[i] = Integer.parseInt(str[i]);
        }
        Expensespayable expensespayable = new Expensespayable();
        expensespayable.setIds(expensespayableids);
        expensespayable.setUpdateTime(new Date());
        /*oilApply.setUpdateBy("");*/
        Integer result = expensespayableMapper.deleteByIds(expensespayable);
        return null != result && result >= 1;
    }

    @Override
    public Page selectCnOrderList(Query query, FinanceQueryDTO financeQueryDTO) {
        /*获取车辆类型下的车牌号并加入查询条件*/
        ArrayList<String> trucks = orderServiceImpl.getPlates(financeQueryDTO.getTruckAttribute());
        String [] plates =  new String[trucks.size()];
        trucks.toArray(plates);
        financeQueryDTO.setPlates(plates);
        /*获取车辆类型字段数据*/
        List<SysDictVO> attrs = systemFeginServer.findDictByType("truck_attribute", financeQueryDTO.getTenantId());
        List<ReceiveBillOrder> receiveBillOrders = expensespayableMapper.selectCnOrderList(query, financeQueryDTO);
        //处理账单详情
        for (ReceiveBillOrder rbo : receiveBillOrders) {
            //获取客户利率及姓名
            CustomerVO customerVO = getCustomerByCustomerId(rbo.getCustomerId(), financeQueryDTO.getTenantId());
            rbo.setCustomerName(customerVO.getCustomerName() != null ? customerVO.getCustomerName() : "");
            /*处理车辆信息*/
            TruckVO truckVO = new TruckVO();
            truckVO.setTenantId(financeQueryDTO.getTenantId());
            truckVO.setPlateNumber(rbo.getPlateNumber());
            List<TruckVO> truck = truckFeign.selectTruckList(truckVO);
            if(truck != null && truck.size() == 1){
                rbo.setTruckAttribute(orderServiceImpl.getLabel(attrs, truck.get(0).getAttribute()));
            }
        }
        query.setRecords(receiveBillOrders);
        return query;
    }

    @Override
    public Page selectPhOrderList(Query query, FinanceQueryDTO financeQueryDTO) {
        /*获取车辆类型下的车牌号并加入查询条件*/
        ArrayList<String> trucks = orderServiceImpl.getPlates(financeQueryDTO.getTruckAttribute());
        String [] plates =  new String[trucks.size()];
        trucks.toArray(plates);
        financeQueryDTO.setPlates(plates);
        /*获取车辆类型字段数据*/
        List<SysDictVO> attrs = systemFeginServer.findDictByType("truck_attribute", financeQueryDTO.getTenantId());
        List<ReceiveBillCommonGoods> receiveBillCommonGoods = expensespayableMapper.selectPhOrderList(query, financeQueryDTO);
        //处理账单详情
        for (ReceiveBillCommonGoods rbcg : receiveBillCommonGoods) {
            //获取客户利率及姓名
            CustomerVO customerVO = getCustomerByCustomerId(rbcg.getCustomerId(), financeQueryDTO.getTenantId());
            rbcg.setCustomerName(customerVO.getCustomerName() != null ? customerVO.getCustomerName() : "");
            /*处理车辆信息*/
            TruckVO truckVO = new TruckVO();
            truckVO.setTenantId(financeQueryDTO.getTenantId());
            truckVO.setPlateNumber(rbcg.getPlateNumber());
            List<TruckVO> truck = truckFeign.selectTruckList(truckVO);
            if(truck != null && truck.size() == 1){
                rbcg.setTruckAttribute(orderServiceImpl.getLabel(attrs, truck.get(0).getAttribute()));
            }
        }
        query.setRecords(receiveBillCommonGoods);
        return query;
    }

    @Override
    public Page selectPdOrderList(Query query, FinanceQueryDTO financeQueryDTO) {
        List<ReceiveBillShortOrder> receiveBillShortOrders = expensespayableMapper.selectPdOrderList(query, financeQueryDTO);
        query.setRecords(receiveBillShortOrders);
        return query;
    }

    @Override
    public BigDecimal getTotalFee(FinanceQueryDTO financeQueryDTO) {
        /*获取车辆类型下的车牌号并加入查询条件*/
        ArrayList<String> trucks = orderServiceImpl.getPlates(financeQueryDTO.getTruckAttribute());
        String [] plates =  new String[trucks.size()];
        trucks.toArray(plates);
        financeQueryDTO.setPlates(plates);
        BigDecimal totalFee = new BigDecimal(0);
        switch (financeQueryDTO.getOrderType()){
            case "0" :
                List<ReceiveBillOrder> receiveBillOrders = expensespayableMapper.selectCnOrderList(financeQueryDTO);
                for (ReceiveBillOrder rbo : receiveBillOrders) {
                    totalFee = totalFee.add(rbo.getTotalFee());
                }
                break;
            case "1" :
                List<ReceiveBillCommonGoods> receiveBillCommonGoods = expensespayableMapper.selectPhOrderList(financeQueryDTO);
                for (ReceiveBillCommonGoods rbo : receiveBillCommonGoods) {
                    totalFee = totalFee.add(rbo.getTotalFee());
                }
                break;
        }
        return totalFee;
    }

    //根据订单号统计订单总费用
    @Override
    public BigDecimal countOrderTotalFee(String[] orderIds, Integer tenantId) {
        return expensespayableMapper.countOrderTotalFee(orderIds, tenantId);
    }

    //根据订单号统计订单总费用
    @Override
    public BigDecimal countCommonGoodsTotalFee(String[] orderIds, Integer tenantId) {
        return expensespayableMapper.countCommonGoodsTotalFee(orderIds, tenantId);
    }

    //根据订单号统计订单总费用
    @Override
    public BigDecimal countShortOrderTotalFee(String[] orderIds, Integer tenantId) {
        return expensespayableMapper.countShortOrderTotalFee(orderIds, tenantId);
    }

    @Override
    public Boolean SettlementProcess(Expensespayable expensespayable) {
        return expensespayableMapper.SettlementProcess(expensespayable);
    }

    @Override
    public List<Expensespayable> selectByIds(String ids) {
        String[] str = ids.split(",");
// 声明一个int类型的数组.数组长度和String类型的数组长度一致
        int[] expensespayableids = new int[str.length];
// 对String数组进行遍历循环，并转换成int类型的数组
        for (int i = 0; i < str.length; i++) {
            expensespayableids[i] = Integer.parseInt(str[i]);
        }
        Expensespayable expensespayable = new Expensespayable();
        expensespayable.setIds(expensespayableids);
        return expensespayableMapper.selectByIds(expensespayable);
    }

    @Override
    public BigDecimal countTodayMoney(Integer tenantId) {
        return expensespayableMapper.countTodayMoney(tenantId);
    }

    /**
     * 根据客户id获取客户信息
     *
     * @param customerId
     * @param tenantId
     * @return
     */
    public CustomerVO getCustomerByCustomerId(Integer customerId, Integer tenantId) {
        CustomerVO customerVO = new CustomerVO();
        customerVO.setCustomerId(customerId);
        customerVO.setTenantId(tenantId);
        List<CustomerVO> result = truckFeign.selectAllForFegin(customerVO);
        if(result != null){
            return result.get(0);
        }
        return null;
    }

    /**
     * 将利率转为百分比字符串
     *
     * @param rate
     * @return
     */
    public String getRateStr(BigDecimal rate) {
        NumberFormat percent = NumberFormat.getPercentInstance();
        percent.setMaximumFractionDigits(2);
        return percent.format(rate.doubleValue());
    }

    @Override
    public Boolean exportBillByQuery(HttpServletRequest request, HttpServletResponse response, FinanceQueryDTO queryDTO) {
        //导出集装箱账单
        try {
            /*获取车辆类型字段数据*/
            List<SysDictVO> attrs = systemFeginServer.findDictByType("truck_attribute", queryDTO.getTenantId());
            if (queryDTO.getOrderType().equals("0")) {
                List<ReceiveBillOrder> cnPayDetails = expensespayableMapper.selectCnOrderList(queryDTO);
                dealReceiveBillOrderList(cnPayDetails,attrs,queryDTO.getTenantId());
                DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                String excelName = fmt.format(new Date()) + "-集装箱账单信息";
                ExcelUtil<ReceiveBillOrder> util = new ExcelUtil<>(ReceiveBillOrder.class);
                util.exportExcel(request, response, cnPayDetails, excelName, null);
            }

            //导出普货账单
            else if (queryDTO.getOrderType().equals("1")) {
                List<ReceiveBillCommonGoods> phNeedPayDetails = expensespayableMapper.selectPhOrderList(queryDTO);
                dealReceiveBillCommonGoodsList(phNeedPayDetails,attrs,queryDTO.getTenantId());
                DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                String excelName = fmt.format(new Date()) + "-普货账单信息";
                ExcelUtil<ReceiveBillCommonGoods> util = new ExcelUtil<>(ReceiveBillCommonGoods.class);
                util.exportExcel(request, response, phNeedPayDetails, excelName, null);
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    @Override
    public Boolean exportExpensespayable(HttpServletRequest request, HttpServletResponse response, Expensespayable expensespayable) {
        //导出对账单
        List<Expensespayable> expensespayables = expensespayableMapper.selectExpensespayableByIds(expensespayable);
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        String excelName = fmt.format(new Date()) + "-对账单信息";
        ExcelUtil<Expensespayable> util = new ExcelUtil<>(Expensespayable.class);
        util.exportExcel(request, response, expensespayables, excelName, null);
        return true;
    }

    @Override
    public Boolean exportExpensespayableByBill(HttpServletRequest request, HttpServletResponse response, Expensespayable expensespayable) {
        /*获取车辆类型字段数据*/
        List<SysDictVO> attrs = systemFeginServer.findDictByType("truck_attribute", expensespayable.getTenantId());
        //查询对账单信息
        Expensespayable payable = expensespayableMapper.selectByAccountPayId(expensespayable.getAccountPayId());
        //查询该对账单下的所有订单
        BillMiddle billMiddle = new BillMiddle();
        billMiddle.setAccountPayNumber(expensespayable.getAccountPayId());
        billMiddle.setTenantId(expensespayable.getTenantId());
        List<BillMiddle> billMiddleList = billMiddleMapper.selectByAccountPayNumber(billMiddle);
        if (billMiddleList.size() > 0) {
            String[] orderIds = new String[billMiddleList.size()];
            for (int i = 0; i < billMiddleList.size(); i++) {
                orderIds[i] = billMiddleList.get(i).getOrderNumber();
            }
            if (orderIds.length > 0) {
                if ("集装箱订单".equals(payable.getOrderType())) {
                    List<ReceiveBillOrder> receiveBillOrders = expensespayableMapper.selectCnByOrderIds(orderIds);
                    dealReceiveBillOrderList(receiveBillOrders,attrs,expensespayable.getTenantId());
                    DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                    String excelName = fmt.format(new Date()) + "-集装箱对账单信息";
                    ExcelUtil<ReceiveBillOrder> util = new ExcelUtil<>(ReceiveBillOrder.class);
                    util.exportExcel(request, response, receiveBillOrders, excelName, null);
                    return true;
                } else if ("普货订单".equals(payable.getOrderType())) {
                    List<ReceiveBillCommonGoods> commonGoods = expensespayableMapper.selectPhByOrderIds(orderIds);
                    dealReceiveBillCommonGoodsList(commonGoods,attrs,expensespayable.getTenantId());
                    DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                    String excelName = fmt.format(new Date()) + "-普货对账单信息";
                    ExcelUtil<ReceiveBillCommonGoods> util = new ExcelUtil<>(ReceiveBillCommonGoods.class);
                    util.exportExcel(request, response, commonGoods, excelName, null);
                    return true;
                } else {
                    List<ReceiveBillShortOrder> ordShortOrders = expensespayableMapper.selectPdByOrderIds(orderIds);
                    DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                    String excelName = fmt.format(new Date()) + "-盘短对账单信息";
                    ExcelUtil<ReceiveBillShortOrder> util = new ExcelUtil<>(ReceiveBillShortOrder.class);
                    util.exportExcel(request, response, ordShortOrders, excelName, null);
                    return true;
                }
            } else {
                return false;
            }
        }
        return false;
    }

    @Override
    public Page selectExpensespayableList(Query query, Expensespayable expensespayable) {
        List<Expensespayable> expensespayables = expensespayableMapper.selectExpensespayableList(query,expensespayable);
        query.setRecords(expensespayables);
        return query;
    }

    @Override
    public List<ReceiveBillOrder> selectCnByOrderIds(String[] orderIds) {
        return expensespayableMapper.selectCnByOrderIds(orderIds);
    }

    @Override
    public List<ReceiveBillCommonGoods> selectPhByOrderIds(String[] orderIds) {
        return expensespayableMapper.selectPhByOrderIds(orderIds);
    }

    @Override
    public List<ReceiveBillShortOrder> selectPdByOrderIds(String[] orderIds) {
        return expensespayableMapper.selectPdByOrderIds(orderIds);
    }

    @Override
    public Expensespayable selectByAccountPayId(String accountPayNumber) {
        return expensespayableMapper.selectByAccountPayId(accountPayNumber);
    }

    @Override
    public ReceiveBillCommonGoods selectPhByOrderId(String orderId) {
        return expensespayableMapper.selectPhByOrderId(orderId);
    }

    @Override
    public ReceiveBillOrder selectCnByOrderId(String orderId) {
        return expensespayableMapper.selectCnByOrderId(orderId);
    }

    public String getStatus(String balanceStatus){
        if(balanceStatus != null) {
            switch (balanceStatus) {
                case "0":
                    balanceStatus = "未结算";
                    break;
                case "1":
                    balanceStatus = "已发送";
                    break;
                case "2":
                    balanceStatus = "普通结算";
                    break;
                case "3":
                    balanceStatus = "开票结算";
                    break;
                case "4":
                    balanceStatus = "司机已确认";
                    break;
            }
        }
        return balanceStatus;
    }

    //集装箱导出数据处理
    public void dealReceiveBillOrderList(List<ReceiveBillOrder> receiveBillOrders,List<SysDictVO> attrs,Integer tenantId) {
        for(ReceiveBillOrder receiveBillOrder:receiveBillOrders){
                //处理导出数据
                //结算方式
                if (receiveBillOrder.getSettlement() != null) {
                    receiveBillOrder.setSettlement(receiveBillOrder.getSettlement().equals("0") ? "单结" : "月结");
                }
            receiveBillOrder.setBalanceStatus(getStatus(receiveBillOrder.getBalanceStatus()));
            receiveBillOrder.setIsAddToBill(receiveBillOrder.getIsAddToBill().equals("0") ? "未加入" : "已加入");
            CustomerVO customer = getCustomerByCustomerId(receiveBillOrder.getCustomerId(),tenantId);
            receiveBillOrder.setCustomerName(customer!=null?customer.getCustomerName():"");
                /*处理车辆信息*/
                TruckVO truckVO = new TruckVO();
                truckVO.setTenantId(tenantId);
                truckVO.setPlateNumber(receiveBillOrder.getPlateNumber());
                List<TruckVO> truck = truckFeign.selectTruckList(truckVO);
                if(truck != null && truck.size() == 1){
                    receiveBillOrder.setTruckAttribute(orderServiceImpl.getLabel(attrs, truck.get(0).getAttribute()));
                }

            /*提送货地址处理*/
            /*提货地*/
            List<OrdPickupArrivalAdd> pickupAdds = addMapper.selectPickupByOrderId(receiveBillOrder.getOrderId());
            if (pickupAdds.size() != 0) {
                String adress="";
                for (OrdPickupArrivalAdd pickupAdd : pickupAdds) {
                    adress=adress+"-"+pickupAdd.getAddressCity();
                }
                receiveBillOrder.setPickupGoodsDetailplace(adress.replaceFirst("-",""));
            }else{
                receiveBillOrder.setPickupGoodsDetailplace("");
            }
            /*送货地*/
            List<OrdPickupArrivalAdd> arrivalAdds = addMapper.selectArrivalByOrderId(receiveBillOrder.getOrderId());
            if (arrivalAdds.size() != 0) {
                String adress="";
                for (OrdPickupArrivalAdd arrivalAdd : arrivalAdds) {
                    adress=adress+"-"+arrivalAdd.getAddressCity();
                }
                receiveBillOrder.setSendGoodsDetailplace(adress.replaceFirst("-",""));
            }else{
                receiveBillOrder.setSendGoodsDetailplace("");
            }
        }
    }

    //普货处理导出数据
    public void dealReceiveBillCommonGoodsList(List<ReceiveBillCommonGoods> commonGoods,List<SysDictVO> attrs,Integer tenantId) {
        for(ReceiveBillCommonGoods receiveBillCommonGoods:commonGoods){
                //结算状态
                if (receiveBillCommonGoods.getSettlement() != null) {
                    receiveBillCommonGoods.setSettlement(receiveBillCommonGoods.getSettlement().equals("0") ? "单结" : "月结");
                }
            receiveBillCommonGoods.setBalanceStatus(getStatus(receiveBillCommonGoods.getBalanceStatus()));
            receiveBillCommonGoods.setIsAddToBill(receiveBillCommonGoods.getIsAddToBill().equals("0") ? "未加入" : "已加入");
            CustomerVO customer = getCustomerByCustomerId(receiveBillCommonGoods.getCustomerId(),tenantId);
            receiveBillCommonGoods.setCustomerName(customer!=null?customer.getCustomerName():"");
                /*处理车辆信息*/
                TruckVO truckVO = new TruckVO();
                truckVO.setTenantId(tenantId);
                truckVO.setPlateNumber(receiveBillCommonGoods.getPlateNumber());
                List<TruckVO> truck = truckFeign.selectTruckList(truckVO);
                if(truck != null && truck.size() == 1){
                    receiveBillCommonGoods.setTruckAttribute(orderServiceImpl.getLabel(attrs, truck.get(0).getAttribute()));
                }

            /*提送货地址处理*/
            /*提货地*/
            List<OrdPickupArrivalAdd> pickupAdds = addMapper.selectPickupByOrderId(receiveBillCommonGoods.getOrderId());
            if(pickupAdds.size()!=0){
                String adress="";
                for(OrdPickupArrivalAdd pickupAdd:pickupAdds){
                    adress=adress+"-"+pickupAdd.getAddressCity();
                }
                receiveBillCommonGoods.setShipperPlace(adress.replaceFirst("-",""));
            }else{
                receiveBillCommonGoods.setShipperPlace("");
            }
            /*送货地*/
            List<OrdPickupArrivalAdd> arrivalAdds = addMapper.selectArrivalByOrderId(receiveBillCommonGoods.getOrderId());
            if(arrivalAdds.size()!=0){
                String adress="";
                for(OrdPickupArrivalAdd arrivalAdd:arrivalAdds){
                    adress=adress+"-"+arrivalAdd.getAddressCity();
                }
                receiveBillCommonGoods.setPickerPlace(adress.replaceFirst("-",""));
            }else{
                receiveBillCommonGoods.setPickerPlace("");
            }
        }
    }
}
