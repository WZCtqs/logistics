package com.zhkj.lc.order.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.order.dto.*;
import com.zhkj.lc.order.model.entity.OrdCommonGoods;
import com.zhkj.lc.order.model.entity.OrdOrder;
import com.zhkj.lc.order.model.entity.OrdOrderLogisticsVo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author cb
 * @since 2019-01-03
 */
public interface OrdCommonGoodsMapper extends BaseMapper<OrdCommonGoods> {

    Map<String,String> getPhOrderId(@Param("prefix") String prefix, @Param("orderId") String orderId);

    void insertCommonOrder(OrdCommonGoods ordCommonGoods);

    /**
     * 查询订单详细信息，订单中心用
     * @param objectQuery
     * @param commonOrdSearch
     * @return
     */
    List<OrdCommonGoodsVo> selectCommonOrdList(Query objectQuery, CommonOrdSearch commonOrdSearch);

    /**
     * 查询订单管理基本信息，订单管理用
     * @param objectQuery
     * @param commonOrdSearch
     * @return
     */
    List<CommonGoodsVO> selectManageGoodsList(Query objectQuery, CommonOrdSearch commonOrdSearch);

    /**
     * 订单管理导出集合
     * @param phid
     * @return
     */
    List<OrdCommonGoodsVo> selectExportGoodsList(@Param("phid") String [] phid);

    /**
     * 订单中心导出集合
     * @param phid
     * @return
     */
    List<OrdCommonGoodsVo> selectCenterExportList(@Param("phid") String [] phid);

    OrdCommonGoodsVo selectOneById(Integer id);

    Boolean updateCommonOrd(OrdCommonGoods ordCommonGoods);

    //小程序更新订单状态用
    void updateCommonOrdAPP(PhOrdForUpd phOrdForUpd);

    OrdCommonGoodsVo selectByOrderId(OrdCommonGoodsVo ordCommonGoodsVo);

    List<CommonOrdSearch> selectOrderByDriverId(OrdCommonGoods ordCommonGoods);

    List<CommonOrdSearch> selectOrderByDriverIdFeign(OrdCommonGoods ordCommonGoods);

    //异常情况根据订单编号获取订单基本信息
    CommonGoodsVO selectPhOrdByOrderId(@Param("orderId") String orderId);

    CommonGoodsVO selectProcOrdByDriverId(@Param("driverId") Integer driverId);

    List<OrdCommonGoods> selectReceiveOrdCommonGoods(OrdCommonGoods ordCommonGoods);

    List<OrdCommonGoods> selectDispatchOrdCommonGoods(OrdCommonGoods ordCommonGoods);

    //小程序端获取订单详情
    OrdCommonGoodsForApp selectDetailByOrderId(@Param("orderId") String orderId);

    //小程序更新订单状态时根据订单编号获取数据
    PhOrdForUpd selectUPDOrdByOrderId (@Param("orderId")String orderId);

    Integer hasRecCode(@Param("orderId") String orderId,@Param("receiptCode") String receiptCode);

    int countByGoodsOrderStatus(OrdCommonGoods commonGoods);

    List<OrdOrderLogisticsVo> selectOrder(OrdCommonGoods ordCommonGoods);

    List<CommonGoodsWxSelect> selectListForWX(CommonOrdSearch ordCommonGoods);

    CommonGoodsWxSelect selectByOrderIdForWX(@Param("orderId")String orderId,@Param("tenantId")Integer tenantId);

    Boolean updateOrderBalanceStatus(OrdCommonGoods commonGoods);

    List<Integer> countOrderNumber(@Param("mtenantId")Integer tenantId);

    List<BigDecimal> countMoney(@Param("mtenantId")Integer tenantId);

    Boolean updateByOrderIds(@Param("orderIds") String[] orderIds);

    PhOrdForUpd selectCommongoodsByOrderId(String orderId, Integer tenantId);

    Integer selectCountByDriver(@Param("driverId") Integer driverId, @Param("tenantId")Integer tenantId, @Param("startDate") String startDate, @Param("endDate")String endDate);

    Boolean updateBillByOrderIds(@Param("orderIds") String[] orderIds, @Param("updateBy") String updateBy);

    Boolean updateCommonOrder(ReceiveBillCommonGoods ordCommonGoods);

    /**
     *
     * 根据司机id查询未结束订单数量
     *
     * @param driverIds
     * @param tenantId
     * @return 订单数量
     */
    Integer selectProcCGByDriverIds(@Param("driverIds") Integer[] driverIds, @Param("tenantId")Integer tenantId);

    List<CommonGoodsVO> selectPlateNumberByProcCG(@Param("tenantId")Integer tenantId);

    OrdCommonGoods selectOrderPhoneByOrderId(@Param("orderId") String orderId, @Param("tenantId")Integer tenantId);
}
