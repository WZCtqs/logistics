package com.zhkj.lc.order.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.util.R;
import com.zhkj.lc.common.util.UserUtils;
import com.zhkj.lc.common.web.BaseController;
import com.zhkj.lc.order.dto.ShortSearch;
import com.zhkj.lc.order.model.entity.OrdShortOrder;
import com.zhkj.lc.order.model.entity.ShortOrderBill;
import com.zhkj.lc.order.service.IOrdShortOrderService;
import com.zhkj.lc.order.service.ShortOrderBillService;
import com.zhkj.lc.order.utils.CommonUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <p>
 * 盘短管理信息 前端控制器
 * </p>
 *
 * @author wzc
 * @since 2018-12-07
 */
@RestController
@RequestMapping("/shortOrderBill")
public class OrdShortBillController extends BaseController {
    @Autowired private IOrdShortOrderService ordShortOrderService;

    @Autowired
    private ShortOrderBillService shortOrderBillService;

    /**
    * 通过ID查询
    *
    * @param accountBillId 对账单编号
    * @return OrdShortOrder
    */
    @ApiOperation(value = "通过对账单编号查询盘短订单",notes = "接口后直接跟对账单编号")
    @ApiImplicitParam(name = "accountBillId",value = "对账单编号",required = true,dataType = "String",paramType = "path")
    @GetMapping("/{accountBillId}")
    public ShortOrderBill get(@PathVariable String accountBillId) {
        return shortOrderBillService.selectOrdersByBillId(accountBillId, 0);
    }

    /**
    * 分页查询信息
    *
    * @param params 分页对象
    * @return 分页对象
    */
    @ApiOperation(value = "分页查询",notes = "业务种类和路线均传相应value值")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "params",value = "分页参数：page页,size页数据量",paramType = "query",dataType = "Map"),
            @ApiImplicitParam(name = "ordShortOrder", value = "订单编号：orderId,业务日期:orderDate/orderDateTo,业务种类:shortType,路线:transLine,车牌号:plateNumber",paramType = "query",dataType = "CommonOrdSearch")})
    @RequestMapping("/page")
    public Page page(@RequestParam Map<String, Object> params, ShortSearch ordShortOrder) {
        ordShortOrder.setTenantId(getTenantId());
        return ordShortOrderService.selectShortOrderList(new Query<>(params), ordShortOrder);
    }

    /**
     *
     * 功能描述: 应付应收盘短订单信息导出excel
     *
     * @param ordShortOrder 查询条件
     * @return 导出结果信息
     * @auther wzc
     * @date 2018/12/13 9:36
     */
    @ApiOperation(value = "导出功能",notes = "将所选导出对象id通过','拼接传值")
    @ApiImplicitParam(name = "ids",value = "订单编号字符串")
    @GetMapping("exportExcel")
    public R<Boolean> exportShortOrder(HttpServletResponse response, ShortSearch ordShortOrder) throws Exception {
        ordShortOrder.setTenantId(getTenantId());
        return ordShortOrderService.exportShortOrder(response, ordShortOrder);
    }

    /**
     * 生成对账单
     * @param  ids  实体
     * @return success/false
     */
    @ApiOperation(value = "生成对账单")
    @ApiImplicitParam(name = "ids: 盘短订单id字符串")
    @PostMapping
    public R<Boolean> add(@RequestParam String ids) {
        return shortOrderBillService.cerateDZD(ids, "wanghzhichao", 0);
    }

    /**
     * 删除
     * @param accopuntBillId ID
     * @return success/false
     */
    @ApiOperation(value = "批量删除功能",notes = "")
    @DeleteMapping("/{accopuntBillId}")
    public R<Boolean> delete(@PathVariable String accopuntBillId) {
        return shortOrderBillService.deleteBillById(accopuntBillId, "wanghzichao", 0);
    }

    /**
     * 编辑
     * @param  ordShortOrder  实体
     * @return success/false
     */
    @ApiOperation(value = "编辑功能",notes = "")
    @PutMapping
    public R<Boolean> edit(@RequestBody OrdShortOrder ordShortOrder) {
        ordShortOrder.setUpdateBy(UserUtils.getUser());
        return new R<>(ordShortOrderService.updateShortOrder(ordShortOrder));
    }
    /**
     * 移除订单
     * @param  accountBillId
     * @param  orderId  实体
     * @return success/false
     */
    @ApiOperation(value = "编辑功能",notes = "")
    @ApiImplicitParam(name = "accountBillId 对账单编号，orderId 订单编号")
    @GetMapping("removeOrder")
    public R<Boolean> removeOrder(String accountBillId, String orderId) {
        return shortOrderBillService.removeShortOrder(accountBillId, orderId, "wang",0);
    }

}
