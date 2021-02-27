package com.zhkj.lc.oilcard.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.oilcard.model.PointAmount;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description 公司积分余额 Mapper 接口
 * @Author ckj
 * @Date 2019/3/5 20:49
 */
public interface PointAmountMapper extends BaseMapper<PointAmount> {

    /**
     * 根据id获取公司积分余额信息
     *
     * @param id
     * @return 公司积分余额
     */
    PointAmount selectPointAmount(@Param("id") Integer id);

    /**
     * 根据ids查询公司积分余额
     *
     * @param ids
     * @return 公司积分余额集合
     */
    List<PointAmount> selectPointAmountListByIds(@Param("ids") String[] ids);

    /**
     * 根据条件查询油卡现金余额
     *
     * @param pointAmount
     * @return 公司积分余额集合
     */
    List<PointAmount> selectPointAmountList(PointAmount pointAmount);

    /**
     * 分页根据条件查询油卡现金余额
     *
     * @param query 分页参数
     * @param pointAmount
     * @return 公司积分余额集合
     */
    List<PointAmount> selectPointAmountList(Query query, PointAmount pointAmount);

    /**
     * 加油充值公司重复
     *
     * @param tenantId 租户id
     * @param yearMonth 年月
     * @param company 公司名字
     * @return 公司积分余额
     */
    PointAmount pointAmount2(@Param("tenantId") Integer tenantId, @Param("yearMonth") String yearMonth, @Param("company") String company);

    /**
     * 更新
     *
     * @param pointAmount 公司积分余额
     * @return 结果
     */
    Integer updatePointAmount(PointAmount pointAmount);

    /**
     * 添加
     *
     * @param pointAmount 公司积分余额
     * @return 结果
     */
    Integer insertPointAmount(PointAmount pointAmount);


    /**
     * 删除
     *
     * @param ids 加油ids
     * @param updateBy 更新者
     * @param delFlag 删除标识
     * @return 结果
     */
    Integer deletePointAmount(@Param("ids") String[] ids, @Param("updateBy") String updateBy, @Param("delFlag") String delFlag);

}
