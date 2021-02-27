package com.zhkj.lc.order.model.entity;

import lombok.Data;

import java.util.Date;

@Data
public class OrdExConDTO {
    private String orderId;

    private String fromDate;

    private String toDate;

    private Integer tenantId;
}
