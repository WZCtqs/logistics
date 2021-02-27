package com.zhkj.lc.oilcard.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.oilcard.model.OilMajorRecharge;

import java.util.List;

/**
 * <p>
 *  主卡充值服务类
 * </p>
 *
 * @author ckj
 * @since 2019-02-11
 */
public interface IOilMajorRechargeService extends IService<OilMajorRecharge> {

    /**
     * 根据id获取主卡充值信息
     * @param majorRechargeId 主卡充值id
     * @return 主卡充值id
     */
    OilMajorRecharge selectMajorRecharge(Integer majorRechargeId);

    /**
     * 根据ids获取主卡充值信息
     * @param ids 主卡充值ids
     * @return 主卡充值集合
     */
    List<OilMajorRecharge> selectMajorRechargeByIds(String ids);

    /**
     * 根据条件获取主卡充值信息
     * @param oilMajorRecharge 主卡充值信息
     * @return 主卡充值集合
     */
    List<OilMajorRecharge> selectMajorRechargeList(OilMajorRecharge oilMajorRecharge);

    /**
     * 根据条件分页获取主卡充值信息
     * @param query 分页参数
     * @param oilMajorRecharge 主卡充值信息
     * @return 主卡充值集合
     */
    Page<OilMajorRecharge> selectMajorRechargeList(Query query, OilMajorRecharge oilMajorRecharge);

    /**
     * 更新
     * @param  oilMajorRecharge  主卡充值信息
     * @return 结果
     */
    boolean updateMajorRecharge(OilMajorRecharge oilMajorRecharge);

    /**
     * 添加
     * @param  oilMajorRecharge  主卡充值信息
     * @return 结果
     */
    boolean insertMajorRecharge(OilMajorRecharge oilMajorRecharge);

    /**
     * 删除
     * @param  ids  主卡充值ids
     * @param updateBy 更新者
     * @return 结果
     */
    boolean deleteMajorRecharge(String ids, String updateBy);
}
