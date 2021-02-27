package com.zhkj.lc.order.mapper;

import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.order.model.entity.OrdExConDTO;
import com.zhkj.lc.order.model.entity.OrdExceptionCondition;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;


import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author cb
 * @since 2019-01-11
 */
public interface OrdExceptionConditionMapper extends BaseMapper<OrdExceptionCondition> {

    List<OrdExceptionCondition> getExConditions(Query query,OrdExConDTO OrdExConDTO);

    List<OrdExceptionCondition> selectExCondition(@Param("orderId")String orderId);

    List<OrdExceptionCondition> selectExportEXConList(@Param("ycId") Integer []ycId);

    void deleteByExIds(@Param("exid")String[] exid);

    List<OrdExceptionCondition> getExConditionsByOrderId(Query query,@Param("orderId") String orderId);

    Integer deleteByOrderId(@Param("delFlag") String delFlag, @Param("tenantId") Integer tenantId, @Param("orderId") String orderId);
}
