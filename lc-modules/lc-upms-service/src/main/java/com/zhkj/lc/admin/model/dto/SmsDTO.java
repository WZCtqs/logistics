package com.zhkj.lc.admin.model.dto;

import com.zhkj.lc.admin.model.entity.SysSmsTemp;
import lombok.Data;

import java.util.List;

/**
 * @Auther: HP
 * @Date: 2019/2/21 10:06
 * @Description:
 */
@Data
public class SmsDTO {

    private String sys;

    private String isSend;

    private List<SysSmsTemp> tempList;

    private Integer tenantId;

    private String updateBy;

}
