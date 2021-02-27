package com.zhkj.lc.order.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.util.R;
import com.zhkj.lc.order.dto.*;
import com.zhkj.lc.order.model.entity.OrdOrder;
import com.zhkj.lc.order.model.entity.OrdOrderLogisticsVo;
import com.zhkj.lc.order.model.entity.TruBusiness;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单管理 服务类
 * </p>
 *
 * @author wzc
 * @since 2018-12-07
 */
public interface IOrdOrderService extends IService<OrdOrder> {

    /**
     * 查询订单管理信息
     *
     * @param orderId 订单管理ID
     * @return 订单管理信息
     */
    OrdOrder selectOrderById(String orderId, Integer tenantId);

    OrdOrder selectOrderBaseById(String orderId, Integer tenantId);

    /**
     * 查询订单管理列表
     *
     * @param query 订单管理信息
     * @return 订单管理集合
     */
    Page<OrdOrder> selectOrderList(Query query, OrderSearch orderSearch);

    /**
     * 新增订单管理
     *
     * @param order 订单管理信息
     * @return 结果
     */
    String insertOrder(OrdOrder order) throws IOException;

    /**
     * 修改订单管理
     *
     * @param order 订单管理信息
     * @return 结果
     */
    boolean update(OrdOrder order) throws IOException;

    boolean updateOrderStatus(OrdOrder order);

    /**
     * 删除订单管理信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    boolean deleteOrderByIds(String ids, Integer tenantId, String loginName);

    /**
     *
     * 功能描述:导入文件
     *
     * @param file 文件
     * @param loginName 登陆者
     * @return boolean
     * @auther wzc
     * @date 2018/12/25 20:00
     */
    R<Boolean> importOrder(MultipartFile file, String loginName, Integer tenantId) throws Exception;

    /**
     *
     * 功能描述:导出查询
     *
     * @param response
     * @param orderSearch
     * @return boolean
     * @auther wzc
     * @date 2018/12/25 20:00
     */
    boolean exportOrder(HttpServletResponse response, OrderSearch orderSearch);

    OrdOrderForApp selectOrderByOrderIdForApp(String orderId, Integer tenantId);

    List<OrderSearch> selectOrderByDriverId(OrdOrder ordOrder);

    List<OrderSearch> selectOrderByDriverIdFeign(OrdOrder ordOrder);

    /*根据司机id查询未完成订单*/
    OrdOrder seleteNotEndOrderByDriverId(Integer driverId);

    List<CommonGoodsWxSelect> selectListForWX(CommonOrdSearch ordOrder);

    CommonGoodsWxSelect selectByOrderIdForWX(String orderId,Integer tenantId);

    Integer hasReceiptCode(String orderId, String receiptCode);

    /*箱行亚欧数据流入接口*/
    String insertUpStreamOrder(OrdOrder ordOrder);

    boolean updateOrderByUp(OrdOrderDTO ordOrderDTO);

    /*箱行亚欧派单取消接口*/
    boolean cancelByUpstreamId(OrdOrder ordOrder);

    /*箱信通流入订单查询*/
    Page<OrdOrder> selectUpstreamOrder(Query query, OrdOrder ordOrder);

    boolean upStreamOrderExport(HttpServletResponse response, OrderSearch orderSearch);

    List<TruBusiness> selectTruckBusiness(String plateNumber, String feeMonth,Integer tenantId);

    int countByOrderStatus(OrdOrder order);

    List<OrdOrderLogisticsVo> selectOrder(OrdOrder ordOrder);

    Boolean updateOrderBalanceStatus(OrdOrder ordOrder);

    List<Integer> countOrderNumber(Integer tenantId);

    List<BigDecimal> countMoney(Integer tenantId);

    Boolean updateByOrderIds(String[] orderIds);

    OrdOrder selectOrderByOrderId(String orderId,Integer tenantId);

    Integer selectCountByDriver(Integer driverId, Integer tenantId, String startDate, String endDate);

    Boolean updateBillByOrderIds(String[] orderIds,String login);

    Map getGPSList(String orderId, Integer tenantId);

    R<Boolean> reportExcepFee(UpStreamOrderExcepFee orderExcepFee);

    Boolean returnExcepFee(UpStreamOrderExcepFee orderExcepFee);

    OrderForGPSSystem getPlateNumberOrder(String plateNumber);

    String getAddressByplateNumber(String plateNumber);

    R<Boolean> smsSendAgain(String orderId, Integer tenantId, String phone, Integer sort, String plate);

    BigDecimal getRate(Integer tenantId);
}
