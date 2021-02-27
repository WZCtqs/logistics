package com.zhkj.lc.oilcard.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.oilcard.model.OilCashAmount;

import java.util.List;

/**
 * @Description 加油现金余额 Service 接口
 * @Author ckj
 * @Date 2019/3/5 8:51
 */
public interface IOilCashAmountService extends IService<OilCashAmount> {

    /**
     * 根据id获取加油现金余额信息
     *
     * @param id
     * @return 加油现金余额
     */
    OilCashAmount selectOilCashAmount(Integer id);

    /**
     * 根据ids查询油卡现金余额
     *
     * @param ids
     * @return 加油现金余额集合
     */
    List<OilCashAmount> selectOilCashAmountListByIds(String ids);

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
    Page<OilCashAmount> selectOilCashAmountList(Query query, OilCashAmount oilCashAmount);

    /**
     * 加油充值公司重复
     *
     * @param tenantId 租户id
     * @param yearMonth 年月
     * @param company 公司名字
     * @return 加油现金余额
     */
    OilCashAmount oilCashAmount2(Integer tenantId, String yearMonth, String company);

    /**
     * 更新
     *
     * @param oilCashAmount 加油现金余额
     * @return 结果
     */
    boolean updateOilCashAmount(OilCashAmount oilCashAmount);

    /**
     * 添加
     *
     * @param oilCashAmount 加油现金余额
     * @return 结果
     */
    boolean insertOilCashAmount(OilCashAmount oilCashAmount);


    /**
     * 删除
     *
     * @param ids
     * @return 结果
     */
    boolean deleteOilCashAmount(String ids, String updateBy);
}
