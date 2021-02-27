package com.zhkj.lc.oilcard.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.oilcard.model.MajorMonthRecharge;
import com.zhkj.lc.oilcard.model.OilCardCashAmount;

import java.util.List;

/**
 * <p>
 *  主卡月充值服务类
 * </p>
 *
 * @author ckj
 * @since 2019-02-11
 */
public interface IMajorMonthRechargeService extends IService<MajorMonthRecharge> {
    /**
     * 根据id获取主卡月充值信息
     * @param majorMonthId 主卡月充值id
     * @return 主卡月充值信息
     */
    MajorMonthRecharge selectMajorMonthRecharge(Integer majorMonthId);

    /**
     * 根据ids获取主卡月充值信息
     * @param ids 主卡月充值ids
     * @return 主卡月充值集合
     */
    List<MajorMonthRecharge> selectMajorMonthRechargeByIds(String ids);
    /**
     * 根据条件获取主卡月充值信息
     * @param majorId 主卡id
     * @param tenantId 租户id
     * @param yearMonth 年月
     * @return 主卡月充值信息
     */
    MajorMonthRecharge selectMajorMonthRechargeUpdate(Integer majorId, Integer tenantId, String yearMonth);
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
    Page<MajorMonthRecharge> selectMajorMonthRechargeList(Query query, MajorMonthRecharge majorMonthRecharge);

    /**
     * 根据条件获取公司油卡现金金额信息
     * @param yearMonth 年月
     * @param tenantId 租户id
     * @return 公司油卡现金金额集合
     */
    List<OilCardCashAmount> selectMajorMonthRechargeCashAmount(String yearMonth, Integer tenantId);

    /**
     * 根据条件获取公司油卡现金金额信息
     * @param yearMonth 年月
     * @param tenantId 租户id
     * @return 公司油卡现金金额集合
     */
    Page<OilCardCashAmount> selectMajorMonthRechargeCashAmount(Query query ,String yearMonth, Integer tenantId);

    /**
     * 更新
     * @param  majorMonthRecharge  主卡月充值信息
     * @return 结果
     */
    boolean updateMajorMonthRecharge(MajorMonthRecharge majorMonthRecharge);

    /**
     * 添加
     * @param  majorMonthRecharge  主卡月充值信息
     * @return 结果
     */
    boolean insertMajorMonthRecharge(MajorMonthRecharge majorMonthRecharge);

    /**
     * 删除
     * @param  ids  主卡月充值ids
     * @param updateBy 更新者
     * @return 结果
     */
    boolean deleteMajorMonthRecharge(String ids, String updateBy);
}
