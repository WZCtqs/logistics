package com.zhkj.lc.trunk.utils.wechat.message.req;

/**
 * ClassName: TextMessageResp
 * @Description: 文本消息消息体
 * @author Hester
 * @Date 20190109
 */
public class TextMessage extends BaseMessage {
    // 消息内容
    private String Content;

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
}
