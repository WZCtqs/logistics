package com.zhkj.lc.oilcard.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.oilcard.model.MajorMonthRecharge;
import com.zhkj.lc.oilcard.model.OilCardCashAmount;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  主卡月充值Mapper 接口
 * </p>
 *
 * @author ckj
 * @since 2019-02-11
 */
public interface MajorMonthRechargeMapper extends BaseMapper<MajorMonthRecharge> {

    /**
     * 根据id获取主卡月充值信息
     * @param majorMonthId 主卡月充值id
     * @return 主卡月充值信息
     */
    MajorMonthRecharge selectMajorMonthRecharge(@Param("majorMonthId") Integer majorMonthId);

    /**
     * 根据ids获取主卡月充值信息
     * @param ids 主卡月充值ids
     * @return 主卡月充值集合
     */
    List<MajorMonthRecharge> selectMajorMonthRechargeByIds(@Param("ids") String[] ids);

    /**
     * 根据条件获取主卡月充值信息
     * @param majorId 主卡id
     * @param tenantId 租户id
     * @param yearMonth 年月
     * @return 主卡月充值信息
     */
    MajorMonthRecharge selectMajorMonthRechargeUpdate(@Param("majorId") Integer majorId, @Param("tenantId") Integer tenantId, @Param("yearMonth") String yearMonth);

    /**
     * 根据条件获取主卡月充值信息
     * @param majorMonthRecharge 主卡月充值信息
     * @return 主卡月充值集合
     */
    List<MajorMonthRecharge> selectMajorMonthRechargeList(MajorMonthRecharge majorMonthRecharge);

    /**
     * 根据条件获取主卡月充值信息
     * @param query 分页参数
     * @param majorMonthRecharge 主卡月充值信息
     * @return 主卡月充值集合
     */
    List<MajorMonthRecharge> selectMajorMonthRechargeList(Query query, MajorMonthRecharge majorMonthRecharge);

    /**
     * 根据条件获取公司油卡现金金额信息
     * @param yearMonth 年月
     * @param tenantId 租户id
     * @return 公司油卡现金金额集合
     */
    List<OilCardCashAmount> selectMajorMonthRechargeCashAmount(@Param("yearMonth") String yearMonth, @Param("tenantId") Integer tenantId);

    /**
     * 根据条件获取公司油卡现金金额信息
     * @param yearMonth 年月
     * @param tenantId 租户id
     * @return 公司油卡现金金额集合
     */
    List<OilCardCashAmount> selectMajorMonthRechargeCashAmount(Query query, @Param("yearMonth") String yearMonth, @Param("tenantId") Integer tenantId);


    /**
     * 更新
     * @param  majorMonthRecharge  主卡月充值信息
     * @return 结果
     */
    Integer updateMajorMonthRecharge(MajorMonthRecharge majorMonthRecharge);

    /**
     * 添加
     * @param  majorMonthRecharge  主卡月充值信息
     * @return 结果
     */
    Integer insertMajorMonthRecharge(MajorMonthRecharge majorMonthRecharge);

    /**
     * 删除
     * @param  ids  主卡月充值ids
     * @param updateBy 更新者
     * @param delFlag 删除标识
     * @return 结果
     */
    Integer deleteMajorMonthRecharge(@Param("ids") String[] ids, @Param("updateBy") String updateBy, @Param("delFlag") String delFlag);
}
