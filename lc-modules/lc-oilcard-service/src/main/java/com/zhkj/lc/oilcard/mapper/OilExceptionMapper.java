package com.zhkj.lc.oilcard.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.oilcard.model.OilException;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * <p>
 * 油卡异常表 Mapper 接口
 * </p>
 *
 * @author wzc
 * @since 2018-12-07
 */
public interface OilExceptionMapper extends BaseMapper<OilException> {

    /**
     * <p>
     * 根据ID查询
     * </p>
     * @param exceptionId 异常油卡id
     * @return 异常油卡信息
     */
    OilException selectByExceptionId(@Param("exceptionId") Integer exceptionId);

    /**
     * <p>
     * 根据IDS查询
     * </p>
     * @param ids 异常油卡id
     * @return 异常油卡集合
     */
    List<OilException> selectListByIds(@Param("ids") String[] ids);
    /**
     * <p>
     * 导出excel使用的查询语句
     * </p>
     *
     * @return List<T>
     */
    List<OilException> selectPageList(OilException oilException);

    /**
     * <p>
     * 根据 entity 条件，查询全部记录（并翻页）
     * </p>
     *
     * @param query 分页查询条件（可以为 RowBounds.DEFAULT）
     * @param oilException   实体对象封装操作类（可以为 null）
     * @return List<T>
     */
    List<OilException> selectPageList(Query query, OilException oilException);

    /**
     * 根据车主姓名查找
     */
    public OilException selectByOwner(String truckOwner);

    /**
     * 根据车主姓名查找
     */
    public OilException selectByDriver(Integer ownerDriverId);

    /**
     * 批量删除油卡异常
     *
     * @param exceptionIds 需要删除的数据ID
     * @return 结果
     */
    public Boolean deleteExceptionByIds(String[] exceptionIds);
}
