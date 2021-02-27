package com.zhkj.lc.order.controller;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Date;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.gson.Gson;
import com.zhkj.lc.common.util.UserUtils;
import com.zhkj.lc.common.util.http.HttpClientUtil;
import com.zhkj.lc.common.vo.DriverVO;
import com.zhkj.lc.order.dto.SysSmsTempVO;
import com.zhkj.lc.order.feign.CommonGoodsTruckFeign;
import com.zhkj.lc.order.feign.SystemFeginServer;
import com.zhkj.lc.order.feign.TrunkFeign;
import com.zhkj.lc.order.mapper.OrderSettlementMapper;
import com.zhkj.lc.order.model.entity.*;
import com.zhkj.lc.order.service.IOrdCommonGoodsService;
import com.zhkj.lc.order.service.IOrdOrderService;
import com.zhkj.lc.order.service.IOrdPickupArrivalAdd;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.zhkj.lc.common.constant.CommonConstant;
import com.zhkj.lc.common.util.R;
import com.zhkj.lc.order.service.IOrdOrderLogisticsService;
import com.zhkj.lc.common.web.BaseController;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 运输跟踪 前端控制器
 * </p>
 *
 * @author wzc
 * @since 2018-12-07
 */
@RestController
@RequestMapping("/ordOrderLogistics")
public class OrdOrderLogisticsController extends BaseController {
    @Autowired private IOrdOrderLogisticsService ordOrderLogisticsService;

    @Autowired
    private CommonGoodsTruckFeign commonGoodsTruckFeign;

    @Autowired
    private IOrdCommonGoodsService commonGoodsService;
    @Autowired
    private SystemFeginServer systemFeginServer;
    @Autowired
    private OrderSettlementMapper settlementMapper;
    @Autowired
    private TrunkFeign trunkFeign;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private IOrdOrderService iOrdOrderService;

    @Autowired
    private IOrdPickupArrivalAdd arrivalAdd;

    String xxyoUrl = "http://xxt.zhonghaokeji.cn/coc/PC/updLogistics.do";//线上箱型亚欧地址
    //String gpsUrl = "http://248864g4p0.qicp.vip/RoadGPS/pc/carSelect/getDataFromRoad.do";//线下gpsUrl地址
    //String xxyoUrl = "http://6c7hr8.natappfree.cc/coc/PC/updLogistics.do";//线下gps地址
    String gpsUrl = "http://171.15.132.161:8081/RoadGPS/pc/carSelect/getDataFromRoad.do";//线上gpsUrl地址
    @ApiOperation(value = "司机接单",notes = "")
    @ApiImplicitParam(name = "orderId",value = "订单编号",required = true,dataType = "String",paramType = "path")
    @GetMapping("driverReceipt")
    @Transactional
    public R<Boolean> driverReceipt(String orderId, String logisticsMsg) throws IOException {
        Integer tenantId = Integer.parseInt(request.getHeader("tenantId"));
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (StringUtils.isNotEmpty(logisticsMsg)) {
            new R<>(Boolean.FALSE);
        }
        System.out.println("开始接单------");
        R<Boolean> f = ordOrderLogisticsService.driverReceipt(orderId, logisticsMsg, tenantId);
        OrdOrder ordOrder = iOrdOrderService.selectOrderByOrderId(orderId, tenantId);
        if (f.getData() && ordOrder != null) {
            //判断订单是否从箱型亚欧流入

            if (StringUtils.isNotEmpty(ordOrder.getUpstreamId())) {
                System.out.println(ordOrder.getUpdateTime());
                String getordertime = sf.format(ordOrder.getUpdateTime());
                Map<String, String> map = new HashMap<>();
                map.put("getordertime", getordertime);
                map.put("uuid", orderId);
                Gson gson = new Gson();
                String json = gson.toJson(map);
                //接完单把时间发给gps
                 String gpsUrl = "http://171.15.132.161:8081/RoadGPS/pc/carSelect/getOrderTime.do";
                 HttpClientUtil.doPostJson(gpsUrl,json);
            }

        }
        return f;
        // return ordOrderLogisticsService.driverReceipt(orderId, logisticsMsg, tenantId);
    }

