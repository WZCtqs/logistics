package com.zhkj.lc.common.api.common;

/**
 * 订单文件类型枚举
 */
public enum FileType {

    //  集装箱
    TXD("提箱单",1),
    TXDGZ("提箱单公章",2),
    TXPZ("提箱凭证",3),
    HXPZ("还箱凭证",4),
    HXD("还箱单",5),
    //通用
    THPZ("提货凭证",6),
    QSPZ("签收凭证",7),
    //异常费用
    YCLQF("异常路桥费",8),
    YCFKF("异常放空费",9),
    YCYCF("异常压车费",10),
    //异常情况
    YCQK("异常情况",11);


    private String name;
    private int index;

    // 构造方法
    private FileType(String name, int index) {
        this.name = name;
        this.index = index;
    }

    // 普通方法
    public static String getName(int index) {
        for (FileType c : FileType.values()) {
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
