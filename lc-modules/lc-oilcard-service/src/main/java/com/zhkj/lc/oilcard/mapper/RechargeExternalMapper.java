package com.zhkj.lc.oilcard.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.oilcard.model.RechargeExternal;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *   外调车油卡充值管理 Mapper 接口
 * </p>
 *
 * @author ckj
 * @since 2018-12-14
 */
public interface RechargeExternalMapper extends BaseMapper<RechargeExternal> {

	/**
	 * 根据充值ids获取外调车油卡充值列表
	 *
	 * @param ids 外调车油卡ids
	 * @return 外调车油卡充值集合
	 */
	List<RechargeExternal> selectRechargeExternalListByIds(@Param("ids") String[] ids);

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
	 * @param query 分页参数
	 * @param rechargeExternal 外调车油卡充值信息
	 * @return 外调车油卡充值集合
	 */
	List<RechargeExternal> selectRechargeExternalList(Query query, RechargeExternal rechargeExternal);
}