package com.zhkj.lc.order.controller;

import com.zhkj.lc.common.constant.CommonConstant;
import com.zhkj.lc.common.util.AjaxResult;
import com.zhkj.lc.common.util.DateUtils;
import com.zhkj.lc.common.util.R;

import com.zhkj.lc.common.util.excel.ExcelUtil;
import com.zhkj.lc.common.vo.OilCardExportVO;
import com.zhkj.lc.common.vo.SysDictVO;
import com.zhkj.lc.common.web.BaseController;
import com.zhkj.lc.order.dto.*;
import com.zhkj.lc.order.feign.SystemFeginServer;
import com.zhkj.lc.order.mapper.NeedPayBillMapper;
import com.zhkj.lc.order.model.entity.*;
import com.zhkj.lc.order.service.INeedPayBillService;
import com.zhkj.lc.order.service.IOrdCommonGoodsService;
import com.zhkj.lc.order.service.IOrdExceptionConditionService;
import com.zhkj.lc.order.service.IOrdOrderService;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/app")
@Api(description = "小程序端部分接口")
public class AppController extends BaseController  {

    @Autowired
    private IOrdCommonGoodsService ordCommonGoodsService;

    @Autowired
    private IOrdOrderService ordOrderService;

    @Autowired
    private IOrdExceptionConditionService ordExceptionConditionService;

    @Autowired
    private NeedPayBillMapper needPayBillMapper;

    @Autowired
    HttpServletRequest request;

    @Autowired
    private SystemFeginServer systemFeginServer;

    @Autowired
    private INeedPayBillService needPayBillService;
    /**
     * 小程序端根据司机id获取订单基本信息
     *
     * @param driverId
     * @return OrderBaseInfoForApp
     */
    @GetMapping("/getOrdBaseInfo")
    @ApiOperation(value = "根据司机id获取该司机正在进行的订单基本信息")
    public R<OrderBaseInfoForApp> getOrderInfo(Integer driverId) {

        OrderBaseInfoForApp orderBaseInfoForApp = new OrderBaseInfoForApp();
        //获取该司机当前正在进行中的订单，即status为未完成的订单
        //获取正在进行的普货订单
        CommonGoodsVO commonGoodsVO = null;
        commonGoodsVO = ordCommonGoodsService.selectNotEndPhOrdByDriverId(driverId);
        //获取正在进行的集装箱订单
        OrdOrder ordOrder = null;
        ordOrder = ordOrderService.seleteNotEndOrderByDriverId(driverId);
        //当前运输的是集装箱订单
        if (ordOrder != null) {
            orderBaseInfoForApp.setOrderId(ordOrder.getOrderId());
            orderBaseInfoForApp.setOrderType("集装箱");
            List<String> goodsInfo = new ArrayList<>();
            orderBaseInfoForApp.setContainerNo(ordOrder.getContainerNo());
            orderBaseInfoForApp.setSealNumber(ordOrder.getSealNumber());
            //货物信息
            String goodDesc = ordOrder.getProductName() + "/重" + ordOrder.getWeight();
            goodsInfo.add(goodDesc);
            orderBaseInfoForApp.setGoodsInfo(goodsInfo);
            //订单状态
            orderBaseInfoForApp.setStatus(getStatusLabel(ordOrder.getStatus(), ordOrder.getTenantId()));
            orderBaseInfoForApp.setType(ordOrder.getType());
            /*获取订单路线*/
            orderBaseInfoForApp.setOrdStart(getStartEndAdd(ordOrder.getPickupAdds(), true));
            orderBaseInfoForApp.setOrdEnd(getStartEndAdd(ordOrder.getArrivalAdds(), false));
            if(ordOrder.getType().equals("0")){
                orderBaseInfoForApp.setOrdStart(ordOrder.getPickupConPlace());
            }else {
                orderBaseInfoForApp.setOrdEnd(ordOrder.getReturnConPlace());
            }
            /*获取当前地址信息*/
            orderBaseInfoForApp.setNowAdd(getNowAdd(ordOrder.getPickupAdds(),ordOrder.getArrivalAdds()));

            orderBaseInfoForApp.setPickupAdds(ordOrder.getPickupAdds());
            orderBaseInfoForApp.setArrivalAdds(ordOrder.getArrivalAdds());
            //订单上次状态的时间
            List<OrdOrderLogistics> logistics = ordOrder.getOrdOrderLogistics();
            orderBaseInfoForApp.setLastStaTime(logistics.get(0).getLogisticsTime());
        }
        //当前运输的是普货订单
        else if (commonGoodsVO != null) {
            orderBaseInfoForApp.setOrderId(commonGoodsVO.getMorderId());
            orderBaseInfoForApp.setOrderType("普货");
            //提货方式获取值
            if(commonGoodsVO.getPickGoodsWay().equals("0")){
                orderBaseInfoForApp.setPickGoodsWay("上门提货");
            }
            else if(commonGoodsVO.getPickGoodsWay().equals("1")){
                orderBaseInfoForApp.setPickGoodsWay("派车直送");
            }
            //货物信息
             List<String> goodsInfo = new ArrayList<>();
            for (CommonGoodsInfo cgInfo : commonGoodsVO.getCommonGoodsInfos()) {
                String goodDesc = cgInfo.getGoodsName() + "/重" + cgInfo.getWeight();
                goodsInfo.add(goodDesc);
            }
            orderBaseInfoForApp.setGoodsInfo(goodsInfo);
            //订单状态
            orderBaseInfoForApp.setStatus(getStatusLabel(commonGoodsVO.getStatus(), commonGoodsVO.getMtenantId()));
            //订单路线
//            orderBaseInfoForApp.setOrdStart(OrdExceptionConditionServiceImpl.getPlace(commonGoodsVO.getSendGoodsPlace()));
//            orderBaseInfoForApp.setOrdEnd(OrdExceptionConditionServiceImpl.getPlace(commonGoodsVO.getPickGoodsPlace()));
            orderBaseInfoForApp.setOrdStart(getStartEndAdd(commonGoodsVO.getPickupAdds(), true));
            orderBaseInfoForApp.setOrdEnd(getStartEndAdd(commonGoodsVO.getArrivalAdds(), false));

            /*获取当前地址信息*/
            orderBaseInfoForApp.setNowAdd(getNowAdd(commonGoodsVO.getPickupAdds(),commonGoodsVO.getArrivalAdds()));

            orderBaseInfoForApp.setPickupAdds(commonGoodsVO.getPickupAdds());
            orderBaseInfoForApp.setArrivalAdds(commonGoodsVO.getArrivalAdds());

            //订单上次状态的更新时间
            List<OrdOrderLogistics> logistics = commonGoodsVO.getOrdOrderLogistics();
            if(logistics != null && logistics.size() > 0){
                orderBaseInfoForApp.setLastStaTime(logistics == null? null: logistics.get(0).getLogisticsTime());
            }
        }
        if(ordOrder == null && commonGoodsVO == null){
            return new R<>();
        }
        return new R<>(orderBaseInfoForApp);
    }

