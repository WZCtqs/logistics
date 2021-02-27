package com.zhkj.lc.oilcard.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.zhkj.lc.common.util.*;
import com.zhkj.lc.common.util.excel.ExcelUtil;
import com.zhkj.lc.common.web.BaseController;
import com.zhkj.lc.oilcard.model.MajorMonthRecharge;
import com.zhkj.lc.oilcard.model.OilCardCashAmount;
import com.zhkj.lc.oilcard.model.OilRechargeTotal;
import com.zhkj.lc.oilcard.service.IMajorMonthRechargeService;
import com.zhkj.lc.oilcard.service.IOilRechargeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  主卡月充值前端控制器
 * </p>
 *
 * @author ckj
 * @since 2019-02-11
 */
@Api(description = "主卡月充值接口")
@RestController
@RequestMapping("/majorMonthRecharge")
public class MajorMonthRechargeController extends BaseController {

    @Autowired private IMajorMonthRechargeService majorMonthRechargeService;

    @Autowired private IOilRechargeService oilRechargeService;

    /**
    * 通过ID查询
    *
    * @param id ID
    * @return MajorMonthRecharge
    */
    @GetMapping("/{id}")
    public R<MajorMonthRecharge> get(@PathVariable Integer id) {
        return new R<>(majorMonthRechargeService.selectMajorMonthRecharge(id));
    }

    /**
    * 分页查询信息
    *
    * @param params 分页对象
    * @return 分页对象
    */
    @ApiOperation(value = "分页获取主卡月充值信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "params",value = "分页参数,可选字段:page,limit",paramType = "query",dataType = "Map"),
            @ApiImplicitParam(name = "majorMonthRecharge",value = "搜索条件",paramType = "query",dataType = "MajorMonthRecharge")
    })
    @GetMapping("/page")
    public Page page(@RequestParam Map<String, Object> params, MajorMonthRecharge majorMonthRecharge) {
        majorMonthRecharge.setTenantId(getTenantId());
        return majorMonthRechargeService.selectMajorMonthRechargeList(new Query<>(params), majorMonthRecharge);
    }

    /**
     * 添加
     * @param  majorMonthRecharge  实体
     * @return success/false
     */
    @ApiOperation(value = "添加主卡月充值信息")
    @ApiImplicitParam(name = "majorMonthRecharge",value = "主卡月充值信息",paramType = "body",dataType = "MajorMonthRecharge",required = true)
    @PostMapping
    public R<Boolean> add(@RequestBody MajorMonthRecharge majorMonthRecharge) {

        majorMonthRecharge.setCreateBy(UserUtils.getUser());
        majorMonthRecharge.setTenantId(getTenantId());
        majorMonthRecharge.setYearMonth(DateUtils.getDateYM()); // YYYY-MM
        return new R<>(majorMonthRechargeService.insertMajorMonthRecharge(majorMonthRecharge));
    }

    /**
     * 删除
     * @param ids IDs
     * @return success/false
     */
    @ApiOperation(value = "删除主卡月充值信息")
    @ApiImplicitParam(name = "ids",value = "主卡月充值信息",paramType = "path",dataType = "String",required = true)
    @DeleteMapping("/{ids}")
    public R<Boolean> delete(@PathVariable("ids") String ids) {
        return new R<>(majorMonthRechargeService.deleteMajorMonthRecharge(ids, UserUtils.getUser()));
    }

    /**
     * 编辑
     * @param  majorMonthRecharge  实体
     * @return success/false
     */
    @ApiOperation(value = "编辑主卡月充值信息")
    @ApiImplicitParam(name = "majorMonthRecharge",value = "主卡月充值信息",paramType = "body",dataType = "MajorMonthRecharge",required = true)
    @PutMapping
    public R<Boolean> edit(@RequestBody MajorMonthRecharge majorMonthRecharge) {
        majorMonthRecharge.setUpdateBy(UserUtils.getUser());
        return new R<>(majorMonthRechargeService.updateMajorMonthRecharge(majorMonthRecharge));
    }

    @ApiOperation(value = "搜索导出")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "选中的主卡月充值ids", paramType = "path", dataType = "String", required = true),
            @ApiImplicitParam(name = "majorMonthRecharge", value = "主卡月充值信息", paramType = "query", dataType = "MajorMonthRecharge")
    })
   @PostMapping("/export/{ids}")
   public AjaxResult export(@PathVariable("ids") String ids, MajorMonthRecharge majorMonthRecharge, HttpServletRequest request, HttpServletResponse response){

        List<MajorMonthRecharge> list;
        if ("all".equals(ids)){
            majorMonthRecharge.setTenantId(getTenantId());
            list = majorMonthRechargeService.selectMajorMonthRechargeList(majorMonthRecharge);
        }else {
            list = majorMonthRechargeService.selectMajorMonthRechargeByIds(ids);
        }

        String sheetName = (majorMonthRecharge.getYearMonth()==null?"":majorMonthRecharge.getYearMonth()) + "-主卡月充值信息";
        ExcelUtil<MajorMonthRecharge> util = new ExcelUtil<MajorMonthRecharge>(MajorMonthRecharge.class);
        return util.exportExcel( request, response, list,sheetName,null);
    }

    @ApiOperation(value = "公司油卡现金金额显示")
    @ApiImplicitParam(name = "yearMonth",value = "月份",paramType = "path",dataType = "String",required = true)
    @GetMapping("/cashAmount")
    public Page<OilCardCashAmount> selectMajorCashAmount(@RequestParam Map<String, Object> params, String yearMonth) {

        return majorMonthRechargeService.selectMajorMonthRechargeCashAmount(new Query(params), yearMonth, getTenantId());
    }

    @ApiOperation(value = "公司油卡现金金额导出")
    @ApiImplicitParam(name = "yearMonth",value = "月份",paramType = "path",dataType = "String",required = true)
    @GetMapping("/cashAmountExport/{yearMonth}")
    public AjaxResult selectMajorCashAmountExport(@PathVariable("yearMonth") String yearMonth, HttpServletRequest request, HttpServletResponse response) {

        List<OilCardCashAmount> list = majorMonthRechargeService.selectMajorMonthRechargeCashAmount(yearMonth, getTenantId());
        ExcelUtil<OilCardCashAmount> util = new ExcelUtil<OilCardCashAmount>(OilCardCashAmount.class);
        return util.exportExcel( request, response, list,(yearMonth==null?"":yearMonth)+"-公司油卡现金金额",null);
    }

    @GetMapping("/oilTotalPage")
    public Page oilTotalPage(@RequestParam Map<String, Object> params, OilRechargeTotal param) {
        param.setTenantId(getTenantId());
        return oilRechargeService.selectOilCardRechargeTotal(new Query<>(params), param);
    }
}
