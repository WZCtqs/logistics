package com.zhkj.lc.common.vo;

import com.zhkj.lc.common.annotation.Excel;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *  主卡基础表
 * </p>
 *
 * @author wzc
 * @since 2019-02-11
 */
@Data
public class OilCardExportVO implements Serializable {

    private static final long serialVersionUID = 1L;

	@Excel(name = "主卡号")
    private String MajorNum;

	@Excel(name = "副卡号")
    private String OilCardNum;

	@Excel(name = "状态")
	private String cardStatus;

	@Excel(name = "类型")
	private String cardType;

	private Integer tenantId;

}
