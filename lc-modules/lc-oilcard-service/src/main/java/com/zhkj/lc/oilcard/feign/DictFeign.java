package com.zhkj.lc.oilcard.feign;

import com.zhkj.lc.common.vo.SysDictVO;
import com.zhkj.lc.oilcard.feign.impl.DictFeignImpl;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "lc-upms-service",fallback = DictFeignImpl.class)
public interface DictFeign {

    @GetMapping("/dict/fegintype/{type}/{tenantId}")
    List<SysDictVO> findDictByType(@PathVariable("type") String type, @PathVariable("tenantId") Integer tenantId);

    @PostMapping("/dict/selectDictByValueAndType")
    SysDictVO selectDict(@RequestBody SysDictVO sysDict);
}
