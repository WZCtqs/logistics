package com.zhkj.lc.oilcard.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.zhkj.lc.common.util.AjaxResult;
import com.zhkj.lc.common.util.DateUtils;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.util.excel.ExcelUtil;
import com.zhkj.lc.common.web.BaseController;
import com.zhkj.lc.oilcard.model.RechargeNormal;
import com.zhkj.lc.oilcard.service.IRechargeNormalService;
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
@Api(description = "正常油卡充值(只包含导出和分页显示接口)")
@RestController
@RequestMapping("/rechargeNormal")
public class RechargeNormalController extends BaseController {

	@Autowired
	private IRechargeNormalService rechargeNormalService;

	/**
	 * 分页查询信息
	 *
	 * @param rechargeNormal 分页对象
	 * @param params 分页参数
	 * @return 分页对象
	 */
	@ApiOperation(value = "搜索分页显示正常油卡充值信息",notes = "params参数不写默认获取第一页10条数据；rechargeNormal参数不写获取全部信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "params",value = "分页参数，字段可选:page,limit",paramType = "query",dataType = "Map"),
			@ApiImplicitParam(name = "rechargeNormal",value = "搜索条件,字段可选:attribute,truckId(通过车牌号plateNumber获取的车辆id),beginTime,endTime(申请时间范围),ownerDriverId等",paramType = "query",dataType = "RechargeNormal")
	})
	@GetMapping("/page")
	public Page<RechargeNormal> page(@RequestParam Map<String, Object> params,RechargeNormal rechargeNormal) {
		rechargeNormal.setRechargeType("0");
		rechargeNormal.setTenantId(getTenantId());
		return rechargeNormalService.selectRechargeNormalListPage(new Query(params), rechargeNormal);
	}

	/**
	 * 搜索导出
	 * @param rechargeNormal 搜索条件
	 * @return 结果
	 */
	@ApiOperation(value = "导出",notes = "参数为空时，导出全部")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "ids", value = "油卡充值ids", paramType = "path", dataType = "String", required = true),
			@ApiImplicitParam(name = "rechargeNormal", value = "正常充值信息,字段可选:attribute,truckId(通过车牌号plateNumber获取的车辆id),beginTime,endTime(申请时间范围),ownerDriverId等", paramType = "query", dataType = "RechargeNormal")
	})
	@PostMapping("/export/{ids}")
	public AjaxResult export(@PathVariable("ids") String ids, RechargeNormal rechargeNormal, HttpServletRequest request, HttpServletResponse response)
	{
		List<RechargeNormal> list;
		if ("all".equals(ids)) {
			rechargeNormal.setRechargeType("0");
			rechargeNormal.setTenantId(getTenantId());
			list = rechargeNormalService.selectRechargeNormalList(rechargeNormal);
		}else {
			list = rechargeNormalService.selectRechargeNormalListByIds(ids);
		}

		String sheetName = DateUtils.getDate()+"-正常油卡充值信息";
		ExcelUtil<RechargeNormal> util = new ExcelUtil<RechargeNormal>(RechargeNormal.class);
		return util.exportExcel(request, response, list, sheetName,null);
	}

	@PostMapping("/list")
	public List<RechargeNormal> export(@RequestBody  RechargeNormal rechargeNormal)
	{
		rechargeNormal.setRechargeType("0");
		return rechargeNormalService.selectRechargedNormalList(rechargeNormal);
	}


}
