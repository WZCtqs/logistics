package com.zhkj.lc.trunk.feign;

import com.zhkj.lc.common.util.R;
import com.zhkj.lc.common.vo.TanentVo;
import com.zhkj.lc.trunk.feign.fallback.TanentFeginServerImpl;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(value = "lc-upms-service",fallback = TanentFeginServerImpl.class)
public interface TanentFegin {

    @PostMapping("/sysTanent/selectTanentList")
    List<TanentVo> selectTanentList();

    @GetMapping("/sysTanent/{tenantId}")
    R<TanentVo> get(@PathVariable("tenantId") Integer tenantId);

    @PostMapping("/sysTanent/selectByWA/{weixinId}")
    TanentVo selectByWA(@PathVariable("weixinId") String weixinId);
}
