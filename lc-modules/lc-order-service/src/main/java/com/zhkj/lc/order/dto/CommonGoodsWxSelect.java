package com.zhkj.lc.order.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhkj.lc.common.vo.GPSVO;
import com.zhkj.lc.order.model.entity.OrdOrderLogistics;
import com.zhkj.lc.order.model.entity.OrdPickupArrivalAdd;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *普货订单管理bean,不含运踪信息，异常情况，费用
 * @author cb
 * @since 2019-01-05
 */


@Data
@ApiModel(value = "普货订单公众号端数据实体")
public class CommonGoodsWxSelect implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    /*普货订单编号*/
    private String morderId;
    /*订单状态*/
    private String status;
    /*发货城市*/
    private String sendGoodsPlace;
    /*到货城市*/
    private String pickGoodsPlace;

    /*创建事件*/
    private Date createTime;

    /*签收码*/
    private String receiptCode;

    private Integer mtenantId;

    /*运踪状态*/
    private List<OrdOrderLogistics> logistics;

    private List<OrdPickupArrivalAdd> arrivalAdds;

    private List<OrdPickupArrivalAdd> pickupAdds;

    private List<GPSVO.Gps> gps;

    private Integer flag;  // 0-我发的    1-我收的
}
