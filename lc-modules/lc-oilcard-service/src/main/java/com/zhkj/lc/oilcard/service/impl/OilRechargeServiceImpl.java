package com.zhkj.lc.oilcard.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zhkj.lc.common.constant.CommonConstant;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.util.StringUtils;
import com.zhkj.lc.common.util.support.Convert;
import com.zhkj.lc.common.vo.*;
import com.zhkj.lc.oilcard.feign.DictFeign;
import com.zhkj.lc.oilcard.feign.OrderFeign;
import com.zhkj.lc.oilcard.feign.TruckFeign;
import com.zhkj.lc.oilcard.mapper.OilRechargeMapper;
import com.zhkj.lc.oilcard.model.OilRecharge;
import com.zhkj.lc.oilcard.model.OilRechargeTotal;
import com.zhkj.lc.oilcard.model.RechargeExternal;
import com.zhkj.lc.oilcard.model.RechargeSum;
import com.zhkj.lc.oilcard.service.IOilRechargeService;
import com.zhkj.lc.oilcard.util.GetIs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 油卡充值表 服务实现类
 * </p>
 *
 * @author ckj
 * @since 2018-12-14
 */
@Service
public class OilRechargeServiceImpl extends ServiceImpl<OilRechargeMapper, OilRecharge> implements IOilRechargeService {


    @Autowired
    private OilRechargeMapper oilRechargeMapper;
    @Autowired
    private OrderFeign orderFeign;
    @Autowired
    private DictService dictService;
    @Autowired
    private TruckFeign truckFeign;
    @Autowired
    private DictFeign dictFeign;


    @Override
    public OilRecharge selectRechargeById(Integer id) {
        OilRecharge oilRecharge = oilRechargeMapper.selectRechargeById(id);
        if (null != oilRecharge.getTruckId()) {
            TruckVO truckVO = truckFeign.getTruckByid(oilRecharge.getTruckId());
            if (null != truckVO) {
                oilRecharge.setAttribute(truckVO.getAttribute());
                oilRecharge.setPlateNumber(truckVO.getPlateNumber());
                oilRecharge.setTruckOwner(truckVO.getTruckOwner());
            }
        }
        if (null != oilRecharge.getOwnerDriverId()) {
            DriverVO driverVO = truckFeign.getDriverByid(oilRecharge.getOwnerDriverId());
            if (null != driverVO) {
                oilRecharge.setDriverPhone(driverVO.getPhone());
                oilRecharge.setDriverName(driverVO.getDriverName());
            }
        }
        return oilRecharge;
    }

    @Override
    public boolean insertOilRecharge(OilRecharge oilRecharge) {
        Integer result;
        if (oilRecharge.getRechargeType().equals("0") || oilRecharge.getRechargeType().equals("1")) {
            double monthSum = 0;
            OilRecharge oilRecharge2 = oilRechargeMapper.selectRechargeMonth(new Date(), oilRecharge.getOilCardId(), oilRecharge.getOwnerDriverId(), oilRecharge.getTenantId());
            if (null != oilRecharge2) {
                monthSum = Double.parseDouble(oilRecharge2.getMonthRechargeSum().toPlainString());
                if (null != oilRecharge2.getRechargeDate()) {
                    // 充值成功后月充值金额加值
                    monthSum = monthSum + Double.parseDouble(oilRecharge.getRechargeSum().toPlainString());
                }
            }

            // 月订单数
            Integer orderNum = orderFeign.selectCountByDriver(oilRecharge.getOwnerDriverId(), oilRecharge.getTenantId());
            oilRecharge.setMonthOrderNum(orderNum == null ? 0 : orderNum);
            oilRecharge.setMonthRechargeSum(new BigDecimal(Double.toString(monthSum)));
            result = oilRechargeMapper.insertOilRecharge(oilRecharge);
        } else {
            result = oilRechargeMapper.insertOilRecharge(oilRecharge);
        }
        return null != result && result == 1;
    }

    @Override
    public boolean updateOilRecharge(OilRecharge oilRecharge) {
        Integer result = oilRechargeMapper.updateOilRecharge(oilRecharge);
        return null != result && result == 1;
    }

    @Override
    public boolean deleteOilRechargeByIds(String updateBy, String ids) {
        Integer result = oilRechargeMapper.deleteOilRechargeByIds(CommonConstant.STATUS_DEL, updateBy, Convert.toStrArray(ids));
        return null != result && result >= 1;
    }

    @Override
    public OilRecharge selectRechargeMonth(Date timeMonth, Integer oilCardId, Integer ownerDriverId, Integer tenantId) {
        return oilRechargeMapper.selectRechargeMonth(timeMonth, oilCardId, ownerDriverId, tenantId);
    }

