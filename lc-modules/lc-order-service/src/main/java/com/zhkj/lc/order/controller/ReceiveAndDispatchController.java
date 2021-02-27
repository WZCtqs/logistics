package com.zhkj.lc.order.controller;

import com.zhkj.lc.common.util.http.HttpClientUtil;
import com.zhkj.lc.common.vo.DriverVO;
import com.zhkj.lc.common.vo.GPSVO;
import com.zhkj.lc.order.dto.*;
import com.google.gson.Gson;
import com.xiaoleilu.hutool.util.ArrayUtil;
import com.zhkj.lc.common.web.BaseController;
import com.zhkj.lc.order.feign.TrunkFeign;
import com.zhkj.lc.order.model.entity.*;
import com.zhkj.lc.order.service.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 后台首页数据统计
 * @Author: wchx
 * @Date: 2019/1/17 10:39
 */
@Controller
@RequestMapping("/receiveAndDispatch")
public class ReceiveAndDispatchController extends BaseController {
    @Autowired
    private IOrdOrderService ordOrderService;
    @Autowired
    private IOrdCommonGoodsService ordCommonGoodsService;
    @Autowired
    private IOrdOrderLogisticsService ordOrderLogisticsService;
    @Autowired
    private IOrdShortOrderService ordShortOrderService;
    @Autowired
    private IExpensespayableService expensespayableService;
    @Autowired
    private TrunkFeign trunkFeign;
    @Autowired
    HttpServletRequest request;

    /**
     * 0
     * 统计当天开单数(接单数)
     * 根据订单状态统计开单数
     * 查询运踪记录表中集装箱
     */
    @GetMapping("/countOrders")
    @ResponseBody
    public Map<String, Integer> countOrders() {
        Map<String, Integer> countMap = new HashMap<>();
        int countOrders = ordOrderLogisticsService.countOrders(getTenantId());
        int countAllOrders = ordOrderLogisticsService.countAllOrders(getTenantId());
        int countShortOrders = ordShortOrderService.countShortOrders(getTenantId());
        countMap.put("orders", countOrders);
        countMap.put("commonOrders", countAllOrders - countOrders);
        countMap.put("countShortOrders", countShortOrders);
        return countMap;
    }

    /**
     * 0
     * 普货订单
     * 根据订单状态统计各状态订单数
     * 0	草稿	2	待接单	 5	待提货
     * 6	提货中	7	运输中	8	签收中	9	已签收
     */
    @GetMapping("/countByGoodsOrderStatus")
    @ResponseBody
    public Map<String, Integer> countByGoodsOrderStatus() {
        Map<String, Integer> countGoodsOrder = new HashMap<>();
        String[] status = new String[]{"6", "7", "8"};
        int Pickingupgoods = 0;//提货中
        int ontheway = 0;//在途
        int signin = 0; //签收中
        OrdCommonGoods commonGoods = new OrdCommonGoods();
        commonGoods.setMtenantId(getTenantId());
        for (int i = 0; i < status.length; i++) {
            commonGoods.setStatus(status[i]);
            if ("6".equals(status[i])) {
                Pickingupgoods = ordCommonGoodsService.countByGoodsOrderStatus(commonGoods);
            }
            if ("7".equals(status[i])) {
                ontheway = ordCommonGoodsService.countByGoodsOrderStatus(commonGoods);
            }
            if ("8".equals(status[i])) {
                signin = ordCommonGoodsService.countByGoodsOrderStatus(commonGoods);
            }
        }
        countGoodsOrder.put("Pickingupgoods", Pickingupgoods);
        countGoodsOrder.put("ontheway", ontheway);
        countGoodsOrder.put("signin", signin);
        return countGoodsOrder;
    }

