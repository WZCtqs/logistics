package com.zhkj.lc.trunk.utils.wechat.dispatcher;

import com.zhkj.lc.trunk.utils.wechat.MessageUtil;
import com.zhkj.lc.trunk.utils.wechat.message.resp.TextMessageResp;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
public class MsgDispatcher {
    public String processMessage(Map<String, String> map) {
        String openid=map.get("FromUserName"); //用户openid
        String mpid=map.get("ToUserName");   //公众号原始ID

        if (map.get("MsgType").equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) { // 文本消息
            //普通文本消息
            TextMessageResp txtmsg=new TextMessageResp();
            txtmsg.setToUserName(openid);
            txtmsg.setFromUserName(mpid);
            txtmsg.setCreateTime(new Date().getTime());
            txtmsg.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);

            System.out.println("==============这是文本消息！");
            txtmsg.setContent("你好！");
            return MessageUtil.textMessageToXml(txtmsg);
        }

        if (map.get("MsgType").equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) { // 图片消息
            System.out.println("==============这是图片消息！");
            return "";
        }


        if (map.get("MsgType").equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) { // 链接消息
            System.out.println("==============这是链接消息！");
            return "";
        }


        if (map.get("MsgType").equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) { // 位置消息
            System.out.println("==============这是位置消息！");
            return "";
        }


        if (map.get("MsgType").equals(MessageUtil.REQ_MESSAGE_TYPE_VIDEO)) { // 视频消息
            System.out.println("==============这是视频消息！");
            return "";
        }


        if (map.get("MsgType").equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) { // 语音消息
            System.out.println("==============这是语音消息！");
            return "";
        }

        return null;
    }
}
