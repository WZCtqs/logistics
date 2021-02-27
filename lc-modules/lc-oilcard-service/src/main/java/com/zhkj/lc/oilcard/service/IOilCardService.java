package com.zhkj.lc.oilcard.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.vo.OilCardExportVO;
import com.zhkj.lc.oilcard.model.OilCard;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 油卡管理 服务类
 * </p>
 *
 * @author wzc
 * @since 2018-12-07
 */
public interface IOilCardService extends IService<OilCard> {
    /**
     * 查询油卡管理信息
     *
     * @param oilCardId 油卡管理ID
     * @return 油卡管理信息
     */
    OilCard selectByOilCardId(Integer oilCardId);

    /**
     * 查询油卡管理信息
     *
     * @param oilCardNumber 油卡号
     * @param tenantId 租户id
     * @return 油卡管理信息
     */
    OilCard selectCardByOilCardNumber(String oilCardNumber, Integer tenantId);

    /**
     * 查询油卡管理信息
     *
     * @param oilCardNumber 油卡号
     * @param tenantId 租户id
     * @return 油卡管理信息
     */
    OilCard selectOilCardByOilCardNumber(@Param("oilCardNumber") String oilCardNumber, @Param("tenantId") Integer tenantId);

    /**
     * 查询油卡号
     *
     * @param tenantId 租户id
     * @return 油卡管理信息
     */
    List<OilCard> selectOilCardNumber(Integer tenantId);

    /**
     * 查询办卡地点
     *
     * @param tenantId 租户id
     * @return 油卡管理信息
     */
    List<String> selectOpenCardPlace(Integer tenantId);

    /**
     * 根剧主油卡Id查询副油卡号集合
     * @param MajorId
     * @return
     */
    List<String> selectOilCardNumByMajorId(Integer MajorId);

    /**
     * 根据车辆ids获取正常油卡数量
     *
     * @param driverIds 车辆ids
     * @return 油卡数量
     */
    Integer countOilCard(Integer[] driverIds, Integer tenantId);

    /**
     * 导出部分油卡信息
     *
     * @param ids 油卡ids
     * @return 油卡管理信息
     */
    List<OilCard> selectCardListByIds(String ids);

    /**
     * 查询油卡管理列表
     *
     * @param card 油卡管理信息
     * @return 油卡管理集合
     */
    List<OilCard> selectCardList(OilCard card);

    /**
     * 查询油卡管理列表
     *
     * @param query 分页用到的信息
     * @param card 油卡管理信息
     * @return 油卡管理集合
     */
    Page<OilCard> selectCardListPage(Query query, OilCard card);

    /**
     * 修改油卡管理
     *
     * @param card 油卡管理信息
     * @return 结果
     */
    boolean updateOilCard(OilCard card);

    /**
     * 批量删除油卡管理
     *
     * @param ids 需要删除的id
     * @param updateBy 删除者
     * @return 结果
     */
    boolean deleteCardByIds(String ids, String updateBy);

    /**
     * 新增油卡基础信息
     *
     * @param oilCard 油卡基础信息
     * @return 结果
     */
    boolean insertOilCard(OilCard oilCard);

    /**
     * 小程序根据油卡申请人查询全部油卡信息
     *
     * @param ownerDriverId 油卡号
     * @param tenantId 租户id
     * @return 油卡信息集合
     */
    List<OilCard> selectAllCardByOwnerId(Integer ownerDriverId,Integer tenantId);

    /**
     * 小程序根据油卡申请人查询正常油卡信息
     *
     * @param ownerDriverId 油卡号
     * @param tenantId 租户id
     * @return 油卡信息集合
     */
    List<OilCard> selectCardByOwnerId(Integer ownerDriverId, Integer tenantId);

    Integer driversCardNum(Integer driverId, Integer tenantId);


    List<OilCardExportVO> selectCardVOListByIds(String ids);

    List<OilCardExportVO> selectCardVOList(OilCard oilCard);

    List<OilCard> findAllCardsByPlateNum(String plateNumber,Integer tenantId);


    List<OilCard> selectAllOilCardNumber(Integer tenantId);

    List<OilCard> findAllCardsByMajorId(Integer majorId, Integer tenantId);
}
