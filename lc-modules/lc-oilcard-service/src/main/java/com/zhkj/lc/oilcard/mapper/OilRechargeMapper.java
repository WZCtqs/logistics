package com.zhkj.lc.oilcard.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.vo.FeeVO;
import com.zhkj.lc.common.vo.ReChargeVO;
import com.zhkj.lc.oilcard.model.OilRecharge;
import com.zhkj.lc.oilcard.model.OilRechargeTotal;
import com.zhkj.lc.oilcard.model.RechargeSum;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 油卡充值表 Mapper 接口
 * </p>
 *
 * @author wzc
 * @since 2018-12-07
 */
public interface OilRechargeMapper extends BaseMapper<OilRecharge> {

    /**
     * 添加油卡充值信息
     * @param rechargeId 油卡充值id
     * @return
     */
    OilRecharge selectRechargeById(@Param("rechargeId") Integer rechargeId);

    /**
     * 添加油卡充值信息
     * @param oilRecharge 油卡充值信息
     * @return
     */
    Integer insertOilRecharge(OilRecharge oilRecharge);

    /**
     * 更新油卡信息
     *
     * @param oilRecharge 油卡信息
     * @return 结果
     */
    Integer updateOilRecharge(OilRecharge oilRecharge);

    /**
     * 批量删除油卡充值
     *
     * @param delFlag 删除标志
     * @param updateBy 操作者
     * @param rechargeIds 删除ids
     * @return 结果
     */
    Integer deleteOilRechargeByIds(@Param("delFlag") String delFlag, @Param("updateBy") String updateBy, @Param("rechargeIds") String[] rechargeIds);

    /**
     * 根据油卡id、司机id、租户id当月最近一次油卡充值记录
     *
     * @param timeMonth 年月
     * @param oilCardId 油卡id
     * @param ownerDriverId 司机id
     * @param tenantId 租户id
     * @return 油卡充值信息
     */
    OilRecharge selectRechargeMonth(@Param("timeMonth") Date timeMonth, @Param("oilCardId") Integer oilCardId, @Param("ownerDriverId") Integer ownerDriverId, @Param("tenantId") Integer tenantId);

    /**
     * 当月审核通过未进行充值的正常油卡充值记录
     *
     * @param oilRecharge 充值记录
     * @return 正常油卡充值信息
     */
    List<OilRecharge> selectNoRechargeMonth(OilRecharge oilRecharge);

    /**
     * 根据车辆类型(自由车、挂靠车、外调车)统计油卡充值汇总
     *
     * @param yearMonth 年月份
     * @param tenantId 租户
     * @param truckIds 车辆id
     * @return 正常油卡充值信息
     */
    List<RechargeSum> selectRechargeByAttribute(@Param("yearMonth") String yearMonth, @Param("tenantId")  Integer tenantId, @Param("attribute") String attribute, @Param("truckIds") String[] truckIds);

    /**
     * 根据办卡地点统计油卡充值汇总
     *
     * @param yearMonth 年月份
     * @param tenantId 租户
     * @return 正常油卡充值信息
     */
    List<RechargeSum> selectRechargeByOpenCardPlace(@Param("yearMonth") String yearMonth,@Param("tenantId") Integer tenantId);

    /**************************************************************************
     *  小程序端接口
     * @author ckj
     * @date 2018-12-28 15:45
     */

    /**
     * 根据司机id、租户id、油卡id获取充值信息
     *
     * @param oilRecharge
     * @return 油卡充值信息集合
     */
    List<OilRecharge> selectRechargeListApplet(OilRecharge oilRecharge);

    BigDecimal selectOilFeeByDriverLast7days(FeeVO feeVO);

    BigDecimal selectOilFeeByDriverMonthdays(FeeVO feeVO);

    BigDecimal selectOilFeeByDriverCurrentSeason(FeeVO feeVO);

    BigDecimal selectOilFeeByDriverLast6Months(FeeVO feeVO);

    BigDecimal selectOilFeeByDriverSometime(FeeVO feeVO);

    List<OilRechargeTotal> selectOilCardRechargeTotal(Query query, OilRechargeTotal param);

    List<OilRechargeTotal> selectOilCardRechargeBymajorId(Query query, OilRechargeTotal param);

    List<ReChargeVO> selectOilRechargeListByPage(Query query,ReChargeVO reChargeVO);

	List<ReChargeVO> selectOilRechargeListAll(ReChargeVO reChargeVO);

	List<ReChargeVO> selectReChargeByIds(@Param("ids")String[] ids);

    BigDecimal selectRechargeSumByTruckId(@Param("tenantId") Integer tenantId,@Param("truckId") Integer truckId);

}
