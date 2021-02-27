package com.zhkj.lc.trunk.utils.wechat.message.req;

/**
 * @Desceiption: 图片消息实体
 * @author Hester
 * @Date 20190109
 */
public class ImageMessage extends BaseMessage {
    // 图片链接
    private String PicUrl;

    public String getPicUrl() {
        return PicUrl;
    }

    public void setPicUrl(String picUrl) {
        PicUrl = picUrl;
    }
}
