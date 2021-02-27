package com.zhkj.lc.oilcard.service;


import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.oilcard.model.OilException;

import java.util.List;


/**
 * <p>
 * 油卡异常表 服务类
 * </p>
 *
 * @author wzc
 * @since 2018-12-07
 */
public interface IOilExceptionService extends IService<OilException> {

    /**
     * <p>
     * 根据ID查询
     * </p>
     * @param exceptionId 异常油卡id
     * @return 异常油卡信息
     */
    OilException selectByExceptionId(Integer exceptionId);

    /**
     * <p>
     * 根据IDS查询
     * </p>
     * @param ids 异常油卡id
     * @return 异常油卡集合
     */
    List<OilException> selectListByIds(String ids);
    /**
     * <p>
     * 翻页查询
     * </p>
     *
     * @return
     */
    List<OilException> selectPageList(OilException oilException);

    /**
     * <p>
     * 翻页查询
     * </p>
     *
     * @param query    翻页对象
     * @param oilException 实体包装类 {@link Wrapper}
     * @return
     */
    Page<OilException> selectPageList(Query query, OilException oilException);
    /**
     * 根据车主姓名查找
     */
    public OilException selectByOwner(String truckOwner);

    /**
     * 根据司机id查找
     */
    public OilException selectByDriver(Integer driverId);

    /**
     * 批量删除油卡异常
     *
     * @param exceptionIds 需要删除的数据ID
     * @return 结果
     */
    public Boolean deleteExceptionByIds(String exceptionIds);

}
