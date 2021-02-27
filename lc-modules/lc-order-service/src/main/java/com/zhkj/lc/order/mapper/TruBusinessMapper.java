package com.zhkj.lc.order.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.order.model.entity.OrdExceptionCondition;
import com.zhkj.lc.order.model.entity.OrderFee;
import com.zhkj.lc.order.model.entity.TruBusiness;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

public interface TruBusinessMapper extends BaseMapper<TruBusiness> {
    List<OrderFee> selectOrderFeeList(@Param("plateNumber") String plateNumber, @Param("feeMonth") String feeMonth,@Param("tenantId")Integer tenantId);
}
