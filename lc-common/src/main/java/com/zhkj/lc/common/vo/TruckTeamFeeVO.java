package com.zhkj.lc.common.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class TruckTeamFeeVO {
    private Integer id;

    @JsonFormat(pattern="yyyy-MM")
    private Date feeMonth;

    private Integer truckId;

    private BigDecimal qrcodeFee;

    private BigDecimal rentFee;

    private BigDecimal checkFee;

    private BigDecimal insuranceFee;

    private BigDecimal otherFee;

    private String delFlag;

    private Date createTime;

    private String createBy;

    private Date updateTime;

    private String updateBy;

    private Integer tenantId;

    private List<DriverVO> list;
}
