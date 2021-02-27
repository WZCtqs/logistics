package com.zhkj.lc.trunk.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zhkj.lc.trunk.model.Invoice;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author cb
 * @since 2019-02-13
 */
public interface InvoiceMapper extends BaseMapper<Invoice> {
    Integer checkInvoiceTitle(@Param("invoiceTitle")String invoiceTitle, @Param("tenantId")Integer tenantId);
    Integer checkInvoiceTitleById(@Param("invoiceTitle")String invoiceTitle, @Param("tenantId")Integer tenantId);
}
