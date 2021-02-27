package com.zhkj.lc.order.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.zhkj.lc.common.util.AjaxResult;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.util.R;
import com.zhkj.lc.order.dto.ShortSearch;
import com.zhkj.lc.order.model.entity.OrdShortOrder;
import com.baomidou.mybatisplus.service.IService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 盘短管理信息 服务类
 * </p>
 *
 * @author wzc
 * @since 2018-12-07
 */
public interface IOrdShortOrderService extends IService<OrdShortOrder> {
    /**
     * 查询盘短管理信息
     *
     * @param orderId 盘短管理ID
     * @return 盘短管理信息
     */
    OrdShortOrder selectShortOrderByOrderId(String orderId);

    /**
     * 查询盘短管理列表
     *
     * @param shortOrder 盘短管理信息
     * @return 盘短管理集合
     */
    Page<OrdShortOrder> selectShortOrderList(Query query, ShortSearch shortOrder);

    /**
     *
     * 功能描述: 应付盘短信息导入
     *
     * @param
     * @return
     * @auther wzc
     * @date 2018/12/12 14:32
     */
    R<Boolean> importShortOrder(MultipartFile file, String loginName, Integer tenantId) throws Exception;

    /**
     *
     * 功能描述: 应付应收盘短订单信息导出excel
     *
     * @param
     * @return
     * @auther wzc
     * @date 2018/12/13 9:35
     */
    R<Boolean> exportShortOrder(HttpServletResponse response, ShortSearch ordShortOrder) throws Exception;

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
     * 删除盘短管理信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    boolean deleteShortOrderByIds(String ids, String loginName);

    int countShortOrders(Integer tenantId);

    Boolean updateOrderBalanceStatus(OrdShortOrder shortOrder);

    List<Integer> countOrderNumber(Integer tenantId);

    List<BigDecimal> countMoney(Integer tenantId);
}
