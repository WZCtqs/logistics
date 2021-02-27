package com.zhkj.lc.trunk.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zhkj.lc.trunk.model.TruTruckfile;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 车辆文件表 Mapper 接口
 * </p>
 *
 * @author wzc
 * @since 2018-12-07
 */
public interface TruTruckfileMapper extends BaseMapper<TruTruckfile> {
    TruTruckfile selectTruckfileById(@Param("truckFileId") int truckFileId);

    boolean insertTruckFile(TruTruckfile truTruckfile);
}
