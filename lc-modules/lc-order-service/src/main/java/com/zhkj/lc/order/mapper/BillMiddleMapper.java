package com.zhkj.lc.order.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zhkj.lc.order.model.entity.BillMiddle;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 应收对账单表 Mapper 接口
 * </p>
 *
 * @author wzc
 * @since 2019-02-15
 */
public interface BillMiddleMapper extends BaseMapper<BillMiddle> {

    List<BillMiddle> selectByAccountPayNumber(BillMiddle billMiddle);

    Boolean removeOrder(BillMiddle billMiddle);

    Boolean removeAllOrder(BillMiddle billMiddle);

    List<BillMiddle> selectOrderIdByaccId(@Param("accountId")Integer accountId, @Param("tenantId") Integer tenantId);
}
