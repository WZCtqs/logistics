package com.zhkj.lc.trunk.service;

import com.baomidou.mybatisplus.service.IService;
import com.zhkj.lc.trunk.model.TruTruckfile;

/**
 * <p>
 * 车辆文件表 服务类
 * </p>
 *
 * @author wzc
 * @since 2018-12-07
 */
public interface ITruTruckfileService extends IService<TruTruckfile> {
    boolean insertTruckFile(TruTruckfile truTruckfile);
}
