package com.zhkj.lc.oilcard.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.oilcard.model.OilApply;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 油卡申请表 Mapper 接口
 * </p>
 *
 * @author wzc
 * @since 2018-12-07
 */
public interface OilApplyMapper extends BaseMapper<OilApply> {

    /**
     * 根据油卡申请id获取油卡申请信息
     *
     * @param applyId 油卡申请id
     * @return 油卡申请
     */
    OilApply selectByApplyId(@Param("id") Integer applyId);

    /**
     * 根据油卡申请ids获取油卡申请信息
     *
     * @param ids 油卡申请ids
     * @return 油卡申请集合
     */
    List<OilApply> selectApplyLisIds(@Param("ids") String[] ids);

    /**
     * 查询油卡申请列表
     *
     * @param oilApply 油卡申请信息
     * @return 油卡申请集合
     */
    List<OilApply> selectApplyList(OilApply oilApply);

    /**
     * 分页查询油卡申请列表
     *
     * @param query 分页参数
     * @param oilApply 油卡申请信息
     * @return 油卡申请集合
     */
    List<OilApply> selectApplyList(Query query, OilApply oilApply);

    /**|
     * 未发送和已发送油卡号重复判断
     *
     * @param oilCardNumber 油卡号
     * @param tenantId 租户id
     * @return 油卡申请集合
     */
    OilApply selectByOilCardNumber(@Param("oilCardNumber") String oilCardNumber, @Param("tenantId") Integer tenantId);

    /**|
     * 未发送和已发送办卡地址
     *
     * @param tenantId 租户id
     * @return 油卡申请集合
     */
    List<String> selectOpenCardPlace(@Param("tenantId") Integer tenantId);

    /**
     * 新增油卡申请
     *
     * @param oilApply 油卡申请信息
     * @return 结果
     */
    Integer insertOilApply(OilApply oilApply);

    /**
     * 修改油卡申请
     *
     * @param oilApply 油卡申请信息
     * @return 结果
     */
    Integer updateApply(OilApply oilApply);

    /**
     * 批量删除油卡申请
     *
     * @param delFlag 删除标志
     * @param updateBy 删除者
     * @param applyIds 需要删除的数据ID
     * @return 结果
     */
    Integer deleteApplyByIds(@Param("delFlag") String delFlag, @Param("updateBy") String updateBy, @Param("applyIds") String[] applyIds);
}
