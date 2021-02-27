package com.zhkj.lc.oilcard.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.zhkj.lc.common.util.*;
import com.zhkj.lc.common.util.excel.ExcelUtil;
import com.zhkj.lc.common.web.BaseController;
import com.zhkj.lc.oilcard.model.PointAmount;
import com.zhkj.lc.oilcard.service.IPointAmountService;
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
 * @Description 公司积分余额 前端控制器
 * @Author ckj
 * @Date 2019/3/5 21:38
 */
@Api(description = "公司积分余额接口")
@RestController
@RequestMapping("/pointAmount")
public class PointAmountController extends BaseController {

    @Autowired private IPointAmountService pointAmountService;

    /**
     * 通过ID查询
     *
     * @param id ID
     * @return OilCard
     */
    @ApiOperation(value = "查询公司积分余额", notes = "根据id查询公司积分余额")
    @ApiImplicitParam(name = "id", value = "油卡基础信息id", required = true, dataType = "Integer", paramType = "path")
    @GetMapping("/{id}")
    public R<PointAmount> get(@PathVariable Integer id) {
        return new R<>(pointAmountService.selectPointAmount(id));
    }

    /**
     * 分页查询信息
     *
     * @param pointAmount 分页对象
     * @param params 分页参数
     * @return 分页对象
     */
    @ApiOperation(value = "搜索分页显示油卡基础信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "params",value = "分页参数,可选字段:page,limit",paramType = "query",dataType = "Map"),
            @ApiImplicitParam(name = "pointAmount",value = "搜索条件",paramType = "query",dataType = "PointAmount")
    })
    @GetMapping("/page")
    public Page<PointAmount> page(@RequestParam Map<String, Object> params, PointAmount pointAmount) {
        pointAmount.setTenantId(getTenantId());
        return pointAmountService.selectPointAmountList(new Query(params), pointAmount);
    }

    /**
     * 添加
     * @param  pointAmount  实体
     * @return success/false
     */
    @ApiOperation(value = "添加")
    @ApiImplicitParam(name = "pointAmount",value = "油卡基础信息类",paramType = "body",dataType = "PointAmount",required = true)
    @Transactional
    @PostMapping
    public R<Boolean> add(@RequestBody PointAmount pointAmount) {

        PointAmount pointAmount1 = new PointAmount();
        pointAmount1.setTenantId(getTenantId());
        pointAmount1.setYearMonth(DateUtils.getLastDateYM());
        pointAmount1.setCompany(pointAmount.getCompany());
        List<PointAmount> list = pointAmountService.selectPointAmountList(pointAmount1);
        if (null != list && list.size() > 0){
            pointAmount.setLastPoint(list.get(0).getPointAmount());
        }
        pointAmount.setYearMonth(DateUtils.getDateYM());
        pointAmount.setCreateBy(UserUtils.getUser());
        pointAmount.setTenantId(getTenantId());
        return new R<>(pointAmountService.insertPointAmount(pointAmount));
    }

    /**
     * 删除
     * @param ids ID
     * @return success/false
     */
    @ApiOperation(value = "删除或者批量删除公司积分余额")
    @ApiImplicitParam(name = "ids",value = "油卡id",paramType = "path",required = true)
    @DeleteMapping("/{ids}")
    public R<Boolean> delete(@PathVariable("ids") String ids) {
        return new R<>(pointAmountService.deletePointAmount(ids,UserUtils.getUser()));
    }

    /**
     * 编辑
     * @param  pointAmount  实体
     * @return success/false
     */
    @ApiOperation(value = "编辑修改公司积分余额",notes = "id是必要的")
    @ApiImplicitParam(name = "pointAmount",value = "公司积分余额",dataType = "PointAmount",paramType = "body",required = true)
    @PutMapping
    public R<Boolean> edit(@RequestBody PointAmount pointAmount) {
        pointAmount.setUpdateBy(UserUtils.getUser());
        return new R<>(pointAmountService.updatePointAmount(pointAmount));
    }

    @ApiOperation(value = "导出",notes = "pointAmount参数中yearMonth是必要的")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "公司积分余额ids", paramType = "path", dataType = "String", required = true),
            @ApiImplicitParam(name = "pointAmount", value = "公司积分余额", paramType = "query", dataType = "PointAmount")
    })
    @PostMapping("/export/{ids}")
    public AjaxResult export(@PathVariable("ids") String ids, PointAmount pointAmount, HttpServletRequest request, HttpServletResponse response)
    {
        List<PointAmount> list;
        if ("all".equals(ids)) {
            pointAmount.setTenantId(getTenantId());
            list = pointAmountService.selectPointAmountList(pointAmount);
        }else {
             list = pointAmountService.selectPointAmountListByIds(ids);
        }
        String sheetName = (pointAmount.getYearMonth() == null?"":pointAmount.getYearMonth())+"公司积分余额信息";
        ExcelUtil<PointAmount> util = new ExcelUtil<PointAmount>(PointAmount.class);
        return util.exportExcel( request, response, list,sheetName,null);
    }

    @ApiOperation(value = "月公司重复", notes = "参数传yearMonth,company")
    @ApiImplicitParam(name = "pointAmount",value = "公司积分余额",dataType = "PointAmount",paramType = "body",required = true)
    @PostMapping("/pointAmount2")
    public R<Boolean> pointAmount2(@RequestBody PointAmount pointAmount) {
        pointAmount.setTenantId(getTenantId());
        List<PointAmount> list = pointAmountService.selectPointAmountList(pointAmount);
        if (null != list && list.size() > 0){
            return new R<>(false);
        }
        return new R<>(true);
    }
}
