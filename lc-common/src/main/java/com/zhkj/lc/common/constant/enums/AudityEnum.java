package com.zhkj.lc.common.constant.enums;

import lombok.Getter;
import lombok.Setter;

/**
 * @author shy
 * @date 2018/12/13
 * 审核状态枚举
 */
public enum AudityEnum {

    NO("0", "未审核"),
    SUCCESS("1", "审核通过"),
    FAIL("2", "审核不通过");

    @Getter
    @Setter
    private String code;
    @Getter
    @Setter
    private String name;

    AudityEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }
    public static String getNameByCode(String code){
        for(AudityEnum audityEnum : AudityEnum.values()){
            if(code.equals(audityEnum.getCode())){
                return audityEnum.getName();
            }
        }
        return null;
    }

}
