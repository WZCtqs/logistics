package com.zhkj.lc.oilcard.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.oilcard.model.RechargeFreight;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *   运费油卡充值管理 Mapper 接口
 * </p>
 *
 * @author ckj
 * @since 2018-12-14
 */
public interface RechargeFreightMapper extends BaseMapper<RechargeFreight> {

	/**
	 * 根据充值ids获取运费车油卡充值列表
	 *
	 * @param ids 油卡充值ids
	 * @return 运费油卡充值集合
	 */
	List<RechargeFreight> selectRechargeFreightListByIds(@Param("ids") String[] ids);

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
	 * @param query 分页参数
     * @param rechargeFreight 运费油卡充值信息
     * @return 运费油卡充值集合
     */
	List<RechargeFreight> selectRechargeFreightList(Query query, RechargeFreight rechargeFreight);
	
}