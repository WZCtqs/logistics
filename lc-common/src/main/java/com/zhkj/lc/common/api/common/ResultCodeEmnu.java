package com.zhkj.lc.common.api.common;

/**
 * @Auther: HP
 * @Date: 2019/1/16 09:43
 * @Description:
 */
public enum ResultCodeEmnu {

    ZONE("请求成功",50000),
    ONE("auth为空",50001),
    TWO("auth鉴权失败",50002),
    THREE("身为空",50003),
    FOUE("手机号码为空",50004),
    FIVE("模版ID为空",50005),
    SIX("手机号码无效",50006),
    SEVEN("身体无效",50007),
    EIGHT("未开通短信业务",50008),
    NINE("下发超频",50009),
    TEN("验证码无效",50010),
    ELEVEN("验证码过期",50011),
    TWELVE("验证码已验证通过",50012),
    THIRTEEN("模版ID无效",50013),
    FOURTEEN("可发短信余量不足",50014),
    FIFTEEN("验证码为空",50015),
    SIXTEEN("API不存在",50016),
    SEVENTEEN("媒体类型不支持",50017),
    EIGHTEEN("请求方法不支持",50018),
    NINETEEN("服务端异常",50019),
    TWENTY("模板审核中",50020),
    TWENTY_ONE("模板审核未通过",50021),
    TWENTY_TWO("模板中参数未全部替换",50022),
    TWENTY_THREE("参数为空",50023),
    TWENTY_FOUE("手机号码已退订",50024),
    TWENTY_FIVE("该API不支持此模版类型",50025),
    TWENTY_SIX("msg_id无效",50026),
    THIRTY("收件人为空",50030),
    THIRTY_ONE("收件人短信接收者数量超过1000",50031),
    THIRTY_FOUR("重复发送",50034),
    THIRTY_FIVE("非法IP请求",50035),
    THIRTY_SIX("应用被列为黑名单",50036),
    THIRTY_SEVEN("短信内容存在敏感词汇",50037),
    THIRTY_EIGHT("短信内容长度错误，文本短信长度不超过350个字，语音短信验证码长度4〜8数字",50038),
    THIRTY_NINE("语音验证码内容错误，验证码仅支持数字",50039),
    FORTY("语音验证码播报语言类型错误",50040),
    FORTY_ONE("验证码有效期错误",50041),
    FIFTY_FOUR("短信正文不能含有特殊符号，如【】",50054);

    private String name;
    private int index;

    // 构造方法
    private ResultCodeEmnu(String name, int index) {
        this.name = name;
        this.index = index;
    }

    // 普通方法
    public static String getName(int index) {
        for (ResultCodeEmnu c : ResultCodeEmnu.values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return null;
    }
    // get set 方法
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
