package com.zhkj.lc.oilcard.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.zhkj.lc.common.util.*;
import com.zhkj.lc.common.util.excel.ExcelUtil;
import com.zhkj.lc.common.web.BaseController;
import com.zhkj.lc.oilcard.model.MajorMonthRecharge;
import com.zhkj.lc.oilcard.model.OilCashAmount;
import com.zhkj.lc.oilcard.model.OilMajorRecharge;
import com.zhkj.lc.oilcard.service.IMajorMonthRechargeService;
import com.zhkj.lc.oilcard.service.IOilCashAmountService;
import com.zhkj.lc.oilcard.service.IOilMajorRechargeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  主卡充值前端控制器
 * </p>
 *
 * @author ckj
 * @since 2019-02-11
 */
@Api(description = "主卡充值接口")
@RestController
@RequestMapping("/oilMajorRecharge")
public class OilMajorRechargeController extends BaseController {

    @Autowired private IOilMajorRechargeService oilMajorRechargeService;
    @Autowired private IMajorMonthRechargeService majorMonthRechargeService;
    @Autowired private IOilCashAmountService oilCashAmountService;

    /**
    * 通过ID查询
    *
    * @param id ID
    * @return OilMajorRecharge
    */
    @ApiOperation(value = "根据id获取主卡充值基础信息")
    @GetMapping("/{id}")
    public R<OilMajorRecharge> get(@PathVariable Integer id) {
        return new R<>(oilMajorRechargeService.selectMajorRecharge(id));
    }