    @ApiOperation(value = "提箱中",notes = "")
    @ApiImplicitParam(name = "orderId",value = "订单编号",required = true,dataType = "String",paramType = "path")
    @GetMapping("pickCn")
    @Transactional
    public R<Boolean> pickCn(String orderId, String logisticsMsg){
        Integer tenantId = Integer.parseInt(request.getHeader("tenantId"));
        return ordOrderLogisticsService.pickCn(orderId, logisticsMsg, tenantId);
    }

    @ApiOperation(value = "提箱完成",notes = "")
    @PutMapping("pickCned")
    @Transactional
    public R<Boolean> pickCned(@RequestBody OrdOrderLogistics logistics){
        logistics.setPhotos(StringUtils.join(logistics.getPaths(),","));
        Integer tenantId = Integer.parseInt(request.getHeader("tenantId"));//提箱凭证接口
        R<Boolean> f = ordOrderLogisticsService.pickCned(logistics,tenantId);
        if(f.getData()) {
            System.out.println("tixiangdi" + logistics.getLogisticsAddress());
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //往箱型亚欧传数据，先判断是否是来自箱型亚欧的单子
            //根据订单id查找该订单的上游编号是否为空
            String orderId = logistics.getOrderId();
            //查找订单信息
            Gson gson = new Gson();
            OrdOrder ordOrder = iOrdOrderService.selectOrderByOrderId(orderId, tenantId);
            if (ordOrder != null && StringUtils.isNotEmpty(ordOrder.getUpstreamId())) {
                String type = ordOrder.getType();
                if (type.equals("0")) {
                    System.out.println("去程才走的点");
                    String addressArray[] = logistics.getLogisticsAddress().split(",");
                    XxtOrder xxtOrder = new XxtOrder();
                    xxtOrder.setLiBindmessageId(orderId);
                    xxtOrder.setLiTrackStaut("1");
                    xxtOrder.setLiCsiOrderNumId(ordOrder.getClassOrder());
                    xxtOrder.setLiYuliu1(ordOrder.getContainerNo());
                    xxtOrder.setLiCarCode(ordOrder.getPlateNumber());
                    xxtOrder.setLiFortuneType("0");
                    xxtOrder.setLiInfoTime(sf.format(new Date()));
                    xxtOrder.setLiInfoAddress(logistics.getLogisticsAddress());
                    xxtOrder.setTenantId(tenantId);
                    xxtOrder.setUpstreamId(ordOrder.getUpstreamId());
                    String json = gson.toJson(xxtOrder);
                    HttpClientUtil.doPostJson(xxyoUrl,json);
                    //gps接口
                    GpsOrder gpsOrder = new GpsOrder();
                    gpsOrder.setUuid(orderId);
                    gpsOrder.setGetboxtime(sf.format(new Date()));//需要确认下，这个目前没有经纬度，
                    if (addressArray.length == 3) {
                        gpsOrder.setGetboxtimeadress(addressArray[2]);
                        gpsOrder.setGetboxtimeadresslon(addressArray[0]);
                        gpsOrder.setGetboxtimeadresslat(addressArray[1]);
                    }
                    //去程第一个点，往gps填一下箱号
                    gpsOrder.setBoxnum(logistics.getContainerNo());
                    String json1 = gson.toJson(gpsOrder);
                    System.out.println("tixiangkaishi1---gps" + gpsOrder);
                    HttpClientUtil.doPostJson(gpsUrl, json1);
                }
            }
        }
        return f;
    }


