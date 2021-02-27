package com.zhkj.lc.admin.model.dto;

import com.baomidou.mybatisplus.annotations.TableField;
import lombok.Data;

import java.util.Date;

/**
 * @author cb
 * @description 新增租户信息
 * @date 2018/12/13
 */
@Data
public class TanentDTO {
    //租户公司名称
    private String tanentName;

    //租户公司简称
    private String shortName;

    /**
     * 公司地址
     */
    private String address;


    /**
     * 租户手机号
     */
    private String phone;

    /**
     * 到期时间
     */
    private Date expireTime;
    /**
     * 开通的功能模块
     */
    private String menuIds;

    private String isPartner;

    private String appid;

    private String appsecret;

    private String weixinId;
}
