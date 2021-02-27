package com.zhkj.lc.trunk.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zhkj.lc.common.constant.CommonConstant;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.util.support.Convert;
import com.zhkj.lc.common.vo.SysDictVO;
import com.zhkj.lc.trunk.feign.DictFeginServer;
import com.zhkj.lc.trunk.mapper.AnnouncementMapper;
import com.zhkj.lc.trunk.model.Announcement;
import com.zhkj.lc.trunk.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description 公告service层
 * @Author ckj
 * @Date 2019/1/3 16:04
 */
@Service
public class AnnouncementServiceImpl extends ServiceImpl<AnnouncementMapper, Announcement> implements AnnouncementService {

    @Autowired
    private AnnouncementMapper announcementMapper;
    @Autowired
    private DictFeginServer dictFeginServer;

    @Override
    public List<Announcement> selectTop3(Integer driverOwerId, Integer tenantId) {
        List<Announcement>  list = announcementMapper.selectTop3(driverOwerId,tenantId);
        if (null != list && list.size() > 0){
            for (int i = 0; i < list.size(); i++){
                list.get(i).setType(getLable(list.get(i).getType(),"announcement_type", tenantId));
                list.get(i).setCheckout(getLable(list.get(i).getCheckout(),"checkout", tenantId));
            }
        }
        return list;
    }

    @Override
    public List<Announcement> selectAllDriverOwer(Announcement announcement){
        List<Announcement>  list = announcementMapper.selectAllDriverOwer(announcement);
        if (null != list && list.size() > 0){
            for (int i = 0; i < list.size(); i++){
                list.get(i).setType(getLable(list.get(i).getType(),"announcement_type", announcement.getTenantId()));
                list.get(i).setCheckout(getLable(list.get(i).getCheckout(),"checkout", announcement.getTenantId()));
            }
        }
        return list;
    }

    @Override
    public Integer selectDriverOwerCount(Integer driverOwerId, Integer tenantId) {
        return announcementMapper.selectDriverOwerCount(driverOwerId, tenantId);
    }

    @Override
    public boolean updateCheckoutById(String updateBy, Integer driverOwerId, String announcementIds, Integer tenantId) {

        if (null != announcementIds && "all".equals(announcementIds)){
            List<String> list = announcementMapper.selectDriverOwerCheckout(driverOwerId,tenantId);
            announcementIds = String.join(",", list);
        }
        Integer result = announcementMapper.updateCheckoutById(updateBy,driverOwerId,Convert.toStrArray(announcementIds));
        return null != result && result >=1;
    }

    @Override
    public List<Announcement> selectAll(Announcement announcement) {
        List<Announcement> list = announcementMapper.selectAllLimit20(announcement);
        if (null != list && list.size() > 0){
            for (int i = 0; i < list.size(); i++){
                list.get(i).setType(getLable(list.get(i).getType(),"announcement_type", announcement.getTenantId()));
                list.get(i).setCheckout(getLable(list.get(i).getCheckout(),"checkout", announcement.getTenantId()));
            }
        }
        return list;
    }

    @Override
    public boolean update2CheckedById(String ids, String updateBy){
        Integer result = announcementMapper.update2CheckedById(Convert.toStrArray(ids), updateBy);
        return null != result && result >=1;
    }

    @Override
    public Page<Announcement> selectAll(Query query, Announcement announcement) {
        List<Announcement>  list = announcementMapper.selectAll(query, announcement);
        if (null != list && list.size() > 0){
            for (int i = 0; i < list.size(); i++){
                list.get(i).setType(getLable(list.get(i).getType(),"announcement_type", announcement.getTenantId()));
                list.get(i).setCheckout(getLable(list.get(i).getCheckout(),"checkout", announcement.getTenantId()));
            }
        }
        query.setRecords(list);
        return query;
    }

    @Override
    public boolean insertAnnouncement(Announcement announcement){
        Integer result = announcementMapper.insertAnnouncement(announcement);
        return null != result && result == 1;
    }

    @Override
    public boolean updateAnnouncementById(Announcement announcement){
        Integer result = announcementMapper.updateAnnouncementById(announcement);
        return null != result && result == 1;
    }

    @Override
    public boolean deleteByIds(String updateBy, String ids) {
        Integer result = announcementMapper.deleteByIds(CommonConstant.STATUS_DEL, updateBy, Convert.toStrArray(ids));
        return null != result && result >= 1;
    }

    @Override
    public List<Announcement> selectTruckOwnAnnouncement(Announcement announcement) {
        return announcementMapper.selectTruckOwnAnnouncement(announcement);
    }

    @Override
    public Integer selectTruckOwnCount(Integer truckOwnId, Integer tenantId) {
        return announcementMapper.selectTruckOwnCount(truckOwnId,tenantId);
    }

    private String getLable(String value, String type, Integer tenantId){
        if (null != value && !value.equals("") && null != dictFeginServer.findDictByTypeForFegin(type,tenantId)) {
            SysDictVO sysDictVO = new SysDictVO();
            sysDictVO.setType(type);
            sysDictVO.setValue(value);
            sysDictVO.setTenantId(tenantId);
            if (null != type && "checkout".equals(type)){
                if ("0".equals(value)){
                    return "已查看";
                }else {
                    return "未查看";
                }
            }
            return dictFeginServer.selectDict(sysDictVO).getLabel();
        }
        return value;
    }
}