    /**
     * 添加
     * @param ordExceptionCondition
     * @return success/false
     */
    @PostMapping("/addExCondition")
    @ApiOperation(value = "添加订单异常情况信息",notes = "id不用设值,司机信息不用设置;订单id值必写")
    public R<Boolean> add(@RequestBody OrdExceptionCondition ordExceptionCondition) {
        Integer tenantId = Integer.parseInt(request.getHeader("tenantId"));
        try {
            ordExceptionCondition.setSubmitDate(new Date());
            //设置租户id
            ordExceptionCondition.setTenantId(tenantId);
            ordExceptionCondition.setOrderStatus(ordOrderService.selectOrderByOrderId(ordExceptionCondition.getOrderId(),getTenantId()).getStatus());

            //图片路径处理
            ordExceptionCondition.setOecFile(StringUtils.join(ordExceptionCondition.getPaths(),","));
            return new R<>(ordExceptionConditionService.insert(ordExceptionCondition));
        }catch (Exception e){
            logger.info("新增异常情况出错！");
            return new R<>(Boolean.FALSE);
        }

    }


    @GetMapping("/getOrdDetails")
    @ApiOperation(value = "根据订单编号获取订单详情")
    public R<OrdCommonGoodsForApp> getOrdDetailInfo(String orderId){
        Integer tenantId = Integer.parseInt(request.getHeader("tenantId"));
        return new R<>(ordCommonGoodsService.selectDetailByOrderId(orderId,tenantId));
    }

    @GetMapping("/getCNOrdDetails")
    @ApiOperation(value = "根据集装箱订单编号获取订单详情")
    public R<OrdOrderForApp> getCNOrdDetailInfo(String orderId){
        Integer tenantId = Integer.parseInt(request.getHeader("tenantId"));
        return new R<>(ordOrderService.selectOrderByOrderIdForApp(orderId,tenantId));
    }