    /**
     * 集装箱订单
     * 根据订单状态统计各状态订单数
     * 0	草稿	 1	待派车	2	待接单	3	待提箱	 4	提箱中	 5	待提货
     * 6	提货中	7	运输中	8	签收中	9	已签收	10	还箱中	11	已还箱
     */
    @GetMapping("/countByOrderStatus")
    @ResponseBody
    public Map<String, Integer> countByOrderStatus() {
        Map<String, Integer> countOrder = new HashMap<>();
        String[] status = new String[]{"0", "02", "1", "2", "3", "4", "5", "6", "7", "8"};
        int plan = 0;//计划中
        int cfmd = 0;//已确认
        int Pickingupgoods = 0;//提货中
        int ontheway = 0;//在途
        int signin = 0; //签收中
        OrdOrder order = new OrdOrder();
        //order.setTenantId(0);
        order.setTenantId(getTenantId());
        for (int i = 0; i < status.length; i++) {
            order.setStatus(status[i]);
            if ("0".equals(status[i])) {
                plan = ordOrderService.countByOrderStatus(order);
            }
            if ("02".equals(status[i])) {
//                plan += ordOrderService.countByOrderStatus(order);
            }
            if ("1".equals(status[i])) {
                cfmd += ordOrderService.countByOrderStatus(order);
            }
            if ("2".equals(status[i])) {
                cfmd += ordOrderService.countByOrderStatus(order);
            }
            if ("3".equals(status[i])) {
                cfmd += ordOrderService.countByOrderStatus(order);
            }
            if ("4".equals(status[i])) {
                cfmd += ordOrderService.countByOrderStatus(order);
            }
            if ("5".equals(status[i])) {
                cfmd += ordOrderService.countByOrderStatus(order);
            }
            if ("6".equals(status[i])) {
                Pickingupgoods = ordOrderService.countByOrderStatus(order);
            }
            if ("7".equals(status[i])) {
                ontheway = ordOrderService.countByOrderStatus(order);
            }
            if ("8".equals(status[i])) {
                signin = ordOrderService.countByOrderStatus(order);
            }
        }
        countOrder.put("plan", plan);
        countOrder.put("cfmd", cfmd);
        countOrder.put("Pickingupgoods", Pickingupgoods);
        countOrder.put("ontheway", ontheway);
        countOrder.put("signin", signin);
        return countOrder;
    }

    /**
     * 查询司机正在运送的订单号,添加订单GPS运踪信息
     */
    @ApiOperation(value = "查询司机正在运送的订单号,添加订单GPS运踪信息")
    @GetMapping("/addLogisticsByDriverId")
    @ResponseBody
    public Boolean addLogisticsByOrderId(Integer mdriver, String logisticsMsg) {
        Integer tenantId = Integer.parseInt(request.getHeader("tenantId"));
        OrdCommonGoods ordCommonGoods = new OrdCommonGoods();
        ordCommonGoods.setMtenantId(tenantId);
        ordCommonGoods.setDriverId(mdriver);
        OrdOrder ordOrder1 = new OrdOrder();
        ordOrder1.setTenantId(tenantId);
        ordOrder1.setDriverId(mdriver);
        System.out.println("*****************租户id:" + tenantId + "   *****************");
        List<CommonOrdSearch> ordCommonTrucks = ordCommonGoodsService.selectOrderByDriverId(ordCommonGoods);
        List<OrderSearch> ordOrders = ordOrderService.selectOrderByDriverId(ordOrder1);
        System.out.println("ordCommonTrucks: " + ordCommonTrucks.size() + "ordOrders: " + ordOrders.size());
        Boolean b = false;
        if (ordCommonTrucks.size() > 0) {
            for (CommonOrdSearch ordCommonTruck : ordCommonTrucks) {
                OrdOrderLogistics ordOrderLogistics = new OrdOrderLogistics();
                String orderId = ordCommonTruck.getOrderId();
                ordOrderLogistics.setOrderId(orderId);
                ordOrderLogistics.setTenantId(tenantId);
                ordOrderLogistics.setOrderStatus(ordCommonTruck.getStatus());
                ordOrderLogistics.setLogisticsMsg("运输中");
                ordOrderLogistics.setLogisticsAddress(logisticsMsg);
                ordOrderLogisticsService.insertOrderLogistics(ordOrderLogistics);
                b = true;
            }
        }
        if (ordOrders.size() > 0) {
            for (OrderSearch ordOrder : ordOrders) {
                OrdOrderLogistics ordOrderLogistics = new OrdOrderLogistics();
                String orderId = ordOrder.getOrderId();
                ordOrderLogistics.setOrderId(orderId);
                ordOrderLogistics.setTenantId(tenantId);
                ordOrderLogistics.setOrderStatus(ordOrder.getStatus());
                ordOrderLogistics.setLogisticsAddress(logisticsMsg);
                ordOrderLogistics.setLogisticsMsg("运输中");
                ordOrderLogisticsService.insertOrderLogistics(ordOrderLogistics);
                b = true;
            }
        }
        return b;
    }

