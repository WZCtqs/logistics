package com.zhkj.lc.order.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.web.BaseController;
import com.zhkj.lc.order.model.entity.TruBusiness;
import com.zhkj.lc.order.service.IOrdOrderService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/truckFee")
public class TruckFeeController extends BaseController {

    @Autowired
    private IOrdOrderService ordOrderService;
    @GetMapping("/page")
    @ApiOperation(value = "车辆费用统计查询")
    public List<TruBusiness> getTruckFee( @RequestParam(value = "plateNumber",required = false) String plateNumber, @RequestParam(value = "feeMonth" ,required = false) String feeMonth){
        return ordOrderService.selectTruckBusiness(plateNumber,feeMonth, getTenantId());
    }
}
