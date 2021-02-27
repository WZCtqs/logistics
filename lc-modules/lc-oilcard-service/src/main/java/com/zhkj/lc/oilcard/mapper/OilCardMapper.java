package com.zhkj.lc.oilcard.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.vo.OilCardExportVO;
import com.zhkj.lc.common.vo.OilCardVO;
import com.zhkj.lc.oilcard.model.OilCard;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 油卡管理 Mapper 接口
 * </p>
 *
 * @author wzc
 * @since 2018-12-07
 */
public interface OilCardMapper extends BaseMapper<OilCard> {

    /**
     * 查询油卡管理信息
     *
     * @param oilCardId 油卡管理ID
     * @return 油卡管理信息
     */
    OilCard selectByOilCardId(@Param("oilCardId") Integer oilCardId);

    /**
     * 查询油卡管理信息
     *
     * @param oilCardNumber 油卡号
     * @param tenantId 租户id
     * @return 油卡管理信息
     */
    OilCard selectCardByOilCardNumber(@Param("oilCardNumber") String oilCardNumber, @Param("tenantId") Integer tenantId);

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
    List<OilCard> selectOilCardNumber(@Param("tenantId") Integer tenantId);

    /**
     * 查询办卡地点
     *
     * @param tenantId 租户id
     * @return 油卡管理信息
     */
    List<String> selectOpenCardPlace(@Param("tenantId") Integer tenantId);

    /**
     * 根剧主油卡Id查询副油卡号集合
     * @param majorId
     * @return
     */
    List<String> selectOilCardNumByMajorId(@Param("majorId")Integer majorId);

    /**
     * 根据车辆ids和租户id获取正常油卡数量
     *
     * @param driverIds 油卡信息
     * @param tenantId 租户id
     * @return 油卡数量
     */
    Integer countOilCard(@Param("driverIds") Integer[] driverIds, @Param("tenantId") Integer tenantId);

    /**
     * 导出部分油卡信息
     *
     * @param ids 油卡ids
     * @return 油卡管理信息
     */
    List<OilCard> selectCardListByIds(@Param("ids") String[] ids);

    List<OilCardExportVO> selectCardVOListByIds(@Param("ids") String[] ids);

    List<OilCardExportVO> selectCardVOList(OilCard oilCard);

    /**
     * 查询油卡管理列表
     *
     * @param card 油卡管理信息
     * @return 油卡管理集合
     */
    List<OilCard> selectCardList(OilCard card);

    /**
     * 分页查询油卡管理列表
     *
     * @param query 分页参数
     * @param card 油卡管理信息
     * @return 油卡管理集合
     */
    List<OilCard> selectCardList(Query query, OilCard card);

    /**
     * 修改油卡管理
     *
     * @param card 油卡管理信息
     * @return 结果
     */
    Integer updateOilCard(OilCard card);

    /**
     * 批量删除油卡管理
     *
     * @param delFlag 删除标志
     * @param updateBy 删除者
     * @param oilCardIds 需要删除的数据ID
     * @return 结果
     */
    Integer deleteCardByIds(@Param("delFlag") String delFlag, @Param("updateBy") String updateBy, @Param("oilCardIds") String[] oilCardIds);

    /**
     * 新增油卡基础信息
     *
     * @param oilCard 油卡基础信息
     * @return 结果
     */
    Integer insertOilCard(OilCard oilCard);

    /**
     * 小程序根据油卡申请人查询全部油卡信息
     *
     * @param ownerDriverId 油卡号
     * @param tenantId 租户id
     * @return 油卡信息集合
     */
    List<OilCard> selectAllCardByOwnerId(@Param("ownerDriverId") Integer ownerDriverId, @Param("tenantId") Integer tenantId);

    /**
     * 小程序根据油卡申请人查询正常油卡信息
     *
     * @param ownerDriverId 油卡号
     * @param tenantId 租户id
     * @return 油卡信息集合
     */
    List<OilCard> selectCardByOwnerId(@Param("ownerDriverId") Integer ownerDriverId, @Param("tenantId") Integer tenantId);

    Integer driversCardNum(@Param("driverId") Integer driverId, @Param("tenantId") Integer tenantId);

    List<OilCard> findAllCardsByPlateNum(@Param("plateNumber") String plateNumber,@Param("tenantId") Integer tenantId);


    List<OilCardVO> countCardNumByTruckId(@Param("plateNumber") String plateNumber, @Param("tenantId") Integer tenantId);

    List<OilCard> selectAllOilCardNumber(@Param("tenantId") Integer tenantId);


    List<OilCard>  findAllCardsByMajorId(@Param("majorId") Integer majorId, @Param("tenantId") Integer tenantId);
}
