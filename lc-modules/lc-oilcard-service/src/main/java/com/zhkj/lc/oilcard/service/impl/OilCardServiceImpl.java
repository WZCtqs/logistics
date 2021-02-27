package com.zhkj.lc.oilcard.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zhkj.lc.common.constant.CommonConstant;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.util.support.Convert;
import com.zhkj.lc.common.vo.DriverVO;
import com.zhkj.lc.common.vo.OilCardExportVO;
import com.zhkj.lc.common.vo.TruckVO;
import com.zhkj.lc.oilcard.feign.TruckFeign;
import com.zhkj.lc.oilcard.mapper.OilApplyMapper;
import com.zhkj.lc.oilcard.mapper.OilCardMapper;
import com.zhkj.lc.oilcard.mapper.OilMajorMapper;
import com.zhkj.lc.oilcard.model.OilApply;
import com.zhkj.lc.oilcard.model.OilCard;
import com.zhkj.lc.oilcard.model.OilMajor;
import com.zhkj.lc.oilcard.service.IOilCardService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 油卡管理 服务实现类
 * </p>
 *
 * @author wzc
 * @since 2018-12-07
 */
@Service
public class OilCardServiceImpl extends ServiceImpl<OilCardMapper, OilCard> implements IOilCardService {

    @Autowired private OilCardMapper oilCardMapper;
    @Autowired private TruckFeign truckFeign;
    @Autowired private DictService dictService;
    @Autowired private OilMajorMapper oilMajorMapper;
    @Autowired private OilApplyMapper oilApplyMapper;

    @Override
    public OilCard selectByOilCardId(Integer oilCardId) {
        OilCard oilCard = oilCardMapper.selectByOilCardId(oilCardId);
        if (null != oilCard) {
            if (null != oilCard.getTruckId()) {
                TruckVO truckVO = truckFeign.getTruckByid(oilCard.getTruckId());
                if (null != truckVO) {
                    oilCard.setAttribute(truckVO.getAttribute());
                    oilCard.setPlateNumber(truckVO.getPlateNumber());
                    oilCard.setTruckOwner(truckVO.getTruckOwner());
                    oilCard.setTruckOwnerPhone(truckVO.getTruckOwnerPhone());
                }
            }
            if (null != oilCard.getOwnerDriverId()) {
                DriverVO driverVO = truckFeign.getDriverByid(oilCard.getOwnerDriverId());
                if (null != driverVO) {
                    oilCard.setDriverPhone(driverVO.getPhone());
                    oilCard.setDriverName(driverVO.getDriverName());
                }
            }
        }
        return oilCard;
    }

    @Override
    public OilCard selectCardByOilCardNumber(String oilCardNumber, Integer tenantId) {
        return oilCardMapper.selectCardByOilCardNumber(oilCardNumber, tenantId);
    }

	@Override
	public OilCard selectOilCardByOilCardNumber(String oilCardNumber, Integer tenantId) {
		return oilCardMapper.selectOilCardByOilCardNumber(oilCardNumber,tenantId);
	}


	@Override
    public List<OilCard> selectOilCardNumber(Integer tenantId) {
        return oilCardMapper.selectOilCardNumber(tenantId);
    }

    @Override
    public List<String> selectOpenCardPlace(Integer tenantId) {
        return oilCardMapper.selectOpenCardPlace(tenantId);
    }

    @Override
    public List<String> selectOilCardNumByMajorId(Integer MajorId){
        return oilCardMapper.selectOilCardNumByMajorId(MajorId);
    }

    @Override
    public Integer countOilCard(Integer[] driverIds, Integer tenantId) {
        return oilCardMapper.countOilCard(driverIds, tenantId);
    }

    @Override
    public List<OilCard> selectCardListByIds(String ids){
        List<OilCard> list = oilCardMapper.selectCardListByIds(Convert.toStrArray(ids));
        return listDict(list);
    }

