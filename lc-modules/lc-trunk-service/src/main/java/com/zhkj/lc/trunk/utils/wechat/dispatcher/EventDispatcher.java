package com.zhkj.lc.trunk.utils.wechat.dispatcher;

import com.zhkj.lc.trunk.utils.wechat.GetUseInfo;
import com.zhkj.lc.trunk.utils.wechat.MessageUtil;
import com.zhkj.lc.trunk.utils.wechat.message.resp.TextMessageResp;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class EventDispatcher {
    @Autowired
    private GetUseInfo getUseInfo;

    private static Logger logger = Logger.getLogger(EventDispatcher.class);
    public String processEvent(Map<String, String> map) {
        String openid=map.get("FromUserName"); //用户openid
        String mpid=map.get("ToUserName");   //公众号原始ID

        //普通文本消息
        TextMessageResp txtmsg=new TextMessageResp();
        txtmsg.setToUserName(openid);
        txtmsg.setFromUserName(mpid);
        txtmsg.setCreateTime(new Date().getTime());
        txtmsg.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
        if (map.get("Event").equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) { //关注事件
            System.out.println("==============这是关注事件！");
            try {
                HashMap<String, String> userinfo=getUseInfo.Openid_userinfo(openid,mpid);
                txtmsg.setContent("你好，"+userinfo.get("nickname"));
                return MessageUtil.textMessageToXml(txtmsg);
            } catch (Exception e) {
                System.out.println("====代码有问题额☺！");
                logger.error(e,e);
            }
        }

        if (map.get("Event").equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) { //取消关注事件
            System.out.println("==============这是取消关注事件！");
            return "";
        }

        if (map.get("Event").equals(MessageUtil.EVENT_TYPE_SCAN)) { //扫描二维码事件
            System.out.println("==============这是扫描二维码事件！");
            return "";
        }

        if (map.get("Event").equals(MessageUtil.EVENT_TYPE_LOCATION)) { //位置上报事件
            System.out.println("==============这是位置上报事件！");
            return "";
        }

        if (map.get("Event").equals(MessageUtil.EVENT_TYPE_CLICK)) { //自定义菜单点击事件
            System.out.println("==============这是自定义菜单点击事件！");
            return "";
        }

        if (map.get("Event").equals(MessageUtil.EVENT_TYPE_VIEW)) { //自定义菜单View事件
            System.out.println("==============这是自定义菜单View事件！");
            return "";
        }

        return null;
    }
}
