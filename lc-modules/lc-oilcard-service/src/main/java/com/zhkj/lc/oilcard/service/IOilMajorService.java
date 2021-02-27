package com.zhkj.lc.oilcard.service;

import com.baomidou.mybatisplus.service.IService;
import com.zhkj.lc.oilcard.model.OilMajor;

import java.util.List;

/**
 * <p>
 *  主卡基础信息服务类
 * </p>
 *
 * @author wzc
 * @since 2019-02-11
 */
public interface IOilMajorService extends IService<OilMajor> {

    /**
     * 获取全部主卡号
     * @param tenantId 租户id
     * @return 主卡集合
     */
    List<OilMajor> selectOilMajorNumber(Integer tenantId);

    /**
     * 判断主卡号是否重复
     * @param  majorNumber  主卡号
     * @return 主卡信息
     */
    OilMajor majorNumber2(String majorNumber, Integer tenantId);

    /**
     * 更新
     * @param  oilMajor  主卡信息
     * @return 结果
     */
    boolean updateOilMajor(OilMajor oilMajor);

    /**
     * 添加
     * @param  oilMajor  主卡信息
     * @return 结果
     */
    boolean insertOilMajor(OilMajor oilMajor);

    /**
     * 删除
     * @param  ids  主卡ids
     * @param updateBy 更新者
     * @return 结果
     */
    boolean deleteOilMajor(String ids, String updateBy);

    OilMajor findOne(String majorNumber,Integer tenantId);
}
