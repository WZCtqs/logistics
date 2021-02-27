package com.zhkj.lc.oilcard.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.util.support.Convert;
import com.zhkj.lc.common.vo.DriverVO;
import com.zhkj.lc.common.vo.TruckVO;
import com.zhkj.lc.oilcard.feign.TruckFeign;
import com.zhkj.lc.oilcard.mapper.RechargeExternalMapper;
import com.zhkj.lc.oilcard.model.RechargeExternal;
import com.zhkj.lc.oilcard.service.IRechargeExternalService;
import com.zhkj.lc.oilcard.util.GetIs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 外调车油卡充值管理 服务实现类
 * </p>
 *
 * @author ckj
 * @since 2018-12-14
 */
@Service
public class RechargeExternalServiceImpl extends ServiceImpl<RechargeExternalMapper, RechargeExternal> implements IRechargeExternalService {

	@Autowired private RechargeExternalMapper rechargeExternalMapper;
	@Autowired private TruckFeign truckFeign;
	@Autowired private DictService dictService;

	@Override
	public List<RechargeExternal> selectRechargeExternalListByIds(String ids) {
		return listDict(rechargeExternalMapper.selectRechargeExternalListByIds(Convert.toStrArray(ids)));
	}

	@Override
	public List<RechargeExternal> selectRechargeExternalList(RechargeExternal rechargeExternal) {

		/*String[] driverIds = dictService.getDriverIds(rechargeExternal.getDriverName());
		rechargeExternal.setDriverIds(driverIds);*/
		return listDict(rechargeExternalMapper.selectRechargeExternalList(rechargeExternal));
	}

	@Override
	public Page<RechargeExternal> selectRechargeExternalListPage(Query query, RechargeExternal rechargeExternal) {

		/*String[] driverIds = dictService.getDriverIds(rechargeExternal.getDriverName());
		rechargeExternal.setDriverIds(driverIds);*/

		List<RechargeExternal> list = rechargeExternalMapper.selectRechargeExternalList(query, rechargeExternal);
		if (null != list && list.size() > 0) {
			TruckVO truckVO;
			DriverVO driverVO;
			for (int i = 0; i < list.size(); i++) {
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
		query.setRecords(list);
		return query;
	}

	private List<RechargeExternal> listDict(List<RechargeExternal> list) {
		if (null != list && list.size()>0) {
			TruckVO truckVO;
			DriverVO driverVO;
			for (int i = 0; i < list.size(); i++) {
				list.get(i).setRechargeType("外调车充值");

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
						list.get(i).setDriverPhone(driverVO.getPhone());
						list.get(i).setDriverName(driverVO.getDriverName());
					}
				}
			}
		}
		return list;
	}

}
