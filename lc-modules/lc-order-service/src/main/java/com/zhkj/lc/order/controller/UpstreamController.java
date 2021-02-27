package com.zhkj.lc.order.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.zhkj.lc.common.constant.CommonConstant;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.util.R;
import com.zhkj.lc.common.util.UserUtils;
import com.zhkj.lc.common.util.support.Convert;
import com.zhkj.lc.common.vo.CustomerVO;
import com.zhkj.lc.common.web.BaseController;
import com.zhkj.lc.order.dto.*;
import com.zhkj.lc.order.feign.TrunkFeign;
import com.zhkj.lc.order.mapper.OrdPickupArrivalAddMapper;
import com.zhkj.lc.order.model.entity.OrdOrder;
import com.zhkj.lc.order.model.entity.OrdPickupArrivalAdd;
import com.zhkj.lc.order.service.IOrdOrderService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @Auther: HP
 * @Date: 2019/1/28 19:50
 * @Description:
 */
@RestController
@RequestMapping("/upStream")
public class UpstreamController extends BaseController {

    @Autowired
    private IOrdOrderService ordOrderService;

    @Autowired
    private TrunkFeign trunkFeign;
    @Autowired
    private OrdPickupArrivalAddMapper addMapper;

    @ApiOperation(value = "箱行亚欧系统流入系统接口")
    @PostMapping("add")
    @Transactional
    public R<Boolean> addOrder(@RequestBody OrdOrder ordOrder){

        ordOrder.setCreateBy("箱信通");
        /*获取郑州陆港客户id*/
        CustomerVO customerVO = new CustomerVO();
        customerVO.setTenantId(ordOrder.getTenantId());
        customerVO.setCustomerName("郑州国际陆港开发建设有限公司");
        List<CustomerVO> customerVOS = trunkFeign.getCustomerList(customerVO);
        if(customerVOS !=null && customerVOS.size() == 1){
            ordOrder.setCustomerId(customerVOS.get(0).getCustomerId());
        }
        if(ordOrder.getType().equals("0")){ /*去程，把还箱地和送货地设为null*/
            ordOrder.setSendGoodsPlace("河南省/郑州市/管城回族区");
            ordOrder.setSendGoodsDetailplace("河南省郑州市管城回族区郑州国际陆港");
            ordOrder.setConsignee("郑州陆港");//收货人-郑州陆港
            ordOrder.setConsigneePhone("0371-55177817");//收货人联系方式（0371-**）
            ordOrder.setReturnConPlace(null);
            ordOrder.setReturnConDetailplace(null);
        }else if(ordOrder.getType().equals("1")){ /*回程，吧提箱地和提货地设为null*/
            ordOrder.setPickupConPlace(null);
            ordOrder.setPickupConDetailplace(null);
            ordOrder.setConsignor("郑州陆港");//发货人
            ordOrder.setConsignorPhone("0371-55177817");//发货人电话（0371-**）
            ordOrder.setPickupGoodsPlace("河南省/郑州市/管城回族区");
            ordOrder.setPickupGoodsDetailplace("河南省郑州市管城回族区郑州国际陆港");
        }
        // '车队名，ID，箱量，箱号-箱号-箱号'
        String[] logistics = ordOrder.getLogistics();
        for(int i=0; i<logistics.length; i++){
            /*单个车队*/
            /*定义各个数据值*/
            if(logistics[i]!= null && logistics[i] != ""){
                String[] tenant = Convert.toStrArray(logistics[i]);
                ordOrder.setCarrier(tenant[0]);
                ordOrder.setTenantId(Integer.valueOf(tenant[1]));
                ordOrder.setUpstreamRemark(tenant[2]);
                ordOrder.setContainerNum(1);
                int num = Integer.valueOf(tenant[3]);
                for(int j = 0; j < num; j++){
                    if(tenant.length > 4){
                        String[] containerNos = tenant[4].split("-");
                        ordOrder.setContainerNo(containerNos[j]);
                    }
                    String orderId = ordOrderService.insertUpStreamOrder(ordOrder);
                    /*保存提货地*/
                    OrdPickupArrivalAdd pickAdd = new OrdPickupArrivalAdd();
                    pickAdd.setOrderId(orderId);
                    pickAdd.setAddType("0");
                    pickAdd.setAddressCity(ordOrder.getPickupGoodsPlace());
                    pickAdd.setAddressDetailPlace(ordOrder.getPickupGoodsDetailplace());
                    pickAdd.setTenantId(ordOrder.getTenantId());
                    pickAdd.setContacts(ordOrder.getConsignor());//发货人
                    pickAdd.setContactsPhone(ordOrder.getConsignorPhone());//发货人电话
                    pickAdd.setPlanTime(ordOrder.getPickupGoodsDate());
                    pickAdd.setSort(0);
                    addMapper.insert(pickAdd);
                    OrdPickupArrivalAdd arrivalAdd = new OrdPickupArrivalAdd();
                    arrivalAdd.setOrderId(orderId);
                    arrivalAdd.setAddType("1");
                    arrivalAdd.setAddressCity(ordOrder.getSendGoodsPlace());
                    arrivalAdd.setAddressDetailPlace(ordOrder.getSendGoodsDetailplace());
                    arrivalAdd.setTenantId(ordOrder.getTenantId());
                    arrivalAdd.setContacts(ordOrder.getConsignee());//收货人
                    arrivalAdd.setContactsPhone(ordOrder.getConsigneePhone());//收货人电话
                    arrivalAdd.setPlanTime(ordOrder.getSendGoodsDate());
                    arrivalAdd.setSort(0);
                    addMapper.insert(arrivalAdd);
                }
            }
        }
        return new R<>(Boolean.TRUE);
    }
    @ApiOperation(value = "箱行亚欧系统备注重去重回接口")
    @PostMapping("upd")
    @Transactional
    public R<Boolean> updOrder(@RequestBody OrdOrderDTO ordOrderDTO){

        ordOrderService.updateOrderByUp(ordOrderDTO);
        return new R<>(Boolean.TRUE);
    }

