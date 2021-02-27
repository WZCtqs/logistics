package com.zhkj.lc.order.mapper;

import com.zhkj.lc.order.model.entity.OrdCommonFile;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author cb
 * @since 2019-01-03
 */
public interface OrdCommonFileMapper extends BaseMapper<OrdCommonFile> {

    OrdCommonFile selectCommonOrdFileById(String orderId);
}
