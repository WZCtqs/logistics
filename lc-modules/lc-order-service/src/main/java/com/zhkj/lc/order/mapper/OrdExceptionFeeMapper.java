package com.zhkj.lc.order.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.order.model.entity.OrdExceptionFee;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  异常费用 Mapper 接口
 * </p>
 *
 * @author wzc
 * @since 2019-01-11
 */
public interface OrdExceptionFeeMapper extends BaseMapper<OrdExceptionFee> {

    /**
     * 根据id获取异常费用信息
     * @param id 异常费用id
     * @return 异常费用
     */
    OrdExceptionFee getByid(@Param("id") Integer id);

    /**
     * 根据ids查询异常费用管理列表
     *
     * @param ids 异常费用ids
     * @return 异常费用管理集合
     */
    List<OrdExceptionFee> selectOrdExceptionFeeListByIds(@Param("ids") String[] ids);

    /**
     * 查询异常费用管理列表
     *
     * @param ordExceptionFee 异常费用信息
     * @return 异常费用集合
     */
    List<OrdExceptionFee> selectOrdExceptionFeeList(OrdExceptionFee ordExceptionFee);

    /**
     * 分页查询异常费用列表
     *
     * @param query 分页参数
     * @param ordExceptionFee 异常费用信息
     * @return 异常费用集合
     */
    List<OrdExceptionFee> selectOrdExceptionFeeList(Query query, OrdExceptionFee ordExceptionFee);

    /**
     *  新增异常费用信息
     *
     * @param ordExceptionFee 异常费用信息
     * @return 结果
     * @author ckj
     * @date 2019-01-11 17:56
     */
    Integer insertOrdExceptionFee(OrdExceptionFee ordExceptionFee);

    /**
     *  新增异常费用信息
     *
     * @param ordExceptionFee 异常费用信息
     * @return 结果
     * @author ckj
     * @date 2019-01-11 17:56
     */
    Integer updateOrdExceptionFee(OrdExceptionFee ordExceptionFee);

    /**
     * 批量删除订单管理
     *
     * @param delFlag 删除标识
     * @param tenantId 租户id
     * @param updateBy 操作者id
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    Integer deleteOrdExceptionFeeByIds(@Param("delFlag") String delFlag, @Param("tenantId") Integer tenantId, @Param("updateBy") String updateBy, @Param("ids") String[] ids);


    List<OrdExceptionFee> selectExFee(@Param("orderId")String orderId);

    List<OrdExceptionFee> selectByOrderIdForMoney(@Param("orderId")String orderId);

    Integer deleteByOrderId(@Param("delFlag") String delFlag, @Param("tenantId") Integer tenantId, @Param("updateBy") String updateBy, @Param("orderId") String orderId);

    /*根据对账单号获取订单所有压车费*/
    BigDecimal selectYcFeeByAccountId(String accountId);
    /*根据对账单号获取订单所有其他异常费*/
    BigDecimal selectOtherFeeByAccountId(String accountId);
}
