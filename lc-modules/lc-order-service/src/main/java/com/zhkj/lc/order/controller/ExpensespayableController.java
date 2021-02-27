package com.zhkj.lc.order.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.zhkj.lc.common.constant.CommonConstant;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.util.R;
import com.zhkj.lc.common.util.UserUtils;
import com.zhkj.lc.common.vo.CustomerVO;
import com.zhkj.lc.common.web.BaseController;
import com.zhkj.lc.order.dto.*;
import com.zhkj.lc.order.feign.TrunkFeign;
import com.zhkj.lc.order.model.entity.*;
import com.zhkj.lc.order.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 *     应收费用列表
 * 应收对账单表 前端控制器
 * </p>
 *
 * @author wzc
 * @since 2019-02-15
 */
@Api(description = "应收费用列表、应收对账单接口")
@RestController
@RequestMapping("/expensespayable")
public class  ExpensespayableController extends BaseController {
    @Autowired
    private IExpensespayableService expensespayableService;
    @Autowired
    private IBillMiddleService billMiddleService;
    @Autowired
    private TrunkFeign trunkFeign;
    @Autowired
    private IOrdOrderService ordOrderService;
    @Autowired
    private IOrdShortOrderService shortOrderService;
    @Autowired
    private IOrdCommonGoodsService commonGoodsService;

    /**
     * 分页查询对账单信息
     *
     * @param params 分页对象
     * @return 分页对象
     */
    @PostMapping("/selectExpensespayableList")
    @ApiOperation(value = "分页查询信息")
    public Page selectExpensespayableList(@RequestParam Map<String, Object> params,Expensespayable expensespayable) {
        params.put(CommonConstant.DEL_FLAG, CommonConstant.STATUS_NORMAL);
        expensespayable.setTenantId(getTenantId());
        return expensespayableService.selectExpensespayableList(new Query<>(params), expensespayable);
    }
    /**
     * 编辑对账单
     *
     * @param expensespayable 实体
     * @return success/false
     */
    @PutMapping
    @ApiOperation(value = "编辑对账单")
    public R<Boolean> edit(@RequestBody Expensespayable expensespayable) {
        expensespayable.setUpdateTime(new Date());
        expensespayable.setUpdateBy(UserUtils.getUser());
        return new R<>(expensespayableService.updateById(expensespayable));
    }


    /**
     * 通过ID查询
     *
     * @param id ID
     * @return expensespayable
     */
    @GetMapping("/{id}")
    public R<Expensespayable> get(@PathVariable Integer id) {
        return new R<>(expensespayableService.selectById(id));
    }

    /**
     * 分页显示应收费用信息
     */
    @PostMapping("/getOrderList")
    @ApiOperation(value = "分页显示每单应收费用信息")
    public Page<NeedPayBaseModel> getOrderList(@RequestParam Map<String, Object> params, FinanceQueryDTO financeQueryDTO) {
        financeQueryDTO.setTenantId(getTenantId());
        if (financeQueryDTO.getOrderType().equals("0")) {//如果是集装箱类型
            return expensespayableService.selectCnOrderList(new Query<>(params), financeQueryDTO);
        } else if (financeQueryDTO.getOrderType().equals("1")) {//如果是普货类型
            return expensespayableService.selectPhOrderList(new Query<>(params), financeQueryDTO);
        } else {//如果是盘短类型
            return expensespayableService.selectPdOrderList(new Query<>(params), financeQueryDTO);
        }
    }

    @GetMapping("/total")
    public BigDecimal getTotalFee(FinanceQueryDTO financeQueryDTO){
        financeQueryDTO.setTenantId(getTenantId());
        return expensespayableService.getTotalFee(financeQueryDTO);
    }

