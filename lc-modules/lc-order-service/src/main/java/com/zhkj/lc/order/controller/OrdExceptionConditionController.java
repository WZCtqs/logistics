package com.zhkj.lc.order.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Date;

import com.zhkj.lc.order.model.entity.OrdExConDTO;
import com.zhkj.lc.order.service.IOrdOrderService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.zhkj.lc.common.constant.CommonConstant;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.util.R;
import com.zhkj.lc.order.model.entity.OrdExceptionCondition;
import com.zhkj.lc.order.service.IOrdExceptionConditionService;
import com.zhkj.lc.common.web.BaseController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author cb
 * @since 2019-01-11
 */
@RestController
@RequestMapping("/ordExceptionCondition")
public class OrdExceptionConditionController extends BaseController {
    @Autowired
    private IOrdExceptionConditionService ordExceptionConditionService;
    @Autowired
    private IOrdOrderService ordOrderService;

    /**
     * 通过ID查询
     *
     * @param id ID
     * @return OrdExceptionCondition
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "根据id获取单条异常情况信息")
    public R<OrdExceptionCondition> get(@PathVariable Integer id) {
        return new R<>(ordExceptionConditionService.selectExConditionById(id, getTenantId()));
    }

    @GetMapping("/fromCenter/{orderId}")
    @ApiOperation(value = "根据订单编号查询该订单的异常情况集合")
    public Page ecPage(@PathVariable("orderId") String orderId) {
        Map<String, Object> params = new HashMap<String, Object>();
        return ordExceptionConditionService.selectExPageByOrderId(new Query<>(params), orderId,getTenantId());
    }

    /**
     * 分页查询信息
     *
     * @param params 分页对象
     * @return 分页对象
     */
    @GetMapping("/page")
    @ApiOperation(value = "查询异常情况分页数据")
    public Page page(@RequestParam Map<String, Object> params, OrdExConDTO ordExConDTO) {
        ordExConDTO.setTenantId(getTenantId());
        return ordExceptionConditionService.selectExConditionPage(new Query<>(params), ordExConDTO);
    }


    /**
     * 删除
     *
     * @param ids
     * @return success/false
     */
    @DeleteMapping("/{ids}")
    @ApiOperation(value = "根据id删除单条异常情况信息")
    public R<Boolean> delete(@PathVariable String ids) {

        return new R<>(ordExceptionConditionService.deleteByIds(ids));
    }

    /**
     * 编辑
     *
     * @param ordExceptionCondition 实体
     * @return success/false
     */
    @PutMapping
    @ApiOperation(value = "编辑保存异常情况")
    public R<Boolean> edit(@RequestBody OrdExceptionCondition ordExceptionCondition) {
          return new R<>(ordExceptionConditionService.updateById(ordExceptionCondition));
    }

    @GetMapping("/exportEXCondition")
    @ApiOperation(value = "导出异常情况")
    public Boolean exportEX(HttpServletRequest request, HttpServletResponse response, String ids) {
        return ordExceptionConditionService.exportCondition(request, response, ids, getTenantId());
    }
}
