package com.zhkj.lc.order.controller;

import com.google.gson.Gson;
import com.zhkj.lc.common.vo.GPSVO;
import com.zhkj.lc.common.vo.SysDictVO;
import com.zhkj.lc.common.web.BaseController;
import com.zhkj.lc.order.dto.*;
import com.zhkj.lc.order.model.entity.OrdCommonGoods;
import com.zhkj.lc.order.model.entity.OrdOrder;
import com.zhkj.lc.order.model.entity.OrdOrderLogistics;
import com.zhkj.lc.order.service.IOrdCommonGoodsService;
import com.zhkj.lc.order.service.IOrdOrderLogisticsService;
import com.zhkj.lc.order.service.IOrdOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@CrossOrigin
@RequestMapping("/wxPublic")
@Api(value = "微信公众号端部分接口")
public class WeiXinPublicController extends BaseController {

    @Autowired
    private IOrdCommonGoodsService ordCommonGoodsService;

    @Autowired
    private IOrdOrderService ordOrderService;

    @Autowired
    private IOrdOrderLogisticsService ordOrderLogisticsService;

    @Autowired
    HttpServletRequest request;

    /**
     *
     * 功能描述: 微信客户端提交订单
     *
     * @param ordCommonGoods
     * @return com.zhkj.lc.common.util.R<java.lang.Boolean>
     * @auther wzc
     * @date 2019/2/13 8:40
     */
    @Transactional
    @RequestMapping("add")
    @CrossOrigin
    @ApiOperation(value = "公众号添加普货订单", notes = "")
    public Boolean add(CommonGoodsForWXPublic ordCommonGoods) {
//        ordCommonGoods.setMtenantId(0);
        ordCommonGoods.setStatus("0");
        ordCommonGoods.setCreateTime(new Date());
        ordCommonGoods.setCreateBy("测试");
        ordCommonGoods.setMtenantId(ordCommonGoods.getTenantId());
        return ordCommonGoodsService.wxAddCommonOrder(ordCommonGoods);
//        return new R<>(Boolean.TRUE);
    }

    @ApiOperation(value = "查询订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "shipperPhone,查询我发送的：必填；pickerPhone,查询我签收的：必填；tenantId，租户id必填；订单状态status用逗号分隔"),
            @ApiImplicitParam(name = "orderId,订单编号；driverReceiptTime,司机接单时间；sendGoodsTime,提货时间；receivedTime，签收时间")
    })
    @RequestMapping("listForWX")
    public List<CommonGoodsWxSelect> selectListForWX(CommonOrdSearch ordSearch){
        List<CommonGoodsWxSelect> commonGoodsWxSelects = ordCommonGoodsService.selectListForWX(ordSearch);
        List<CommonGoodsWxSelect> orderWxSelects = ordOrderService.selectListForWX(ordSearch);
        commonGoodsWxSelects.addAll(orderWxSelects);
        return commonGoodsWxSelects;
    }

    @ApiOperation(value = "根据订单编号查询订单")
    @RequestMapping("selectByOrderId")
    public CommonGoodsWxSelect selectByOrderId(CommonOrdSearch ordSearch){
        List<CommonGoodsWxSelect> order = null;
        switch (ordSearch.getOrderId().substring(0,2)){
            case "PH":
                order = ordCommonGoodsService.selectListForWX(ordSearch);
                break;
            case "CN":
                order = ordOrderService.selectListForWX(ordSearch);
                break;
        }
        if( order != null && order.size() == 1){
            List<OrdOrderLogistics> logistics = ordOrderLogisticsService.selectOrderList(ordSearch.getOrderId());
            order.get(0).setLogistics(logistics);
            Map map = ordOrderService.getGPSList(ordSearch.getOrderId(), ordSearch.getTenantId());
            if(map != null && map.get("gps") != null){
                order.get(0).setGps((List<GPSVO.Gps>)map.get("gps"));
            }
            return order.get(0);
        }
        return null;
    }

    /**
     * 根据订单id查询运踪信息
     * @param orderId
     * @return
     */
    @ApiOperation(value = "根据订单id查询运踪信息")
    @GetMapping("/seletLogisticsByOrderId/{orderId}")
    @ResponseBody
    public String seletLogisticsByOrderId(@PathVariable("orderId") String orderId,@RequestParam("callback") String callback,@RequestParam("tenantId") Integer tenantId){
        OrdCommonGoodsVo ordCommonGoods1 = new OrdCommonGoodsVo();
        OrdOrderLogistics ordOrderLogistics1 = new OrdOrderLogistics();
        System.out.println("*****************租户id:"+tenantId+"   *****************");
        Map<String,Object> map = new HashMap<>();
        ordOrderLogistics1.setTenantId(tenantId);
        ordOrderLogistics1.setOrderId(orderId);
        List<OrdOrderLogistics> ordOrderLogistics = ordOrderLogisticsService.selectOrderListByTenantId(ordOrderLogistics1);
        map.put("ordOrderLogistics",ordOrderLogistics);
        String pre = orderId.substring(0,2);
        if("PH".equals(pre)){
            ordCommonGoods1.setMtenantId(tenantId);
            ordCommonGoods1.setMorderId(orderId);
            OrdCommonGoodsVo ordCommonGoods = ordCommonGoodsService.selectOneByOrderId(ordCommonGoods1);
            map.put("ordCommonGoods",ordCommonGoods);
        }else{
            OrdOrder  ordOrders = ordOrderService.selectOrderById(orderId,tenantId);
            map.put("ordOrders",ordOrders);
        }
        Gson gson=new Gson();   //google的一个json工具库
        return callback+"("+gson.toJson(map)+")";   //构造返回值
    }
}
