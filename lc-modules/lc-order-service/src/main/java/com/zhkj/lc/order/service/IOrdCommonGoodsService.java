package com.zhkj.lc.order.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.util.R;
import com.zhkj.lc.common.vo.DriverVO;
import com.zhkj.lc.order.dto.*;
import com.zhkj.lc.order.model.entity.OrdCommonGoods;
import com.zhkj.lc.order.model.entity.OrdOrderLogisticsVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author cb
 * @since 2019-01-03
 */
public interface IOrdCommonGoodsService extends IService<OrdCommonGoods> {

    Boolean addCommonOrder(OrdCommonGoods ordCommonGoods);

    Page selectCommonOrdPage(Query objectQuery, CommonOrdSearch commonOrdSearch);

    Page selectManageGoodsList(Query objectQuery, CommonOrdSearch commonOrdSearch);

    R<Boolean> importPhOrd(MultipartFile file,String loginName, Integer tenantId);

    OrdCommonGoodsVo selectComOrdById(Integer id,Integer tenantId);

    Boolean updatePhOrd(OrdCommonGoods ordCommonGoods,Integer tenantId);

    /**
     * 更新订单状态
     * @param map
     * @return
     */

    Boolean deleteByIds(Map<Integer,String> map, Integer tenantId, String login);

    R<Boolean> sendDriverWhenAdding(OrdCommonGoods ordCommonGoods,Integer tenantId);

    R<Boolean> sendDriverAtManagePage(OrdCommonGoodsVo ordCommonGoods);

    OrdCommonGoodsVo selectOneByOrderId(OrdCommonGoodsVo ordCommonGoodsVo);

    List<CommonOrdSearch> selectOrderByDriverId(OrdCommonGoods ordCommonGoods);

    List<CommonOrdSearch> selectOrderByDriverIdFeign(OrdCommonGoods ordCommonGoods);

    Boolean exportPHCenter(HttpServletRequest request, HttpServletResponse response, String ids,Integer tenantId);

    Page selectCenterPage(Query<Object> objectQuery, CommonOrdSearch commonOrdSearch);

    Boolean exportPhOrd(HttpServletRequest request, HttpServletResponse response, String ids,Integer tenantId);

    CommonGoodsVO selectNotEndPhOrdByDriverId(Integer driverId);

    OrdCommonGoodsForApp selectDetailByOrderId(String orderId,Integer tenantId);

    List<OrdCommonGoods> selectReceiveOrdCommonGoods(OrdCommonGoods ordCommonGoods);

    List<OrdCommonGoods> selectDispatchOrdCommonGoods(OrdCommonGoods ordCommonGoods);

    //小程序端更新订单所需查询
    PhOrdForUpd selectUpdOrdForApp(String orderId);

    List<OrderBaseInfoForApp> selectCompletedOrderForApp(String driverId);

    int countByGoodsOrderStatus(OrdCommonGoods commonGoods);

    List<OrdOrderLogisticsVo> selectOrder(OrdCommonGoods ordCommonGoods);

    /*微信公众号端下单*/
    Boolean wxAddCommonOrder(CommonGoodsForWXPublic ordCommonGoods);

    List<CommonGoodsWxSelect> selectListForWX(CommonOrdSearch ordSearch);

    CommonGoodsWxSelect selectByOrderIdForWX(String orderId,Integer tenantId);

    Boolean updateOrderBalanceStatus(OrdCommonGoods commonGoods);

    List<Integer> countOrderNumber(Integer tenantId);

    List<BigDecimal> countMoney(Integer tenantId);

    Boolean updateByOrderIds(String[] orderIds);

    PhOrdForUpd selectUPDOrdByOrderId(String orderId);

    PhOrdForUpd selectCommongoodsByOrderId(String orderId, Integer tenantId);
    Integer selectCountByDriver(Integer driverId, Integer tenantId, String startDate, String endDate);

    Boolean updateCommonOrd(OrdCommonGoods ordCommonGoods);

    Boolean updateBillByOrderIds(String[] orderIds, String login);

    Boolean updateCommonOrder(ReceiveBillCommonGoods ordCommonGoods);


    /**
     *
     * 根据司机id查询未结束订单数量
     *
     * @param driverIds
     * @param tenantId
     * @return 订单数量
     */
    Integer selectProcOrdByDriverIds(Integer[] driverIds, Integer tenantId);

    List<Integer> selectTruckIdByProc(Integer tenantId);

    List<DriverVO> selectPlateNumberByProc(Integer tenantId);
}