    /**
     * 查询司机正在运送的订单号的最新运踪信息
     */
    @ApiOperation(value = "查询司机正在运送的订单号,添加订单GPS运踪信息")
    @GetMapping("/selectLogistics")
    @ResponseBody
    public String[][] selectLogistics() {
        Integer tenantId = getTenantId();
        OrdCommonGoods ordCommonGoods = new OrdCommonGoods();
        ordCommonGoods.setMtenantId(tenantId);
        OrdOrder ordOrder = new OrdOrder();
        ordOrder.setTenantId(tenantId);
        List<OrdOrderLogisticsVo> ordCommonOrderLogistics = ordCommonGoodsService.selectOrder(ordCommonGoods);
        List<OrdOrderLogisticsVo> ordOrderLogistics = ordOrderService.selectOrder(ordOrder);
        int size1 = ordCommonOrderLogistics.size();
        int size2 = ordOrderLogistics.size();
        int size = size1 + size2;
        String[][] logistics = new String[size][4];
        String[][] commonOrderLogistics = new String[size1][4];
        String[][] orderLogistics = new String[size2][4];
        if (size1 > 0) {
            for (int i = 0; i < size1; i++) {
                if (null != ordCommonOrderLogistics.get(i).getLogisticsAddress()) {
                    String address = ordCommonOrderLogistics.get(i).getLogisticsAddress().replace("，",",");
                    String[] splitAddress = new String[7];
                    String[] splitAddress1 = address.split(",");
                    if (splitAddress1.length == 3) {
                        DriverVO driverVO = trunkFeign.selectDriverStatus(ordCommonOrderLogistics.get(i).getDriverId(), tenantId);
                        splitAddress[0] = splitAddress1[0];
                        splitAddress[1] = splitAddress1[1];
                        splitAddress[2] = "地址：" + splitAddress1[2];
                        splitAddress[3] = "订单号：" + ordCommonOrderLogistics.get(i).getOrderId();
                        splitAddress[4] = driverVO!=null?"司机姓名：" + driverVO.getDriverName():"";
                        splitAddress[5] = driverVO!=null?"车牌号：" + driverVO.getPlateNumber():"";
                        splitAddress[6] = driverVO!=null?"所属车队：" + driverVO.getTeamName():"";
                    }
                    commonOrderLogistics[i] = splitAddress;
                }
            }
        }
        if (size2 > 0) {
            for (int i = 0; i < size2; i++) {
                if (null != ordOrderLogistics.get(i).getLogisticsAddress()) {
                    String address = ordOrderLogistics.get(i).getLogisticsAddress().replace("，",",");
                    String[] splitAddress = new String[7];
                    String[] splitAddress1 = address.split(",");
                    if (splitAddress1.length == 3) {
                        DriverVO driverVO = trunkFeign.selectDriverStatus(ordOrderLogistics.get(i).getDriverId(), tenantId);
                        splitAddress[0] = splitAddress1[0];
                        splitAddress[1] = splitAddress1[1];
                        splitAddress[2] = "地址：" + splitAddress1[2];
                        splitAddress[3] = "订单号：" + ordOrderLogistics.get(i).getOrderId();
                        splitAddress[4] = driverVO!=null?"司机姓名：" + driverVO.getDriverName():"";
                        splitAddress[5] = driverVO!=null?"车牌号：" + driverVO.getPlateNumber():"";
                        splitAddress[6] = driverVO!=null?"所属车队：" + driverVO.getTeamName():"";
                    }
                    orderLogistics[i] = splitAddress;
                }
            }
        }
        logistics = (String[][]) ArrayUtil.addAll(commonOrderLogistics, orderLogistics);
        return logistics;
    }

