package com.zhkj.lc.trunk.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zhkj.lc.trunk.model.FreightRoute;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 司机常跑路线 Mapper 接口
 * </p>
 *
 * @author wzc
 * @since 2019-01-03
 */
public interface FreightRouteMapper extends BaseMapper<FreightRoute> {
    public List<FreightRoute> selectByDriverId(@Param("driverId") int driverId);
}
