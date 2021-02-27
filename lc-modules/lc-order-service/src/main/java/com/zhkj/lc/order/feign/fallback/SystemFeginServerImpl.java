package com.zhkj.lc.order.feign.fallback;

import com.zhkj.lc.common.vo.SysDictVO;
import com.zhkj.lc.common.vo.TanentVo;
import com.zhkj.lc.order.dto.SysAnnouncementDto;
import com.zhkj.lc.order.dto.SysSmsTempVO;
import com.zhkj.lc.order.feign.SystemFeginServer;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @Auther: wzc
 * @Date: 2018/12/12 08:42
 * @Description:
 */
@Service
public class SystemFeginServerImpl implements SystemFeginServer {

    @Override
    public List<SysDictVO> findDictByType(String type, Integer tenantId) {
        return null;
    }

    @Override
    public SysDictVO selectDict(SysDictVO sysDictVO) {
        return null;
    }

    @Override
    public TanentVo selectTenant(Integer tenantId) {
        return null;
    }

    @Override
    public Boolean selectIsSend(Integer tenantId) {
        return null;
    }

    @Override
    public SysSmsTempVO selectSmsSetByTplId(Integer tenantId, Integer tpl_id) {
        return null;
    }


}