    @Override
    public List<OilCard> selectCardList(OilCard card) {

        List<OilCard> listOilCard = new ArrayList<>();
        Map<Boolean,  String[]> map = dictService.booleanTruckIds(card.getTruckId(), card.getAttribute());
        Boolean flag = true;
        for (Map.Entry<Boolean,  String[]> entry : map.entrySet()) {
            flag = entry.getKey();
            card.setTruckIds(entry.getValue());
        }
        if (flag) {
            listOilCard = listDict(oilCardMapper.selectCardList(card));
        }
        return listOilCard;
    }

    @Override
    public Page selectCardListPage(Query query,OilCard card) {

        List<OilCard> listOilCard = new ArrayList<>();
        Map<Boolean,  String[]> map = dictService.booleanTruckIds(card.getTruckId(), card.getAttribute());
        Boolean flag = true;
        for (Map.Entry<Boolean,  String[]> entry : map.entrySet()) {
            flag = entry.getKey();
            card.setTruckIds(entry.getValue());
        }
        if (flag) {
            listOilCard = oilCardMapper.selectCardList(query, card);
            TruckVO truckVO;
            DriverVO driverVO;
            for (int i = 0; i < listOilCard.size(); i++) {
                if (null != listOilCard.get(i).getTruckId()) {
                    truckVO = truckFeign.getTruckByid(listOilCard.get(i).getTruckId());
                    if (null != truckVO) {
                        listOilCard.get(i).setAttribute(truckVO.getAttribute());
                        listOilCard.get(i).setPlateNumber(truckVO.getPlateNumber());
                        listOilCard.get(i).setTruckOwner(truckVO.getTruckOwner());
                        listOilCard.get(i).setTruckOwnerPhone(truckVO.getTruckOwnerPhone());
                    }
                }
                if (null != listOilCard.get(i).getOwnerDriverId()) {
                    driverVO = truckFeign.getDriverByid(listOilCard.get(i).getOwnerDriverId());
                    if (null != driverVO) {
                        listOilCard.get(i).setDriverPhone(driverVO.getPhone());
                        listOilCard.get(i).setDriverName(driverVO.getDriverName());
                    }
                }
            }
        }
        query.setRecords(listOilCard);
        return query;
    }

    @Override
    public boolean updateOilCard(OilCard card) {
        Integer result = oilCardMapper.updateOilCard(card);
        return null != result && result == 1;
    }

    /**
     * 只在退卡后才可以删除
     *
     * @return 结果
     */
    @Override
    @Transactional
    public boolean deleteCardByIds(String ids, String updateBy) {
        String[] idArray = Convert.toStrArray(ids);
        /* Integer result = 0;
        OilCard oilCard;
        for(int i = 0, j = 1; i < idArray.length; i++, j++){
            oilCard = oilCardMapper.selectByOilCardId(Integer.valueOf(idArray[i]));
            result = result + oilCardMapper.deleteCardByIds(CommonConstant.STATUS_DEL, updateBy, new String[]{idArray[i]});
            if (result == j) {
                OilMajor oilMajor = oilMajorMapper.selectById(oilCard.getMajorId());
                oilMajor.setCardNum(oilMajor.getCardNum() - 1);
                oilMajorMapper.updateOilMajor(oilMajor);
            }else {
                j--;
            }
        }*/
        String[] applyIdArray = new String[idArray.length];
        for (int i = 0; i < idArray.length; i ++){
            //System.out.println("ffff"+oilCardMapper.selectByOilCardId(Integer.valueOf(idArray[i])));
            if(oilCardMapper.selectByOilCardId(Integer.valueOf(idArray[i])).getApplyId() != null){
                applyIdArray[i] = String.valueOf(oilCardMapper.selectByOilCardId(Integer.valueOf(idArray[i])).getApplyId());
            }

        }
        Integer result = oilCardMapper.deleteCardByIds(CommonConstant.STATUS_DEL, updateBy, idArray);
        oilApplyMapper.deleteApplyByIds(CommonConstant.STATUS_DEL, updateBy, applyIdArray);

        return null != result && result >= 1;
    }

