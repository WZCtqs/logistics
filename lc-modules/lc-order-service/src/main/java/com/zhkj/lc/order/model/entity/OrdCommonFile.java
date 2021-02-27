package com.zhkj.lc.order.model.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author cb
 * @since 2019-01-03
 */
@Data
@TableName("ord_common_file")
public class OrdCommonFile extends Model<OrdCommonFile> {

    private static final long serialVersionUID = 1L;

    /**
     * 普货订单编号
     */
    @TableId("order_id")
    private String orderId;
    /**
     * 签收单url
     */
    @TableField("pick_file")
    private String pickFile;
    /**
     * 派车单url
     */
    @TableField("send_truck_file")
    private String sendTruckFile;
    /**
     * 签收单
     */
    @TableField("receipt_note")
    private String receiptNote;
    /**
     * 装车照片
     */
    @TableField("entrucking_img")
    private String entruckingImg;
    /**
     * 温度照片
     */
    @TableField("temp_img")
    private String tempImg;
    /**
     * 车门照片
     */
    @TableField("car_door_img")
    private String carDoorImg;
    /**
     * 租户id
     */
    @TableField("tenant_id")
    private Integer tenantId;



    @Override
    protected Serializable pkVal() {
        return this.orderId;
    }

    @Override
    public String toString() {
        return "OrdCommonFile{" +
        ", orderId=" + orderId +
        ", pickFile=" + pickFile +
        ", sendTruckFile=" + sendTruckFile +
        ", receiptNote=" + receiptNote +
        ", entruckingImg=" + entruckingImg +
        ", tempImg=" + tempImg +
        ", carDoorImg=" + carDoorImg +

        "}";
    }
}
