package com.zhkj.lc.trunk.service.impl;

import com.zhkj.lc.trunk.mapper.InvoiceMapper;
import com.zhkj.lc.trunk.model.Invoice;
import com.zhkj.lc.trunk.service.IInvoiceService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author cb
 * @since 2019-02-13
 */
@Service
public class InvoiceServiceImpl extends ServiceImpl<InvoiceMapper, Invoice> implements IInvoiceService {

    @Autowired InvoiceMapper invoiceMapper;

    @Override
    public Integer checkInvoiceTitle(String invoiceTitle, Integer tenantId) {
        return invoiceMapper.checkInvoiceTitle(invoiceTitle,tenantId);
    }

    @Override
    public Integer checkInvoiceTitleById(String invoiceTitle, Integer tenantId) {
        return invoiceMapper.checkInvoiceTitleById(invoiceTitle,tenantId);
    }
}
