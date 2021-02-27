/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the lc4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */

package com.zhkj.lc.common.constant;

/**
 * @author lengleng
 * @date 2017/10/29
 */
public interface CommonConstant {
    /**
     * token请求头名称
     */
    String REQ_HEADER = "Authorization";

    /**
     * token分割符
     */
    String TOKEN_SPLIT = "Bearer ";

    /**
     * jwt签名
     */
    String SIGN_KEY = "lc";
    /**
     * 删除
     */
    String STATUS_DEL = "1";
    /**
     * 正常
     */
    String STATUS_NORMAL = "0";

    String TANENT_NORMAL="1";


    String TANENT_UNNORMAL="0";

    /**
     * 锁定
     */
    String STATUS_LOCK = "9";

    /**
     * 菜单
     */
    String MENU = "0";

    /**
     * 按钮
     */
    String BUTTON = "1";

    /**
     * 删除标记
     */
    String DEL_FLAG = "del_flag";

    /**
     * 编码
     */
    String UTF8 = "UTF-8";

    /**
     * JSON 资源
     */
    String CONTENT_TYPE = "application/json; charset=utf-8";

    /**
     * 阿里大鱼
     */
    String ALIYUN_SMS = "aliyun_sms";

    /**
     * 路由信息Redis保存的key
     */
    String ROUTE_KEY = "_ROUTE_KEY";

    /**
     * 上游系统订单-计划
     */
    String ORDER_JH = "01";

    /**
     * 上游系统订单-确认
     */
    String ORDER_QR = "02";

    /**
     * 上游系统订单-取消
     */
    String ORDER_QX = "03";

    /**
     * 订单草稿状态
     */
    String ORDER_CG = "0";

    /**
     * 待接单
     */
    String ORDER_DJD = "2";
    /**
     * 待提箱
     */
    String ORDER_DTX = "3";

    /**
     * 提箱中
     */
    String ORDER_TXZ = "4";
    /**
     * 待提货
     */
    String ORDER_DTH = "5";
    /**
     * 提货中
     */
    String ORDER_THZ = "6";
    /**
     * 运输中
     */
    String ORDER_YSZ = "7";
    /**
     * 签收中
     */
    String ORDER_DQS = "8";
    /**
     * 已签收
     */
    String ORDER_YQS = "9";

    /**
     * 待还箱
     */
    String ORDER_DHX = "10";
    /**
     * 已还箱
     */
    String ORDER_YHX = "11";
    /**
     * 已发送
     */
    String  IS_SEND = "1";
    /**
     * 订单派车
     */
    String PC_MSG = "订单正在派车中";

    /**
     * 无异常
     */
    String WYC = "0";

    /**
     * 异常费用
     */
    String YCFY = "1";
    /**
     * 异常情况
     */
    String YCQK = "2";
    /**
     * 异常费用和异常情况
     */
    String YC_ALL = "3";

    /**
     * 司机请假
     */
    String SZQJ = "0";
    /**
     * 司机在途
     */
    String SJZT = "1";
    /**
     * 司机空闲
     */
    String SJKX = "2";


    /**
     * 账单未分配
     */
    String WFP = "0";
    /**
     * 账单未发送
     */
    String WFS = "1";
    /**
     * 账单已发送
     */
    String YFS = "2";
    /**
     * 司机已反馈
     */
    String YFK = "3";
    /**
     * 司机已确认
     */
    String YQR = "4";
    /**
     * 账单已结算
     */
    String YJS = "5";

    /**
     * 签收码提醒（货主）
     */
    Integer TPL_ID_RCODE = 2880796;
    /**
     * 新订单提醒（司机）
     */
    Integer TPL_ID_NEW = 2859854;
    /**
     * 已派车提醒（货主）
     */
    Integer TPL_ID_SEND = 2859850;
    /**
     * 账单提醒（司机）
     */
    Integer TPL_ID_BILL = 2859848;
}
