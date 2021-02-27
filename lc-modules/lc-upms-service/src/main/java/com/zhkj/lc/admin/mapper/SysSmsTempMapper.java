package com.zhkj.lc.admin.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zhkj.lc.admin.model.entity.SysDept;
import com.zhkj.lc.admin.model.entity.SysSmsTemp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 部门管理 Mapper 接口
 * </p>
 *
 * @author lengleng
 * @since 2018-01-20
 */
public interface SysSmsTempMapper extends BaseMapper<SysSmsTemp> {

    List<SysSmsTemp> selectSmsList(Integer tenantId);

    List<SysSmsTemp> selectForTenant();

    boolean update(SysSmsTemp sysSmsTemp);

    Integer selectIsSend(Integer tenantId);

    SysSmsTemp selectSmsSetByTplId(@Param("tenantId")Integer tenantId, @Param("tpl_id")Integer tpl_id);
}