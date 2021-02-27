package com.zhkj.lc.oilcard.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.oilcard.model.PointAmount;

import java.util.List;

/**
 * @Description 公司积分余额 Service 接口
 * @Author ckj
 * @Date 2019/3/5 21:08
 */
public interface IPointAmountService extends IService<PointAmount> {

    /**
     * 根据id获取公司积分余额信息
     *
     * @param id
     * @return 公司积分余额
     */
    PointAmount selectPointAmount(Integer id);

    /**
     * 根据ids查询公司积分余额
     *
     * @param ids
     * @return 公司积分余额集合
     */
    List<PointAmount> selectPointAmountListByIds(String ids);

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
    Page<PointAmount> selectPointAmountList(Query query, PointAmount pointAmount);

    /**
     * 加油充值公司重复
     *
     * @param tenantId 租户id
     * @param yearMonth 年月
     * @param company 公司名字
     * @return 公司积分余额
     */
    PointAmount pointAmount2(Integer tenantId, String yearMonth, String company);

    /**
     * 更新
     *
     * @param pointAmount 公司积分余额
     * @return 结果
     */
    boolean updatePointAmount(PointAmount pointAmount);

    /**
     * 添加
     *
     * @param pointAmount 公司积分余额
     * @return 结果
     */
    boolean insertPointAmount(PointAmount pointAmount);


    /**
     * 删除
     *
     * @param ids
     * @return 结果
     */
    boolean deletePointAmount(String ids, String updateBy);
}
