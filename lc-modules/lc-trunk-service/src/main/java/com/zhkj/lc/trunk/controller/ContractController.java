package com.zhkj.lc.trunk.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.zhkj.lc.common.constant.CommonConstant;
import com.zhkj.lc.common.util.*;
import com.zhkj.lc.common.util.excel.ExcelUtil;
import com.zhkj.lc.common.util.support.Convert;
import com.zhkj.lc.common.web.BaseController;
import com.zhkj.lc.trunk.model.Contract;
import com.zhkj.lc.trunk.service.IContractService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 合同管理 前端控制器
 * </p>
 *
 * @author wzc
 * @since 2018-12-07
 */
@Api(description = "合同管理接口")
@RestController
@RequestMapping("/contract")
public class ContractController extends BaseController {
    @Autowired private IContractService contractService;

    /**
     * 根据合同状态查询所有合同
     * @return
     */
    @ApiOperation(value = "查询所有未过期合同信息")
    @GetMapping("/selectAllUnexpiredContract")
    public List<Contract> selectAllUnexpiredContract(Contract contract){
        if(null == contract.getTenantId()){
            contract.setTenantId(getTenantId());
        }
        return contractService.selectAllUnexpiredContract(contract);
    }
    /**
     * 批量删除
     * @param contractNumbers
     * @return success/false
     */
    @ApiOperation(value = "删除或者批量删除油卡申请信息")
    @ApiImplicitParam(name = "contractNumbers",value = "合同编号",paramType = "path",required = true,example = "1,2")
    @DeleteMapping("/delectIds/{contractNumbers}")
    public R<Boolean> deleteIds(@PathVariable String contractNumbers) {
        return new R<>(contractService.deleteByIds(contractNumbers));
    }

    /**
     * 通过合同编号查询
     * @Param String contractNumber
     * @return Contract
     */
    @ApiOperation(value = "查询合同信息", notes = "根据合同编号查询合同信息")
    @ApiImplicitParam(name = "contractNumber", value = "合同编号", required = true, dataType = "String", paramType = "path")
    @GetMapping("/{contractNumber}")
    public Contract get(@PathVariable String contractNumber) {
        Contract contract = new Contract();
       //contract.setTenantId(getTenantId());
        contract.setContractNumber(contractNumber);
        return contractService.selectByContractNumber(contract);
    }


