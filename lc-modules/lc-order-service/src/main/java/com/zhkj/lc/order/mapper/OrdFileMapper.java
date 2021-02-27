package com.zhkj.lc.order.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zhkj.lc.order.model.entity.OrdFile;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author cb
 * @since 2019-01-24
 */
public interface OrdFileMapper extends BaseMapper<OrdFile> {

    List<OrdFile> selectOrdFiles(@Param("orderId")String orderId);
}
