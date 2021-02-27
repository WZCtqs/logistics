package com.zhkj.lc.trunk.feign.fallback;

import com.zhkj.lc.common.util.R;
import com.zhkj.lc.common.vo.TanentVo;
import com.zhkj.lc.trunk.feign.TanentFegin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
public class TanentFeginServerImpl implements TanentFegin {
    Logger logger = LoggerFactory.getLogger(TanentFeginServerImpl.class);
    @Override
    public List<TanentVo> selectTanentList(){
        logger.info("租户信息获取失败");
        return null;
    }

    @Override
    public R<TanentVo> get(@PathVariable Integer tenantId){
        logger.info("租户信息获取失败");
        return null;
    }

    @Override
    public TanentVo selectByWA(String weixinId){
        logger.info("租户信息获取失败");
        return null;
    }
}