    /**
     * 添加对账单
     *
     * @param financeQueryDTO 财务应收应付查询参数实体类
     * @return success/false
     */
    @Transactional
    @PostMapping("addExpensespayable")
    @ApiOperation(value = "添加对账单")
    public Boolean addExpensespayable(@RequestBody FinanceQueryDTO financeQueryDTO) throws Exception {
        Expensespayable expensespayable = new Expensespayable();
        String number = getExpensespayableNumber();//生成对账单号
        String[] ids = financeQueryDTO.getOrderIds();//获取订单id数组
        Integer id = financeQueryDTO.getCustomerId();//客户id
        CustomerVO customer = new CustomerVO();
        customer.setCustomerId(id);
        customer.setTenantId(getTenantId());
        CustomerVO customerVO = trunkFeign.selectCustomerById(customer);//查询客户信息
        //根据订单号统计订单总费用
        BigDecimal totalFee = null;
        if ("0".equals(financeQueryDTO.getOrderType())) {
            expensespayable.setOrderType("集装箱订单");//订单类型
            totalFee = expensespayableService.countOrderTotalFee(ids, getTenantId());
            //更新集装箱订单字段值(加入对账单)
            ordOrderService.updateByOrderIds(ids);
        }
        if ("1".equals(financeQueryDTO.getOrderType())) {
            expensespayable.setOrderType("普货订单");//订单类型
            totalFee = expensespayableService.countCommonGoodsTotalFee(ids, getTenantId());
            //更新普货订单字段值(加入对账单)
            commonGoodsService.updateByOrderIds(ids);
        }
        if ("2".equals(financeQueryDTO.getOrderType())) {
            expensespayable.setOrderType("盘短订单");//订单类型
            totalFee = expensespayableService.countShortOrderTotalFee(ids, getTenantId());
        }
        //应收对账单信息添加
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String startTime = financeQueryDTO.getStartTime();
        expensespayable.setAccountPayId(number);//对账单编号
        expensespayable.setPayDateStart(sdf.parse(financeQueryDTO.getStartTime()));//结算年月(开始)
        expensespayable.setPayDateEnd(sdf.parse(financeQueryDTO.getEndTime()));//结算年月(截止)
        expensespayable.setCustomerName(customerVO.getCustomerName());//客户名称
        expensespayable.setOrderAmount(ids.length);//订单数量
        expensespayable.setRate(financeQueryDTO.getRate());//利率
        expensespayable.setTotalFee(totalFee); //合计费用
        expensespayable.setSettlementStatus("0");//对账单状态(0:未结算;1.已提交;2:普通结算;3:开票结算;4:司机已确认)
        expensespayable.setCreateTime(new Date());
        expensespayable.setTenantId(getTenantId());
        //订单对账单中间表信息添加
        BillMiddle billMiddle = new BillMiddle();
        for (String orderNumber : ids) {
            billMiddle.setAccountPayNumber(number);
            billMiddle.setOrderNumber(orderNumber);
            billMiddle.setTenantId(getTenantId());
            billMiddleService.insert(billMiddle);
        }
        return expensespayableService.insert(expensespayable);
    }

