package com.zhkj.lc.oilcard.controller;


import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.util.PoiPublicUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.zhkj.lc.common.util.*;
import com.zhkj.lc.common.util.excel.ExcelUtil;
import com.zhkj.lc.common.vo.OilCardExportVO;
import com.zhkj.lc.common.vo.OilMajorVO;
import com.zhkj.lc.common.vo.ReChargeVO;
import com.zhkj.lc.common.web.BaseController;
import com.zhkj.lc.oilcard.model.OilApply;
import com.zhkj.lc.oilcard.model.OilCard;
import com.zhkj.lc.oilcard.model.OilMajor;
import com.zhkj.lc.oilcard.service.IOilCardService;
import com.zhkj.lc.oilcard.service.IOilMajorService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.*;

import com.zhkj.lc.oilcard.util.CommonUtils;


@Api(description = "油卡管理")
@RestController
@RequestMapping("/oilCardManage")
public class OilCardManagerController extends BaseController {

	@Autowired
	IOilMajorService oilMajorService;
	@Autowired
	IOilCardService oilCardService;
	@Resource
	private ResourceLoader resourceLoader;

	@PostMapping("/importExcel")
	@Transactional
	@ApiOperation(value = "导入Excel",notes = "上传模板为中石油导出的Excel表格")
	public R<Boolean> importExcel(@RequestParam("file") MultipartFile file) throws Exception{
		if(file.isEmpty()){
			return new R<>(Boolean.FALSE,"上传文件为空！请重新选择...");
			}
	    Integer tenantId = getTenantId();
		//从数据库里查出当前租户的主油卡
		List<OilMajor> oilMajors = oilMajorService.selectOilMajorNumber(tenantId);
		//从数据库查出当前租户的副油卡数据
		List<OilCard> oilCards = oilCardService.selectAllOilCardNumber(tenantId);

		//将oilMajor实体类转成oilMajorVO
		List<OilMajorVO> oilMajorVOs = new ArrayList<>();
		oilMajors.forEach(mo->{
			oilMajorVOs.add(new OilMajorVO(mo.getMajorNumber()));
		});

		ImportParams params = new ImportParams();
		params.setHeadRows(1);
		params.setTitleRows(1);
		InputStream inputStream = file.getInputStream();
		System.out.println(inputStream);
		List<OilMajorVO> list = ExcelImportUtil.importExcel(inputStream, OilMajorVO.class,params);
		List<OilMajorVO> imOilMajorVOS = new ArrayList<>();
		List<OilMajorVO> imOilCardVOS = new ArrayList<>();
		//List<OilMajorVO> imNewOilCardVOS = new ArrayList<>();
		Set set = new HashSet();

		list.forEach(oilMajorVO -> {
			if (!StringUtils.isEmpty(oilMajorVO.getImMajorNum())){
				imOilCardVOS.add(oilMajorVO);
				imOilMajorVOS.add(oilMajorVO);
			}
		});
		//主卡号先去重
		for(int i= 0;i<imOilMajorVOS.size()-1;i++){
			for(int j=imOilMajorVOS.size()-1;j>i;j--){
				if(imOilMajorVOS.get(j).getImMajorNum().equals(imOilMajorVOS.get(i).getImMajorNum())){
					imOilMajorVOS.remove(j);
				}
			}
		}

		//System.out.println("-----------"+imNewOilCardVOS.size());

		//导入的主油卡信息与查出的比较取差集
		imOilMajorVOS.removeAll(oilMajorVOs);
		System.out.println("-----------"+imOilMajorVOS.size());
		//<-----------------插入主油卡数据start---------------->
		List<OilMajor> majors = new ArrayList<>();
		imOilMajorVOS.forEach(vo->{
			majors.add(new OilMajor(vo.getImMajorNum(),vo.getCompany(),tenantId,vo.getMajorName()));
		});
		if(majors.size()>0) {
			boolean isImMajorsSave = oilMajorService.insertBatch(majors);
			if (!isImMajorsSave) {
				return new R<>(Boolean.FALSE, "主油卡信息插入失败");
			}
		}

		//<-----------------插入主油卡数据end---------------->

		//<-----------------插入副油卡数据start---------------->
		//将oilMajorVO实体类转成oilcard
		List<OilCard> oilCardVOs = new ArrayList<>();
		imOilCardVOS.forEach(oilMajorVO -> {
			oilCardVOs.add(new OilCard(oilMajorVO.getImOilCardNum(),oilMajorVO.getImMajorNum()));
		});
		for(int i= 0;i<oilCardVOs.size()-1;i++){
			for(int j=oilCardVOs.size()-1;j>i;j--){
                      if(oilCardVOs.get(j).getOilCardNumber().equals(oilCardVOs.get(i).getOilCardNumber())){
						  oilCardVOs.remove(j);
					  }
			}
		}
		oilCardVOs.removeAll(oilCards);
		oilCardVOs.forEach(vo->{

			OilMajor oilMajor = oilMajorService.findOne(vo.getMajorNumber(),tenantId);
			vo.setMajorId(oilMajor.getMajorId());
			vo.setTenantId(tenantId);
			vo.setCreateTime(new Date());
			vo.setCardStatus("未使用");
		});
		if(oilCardVOs.size()<=0){
			return new R<>(Boolean.FALSE,"油卡号重复无法插入！");
		}
		if(oilCardVOs.size()>0){
			boolean isImOilcardSave = oilCardService.insertBatch(oilCardVOs);
			if (!isImOilcardSave) {
				return new R<>(Boolean.FALSE,"副油卡信息插入失败");
			}
		}
		//<-----------------插入副油卡数据end---------------->
		return new R<>(Boolean.TRUE,"油卡信息导入完成");
	}

