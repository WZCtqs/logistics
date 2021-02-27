package com.zhkj.lc.oilcard.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zhkj.lc.oilcard.model.OilMajor;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  主卡基础信息Mapper 接口
 * </p>
 *
 * @author wzc
 * @since 2019-02-11
 */
public interface OilMajorMapper extends BaseMapper<OilMajor> {

    /**
     * 获取全部主卡号
     * @param tenantId 租户id
     * @return 主卡集合
     */
    List<OilMajor> selectOilMajorNumber(@Param("tenantId") Integer tenantId);

    /**
     * 判断主卡号是否重复
     * @param  majorNumber  主卡号
     * @return 主卡信息
     */
    OilMajor majorNumber2(@Param("majorNumber") String majorNumber, @Param("tenantId") Integer tenantId);

    /**
     * 更新
     * @param  oilMajor  主卡信息
     * @return 结果
     */
    Integer updateOilMajor(OilMajor oilMajor);

    /**
     * 添加
     * @param  oilMajor  主卡信息
     * @return 结果
     */
    Integer insertOilMajor(OilMajor oilMajor);

    /**
     * 删除
     * @param  ids  主卡ids
     * @param updateBy 更新者
     * @param delFlag 删除标识
     * @return 结果
     */
    Integer deleteOilMajor(@Param("ids") String[] ids, @Param("updateBy") String updateBy, @Param("delFlag") String delFlag);
}
