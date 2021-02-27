package com.zhkj.lc.order.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.order.dto.*;
import com.zhkj.lc.order.model.entity.OrdOrder;
import com.zhkj.lc.order.model.entity.OrdOrderLogisticsVo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单管理 Mapper 接口
 * </p>
 *
 * @author wzc
 * @since 2018-12-07
 */
public interface OrdOrderMapper extends BaseMapper<OrdOrder> {

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
     * 查询订单管理信息
     *
     * @param orderId 订单管理ID
     * @return 订单管理信息
     */
    OrdOrder  selectOrderById(@Param("orderId") String orderId, @Param("tenantId") Integer tenantId);

    /**
     * 查询订单管理列表
     *
     * @param query 订单管理信息
     * @return 订单管理集合
     */
    List<OrdOrder> selectOrderListByPage(Query query, OrderSearch orderSearch);
    /**
     * 查询订单管理列表
     *
     * @param  ordOrder
     * @return 订单管理集合
     */
    List<OrdOrder> selectOrderListByPage(OrderSearch ordOrder);
    /**
     * 查询订单管理列表
     *
     * @param  ordOrder
     * @return 订单管理集合
     */
    List<OrdOrderForExport> selectOrderListForExport(OrderSearch ordOrder);
    /**
     * 新增订单管理
     *
     * @param order 订单管理信息
     * @return 结果
     */
    boolean  insertOrder(OrdOrder order);

    /**
     * 修改订单管理
     *
     * @param order 订单管理信息
     * @return 结果
     */
    boolean updateOrder(OrdOrder order);

    /*箱信通取消订单接口*/
    boolean cancelByUpstreamId(OrdOrder order);

    /*箱信通流入“计划”订单*/
    List<OrdOrder> selectUpstreamOrder(Query query, OrdOrder order);

    List<UpStreamOrder> selectUpStreamOrderExport(OrderSearch orderSearch);

    /**
     * 批量删除订单管理
     *
     * @param orderIds 需要删除的数据ID
     * @return 结果
     */
    boolean deleteOrderByIds(@Param("delFlag") String delFlag, @Param("tenantId") Integer tenantId, @Param("updateBy") String updateBy,@Param("orderIds") String[] orderIds);

    /**
     *
     * 功能描述:小程序查询订单
     *
     * @param orderId
     * @return java.util.List<com.zhkj.lc.order.model.entity.OrdOrder>
     * @auther wzc
     * @date 2018/12/26 8:11
     */
    OrdOrderForApp selectOrderByOrderIdForApp(@Param("orderId") String orderId, @Param("tenantId") Integer tenantId);

    List<OrderSearch> selectOrderByDriverId(OrdOrder ordOrder);

    List<OrderSearch> selectOrderByDriverIdFeign(OrdOrder ordOrder);

    /**
     *
     * 功能描述: 根据司机id查询未结束订单
     *
     * @param driverId
     * @return java.util.List<com.zhkj.lc.order.model.entity.OrdOrder>
     * @auther wzc
     * @date 2019/1/17 9:46
     */
    OrdOrder seleteNotEndOrderByDriverId(@Param("driverId") Integer driverId);

    List<CommonGoodsWxSelect> selectListForWX(CommonOrdSearch ordOrder);

    CommonGoodsWxSelect selectByOrderIdForWX(@Param("orderId")String orderId,@Param("tenantId")Integer tenantId);

    Integer hasRecCode(@Param("orderId")String orderId, @Param("receiptCode")String receiptCode);

    Integer hasRecCodeByAdd(@Param("orderId")String orderId, @Param("receiptCode")String receiptCode, @Param("sort")String sort);

    int countByOrderStatus(OrdOrder order);

    List<OrdOrderLogisticsVo> selectOrder(OrdOrder ordOrder);

    Boolean updateOrderBalanceStatus(OrdOrder ordOrder);

    List<Integer> countOrderNumber(@Param("tenantId")Integer tenantId);

    List<BigDecimal> countMoney(@Param("tenantId") Integer tenantId);

    Boolean updateByOrderIds(@Param("orderIds") String[] orderIds);

    OrdOrder selectOrderByOrderId(@Param("orderId") String orderId, @Param("tenantId") Integer tenantId);

    Integer selectCountByDriver(@Param("driverId") Integer driverId, @Param("tenantId")Integer tenantId, @Param("startDate") String startDate, @Param("endDate")String endDate);

    Boolean updateBillByOrderIds(@Param("orderIds") String[] orderIds, @Param("updateBy") String updateBy);

    Boolean addForYFByOrderIds(@Param("orderIds") String[] orderIds, @Param("updateBy") String updateBy);

    Boolean removeForYFByOrderIds(@Param("orderIds") String[] orderIds, @Param("updateBy") String updateBy);

    OrdOrder selectForGPSByOrderId(String orderId);

    Boolean updateOrderByUp(OrdOrderDTO ordOrderDTO);

    /**
     *
     * 根据司机id查询未结束订单数量
     *
     * @param driverIds
     * @param tenantId
     * @return 订单数量
     */
    Integer selectProcOrdByDriverIds(@Param("driverIds") Integer[] driverIds, @Param("tenantId")Integer tenantId);

    List<OrderForGPSSystem> selectOrderByPlateNumberForGPSSystem(String plateNumber);

    OrdOrder selectOrderPhoneByOrderId(@Param("orderId") String orderId, @Param("tenantId")Integer tenantId);

    List<OrdOrder> selectPlateNumberByProcOrd(@Param("tenantId")Integer tenantId);

    OrdOrder selectOrderBaseById(@Param("orderId") String orderId, @Param("tenantId") Integer tenantId);
}
