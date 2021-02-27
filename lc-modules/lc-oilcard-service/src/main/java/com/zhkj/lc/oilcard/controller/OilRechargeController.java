package com.zhkj.lc.oilcard.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.zhkj.lc.common.util.*;
import com.zhkj.lc.common.util.excel.ExcelUtil;
import com.zhkj.lc.common.vo.AnnouncementVO;
import com.zhkj.lc.common.vo.DriverVO;
import com.zhkj.lc.common.vo.FeeVO;
import com.zhkj.lc.common.vo.ReChargeVO;
import com.zhkj.lc.common.web.BaseController;
import com.zhkj.lc.oilcard.feign.OrderFeign;
import com.zhkj.lc.oilcard.feign.TruckFeign;
import com.zhkj.lc.oilcard.model.*;
import com.zhkj.lc.oilcard.service.IMajorMonthRechargeService;
import com.zhkj.lc.oilcard.service.IOilCardService;
import com.zhkj.lc.oilcard.service.IOilRechargeService;
import com.zhkj.lc.oilcard.service.IOilTruckMonthRechargeService;
import com.zhkj.lc.oilcard.service.impl.DictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 油卡充值表 前端控制器
 * </p>
 *
 * @author wzc
 * @since 2018-12-07
 */
@Api(description = "油卡充值(不包含导出和分页显示接口)")
@RestController
@RequestMapping("/oilRecharge")
public class OilRechargeController extends BaseController {

    @Autowired private IOilRechargeService oilRechargeService;
    @Autowired private IOilCardService oilCardService;
    @Autowired private IMajorMonthRechargeService majorMonthRechargeService;
    @Autowired private DictService dictService;
    @Autowired private TruckFeign truckFeign;
    @Autowired private OrderFeign orderFeign;
    @Autowired private IOilTruckMonthRechargeService oilTruckMonthRechargeService;
    /**
    * 通过ID查询
    *
    * @param id ID
    * @return OilRecharge
    */
    @ApiOperation(value = "根据充值id获取充值记录")
    @ApiImplicitParam(name = "rechargeId",value = "充值id",paramType = "path",dataType = "Integer",required = true,example = "1")
    @GetMapping("/{id}")
    public R<OilRecharge> get(@PathVariable Integer id) {
        return new R<>(oilRechargeService.selectRechargeById(id));
    }

    /**
     * 添加
     * @param  oilRecharge  实体
     * @return success/false
     */
    @ApiOperation(value = "申请添加充值记录")
    @ApiImplicitParam(name = "oilRecharge",value = "申请的充值信息,rechargeId不用填，添加后自动有值,字段：oilCardId,truckId,majorId,oilCardNumber,MajorNumber",dataType = "OilRecharge",paramType = "body",required = true)
    @Transactional
    @PostMapping
    public R<Boolean> add(@RequestBody OilRecharge oilRecharge) {
        //rechargeType  充值类型（0：正常，1：运费，2：外调车
       // System.out.println("0000"+oilRecharge);
        if (null == oilRecharge.getCreateBy()) {
            oilRecharge.setCreateBy(UserUtils.getUser());
        }
        if (null == oilRecharge.getTenantId()) {
            oilRecharge.setTenantId(getTenantId());
        }
        if(oilRecharge.getRechargeType().equals("2")){
            //如果是外调车校验订单号
            if (StringUtils.isEmpty(oilRecharge.getOrderId())){
                return new R<>(false,"订单号不存在");
            }
            boolean b = orderFeign.setSettlementStatus(oilRecharge.getOrderId());
            //todo	修改msg中的信息
            if (!b){
                return new R<>(b,"调用orderFeign接口返回false");
            }
        }

		return new R<>(oilRechargeService.insertOilRecharge(oilRecharge));
    }

    /**
     * 删除
     * @param ids IDs
     * @return success/false
     */
    @ApiOperation(value = "根据充值id删除或批量删除", notes = "如果已经进行充值操作了，就不能删除")
    @ApiImplicitParam(name = "ids",value = "充值id",paramType = "path",required = true,example = "1,2")
    @DeleteMapping("/{ids}")
    public R<Boolean> delete(@PathVariable String ids) {
        return new R<>(oilRechargeService.deleteOilRechargeByIds(UserUtils.getUser(),ids));
    }

