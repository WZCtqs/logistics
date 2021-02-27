package com.zhkj.lc.common.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author cb
 * @description
 * @date 2018/12/18
 */
@Data
public class TanentVo implements Serializable {


    private static final long serialVersionUID = 7676568045987750120L;

    private int tenantId;

    private String tanentName;

    private String shortName;

    private String address;

    private String phone;

    private Date expireTime;

    private String menuIds;

    private String status;

    private String appid;

    private String appsecret;

    private String weixinId;

    private String delFlag;

    private Date createTime;

    private Date updateTime;

    //是否快要过期，默认一周。
    private Integer isExpried;

    private String isPartner;

    private String oldPsw;
}
