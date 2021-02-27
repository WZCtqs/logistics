package com.zhkj.lc.common.vo;



import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotations.TableField;
import com.zhkj.lc.common.util.StringUtils;
import io.swagger.annotations.ApiModelProperty;
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
public class OilMajorVO implements Serializable {

    private static final long serialVersionUID = 1L;

	public OilMajorVO() {
	}

	public OilMajorVO(String imMajorNum) {
		this.imMajorNum = imMajorNum;
	}


	@Excel(name = "主卡名")
	private String majorName;

	//todo 列名问题
	@Excel(name = "主卡号")
    private String imMajorNum;


	@Excel(name = "油卡号")
    private String imOilCardNum;

	@Excel(name = "持卡人")
    private String ownerName;


	@Excel(name = "油卡归属公司")
	private String company;

	@Excel(name = "车牌号")
	private String carNum;

	@Excel(name = "司机")
	private String driver;

	@Override
	public boolean equals(Object obj) {
		OilMajorVO u = (OilMajorVO) obj;
		if (StringUtils.isEmpty(u.getImMajorNum())){
			return false;
		}
		return imMajorNum.equals(u.getImMajorNum());
	}

}
