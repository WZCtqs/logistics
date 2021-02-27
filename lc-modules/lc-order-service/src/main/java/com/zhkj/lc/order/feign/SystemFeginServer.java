package com.zhkj.lc.order.feign;

import com.zhkj.lc.common.vo.SysDictVO;
import com.zhkj.lc.common.vo.TanentVo;
import com.zhkj.lc.order.dto.SysSmsTempVO;
import com.zhkj.lc.order.dto.SysAnnouncementDto;
import com.zhkj.lc.order.feign.fallback.SystemFeginServerImpl;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Auther: wzc
 * @Date: 2018/12/12 08:41
 * @Description:
 */
@FeignClient(value = "lc-upms-service",fallback = SystemFeginServerImpl.class)
public interface SystemFeginServer {

    @GetMapping("dict/fegintype/{type}/{tenantId}")
    List<SysDictVO> findDictByType(@PathVariable("type") String type, @PathVariable("tenantId") Integer tenantId);

    @PostMapping("dict/selectDictByValueAndType")
    SysDictVO selectDict(@RequestBody SysDictVO sysDictVO);

    @GetMapping("sysTanent/{tenantId}")
    TanentVo selectTenant(@PathVariable("tenantId") Integer tenantId);

    @GetMapping("sysSmsTemp/{tenantId}")
    Boolean selectIsSend(@PathVariable("tenantId") Integer tenantId);

    @GetMapping("sysSmsTemp/{tenantId}/{tpl_id}")
    SysSmsTempVO selectSmsSetByTplId(@PathVariable("tenantId") Integer tenantId,@PathVariable("tpl_id") Integer tpl_id);


}
