package com.zhkj.lc.oilcard.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.zhkj.lc.common.util.AjaxResult;
import com.zhkj.lc.common.util.DateUtils;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.util.excel.ExcelUtil;
import com.zhkj.lc.common.web.BaseController;
import com.zhkj.lc.oilcard.model.RechargeFreight;
import com.zhkj.lc.oilcard.service.IRechargeFreightService;
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
 * 正常油卡充值 前端控制器
 * </p>
 *
 * @author ckj
 * @since 2018-12-14
 */
@Api(description = "运费油卡充值(只包含导出和分页显示接口)")
@RestController
@RequestMapping("/rechargeFreight")
public class RechargeFreightController extends BaseController {

	@Autowired
	private IRechargeFreightService rechargeFreightService;

	/**
	 * 分页查询信息
	 *
	 * @param params 分页对象
	 * @param rechargeFreight 搜索信息
	 * @return 分页对象
	 */
	@ApiOperation(value = "搜索分页显示运费油卡充值信息",notes = "params参数不写默认获取第一页10条数据；rechargeFreight参数不写获取全部信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "params",value = "分页参数,可选字段:page,limit",paramType = "query",dataType = "Map"),
			@ApiImplicitParam(name = "RechargeFreight",value = "搜索条件,字段可选:cardType,openCardPlace,truckOwner,beginTime,endTime(申请时间范围),ownerDriverId等",paramType = "query",dataType = "RechargeFreight")
	})
	@GetMapping("/page")
	public Page<RechargeFreight> page(@RequestParam Map<String, Object> params, RechargeFreight rechargeFreight)
	{
		rechargeFreight.setRechargeType("1");
		rechargeFreight.setTenantId(getTenantId());
		return rechargeFreightService.selectRechargeFreightListPage(new Query<>(params), rechargeFreight);
	}

	/**
	 * 导出
	 * @param rechargeFreight 搜索信息
	 * @return 结果
	 */
	@ApiOperation(value = "导出",notes = "参数为空时，导出全部")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "ids", value = "油卡充值ids", paramType = "path", dataType = "String", required = true),
			@ApiImplicitParam(name = "rechargeFreight", value = "搜索条件，字段可选:cardType,openCardPlace,truckOwner,beginTime,endTime(申请时间范围),ownerDriverId等", paramType = "query", dataType = "RechargeFreight")
	})
	@PostMapping("/export/{ids}")
	public AjaxResult export(@PathVariable("ids") String ids, RechargeFreight rechargeFreight, HttpServletRequest request, HttpServletResponse response)
	{
		List<RechargeFreight> list;
		if ("all".equals(ids)) {
			rechargeFreight.setRechargeType("1");
			rechargeFreight.setTenantId(getTenantId());
			list = rechargeFreightService.selectRechargeFreightList(rechargeFreight);
		}else {
			list = rechargeFreightService.selectRechargeFreightListByIds(ids);
		}

		String sheetName = DateUtils.getDate()+"-运费油卡充值信息";
		ExcelUtil<RechargeFreight> util = new ExcelUtil<RechargeFreight>(RechargeFreight.class);
		return util.exportExcel(request, response, list, sheetName,null);
	}
}
