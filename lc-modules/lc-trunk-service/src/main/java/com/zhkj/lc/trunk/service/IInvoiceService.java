package com.zhkj.lc.trunk.service;

import com.baomidou.mybatisplus.service.IService;
import com.zhkj.lc.trunk.model.Invoice;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author cb
 * @since 2019-02-13
 */
public interface IInvoiceService extends IService<Invoice> {
    Integer checkInvoiceTitle(String invoiceTitle, Integer tenantId);
    Integer checkInvoiceTitleById(String invoiceTitle, Integer tenantId);
}
