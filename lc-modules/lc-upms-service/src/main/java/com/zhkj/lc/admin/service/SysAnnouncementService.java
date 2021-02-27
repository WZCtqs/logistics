package com.zhkj.lc.admin.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.zhkj.lc.admin.model.entity.SysAnnouncement;
import com.zhkj.lc.common.util.Query;

import java.util.List;

/**
 * @Description 公告service层接口
 * @Author ckj
 * @Date 2019/1/3 16:04
 */
public interface SysAnnouncementService extends IService<SysAnnouncement> {

    /**
     * 获取个人和公共公告，时间降序排序前三个
     * @param driverOwerId 接受公告者id
     * @return 公告集合
     */
    public List<SysAnnouncement> selectTop3(Integer driverOwerId);

    /**
     * 获取全部公告
     *  @param sysAnnouncement 公共信息
     *  @return 公告集合
     */
    public List<SysAnnouncement> selectAll(SysAnnouncement sysAnnouncement);

    /**
     * 分页获取公告
     *  @param query 分页参数
     *  @param sysAnnouncement 公告信息
     *  @return 公告集合
     */
    public Page<SysAnnouncement> selectAll(Query query, SysAnnouncement sysAnnouncement);

    /**
     * 修改公告信息
     *
     * @param sysAnnouncement 公告信息
     * @return 结果
     */
    public boolean updateById(SysAnnouncement sysAnnouncement);

    /**
     * 批量删除公告管理
     *
     * @param updateBy 删除者
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public boolean deleteByIds(String updateBy, String ids);
}
