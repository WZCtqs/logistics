package com.zhkj.lc.oilcard.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zhkj.lc.common.util.DateUtils;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.util.support.Convert;
import com.zhkj.lc.common.vo.DriverVO;
import com.zhkj.lc.common.vo.TruckVO;
import com.zhkj.lc.oilcard.feign.TruckFeign;
import com.zhkj.lc.oilcard.mapper.RechargeFreightMapper;
import com.zhkj.lc.oilcard.model.OilException;
import com.zhkj.lc.oilcard.model.OilTruckMonthRecharge;
import com.zhkj.lc.oilcard.model.RechargeFreight;
import com.zhkj.lc.oilcard.service.IOilTruckMonthRechargeService;
import com.zhkj.lc.oilcard.service.IRechargeFreightService;
import com.zhkj.lc.oilcard.util.GetIs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 运费油卡充值管理 服务实现类
 * </p>
 *
 * @author ckj
 * @since 2018-12-14
 */
@Service
public class RechargeFreightServiceImpl extends ServiceImpl<RechargeFreightMapper, RechargeFreight> implements IRechargeFreightService {

	@Autowired private RechargeFreightMapper rechargeFreightMapper;
	@Autowired private TruckFeign truckFeign;
	@Autowired private DictService dictService;
    @Autowired private IOilTruckMonthRechargeService iOilTruckMonthRechargeService;
	@Override
	public List<RechargeFreight> selectRechargeFreightListByIds(String ids) {
		return listDict(rechargeFreightMapper.selectRechargeFreightListByIds(Convert.toStrArray(ids)));
	}

	@Override
	public List<RechargeFreight> selectRechargeFreightList(RechargeFreight rechargeFreight) {

		/*String[] driverIds = dictService.getDriverIds(rechargeFreight.getDriverName());
		rechargeFreight.setDriverIds(driverIds);*/
		List<RechargeFreight> freightList = new ArrayList<>();
		Map<Boolean,  String[]> map = dictService.booleanTruckIds(rechargeFreight.getTruckId(), rechargeFreight.getAttribute());
		Boolean flag = true;
		for (Map.Entry<Boolean,  String[]> entry : map.entrySet()) {
			flag = entry.getKey();
			rechargeFreight.setTruckIds(entry.getValue());
		}
		if (flag) {
			freightList = listDict(rechargeFreightMapper.selectRechargeFreightList(rechargeFreight));
		}
		return freightList;
	}

	@Override
	public Page<RechargeFreight> selectRechargeFreightListPage(Query query, RechargeFreight rechargeFreight) {

		/*String[] driverIds = dictService.getDriverIds(rechargeFreight.getDriverName());
		rechargeFreight.setDriverIds(driverIds);*/
		List<RechargeFreight> list = new ArrayList<>();
		Map<Boolean,  String[]> map = dictService.booleanTruckIds(rechargeFreight.getTruckId(), rechargeFreight.getAttribute());
		Boolean flag = true;
		for (Map.Entry<Boolean,  String[]> entry : map.entrySet()) {
			flag = entry.getKey();
			rechargeFreight.setTruckIds(entry.getValue());
		}
		if (flag) {
			list = rechargeFreightMapper.selectRechargeFreightList(query, rechargeFreight);
		}
		if (null != list && list.size() > 0) {
			TruckVO truckVO;
			DriverVO driverVO;
			String isSuggestRecharge;
			for (int i = 0; i < list.size(); i++) {
				// 月充值订单量 * 10000 - 申请充值金额 - 月充值金额
				isSuggestRecharge = GetIs.getIsSuggestRecharge(list.get(i).getMonthOrderNum(), list.get(i).getRechargeSum(), list.get(i).getMonthRechargeSum());
				list.get(i).setIsSuggestRecharge(isSuggestRecharge);

				if (null != list.get(i).getTruckId()) {
					truckVO = truckFeign.getTruckByid(list.get(i).getTruckId());
					OilTruckMonthRecharge  oilTruckMonthRecharge = iOilTruckMonthRechargeService.selectByTruckIdWithYearMonth(list.get(i).getTruckId(),rechargeFreight.getTenantId(), DateUtils.getDateYM());
					if (null != truckVO) {
						list.get(i).setAttribute(truckVO.getAttribute());
						list.get(i).setTruckOwner(truckVO.getTruckOwner());
						list.get(i).setPlateNumber(truckVO.getPlateNumber());
						if(oilTruckMonthRecharge != null){
							list.get(i).setAmount(oilTruckMonthRecharge.getBalance());
						}

					}
				}
				if (null != list.get(i).getOwnerDriverId()) {
					driverVO = truckFeign.getDriverByid(list.get(i).getOwnerDriverId());
					if (null != driverVO) {
						list.get(i).setDriverPhone(driverVO.getPhone());
						list.get(i).setDriverName(driverVO.getDriverName());
					}
				}
			}
		}
		query.setRecords(list);
		return query;
	}

	private List<RechargeFreight> listDict(List<RechargeFreight> list){
		if (null != list && list.size()>0) {
			TruckVO truckVO;
			DriverVO driverVO;
			for (int i = 0; i < list.size(); i++) {

				list.get(i).setCardType(dictService.getLabel("oilcard_type", list.get(i).getCardType(), list.get(i).getTenantId()));

				list.get(i).setRechargeType("运费充值");
				// 月充值订单量 * 10000 - 申请充值金额 - 月充值金额
				String isSuggestRecharge = GetIs.getIsSuggestRecharge(list.get(i).getMonthOrderNum(), list.get(i).getRechargeSum(), list.get(i).getMonthRechargeSum());
				list.get(i).setIsSuggestRecharge(isSuggestRecharge);

				Map<String, String> map = GetIs.getIsPassAndRecharge(list.get(i).getIsPassed(), list.get(i).getRechargeDate());
				list.get(i).setIsPassed(map.get("isPassed"));
				list.get(i).setIsRechargeed(map.get("isRechargeed"));

				if (null != list.get(i).getTruckId()) {
					truckVO = truckFeign.getTruckByid(list.get(i).getTruckId());
					if (null != truckVO) {
						list.get(i).setAttribute(dictService.getLabel("truck_attribute", truckVO.getAttribute(), list.get(i).getTenantId()));
						list.get(i).setPlateNumber(truckVO.getPlateNumber());
						list.get(i).setTruckOwner(truckVO.getTruckOwner());
					}
				}
				if (null != list.get(i).getOwnerDriverId()) {
					driverVO = truckFeign.getDriverByid(list.get(i).getOwnerDriverId());
					if (null != driverVO) {
						list.get(i).setDriverName(driverVO.getDriverName());
						list.get(i).setDriverPhone(driverVO.getPhone());
					}
				}
			}
		}
		return list;
	}

}
