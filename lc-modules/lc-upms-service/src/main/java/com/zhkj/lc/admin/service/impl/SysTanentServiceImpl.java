package com.zhkj.lc.admin.service.impl;


import com.baomidou.mybatisplus.plugins.Page;
import com.zhkj.lc.admin.mapper.SysTanentMapper;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zhkj.lc.admin.model.entity.SysTanent;
import com.zhkj.lc.admin.service.SysTanentService;
import com.zhkj.lc.common.util.DateUtils;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.util.StringUtils;
import com.zhkj.lc.common.vo.TanentVo;
import com.zhkj.lc.common.vo.TruckVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * <p>
 * 系统租户信息表 服务实现类
 * </p>
 *
 * @author cb
 * @since 2018-12-13
 */
@Service
public class SysTanentServiceImpl extends ServiceImpl<SysTanentMapper, SysTanent> implements SysTanentService {

    @Autowired
    private SysTanentMapper sysTanentMapper;
    @Override
    public void insertTanentInfo(SysTanent sysTanent) {
        sysTanentMapper.insertTanentInfo(sysTanent);
    }

    @Override
    public Page selectTanentVoPage(Query query, TanentVo tanentVo) {

        List<TanentVo> tanentVos = sysTanentMapper.selectTanentVoPage(query,tanentVo);
        //循环判断当前租户的账号是否一周之内过期
        for (TanentVo tvo: tanentVos) {
            if(DateUtils.getDayDiffFromToday(tvo.getExpireTime())){
                tvo.setIsExpried(0);
            }
            else {
                tvo.setIsExpried(1);
            }
        }
        query.setRecords(tanentVos);
        return query;
    }


    @Override
    public SysTanent selectTanentInfo(Integer tenantId) {
        SysTanent sysTanent = sysTanentMapper.selectBytenantId(tenantId);
        if(DateUtils.getDayDiffFromToday(sysTanent.getExpireTime())){
            sysTanent.setIsExpried(0);
        }else {
            sysTanent.setIsExpried(1);
        }
        return sysTanent;
    }

    @Override
    public List<TanentVo> selectTanentList(){
        return sysTanentMapper.selectTanentVoPage();
    }

    @Override
    public TanentVo selectByWA(String weixinId){
        return sysTanentMapper.selectByWA(weixinId);
    }

    @Override
    public Boolean findPhone(String phone) {
        if(StringUtils.isEmpty(phone)){
            return true;
        }
        List<TanentVo> tanentVoList = sysTanentMapper.findPhone(phone);
        if(tanentVoList.size()==0){
          return true;
        }else {
            return false;
        }
    }

    @Override
    public Boolean fingByPhone(String phone, Integer tenantId) {
        List<TanentVo> tanentVoList = sysTanentMapper.findPhone(phone);
        if(tanentVoList != null && (tanentVoList.get(0).getTenantId() != tenantId)){
            return false;
        }
        return true;
    }

    @Override
    public String selectTenantShortName(Integer tenantId) {
        return sysTanentMapper.selectTenantShortName(tenantId);
    }

}
