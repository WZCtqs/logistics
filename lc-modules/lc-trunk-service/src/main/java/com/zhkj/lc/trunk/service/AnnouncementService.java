package com.zhkj.lc.trunk.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.trunk.model.Announcement;

import java.util.List;

/**
 * @Description 公告service层接口
 * @Author ckj
 * @Date 2019/1/3 16:04
 */
public interface AnnouncementService extends IService<Announcement> {

    /**
     * 获取个人和公共公告，时间降序排序前三个
     * @param driverOwerId 接受公告者id
     * @param tenantId 租户id
     * @return 公告集合
     */
    List<Announcement> selectTop3(Integer driverOwerId, Integer tenantId);

    /**
     * 分页获取公共或司机、车主公告
     *  @param announcement 公告信息
     *  @return 公告集合
     */
    List<Announcement> selectAllDriverOwer(Announcement announcement);

    /**
     * 获取公共或司机/车主或客服未查看公告数量
     *  @param driverOwerId 司机或者车主id（即登录用户对应的司机id）
     * @param tenantId 租户id
     *  @return 数量
     */
    Integer selectDriverOwerCount(Integer driverOwerId, Integer tenantId);

    /**
     * 获取公共或司机/车主或客服未查看公告id集合
     *  @param updateBy 更新者
     *  @param driverOwerId 司机或者车主id（即登录用户对应的司机id）
     *  @param announcementIds 公告id
     *  @param tenantId 租户id
     *  @return 结果
     */
    boolean updateCheckoutById(String updateBy, Integer driverOwerId, String announcementIds, Integer tenantId);

    /**
     * 将客服公告未查看转为已查看
     *
     * @param ids IDs
     * @param updateBy 更新者
     * @return 结果
     */
    boolean update2CheckedById(String ids, String updateBy);

    /**
     * 获取全部公告
     *  @param announcement 公共信息
     *  @return 公告集合
     */
    List<Announcement> selectAll(Announcement announcement);

    /**
     * 分页获取公告
     *  @param query 分页参数
     *  @param announcement 公告信息
     *  @return 公告集合
     */
    Page<Announcement> selectAll(Query query, Announcement announcement);

    /**
     * 添加公告信息
     *
     * @param announcement 公告信息
     * @return 结果
     */
    boolean insertAnnouncement(Announcement announcement);

    /**
     * 修改公告信息
     *
     * @param announcement 公告信息
     * @return 结果
     */
    boolean updateAnnouncementById(Announcement announcement);

    /**
     * 批量删除公告管理
     *
     * @param updateBy 删除者
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    boolean deleteByIds(String updateBy, String ids);


    List<Announcement> selectTruckOwnAnnouncement(Announcement announcement);

    Integer selectTruckOwnCount(Integer truckOwnId, Integer tenantId);
}
