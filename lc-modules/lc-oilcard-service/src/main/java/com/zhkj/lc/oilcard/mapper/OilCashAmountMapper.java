package com.zhkj.lc.oilcard.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.oilcard.model.OilCashAmount;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description 加油现金余额 Mapper 接口
 * @Author ckj
 * @Date 2019/3/5 8:51
 */
public interface OilCashAmountMapper extends BaseMapper<OilCashAmount> {

    /**
     * 根据id获取加油现金余额信息
     *
     * @param id
     * @return 加油现金余额
     */
    OilCashAmount selectOilCashAmount(@Param("id") Integer id);

    /**
     * 根据ids查询油卡现金余额
     *
     * @param ids
     * @return 加油现金余额集合
     */
    List<OilCashAmount> selectOilCashAmountListByIds(@Param("ids") String[] ids);

    /**
     * 根据条件查询油卡现金余额
     *
     * @param oilCashAmount
     * @return 加油现金余额集合
     */
    List<OilCashAmount> selectOilCashAmountList(OilCashAmount oilCashAmount);

    /**
     * 分页根据条件查询油卡现金余额
     *
     * @param query 分页参数
     * @param oilCashAmount
     * @return 加油现金余额集合
     */
    List<OilCashAmount> selectOilCashAmountList(Query query, OilCashAmount oilCashAmount);

    /**
     * 加油充值公司重复
     *
     * @param tenantId 租户id
     * @param yearMonth 年月
     * @param company 公司名字
     * @return 加油现金余额
     */
    OilCashAmount oilCashAmount2(@Param("tenantId") Integer tenantId, @Param("yearMonth") String yearMonth, @Param("company") String company);

    /**
     * 更新
     *
     * @param oilCashAmount 加油现金余额
     * @return 结果
     */
    Integer updateOilCashAmount(OilCashAmount oilCashAmount);

    /**
     * 添加
     *
     * @param oilCashAmount 加油现金余额
     * @return 结果
     */
    Integer insertOilCashAmount(OilCashAmount oilCashAmount);


    /**
     * 删除
     *
     * @param ids 加油ids
     * @param updateBy 更新者
     * @param delFlag 删除标识
     * @return 结果
     */
    Integer deleteOilCashAmount(@Param("ids") String[] ids, @Param("updateBy") String updateBy, @Param("delFlag") String delFlag);

}
