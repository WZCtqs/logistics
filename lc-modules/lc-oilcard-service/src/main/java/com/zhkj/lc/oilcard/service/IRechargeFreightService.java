package com.zhkj.lc.oilcard.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.oilcard.model.RechargeFreight;

import java.util.List;

/**
 * <p>
 * 运费油卡充值管理 服务类
 * </p>
 *
 * @author ckj
 * @since 2018-12-14
 */
public interface IRechargeFreightService extends IService<RechargeFreight> {

	/**
	 * 根据充值ids获取运费车油卡充值列表
	 *
	 * @param ids 油卡充值ids
	 * @return 运费油卡充值集合
	 */
	List<RechargeFreight> selectRechargeFreightListByIds(String ids);

	/**
     * 查询运费油卡充值列表
     * 
     * @param rechargeFreight 运费油卡充值信息
     * @return 运费油卡充值集合
     */
	List<RechargeFreight> selectRechargeFreightList(RechargeFreight rechargeFreight);

	/**
	 * 分页查询运费油卡充值列表
	 *
	 * @param rechargeFreight 运费油卡充值信息
	 * @param query 分页参数
	 * @return 运费油卡充值集合
	 */
	Page<RechargeFreight> selectRechargeFreightListPage(Query query, RechargeFreight rechargeFreight);

}
