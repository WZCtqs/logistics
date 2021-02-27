package com.zhkj.lc.common.vo;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import lombok.Data;

import java.util.Date;

/**
 * @Auther: wzc
 * @Date: 2018/12/10 08:48
 * @Description: 合同管理
 */
@Data
public class ContractVO {

    /**
     * 合同编号
     */
    private String contractNumber;
    /**
     * 合同名称
     */
    private String contractName;
    /**
     * 合同类型
     */
    private String contractType;
    /**
     * 客户id
     */
    private Integer customerId;
    /**
     * 联系人
     */
    private String contact;
    /**
     * 业务员
     */
    private String saleman;
    /**
     * 签订时间
     */
    private Date signDate;
    /**
     * 到期时间
     */
    private Date expiryDate;
    /**
     * 提醒时间
     */
    private Date remindDate;
    /**
     * 合同状态
     */
    private String status;
    /**
     * 合同文件
     */
    private String contractFile;
    /**
     * 备注
     */
    private String remark;

}