    /**
     * 编辑
     * @param  oilRecharge  实体
     * @return success/false
     */
    @ApiOperation(value = "根据充值id编辑充值信息",notes = "oilRecharge参数中字段rechargeId是必须的")
    @ApiImplicitParam(name = "oilRecharge",value = "充值记录信息",dataType = "OilRecharge",paramType = "body",required = true)
    @Transactional
    @PutMapping
    public R<Boolean> edit(@RequestBody OilRecharge oilRecharge) {
        if (null == oilRecharge.getUpdateBy()) {
            oilRecharge.setUpdateBy(UserUtils.getUser());
        }
        return new R<>(oilRechargeService.updateOilRecharge(oilRecharge));
    }

    /**
     * 根据油卡号id获取油卡信息
     * @param id 油卡id
     * @return 油卡信息
     */
    @ApiOperation(value = "油卡号下拉选择后，根据油卡号id获取油卡其他信息")
    @ApiImplicitParam(name = "oilCardId",value = "油卡号id",paramType = "path",dataType = "Integer",required = true, example = "1")
    @GetMapping("/getOilCardById/{id}")
    public OilCard getOilCard(@PathVariable Integer id)
    {
        OilCard card = oilCardService.selectByOilCardId(id);
        if(card != null) {
            card.setCardType(dictService.getLabel("oilcard_type", card.getCardType(), card.getTenantId()));
            card.setAttribute(dictService.getLabel("truck_attribute", card.getAttribute(), card.getTenantId()));
        }
        return card;
    }

