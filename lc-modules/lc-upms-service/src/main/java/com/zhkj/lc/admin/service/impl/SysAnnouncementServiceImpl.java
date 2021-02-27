package com.zhkj.lc.admin.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zhkj.lc.admin.mapper.SysAnnouncementMapper;
import com.zhkj.lc.admin.model.entity.SysAnnouncement;
import com.zhkj.lc.admin.service.SysAnnouncementService;
import com.zhkj.lc.common.constant.CommonConstant;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.util.support.Convert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description 公告service层
 * @Author ckj
 * @Date 2019/1/3 16:04
 */
@Service
public class SysAnnouncementServiceImpl extends ServiceImpl<SysAnnouncementMapper, SysAnnouncement> implements SysAnnouncementService {

    @Autowired
    private SysAnnouncementMapper sysAnnouncementMapper;

    @Override
    public List<SysAnnouncement> selectTop3(Integer driverOwerId) {
        return sysAnnouncementMapper.selectTop3(driverOwerId);
    }

    @Override
    public List<SysAnnouncement> selectAll(SysAnnouncement sysAnnouncement) {
        return sysAnnouncementMapper.selectAll(sysAnnouncement);
    }

    @Override
    public Page<SysAnnouncement> selectAll(Query query, SysAnnouncement sysAnnouncement) {
        List<SysAnnouncement>  list = sysAnnouncementMapper.selectAll(query, sysAnnouncement);
        query.setRecords(list);
        return query;
    }

    @Override
    public boolean updateById(SysAnnouncement sysAnnouncement){
        Integer result = sysAnnouncementMapper.updateById(sysAnnouncement);
        return null != result && result == 1;
    }

    @Override
    public boolean deleteByIds(String updateBy, String ids) {
        Integer result = sysAnnouncementMapper.deleteByIds(CommonConstant.STATUS_DEL, updateBy, Convert.toStrArray(ids));
        return null != result && result >= 1;
    }
}
