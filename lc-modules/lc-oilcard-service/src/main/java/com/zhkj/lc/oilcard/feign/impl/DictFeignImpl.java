package com.zhkj.lc.oilcard.feign.impl;

import com.zhkj.lc.common.vo.SysDictVO;
import com.zhkj.lc.oilcard.feign.DictFeign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class DictFeignImpl implements DictFeign {

    private Logger logger = LoggerFactory.getLogger(SysDictVO.class);

    @Override
    public List<SysDictVO> findDictByType(String type, Integer tenantId) {
        logger.info("根据type获取字典信息失败！");
        return null;
    }

    //
    @Override
    public SysDictVO selectDict(SysDictVO sysDict) {
        logger.info("根据type、value或者type、label获取字典信息失败！");
        return null;
    }
}
