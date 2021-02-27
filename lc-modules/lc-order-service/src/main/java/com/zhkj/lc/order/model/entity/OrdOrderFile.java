package com.zhkj.lc.order.model.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 订单文件表(派车单、附件、运输拍照)
 * </p>
 *
 * @author wzc
 * @since 2018-12-07
 */
@Data
@TableName("ord_order_file")
public class OrdOrderFile extends Model<OrdOrderFile> {

    private static final long serialVersionUID = 1L;

    /**
     * 订单编号
     */
    @TableId("order_id")
    private String orderId;
    /**
     * 派车单
     */
    @TableField("send_truck_file")
    private String sendTruckFile;
    /**
     * 提箱单原件
     */
    @TableField("pickup_cn_file")
    private String pickupCnFile;
    /**
     * 公章原件
     */
    @TableField("official_seal")
    private String officialSeal;
    /**
     * 公章提箱单
     */
    @TableField("pickup_cn_newfile")
    private String pickupCnNewfile;
    /**
     * 上传附件
     */
    @TableField("file_a")
    private String fileA;
    /**
     * 上传附件
     */
    @TableField("file_b")
    private String fileB;
    /**
     * 上传附件
     */
    @TableField("file_c")
    private String fileC;
    /**
     * 上传附件
     */
    @TableField("file_d")
    private String fileD;
    /**
     * 上传附件
     */
    @TableField("file_e")
    private String fileE;
    /**
     * 上传附件
     */
    @TableField("file_f")
    private String fileF;
    /**
     * 上传附件
     */
    @TableField("file_g")
    private String fileG;
    /**
     * 上传附件
     */
    @TableField("file_h")
    private String fileH;
    /**
     * 上传附件
     */
    @TableField("file_i")
    private String fileI;
    /**
     * 上传附件
     */
    @TableField("file_j")
    private String fileJ;
    /**
     * 租户id
     */
    @TableField("tenant_id")
    private Integer tenantId;

    /*其他原件url*/
    private String[] otherFiles;

    @Override
    protected Serializable pkVal() {
        return this.orderId;
    }

    @Override
    public String toString() {
        return "OrdOrderFile{" +
        "orderId=" + orderId +
        ", sendTruckFile=" + sendTruckFile +
        ", fileA=" + fileA +
        ", fileB=" + fileB +
        ", fileC=" + fileC +
        ", fileD=" + fileD +
        ", fileE=" + fileE +
        ", fileF=" + fileF +
        ", fileG=" + fileG +
        ", fileH=" + fileH +
        ", fileI=" + fileI +
        ", fileJ=" + fileJ +
        ", tenantId=" + tenantId +
        "}";
    }
}