    /**
     * 设置审核状态
     * @param oilRecharge 充值信息
     * @return 结果
     */
    @ApiOperation(value = "点击审核通过按钮,调的接口", notes = "参数oilRecharge中必要的字段{rechargeId,oilCardId,isPassed,ownerDriverId,rechargeSum,rechargeType,failRemark}")
    @ApiImplicitParam(name = "oilRecharge",value = "油卡充值记录",dataType = "OilRecharge",paramType = "body",required = true)
    @Transactional
    @PostMapping("/isPassed")
    public R<Boolean> isPassedUpdate(@RequestBody OilRecharge oilRecharge)
    {
        // 审核情况以公告的方式发送给司机
        AnnouncementVO announcementVO = new AnnouncementVO();
        announcementVO.setTitle("油卡充值审核");
        announcementVO.setType("0"); // 个人
        announcementVO.setDriverOwerId(oilRecharge.getOwnerDriverId());
        announcementVO.setCreateBy(UserUtils.getUser());
        announcementVO.setTenantId(getTenantId());

        if("1".equals(oilRecharge.getIsPassed()) ) {
            R<Boolean> r = new R<>(false);
            oilRecharge.setTenantId(getTenantId());
            List<OilRecharge> oilRechargeNoRecharge = oilRechargeService.selectNoRechargeMonth(oilRecharge);
            if (null != oilRechargeNoRecharge && oilRechargeNoRecharge.size() > 0){
                r.setMsg("油卡 "+(oilRechargeNoRecharge.get(0).getOilCardNumber()==null?"":oilRechargeNoRecharge.get(0).getOilCardNumber())+" 有审核通过但未充值的充值记录，请先充值之后再进行审核！");
                return r;
            }

            if ("1".equals(oilRecharge.getRechargeType()) || ("0").equals(oilRecharge.getRechargeType())) {
                //运费油卡充值要将充值金额添加到车辆油卡月汇总表模块
                OilTruckMonthRecharge oilTruckMonthRecharge = oilTruckMonthRechargeService.selectByTruckIdWithYearMonth(oilRecharge.getTruckId(),getTenantId(), DateUtils.getDateYM());
                if(oilTruckMonthRecharge != null){
                    if(oilTruckMonthRecharge.getBalance() !=null && (oilTruckMonthRecharge.getBalance().subtract(oilRecharge.getRechargeSum())).doubleValue()>=0){

                    }else {
                        r.setMsg("该车辆的运费油卡余额，小于运费充值金额，不能进行充值！");
                        return r;
                    }
                }
              /*  DriverVO driverVO = truckFeign.getDriverByid(oilRecharge.getOwnerDriverId());
                if (null != driverVO) {
                    if (null != driverVO.getFreightOilcardAmount() && (driverVO.getFreightOilcardAmount().subtract(oilRecharge.getRechargeSum())).doubleValue() >= 0) {
                    } else {
                        r.setMsg("司机" + (driverVO.getDriverName() == null?"":driverVO.getDriverName()) + "的运费油卡余额，小于运费充值金额，不能进行充值！");
                        return r;
                    }
                }else {
                    r.setMsg("没有司机信息，没有对应的运费油卡余额，不能进行运费充值！");
                    return r;
                }*/
            }
            OilCard oilCard = oilCardService.selectByOilCardId(oilRecharge.getOilCardId());
            if (null != oilCard && null != oilCard.getMajorId()) {

                MajorMonthRecharge majorMonthRecharge = new MajorMonthRecharge();
                majorMonthRecharge.setYearMonth(DateUtils.getDateYM());
                majorMonthRecharge.setMajorId(oilRecharge.getMajorId());
                majorMonthRecharge.setTenantId(oilRecharge.getTenantId());

                // 主卡月充值
                List<MajorMonthRecharge> list = majorMonthRechargeService.selectMajorMonthRechargeList(majorMonthRecharge);
                if (null != list && list.size() > 0) {
                    if(null == list.get(0).getMonthAmount()){
                        r.setMsg("主卡 " + oilCard.getMajorNumber() + " 没有余额，请给主卡充值！");
                        return r;
                    }else {
                        if ((list.get(0).getMonthAmount().subtract(oilRecharge.getRechargeSum())).doubleValue() >= 0) {
                            announcementVO.setContent("油卡充值审核通过，待充值！");
                            truckFeign.addFeign(announcementVO);
                            oilRecharge.setUpdateBy(UserUtils.getUser());
                            return new R<>(oilRechargeService.updateOilRecharge(oilRecharge));
                        } else {
                            r.setMsg("油卡" + oilCard.getOilCardNumber() + "充值金额，大于主卡" + oilCard.getMajorNumber() + "的可充值分配余额 " + list.get(0).getMonthAmount().toPlainString() + "！");
                            return r;
                        }
                    }
                } else {
                    r.setMsg(oilCard.getMajorNumber() + " 主卡该月没有充值，没有金额分配给附属卡充值！");
                    return r;
                }
            } else {
                r.setMsg("添加信息要包含油卡信息，且油卡要有对应的主卡！");
                return r;
            }
        }else {
            announcementVO.setContent("油卡充值审核不通过，原因：" + (oilRecharge.getFailRemark() == null ? "" : oilRecharge.getFailRemark()));
            truckFeign.addFeign(announcementVO);
            oilRecharge.setUpdateBy(UserUtils.getUser());
            return new R<>(oilRechargeService.updateOilRecharge(oilRecharge));
        }
    }

