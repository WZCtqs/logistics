package com.zhkj.lc.trunk.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.zhkj.lc.common.constant.CommonConstant;
import com.zhkj.lc.common.util.R;
import com.zhkj.lc.common.util.UserUtils;
import com.zhkj.lc.common.util.support.Convert;
import com.zhkj.lc.common.web.BaseController;
import com.zhkj.lc.trunk.model.TaxRate;
import com.zhkj.lc.trunk.service.ITaxRateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 税率 信息操作处理
 * 
 * @author tqs
 * @date 2019-05-06
 */
@RestController
@AllArgsConstructor
@Api(description = "税率管理")
@RequestMapping("/taxRate")
public class TaxRateController extends BaseController
{
	@Autowired
	private ITaxRateService taxRateService;

	private final RedisTemplate redisTemplate;

	@ApiOperation(value = "新增利率")
	@PostMapping
	public R<Boolean> addTaxRate(@RequestBody TaxRate taxRate){
		taxRate.setTenantId(getTenantId());
		taxRate.setCreateBy(UserUtils.getUser());
		return new R<>(taxRateService.updateById(taxRate));
	}

	@ApiOperation(value = "设置默认税率")
	@GetMapping("setDefaultRate/{id}")
	public R<Boolean> setDefaultRate(@PathVariable("id") Integer id){
		TaxRate taxRate = new TaxRate();
		taxRate.setId(id);
		taxRate.setTenantId(getTenantId());
		taxRate.setUpdateBy(UserUtils.getUser());
		return new R<>(taxRateService.setDefaultRate(taxRate));
	}

	@ApiOperation(value = "查询税率设置（含历史数据）")
	@GetMapping
	public List<TaxRate> getRate(){
		TaxRate taxRate = new TaxRate();
		taxRate.setTenantId(getTenantId());
		EntityWrapper wrapper = new EntityWrapper();
		wrapper.setEntity(taxRate);
		wrapper.orderDesc(Collections.singleton("id"));
		return taxRateService.selectList(wrapper);
	}

	@ApiOperation(value = "服务调用查询当前利率")
	@GetMapping("/{tenantId}")
	public TaxRate getRateForFegin(@PathVariable("tenantId") Integer tenantId){
		TaxRate taxRate = new TaxRate();
		taxRate.setTenantId(tenantId);
		taxRate.setDelFlag(CommonConstant.STATUS_NORMAL);
		Object obj = redisTemplate.opsForValue().get("SYSTEM_TAX_RATE_" + tenantId);
		if(obj != null){
			taxRate.setTaxRate((BigDecimal) obj);
			return taxRate;
		}else {
			taxRate = taxRateService.selectOne(new EntityWrapper<>(taxRate));
			if(taxRate != null){
				redisTemplate.opsForValue().set("SYSTEM_TAX_RATE_" + tenantId, taxRate.getTaxRate());
			}
		}
		return taxRate;
	}

	@ApiOperation(value = "查询所有利率")
	@GetMapping("allTax")
	public List<TaxRate> allTax(){
		Integer tenantId = getTenantId();
		List<TaxRate> list = new ArrayList<>();
		Object obj = redisTemplate.opsForValue().get("SYSTEM_TAX_LIST_" + tenantId);
		if(obj != null){
			list = (List<TaxRate>) obj;
			for (TaxRate r :
					list) {
				System.out.println(r.toString());
			}
		}else {
			TaxRate rate = new TaxRate();
			rate.setTenantId(tenantId);
			list = taxRateService.selectList(new EntityWrapper<>(rate));
			redisTemplate.opsForValue().set("SYSTEM_TAX_LIST_" + tenantId, list, 2, TimeUnit.HOURS);
		}
		return list;
	}

	@ApiOperation(value = "根据id删除")
	@DeleteMapping("/{ids}")
	public R<Boolean> deleteRate(@PathVariable String ids){
		Integer[] id_arr = Convert.toIntArray(ids);
		for (int i = 0; i < id_arr.length; i++) {
			taxRateService.deleteById(id_arr[i]);
		}
		return new R<>(Boolean.TRUE);
	}

}
