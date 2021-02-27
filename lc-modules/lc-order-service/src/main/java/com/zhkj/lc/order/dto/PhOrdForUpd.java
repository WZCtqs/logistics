package com.zhkj.lc.order.dto;

import com.zhkj.lc.common.vo.DriverVO;
import com.zhkj.lc.order.model.entity.OrdPickupArrivalAdd;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 更新普货订单状态用
 */
@Data
public class PhOrdForUpd {

    private Integer id;

    private String orderId;

    private String status;

    private String isSend;

    private Date sendTruckDate;

    private String receiptCode;

    private String updateBy;

    private Integer driverId;

    private DriverVO driverVO;

    private String plateNumber;

    private String pickerPhone;

    private String shipperPhone;

    private List<OrdPickupArrivalAdd> arrivalAdds;

    private List<OrdPickupArrivalAdd> pickupAdds;
}
