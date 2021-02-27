package com.zhkj.lc.trunk.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zhkj.lc.common.constant.CommonConstant;
import com.zhkj.lc.trunk.mapper.LoanMapper;
import com.zhkj.lc.trunk.mapper.TaxRateMapper;
import com.zhkj.lc.trunk.model.TaxRate;
import com.zhkj.lc.trunk.service.ITaxRateService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 税率 服务层实现
 *
 * @author tqs
 * @date 2019-05-06
 */
@AllArgsConstructor
@Service
public class TaxRateServiceImpl extends ServiceImpl<TaxRateMapper,TaxRate> implements ITaxRateService
{
	@Autowired private TaxRateMapper taxRateMapper;

	private final RedisTemplate redisTemplate;

	@Transactional
	@Override
	public boolean updateById(TaxRate taxRate){

		/*如果已经有税率 将新税率设置为非默认状态*/
		TaxRate rate = new TaxRate();
		rate.setTenantId(taxRate.getTenantId());
		List<TaxRate> list = taxRateMapper.selectList(new EntityWrapper<>(rate));
		if(list != null && list.size() > 0){
			taxRate.setDelFlag(CommonConstant.STATUS_DEL);
		}else {
			/*否则设置为默认状态*/
			taxRate.setDelFlag(CommonConstant.STATUS_NORMAL);
			redisTemplate.opsForValue().set("SYSTEM_TAX_RATE_" + taxRate.getTenantId(), taxRate.getTaxRate());
		}
		taxRateMapper.insert(taxRate);
		return true;
	}

	@Override
	@Transactional
	public boolean setDefaultRate(TaxRate taxRate) {
		TaxRate rate = new TaxRate();
		rate.setDelFlag(CommonConstant.STATUS_DEL);
		TaxRate entity = new TaxRate();
		entity.setTenantId(taxRate.getTenantId());
		taxRateMapper.update(rate, new EntityWrapper<>(entity));
		taxRate.setDelFlag(CommonConstant.STATUS_NORMAL);
		return taxRateMapper.updateById(taxRate) == 1? true : false;
	}
}
