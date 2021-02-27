package com.zhkj.lc.order.controller;

import com.zhkj.lc.common.util.R;
import com.zhkj.lc.common.web.BaseController;
import com.zhkj.lc.order.model.entity.*;
import com.zhkj.lc.order.service.OrdAdditionalRecordingService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/ordOrder")
public class OrdAdditionalRecordingController extends BaseController {
    @Autowired private OrdAdditionalRecordingService ordAdditionalRecordingService;



    @ApiOperation(value = "集装箱-订单补录")
    @PostMapping("/ordAdditionalRecording")
    public R ordAdditionalRecording(@RequestBody OrdOrder ordOrder) throws IOException {
        Integer tenantId = getTenantId();
        return new R(ordAdditionalRecordingService.ordAdditionalRecording(ordOrder, tenantId));
    }

    @ApiOperation(value = "普货-订单补录")
    @RequestMapping("/ordAdditionalRecordingPH")
    public R ordAdditionalRecordingPH(@RequestBody OrdCommonGoods ordCommonGoods) throws IOException {
        Integer tenantId = getTenantId();

        return new R(ordAdditionalRecordingService.ordAdditionalRecordingPH(ordCommonGoods, tenantId));
    }
}
