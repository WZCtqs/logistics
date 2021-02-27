package com.zhkj.lc.order.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.vo.SysDictVO;
import com.zhkj.lc.order.dto.OrdCommonTruckVO;
import com.zhkj.lc.order.model.entity.OrdExceptionFee;
import com.zhkj.lc.order.model.entity.OrdOrder;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wzc
 * @since 2019-01-11
 */
public interface IOrdExceptionFeeService extends IService<OrdExceptionFee> {

    /**
     * 根据id获取异常费用信息
     * @param id 异常费用id
     * @return 异常费用
     */
    OrdExceptionFee getByid(Integer id);

    /**
     * 根据ids查询异常费用管理列表
     *
     * @param ids 异常费用ids
     * @return 异常费用管理集合
     */
    List<OrdExceptionFee> selectOrdExceptionFeeListByIds(String ids);

    /**
     * 查询异常费用管理列表
     *
     * @param ordExceptionFee 异常费用信息
     * @return 异常费用管理集合
     */
    List<OrdExceptionFee> selectOrdExceptionFeeList(OrdExceptionFee ordExceptionFee);

    /**
     * 分页查询异常费用列表
     *
     * @param query 分页参数
     * @param ordExceptionFee 异常费用信息
     * @return 异常费用集合
     */
    Page<OrdExceptionFee> selectOrdExceptionFeeList(Query query, OrdExceptionFee ordExceptionFee);

    /**
     *  新增异常费用信息
     *
     * @param ordExceptionFee 异常费用信息
     * @return 结果
     * @author ckj
     * @date 2019-01-11 17:56
     */
    boolean insertOrdExceptionFee(OrdExceptionFee ordExceptionFee);

    /**
     *  新增异常费用信息
     *
     * @param ordExceptionFee 异常费用信息
     * @return 结果
     * @author ckj
     * @date 2019-01-11 17:56
     */
    boolean updateOrdExceptionFee(OrdExceptionFee ordExceptionFee, OrdOrder ordOrder, OrdCommonTruckVO truckVO);

    /**
     * 批量删除订单管理
     *
     * @param tenantId 租户id
     * @param updateBy 操作者id
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    boolean deleteOrdExceptionFeeByIds(Integer tenantId, String updateBy, String ids);

    /**
     * 根据订单id查询该订单下所有异常费用
     * @param orderId
     * @return
     */
    List<OrdExceptionFee> selectExFee(String orderId);
}