    /**
     * 充值
     * @param oilRecharge 充值信息
     * @return 结果
     */
    @ApiOperation(value = "点击充值，允许充值按钮,调的接口", notes = "参数oilRecharge中必要的字段{rechargeId,majorId,rechargeType,rechargeSum,monthRechargeSum,ownerDriverId}；充值成功后有充值时间rechargeDate")
    @ApiImplicitParam(name = "oilRecharge",value = "油卡充值记录",dataType = "OilRecharge", paramType = "body",required = true)
    @Transactional
    @PostMapping("/rechargeR")
    public R<Boolean> recharge(@RequestBody OilRecharge oilRecharge){
        String loginUser = UserUtils.getUser();
        oilRecharge.setUpdateBy(loginUser);
        //充值类型(0：正常，1：运费，2：外调车；添加时必需)，0,1不需要订单号，需要与车辆绑定
        OilRecharge oilRechargeSelect = oilRechargeService.selectRechargeById(oilRecharge.getRechargeId());
        oilRecharge.setRechargeDate(new Date());
        if ("1".equals(oilRechargeSelect.getRechargeType())|| ("0").equals(oilRecharge.getRechargeType())) {
            //运费油卡充值要将充值金额添加到车辆油卡月汇总表模块
            OilTruckMonthRecharge oilTruckMonthRecharge = oilTruckMonthRechargeService.selectByTruckIdWithYearMonth(oilRecharge.getTruckId(),getTenantId(), DateUtils.getDateYM());
            if(oilTruckMonthRecharge != null){
               // System.out.println("-----"+oilTruckMonthRecharge);
                oilTruckMonthRecharge.setDistributeSum(oilTruckMonthRecharge.getDistributeSum().add(oilRecharge.getRechargeSum()));//分配金额相加
                oilTruckMonthRecharge.setBalance(oilTruckMonthRecharge.getBalance().subtract(oilRecharge.getRechargeSum()));//本月余额
                oilTruckMonthRechargeService.updateTruckMonthRecharge(oilTruckMonthRecharge);
            }else{
                return new R<>(false,"该车辆没有对应的运费油卡余额，不能进行运费充值！");
            }
            oilRecharge.setMonthRechargeSum(
                    BigDecimal.valueOf(
                            Double.parseDouble(oilRecharge.getMonthRechargeSum().toPlainString())
                                    + Double.parseDouble(oilRecharge.getRechargeSum().toPlainString())//月充值金额
                    )
            );

        }else {
            //外调车油卡往结算接口传值
            if (StringUtils.isEmpty(oilRecharge.getOrderId()) || StringUtils.isEmpty(loginUser)) {
                return new R<>(false, "订单号或用户信息为空");
            }
            boolean b = orderFeign.rechargeCompleted(oilRecharge.getOrderId(), oilRecharge.getUpdateBy());
            if (!b) {
                //todo 修改 msg返回值
                return new R<>(false,"失败");
            }
        }

        /*if (!oilRecharge.getRechargeType().equals("2")) {
            oilRecharge.setMonthRechargeSum(
                    BigDecimal.valueOf(
                            Double.parseDouble(oilRecharge.getMonthRechargeSum().toPlainString())
                                    + Double.parseDouble(oilRecharge.getRechargeSum().toPlainString())//月充值金额
                    )
            );
        }else {
            //外调车油卡往结算接口传值
            if (StringUtils.isEmpty(oilRecharge.getOrderId()) || StringUtils.isEmpty(loginUser)) {
                return new R<>(false, "订单号或用户信息为空");
            }
            boolean b = orderFeign.rechargeCompleted(oilRecharge.getOrderId(), oilRecharge.getUpdateBy());
            if (!b) {
                //todo 修改 msg返回值
                return new R<>(false,"失败");
            }
        }*/


        AnnouncementVO announcementVO = new AnnouncementVO();
        announcementVO.setTitle("油卡充值结果");
        announcementVO.setType("0"); // 个人
        announcementVO.setDriverOwerId(oilRechargeSelect.getOwnerDriverId());
        announcementVO.setCreateBy(loginUser);
        announcementVO.setTenantId(getTenantId());
        announcementVO.setContent("油卡 "+(oilRechargeSelect.getOilCardNumber()==null?"":oilRechargeSelect.getOilCardNumber())+" 充值 "+(oilRechargeSelect.getRechargeSum()==null?"":oilRechargeSelect.getRechargeSum())+" 成功！");
        truckFeign.addFeign(announcementVO);

        // 油卡充值要将充值金额添加到主卡月分配金额中
        MajorMonthRecharge majorMonthRecharge =  majorMonthRechargeService.selectMajorMonthRechargeUpdate(oilRecharge.getMajorId(), getTenantId(), DateUtils.getDateYM());
        if (null != majorMonthRecharge){
            majorMonthRecharge.setDistributeSum(majorMonthRecharge.getDistributeSum().add(oilRecharge.getRechargeSum()));
            majorMonthRechargeService.updateMajorMonthRecharge(majorMonthRecharge);
        }else {
            majorMonthRecharge = new MajorMonthRecharge();
            majorMonthRecharge.setMajorId(oilRecharge.getMajorId());
            majorMonthRecharge.setYearMonth(DateUtils.getDateYM());
            majorMonthRecharge.setDistributeSum(oilRecharge.getRechargeSum());
            majorMonthRecharge.setTenantId(getTenantId());
            majorMonthRecharge.setCreateBy(loginUser);
            majorMonthRechargeService.insertMajorMonthRecharge(majorMonthRecharge);
        }

		return new R<>(oilRechargeService.updateOilRecharge(oilRecharge));
    }

