package com.zhkj.lc.oilcard.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.zhkj.lc.common.util.AjaxResult;
import com.zhkj.lc.common.util.DateUtils;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.util.excel.ExcelUtil;
import com.zhkj.lc.common.web.BaseController;
import com.zhkj.lc.oilcard.model.RechargeExternal;
import com.zhkj.lc.oilcard.service.IRechargeExternalService;
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
 * 外调车油卡充值 前端控制器
 * </p>
 *
 * @author ckj
 * @since 2018-12-14
 */
@Api(description = "外调车油卡充值(只包含导出和分页显示接口)")
@RestController
@RequestMapping("/rechargeExternal")
public class RechargeExternalController extends BaseController {

	@Autowired
	private IRechargeExternalService rechargeExternalService;

	/**
	 * 搜索导出
	 * @param rechargeExternal 搜索条件
	 * @return 结果
	 */
	@ApiOperation(value = "搜索导出",notes = "rechargeExternal参数不写获取全部信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "ids", value = "油卡充值ids", paramType = "path", dataType = "String", required = true),
			@ApiImplicitParam(name = "rechargeExternal", value = "外调车充值信息，可选字段：cardType,truckOwner,beginTime,endTime(申请时间范围),ownerDriverId等", paramType = "query", dataType = "RechargeExternal")
	})
	@PostMapping("/export/{ids}")
	public AjaxResult export(@PathVariable("ids") String ids, RechargeExternal rechargeExternal, HttpServletRequest request, HttpServletResponse response)
	{
		List<RechargeExternal> list;
		if ("all".equals(ids)) {
			rechargeExternal.setTenantId(getTenantId());
			rechargeExternal.setRechargeType("2");
			list = rechargeExternalService.selectRechargeExternalList(rechargeExternal);
		}else {
			list = rechargeExternalService.selectRechargeExternalListByIds(ids);
		}

		String sheetName = DateUtils.getDate() +"-外调车油卡充值信息";
		ExcelUtil<RechargeExternal> util = new ExcelUtil<RechargeExternal>(RechargeExternal.class);
		return util.exportExcel(request, response, list, sheetName,null);
	}

	/**
	 * 分页查询信息
	 *
	 * @param params 分页对象
	 * @return 分页对象
	 */
	@ApiOperation(value = "搜索分页显示外调车油卡充值信息",notes = "params参数不写默认获取第一页10条数据；rechargeExternal参数不写获取全部信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "params",value = "分页参数，字段可选：page,limit",paramType = "query",dataType = "Map"),
			@ApiImplicitParam(name = "rechargeExternal",value = "搜索条件,条件字段可选：cardType,truckOwner,beginTime,endTime(申请时间范围),ownerDriverId等",paramType = "query",dataType = "RechargeExternal")
	})
	@GetMapping("/page")
	public Page<RechargeExternal> page(@RequestParam Map<String, Object> params, RechargeExternal rechargeExternal)
	{
		rechargeExternal.setRechargeType("2");
		rechargeExternal.setTenantId(getTenantId());
		return rechargeExternalService.selectRechargeExternalListPage(new Query<>(params), rechargeExternal);
	}
}
