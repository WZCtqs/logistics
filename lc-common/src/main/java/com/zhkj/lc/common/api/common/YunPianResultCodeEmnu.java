package com.zhkj.lc.common.api.common;

/**
 * @Auther: HP
 * @Date: 2019/1/16 09:43
 * @Description:
 */
public enum YunPianResultCodeEmnu {

    ZONE("请求成功",0),
    ONE("请求参数缺失",1),
    TWO("手机号格式不正确",2),
    THREE("账户余额不足",3),
    FOUE("关键词屏蔽",4),
    EIGHT("同一手机号30秒内重复提交相同的内容",8),
    NINE("同一手机号5分钟内重复提交相同的内容超过3次",9),
    TEN("手机号防骚扰名单过滤",10),
    FIFTEEN("签名不匹配",15),
    SIXTEEN("签名格式不正确",16),
    SEVENTEEN("24小时内同一手机号发送次数超过限制",17),
    NINETEEN("请求已失效",19),
    TWENTY("不支持的国家地区",20),
    TWENTY_TWO("1小时内同一手机号发送次数超过限制",22),
    TWENTY_THREE("运营商错误，请稍后重试",28),
    TWENTY_FOUE("未知异常，请稍后重试",-50),
    TWENTY_FIVE("系统繁忙，请稍后重试",-51),
    TWENTY_SIX("提交短信失败，请稍后重试",-53);

    private String name;
    private int index;

    // 构造方法
    private YunPianResultCodeEmnu(String name, int index) {
        this.name = name;
        this.index = index;
    }

    // 普通方法
    public static String getName(int index) {
        for (YunPianResultCodeEmnu c : YunPianResultCodeEmnu.values()) {
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
