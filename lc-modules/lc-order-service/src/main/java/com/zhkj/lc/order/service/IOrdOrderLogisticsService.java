package com.zhkj.lc.order.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.util.R;
import com.zhkj.lc.order.model.entity.OrdOrderLogistics;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 * 运输跟踪 服务类
 * </p>
 *
 * @author wzc
 * @since 2018-12-07
 */
public interface IOrdOrderLogisticsService extends IService<OrdOrderLogistics> {
    /**
     * 查询运输跟踪信息
     *
     * @param orderId 运输跟踪ID
     * @return 运输跟踪信息
     **/
    List<OrdOrderLogistics> selectOrderList(String orderId);

    /**
     * 查询运输跟踪信息
     * 按租户id查询
     * @param ordOrderLogistics 运输跟踪ID
     * @return 运输跟踪信息
     **/
    List<OrdOrderLogistics> selectOrderListByTenantId(OrdOrderLogistics ordOrderLogistics);

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
     * 删除运输跟踪信息
     *
     * @param id 需要删除的数据ID
     * @return 结果
     */
    boolean deleteOrderLogisticsById(Integer id);

    /**
     *
     * 功能描述: 司机接单接口
     *
     * @param ordeId 订单编号
     * @return com.zhkj.lc.common.util.R<java.lang.Boolean>
     * @auther wzc
     * @date 2019/1/16 14:01
     */
    R<Boolean> driverReceipt(String ordeId, String logisticsMsg, Integer tenantId);

    /**
     *
     * 功能描述:  集装箱订单——司机提箱中接口
     *
     * @param orderId
     * @return com.zhkj.lc.common.util.R<java.lang.Boolean>
     * @auther wzc
     * @date 2019/1/16 14:13
     */
    R<Boolean> pickCn(String orderId, String logisticsMsg, Integer tenantId);

    /**
     *
     * 功能描述: 司机提货完成，上传提货凭证
     *
     * @param logistics	运踪信息
     * @return com.zhkj.lc.common.util.R<java.lang.Boolean>
     * @auther wzc
     * @date 2019/1/16 14:19
     */
    R<Boolean> pickCned(OrdOrderLogistics logistics,Integer tenantId);

    /**
     *
     * 功能描述: 提货中
     *
     * @param orderId
     * @return com.zhkj.lc.common.util.R<java.lang.Boolean>
     * @auther wzc
     * @date 2019/1/16 14:20
     */
    R<Boolean> pickupGoods(String orderId, String logisticsMsg,Integer tenantId);
    /**
     *
     * 功能描述: 提货完成，上传提货凭证
     *
     * @param logistics 凭证信息
     * @return com.zhkj.lc.common.util.R<java.lang.Boolean>
     * @auther wzc
     * @date 2019/1/16 14:34
     */
    R<Boolean> pickupGoodsed(OrdOrderLogistics logistics,Integer tenantId);

    /**
     *
     * 功能描述: 签收中
     *
     * @param orderId
     * @return com.zhkj.lc.common.util.R<java.lang.Boolean>
     * @auther wzc
     * @date 2019/1/16 14:20
     */
    R<Boolean> receipting(String orderId, String logisticsMsg, Integer tenantId);
    /**
     *
     * 功能描述: 上传签收凭证
     *
     * @param logistics 凭证信息
     * @return com.zhkj.lc.common.util.R<java.lang.Boolean>
     * @auther wzc
     * @date 2019/1/16 14:34
     */
    R<Boolean> receipted(OrdOrderLogistics logistics);
    /**
     *
     * 功能描述:  集装箱订单——司机提箱中接口
     *
     * @param orderId
     * @return com.zhkj.lc.common.util.R<java.lang.Boolean>
     * @auther wzc
     * @date 2019/1/16 14:13
     */
    R<Boolean> returnCn(String orderId, String logisticsMsg,Integer tenantId);

    /**
     *
     * 功能描述: 司机提货完成，上传提货凭证
     *
     * @param logistics	运踪信息
     * @return com.zhkj.lc.common.util.R<java.lang.Boolean>
     * @auther wzc
     * @date 2019/1/16 14:19
     */
    R<Boolean> returnCned(OrdOrderLogistics logistics,Integer tenantId);

    Boolean hasReceiptCode(String receiptCode,String orderId,Integer driverId,Integer tenantId, Integer addId);

    Integer hasqsInfo(String orderId);

    int countOrders(Integer tenantId);

    int countAllOrders(Integer tenantId);

    OrdOrderLogistics selectFirstOrderLogistics(String orderId,String orderStatus);

    OrdOrderLogistics selectLastOrderLogistics(String orderId,String orderStatus);

    OrdOrderLogistics selectByOrderIdAndStatus(String orderId,String orderStatus);
}