    /**
     * 对账单订单结算确认流程
     * 参数:对账单编号,结算状态
     */
    @PostMapping("SettlementProcess")
    @ApiOperation(value = "对账单订单结算确认流程")
    public Boolean SettlementProcess(@RequestParam("accountPayId") String accountPayId ,@RequestParam("orderType") String orderType,@RequestParam("settlementStatus") String settlementStatus) {
        //查询该对账单下的所有订单
        BillMiddle billMiddle = new BillMiddle();
        billMiddle.setAccountPayNumber(accountPayId);
        billMiddle.setTenantId(getTenantId());
        List<BillMiddle> billMiddleList = billMiddleService.selectByAccountPayNumber(billMiddle);
        if ("集装箱订单".equals(orderType)) {
            for (BillMiddle billMiddle1 : billMiddleList) {
                OrdOrder ordOrder = new OrdOrder();
                ordOrder.setOrderId(billMiddle1.getOrderNumber());
                ordOrder.setBalanceStatus(settlementStatus);
                ordOrder.setUpdateTime(new Date());
                ordOrderService.updateOrderBalanceStatus(ordOrder);
            }
        }
        if ("普货订单".equals(orderType)) {
            for (BillMiddle billMiddle1 : billMiddleList) {
                OrdCommonGoods commonGoods = new OrdCommonGoods();
                commonGoods.setMorderId(billMiddle1.getOrderNumber());
                commonGoods.setBalanceStatus(settlementStatus);
                commonGoods.setUpdateTime(new Date());
                commonGoodsService.updateOrderBalanceStatus(commonGoods);
            }
        }
        if ("盘短订单".equals(orderType)) {
            for (BillMiddle billMiddle1 : billMiddleList) {
                OrdShortOrder shortOrder = new OrdShortOrder();
                shortOrder.setOrderId(billMiddle1.getOrderNumber());
                shortOrder.setBalanceStatus(settlementStatus);
                shortOrder.setUpdateTime(new Date());
                shortOrderService.updateOrderBalanceStatus(shortOrder);
            }
        }
        Expensespayable expensespayable = new Expensespayable();
        expensespayable.setAccountPayId(accountPayId);
        expensespayable.setOrderType(orderType);
        expensespayable.setSettlementStatus(settlementStatus);
        return expensespayableService.SettlementProcess(expensespayable);
    }

    /**
     * 查询所有客户
     */
    @PostMapping("selectAllCustomers")
    @ApiOperation(value = "查询所有客户")
    public List<CustomerVO> selectAllCustomers(@RequestBody CustomerVO customerVO) {
        customerVO.setTenantId(getTenantId());
        return trunkFeign.selectAllCustomers(customerVO);
    }

    /**
     * 对账单号生成方法
     */

    public String getExpensespayableNumber() {
        String expensespayableNumber = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String str = format.format(new Date());
        int id = getTenantId();
        expensespayableNumber = "YS";
        return expensespayableNumber + String.valueOf(id) + str;
    }

    /**
     * 集装箱订单调账
     */
    @PostMapping("updateOrderFee")
    @ApiOperation(value = "集装箱订单调账")
    public Boolean updateOrderFee(@RequestBody OrdOrder ordOrder) throws IOException {
        return ordOrderService.updateOrderStatus(ordOrder);
    }

    /**
     * 普货订单调账
     */
    @PostMapping("updateCommonGoodsOrderFee")
    @ApiOperation(value = "普货订单调账")
    public Boolean updateCommonGoodsOrderFee(@RequestBody ReceiveBillCommonGoods ordCommonGoods) {
        return commonGoodsService.updateCommonOrder(ordCommonGoods);
    }


    /**
     * 订单单结
     * 结算
     */
    @GetMapping("orderBalance/{orderId}/{balanceStatus}")
    @ApiOperation(value = "订单单结")
    public Boolean orderBalance(@PathVariable String orderId, @PathVariable Integer balanceStatus) {
        String status = balanceStatus.toString();
        if ("PH".equals(orderId.substring(0, 2))) {
            OrdCommonGoods commonGoods = new OrdCommonGoods();
            commonGoods.setMorderId(orderId);
            commonGoods.setBalanceStatus(status);
            commonGoods.setUpdateTime(new Date());
            return commonGoodsService.updateOrderBalanceStatus(commonGoods);
        } else if ("CN".equals(orderId.substring(0, 2))) {
            OrdOrder ordOrder = new OrdOrder();
            ordOrder.setOrderId(orderId);
            ordOrder.setBalanceStatus(status);
            ordOrder.setUpdateTime(new Date());
            return ordOrderService.updateOrderBalanceStatus(ordOrder);
        } else {
            OrdShortOrder shortOrder = new OrdShortOrder();
            shortOrder.setOrderId(orderId);
            shortOrder.setBalanceStatus(status);
            shortOrder.setUpdateTime(new Date());
            return shortOrderService.updateOrderBalanceStatus(shortOrder);
        }
    }