    @Override
    public List<OilRecharge> selectNoRechargeMonth(OilRecharge oilRecharge) {
        return oilRechargeMapper.selectNoRechargeMonth(oilRecharge);
    }

    @Override
    public Page<RechargeSum> selectRechargeByRechrageSort(Query query, String yearMonth, Integer tenantId) {
        query.setRecords(selectRechargeByRechrageSortList(yearMonth, tenantId));
        return query;
    }

    @Override
    public List<RechargeSum> selectRechargeByRechrageSort(String yearMonth, Integer tenantId) {
        return selectRechargeByRechrageSortList(yearMonth, tenantId);
    }

    public List<RechargeSum> selectRechargeByRechrageSortList(String yearMonth, Integer tenantId) {
        List<RechargeSum> list = new ArrayList<>();
        List<RechargeSum> list11;
        TruckVO truckVO = new TruckVO();
        truckVO.setTenantId(tenantId);
        List<SysDictVO> dictVOS = dictService.getType("truck_attribute", tenantId);
        List<TruckVO> listTruck;
        String[] ids;
        if (null != dictVOS && dictVOS.size() > 0) {
            for (SysDictVO dictVO : dictVOS) {
                truckVO.setAttribute(dictVO.getValue());
                listTruck = truckFeign.listTruck(truckVO);
                if (null != listTruck && listTruck.size() > 0) {
                    ids = new String[listTruck.size()];
                    for (int i = 0; i < listTruck.size(); i++) {
                        ids[i] = listTruck.get(i).getTruckId().toString();
                    }
                    list11 = oilRechargeMapper.selectRechargeByAttribute(yearMonth, tenantId, dictVO.getLabel(), ids);
                    list.addAll(list11);
                }
            }
        }
        List<RechargeSum> list1 = oilRechargeMapper.selectRechargeByOpenCardPlace(yearMonth, tenantId);
        list.addAll(list1);
        return list;
    }

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
    @Override
    public List<OilRecharge> selectRechargeListApplet(OilRecharge oilRecharge) {
        List<OilRecharge> list = oilRechargeMapper.selectRechargeListApplet(oilRecharge);
        if (null != list && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (null != list.get(i).getRechargeDate() && "1".equals(list.get(i).getIsPassed())) {
                    list.get(i).setIsPassed("已充值");
                } else if (null == list.get(i).getRechargeDate() && "1".equals(list.get(i).getIsPassed())) {
                    list.get(i).setIsPassed("审核通过待充值");
                } else if (null == list.get(i).getRechargeDate() && "0".equals(list.get(i).getIsPassed())) {
                    list.get(i).setIsPassed("待审核");
                } else if (null == list.get(i).getRechargeDate() && "2".equals(list.get(i).getIsPassed())) {
                    list.get(i).setIsPassed("审核未通过");
                }
            }
        }
        return list;
    }

    @Override
    public BigDecimal selectOilFeeByDriverLast7days(FeeVO feeVO) {
        return oilRechargeMapper.selectOilFeeByDriverLast7days(feeVO);
    }

    @Override
    public BigDecimal selectOilFeeByDriverMonthdays(FeeVO feeVO) {
        return oilRechargeMapper.selectOilFeeByDriverMonthdays(feeVO);
    }

    @Override
    public BigDecimal selectOilFeeByDriverCurrentSeason(FeeVO feeVO) {
        return oilRechargeMapper.selectOilFeeByDriverCurrentSeason(feeVO);
    }

    @Override
    public BigDecimal selectOilFeeByDriverLast6Months(FeeVO feeVO) {
        return oilRechargeMapper.selectOilFeeByDriverLast6Months(feeVO);
    }

    @Override
    public BigDecimal selectOilFeeByDriverSometime(FeeVO feeVO) {
        return oilRechargeMapper.selectOilFeeByDriverSometime(feeVO);
    }

    @Override
    public Page<OilRechargeTotal> selectOilCardRechargeTotal(Query query, OilRechargeTotal param) {
        List<OilRechargeTotal> rechargeTotals;
        if (param.getIsSelectSub() != null) {
            rechargeTotals = oilRechargeMapper.selectOilCardRechargeBymajorId(query, param);
        } else {
            rechargeTotals = oilRechargeMapper.selectOilCardRechargeTotal(query, param);
        }
        if (rechargeTotals != null && rechargeTotals.size() > 0) {
            List<SysDictVO> oilTypes = dictFeign.findDictByType("oilcard_type", param.getTenantId());
            List<SysDictVO> truckTypes = dictService.getType("truck_attribute", param.getTenantId());
            for (OilRechargeTotal oil : rechargeTotals) {
                /*处理油卡类型*/
                oil.setCardType(getLabel(oilTypes, oil.getCardType()));
                /*处理车辆信息*/
                if (null != oil.getTruckId()) {
                    TruckVO truckVO = truckFeign.getTruckByid(oil.getTruckId());
                    if (null != truckVO) {
                        oil.setAttribute(getLabel(truckTypes, truckVO.getAttribute()));
                        oil.setPlateNumber(truckVO.getPlateNumber());
                    }
                }
                /*处理司机信息*/
                if (null != oil.getDriverId()) {
                    DriverVO driverVO = truckFeign.getDriverByid(oil.getDriverId());
                    if (null != driverVO) {
                        oil.setDriverPhone(driverVO.getPhone());
                        oil.setDriverName(driverVO.getDriverName());
                    }
                }
            }
        }
        query.setRecords(rechargeTotals);
        return query;
    }

	@Override
	public Page<ReChargeVO> selectOilRechargeListByPage(Query query, ReChargeVO reChargeVO) {

		List<ReChargeVO> list = oilRechargeMapper.selectOilRechargeListByPage(query, reChargeVO);
		list = formatList(list);
		query.setRecords(list);
		return query;
	}

	@Override
	public List<ReChargeVO> selectOilRechargeListAll(ReChargeVO reChargeVO) {
		return formatList(oilRechargeMapper.selectOilRechargeListAll(reChargeVO));
	}


	@Override
	public List<ReChargeVO> selectReChargeByIds(String ids) {
		return formatList(oilRechargeMapper.selectReChargeByIds(Convert.toStrArray(ids)));
	}

	public String getLabel(List<SysDictVO> dicts, String value) {
        if (value == null || ("").equals(value)) {
            return null;
        }
        for (SysDictVO dict : dicts) {
            if (value.equals(dict.getValue())) {
                return dict.getLabel();
            }
        }
        return null;
    }

    public List<ReChargeVO> formatList(List<ReChargeVO> list){
		if (null != list && list.size() > 0) {
			TruckVO truckVO;
			DriverVO driverVO;
			for (int i = 0; i < list.size(); i++) {

				Map<String, String> map = GetIs.getMaps(list.get(i).getMonthOrderNum(), list.get(i).getRechargeSum(), list.get(i).getMonthRechargeSum(),list.get(i).getIsPassed(), list.get(i).getRechargeDate());
				list.get(i).setIsPassed(map.get("isPassed"));
				list.get(i).setIsRechargeed(map.get("isRechargeed"));
				list.get(i).setIsSuggestRecharge(map.get("isSuggestRecharge"));


				if (null != list.get(i).getOwnerDriverId()) {
					driverVO = truckFeign.getDriverByid(list.get(i).getOwnerDriverId());
					if (null != driverVO) {
						list.get(i).setDriverPhone(driverVO.getPhone());
						list.get(i).setDriverName(driverVO.getDriverName());
					}
				}

				if (null != list.get(i).getRechargeType()){
					switch (list.get(i).getRechargeType()) {
						case "0":
							list.get(i).setRechargeType("正常充值");
							break;
						case "1":
							list.get(i).setRechargeType("运费充值");
							break;
						case "2":
							list.get(i).setRechargeType("外调车充值");
							break;
						default:

					}
				}

				if (!StringUtils.isEmpty(list.get(i).getCardType())){
					switch (list.get(i).getCardType()) {
						case "0":
							list.get(i).setCardType("普卡");
							break;
						case "1":
							list.get(i).setCardType("专卡限制车号");
							break;
						case "2":
							list.get(i).setCardType("柴油专用卡");
							break;
					}
				}
				if (null != list.get(i).getTruckId()) {
					truckVO = truckFeign.getTruckByid(list.get(i).getTruckId());
					if (null != truckVO) {
						list.get(i).setAttribute(truckVO.getAttribute());
						list.get(i).setPlateNumber(truckVO.getPlateNumber());
						list.get(i).setTruckOwner(truckVO.getTruckOwner());
					}
				}

				if (!StringUtils.isEmpty(list.get(i).getAttribute())) {
					switch (list.get(i).getAttribute()) {
						case "0":
							list.get(i).setAttribute("自有车");
							break;
						case "1":
							list.get(i).setAttribute("挂靠车");
							break;
						case "2":
							list.get(i).setAttribute("外调车");
							break;
						default:
					}
				}

			}
		}

		return list;
	}
}
