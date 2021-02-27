package com.zhkj.lc.trunk.utils.wechat;

import com.zhkj.lc.common.constant.SecurityConstants;
import com.zhkj.lc.common.vo.TanentVo;
import com.zhkj.lc.trunk.feign.TanentFegin;
import com.zhkj.lc.trunk.utils.wechat.start.GlobalConstants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor
@Component
public class WeChatTask {
    @Autowired
    private TanentFegin tanentFegin;

    private final RedisTemplate redisTemplate;



    /**
     * @Description: 任务执行体
     * @param @throws Exception
     */
    @Scheduled(initialDelay = 1000, fixedDelay = 1100000)
    public void getToken_getTicket() throws Exception {
        List<TanentVo> tanents = tanentFegin.selectTanentList();
        for(TanentVo tanent : tanents){
            if(null != tanent.getAppid() && null != tanent.getAppsecret()){
                Map<String, String> params = new HashMap<String, String>();
                //获取token执行体
                params.put("grant_type", "client_credential");
                params.put("appid", tanent.getAppid());
                params.put("secret", tanent.getAppsecret());
                String jstoken = HttpUtil.sendGet("https://api.weixin.qq.com/cgi-bin/token", params);
                try{
                    String access_token = JSONObject.fromObject(jstoken).getString(
                            "access_token");
                    // 获取到token并赋值保存
                    redisTemplate.opsForValue().set("access_token"+tanent.getTenantId(), access_token);

                    System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
                            +"租户"+tanent.getTenantId()+"  token为=============================="+
                            redisTemplate.opsForValue().get("access_token"+tanent.getTenantId()));
                }catch (Exception e){
                    System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
                            +"租户"+tanent.getTenantId()+"  获取token失败,请检查appid/appsecret是否与公众号一致");
                }

            }
        }
    }


    /*public void getToken_getTicket() throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        //获取token执行体
        params.put("grant_type", "client_credential");
        params.put("appid", GlobalConstants.getInterfaceUrl("appid"));
        params.put("secret", GlobalConstants.getInterfaceUrl("AppSecret"));
        String jstoken = HttpUtil.sendGet(
                GlobalConstants.getInterfaceUrl("tokenUrl"), params);
        String access_token = JSONObject.fromObject(jstoken).getString(
                "access_token"); // 获取到token并赋值保存
        GlobalConstants.interfaceUrlProperties.put("access_token", access_token);
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"token为=============================="+access_token);
    }*/
}