    @ApiOperation(value = "提货中",notes = "")
    @ApiImplicitParam(name = "orderId",value = "订单编号",required = true,dataType = "String",paramType = "path")
    @GetMapping("pickupGoods")
    @Transactional
    public R<Boolean> pickupGoods(String orderId, String logisticsMsg){
        if (StringUtils.isNotEmpty(logisticsMsg)) {
            new R<>(Boolean.FALSE);
        }
        Integer tenantId = Integer.parseInt(request.getHeader("tenantId"));//达到提货地
        R<Boolean> f = ordOrderLogisticsService.pickupGoods(orderId, logisticsMsg, tenantId);
        if(f.getData()) {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //往箱型亚欧传数据，先判断是否是来自箱型亚欧的单子,根据订单id查找该订单的上游编号是否为空
            String addressArray[] = logisticsMsg.split(",");
            Gson gson = new Gson();
            OrdOrder ordOrder = iOrdOrderService.selectOrderByOrderId(orderId, tenantId);
            if (ordOrder != null && StringUtils.isNotEmpty(ordOrder.getUpstreamId())) {
                String type = ordOrder.getType();
                if (type.equals("0")) {
                    //去程,去程需要往gps传第二个状态
                    if (ordOrder.getUpstreamId() != null) {
                        XxtOrder xxtOrder = new XxtOrder();
                        xxtOrder.setLiBindmessageId(orderId);
                        xxtOrder.setLiTrackStaut("2");
                        xxtOrder.setLiCsiOrderNumId(ordOrder.getClassOrder());
                        xxtOrder.setLiYuliu1(ordOrder.getContainerNo());
                        xxtOrder.setLiCarCode(ordOrder.getPlateNumber());
                        xxtOrder.setLiFortuneType("0");
                        xxtOrder.setLiInfoTime(sf.format(new Date()));
                        xxtOrder.setLiInfoAddress(logisticsMsg);//达到提货地，小程序会定位拿到这个地址吗
                        xxtOrder.setTenantId(tenantId);
                        xxtOrder.setUpstreamId(ordOrder.getUpstreamId());
                        String json = gson.toJson(xxtOrder);
                        System.out.println("kaishi1diaolea--需要往箱型亚欧传第一个状态" + xxtOrder);
                        HttpClientUtil.doPostJson(xxyoUrl,json);
                    }
                    //gps
                    GpsOrder gpsOrder = new GpsOrder();
                    gpsOrder.setUuid(orderId);
                    gpsOrder.setArrivetime(sf.format(new Date()));
                    if (addressArray.length == 3) {
                        gpsOrder.setArrivetimeadresslon(addressArray[0]);
                        gpsOrder.setArrivetimeadresslat(addressArray[1]);
                        gpsOrder.setArrivetimeadress(addressArray[2]);
                    }
                    String json1 = gson.toJson(gpsOrder);
                    System.out.println("gps---");
                    HttpClientUtil.doPostJson(gpsUrl, json1);
                    System.out.println("gps---去程需要往gps传第一个状态" + gpsOrder);
                }
                //回程的时候都不传
            }
        }
        return f;
    }


    @ApiOperation(value = "上传提货凭证",notes = "")
    @PutMapping("pickupGoodsed")
    @Transactional
    public R<Boolean> pickupGoodsed(@RequestBody OrdOrderLogistics logistics){
        System.out.println("gangkaishi"+logistics.getLogisticsMsg());
        logistics.setPhotos(StringUtils.join(logistics.getPaths(),","));
        Integer tenantId = Integer.parseInt(request.getHeader("tenantId"));
        R<Boolean> f = ordOrderLogisticsService.pickupGoodsed(logistics,tenantId);
        if(f.getData()) {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String addressArray[] = logistics.getLogisticsAddress().split(",");
            System.out.println("gangkaishi" + addressArray);
            //往箱型亚欧传数据，先判断是否是来自箱型亚欧的单子
            //根据订单id查找该订单的上游编号是否为空
            String orderId = logistics.getOrderId();
            //查找订单信息
            Gson gson = new Gson();
            OrdOrder ordOrder = iOrdOrderService.selectOrderByOrderId(orderId, tenantId);
            XxtOrder xxtOrder = new XxtOrder();
            GpsOrder gpsOrder = new GpsOrder(); //gps
            if (ordOrder != null && StringUtils.isNotEmpty(ordOrder.getUpstreamId())) {
                String type = ordOrder.getType();
                System.out.println("typeleixing------" + type);
                if (type.equals("0")) {
                    //去程
                    xxtOrder.setLiTrackStaut("3");
                    xxtOrder.setLiFortuneType("0");
                    System.out.println("去程第三点");
                    if (addressArray.length == 3) {
                        gpsOrder.setGetgoodtimeadress(addressArray[2]);
                        gpsOrder.setGetgoodtimeadresslon(addressArray[0]);
                        gpsOrder.setGetgoodtimeadresslat(addressArray[1]);
                    }
                    gpsOrder.setGetgoodtime(sf.format(new Date()));
                } else {
                    //回程
                    xxtOrder.setLiTrackStaut("1");//回程第一个状态
                    System.out.println("回城第一个点");
                    xxtOrder.setLiFortuneType("1");
                    if (addressArray.length == 3) {
                        gpsOrder.setGetboxtimeadress(addressArray[2]);
                        gpsOrder.setGetboxtimeadresslon(addressArray[0]);
                        gpsOrder.setGetboxtimeadresslat(addressArray[1]);
                    }
                    gpsOrder.setGetboxtime(sf.format(new Date()));
                }
                xxtOrder.setLiBindmessageId(orderId);
                xxtOrder.setLiCsiOrderNumId(ordOrder.getClassOrder());
                xxtOrder.setLiYuliu1(ordOrder.getContainerNo());
                xxtOrder.setLiCarCode(ordOrder.getPlateNumber());
                xxtOrder.setLiInfoTime(sf.format(new Date()));
                xxtOrder.setLiInfoAddress(logistics.getLogisticsAddress());
                xxtOrder.setTenantId(tenantId);
                xxtOrder.setUpstreamId(ordOrder.getUpstreamId());
                String json = gson.toJson(xxtOrder);
                System.out.println("kaishi1diaolea" + xxtOrder);
                HttpClientUtil.doPostJson(xxyoUrl,json);

                gpsOrder.setUuid(orderId);
                String json1 = gson.toJson(gpsOrder);
                HttpClientUtil.doPostJson(gpsUrl, json1);
                System.out.println("gps---" + gpsOrder);
            }
        }
        return f;
    }

