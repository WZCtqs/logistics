package com.zhkj.lc.order.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhkj.lc.common.vo.CustomerVO;
import com.zhkj.lc.common.vo.DriverVO;
import com.zhkj.lc.order.model.entity.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 订单管理
 * </p>
 *
 * @author wzc
 * @since 2018-12-07
 */
@Data
@TableName("ord_order")
public class OrdOrderForApp extends OrdOrder {

    private static final long serialVersionUID = 1L;

    //提货凭证
    @ApiModelProperty(value = "提货凭证")
    private String[] pickGoodsImgList;

    //签收凭证
    @ApiModelProperty(value = "签收凭证")
    private String[] receiptImgList;

    //提箱凭证
    @ApiModelProperty(value = "提箱凭证")
    private String[] pickCnImgList;

    //还箱凭证
    @ApiModelProperty(value = "还箱凭证")
    private String[] returnCnImgList;

    //上传凭证
    @ApiModelProperty(value = "上传凭证")
    private String[] fileList;


}
