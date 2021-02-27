package com.zhkj.lc.trunk.service;


import com.baomidou.mybatisplus.service.IService;
import com.zhkj.lc.trunk.model.TaxRate;

/**
 * 税率 服务层
 * 
 * @author tqs
 * @date 2019-05-06
 */
public interface ITaxRateService extends IService<TaxRate>
{
	boolean updateById(TaxRate taxRate);

	boolean setDefaultRate(TaxRate taxRate);
}