    /**
     * 批量删除对账单
     *
     * @param ids ID
     * @return success/false
     */
    @DeleteMapping("/{ids}")
    @ApiOperation(value = "批量删除")
    public Boolean delete(@PathVariable String ids) {
        List<Expensespayable> expensespayables = expensespayableService.selectByIds(ids);
        for (Expensespayable expensespayable : expensespayables) {
            BillMiddle billMiddle = new BillMiddle();
            billMiddle.setTenantId(getTenantId());
            billMiddle.setAccountPayNumber(expensespayable.getAccountPayId());
            List<BillMiddle> billMiddles = billMiddleService.selectByAccountPayNumber(billMiddle);
            String[] phorderIds = new String[billMiddles.size()];
            String[] cnorderIds = new String[billMiddles.size()];
            String[] pdorderIds = new String[billMiddles.size()];
            for (int i = 0; i< billMiddles.size(); i++) {
                if ("PH".equals(billMiddles.get(i).getOrderNumber().substring(0, 2))) {
                    phorderIds[i] = billMiddles.get(i).getOrderNumber();
                }
                if ("CN".equals(billMiddles.get(i).getOrderNumber().substring(0, 2))) {
                    cnorderIds[i] = billMiddles.get(i).getOrderNumber();
                }
                if ("PD".equals(billMiddles.get(i).getOrderNumber().substring(0, 2))) {
                    pdorderIds[i] = billMiddles.get(i).getOrderNumber();
                }
            }
            if(phorderIds.length>0){
                commonGoodsService.updateBillByOrderIds(phorderIds, UserUtils.getUser());
            }
            if(cnorderIds.length>0){
                ordOrderService.updateBillByOrderIds(cnorderIds, UserUtils.getUser());
            }
            billMiddleService.removeAllOrder(billMiddle);
        }
        return expensespayableService.deleteByIds(ids);
    }

    /**
     * 移除对账单中的订单
     */
    @GetMapping("removeOrder")
    @ApiOperation(value = "移除对账单中的订单")
    public Boolean removeOrder(@RequestParam("accountPayNumber") String accountPayNumber ,@RequestParam("orderNumber") String orderNumber) {
        BillMiddle billMiddle = new BillMiddle();
        billMiddle.setAccountPayNumber(accountPayNumber);
        billMiddle.setOrderNumber(orderNumber);
        if (null != accountPayNumber){
            Expensespayable expensespayable =  expensespayableService.selectByAccountPayId(accountPayNumber);
            if (null != orderNumber){
                String orderId = orderNumber;
                if ("PH".equals(orderId.substring(0, 2))) {
                    String[] orderIds = {orderId};
                    ReceiveBillCommonGoods receiveBillCommonGoods =  expensespayableService.selectPhByOrderId(orderId);
                    commonGoodsService.updateBillByOrderIds(orderIds,UserUtils.getUser());
                    billMiddleService.removeOrder(billMiddle);
                    expensespayable.setOrderAmount(expensespayable.getOrderAmount() - 1);
                    expensespayable.setTotalFee(expensespayable.getTotalFee().subtract(receiveBillCommonGoods.getTotalFee()));
                } else if ("CN".equals(orderId.substring(0, 2))) {
                    String[] orderIds = {orderId};
                    ReceiveBillOrder receiveBillOrder =  expensespayableService.selectCnByOrderId(orderId);
                    ordOrderService.updateBillByOrderIds(orderIds,UserUtils.getUser());
                    billMiddleService.removeOrder(billMiddle);
                    expensespayable.setOrderAmount(expensespayable.getOrderAmount() - 1);
                    expensespayable.setTotalFee(expensespayable.getTotalFee().subtract(receiveBillOrder.getTotalFee()));
                } else {
                    //盘短,暂时不写
                }
                return expensespayableService.updateById(expensespayable);
            }
        }
        return false;
    }

