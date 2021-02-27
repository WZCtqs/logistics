package com.zhkj.lc.trunk.feign;

import com.zhkj.lc.common.util.R;
import com.zhkj.lc.common.vo.SysDictVO;
import com.zhkj.lc.trunk.feign.fallback.DictFeginServerImpl;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 * @author ckj
 * @date 2019-01-12 16:03
 */
@FeignClient(value = "lc-upms-service",fallback = DictFeginServerImpl.class)
public interface DictFeginServer {

    @GetMapping("/dict/fegintype/{type}/{tenantId}")
    List<SysDictVO> findDictByType(@PathVariable("type") String type, @PathVariable("tenantId") Integer tenantId);

    @GetMapping("/dict/fegintype/{type}/{tenantId}")
    List<SysDictVO> findDictByTypeForFegin(@PathVariable("type") String type, @PathVariable("tenantId") Integer tenantId);

    @GetMapping("/dict/wechat/type")
    List<SysDictVO> wechatSelectDictByType(@RequestParam("type") String type,@RequestParam("tenantId") Integer tenantId);

    @PostMapping("/dict/selectDictByValueAndType")
    SysDictVO selectDict(@RequestBody SysDictVO sysDictVO);

    @PutMapping("/dict")
    R<Boolean> editDict(@RequestBody SysDictVO sysDictVO);

    @GetMapping("/dict/{id}")
    SysDictVO dict(@PathVariable("id") Integer id);
}