    /**
     * 搜索信息
     *
     * @param params 分页对象
     * @return 分页对象
     */
    @ApiOperation(value = "搜索分页显示合同信息",notes = "params参数不写默认获取第一页10条数据；contract参数不写即为获取全部信息:")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "params",value = "分页参数：page页,size页数据量",paramType = "query",dataType = "Map"),
            @ApiImplicitParam(name = "contract",value = "搜索条件,字段可选:contractNumber,contractName,contractType,beginTime,endTime(时间的范围)等",paramType = "query",dataType = "Contract")
    })
    @PostMapping("/pageList")
    public Page pageList(@RequestParam Map<String, Object> params,Contract contract) {
        params.put(CommonConstant.DEL_FLAG, CommonConstant.STATUS_NORMAL);
        if(null == contract.getTenantId()){
            contract.setTenantId(getTenantId());
        }
        return contractService.selectPageList(new Query<>(params), contract);
    }

    /**
     * 添加
     * @param  contract  实体
     * @return success/false
     */
    @ApiOperation(value = "添加")
    @ApiImplicitParam(name = "contract",value = "合同信息类",paramType = "body",dataType = "Contract",required = true)
    @PostMapping
    public R<Boolean> add(@RequestBody Contract contract) {
        if(null == contract.getTenantId()){
            contract.setTenantId(getTenantId());
        }
        if(null != contract.getContractType()){
            contract.setContractNumber(getContractNumber(contract.getContractType()));
            if ("1".equals(contract.getContractType())){
                contract.setContractType("HD");
            }
            if ("2".equals(contract.getContractType())){
                contract.setContractType("GK");
            }
            if ("3".equals(contract.getContractType())){
                contract.setContractType("CD");
            }
        }
        String status = contractaSetStatus(contract.getSignDate(), contract.getExpiryDate(), contract.getRemindDate());
        contract.setStatus(status);
        contract.setCreateTime(new Date());
        contract.setCreateBy(UserUtils.getUser());
        if(contract.getFiles()!=null &&  contract.getFiles().length > 0){
            contract.setContractFile(StringUtils.join(contract.getFiles(),","));
        }
        return new R<>(contractService.insert(contract));
    }

    /**
     * 编辑
     * @param  contract  实体
     * @return success/false
     */
    @ApiOperation(value = "编辑修改合同信息",notes = "contractNumber是必要的")
    @ApiImplicitParam(name = "contract",value = "合同信息",dataType = "Contract",paramType = "body",required = true)
    @PutMapping
    public R<Boolean> edit(@RequestBody Contract contract) {
        if(null != contract.getContractType()){
            if ("1".equals(contract.getContractType())){
                contract.setContractType("HD");
            }
            if ("2".equals(contract.getContractType())){
                contract.setContractType("GK");
            }
            if ("3".equals(contract.getContractType())){
                contract.setContractType("CD");
            }
        }
        String status = contractaSetStatus(contract.getSignDate(), contract.getExpiryDate(), contract.getRemindDate());
        contract.setStatus(status);
        contract.setUpdateTime(new Date());
        contract.setUpdateBy(UserUtils.getUser());
        if(contract.getFiles()!=null &&  contract.getFiles().length > 0){
            contract.setContractFile(StringUtils.join(contract.getFiles(),","));
        }
        return new R<>(contractService.updateById(contract));
    }


    /**
     * 导出合同信息
     * @param contractNumbers
     * @return
     */
    @ApiOperation(value = "搜索导出",notes = "参数为空时，导出全部；")
    @ApiImplicitParam(name = "contractNumbers",value = "合同编号", paramType = "query",dataType = "String",example = "1,2")
    @PostMapping("/export/{contractNumbers}")
    public AjaxResult export(HttpServletResponse response, @PathVariable String contractNumbers) {
        List<Contract> list = null;
        Contract contract = new Contract();
        if("all".equals(contractNumbers)){
            contract.setTenantId(getTenantId());
            list = contractService.selectAll(contract);
        }else {
            list = contractService.selectByIds(contractNumbers);
        }
        for (int i = 0; i < list.size(); i++) {
            /*System.out.println("5555555"+list.get(i));*/
            if (list.get(i).getStatus() == null || list.get(i).getStatus().equals("")) {
                list.get(i).setStatus("未知");
            } else if (list.get(i).getStatus().equals("0")) {
                list.get(i).setStatus("正常");
            } else if (list.get(i).getStatus().equals("1")) {
                list.get(i).setStatus("临期");
            } else {
                list.get(i).setStatus("过期");
            }
            if (list.get(i).getContact() == null || list.get(i).getContact().equals("")) {
                list.get(i).setContact("暂无");
            }
            if (list.get(i).getSaleman() == null || list.get(i).getSaleman().equals("")) {
                list.get(i).setSaleman("暂无");
            }
            if (list.get(i).getRemark() == null || list.get(i).getRemark().equals("")) {
                list.get(i).setRemark("暂无");
            }
            if(null != contract.getContractType()){
                if ("HD".equals(contract.getContractType())){
                    contract.setContractType("货代合同");
                }
                if ("GK".equals(contract.getContractType())){
                    contract.setContractType("挂靠合同");
                }
                if ("CD".equals(contract.getContractType())){
                    contract.setContractType("车队合同");
                }
            }
        }
        ExcelUtil<Contract> util = new ExcelUtil<Contract>(Contract.class);
        return util.exportExcel(response,list, "合同信息",null);
    }

    /**
     * 合同编号生成方法
     */
    public String getContractNumber(String contractType){
        String contractNumber = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String str = format.format(new Date());
        int id = getTenantId();
        if ("1".equals(contractType)){
            contractNumber = "HD";
        }
        if ("3".equals(contractType)){
            contractNumber = "CD";
        }
        if ("2".equals(contractType)){
            contractNumber = "GK";
        }
        return contractNumber+String.valueOf(id)+str;
    }

    public String contractaSetStatus(Date signDate, Date expiryDate, Date remindDate){
        String status = new String();
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 3);//当前时间加三个月，即三个月后的时间    
        Date time = calendar.getTime();//获取三个月后的时间
        if (null != remindDate) {
            if (time.after(remindDate)) {
                if (null != expiryDate && date.after(expiryDate)){
                    status = "2";
                }else {
                    status = "1";
                }
            }else{
                status = "0";
            }
        }
        return status;
    }
}