    public String getStatusLabel(String value, Integer tenantId){
        SysDictVO sysDictVO = new SysDictVO();
        sysDictVO.setType("order_status");
        sysDictVO.setValue(value);
        sysDictVO.setTenantId(tenantId);
        return systemFeginServer.selectDict(sysDictVO).getLabel();
    }

    @GetMapping("completedOrder")
    @ApiOperation(value = "根据司机id查询已完成订单")
    public List<OrderBaseInfoForApp> selectCompletedOrder(String driverId){
        //System.out.println(UserUtils.getUser());
        return ordCommonGoodsService.selectCompletedOrderForApp(driverId);
    }

    /**
     * 小程序我的待办获取运单对账的代办数量和反馈处理中的数量
     */
    @GetMapping("/billNum")
    @ApiOperation(value = "小程序我的待办获取“运单对账”的代办数量和反馈处理中的数量")
    public BillBaseInfo getBillBase(Integer driverId){
         BillBaseInfo billBaseInfo = needPayBillMapper.getBillNum(driverId);
         return billBaseInfo;
    }

    //车主
    @GetMapping("/truckownBillNum")
    @ApiOperation(value = "小程序我的待办获取“运单对账”的代办数量和反馈处理中的数量")
    public BillBaseInfo getBillBase(@RequestParam("truckownId")Integer truckownId,@RequestParam("tenantId")Integer tenantId){
        BillBaseInfo billBaseInfo = needPayBillService.getTruckownBillNum(truckownId,tenantId);
        return billBaseInfo;
    }


    @GetMapping("/accountBillNum")
    @ApiOperation(value = "小程序我的待办获取“最终账单”的代办数量和反馈处理中的数量")
    public BillBaseInfo getaccountBillBase(Integer driverId){
        BillBaseInfo billBaseInfo = needPayBillMapper.getAccountBillNum(driverId);
        return billBaseInfo;
    }