    /**
    * 分页查询信息
    *
    * @param params 分页对象
    * @return 分页对象
    */
    @ApiOperation(value = "搜索分页显示主卡充值信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "params",value = "分页参数,可选字段:page,limit",paramType = "query",dataType = "Map"),
            @ApiImplicitParam(name = "oilMajorRecharge",value = "主卡充值信息",paramType = "query",dataType = "OilMajorRecharge")
    })
    @GetMapping("/page")
    public Page page(@RequestParam Map<String, Object> params, OilMajorRecharge oilMajorRecharge) {
        oilMajorRecharge.setTenantId(getTenantId());
        return oilMajorRechargeService.selectMajorRechargeList(new Query(params), oilMajorRecharge);
    }

    /**
     * 添加
     * @param  oilMajorRecharge  实体
     * @return success/false
     */
    @ApiOperation(value = "添加主卡充值信息",notes = "主卡充值id值后台自动生成")
    @ApiImplicitParam(name = "oilMajorRecharge",value = "主卡充值信息",paramType = "body",dataType = "OilMajorRecharge", required = true)
    @Transactional
    @PostMapping
    public R<Boolean> add(@RequestBody OilMajorRecharge oilMajorRecharge) {
        Integer tenantId = getTenantId();
        String createBy = UserUtils.getUser();
        String dateYM = DateUtils.getDateYM();
        R r = new R(false);
        // 油卡充值要将充值金额添加到主卡月分配金额中
        MajorMonthRecharge majorMonthRecharge =  majorMonthRechargeService.selectMajorMonthRechargeUpdate(oilMajorRecharge.getMajorId(), tenantId, dateYM);
        if (null == majorMonthRecharge){
            majorMonthRecharge = new MajorMonthRecharge();
            majorMonthRecharge.setMajorId(oilMajorRecharge.getMajorId());
            majorMonthRecharge.setYearMonth(dateYM);
            majorMonthRecharge.setTenantId(tenantId);
            majorMonthRecharge.setCreateBy(createBy);
            majorMonthRechargeService.insertMajorMonthRecharge(majorMonthRecharge);
        }

        majorMonthRecharge =  majorMonthRechargeService.selectMajorMonthRechargeUpdate(oilMajorRecharge.getMajorId(), tenantId, dateYM);

        // 加油站现金余额
        OilCashAmount oilCashAmount = oilCashAmountService.oilCashAmount2(majorMonthRecharge.getTenantId(), dateYM, majorMonthRecharge.getMajorCompany());

        if (null == oilCashAmount){
            r.setMsg("加油站当月没有给 "+ majorMonthRecharge.getMajorCompany() + " 分配充值金额，无法对附属主卡进行充值！");
            return r;
        }else {
            if (oilCashAmount.getCashAmount().subtract(oilMajorRecharge.getMajorRechargeAmount()).doubleValue() >= 0){

                // 更新加油现金余额的月分配金额
                if(oilMajorRecharge.getRechargeType().equals("0")){
                    oilCashAmount.setRechargeCash(oilCashAmount.getRechargeCash().add(oilMajorRecharge.getMajorRechargeAmount()));
                }
                oilCashAmount.setDistributeCash(oilCashAmount.getDistributeCash().add(oilMajorRecharge.getMajorRechargeAmount()));
                oilCashAmountService.updateOilCashAmount(oilCashAmount);
                // 更新主卡月充值金额
                majorMonthRecharge.setRechargeSum(majorMonthRecharge.getRechargeSum().add(oilMajorRecharge.getMajorRechargeAmount()));
                majorMonthRechargeService.updateMajorMonthRecharge(majorMonthRecharge);
                // 新增
                oilMajorRecharge.setMajorRechargeDate(new Date());
                oilMajorRecharge.setTenantId(tenantId);
                oilMajorRecharge.setCreateBy(createBy);
                return new R<>(oilMajorRechargeService.insertMajorRecharge(oilMajorRecharge));
            }else {
                r.setMsg("主卡 "+oilMajorRecharge.getMajorNumber()+" 充值金额大于加油站给 "+majorMonthRecharge.getMajorCompany()+" 可充值分配的金额！");
                return r;
            }
        }
    }

    /**
     * 删除
     * @param ids IDs
     * @return success/false
     */
    @ApiOperation(value = "删除主卡充值信息")
    @ApiImplicitParam(name = "ids",value = "主卡充值ids",paramType = "path",dataType = "String", required = true)
    @DeleteMapping("/{ids}")
    public R<Boolean> delete(@PathVariable("ids") String ids) {
        return new R<>(oilMajorRechargeService.deleteMajorRecharge(ids, UserUtils.getUser()));
    }

    /**
     * 编辑
     * @param  oilMajorRecharge  实体
     * @return success/false
     */
    @ApiOperation(value = "编辑主卡充值信息")
    @ApiImplicitParam(name = "oilMajorRecharge",value = "主卡充值信息",paramType = "body",dataType = "OilMajorRecharge",required = true)
    @PutMapping
    public R<Boolean> edit(@RequestBody OilMajorRecharge oilMajorRecharge) {
        oilMajorRecharge.setTenantId(getTenantId());
        oilMajorRecharge.setUpdateBy(UserUtils.getUser());
        return new R<>(oilMajorRechargeService.updateMajorRecharge(oilMajorRecharge));
    }

    /**
     * 编辑
     * @param  oilMajorRecharge  实体
     * @return success/false
     */
    @ApiOperation(value = "导出主卡充值信息")
    @ApiImplicitParam(name = "oilMajorRecharge",value = "主卡充值信息(majorId主卡id，年月yearMonth，)",paramType = "body",dataType = "OilMajorRecharge")
    @PostMapping("/export/{ids}")
    public AjaxResult export(@PathVariable("ids") String ids, OilMajorRecharge oilMajorRecharge, HttpServletRequest request, HttpServletResponse response) {

        List<OilMajorRecharge> list;
        if ("all".equals(ids)){
            list = oilMajorRechargeService.selectMajorRechargeByIds(ids);
        }else {
            oilMajorRecharge.setTenantId(getTenantId());
            list = oilMajorRechargeService.selectMajorRechargeList(oilMajorRecharge);
        }

        String sheetName = DateUtils.getDate() + "-主卡充值信息";
        ExcelUtil<OilMajorRecharge> excelUtil = new  ExcelUtil<OilMajorRecharge>(OilMajorRecharge.class);
        return excelUtil.exportExcel(request, response, list, sheetName, null);
    }

}
