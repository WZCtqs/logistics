package com.zhkj.lc.common.api;

import cn.jiguang.common.resp.BaseResult;
import com.google.gson.annotations.Expose;


public class ValidSMSResult extends BaseResult {

	@Expose Boolean is_valid;
	
	public Boolean getIsValid() {
		return is_valid;
	}
	
}
