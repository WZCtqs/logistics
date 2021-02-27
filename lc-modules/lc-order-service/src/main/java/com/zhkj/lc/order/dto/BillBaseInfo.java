package com.zhkj.lc.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 小程序获取待办的账单数量和正在审核的账单数量
 */
@Data
@ApiModel(value = "小程序获取待办的账单数量和正在审核的账单数量")
public class BillBaseInfo {

    /**
     * 待确认数量
     */
    @ApiModelProperty(value = "待确认数量")
    private int newBillNum;
    /**
     * 反馈处理中
     */
    @ApiModelProperty(value = "反馈处理中数量")
    private int checkBillNum;
}
