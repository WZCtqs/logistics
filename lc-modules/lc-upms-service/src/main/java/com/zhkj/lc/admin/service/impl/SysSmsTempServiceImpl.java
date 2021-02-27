package com.zhkj.lc.admin.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zhkj.lc.admin.mapper.SysSmsTempMapper;
import com.zhkj.lc.admin.model.dto.SmsDTO;
import com.zhkj.lc.admin.model.entity.SysSmsTemp;
import com.zhkj.lc.admin.service.SysSmsTempService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 部门管理 服务实现类
 * </p>
 *
 * @author lengleng
 * @since 2018-01-20
 */
@Service
public class SysSmsTempServiceImpl extends ServiceImpl<SysSmsTempMapper, SysSmsTemp> implements SysSmsTempService {

    @Autowired
    private SysSmsTempMapper smsTempMapper;


    @Override
    public List<SysSmsTemp> selectSmsList(Integer tenantId) {
        List<SysSmsTemp> smsTemps = smsTempMapper.selectSmsList(tenantId);
        for(SysSmsTemp sms:smsTemps){
            sms.setIsDriver(sms.getIsSendDriver().equals("0"));
            sms.setIsReceice(sms.getIsSendReceice().equals("0"));
            sms.setIsPicker(sms.getIsSendPicker().equals("0"));
        };
        return smsTemps;
    }

    @Override
    public List<SysSmsTemp> selectForTenant() {
        return smsTempMapper.selectForTenant();
    }

    @Override
    public boolean edit(SmsDTO smsDTO) {
        /*是否发送*/
        String isSend = smsDTO.getIsSend();
        /*登陆者*/
        String updateBy = smsDTO.getUpdateBy();

        List<SysSmsTemp>smsTemps = smsDTO.getTempList();
        for(SysSmsTemp sms : smsTemps){
            sms.setIsSend(isSend);
            /*如果不发送，所有发送选项都是不发送*/
            if(isSend.equals("1")){
                sms.setIsSendDriver("1");
                sms.setIsSendReceice("1");
                sms.setIsSendPicker("1");
            }else{
                sms.setIsSendDriver(sms.getIsDriver()?"0":"1");
                sms.setIsSendReceice(sms.getIsReceice()?"0":"1");
                sms.setIsSendPicker(sms.getIsPicker()?"0":"1");
            }
            sms.setTenantId(smsDTO.getTenantId());
            sms.setUpdateTime(new Date());
            sms.setUpdateBy(updateBy);
            smsTempMapper.update(sms);
        }
        return true;
    }

    @Override
    public boolean selectIsSend(Integer tenantId) {
        Integer i = smsTempMapper.selectIsSend(tenantId);
        if(i == 0){
            return true;
        }else
            return false;
    }

    @Override
    public SysSmsTemp selectSmsSetByTplId(Integer tenantId, Integer tpl_id){
        return smsTempMapper.selectSmsSetByTplId(tenantId, tpl_id);
    }
}
