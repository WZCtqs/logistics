package com.zhkj.lc.order.mapper;

import com.zhkj.lc.order.model.entity.OrdOrderLogistics;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 运输跟踪 Mapper 接口
 * </p>
 *
 * @author wzc
 * @since 2018-12-07
 */
public interface OrdOrderLogisticsMapper extends BaseMapper<OrdOrderLogistics> {
    /**
     * 查询运输跟踪信息
     *
     * @param orderId 运输跟踪
     * @return 运输跟踪信息
     */
    List<OrdOrderLogistics> selectOrderLogisticsList(@Param("orderId") String orderId);

    /**
     * 新增运输跟踪
     *
     * @param orderLogistics 运输跟踪信息
     * @return 结果
     */
    boolean insertOrderLogistics(OrdOrderLogistics orderLogistics);

    /**
     * 修改运输跟踪
     *
     * @param orderLogistics 运输跟踪信息
     * @return 结果
     */
    boolean updateOrderLogistics(OrdOrderLogistics orderLogistics);

    /**
     * 删除运输跟踪
     *
     * @param id 运输跟踪ID
     * @return 结果
     */
    boolean deleteOrderLogisticsById(Integer id);

    List<OrdOrderLogistics> selectOrderYHX(@Param(value = "orderId") String orderId);

    List<OrdOrderLogistics> selectOrderYQS(String orderId);

    List<OrdOrderLogistics> selectOrderListByTenantId(OrdOrderLogistics ordOrderLogistics);

    Integer hasqsInfo(@Param("orderId") String orderId);

    int countOrders(@Param("tenantId")Integer tenantId);

    int countAllOrders(@Param("tenantId")Integer tenantId);

    OrdOrderLogistics selectFirstOrderLogistics(@Param("orderId") String orderId,@Param("orderStatus") String orderStatus);

    OrdOrderLogistics selectLastOrderLogistics(@Param("orderId") String orderId,@Param("orderStatus") String orderStatus);

    OrdOrderLogistics selectByOrderIdAndStatus(@Param("orderId") String orderId,@Param("orderStatus") String orderStatus);
}
