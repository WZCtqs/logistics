package com.zhkj.lc.trunk.controller;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Date;

import com.zhkj.lc.common.util.UserUtils;
import com.zhkj.lc.trunk.model.Invoice;
import com.zhkj.lc.trunk.service.IInvoiceService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.zhkj.lc.common.constant.CommonConstant;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.util.R;
import com.zhkj.lc.common.web.BaseController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author cb
 * @since 2019-02-13
 */
@RestController
@RequestMapping("/invoice")
public class InvoiceController extends BaseController {
    @Autowired private IInvoiceService invoiceService;

    /**
    * 通过ID查询
    *
    * @param id ID
    * @return Invoice
    */
    @GetMapping("/{id}")
    @ApiOperation(value = "根据id获取发票详细信息")
    public R<Invoice> get(@PathVariable Integer id) {
        return new R<>(invoiceService.selectById(id));
    }


    /**
    * 分页查询信息
    *
    * @param params 分页对象
    * @return 分页对象
    */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询发票信息")
    public Page page(@RequestParam Map<String, Object> params) {
        params.put(CommonConstant.DEL_FLAG, CommonConstant.STATUS_NORMAL);
        Invoice condition = new Invoice();
        condition.setTenantId(getTenantId());
        return invoiceService.selectPage(new Query<>(params), new EntityWrapper<>(condition));
    }

    /**
     * 添加
     * @param  invoice  实体
     * @return success/false
     */
    @PostMapping
    @ApiOperation(value = "新增发票信息")
    public R<Boolean> add(@RequestBody Invoice invoice) {
        Integer flag = invoiceService.checkInvoiceTitle(invoice.getInvoiceTitle(), getTenantId());
        if(null != flag && flag == 1){
            return new R<>(Boolean.FALSE,"该发票抬头已存在！");
        }
        invoice.setDelFlag(CommonConstant.STATUS_NORMAL);
        invoice.setTenantId(getTenantId());
        invoice.setCreateTime(new Date());
        invoice.setCreateBy(UserUtils.getUser());
        return new R<>(invoiceService.insert(invoice));
    }

    /**
     * 删除
     * @param ids
     * @return success/false
     */
    @DeleteMapping("/{ids}")
    @ApiOperation(value = "批量删除发票信息")
    public R<Boolean> delete(@PathVariable String ids) {
        List<Invoice> invoices = new ArrayList<>();
        String []iids = ids.split(",");
        for (int i = 0;i<iids.length;i++){
            Invoice invoice = new Invoice();
            invoice.setId(Integer.parseInt(iids[i]));
            invoice.setUpdateTime(new Date());
            invoice.setUpdateBy(UserUtils.getUser());
            invoice.setDelFlag(CommonConstant.STATUS_DEL);
            invoices.add(invoice);
        }

        return new R<>(invoiceService.updateBatchById(invoices));
    }

    /**
     * 编辑
     * @param  invoice  实体
     * @return success/false
     */
    @PutMapping
    @ApiOperation(value = "编辑保存发票信息")
    public R<Boolean> edit(@RequestBody Invoice invoice) {
        Integer flag = invoiceService.checkInvoiceTitleById(invoice.getInvoiceTitle(), getTenantId());
        if(null != flag && flag != invoice.getId()){
            return new R<>(Boolean.FALSE,"该发票抬头已存在！");
        }
        invoice.setUpdateTime(new Date());
        invoice.setUpdateBy(UserUtils.getUser());
        return new R<>(invoiceService.updateById(invoice));
    }
}
