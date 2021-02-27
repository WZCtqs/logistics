package com.zhkj.lc.admin.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zhkj.lc.admin.model.entity.SysTanent;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.vo.TanentVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 系统租户信息表 Mapper 接口
 * </p>
 *
 * @author cb
 * @since 2018-12-13
 */
@Repository
public interface SysTanentMapper extends BaseMapper<SysTanent> {

    public void insertTanentInfo(SysTanent sysTanent);

    public List<TanentVo> selectTanentVoPage(Query query, TanentVo tanentVo);

    SysTanent selectBytenantId(@Param("tenantId") Integer tenantId);

    List<TanentVo> selectTanentVoPage();

    TanentVo selectByWA(@Param("weixinId") String weixinId);

    List<TanentVo> findPhone(String phone);

    String selectTenantShortName(@Param("tenantId") Integer tenantId);
}