    //车主
    @GetMapping("/truckownAccountBillNum")
    @ApiOperation(value = "小程序我的待办获取“最终账单”的代办数量和反馈处理中的数量")
    public BillBaseInfo getTruckownAccountBillBase(@RequestParam("truckownId")Integer truckownId,@RequestParam("tenantId")Integer tenantId){
        BillBaseInfo billBaseInfo = needPayBillService.getTruckownAccountBillBase(truckownId,tenantId);
        return billBaseInfo;
    }
    /**
     * 小程序查看账单核对基本信息
     */
    @GetMapping("/billBaseInfo")
    @ApiOperation(value = "小程序查看账单核对基本信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "driverId", value = "司机id", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "tenantId", value = "租户id", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "billStatus", value = "账单状态（2：待确认，3：反馈中，5：已完成）", required = true, dataType = "string", paramType = "query")})
    public List<OrderBill> getBillBaseInfo(@RequestParam("driverId")Integer driverId,
                                           @RequestParam("orderId")String orderId,
                                           @RequestParam("billStatus")String billStatus,
                                           @RequestParam("tenantId")Integer tenantId){
        return needPayBillService.getBillBaseInfo(driverId,orderId, billStatus, tenantId);
    }

    //车主  小程序查看账单核对基本信息
    @GetMapping("/getTruckownBillBaseInfo")
    @ApiOperation(value = "小程序查看账单核对基本信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "truckownId", value = "车主id", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "tenantId", value = "租户id", required = true, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "time", value = "时间条件 2019-06-01", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "driverName", value = "司机姓名", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "billStatus", value = "账单状态（2：待确认，3：反馈中，5：已完成）", required = true, dataType = "string", paramType = "query")})
    public List<OrderBill> getTruckownBillBaseInfo(@RequestParam("truckownId")Integer truckownId,
                                           @RequestParam("orderId")String orderId,
                                           @RequestParam("billStatus")String billStatus,
                                           @RequestParam("tenantId")Integer tenantId,
                                           @RequestParam("time")String time,
                                           @RequestParam("driverName")String driverName){
        return needPayBillService.getTruckownBillBaseInfo(truckownId,orderId, billStatus, tenantId,time,driverName);
    }

	@GetMapping("/exportTruckownBillBaseInfo")
	@ApiOperation(value = "导出小程序单结账单核对基本信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "truckownId", value = "车主id", required = true, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "tenantId", value = "租户id", required = true, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "time", value = "时间条件 2019-06-01", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "driverName", value = "司机姓名", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "billStatus", value = "账单状态（2：待确认，3：反馈中，5：已完成）", required = true, dataType = "string", paramType = "query")})
	public AjaxResult exportTruckownBillBaseInfo(@RequestParam("truckownId")Integer truckownId,
							@RequestParam("orderId")String orderId,
							@RequestParam("billStatus")String billStatus,
							@RequestParam("tenantId")Integer tenantId,
							@RequestParam("time")String time,
							@RequestParam("driverName")String driverName,
							 HttpServletRequest request,
							 HttpServletResponse response)
	{
		List<OrderBill> truckownBillBaseInfo = needPayBillService.getTruckownBillBaseInfo(truckownId, orderId, billStatus, tenantId, time, driverName);
		String sheetName = DateUtils.getDate() +"-单结账单汇总表";
		ExcelUtil<OrderBill> util = new ExcelUtil<OrderBill>(OrderBill.class);
		return util.exportExcel(request, response, truckownBillBaseInfo, sheetName,null);
	}

    @GetMapping("/accountBillBaseInfo")
    @ApiOperation(value = "（应付对账单）小程序查看账单核对基本信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "driverId", value = "司机id", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "tenantId", value = "租户id", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "billStatus", value = "账单状态（2：待确认，3：反馈中，5：已完成）", required = true, dataType = "String", paramType = "query")})
    public List<NeedPayBill> accountBillBaseInfo(@RequestParam("driverId")Integer driverId,@RequestParam("billStatus")String billStatus,@RequestParam("tenantId")Integer tenantId){
        return needPayBillService.accountBillBaseInfo(driverId,billStatus, tenantId);
    }

    //车主查看对账单
    @GetMapping("/truckownAccountBillBaseInfo")
    @ApiOperation(value = "（应付对账单）小程序查看账单核对基本信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "truckownId", value = "车主id", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "tenantId", value = "租户id", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "time", value = "时间条件 2019-06-01", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "driverName", value = "司机姓名", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "billStatus", value = "账单状态（2：待确认，3：反馈中，5：已完成）", required = true, dataType = "String", paramType = "query")})
    public List<NeedPayBill> truckAccountBillBaseInfo(@RequestParam("truckownId")Integer truckownId,
													  @RequestParam("billStatus")String billStatus,
													  @RequestParam("tenantId")Integer tenantId,
													  @RequestParam("time")String time,
													  @RequestParam("driverName")String driverName){
        return needPayBillService.truckAccountBillBaseInfo(truckownId,billStatus, tenantId,time,driverName);
    }
	@GetMapping("/exportTruckownAccountBillBaseInfo")
	@ApiOperation(value = "导出小程序单结账单核对基本信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "truckownId", value = "车主id", required = true, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "tenantId", value = "租户id", required = true, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "time", value = "时间条件 2019-06-01", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "driverName", value = "司机姓名", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "billStatus", value = "账单状态（2：待确认，3：反馈中，5：已完成）", required = true, dataType = "string", paramType = "query")})
	public AjaxResult exportTruckownAccountBillBaseInfo(@RequestParam("truckownId")Integer truckownId,
							 @RequestParam("orderId")String orderId,
							 @RequestParam("billStatus")String billStatus,
							 @RequestParam("tenantId")Integer tenantId,
							 @RequestParam("time")String time,
							 @RequestParam("driverName")String driverName,
							 HttpServletRequest request,
							 HttpServletResponse response){

		List<NeedPayBill> needPayBills = needPayBillService.truckAccountBillBaseInfo(truckownId, billStatus, tenantId, time, driverName);
		String sheetName = DateUtils.getDate() +"-月结账单汇总表";
		ExcelUtil<NeedPayBill> util = new ExcelUtil<NeedPayBill>(NeedPayBill.class);
		return util.exportExcel(request, response, needPayBills, sheetName,null);
	}

    /**
     * 小程序司机确认账单
     */
    @GetMapping("/confirmBill")
    @ApiOperation(value = "小程序司机确认账单")
    public R<Boolean> confirmBill(@RequestParam("orderId")String orderId){
        return new R<>(needPayBillService.confirmBill(orderId));
    }

    //加车主
    @GetMapping("/confirmAccountBill")
    @ApiOperation(value = "小程序司机最终账单 确认账单")
    public R<Boolean> confirmAccountBill(@RequestParam("id")Integer id){
        NeedPayBill needPayBill = new NeedPayBill();
        needPayBill.setId(id);
        needPayBill.setUpdateTime(new Date());
        needPayBill.setSettlementStatus(CommonConstant.YQR);
        return new R<>(needPayBillService.updateById(needPayBill));
    }

    /**
     * 小程序司机反馈账单
     */
    @GetMapping("/feedBack")
    @ApiOperation(value = "小程序司机反馈账单")
    public R<Boolean> feedback(@RequestParam("orderId")String orderId,@RequestParam("feedMsg")String feedMsg){
        return new R<>(needPayBillService.feedback(orderId,feedMsg));
    }

    //加车主
    @GetMapping("/feedBackAccountBill")
    @ApiOperation(value = "小程序司机反馈最终账单")
    public R<Boolean> feedBackAccountBill(@RequestParam("id")Integer id,
                                          @RequestParam("feedMsg")String feedMsg,
                                          @RequestParam("feedbackMoney") BigDecimal feedbackMoney){
        NeedPayBill needPayBill = new NeedPayBill();
        needPayBill.setId(id);
        needPayBill.setSettlementStatus(CommonConstant.YFK);
        needPayBill.setUpdateTime(new Date());
        needPayBill.setFeedback(feedMsg);
        needPayBill.setFeedbackMoney(feedbackMoney);
        return new R<>(needPayBillService.updateById(needPayBill));
    }

   /* @GetMapping("/TruckownFeedBackAccountBill")
    @ApiOperation(value = "小程序车主反馈最终账单")
    public R<Boolean> TruckownFeedBackAccountBill(@RequestParam("id")Integer id,
                                          @RequestParam("feedMsg")String feedMsg,
                                          @RequestParam("feedbackMoney") BigDecimal feedbackMoney){
        NeedPayBill needPayBill = new NeedPayBill();
        needPayBill.setId(id);
        needPayBill.setSettlementStatus(CommonConstant.YFK);
        needPayBill.setUpdateTime(new Date());
        needPayBill.setFeedback(feedMsg);
        needPayBill.setFeedbackMoney(feedbackMoney);
        return new R<>(needPayBillService.updateById(needPayBill));
    }*/



    @GetMapping("/revoke")
    @ApiOperation(value = "小程序对账单撤销反馈并确认")
    public R<Boolean> cancleFeedBack(String orderId){
        return new R<>(needPayBillService.cancleFeedback(orderId));
    }


    //加车主
    @GetMapping("/revokeAccountFeed")
    @ApiOperation(value = "小程序最终账单撤销反馈并确认")
    public R<Boolean> cancleAccountFeedBack(Integer id){
        NeedPayBill needPayBill = new NeedPayBill();
        needPayBill.setId(id);
        needPayBill.setSettlementStatus(CommonConstant.YFS);
        needPayBill.setUpdateTime(new Date());
        needPayBill.setFeedback("");
        return new R<>(needPayBillService.updateById(needPayBill));
    }

    @GetMapping("/getExceptionFeeType")
    public List<SysDictVO> getExceptionFeeType(){
        Integer tenantId = Integer.parseInt(request.getHeader("tenantId"));
        return systemFeginServer.findDictByType("exception_fee_type",tenantId);
    }

    public String getStartEndAdd(List<OrdPickupArrivalAdd> list, boolean flag){
        String add  = null;
        if(list !=null && list.size() > 0) {
            /*flag = true,取开始地址*/
            if (flag) {
                add = list.get(0).getAddressCity();
            }
            /*flag = false,取结束地址*/
            else {
                add = list.get(list.size() - 1).getAddressCity();
            }
            String[] adds = add.split("/");
            add = adds[1];
        }
        return add;
    }

    public OrdPickupArrivalAdd getNowAdd(List<OrdPickupArrivalAdd> pickupAdds, List<OrdPickupArrivalAdd> arrivalAdds){
        for(int i=0;i<pickupAdds.size();i++){
            if(pickupAdds.get(i).getSuccessTime() == null){
                return pickupAdds.get(i);
            }
        }
        for(int i=0;i<arrivalAdds.size();i++){
            if(arrivalAdds.get(i).getSuccessTime() == null){
                return arrivalAdds.get(i);
            }
        }
        return null;
    }

}