    @ApiOperation(value = "签收中",notes = "")
    @ApiImplicitParam(name = "orderId",value = "订单编号",required = true,dataType = "String",paramType = "path")
    @GetMapping("receipting")
    @Transactional
    public R<Boolean> receipting(String orderId, String logisticsMsg){
        Integer tenantId = Integer.parseInt(request.getHeader("tenantId"));
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String addressArray[] = logisticsMsg.split(",");
        if(addressArray.length!=3){
            return  new R<>(Boolean.FALSE);
        }
        R<Boolean> f = ordOrderLogisticsService.receipting(orderId, logisticsMsg, tenantId);
        if(f.getData()) {
            //往箱型亚欧传数据，先判断是否是来自箱型亚欧的单,根据订单id查找该订单的上游编号是否为空
            Gson gson = new Gson();
            OrdOrder ordOrder = iOrdOrderService.selectOrderByOrderId(orderId, tenantId);
            XxtOrder xxtOrder = new XxtOrder();
            GpsOrder gpsOrder = new GpsOrder();
            if (ordOrder != null && StringUtils.isNotEmpty(ordOrder.getUpstreamId())) {
                String type = ordOrder.getType();
                //System.out.println("typeleixing------" + type);
                if (type.equals("0")) {
                    //去程
                    xxtOrder.setLiFortuneType("0");
                    xxtOrder.setLiTrackStaut("4");
                    if (addressArray.length == 3) {
                        gpsOrder.setBackboxadress(addressArray[2]);
                        gpsOrder.setBackboxadresslon(addressArray[0]);
                        gpsOrder.setBackboxadresslat(addressArray[1]);
                    }
                    gpsOrder.setDestinationtime(sf.format(new Date()));

                } else {
                    //回程
                    xxtOrder.setLiFortuneType("1");
                    System.out.println("回程第二点");
                    xxtOrder.setLiTrackStaut("2");//回程第二状态
                    if (addressArray.length == 3) {
                        gpsOrder.setArrivetimeadresslon(addressArray[0]);
                        gpsOrder.setArrivetimeadresslat(addressArray[1]);
                        gpsOrder.setArrivetimeadress(addressArray[2]);
                    }
                    gpsOrder.setArrivetime(sf.format(new Date()));
                }
                xxtOrder.setLiBindmessageId(orderId);
                xxtOrder.setLiCsiOrderNumId(ordOrder.getClassOrder());
                xxtOrder.setLiYuliu1(ordOrder.getContainerNo());
                xxtOrder.setLiCarCode(ordOrder.getPlateNumber());
                xxtOrder.setLiInfoTime(sf.format(new Date()));
                xxtOrder.setLiInfoAddress(logisticsMsg);//达到提货地
                xxtOrder.setTenantId(tenantId);
                xxtOrder.setUpstreamId(ordOrder.getUpstreamId());
                String json = gson.toJson(xxtOrder);
                System.out.println("kaishi1diaolea到达卸货地" + xxtOrder);
                HttpClientUtil.doPostJson(xxyoUrl,json);
                gpsOrder.setUuid(orderId);
                String json1 = gson.toJson(gpsOrder);
                HttpClientUtil.doPostJson(gpsUrl, json1);
                System.out.println("gps---到达卸货地" + gpsOrder);
            }
        }
        return  new R<>(Boolean.FALSE);
    }

