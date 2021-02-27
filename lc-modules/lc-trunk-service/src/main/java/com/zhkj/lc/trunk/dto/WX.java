package com.zhkj.lc.trunk.dto;

import lombok.Data;

/**
 * @Auther: HP
 * @Date: 2018/12/28 14:09
 * @Description:
 */
@Data
public class WX {

    /*用户唯一标识*/
    private String openid;
    /*会话密钥*/
    private String session_key;
    /*用户在开放平台的唯一标识符，在满足 UnionID 下发条件的情况下会返回，详见 UnionID 机制说明。*/
    private String unionid;
    /*错误码*/
    private String errcode;
    /*错误信息*/
    private String errmsg;
}