    /**
     * 七日内订单统计
     */
    @GetMapping("/countOrderNumber")
    @ResponseBody
    public Map<String, Integer[]> countOrderNumber() {
        Map<String, Integer[]> countMap = new HashMap<>();
        List<Integer> orders = ordOrderService.countOrderNumber(getTenantId());
        List<Integer> commonOrders = ordCommonGoodsService.countOrderNumber(getTenantId());
        List<Integer> countShortOrders = ordShortOrderService.countOrderNumber(getTenantId());
        Integer[] ordersNumber = new Integer[orders.size()];
        Integer[] commonOrdersNumber = new Integer[commonOrders.size()];
        Integer[] countShortOrdersNumber = new Integer[countShortOrders.size()];
        for (int i = 0; i < orders.size(); i++) {
            ordersNumber[i] = orders.get(i).intValue();
        }
        for (int i = 0; i < commonOrders.size(); i++) {
            commonOrdersNumber[i] = commonOrders.get(i).intValue();
        }
        for (int i = 0; i < countShortOrders.size(); i++) {
            countShortOrdersNumber[i] = countShortOrders.get(i).intValue();
        }
        countMap.put("orders", ordersNumber);
        countMap.put("commonOrders", commonOrdersNumber);
        countMap.put("countShortOrders", countShortOrdersNumber);
        return countMap;
    }

    /**
     * 七日内营业额统计
     */
    @GetMapping("/countMoney")
    @ResponseBody
    public Map<String, BigDecimal[]> countMoney() {
        Map<String, BigDecimal[]> countMap = new HashMap<>();
        List<BigDecimal> countOrders = ordOrderService.countMoney(getTenantId());
        List<BigDecimal> commonOrders = ordCommonGoodsService.countMoney(getTenantId());
        List<BigDecimal> countShortOrders = ordShortOrderService.countMoney(getTenantId());
        BigDecimal[] ordersMoney = new BigDecimal[countOrders.size()];
        BigDecimal[] commonOrdersMoney = new BigDecimal[commonOrders.size()];
        BigDecimal[] countShortOrdersMoney = new BigDecimal[countShortOrders.size()];
        BigDecimal bigss = BigDecimal.ZERO;
        for (int i = 0; i < countOrders.size(); i++) {
            ordersMoney[i] = countOrders.get(i);
        }
        for (int i = 0; i < commonOrders.size(); i++) {
            commonOrdersMoney[i] = commonOrders.get(i);
        }
        for (int i = 0; i < countShortOrders.size(); i++) {
            countShortOrdersMoney[i] = countShortOrders.get(i);
        }
        countMap.put("orders", ordersMoney);
        countMap.put("commonOrders", commonOrdersMoney);
        countMap.put("countShortOrders", countShortOrdersMoney);
        return countMap;
    }


    /**
     * 今日应收应付款
     */
    @GetMapping("/countTodayMoney")
    @ResponseBody
    public Map<String, BigDecimal> countTodayMoney() {
        Map<String, BigDecimal> countMap = new HashMap<>();
        BigDecimal receiveMoney = expensespayableService.countTodayMoney(getTenantId());//应收
        BigDecimal meetMoney = expensespayableService.countTodayMoney(getTenantId());//应付
        countMap.put("receiveMoney", receiveMoney);
        countMap.put("meetMoney", meetMoney);
        return countMap;
    }

