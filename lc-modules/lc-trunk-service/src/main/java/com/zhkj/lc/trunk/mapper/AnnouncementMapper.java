package com.zhkj.lc.trunk.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.trunk.model.Announcement;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description 公告mapper层
 * @Author ckj
 * @Date 2019/1/3 15:10
 */
public interface AnnouncementMapper extends BaseMapper<Announcement> {

    /**
     * 获取个人和公共公告，时间降序排序前三个
     * @param driverOwerId 接受公告者id
     * @return 公告集合
     */
    List<Announcement> selectTop3(@Param(value = "driverOwerId") Integer driverOwerId, @Param(value = "tenantId") Integer tenantId);

    /**
     * 分页获取公共或司机、车主公告
     *  @param announcement 公告信息
     *  @return 公告集合
     */
    List<Announcement> selectAllDriverOwer(Announcement announcement);

    /**
     * 获取公共或司机/车主或客服未查看公告数量
     *  @param driverOwerId 司机或者车主id（即登录用户对应的司机id）
     *  @return 数量
     */
    Integer selectDriverOwerCount(@Param(value = "driverOwerId") Integer driverOwerId, @Param(value = "tenantId") Integer tenantId);

    /**
     * 获取公共或司机/车主或客服未查看公告id集合
     *  @param driverOwerId 司机或者车主id（即登录用户对应的司机id）
     *  @return 公告信息id集合
     */
    List<String> selectDriverOwerCheckout(@Param(value = "driverOwerId") Integer driverOwerId, @Param(value = "tenantId") Integer tenantId);

    /**
     * 获取公共或司机/车主或客服未查看公告id集合
     *  @param updateBy 更新者
     *  @param driverOwerId 司机或者车主id（即登录用户对应的司机id）
     *  @param announcementIds 公告id
     *  @return 结果
     */
    Integer updateCheckoutById(@Param("updateBy") String updateBy, @Param("driverOwerId") Integer driverOwerId, @Param("announcementIds") String[] announcementIds);

    /**
     * 获取全部公告
     *  @param announcement 公共信息
     *  @return 公告集合
     */
    List<Announcement> selectAllLimit20(Announcement announcement);

    /**
     * 将客服公告未查看转为已查看
     * @param announcementIds IDs
     * @param updateBy 更新者
     * @return 结果
     */
    Integer update2CheckedById(@Param("announcementIds") String[] announcementIds, @Param("updateBy") String updateBy);

    /**
     * 获取全部公告
     *  @param announcement 公共信息
     *  @return 公告集合
     */
    List<Announcement> selectAll(Query query, Announcement announcement);

    /**
     * 添加公告信息
     *
     * @param announcement 公告信息
     * @return 结果
     */
    Integer insertAnnouncement(Announcement announcement);

    /**
     * 修改公告信息
     *
     * @param announcement 公告信息
     * @return 结果
     */
    Integer updateAnnouncementById(Announcement announcement);

    /**
     * 批量删除公告管理
     *
     * @param delFlag 删除标志
     * @param updateBy 删除者
     * @param announcementIds 需要删除的数据ID
     * @return 结果
     */
    Integer deleteByIds(@Param("delFlag") String delFlag, @Param("updateBy") String updateBy, @Param("announcementIds") String[] announcementIds);


    List<Announcement> selectTruckOwnAnnouncement(Announcement announcement);

    Integer selectTruckOwnCount( @Param(value = "truckOwnId")Integer truckOwnId,  @Param(value = "tenantId")Integer tenantId);
}
