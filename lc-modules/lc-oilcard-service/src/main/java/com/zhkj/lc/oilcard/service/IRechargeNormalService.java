package com.zhkj.lc.oilcard.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.oilcard.model.RechargeNormal;

import java.util.List;

/**
 * <p>
 * 正常油卡充值管理 服务类
 * </p>
 *
 * @author ckj
 * @since 2018-12-14
 */
public interface IRechargeNormalService extends IService<RechargeNormal> {

	/**
	 * 根据充值ids正常油卡充值列表
	 *
	 * @param ids 油卡充值IDS
	 * @return 正常油卡充值集合
	 */
	List<RechargeNormal> selectRechargeNormalListByIds(String ids);

	/**
     * 查询正常油卡充值列表
     * 
     * @param rechargeNormal 正常油卡充值信息
     * @return 正常油卡充值集合
     */
	List<RechargeNormal> selectRechargeNormalList(RechargeNormal rechargeNormal);

	/**
	 * 分页查询正常油卡充值列表
	 *
	 * @param rechargeNormal 正常油卡充值信息
	 * @param query 分页参数
	 * @return 正常油卡充值集合
	 */
	Page<RechargeNormal> selectRechargeNormalListPage(Query query, RechargeNormal rechargeNormal);

	/**
	 * 查询正常油卡已充值列表
	 *
	 * @param rechargeNormal 正常油卡充值信息
	 * @return 正常油卡充值集合
	 */
	List<RechargeNormal> selectRechargedNormalList(RechargeNormal rechargeNormal);
}