    /**
     * 获取订单的车辆运踪GPS信息（未用）
     * http://192.168.16.153:8080/RoadGPS/road/getWork.do?plateNum=%E5%86%80DW9513&startTime=2018-12-01%2016:56:35&endTime=2019-02-20%2016:56:46
     */
    @GetMapping("/getOrderedTruckGPS/{orderId}")
    @ResponseBody
    public List<GPSVO.Gps> getOrderedTruckGPS(@PathVariable String orderId) {
        List<PolylinePathForOrder> polylinePathForOrders = new ArrayList<PolylinePathForOrder>();
        OrderDriverMessage driverMessage = new OrderDriverMessage();
        Map<String, Object> gps = new HashMap<>();
        //String plateNumber = "冀DW9513";
        //String plateNumber = "冀DY6567";
        //String startTime = "2018-12-03 10:56:35";
        //String endTime = "2018-12-04 16:56:46";
        /*车牌号*/
        String plateNumber = null;
        /*接单时间*/
        String startDate = null;
        /*最终时间*/
        String endDate = null;
        OrdOrder order = new OrdOrder();
        PhOrdForUpd commonGoods = new PhOrdForUpd();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        switch (orderId.substring(0, 2)) {
            case "PH":
                //查询订单状态值
                commonGoods = ordCommonGoodsService.selectCommongoodsByOrderId(orderId, getTenantId());
                //查询订单运踪时间
                if (!(commonGoods.getStatus().compareTo("5") < 0)) {
                    plateNumber = order.getPlateNumber();
                    OrdOrderLogistics ordOrderLogistics = ordOrderLogisticsService.selectFirstOrderLogistics(orderId, "5");
                    startDate = format.format(ordOrderLogistics.getLogisticsTime());
                    if (commonGoods.getStatus().compareTo("9") == 0) {
                        OrdOrderLogistics orderLogistic = ordOrderLogisticsService.selectLastOrderLogistics(orderId, "9");
                        endDate = format.format(orderLogistic.getLogisticsTime());
                    } else {
                        endDate = format.format(new Date());
                    }
                }
                break;
            case "CN":
                //查询订单状态值
                order = ordOrderService.selectOrderByOrderId(orderId, getTenantId());
                //查询订单运踪时间
                if (!(order.getStatus().compareTo("3") < 0)) {
                    plateNumber = order.getPlateNumber();
                    OrdOrderLogistics ordOrderLogistics1 = ordOrderLogisticsService.selectFirstOrderLogistics(orderId, "3");
                    Date start = ordOrderLogistics1.getLogisticsTime();
                    startDate = format.format(start);
                    if (order.getStatus().compareTo("11") == 0) {
                        OrdOrderLogistics orderLogistics2 = ordOrderLogisticsService.selectLastOrderLogistics(orderId, "11");
                        Date end = orderLogistics2.getLogisticsTime();
                        endDate = format.format(end);
                    } else {
                        Date ends = new Date();
                        endDate = format.format(ends);
                    }
                }
                break;
        }
        if (plateNumber != null && startDate != null && endDate != null) {
            try {
                String plate = URLEncoder.encode(plateNumber);
                String startTime = URLEncoder.encode(startDate);
                String endTime = URLEncoder.encode(endDate);
                String url = "http://171.15.132.161:8081/RoadGPS/road/getWork.do?plateNum=" + plate + "&startTime=" + startTime + "&endTime=" + endTime;
                String result = HttpClientUtil.doGet(url);
                //GSON直接解析成对象
                GPSVO resultBean = new Gson().fromJson(result, GPSVO.class);
                //对象中拿到集合
                List<GPSVO.Gps> gpsList = resultBean.getPolylinePathlist();
                return gpsList;
            } catch (Exception e) {

            }
        }
        return null;
    }

}
