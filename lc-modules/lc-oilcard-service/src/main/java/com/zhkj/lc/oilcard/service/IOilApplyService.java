package com.zhkj.lc.oilcard.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.oilcard.model.OilApply;

import java.util.List;

/**
 * <p>
 * 油卡申请表 服务类
 * </p>
 *
 * @author wzc
 * @since 2018-12-07
 */
public interface IOilApplyService extends IService<OilApply> {

    /**
     * 根据油卡申请id获取油卡申请信息
     *
     * @param applyId 油卡申请id
     * @return 油卡申请
     */
    OilApply selectByApplyId(Integer applyId);

    /**
     * 根据油卡申请ids获取油卡申请信息
     *
     * @param ids 油卡申请ids
     * @return 油卡申请集合
     */
    List<OilApply> selectApplyLisIds(String ids);

    /**
     * 查询油卡申请列表
     *
     * @param oilApply 油卡申请信息
     * @return 油卡申请集合
     */
    List<OilApply> selectApplyList(OilApply oilApply);

    /**|
     * 分页查询油卡基础列表
     *
     * @param oilApply 油卡申请信息
     * @param query 分页信息
     * @return 油卡申请集合
     */
    Page<OilApply> selectApplyListPage(Query query, OilApply oilApply);

    /**|
     * 未发送和已发送油卡号重复判断
     *
     * @param oilCardNumber 油卡号
     * @param tenantId 租户id
     * @return 油卡申请集合
     */
    OilApply selectByOilCardNumber(String oilCardNumber, Integer tenantId);

    /**|
     * 未发送和已发送办卡地址
     *
     * @param tenantId 租户id
     * @return 油卡申请集合
     */
    List<String> selectOpenCardPlace(Integer tenantId);

    /**
     * 修改油卡申请
     *
     * @param oilApply 油卡申请信息
     * @return 结果
     */
    boolean updateApply(OilApply oilApply);

    /**
     * 批量删除油卡申请
     *
     * @param ids 需要删除的id
     * @param updateBy 删除者
     * @return 结果
     */
    boolean deleteApplyByIds(String ids, String updateBy);

    /**
     * 新增油卡申请
     *
     * @param oilApply 油卡申请信息
     * @return 结果
     */
    boolean insertOilApply(OilApply oilApply);
}
