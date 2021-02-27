package com.zhkj.lc.trunk.service;

import com.zhkj.lc.common.util.R;

import java.io.IOException;

/**
 * @Auther: HP
 * @Date: 2019/4/11 15:12
 * @Description:
 */
public interface RedisService {

    R<Boolean> sendSmsCode(String mobile) throws IOException;

}
