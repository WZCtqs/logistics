package com.zhkj.lc.oilcard.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.oilcard.model.RechargeExternal;

import java.util.List;

/**
 * <p>
 * 外调车油卡充值管理 服务类
 * </p>
 *
 * @author ckj
 * @since 2018-12-14
 */
public interface IRechargeExternalService extends IService<RechargeExternal> {

	/**
	 * 根据充值ids获取外调车油卡充值列表
	 *
	 * @param ids 外调车油卡ids
	 * @return 外调车油卡充值集合
	 */
	List<RechargeExternal> selectRechargeExternalListByIds(String ids);

	/**
     * 查询外调车油卡充值列表
     * 
     * @param rechargeExternal 外调车油卡充值信息
     * @return 外调车油卡充值集合
     */
	List<RechargeExternal> selectRechargeExternalList(RechargeExternal rechargeExternal);

	/**
	 * 分页查询外调车油卡充值列表
	 *
	 * @param rechargeExternal 外调车油卡充值信息
	 * @param query 分页参数
	 * @return 外调车油卡充值集合
	 */
	Page<RechargeExternal> selectRechargeExternalListPage(Query query , RechargeExternal rechargeExternal);
	
}