    /**
     * 应收订单列表信息导出
     */
    @GetMapping("/exportExpensespayableOrder")
    @ApiOperation(value = "应收订单列表信息导出")

    public Boolean exportExpensespayableOrder(HttpServletRequest request, HttpServletResponse response, String ids,FinanceQueryDTO queryDTO) {
        if (null != ids && ! "".equals(ids)) {
            queryDTO.setOrderIds(ids.split(","));
        }
        queryDTO.setTenantId(getTenantId());
        return expensespayableService.exportBillByQuery(request,response,queryDTO);
    }

    /**
     * 对账单列表信息导出
     */
    @GetMapping("/exportExpensespayableByBill")
    @ApiOperation(value = "对账单列表信息导出")
    public Boolean exportExpensespayableByBill(HttpServletRequest request, HttpServletResponse response,@RequestParam("accountPayId") String accountPayId) {
            Expensespayable expensespayable = new Expensespayable();
            expensespayable.setAccountPayId(accountPayId);
            expensespayable.setTenantId(getTenantId());
            return expensespayableService.exportExpensespayableByBill(request, response, expensespayable);
    }

    /**
     * 对账单对应的订单列表导出
     */
    @GetMapping("/exportExpensespayable")
    @ApiOperation(value = "导出应收账单")
    public Boolean exportExpensespayable(HttpServletRequest request, HttpServletResponse response,@RequestParam("ids") String ids) {
        String[] ids1 = ids.split(",");
        int[] ids2 = new int[ids1.length];
        if (ids1.length > 0) {
            for (int i = 0; i < ids1.length; i++) {
                ids2[i] = Integer.parseInt(ids1[i]);
            }
            Expensespayable expensespayable = new Expensespayable();
            expensespayable.setIds(ids2);
            expensespayable.setTenantId(getTenantId());
            return expensespayableService.exportExpensespayable(request, response, expensespayable);
        }
        return false;
    }


    /**
     * 查询对账单对应的订单列表
     */
    @PostMapping("/selectOrderByBill")
    @ApiOperation(value = "查询对账单对应的订单列表")
    public Map<String,Object> selectOrderByBill(Expensespayable expensespayable) {
        Map<String,Object> mapOrder = new HashMap<>();
        //查询该对账单下的所有订单
        BillMiddle billMiddle = new BillMiddle();
        billMiddle.setAccountPayNumber(expensespayable.getAccountPayId());
        billMiddle.setTenantId(getTenantId());
        List<BillMiddle> billMiddleList = billMiddleService.selectByAccountPayNumber(billMiddle);
        if(null != billMiddleList && billMiddleList.size() > 0){
            String[] orderIds = new String[billMiddleList.size()];
            for (int i = 0; i < billMiddleList.size(); i++) {
                orderIds[i] = billMiddleList.get(i).getOrderNumber();
            }
            if(orderIds.length > 0) {
                if ("集装箱订单".equals(expensespayable.getOrderType())) {
                    List<ReceiveBillOrder> receiveBillOrders = expensespayableService.selectCnByOrderIds(orderIds);
                    mapOrder.put("orderList", receiveBillOrders);
                    return mapOrder;
                } else if ("普货订单".equals(expensespayable.getOrderType())) {
                    List<ReceiveBillCommonGoods> commonGoods = expensespayableService.selectPhByOrderIds(orderIds);
                    mapOrder.put("orderList", commonGoods);
                    return mapOrder;
                } else {
                    List<ReceiveBillShortOrder> ordShortOrders = expensespayableService.selectPdByOrderIds(orderIds);
                    mapOrder.put("orderList", ordShortOrders);
                    return mapOrder;
                }
            }else{
            return null;
            }
        }else{
            return null;
        }
    }
}