	@GetMapping("/getOilInfoByPage")
	@ApiOperation(value = "分页获取主副油卡信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "majorNum",value = "主油卡号",paramType = "query",dataType = "String"),
			@ApiImplicitParam(name = "oilCardNum", value = "富有卡号",paramType = "query",dataType = "String")
	})
	public Page<OilCard> getOilInfoByPage(@RequestParam Map<String, Object> params,OilCard oilCard){
		OilCard o = new OilCard();

		OilMajor m = oilMajorService.findOne(oilCard.getMajorNumber(),getTenantId());
		if (m != null){
			o.setMajorId(m.getMajorId());
		}
		o.setTenantId(getTenantId());
		return oilCardService.selectCardListPage(new Query(params), o);
	}

	@GetMapping("/getOilInfo")
	@ApiOperation(value = "获取主副油卡信息")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "majorNum",value = "主油卡号",paramType = "query",dataType = "String"),
		@ApiImplicitParam(name = "oilCardNum", value = "副油卡号",paramType = "query",dataType = "String")
	})
	public R<List<OilMajor>> getOilInfo(String majorNum,String oilCardNum){
		Integer tenantId = getTenantId();
		//Integer tenantId = 0;
		List<OilMajor> majorList = new ArrayList<>();
		OilCard o = new OilCard();
		o.setTenantId(tenantId);
		Wrapper<OilMajor> wapper = new EntityWrapper<OilMajor>().eq("tenant_id", tenantId);
		//主油卡号和副油卡号都为空
		if (StringUtils.isEmpty(oilCardNum)) {
			if (!StringUtils.isEmpty(majorNum)) {
				wapper.eq("major_number",majorNum);
			}
			majorList = oilMajorService.selectList(wapper);
			if (!(majorList.size() > 0)) {
				return new R<>(null, "该租户下没有主油卡信息", R.FAIL);
			}
			majorList.forEach(oilMajor -> {
				o.setMajorId(oilMajor.getMajorId());
				List<OilCard> oilCardList = oilCardService.selectCardList(o);
				oilMajor.setOilCards(oilCardList);
			});
		}else {

			o.setOilCardNumber(oilCardNum);
			List<OilCard> oilCardList = oilCardService.selectCardList(o);
			if (oilCardList.size() > 0) {
				wapper.eq("major_id", oilCardList.get(0).getMajorId());
				majorList = oilMajorService.selectList(wapper);
				majorList.get(0).setOilCards(oilCardList);
			} else {
				return new R<>(null, "当前副油卡号不存在", R.FAIL);
			}
		}
		return new R<>(majorList);
	}

	@GetMapping("/getOilCardNumByMajorNum")
	@ApiOperation(value = "通过主油卡号查询副油卡号")
	@ApiImplicitParam(name = "majorNum" ,value = "主油卡号",paramType = "query",dataType = "String")
	public R<List<String>> getOilCardNumByMajorId(String majorNum){
		if (StringUtils.isEmpty(majorNum)){
			return new R<>(null,"主油卡号为空",R.FAIL);
		}

		Integer tenantId = getTenantId();
	//	Integer tenantId = 0;
		OilMajor major = oilMajorService.selectOne(new EntityWrapper<OilMajor>().eq("major_number",majorNum).eq("tenant_id",tenantId));
		List<String> oilCardNumLists = oilCardService.selectOilCardNumByMajorId(major.getMajorId());
		if (!(oilCardNumLists.size() > 0)) {
			return new R<>(null,"没有对应的副油卡数据",R.FAIL);
		}

		return new R<>(oilCardNumLists);
	}


	@ApiOperation(value = "测试油卡号是否存在")
	@ApiImplicitParam(name = "oilCardNumber",value = "油卡号",dataType = "String",paramType = "path",required = true)
	@GetMapping("/testOilCardNumber/{oilCardNumber}")
	public R<Boolean> testOilCardNumber(@PathVariable("oilCardNumber") String oilCardNumber)
	{
		OilCard card = oilCardService.selectOilCardByOilCardNumber(oilCardNumber, getTenantId());
		if (null == card){
			return new R<>(false);
		}
		return new R<>(true);
	}


	@PostMapping("/export/{ids}")
	@ApiOperation(value = "导出油卡信息汇总表")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "ids",value = "油卡Id字符串",dataType = "String",paramType = "query",required = true),
			@ApiImplicitParam(name = "oilCard",value = "油卡实体类",dataType = "OilCard",paramType = "query",required = true)
	})
	public AjaxResult export(@PathVariable("ids") String ids, OilCard oilCard, HttpServletRequest request, HttpServletResponse response)
	{
		List<OilCardExportVO> list;
		if ("all".equals(ids)) {
			oilCard.setTenantId(getTenantId());
			list = oilCardService.selectCardVOList(oilCard);
		}else {
			list = oilCardService.selectCardVOListByIds(ids);
		}

		String sheetName = DateUtils.getDate() +"-油卡信息汇总表";
		ExcelUtil<OilCardExportVO> util = new ExcelUtil<OilCardExportVO>(OilCardExportVO.class);
		return util.exportExcel(request, response, list, sheetName,null);
	}






	@GetMapping("/test")
	public R<List<OilCard>> getTest(){
		OilCard o = new OilCard();
		o.setTenantId(getTenantId());
		List<OilCard> oilCardList = oilCardService.selectCardList(o);

		return new R<>(oilCardList);
	}

	@GetMapping("/downloadOilCardModel")
	@ApiOperation(value = "下载主副卡导入模板")
	public void downModel(HttpServletRequest request, HttpServletResponse response) {
		String filename = "主副卡导入模板.xls";
		String path = "static/excel/主副卡导入模板.xls";
		CommonUtils.downloadThymeleaf(resourceLoader,filename,path,request,response);
	}
}
