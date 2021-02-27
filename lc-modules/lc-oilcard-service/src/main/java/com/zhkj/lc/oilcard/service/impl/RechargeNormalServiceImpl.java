package com.zhkj.lc.oilcard.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.util.support.Convert;
import com.zhkj.lc.common.vo.DriverVO;
import com.zhkj.lc.common.vo.TruckVO;
import com.zhkj.lc.oilcard.feign.TruckFeign;
import com.zhkj.lc.oilcard.mapper.RechargeNormalMapper;
import com.zhkj.lc.oilcard.model.RechargeNormal;
import com.zhkj.lc.oilcard.service.IRechargeNormalService;
import com.zhkj.lc.oilcard.util.GetIs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 正常油卡充值管理 服务实现类
 * </p>
 *
 * @author ckj
 * @since 2018-12-14
 */
@Service
public class RechargeNormalServiceImpl extends ServiceImpl<RechargeNormalMapper, RechargeNormal> implements IRechargeNormalService {

	@Autowired private RechargeNormalMapper rechargeNormalMapper;
	@Autowired private TruckFeign truckFeign;
	@Autowired private DictService dictService;

	@Override
	public List<RechargeNormal> selectRechargeNormalListByIds(String ids) {
		return listDict(rechargeNormalMapper.selectRechargeNormalListByIds(Convert.toStrArray(ids)));
	}

	@Override
	public List<RechargeNormal> selectRechargeNormalList(RechargeNormal rechargeNormal) {

		List<RechargeNormal> list = new ArrayList<>();
		Map<Boolean,  String[]> map = dictService.booleanTruckIds(rechargeNormal.getTruckId(), rechargeNormal.getAttribute());
		Boolean flag = true;
		for (Map.Entry<Boolean,  String[]> entry : map.entrySet()) {
			flag = entry.getKey();
			rechargeNormal.setTruckIds(entry.getValue());
		}
		if (flag) {
			list = listDict(rechargeNormalMapper.selectRechargeNormalList(rechargeNormal));
		}
		return list;
	}

	@Override
	public Page<RechargeNormal> selectRechargeNormalListPage(Query query, RechargeNormal rechargeNormal) {

		List<RechargeNormal> list = new ArrayList<>();
		Map<Boolean,  String[]> map = dictService.booleanTruckIds(rechargeNormal.getTruckId(), rechargeNormal.getAttribute());
		Boolean flag = true;
		for (Map.Entry<Boolean,  String[]> entry : map.entrySet()) {
			flag = entry.getKey();
			rechargeNormal.setTruckIds(entry.getValue());
		}
		if (flag) {
			list = rechargeNormalMapper.selectRechargeNormalList(query, rechargeNormal);
			if (null != list && list.size() > 0) {
				String isSuggestRecharge;
				TruckVO truckVO;
				DriverVO driverVO;
				for (int i = 0; i < list.size(); i++) {
					// 月充值订单量 * 10000 - 申请充值金额 - 月充值金额
					isSuggestRecharge = GetIs.getIsSuggestRecharge(list.get(i).getMonthOrderNum(), list.get(i).getRechargeSum(), list.get(i).getMonthRechargeSum());
					list.get(i).setIsSuggestRecharge(isSuggestRecharge);

					if (null != list.get(i).getTruckId()) {
						truckVO = truckFeign.getTruckByid(list.get(i).getTruckId());
						if (null != truckVO) {
							list.get(i).setAttribute(truckVO.getAttribute());
							list.get(i).setPlateNumber(truckVO.getPlateNumber());
							list.get(i).setTruckOwner(truckVO.getTruckOwner());
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
		}
		query.setRecords(list);
		return query;
	}

	@Override
	public List<RechargeNormal> selectRechargedNormalList(RechargeNormal rechargeNormal) {
		return rechargeNormalMapper.selectRechargedNormalList(rechargeNormal);
	}

	private List<RechargeNormal> listDict(List<RechargeNormal> list){
		if (null != list && list.size() > 0) {
			TruckVO truckVO;
			DriverVO driverVO;
			for (int i = 0; i < list.size(); i++) {

				list.get(i).setCardType(dictService.getLabel("oilcard_type", list.get(i).getCardType(), list.get(i).getTenantId()));

				list.get(i).setRechargeType("正常充值");
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