    @ApiOperation(value = "箱行亚欧系统流入系统订单取消接口")
    @GetMapping("cancel")
    @Transactional
    public boolean cancelOrder(String upStreamId,Integer tenantId){
        OrdOrder ordOrder = new OrdOrder();
        ordOrder.setTenantId(tenantId);
        ordOrder.setUpstreamId(upStreamId);
        ordOrder.setStatus(CommonConstant.ORDER_QX);
        return ordOrderService.cancelByUpstreamId(ordOrder);
    }

    @ApiOperation(value = "上游订单查询，orderId-订单编号，customerName-客户名称，status-订单状态:01-计划，02-确认，03-撤销")
    @GetMapping("/page")
    public Page<OrdOrder> selectUpstreamOrder(@RequestParam Map<String, Object> params, OrdOrder ordOrder){
        ordOrder.setTenantId(getTenantId());
        return ordOrderService.selectUpstreamOrder(new Query<>(params),ordOrder);
    }

    /**
     * 删除
     * @param ids ID
     * @return success/false
     */
    @ApiOperation(value = "删除接口，参照订单管理页面删除接口调用")
    @DeleteMapping("/{ids}")
    @Transactional
    public R<Boolean> delete(@PathVariable String ids) {
        return new R<>(ordOrderService.deleteOrderByIds(ids, getTenantId(), UserUtils.getUser()));
    }

    /**
     *
     * 功能描述: 订单数据导出
     *
     * @param
     * @return
     * @auther wzc
     * @date 2018/12/24 16:13
     */
    @ApiOperation(value = "导出接口，参照订单管理页面导出接口调用，ids")
    @GetMapping("exportExcel")
    public boolean exportOrder(HttpServletResponse response, OrderSearch ordOrder){
        ordOrder.setTenantId(getTenantId());
        return ordOrderService.upStreamOrderExport(response, ordOrder);
    }

    /**
     * 通过ID查询
     *
     * @param orderId orderId
     * @return OrdOrde
     */
    @ApiOperation(value = "id查询，参照订单管理页面接口调用")
    @GetMapping("selectByOrderId/{orderId}")
    public OrdOrder  get(@PathVariable String orderId) {
        return ordOrderService.selectOrderById(orderId, getTenantId());
    }

    @ApiOperation(value = "上报异常费用到上游系统")
    @PostMapping("reportExcepFee")
    public R<Boolean> reportExcepFee(@RequestBody UpStreamOrderExcepFee orderExcepFee){
        return ordOrderService.reportExcepFee(orderExcepFee);
    }

    @ApiOperation(value = "接收上游系统异常费用回执")
    @PostMapping("returnExcepFee")
    public Boolean returnExcepFee(@RequestBody UpStreamOrderExcepFee orderExcepFee){
        return ordOrderService.returnExcepFee(orderExcepFee);
    }

    @GetMapping("getPlateNumberOrder")
    public OrderForGPSSystem getPlateNumberOrder(String plateNumber){
        return ordOrderService.getPlateNumberOrder(plateNumber);
    }
}