    @GetMapping("/editReceipt")
    @ApiOperation(value = "编辑签收信息")
    public R<OrdOrderLogistics> editlc(String orderId){
        OrdOrderLogistics  condition = new OrdOrderLogistics();
        condition.setOrderId(orderId);
        condition.setOrderStatus(CommonConstant.ORDER_YQS);
        OrdOrderLogistics logistics = ordOrderLogisticsService.selectOne(new EntityWrapper<>(condition));
        if(logistics!=null){
            OrdPickupArrivalAdd add = arrivalAdd.selectNowReceivingPhoto(orderId);
            if(add != null){
                logistics.setPaths(add.getFiles()!=null ? add.getFiles().split(",") : null);
            }
        }

        return new R<>(logistics);
    }

    @PutMapping("/receiptedFile")
    @ApiOperation(value = "上传签收凭证",notes = "")
    @Transactional
    public R<Boolean> receipted(@RequestBody OrdOrderLogistics logistics){
        Integer tenantId = Integer.parseInt(request.getHeader("tenantId"));
        /*更新签收凭证*/
        OrdPickupArrivalAdd add = new OrdPickupArrivalAdd();
        add.setId(logistics.getAddId());
        add.setFiles(StringUtils.join(logistics.getPaths(),","));
        add.setSuccessAdd(logistics.getLogisticsMsg());
        arrivalAdd.updateById(add);

        logistics.setTenantId(tenantId);
        logistics.setLogisticsAddress(logistics.getLogisticsMsg());//有问题
        //判断订单编号对应的签收状态是否已存在运踪信息，然后执行更新或新增
        Integer exit_flag = ordOrderLogisticsService.hasqsInfo(logistics.getOrderId());//
        //已存在，执行更新
        if(exit_flag==null){
            return ordOrderLogisticsService.receipted(logistics);
        }else if(exit_flag.intValue()==1){
            ordOrderLogisticsService.updateById(logistics);
            return new R<>(Boolean.TRUE);
        }
       else return new R<>(Boolean.FALSE);
    }

