package com.zhkj.lc.trunk.utils.wechat;

import com.zhkj.lc.common.vo.TanentVo;
import com.zhkj.lc.trunk.feign.TanentFegin;
import com.zhkj.lc.trunk.utils.wechat.start.GlobalConstants;
import lombok.AllArgsConstructor;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;

@Component
@AllArgsConstructor
public class GetUseInfo {

    @Autowired
    private TanentFegin tanentFegin;

    private final RedisTemplate redisTemplate;
    /**
     * @Description: 通过openid获取用户微信信息
     * @param @param openid
     * @param @return
     * @param @throws Exception
     */
    public HashMap<String, String> Openid_userinfo(String openid,String mpid)
            throws Exception {

        TanentVo tanent = tanentFegin.selectByWA(mpid);

        HashMap<String, String> params = new HashMap<String, String>();
        String access_token = redisTemplate.opsForValue().get("access_token"+tanent.getTenantId()).toString();
        System.out.println(access_token);
        params.put("access_token", access_token);  //定时器中获取到的token
        params.put("openid", openid);  //需要获取的用户的openid
        params.put("lang", "zh_CN");
        String subscribers = HttpUtil.sendGet("https://api.weixin.qq.com/cgi-bin/user/info", params);
        System.out.println(subscribers);
        params.clear();
        //这里返回参数只取了昵称、头像、和性别
        params.put("nickname",
                JSONObject.fromObject(subscribers).getString("nickname")); //昵称
        params.put("headimgurl",
                JSONObject.fromObject(subscribers).getString("headimgurl"));  //图像
        params.put("sex", JSONObject.fromObject(subscribers).getString("sex"));  //性别
        return params;

    }
}
