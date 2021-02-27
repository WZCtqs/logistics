package com.zhkj.lc.oilcard.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.zhkj.lc.common.util.*;
import com.zhkj.lc.common.util.excel.ExcelUtil;
import com.zhkj.lc.common.web.BaseController;
import com.zhkj.lc.oilcard.model.OilCashAmount;
import com.zhkj.lc.oilcard.service.IOilCashAmountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @Description 加油现金余额 前端控制器
 * @Author ckj
 * @Date 2019/3/5 20:41
 */
@Api(description = "加油现金余额接口")
@RestController
@RequestMapping("/oilCashAmount")
public class OilCashAmountController extends BaseController {

    @Autowired private IOilCashAmountService oilCashAmountService;

    /**
     * 通过ID查询
     *
     * @param id ID
     * @return OilCard
     */
    @ApiOperation(value = "查询加油现金余额", notes = "根据id查询加油现金余额")
    @ApiImplicitParam(name = "id", value = "油卡基础信息id", required = true, dataType = "Integer", paramType = "path")
    @GetMapping("/{id}")
    public R<OilCashAmount> get(@PathVariable Integer id) {
        return new R<>(oilCashAmountService.selectOilCashAmount(id));
    }

    /**
     * 分页查询信息
     *
     * @param oilCashAmount 分页对象
     * @param params 分页参数
     * @return 分页对象
     */
    @ApiOperation(value = "搜索分页显示油卡基础信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "params",value = "分页参数,可选字段:page,limit",paramType = "query",dataType = "Map"),
            @ApiImplicitParam(name = "oilCashAmount",value = "搜索条件",paramType = "query",dataType = "OilCashAmount")
    })
    @GetMapping("/page")
    public Page<OilCashAmount> page(@RequestParam Map<String, Object> params, OilCashAmount oilCashAmount) {
        oilCashAmount.setTenantId(getTenantId());
        return oilCashAmountService.selectOilCashAmountList(new Query(params), oilCashAmount);
    }

    /**
     * 添加
     * @param  oilCashAmount  实体
     * @return success/false
     */
    @ApiOperation(value = "添加")
    @ApiImplicitParam(name = "oilCashAmount",value = "油卡基础信息类",paramType = "body",dataType = "OilCashAmount",required = true)
    @Transactional
    @PostMapping
    public R<Boolean> add(@RequestBody OilCashAmount oilCashAmount) {
        oilCashAmount.setTenantId(getTenantId());
        oilCashAmount.setCreateBy(UserUtils.getUser());
        return new R<>(oilCashAmountService.insertOilCashAmount(oilCashAmount));
    }

    /**
     * 删除
     * @param ids ID
     * @return success/false
     */
    @ApiOperation(value = "删除或者批量删除加油现金余额")
    @ApiImplicitParam(name = "ids",value = "油卡id",paramType = "path",required = true)
    @DeleteMapping("/{ids}")
    public R<Boolean> delete(@PathVariable String ids) {
        return new R<>(oilCashAmountService.deleteOilCashAmount(ids,UserUtils.getUser()));
    }

    /**
     * 编辑
     * @param  oilCashAmount  实体
     * @return success/false
     */
    @ApiOperation(value = "编辑修改加油现金余额",notes = "id是必要的")
    @ApiImplicitParam(name = "oilCashAmount",value = "加油现金余额",dataType = "OilCashAmount",paramType = "body",required = true)
    @PutMapping
    public R<Boolean> edit(@RequestBody OilCashAmount oilCashAmount) {
        oilCashAmount.setUpdateBy(UserUtils.getUser());
        return new R<>(oilCashAmountService.updateOilCashAmount(oilCashAmount));
    }

    @ApiOperation(value = "导出", notes = "oilCashAmount参数必须有yearMonth")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "加油现金余额ids", paramType = "path", dataType = "String", required = true),
            @ApiImplicitParam(name = "oilCashAmount", value = "加油现金余额", paramType = "query", dataType = "OilCashAmount")
    })
    @PostMapping("/export/{ids}")
    public AjaxResult export(@PathVariable("ids") String ids, OilCashAmount oilCashAmount, HttpServletRequest request, HttpServletResponse response)
    {
        List<OilCashAmount> list;
        if ("all".equals(ids)) {
            oilCashAmount.setTenantId(getTenantId());
            list = oilCashAmountService.selectOilCashAmountList(oilCashAmount);
        }else {
            list = oilCashAmountService.selectOilCashAmountListByIds(ids);
        }
        String sheetName = (oilCashAmount.getYearMonth() == null?"":oilCashAmount.getYearMonth())+"-加油现金余额信息";
        ExcelUtil<OilCashAmount> util = new ExcelUtil<OilCashAmount>(OilCashAmount.class);
        return util.exportExcel( request, response, list,sheetName,null);
    }

    @ApiOperation(value = "月公司重复", notes = "参数传yearMonth,company")
    @ApiImplicitParam(name = "oilCashAmount",value = "加油现金余额",dataType = "OilCashAmount",paramType = "body",required = true)
    @PostMapping("/oilCashAmount2")
    public R<Boolean> oilCashAmount2(@RequestBody OilCashAmount oilCashAmount) {
        oilCashAmount.setTenantId(getTenantId());
        List<OilCashAmount> list = oilCashAmountService.selectOilCashAmountList(oilCashAmount);
        if (null != list && list.size() > 0){
            return new R<>(false);
        }
        return new R<>(true);
    }
}
