package com.zhkj.lc.common.api;

import cn.jiguang.common.resp.BaseResult;
import com.google.gson.annotations.Expose;

public class SendSMSResult extends BaseResult {
	
	@Expose String msg_id;
	public String getMessageId() {
		return msg_id;
	}

}
