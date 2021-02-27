package com.zhkj.lc.admin.controller;

import com.zhkj.lc.admin.model.dto.SmsDTO;
import com.zhkj.lc.admin.model.entity.SysSmsTemp;
import com.zhkj.lc.admin.service.SysSmsTempService;
import com.zhkj.lc.common.util.UserUtils;
import com.zhkj.lc.common.web.BaseController;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Auther: HP
 * @Date: 2019/2/21 08:16
 * @Description:
 */
@RestController
@RequestMapping("/sysSmsTemp")
public class SysSmsTempController extends BaseController {

    @Autowired
    SysSmsTempService sysSmsTempService;

    @ApiOperation(value = "查询短信模板接口")
    @GetMapping("tplList")
    public List<SysSmsTemp>tplList(){
        /*获取模板信息数据*/
//        String result = YunPianSMSUtils.getTplList();
        /*json转化成对象集合*/
//        SysSmsTemp[] array = new Gson().fromJson(result, SysSmsTemp[].class);
//        if(array!=null){
//            List<SysSmsTemp> list = Arrays.asList(array);
//            return list;
//        }
        return sysSmsTempService.selectSmsList(getTenantId());
    }

    @ApiOperation(value = "修改短信发送设置")
    @Transactional
    @PutMapping
    public boolean updateSms(@RequestBody SmsDTO smsDTO){
        smsDTO.setTenantId(getTenantId());
        smsDTO.setUpdateBy(UserUtils.getUser());
        return sysSmsTempService.edit(smsDTO);
    }

    @ApiOperation(value = "短信模板新增接口")
    @Transactional
    @PostMapping
    public boolean insert(@RequestBody SysSmsTemp sysSmsTemp){
        /*设置为系统数据*/
        sysSmsTemp.setSys("0");
        return sysSmsTempService.insert(sysSmsTemp);
    }

    @GetMapping("/{tenantId}")
    public Boolean selectIsSend(@PathVariable("tenantId") Integer tenantId){
        return sysSmsTempService.selectIsSend(tenantId);
    }

    @GetMapping("/{tenantId}/{tpl_id}")
    public SysSmsTemp selectSmsSetByTplId(@PathVariable("tenantId") Integer tenantId,@PathVariable("tpl_id") Integer tpl_id){
        return sysSmsTempService.selectSmsSetByTplId(tenantId, tpl_id);
    }
}