    /**
     * 获取司机结算方式和正在进行的订单id
     * @param ownerDriverId 充值信息
     * @return 结果
     */
    @ApiOperation(value = "获取司机结算方式和正在进行的订单id", notes = "返回结果是Map，包含payType、orderId")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ownerDriverId",value = "司机id",dataType = "Integer", paramType = "path",required = true),
            @ApiImplicitParam(name = "oilRecharge",value = "充值信息(字段tenantId，小程序需要传值，后台不需要传值)",dataType = "OilRecharge", paramType = "param")
    })
    @PostMapping( "/bindingOrder/{ownerDriverId}")
    public Map<String, String> bindingOrder(@PathVariable("ownerDriverId") Integer ownerDriverId, OilRecharge oilRecharge){

        Integer td;
        if (null == oilRecharge.getTenantId()){
            td = getTenantId();
        }else {
            td = oilRecharge.getTenantId();
        }
        String payType = truckFeign.getDriverPayType(ownerDriverId, td);
        String orderId = orderFeign.selectByDriverIdForOil(ownerDriverId, td);
        Map<String, String> map = new HashMap<>();
        map.put("payType", payType);
        map.put("orderId", orderId);
        return map;
    }

    /**
     * 当月充值未结束的充值记录
     * @param oilRecharge
     * @return 充值记录集合
     */
    @ApiIgnore
    @PostMapping("/noRechargeMonth")
    public List<OilRecharge> noRechargeMonth(@RequestBody OilRecharge oilRecharge)
    {
        return oilRechargeService.selectNoRechargeMonth(oilRecharge);
    }

    @ApiOperation(value = "油卡充值汇总显示", notes = "返回结果，yearMonth、attribute、monthRechargeSum")
    @ApiImplicitParam(name = "yearMonth",value = "充值信息(字段tenantId，小程序需要传值，后台不需要传值)",dataType = "OilRecharge", paramType = "param")
    @GetMapping( "/rechargeSum")
    public Page<RechargeSum> selectRechargeByAttribute(@RequestParam Map<String, Object> params, String yearMonth) {
        return oilRechargeService.selectRechargeByRechrageSort(new Query(params), yearMonth, getTenantId());
    }

    @ApiOperation(value = "油卡充值汇总导出", notes = "返回结果，yearMonth、attribute、monthRechargeSum")
    @ApiImplicitParam(name = "yearMonth",value = "充值信息(字段tenantId，小程序需要传值，后台不需要传值)",dataType = "OilRecharge", paramType = "param")
    @GetMapping( "/rechargeSumExport/{yearMonth}")
    public AjaxResult selectRechargeByAttributeExport(@PathVariable("yearMonth") String yearMonth, HttpServletRequest request, HttpServletResponse response) {

        List<RechargeSum> list = oilRechargeService.selectRechargeByRechrageSort(yearMonth, getTenantId());
        ExcelUtil<RechargeSum> util = new ExcelUtil<RechargeSum>(RechargeSum.class);
        return util.exportExcel(request, response, list, (yearMonth==null ? "":yearMonth)+"-油卡充值汇总",null);
    }

    /**************************************************************************
     *  小程序端接口
     * @author ckj
     * @date 2018-12-28 15:45
     */

    /**
     * 小程序:获取油卡充值记录
     *
     * @param oilRecharge 油卡充值信息
     * @return  油卡充值记录集合
     */
    @ApiOperation(value = "小程序:获取油卡充值记录",notes = "参数可选字段(前三个是必需的)oilCardId,ownerDriverId,tenantId,applyDate,rechargeDate")
    @ApiImplicitParam(name = "oilRecharge",value = "油卡充值信息",paramType = "query",dataType = "OilRecharge")
    @GetMapping("/applet/list")
    public List<OilRecharge> appletList(OilRecharge oilRecharge)
    {
        return oilRechargeService.selectRechargeListApplet(oilRecharge);
    }


    @PostMapping("selectOilFeeByDriverLast7days")
    public BigDecimal selectOilFeeByDriverLast7days(FeeVO feeVO){
        return oilRechargeService.selectOilFeeByDriverLast7days(feeVO);
    }

    @PostMapping("selectOilFeeByDriverMonthdays")
    public BigDecimal selectOilFeeByDriverMonthdays(FeeVO feeVO){
        return oilRechargeService.selectOilFeeByDriverMonthdays(feeVO);
    }

    @PostMapping("selectOilFeeByDriverCurrentSeason")
    public BigDecimal selectOilFeeByDriverCurrentSeason(FeeVO feeVO){
        return oilRechargeService.selectOilFeeByDriverCurrentSeason(feeVO);
    }

    @PostMapping("selectOilFeeByDriverLast6Months")
    public BigDecimal selectOilFeeByDriverLast6Months(FeeVO feeVO){
        return oilRechargeService.selectOilFeeByDriverLast6Months(feeVO);
    }

    @PostMapping("selectOilFeeByDriverSometime")
    public BigDecimal selectOilFeeByDriverSometime(FeeVO feeVO){
        return oilRechargeService.selectOilFeeByDriverSometime(feeVO);
    }


    @ApiOperation(value = "油卡充值汇总明细")
    @ApiImplicitParam(name = "reChargeVO",dataType = "ReChargeVO", paramType = "param")
    @GetMapping( "/getOilRechargeListByPage")
    public Page<ReChargeVO> selectRechargeByAttribute(@RequestParam Map<String, Object> params, ReChargeVO reChargeVO) throws ParseException {
    	reChargeVO.setTenantId(getTenantId());
        return oilRechargeService.selectOilRechargeListByPage(new Query(params),reChargeVO);
    }



	/**
	 * 搜索导出
	 * @param reChargeVO 搜索条件
	 * @return 结果
	 */
	@ApiOperation(value = "搜索导出")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "ids", value = "油卡充值ids", paramType = "path", dataType = "String", required = true),
			@ApiImplicitParam(name = "reChargeVO", value = "外调车充值信息，可选字段：cardType,truckOwner,beginTime,endTime(申请时间范围),ownerDriverId等", paramType = "query", dataType = "ReChargeVO")
	})
	@PostMapping("/export/{ids}")
	public AjaxResult export(@PathVariable("ids") String ids, ReChargeVO reChargeVO, HttpServletRequest request, HttpServletResponse response)
	{
		List<ReChargeVO> list;
		if ("all".equals(ids)) {
			reChargeVO.setTenantId(getTenantId());
			list = oilRechargeService.selectOilRechargeListAll(reChargeVO);
		}else {
			list = oilRechargeService.selectReChargeByIds(ids);
		}

		String sheetName = DateUtils.getDate() +"-油卡充值信息汇总";
		ExcelUtil<ReChargeVO> util = new ExcelUtil<ReChargeVO>(ReChargeVO.class);
		return util.exportExcel(request, response, list, sheetName,null);
	}

	@GetMapping("/getReChargeVoById/{id}")
    @ApiOperation("根据ID查询充值回单数据")
    @ApiImplicitParam(name = "id",value = "数据的主键",dataType = "String" ,paramType = "query")
    public R<ReChargeVO> getReChargeVoById(@PathVariable("id")String id){
        List<ReChargeVO> list = oilRechargeService.selectReChargeByIds(id);
        ReChargeVO reChargeVO = list.get(0);

        return new R<>(reChargeVO);
    }

}
