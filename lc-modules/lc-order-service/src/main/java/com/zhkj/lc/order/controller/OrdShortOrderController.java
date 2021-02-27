package com.zhkj.lc.order.controller;
import java.util.Map;

import com.zhkj.lc.common.util.UserUtils;
import com.zhkj.lc.order.dto.ShortSearch;
import com.zhkj.lc.order.utils.CommonUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.plugins.Page;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.util.R;
import com.zhkj.lc.order.model.entity.OrdShortOrder;
import com.zhkj.lc.order.service.IOrdShortOrderService;
import com.zhkj.lc.common.web.BaseController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 盘短管理信息 前端控制器
 * </p>
 *
 * @author wzc
 * @since 2018-12-07
 */
@RestController
@RequestMapping("/ordShortOrder")
public class OrdShortOrderController extends BaseController {
    @Autowired private IOrdShortOrderService ordShortOrderService;
    @Resource
    private ResourceLoader resourceLoader;
    /**
    * 通过ID查询
    *
    * @param orderId orderId
    * @return OrdShortOrder
    */
    @ApiOperation(value = "通过ID查询盘短订单",notes = "接口后直接跟订单编号")
    @ApiImplicitParam(name = "orderId",value = "订单编号",required = true,dataType = "String",paramType = "path")
    @GetMapping("/{orderId}")
    public OrdShortOrder get(@PathVariable String orderId) {
        return ordShortOrderService.selectShortOrderByOrderId(orderId);
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
            @ApiImplicitParam(name = "ordShortOrder", value = "订单编号：orderId,业务日期:orderDate/orderDateTo,业务种类:shortType,路线:transLine,车牌号:plateNumber",paramType = "query",dataType = "CommonOrdSearch")
    })
    @RequestMapping("/page")
    public Page page(@RequestParam Map<String, Object> params, ShortSearch ordShortOrder) {
        ordShortOrder.setTenantId(getTenantId());
        return ordShortOrderService.selectShortOrderList(new Query<>(params), ordShortOrder);
    }

    /**
     *
     * 功能描述: 盘短应付应收订单信息导入功能
     *
     * @param file 盘短订单信息excel
     * @return 导入结果信息
     * @auther wzc
     * @date 2018/12/13 9:32
     */
    @PostMapping("importExcel")
    public R<Boolean> importShortOrder(@RequestParam("file") MultipartFile file) throws Exception{
        if(file.isEmpty()){
            return new R<>(Boolean.FALSE,"上传文件为空！请重新选择...");
        }
        return ordShortOrderService.importShortOrder(file, "wangzhichao", 0);
    }

    @GetMapping("/downloadPdImportModel")
    @ApiOperation(value = "下载盘短导入模板")
    public void downModel(HttpServletRequest request, HttpServletResponse response) {
        String filename = "盘短订单导入模板.xls";
        String path = "static/excel/盘短订单导入模板.xls";
        CommonUtils.downloadThymeleaf(resourceLoader,filename,path,request,response);
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
     * 新增
     * @param  ordShortOrder  实体
     * @return success/false
     */
    @ApiOperation(value = "新增功能",notes = "")
    @PostMapping
    public R<Boolean> add(@RequestBody OrdShortOrder ordShortOrder) {
        ordShortOrder.setCreateBy(UserUtils.getUser());
        ordShortOrder.setTenantId(getTenantId());
        return new R<>(ordShortOrderService.insertShortOrder(ordShortOrder));
    }

    /**
     * 删除
     * @param ids ID
     * @return success/false
     */
    @ApiOperation(value = "批量删除功能",notes = "订单编号用','拼接传值")
    @DeleteMapping("/{ids}")
    public Boolean delete(@PathVariable String ids) {
        return ordShortOrderService.deleteShortOrderByIds(ids, UserUtils.getUser());
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
}
