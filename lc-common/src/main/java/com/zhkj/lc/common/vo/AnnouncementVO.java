package com.zhkj.lc.common.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description 公告实体类
 * @Author ckj
 * @Date 2019/1/3 14:52
 */
@Data
public class AnnouncementVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 公告id
     */
    private Integer announcementId;
    /**
     * 公告标题
     */
    private String title;
    /**
     * 公告内容
     */
    private String content;
    /**
     * 公告类型(0个人，1公共，2后台)
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
     * 查看状态
     */
    private String checkout;

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
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;
    /**
     * 更新者
     */
    private String updateBy;
    /**
     * 更新时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateTime;
    /**
     * 备注信息
     */
    private String remarks;
    /**
     * 租户id
     */
    private Integer tenantId;

}