    @Transactional
    @Override
    public boolean insertOilCard(OilCard oilCard) {
        Integer result = oilCardMapper.insertOilCard(oilCard);
        if (null != result && result == 1){
            OilMajor oilMajor = oilMajorMapper.selectById(oilCard.getMajorId());
            if (null != oilMajor) {
                oilMajor.setCardNum(oilMajor.getCardNum() + 1);
                oilMajorMapper.updateOilMajor(oilMajor);
            }
        }
        return  null != result && result == 1;
    }

    private List<OilCard> listDict(List<OilCard> list){
        if (null != list && list.size()>0) {
            TruckVO truckVO;
            DriverVO driverVO;
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setCardType(dictService.getLabel("oilcard_type", list.get(i).getCardType(), list.get(i).getTenantId()));
                if (null != list.get(i).getTruckId()) {
                    truckVO = truckFeign.getTruckByid(list.get(i).getTruckId());
                    if (null != truckVO) {
                        list.get(i).setPlateNumber(truckVO.getPlateNumber());
                        list.get(i).setTruckOwner(truckVO.getTruckOwner());
                        list.get(i).setTruckOwnerPhone(truckVO.getTruckOwnerPhone());
                        list.get(i).setAttribute(dictService.getLabel("truck_attribute", truckVO.getAttribute(), list.get(i).getTenantId()));
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

    /**
     * 小程序根据油卡申请人查询全部油卡信息
     */
    public List<OilCard> selectAllCardByOwnerId(Integer ownerDriverId, Integer tenantId){
        List<OilCard> list = oilCardMapper.selectAllCardByOwnerId(ownerDriverId, tenantId);
        if (null != list && list.size()>0) {
            String status;
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setCardType(dictService.getLabel("oilcard_type", list.get(i).getCardType(), tenantId));
//                list.get(i).setAttribute(dictService.getLabel("truck_attribute", list.get(i).getAttribute()));
                status = list.get(i).getCardStatus();
                if ("挂失".equals(status)){
                    list.get(i).setCardStatus("挂失中");
                } else if ("解锁".equals(status)){
                    list.get(i).setCardStatus("解锁中");
                } else if ("消磁".equals(status)){
                    list.get(i).setCardStatus("消磁中");
                }
            }
        }else {
            return null;
        }
        return list;
    }

    /**
     * 小程序根据油卡申请人查询正常油卡信息
     */
    public List<OilCard> selectCardByOwnerId(Integer ownerDriverId, Integer tenantId){
        return oilCardMapper.selectCardByOwnerId(ownerDriverId, tenantId);
    }

    @Override
    public Integer driversCardNum(Integer driverId, Integer tenantId) {
        return oilCardMapper.driversCardNum(driverId, tenantId);
    }

	@Override
	public List<OilCardExportVO> selectCardVOListByIds(String ids) {
		return oilCardMapper.selectCardVOListByIds(Convert.toStrArray(ids));
	}

	@Override
	public List<OilCardExportVO> selectCardVOList(OilCard oilCard) {
		return oilCardMapper.selectCardVOList(oilCard);
	}

    @Override
    public List<OilCard> findAllCardsByPlateNum(String plateNumber, Integer tenantId) {

        return oilCardMapper.findAllCardsByPlateNum(plateNumber,tenantId);
    }

    @Override
    public List<OilCard> selectAllOilCardNumber(Integer tenantId) {
        return oilCardMapper.selectAllOilCardNumber(tenantId);
    }

    @Override
    public List<OilCard> findAllCardsByMajorId(Integer majorId, Integer tenantId) {

        return oilCardMapper.findAllCardsByMajorId(majorId,tenantId);
    }
}
