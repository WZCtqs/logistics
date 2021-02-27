package com.zhkj.lc.trunk.feign.fallback;

import com.zhkj.lc.common.util.R;
import com.zhkj.lc.common.vo.SysDictVO;
import com.zhkj.lc.trunk.feign.DictFeginServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description
 * @Author ckj
 * @Date 2019/1/12 17:01
 */
@Service
public class DictFeginServerImpl implements DictFeginServer {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public List<SysDictVO> findDictByType(String type, Integer tenantId) {
        logger.info("根据字典类型获取字典集合失败！");
        return null;
    }

    @Override
    public List<SysDictVO> findDictByTypeForFegin(String type, Integer tenantId) {
        logger.info("根据字典类型获取字典集合失败！");
        return null;
    }

    @Override
    public List<SysDictVO> wechatSelectDictByType(String type,Integer tenantId) {
        logger.info("根据字典类型获取字典集合失败！");
        return null;
    }

    @Override
    public SysDictVO selectDict(SysDictVO sysDictVO) {
        logger.info("根据字典获取字典信息失败！");
        return null;
    }

    @Override
    public R<Boolean> editDict(SysDictVO sysDictVO){
        logger.info("字典信息编辑失败！");
        return null;
    }

    @Override
    public SysDictVO dict(Integer id){
        logger.info("根据字典获取字典信息失败");
        return null;
    }
}
