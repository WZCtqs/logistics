package com.zhkj.lc.oilcard.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.oilcard.model.OilMajorRecharge;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  主卡充值Mapper 接口
 * </p>
 *
 * @author ckj
 * @since 2019-02-11
 */
public interface OilMajorRechargeMapper extends BaseMapper<OilMajorRecharge> {

    /**
     * 根据id获取主卡充值信息
     * @param majorRechargeId 主卡充值id
     * @return 主卡充值信息
     */
    OilMajorRecharge selectMajorRecharge(@Param("majorRechargeId") Integer majorRechargeId);

    /**
     * 根据ids获取对应的主卡充值信息
     * @param ids 主卡充值ids
     * @return 主卡充值集合
     */
    List<OilMajorRecharge> selectMajorRechargeByIds(@Param("ids") String[] ids);

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
    List<OilMajorRecharge> selectMajorRechargeList(Query query, OilMajorRecharge oilMajorRecharge);

    /**
     * 更新
     * @param  oilMajorRecharge  主卡充值信息
     * @return 结果
     */
    Integer updateMajorRecharge(OilMajorRecharge oilMajorRecharge);

    /**
     * 添加
     * @param  oilMajorRecharge  主卡充值信息
     * @return 结果
     */
    Integer insertMajorRecharge(OilMajorRecharge oilMajorRecharge);

    /**
     * 删除
     * @param  ids  主卡充值ids
     * @param updateBy 更新者
     * @param delFlag 删除标识
     * @return 结果
     */
    Integer deleteMajorRecharge(@Param("ids") String[] ids, @Param("updateBy") String updateBy, @Param("delFlag") String delFlag);
}