    @GetMapping("/receipted")
    @ApiOperation(value = "上传签收码，更新订单为签收状态")
    public R<Boolean> sendCode(String receiptCode, String orderId, Integer driverId, Integer addId){
        Integer tenantId = Integer.parseInt(request.getHeader("tenantId"));
        Boolean f = ordOrderLogisticsService.hasReceiptCode(receiptCode, orderId, driverId, tenantId,addId);
        if(f) {
            OrdOrderLogistics ordOrderLogistics = ordOrderLogisticsService.selectFirstOrderLogistics(orderId, "9");
            System.out.println("dizhi----" + ordOrderLogistics.getLogisticsMsg());
            String addressArray[] = ordOrderLogistics.getLogisticsAddress().split(",");
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //往箱型亚欧传数据，先判断是否是来自箱型亚欧的单子,根据订单id查找该订单的上游编号是否为空
            Gson gson = new Gson();
            OrdOrder ordOrder = iOrdOrderService.selectOrderByOrderId(orderId, tenantId);
            //回程查找已签收地点
            if (ordOrder != null && StringUtils.isNotEmpty(ordOrder.getUpstreamId())) {
                String type = ordOrder.getType();
                if (type.equals("1")) {
                    System.out.println("回城才走的点");
                    XxtOrder xxtOrder = new XxtOrder();
                    xxtOrder.setLiBindmessageId(orderId);
                    xxtOrder.setLiTrackStaut("3");
                    xxtOrder.setLiCsiOrderNumId(ordOrder.getClassOrder());
                    xxtOrder.setLiYuliu1(ordOrder.getContainerNo());
                    xxtOrder.setLiCarCode(ordOrder.getPlateNumber());
                    //回程
                    xxtOrder.setLiFortuneType("1");
                    xxtOrder.setLiInfoTime(sf.format(new Date()));
                    xxtOrder.setLiInfoAddress(addressArray[2]);//达到卸货地
                    xxtOrder.setTenantId(tenantId);
                    xxtOrder.setUpstreamId(ordOrder.getUpstreamId());
                    String json = gson.toJson(xxtOrder);
                    System.out.println("kaishi1diaolea" + xxtOrder);
                    HttpClientUtil.doPostJson(xxyoUrl,json);

                    GpsOrder gpsOrder = new GpsOrder();
                    gpsOrder.setUuid(orderId);
                    gpsOrder.setGetgoodtime(sf.format(new Date()));
                    gpsOrder.setGetgoodtimeadress(ordOrder.getSendGoodsPlace());
                    if (addressArray.length == 3) {
                        gpsOrder.setGetgoodtimeadress(addressArray[2]);
                        gpsOrder.setGetgoodtimeadresslon(addressArray[0]);
                        gpsOrder.setGetgoodtimeadresslat(addressArray[1]);
                    }
                    String json1 = gson.toJson(gpsOrder);
                    HttpClientUtil.doPostJson(gpsUrl, json1);
                    System.out.println("gps---" + gpsOrder);
                }else{
                    //去程最后的一个点，需要往gps传一下该订单已走完，gps单独写接口区分最好
                    if(StringUtils.isNotEmpty(orderId)){
                        //去程操作完成，发送给gps
                        String url = "http://171.15.132.161:8081/RoadGPS/pc/carSelect/untiedCarSelect.do?uuid="+orderId;
                        HttpClientUtil.doPost(url);
                    }

                }
            }
        }
        return new R<Boolean>(f);
        // return new R<Boolean>(ordOrderLogisticsService.hasReceiptCode(receiptCode, orderId, driverId, tenantId));

    }

    @ApiOperation(value = "还箱中",notes = "")
    @ApiImplicitParam(name = "orderId",value = "订单编号",required = true,dataType = "String",paramType = "path")
    @GetMapping("returnCn")
    @Transactional
    public R<Boolean> returnCn(String orderId, String logisticsMsg){
        Integer tenantId = Integer.parseInt(request.getHeader("tenantId"));
        R<Boolean> f = ordOrderLogisticsService.returnCn(orderId, logisticsMsg,tenantId);
        if(f.getData()) {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String addressArray[] = logisticsMsg.split(",");
            //往箱型亚欧传数据，先判断是否是来自箱型亚欧的单子,根据订单id查找该订单的上游编号是否为空查找订单信息
            Gson gson = new Gson();
            OrdOrder ordOrder = iOrdOrderService.selectOrderByOrderId(orderId, tenantId);
            if (ordOrder != null && StringUtils.isNotEmpty(ordOrder.getUpstreamId())) {
                String type = ordOrder.getType();
                if (type.equals("1")) {
                    System.out.println("huichengcaizoudedian");
                    //回程
                    XxtOrder xxtOrder = new XxtOrder();
                    xxtOrder.setLiBindmessageId(orderId);
                    xxtOrder.setLiTrackStaut("4");
                    xxtOrder.setLiCsiOrderNumId(ordOrder.getClassOrder());
                    xxtOrder.setLiYuliu1(ordOrder.getContainerNo());
                    xxtOrder.setLiCarCode(ordOrder.getPlateNumber());
                    xxtOrder.setLiFortuneType("1");
                    xxtOrder.setLiInfoTime(sf.format(new Date()));
                    xxtOrder.setLiInfoAddress(logisticsMsg);//换箱完成
                    xxtOrder.setTenantId(tenantId);
                    xxtOrder.setUpstreamId(ordOrder.getUpstreamId());
                    String json = gson.toJson(xxtOrder);
                    System.out.println("kaishi1diaolea" + xxtOrder);
                    HttpClientUtil.doPostJson(xxyoUrl,json);
                    GpsOrder gpsOrder = new GpsOrder();
                    gpsOrder.setUuid(orderId);
                    gpsOrder.setDestinationtime(sf.format(new Date()));
                    if (addressArray.length == 3) {
                        gpsOrder.setBackboxadress(addressArray[2]);
                        gpsOrder.setBackboxadresslon(addressArray[0]);
                        gpsOrder.setBackboxadresslat(addressArray[1]);
                    }
                    String json1 = gson.toJson(gpsOrder);
                    HttpClientUtil.doPostJson(gpsUrl, json1);
                    System.out.println("gps---到达还箱地" + gpsOrder);
                }
            }
        }
        return f;
    }


