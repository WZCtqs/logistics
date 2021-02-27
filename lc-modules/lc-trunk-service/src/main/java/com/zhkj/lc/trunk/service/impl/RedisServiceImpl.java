package com.zhkj.lc.trunk.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xiaoleilu.hutool.util.RandomUtil;
import com.zhkj.lc.common.api.YunPianSMSUtils;
import com.zhkj.lc.common.constant.SecurityConstants;
import com.zhkj.lc.common.util.R;
import com.zhkj.lc.trunk.service.RedisService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: HP
 * @Date: 2019/4/11 15:13
 * @Description:
 */
@Slf4j
@Service
@AllArgsConstructor
public class RedisServiceImpl implements RedisService {

    private final RedisTemplate redisTemplate;

    /**
     * 发送验证码
     * <p>
     * 1. 先去redis 查询是否 60S内已经发送
     * 2. 未发送： 判断手机号是否存 ? false :产生4位数字  手机号-验证码
     * 3. 发往消息中心-》发送信息
     * 4. 保存redis
     *
     * @param mobile 手机号
     * @return true、false
     */
    @Override
    public R<Boolean> sendSmsCode(String mobile) throws IOException {
        Object tempCode = redisTemplate.opsForValue().get(SecurityConstants.DEFAULT_CODE_KEY + mobile);
        if (tempCode != null) {
            log.error("用户:{}验证码未失效{}", mobile, tempCode);
            return new R<>(false, "验证码未失效，请失效后再次申请");
        }
//        SysUser params = new SysUser();
//        params.setPhone(mobile);
//        List<SysUser> userList = this.selectList(new EntityWrapper<>(params));
//
//        if (CollectionUtil.isEmpty(userList)) {
//            log.error("根据用户手机号{}查询用户为空", mobile);
//            return new R<>(false, "手机号不存在");
//        }
        String code = RandomUtil.randomNumbers(6);
        JSONObject contextJson = new JSONObject();
        contextJson.put("code", code);
        contextJson.put("product", "lc4Cloud");
        log.info("短信发送请求消息中心 -> 手机号:{} -> 验证码：{}", mobile, code);
        /*发送短信*/
        YunPianSMSUtils.sendWechatCode(mobile,code);

        redisTemplate.opsForValue().set(SecurityConstants.DEFAULT_CODE_KEY + mobile, code, SecurityConstants.DEFAULT_IMAGE_EXPIRE, TimeUnit.SECONDS);
        return new R<>(true);
    }
}
