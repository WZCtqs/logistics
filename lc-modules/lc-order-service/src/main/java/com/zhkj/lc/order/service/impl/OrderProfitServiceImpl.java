package com.zhkj.lc.order.service.impl;

import com.zhkj.lc.common.vo.CustomerVO;
import com.zhkj.lc.common.vo.DriverVO;
import com.zhkj.lc.common.vo.SysDictVO;
import com.zhkj.lc.common.vo.TruckVO;
import com.zhkj.lc.order.dto.HeadOfficeProfit;
import com.zhkj.lc.order.dto.OrderProfit;
import com.zhkj.lc.order.dto.TruckProfit;
import com.zhkj.lc.order.feign.CommonGoodsTruckFeign;
import com.zhkj.lc.order.feign.SystemFeginServer;
import com.zhkj.lc.order.feign.TrunkFeign;
import com.zhkj.lc.order.mapper.OrderProfitMapper;
import com.zhkj.lc.order.model.entity.OrdExceptionFee;
import com.zhkj.lc.order.service.OrderProfitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class OrderProfitServiceImpl implements OrderProfitService {
    @Autowired
    private OrderProfitMapper orderProfitMapper;
    @Autowired
    private TrunkFeign trunkFeign;

    @Autowired
    private SystemFeginServer systemFeginServer;
    @Autowired
    private NeedPayBillServiceImpl needPayBillService;

    @Override
    public List<OrderProfit> selectLast7daysOrderProfit(OrderProfit orderProfit) {
        List<OrderProfit> profitList = new ArrayList<>();
        /*根据客户名获取客户信息*/
        CustomerVO customerVO = new CustomerVO();
        customerVO.setCustomerName(orderProfit.getCustomerName());
        customerVO.setTenantId(orderProfit.getTenantId());
        List<CustomerVO> customerList = trunkFeign.selectAllForFegin(customerVO);
        if(customerList != null && !customerList.isEmpty()){
            orderProfit.setList(customerList);
            /*单结订单*/
            List<OrderProfit> orderProfitList = orderProfitMapper.selectLast7daysOrderProfit(orderProfit);
            if(orderProfitList != null && !orderProfitList.isEmpty()){
                for(OrderProfit order : orderProfitList){
                    DriverVO driverVO = needPayBillService.getDriverByDriverId(order.getDriverId(), order.getTenantId());
                    TruckVO truckVO = needPayBillService.getTruckByPlateNumebr(order.getPlateNumber(), order.getTenantId());
                    /*获取客户信息*/
                    CustomerVO customer = getCustomer(customerList, order.getCustomerId());
                    order.setCustomerName(customer != null? customer.getCustomerName():"");

                    /********************异常费用列表*******************/
                    List<OrdExceptionFee> exFeeList = exceptionfeeSet(order.getExceptionFeeList());
                    for(OrdExceptionFee fee : exFeeList){
                        if(fee.getExceptionFeeType().equals("压车费")){
                            order.setYcFee(order.getYcFee().add(fee.getExceptionFee()!=null?fee.getExceptionFee():new BigDecimal(0)));
                        }
                    }
                    order.setExceptionFee(order.getExceptionFee().subtract(order.getYcFee()));

//                    /*如果是开票*/
//                    if(order.getIsInvoice().equals("1")){
//                        order.setRate(order.getReceivable().multiply(
//                                order.getPayRate().multiply(new BigDecimal(0.01))
//                        ));
//                    }
                    if(truckVO.getAttribute().equals("0")){
                        order.setTruckAttribute("0");
                    }else {
                        order.setTruckAttribute("1");
                    }
                    switch (order.getIsYfInvoice()){
                        // 运输费-0，压车费-0
                        case "0" :
                            break;
                        // 运输费-0，压车费-1
                        case "1" :
                            order.setPayRate(order.getYcFee().multiply(
                                    new BigDecimal(1).subtract(
                                            order.getRate().multiply(new BigDecimal(0.01)))
                            ).setScale(2, BigDecimal.ROUND_HALF_UP));
                            break;
                        // 运输费-1，压车费-0
                        case "2" :
                            order.setPayRate(order.getNeedFee().multiply(
                                    new BigDecimal(1).subtract(
                                            order.getRate().multiply(new BigDecimal(0.01)))
                            ).setScale(2, BigDecimal.ROUND_HALF_UP));
                            break;
                        // 运输费-1，压车费-1
                        case "3" :
                            order.setPayRate(order.getNeedFee().multiply(
                                    new BigDecimal(1).subtract(
                                            order.getRate().multiply(new BigDecimal(0.01)))
                            ).setScale(2, BigDecimal.ROUND_HALF_UP)
                            .add(order.getYcFee().multiply(
                                    new BigDecimal(1).subtract(
                                            order.getRate().multiply(new BigDecimal(0.01)))
                            ).setScale(2, BigDecimal.ROUND_HALF_UP)));
                            break;
                    }
                    order.setExpensesPay(order.getNeedFee().add(order.getExceptionFee().add(order.getYcFee())));
                    profitList.add(order);
                }
            }
            return orderProfitList;
        }
        return null;
    }

    @Override
    public List<TruckProfit> selectTruckOrderProfit(OrderProfit orderProfit) {
        /*获取单票利润信息*/
        List<OrderProfit> orderProfits = selectLast7daysOrderProfit(orderProfit);
        if(orderProfits != null) {
            /*获取车辆信息*/
            Set<String> plates = new HashSet<>();
            for (OrderProfit order : orderProfits) {
                plates.add(order.getPlateNumber());
            }
            /*返回数据集合*/
            List<TruckProfit> truckProfits = new ArrayList<>();
            for (String plate : plates) {
                TruckProfit truckProfit = new TruckProfit();
                int ordersum = 0, kilometre = 0;
                BigDecimal receivable = new BigDecimal(0);//应收
                BigDecimal expensesPay = new BigDecimal(0);//应付
                BigDecimal rate = new BigDecimal(0); //开票
                Iterator<OrderProfit> iter = orderProfits.iterator();
                while (iter.hasNext()) {
                    OrderProfit tt = iter.next();
                    /*如果车牌号一致*/
                    if ((tt.getPlateNumber()).equals(plate)) {
                        ordersum++;
                        kilometre = kilometre + tt.getKilometre();
                        receivable = receivable.add(tt.getReceivable());
                        expensesPay = expensesPay.add(tt.getExceptionFee()).add(tt.getExpensesPay());
                        rate = rate.add(tt.getRate());
                        iter.remove();
                    }
                }
                truckProfit.setPlateNumber(plate);
                truckProfit.setOrderSum(ordersum);
                truckProfit.setKilometre(kilometre);
                truckProfit.setReceivable(receivable);
                truckProfit.setExpensesPay(expensesPay);
                truckProfit.setRate(rate);
                truckProfits.add(truckProfit);
            }
            return truckProfits;
        }
        return null;
    }

    @Override
    public HeadOfficeProfit selectSumProfit(OrderProfit orderProfit) {
        /*获取单票利润信息*/
        List<OrderProfit> orderProfits = selectLast7daysOrderProfit(orderProfit);
        HeadOfficeProfit profit = new HeadOfficeProfit();
        /*初始化费用数据*/
        BigDecimal fee = new BigDecimal(0);
        profit.setRecTransportFee(fee);/*收入-运输费*/
        profit.setRecExceptionFee(fee);/*收入-异常费*/
        profit.setPayTeamTransportFee(fee);/*支出-车队-运输费*/
        profit.setPayTeamExceptionFee(fee);/*支出-车队-异常费*/
        profit.setPayPersonExceptionFee(fee);/*支出-自有-异常费*/
        profit.setRate(fee);/*开票税额*/
        for(OrderProfit order : orderProfits){
            profit.setRecTransportFee(profit.getRecTransportFee().add(order.getReceivable()));
            profit.setRecExceptionFee(profit.getRecExceptionFee().add(order.getExYsFee()));
            if(order.getTruckAttribute().equals("1")) {
                profit.setPayTeamTransportFee(profit.getPayTeamTransportFee().add(order.getNeedFee()));
            }
            profit.setPayTeamExceptionFee(profit.getPayTeamExceptionFee().add(order.getExceptionFee()));
            profit.setRate(profit.getRate().add(order.getRate()));
        }

        return profit;
    }


    public CustomerVO getCustomer(List<CustomerVO> customerList, Integer customerId){
        CustomerVO customerVO = new CustomerVO();
        for(CustomerVO customer : customerList){
            if(customer.getCustomerId().intValue() == customerId.intValue()){
                customerVO = customer;
                break;
            }
        }
        return customerVO;
    }
    /*异常信息处理*/
    public List<OrdExceptionFee> exceptionfeeSet(List<OrdExceptionFee> list){
        if(list != null && list.size() > 0){
            List<SysDictVO> dictVOS = systemFeginServer.findDictByType("exception_fee_type", list.get(0).getTenantId());
            for(OrdExceptionFee fee : list){
                fee.setExceptionFeeType(getLable(dictVOS, fee.getExceptionFeeType()));
            }
        }
        return list;
    }
    public static String getLable(List<SysDictVO> dictVOS, String value){
        String lable = "";
        for(SysDictVO dict : dictVOS) {
            if(value.equals(dict.getValue())){
                lable = dict.getLabel();
                break;
            }
        }
        return lable;
    }
}