    @ApiOperation(value = "还箱完成",notes = "")
    @PutMapping("returnCned")
    @Transactional
    public R<Boolean> returnCned(@RequestBody OrdOrderLogistics logistics){
        logistics.setPhotos(StringUtils.join(logistics.getPaths(),","));
        Integer tenantId = Integer.parseInt(request.getHeader("tenantId"));
        //更新司机状态
        Integer driverId = Integer.parseInt(request.getHeader("driverId"));
        updateDriverStatusOfApp(driverId,CommonConstant.SJKX, tenantId);
        String orderId = logistics.getOrderId();
        if(StringUtils.isNotEmpty(orderId)){
            //回程操作完成，发送给gps
            String url = "http://171.15.132.161:8081/RoadGPS/pc/carSelect/untiedCarSelect.do?uuid="+orderId;
            HttpClientUtil.doPost(url);
        }


        return ordOrderLogisticsService.returnCned(logistics,tenantId);
    }

    @ApiOperation(value = "已提交",notes = "")
    @PutMapping("isEnd")
    @Transactional
    public R<Boolean> returnCn(@RequestParam String  orderId ){
        /*新增结算数据*/
//        OrderSettlement settlement = settlementMapper.selectSettlementByOrderId(orderId);
//        if(settlement == null){
//            settlement = new OrderSettlement();
//            settlement.setOrderId(orderId);
//            settlementMapper.insert(settlement);
//        }
       if(orderId.startsWith("CN")){
        OrdOrder ordOrder = new OrdOrder();
        ordOrder.setOrderId(orderId);
         ordOrder.setStatus("12");
        return new R<>(iOrdOrderService.updateOrderStatus(ordOrder));}
        else{
         OrdCommonGoods ordCommonGoods=new OrdCommonGoods();
         ordCommonGoods.setMorderId(orderId);
         ordCommonGoods.setStatus("12");
           return new R<>( commonGoodsService.updateCommonOrd(ordCommonGoods));
       }


    }

    @GetMapping("/selectLogistics")
    public List<OrdOrderLogistics> seletLogistics(String orderId){
        return ordOrderLogisticsService.selectOrderList(orderId);
    }

    /**
     * 添加
     * @param  ordOrderLogistics  实体
     * @return success/false
     */
    @PostMapping
    public R<Boolean> add(@RequestBody OrdOrderLogistics ordOrderLogistics) {
        return new R<>(ordOrderLogisticsService.insertOrderLogistics(ordOrderLogistics));
    }

    /**
     * 删除
     * @param id ID
     * @return success/false
     */
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable Integer id) {
        return new R<>(ordOrderLogisticsService.deleteOrderLogisticsById(id));
    }

    /**
     * 编辑
     * @param  ordOrderLogistics  实体
     * @return success/false
     */
    @PutMapping
    public R<Boolean> edit(@RequestBody OrdOrderLogistics ordOrderLogistics) {
        return new R<>(ordOrderLogisticsService.updateOrderLogistics(ordOrderLogistics));
    }


    /**
     * 小程序更新司机状态
     * @param driverId
     * @param status
     */

    public void updateDriverStatusOfApp(Integer driverId,String status, Integer tenantId){
        /*查询司机的车牌号*/
        DriverVO driver = trunkFeign.selectDriverStatus(driverId,tenantId);
        /*查询目前车辆下的司机信息*/
        List<DriverVO> ds = trunkFeign.selectDriverStatusByPlateNumber(driver.getPlateNumber());
        Integer [] driverIds =  new Integer[ds.size()];
        for(int i =0 ; i<ds.size();i++){
            driverIds[i] = ds.get(i).getDriverId();
        }
        DriverVO driverVO = new DriverVO();
        driverVO.setDriverIds(driverIds);
        driverVO.setStatus(status);
        commonGoodsTruckFeign.updateDriverSta(driverVO);
    }
}
