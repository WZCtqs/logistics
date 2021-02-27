package com.zhkj.lc.oilcard.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.oilcard.model.RechargeNormal;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *   正常油卡充值管理 Mapper 接口
 * </p>
 *
 * @author ckj
 * @since 2018-12-14
 */
public interface RechargeNormalMapper extends BaseMapper<RechargeNormal> {

	/**
	 * 根据充值ids正常油卡充值列表
	 *
	 * @param ids 油卡充值IDS
	 * @return 正常油卡充值集合
	 */
	List<RechargeNormal> selectRechargeNormalListByIds(@Param("ids") String[] ids);

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
	 * @param query 分页参数
     * @param rechargeNormal 正常油卡充值信息
     * @return 正常油卡充值集合
     */
	List<RechargeNormal> selectRechargeNormalList(Query query, RechargeNormal rechargeNormal);

	/**
	 * 查询正常油卡已充值列表
	 *
	 * @param rechargeNormal 正常油卡充值信息
	 * @return 正常油卡充值集合
	 */
	List<RechargeNormal> selectRechargedNormalList(RechargeNormal rechargeNormal);
}