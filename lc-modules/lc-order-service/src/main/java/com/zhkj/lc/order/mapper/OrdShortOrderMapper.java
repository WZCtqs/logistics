package com.zhkj.lc.order.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.order.dto.*;
import com.zhkj.lc.order.model.entity.OrdShortOrder;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 盘短管理信息 Mapper 接口
 * </p>
 *
 * @author wzc
 * @since 2018-12-07
 */
public interface OrdShortOrderMapper extends BaseMapper<OrdShortOrder> {

    /**
     *
     * 功能描述: 获取订单编号
     *
     * @param prefix	公司前缀
     * @return java.lang.String
     * @auther wzc
     * @date 2018/12/29 17:22
     */
    Map<String,String> getOrderId(@Param("prefix") String prefix, @Param("orderId") String orderId);

    /**
     * 查询盘短管理信息
     *
     * @param orderId 盘短管理ID
     * @return 盘短管理信息
     */
    OrdShortOrder selectShortOrderByOrderId(String orderId);

    /**
     * 查询OrdShortOrder匹配列表盘短管理信息
     */
    List<OrdShortOrder> selectShortOrderList(Query query, ShortSearch shortOrder);

    List<OrdShortOrderForExport> selectShortOrderListForExport(ShortSearch shortOrder);

    /**
     * 新增盘短管理
     *
     * @param shortOrder 盘短管理信息
     * @return 结果
     */
    boolean insertShortOrder(OrdShortOrder shortOrder);

    /**
     * 修改盘短管理
     *
     * @param shortOrder 盘短管理信息
     * @return 结果
     */
    boolean updateShortOrder(OrdShortOrder shortOrder);

    /**
     * 批量删除盘短管理
     *
     * @param delFlag
     * @param shortOrderIds
     * @param updateBy
     * @return
     * @auther wzc
     * @date 2018/12/12 10:50
     */
    boolean deleteShortOrderByIds(@Param("delFlag") String delFlag, @Param("updateBy") String updateBy, @Param("shortOrderIds") String[] shortOrderIds);

    int countShortOrders(@Param("tenantId")Integer tenantId);

    Boolean updateOrderBalanceStatus(OrdShortOrder shortOrder);

    List<Integer> countOrderNumber(@Param("tenantId")Integer tenantId);

    List<BigDecimal> countMoney(@Param("tenantId")Integer tenantId);

    int updateOrderBillStatus(@Param("ids")Integer[] ids);

    boolean removeBill(@Param("orderIds")String[] orderIds, @Param("tenantId")Integer tenantId);

    List<OrdShortOrder> selectByOrderIds(@Param("orderIds")String[] orderIds, @Param("tenantId")Integer tenantId);
}
