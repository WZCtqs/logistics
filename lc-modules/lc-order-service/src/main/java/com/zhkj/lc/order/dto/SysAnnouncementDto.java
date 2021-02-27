package com.zhkj.lc.order.dto;

import com.baomidou.mybatisplus.activerecord.Model;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description 公告实体类
 * @Author ckj
 * @Date 2019/1/3 14:52
 */

@Data
public class SysAnnouncementDto extends Model<SysAnnouncementDto> {

    private static final long serialVersionUID = 1L;

    /**
     * 公告id
     */

    private Integer announcementId;
    /**
     * 公告内容
     */

    private String content;
    /**
     * 公告类型(0公共，1个人)
     */

    private String type;
    /**
     * 公告接收人的id
     */

    private Integer driverOwerId;

    /**
     * 公告接收人名字
     */

    private String driverName;

    /**
     * 删除标志（0存在 1删除）
     */

    private String delFlag;
    /**
     * 创建者
     */

    private String createBy;
    /**
     * 创建时间
     */

    private Date createTime;
    /**
     * 更新者
     */

    private String updateBy;
    /**
     * 更新时间
     */

    private Date updateTime;
    /**
     * 备注信息
     */

    private String remarks;
    /**
     * 租户id
     */

    private Integer tenantId;


    @Override
    protected Serializable pkVal() {
        return this.announcementId;
    }
}
