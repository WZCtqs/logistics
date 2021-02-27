package com.zhkj.lc.common.constant.enums;

import lombok.Getter;
import lombok.Setter;

/**
 * @author shy
 * @date 2018/12/13
 * 支付状态枚举
 */
public enum PayStatusEnum {

    NO("0", "未支付"),
    YES("1", "已支付");

    @Getter
    @Setter
    private String code;
    @Getter
    @Setter
    private String name;

    PayStatusEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }
    public static String getNameByCode(String code){
        for(PayStatusEnum payStatusEnum : PayStatusEnum.values()){
            if(code.equals(payStatusEnum.getCode())){
                return payStatusEnum.getName();
            }
        }
        return null;
    }

}
