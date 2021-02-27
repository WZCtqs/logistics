package com.zhkj.lc.order.controller;

import com.zhkj.lc.common.util.AjaxResult;
import com.zhkj.lc.common.vo.DriverVO;
import com.zhkj.lc.common.vo.FeeVO;
import com.zhkj.lc.common.vo.TruckTeamFeeVO;
import com.zhkj.lc.common.web.BaseController;
import com.zhkj.lc.order.dto.HeadOfficeProfit;
import com.zhkj.lc.order.feign.OilCardFeign;
import com.zhkj.lc.order.feign.TrunkFeign;
import com.zhkj.lc.order.service.HeadOfficeProfitService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/headOfficeProfit")
@Api(description = "总公司利润接口")
public class HeadOfficeProfitController extends BaseController {
    @Autowired
    private HeadOfficeProfitService headOfficeProfitService;
    @Autowired
    private TrunkFeign trunkFeign;
    @Autowired
    private OilCardFeign oilCardFeign;
    /**
     * 总公司近7天利润
     * @return
     */
    @GetMapping("selectLast7days")
    @ApiOperation(value = "总公司近7天利润",notes = "不传参")
    public HeadOfficeProfit selectLast7daysProfit(){
        HeadOfficeProfit headOfficeSum = new HeadOfficeProfit();
        BigDecimal rate = new BigDecimal(0);
        List<DriverVO> driverList = trunkFeign.getDriverTeamType("运输车队",getTenantId());
        if(null != driverList && driverList.size() > 0){
            for( DriverVO driver: driverList){
//                if(driver != null && null != driver.getRate()){
//                    rate=rate.add(headOfficeProfitService.selectLast7daysRate(getTenantId(),driver.getDriverId()).getRate().multiply(driver.getRate()));
//                }
            }
        }
        FeeVO teamFeeVO = new FeeVO();
        teamFeeVO.setTenantId(getTenantId());
        teamFeeVO.setList(driverList);
        TruckTeamFeeVO truckTeamFeeTeam = trunkFeign.selectTruckFeeByDriverLast7days(teamFeeVO);
        HeadOfficeProfit headOfficeProfit = headOfficeProfitService.selectLast7daysTeam(getTenantId(),driverList);

        List<DriverVO> driverListPerson = trunkFeign.getDriverTeamType("个体车队",getTenantId());
        if(driverListPerson.size() > 0 && null != driverListPerson){
            for( DriverVO driver: driverList){
//                if(driver != null && null != driver.getRate()){
//                    rate=rate.add(headOfficeProfitService.selectLast7daysRate(driver.getTenantId(),driver.getDriverId()).getRate().multiply(driver.getRate()));
//                }
            }
        }
        FeeVO personFeeVO = new FeeVO();
        personFeeVO.setTenantId(getTenantId());
        personFeeVO.setList(driverListPerson);
        TruckTeamFeeVO truckTeamFeePerson = trunkFeign.selectTruckFeeByDriverLast7days(personFeeVO);
        HeadOfficeProfit headOfficeProfitPerson = headOfficeProfitService.selectLast7daysPerson(getTenantId(),driverListPerson);

        headOfficeSum.setRecTransportFee(headOfficeProfit.getRecTransportFee().add(headOfficeProfitPerson.getRecTransportFee()));
        headOfficeSum.setRecPickFee(headOfficeProfit.getRecPickFee().add(headOfficeProfitPerson.getRecPickFee()));
        headOfficeSum.setRecPackFee(headOfficeProfit.getRecPackFee().add(headOfficeProfitPerson.getRecPackFee()));
        headOfficeSum.setRecReleaseFee(headOfficeProfit.getRecReleaseFee().add(headOfficeProfitPerson.getRecReleaseFee()));
        headOfficeSum.setRecPickcnFee(headOfficeProfit.getRecPickcnFee().add(headOfficeProfitPerson.getRecPickcnFee()));
        headOfficeSum.setRecExceptionFee(headOfficeProfit.getRecExceptionFee().add(headOfficeProfitPerson.getRecExceptionFee()));
        headOfficeSum.setPayTeamTransportFee(headOfficeProfit.getPayTeamTransportFee());
        headOfficeSum.setPayTeamPackFee(headOfficeProfit.getPayTeamPackFee());
        headOfficeSum.setPayTeamReleaseFee(headOfficeProfit.getPayTeamReleaseFee());
        headOfficeSum.setPayTeamOilFee(headOfficeProfit.getPayTeamOilFee().add(oilCardFeign.selectOilFeeByDriverLast7days(teamFeeVO)));
        headOfficeSum.setPayTeamExceptionFee(headOfficeProfit.getPayTeamExceptionFee());
        headOfficeSum.setPayTeamOtherFee(truckTeamFeeTeam.getOtherFee());
        headOfficeSum.setPayPersonOilFee(headOfficeProfitPerson.getPayPersonOilFee().add(oilCardFeign.selectOilFeeByDriverLast7days(personFeeVO)));
        headOfficeSum.setPayPersonPackFee(headOfficeProfitPerson.getPayPersonPackFee());
        headOfficeSum.setPayPersonReleaseFee(headOfficeProfitPerson.getPayPersonReleaseFee());
        headOfficeSum.setPayPersonExceptionFee(headOfficeProfitPerson.getPayPersonExceptionFee());
        headOfficeSum.setPayPersonOtherFee(truckTeamFeePerson.getOtherFee());
        headOfficeSum.setRate(rate);
        return headOfficeSum;
    }


    /**
     * 查询本月总公司利润
     * @return
     */
    @GetMapping("selectMonthdays")
    @ApiOperation(value = "查询本月总公司利润",notes = "不传参")
    public HeadOfficeProfit selectMonthdays(){
        HeadOfficeProfit headOfficeSum = new HeadOfficeProfit();
        BigDecimal rate = new BigDecimal(0);
        List<DriverVO> driverList = trunkFeign.getDriverTeamType("运输车队",getTenantId());
        if(null != driverList && driverList.size() > 0){
            for( DriverVO driver: driverList){
//                if(driver != null && null != driver.getRate()){
//                    rate=rate.add(headOfficeProfitService.selectMonthdaysRate(driver.getTenantId(),driver.getDriverId()).getRate().multiply(driver.getRate()));
//                }
            }
        }
        FeeVO teamFeeVO = new FeeVO();
        teamFeeVO.setTenantId(getTenantId());
        teamFeeVO.setList(driverList);
        TruckTeamFeeVO truckTeamFeeTeam = trunkFeign.selectTruckFeeByDriverMonthdays(teamFeeVO);
        HeadOfficeProfit headOfficeProfit = headOfficeProfitService.selectMonthdaysTeam(getTenantId(),driverList);

        List<DriverVO> driverListPerson = trunkFeign.getDriverTeamType("个体车队",getTenantId());
        if(driverListPerson.size() > 0 && null != driverListPerson){
            for( DriverVO driver: driverList){
//                if(driver != null && null != driver.getRate()){
//                    rate=rate.add(headOfficeProfitService.selectMonthdaysRate(driver.getTenantId(),driver.getDriverId()).getRate().multiply(driver.getRate()));
//                }
            }
        }
        FeeVO personFeeVO = new FeeVO();
        personFeeVO.setTenantId(getTenantId());
        personFeeVO.setList(driverListPerson);
        TruckTeamFeeVO truckTeamFeePerson = trunkFeign.selectTruckFeeByDriverMonthdays(personFeeVO);
        HeadOfficeProfit headOfficeProfitPerson = headOfficeProfitService.selectMonthdaysPerson(getTenantId(),driverListPerson);

        headOfficeSum.setRecTransportFee(headOfficeProfit.getRecTransportFee().add(headOfficeProfitPerson.getRecTransportFee()));
        headOfficeSum.setRecPickFee(headOfficeProfit.getRecPickFee().add(headOfficeProfitPerson.getRecPickFee()));
        headOfficeSum.setRecPackFee(headOfficeProfit.getRecPackFee().add(headOfficeProfitPerson.getRecPackFee()));
        headOfficeSum.setRecReleaseFee(headOfficeProfit.getRecReleaseFee().add(headOfficeProfitPerson.getRecReleaseFee()));
        headOfficeSum.setRecPickcnFee(headOfficeProfit.getRecPickcnFee().add(headOfficeProfitPerson.getRecPickcnFee()));
        headOfficeSum.setRecExceptionFee(headOfficeProfit.getRecExceptionFee().add(headOfficeProfitPerson.getRecExceptionFee()));
        headOfficeSum.setPayTeamTransportFee(headOfficeProfit.getPayTeamTransportFee());
        headOfficeSum.setPayTeamPackFee(headOfficeProfit.getPayTeamPackFee());
        headOfficeSum.setPayTeamReleaseFee(headOfficeProfit.getPayTeamReleaseFee());
        headOfficeSum.setPayTeamOilFee(headOfficeProfit.getPayTeamOilFee().add(oilCardFeign.selectOilFeeByDriverMonthdays(teamFeeVO)));
        headOfficeSum.setPayTeamExceptionFee(headOfficeProfit.getPayTeamExceptionFee());
        headOfficeSum.setPayTeamOtherFee(truckTeamFeeTeam.getOtherFee());
        headOfficeSum.setPayPersonOilFee(headOfficeProfitPerson.getPayPersonOilFee().add(oilCardFeign.selectOilFeeByDriverMonthdays(personFeeVO)));
        headOfficeSum.setPayPersonPackFee(headOfficeProfitPerson.getPayPersonPackFee());
        headOfficeSum.setPayPersonReleaseFee(headOfficeProfitPerson.getPayPersonReleaseFee());
        headOfficeSum.setPayPersonExceptionFee(headOfficeProfitPerson.getPayPersonExceptionFee());
        headOfficeSum.setPayPersonOtherFee(truckTeamFeePerson.getOtherFee());
        headOfficeSum.setRate(rate);
        return headOfficeSum;
    }

    /**
     * 查询本季度总公司利润
     * @return
     */
    @GetMapping("selectCurrentSeason")
    @ApiOperation(value = "查询本季度总公司利润",notes = "不传参")
    public HeadOfficeProfit selectCurrentSeason(){
        HeadOfficeProfit headOfficeSum = new HeadOfficeProfit();
        BigDecimal rate = new BigDecimal(0);
        List<DriverVO> driverList = trunkFeign.getDriverTeamType("运输车队",getTenantId());
        if(null != driverList && driverList.size() > 0){
            for( DriverVO driver: driverList){
//                if(driver != null && null != driver.getRate()){
//                    rate=rate.add(headOfficeProfitService.selectCurrentSeasonRate(driver.getTenantId(),driver.getDriverId()).getRate().multiply(driver.getRate()));
//                }
            }
        }
        FeeVO teamFeeVO = new FeeVO();
        teamFeeVO.setTenantId(getTenantId());
        teamFeeVO.setList(driverList);
        TruckTeamFeeVO truckTeamFeeTeam = trunkFeign.selectTruckFeeByDriverCurrentSeason(teamFeeVO);
        HeadOfficeProfit headOfficeProfit = headOfficeProfitService.selectCurrentSeasonTeam(getTenantId(),driverList);

        List<DriverVO> driverListPerson = trunkFeign.getDriverTeamType("个体车队",getTenantId());
        if(driverListPerson.size() > 0 && null != driverListPerson){
            for( DriverVO driver: driverList){
//                if(driver != null && null != driver.getRate()){
//                    rate=rate.add(headOfficeProfitService.selectCurrentSeasonRate(driver.getTenantId(),driver.getDriverId()).getRate().multiply(driver.getRate()));
//                }
            }
        }
        FeeVO personFeeVO = new FeeVO();
        personFeeVO.setTenantId(getTenantId());
        personFeeVO.setList(driverListPerson);
        TruckTeamFeeVO truckTeamFeePerson = trunkFeign.selectTruckFeeByDriverCurrentSeason(personFeeVO);
        HeadOfficeProfit headOfficeProfitPerson = headOfficeProfitService.selectCurrentSeasonPerson(getTenantId(),driverListPerson);

        headOfficeSum.setRecTransportFee(headOfficeProfit.getRecTransportFee().add(headOfficeProfitPerson.getRecTransportFee()));
        headOfficeSum.setRecPickFee(headOfficeProfit.getRecPickFee().add(headOfficeProfitPerson.getRecPickFee()));
        headOfficeSum.setRecPackFee(headOfficeProfit.getRecPackFee().add(headOfficeProfitPerson.getRecPackFee()));
        headOfficeSum.setRecReleaseFee(headOfficeProfit.getRecReleaseFee().add(headOfficeProfitPerson.getRecReleaseFee()));
        headOfficeSum.setRecPickcnFee(headOfficeProfit.getRecPickcnFee().add(headOfficeProfitPerson.getRecPickcnFee()));
        headOfficeSum.setRecExceptionFee(headOfficeProfit.getRecExceptionFee().add(headOfficeProfitPerson.getRecExceptionFee()));
        headOfficeSum.setPayTeamTransportFee(headOfficeProfit.getPayTeamTransportFee());
        headOfficeSum.setPayTeamPackFee(headOfficeProfit.getPayTeamPackFee());
        headOfficeSum.setPayTeamReleaseFee(headOfficeProfit.getPayTeamReleaseFee());
        headOfficeSum.setPayTeamOilFee(headOfficeProfit.getPayTeamOilFee().add(oilCardFeign.selectOilFeeByDriverCurrentSeason(teamFeeVO)));
        headOfficeSum.setPayTeamExceptionFee(headOfficeProfit.getPayTeamExceptionFee());
        headOfficeSum.setPayTeamOtherFee(truckTeamFeeTeam.getOtherFee());
        headOfficeSum.setPayPersonOilFee(headOfficeProfitPerson.getPayPersonOilFee().add(oilCardFeign.selectOilFeeByDriverCurrentSeason(personFeeVO)));
        headOfficeSum.setPayPersonPackFee(headOfficeProfitPerson.getPayPersonPackFee());
        headOfficeSum.setPayPersonReleaseFee(headOfficeProfitPerson.getPayPersonReleaseFee());
        headOfficeSum.setPayPersonExceptionFee(headOfficeProfitPerson.getPayPersonExceptionFee());
        headOfficeSum.setPayPersonOtherFee(truckTeamFeePerson.getOtherFee());
        headOfficeSum.setRate(rate);
        return headOfficeSum;
    }


    /**
     * 查询半年总公司利润
     * @return
     */
    @GetMapping("selectLast6Months")
    @ApiOperation(value = "查询半年总公司利润",notes = "不传参")
    public HeadOfficeProfit selectLast6Months(){
        HeadOfficeProfit headOfficeSum = new HeadOfficeProfit();
        BigDecimal rate = new BigDecimal(0);
        List<DriverVO> driverList = trunkFeign.getDriverTeamType("运输车队",getTenantId());
        if(null != driverList && driverList.size() > 0){
            for( DriverVO driver: driverList){
//                if(driver != null && null != driver.getRate()){
//                    rate=rate.add(headOfficeProfitService.selectLast6MonthsRate(driver.getTenantId(),driver.getDriverId()).getRate().multiply(driver.getRate()));
//                }
            }
        }
        FeeVO teamFeeVO = new FeeVO();
        teamFeeVO.setTenantId(getTenantId());
        teamFeeVO.setList(driverList);
        TruckTeamFeeVO truckTeamFeeTeam = trunkFeign.selectTruckFeeByDriverLast6Months(teamFeeVO);
        HeadOfficeProfit headOfficeProfit = headOfficeProfitService.selectLast6MonthsTeam(getTenantId(),driverList);

        List<DriverVO> driverListPerson = trunkFeign.getDriverTeamType("个体车队",getTenantId());
        if(driverListPerson.size() > 0 && null != driverListPerson){
            for( DriverVO driver: driverList){
//                if(driver != null && null != driver.getRate()){
//                    rate=rate.add(headOfficeProfitService.selectLast6MonthsRate(driver.getTenantId(),driver.getDriverId()).getRate().multiply(driver.getRate()));
//                }
            }
        }
        FeeVO personFeeVO = new FeeVO();
        personFeeVO.setTenantId(getTenantId());
        personFeeVO.setList(driverListPerson);
        TruckTeamFeeVO truckTeamFeePerson = trunkFeign.selectTruckFeeByDriverLast6Months(personFeeVO);
        HeadOfficeProfit headOfficeProfitPerson = headOfficeProfitService.selectLast6MonthsPerson(getTenantId(),driverListPerson);

        headOfficeSum.setRecTransportFee(headOfficeProfit.getRecTransportFee().add(headOfficeProfitPerson.getRecTransportFee()));
        headOfficeSum.setRecPickFee(headOfficeProfit.getRecPickFee().add(headOfficeProfitPerson.getRecPickFee()));
        headOfficeSum.setRecPackFee(headOfficeProfit.getRecPackFee().add(headOfficeProfitPerson.getRecPackFee()));
        headOfficeSum.setRecReleaseFee(headOfficeProfit.getRecReleaseFee().add(headOfficeProfitPerson.getRecReleaseFee()));
        headOfficeSum.setRecPickcnFee(headOfficeProfit.getRecPickcnFee().add(headOfficeProfitPerson.getRecPickcnFee()));
        headOfficeSum.setRecExceptionFee(headOfficeProfit.getRecExceptionFee().add(headOfficeProfitPerson.getRecExceptionFee()));
        headOfficeSum.setPayTeamTransportFee(headOfficeProfit.getPayTeamTransportFee());
        headOfficeSum.setPayTeamPackFee(headOfficeProfit.getPayTeamPackFee());
        headOfficeSum.setPayTeamReleaseFee(headOfficeProfit.getPayTeamReleaseFee());
        headOfficeSum.setPayTeamOilFee(headOfficeProfit.getPayTeamOilFee().add(oilCardFeign.selectOilFeeByDriverLast6Months(teamFeeVO)));
        headOfficeSum.setPayTeamExceptionFee(headOfficeProfit.getPayTeamExceptionFee());
        headOfficeSum.setPayTeamOtherFee(truckTeamFeeTeam.getOtherFee());
        headOfficeSum.setPayPersonOilFee(headOfficeProfitPerson.getPayPersonOilFee().add(oilCardFeign.selectOilFeeByDriverLast6Months(personFeeVO)));
        headOfficeSum.setPayPersonPackFee(headOfficeProfitPerson.getPayPersonPackFee());
        headOfficeSum.setPayPersonReleaseFee(headOfficeProfitPerson.getPayPersonReleaseFee());
        headOfficeSum.setPayPersonExceptionFee(headOfficeProfitPerson.getPayPersonExceptionFee());
        headOfficeSum.setPayPersonOtherFee(truckTeamFeePerson.getOtherFee());
        headOfficeSum.setRate(rate);
        return headOfficeSum;
    }

    /**
     * 查询某段时间总公司利润
     * @return
     */
    @GetMapping("selectSomeTime")
    @ApiOperation(value = "查询某段时间总公司利润",notes = "参数：开始时间beginTime,结束时间endTime")
    public HeadOfficeProfit selectSomeTime(HeadOfficeProfit headOfficeProfit){
        headOfficeProfit.setTenantId(getTenantId());
        HeadOfficeProfit headOfficeSum = new HeadOfficeProfit();
        BigDecimal rate = new BigDecimal(0);
        List<DriverVO> driverList = trunkFeign.getDriverTeamType("运输车队",getTenantId());
        if(null != driverList && driverList.size() > 0){
            for( DriverVO driver: driverList){
//                if(driver != null && null != driver.getRate()){
//                    rate=rate.add(headOfficeProfitService.selectSomeTimeRate(headOfficeProfit).getRate().multiply(driver.getRate()));
//                }
            }
        }
        FeeVO teamFeeVO = new FeeVO();
        teamFeeVO.setTenantId(getTenantId());
        teamFeeVO.setList(driverList);
        teamFeeVO.setBeginTime(headOfficeProfit.getBeginTime());
        teamFeeVO.setEndTime(headOfficeProfit.getEndTime());
        TruckTeamFeeVO truckTeamFeeTeam = trunkFeign.selectTruckFeeByDriverSometime(teamFeeVO);
        HeadOfficeProfit headOfficeProfitTeam = headOfficeProfitService.selectSomeTimeTeam(headOfficeProfit);

        List<DriverVO> driverListPerson = trunkFeign.getDriverTeamType("个体车队",getTenantId());
        if(driverListPerson.size() > 0 && null != driverListPerson){
            for( DriverVO driver: driverList){
//                if(driver != null && null != driver.getRate()){
//                    rate=rate.add(headOfficeProfitService.selectSomeTimeRate(headOfficeProfit).getRate().multiply(driver.getRate()));
//                }
            }
        }
        FeeVO personFeeVO = new FeeVO();
        personFeeVO.setTenantId(getTenantId());
        personFeeVO.setList(driverListPerson);
        TruckTeamFeeVO truckTeamFeePerson = trunkFeign.selectTruckFeeByDriverSometime(personFeeVO);
        HeadOfficeProfit headOfficeProfitPerson = headOfficeProfitService.selectSomeTimePerson(headOfficeProfit);

        headOfficeSum.setRecTransportFee(headOfficeProfitTeam.getRecTransportFee().add(headOfficeProfitPerson.getRecTransportFee()));
        headOfficeSum.setRecPickFee(headOfficeProfitTeam.getRecPickFee().add(headOfficeProfitPerson.getRecPickFee()));
        headOfficeSum.setRecPackFee(headOfficeProfitTeam.getRecPackFee().add(headOfficeProfitPerson.getRecPackFee()));
        headOfficeSum.setRecReleaseFee(headOfficeProfitTeam.getRecReleaseFee().add(headOfficeProfitPerson.getRecReleaseFee()));
        headOfficeSum.setRecPickcnFee(headOfficeProfitTeam.getRecPickcnFee().add(headOfficeProfitPerson.getRecPickcnFee()));
        headOfficeSum.setRecExceptionFee(headOfficeProfitTeam.getRecExceptionFee().add(headOfficeProfitPerson.getRecExceptionFee()));
        headOfficeSum.setPayTeamTransportFee(headOfficeProfitTeam.getPayTeamTransportFee());
        headOfficeSum.setPayTeamPackFee(headOfficeProfitTeam.getPayTeamPackFee());
        headOfficeSum.setPayTeamReleaseFee(headOfficeProfitTeam.getPayTeamReleaseFee());
        headOfficeSum.setPayTeamOilFee(headOfficeProfitTeam.getPayTeamOilFee().add(oilCardFeign.selectOilFeeByDriverSometime(teamFeeVO)));
        headOfficeSum.setPayTeamExceptionFee(headOfficeProfitTeam.getPayTeamExceptionFee());
        headOfficeSum.setPayTeamOtherFee(truckTeamFeeTeam.getOtherFee());
        headOfficeSum.setPayPersonOilFee(headOfficeProfitPerson.getPayPersonOilFee().add(oilCardFeign.selectOilFeeByDriverSometime(personFeeVO)));
        headOfficeSum.setPayPersonPackFee(headOfficeProfitPerson.getPayPersonPackFee());
        headOfficeSum.setPayPersonReleaseFee(headOfficeProfitPerson.getPayPersonReleaseFee());
        headOfficeSum.setPayPersonExceptionFee(headOfficeProfitPerson.getPayPersonExceptionFee());
        headOfficeSum.setPayPersonOtherFee(truckTeamFeePerson.getOtherFee());
        headOfficeSum.setRate(rate);
        return headOfficeSum;
    }


    /**
     * 总公司近7天利润
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/exportLast7days")
    @ApiOperation("导出总公司近7天利润")
    public AjaxResult exportLast7days(HttpServletRequest request, HttpServletResponse response) throws IOException {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        String sheetName = fmt.format(new Date())+"-总公司近7天利润信息";
        HeadOfficeProfit headOfficeProfit = selectLast7daysProfit();
        return HeadProfitExcel(sheetName,headOfficeProfit,request,response);
    }

    /**
     * 导出总公司本月利润
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/exportMonthdays")
    @ApiOperation("导出总公司本月利润")
    public AjaxResult exportMonthdays(HttpServletRequest request, HttpServletResponse response) throws IOException {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        String sheetName = fmt.format(new Date())+"-总公司本月利润信息";
        HeadOfficeProfit headOfficeProfit = selectMonthdays();
        return HeadProfitExcel(sheetName,headOfficeProfit,request,response);
    }

    /**
     * 导出总公司本季度利润
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/exportCurrentSeason")
    @ApiOperation("导出总公司本季度利润")
    public AjaxResult exportCurrentSeason(HttpServletRequest request, HttpServletResponse response) throws IOException {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        String sheetName = fmt.format(new Date())+"-总公司本季度利润信息";
        HeadOfficeProfit headOfficeProfit = selectCurrentSeason();
        return HeadProfitExcel(sheetName,headOfficeProfit,request,response);
    }

    /**
     * 导出总公司半年利润
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/exportLast6Months")
    @ApiOperation("导出总公司半年利润")
    public AjaxResult exportLast6Months(HttpServletRequest request, HttpServletResponse response) throws IOException {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        String sheetName = fmt.format(new Date())+"-总公司半年利润信息";
        HeadOfficeProfit headOfficeProfit = selectLast6Months();
        return HeadProfitExcel(sheetName,headOfficeProfit,request,response);
    }

    /**
     * 导出总公司某段时间利润
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/exportSomeTime")
    @ApiOperation(value = "导出总公司某段时间利润",notes = "参数：开始时间beginTime,结束时间endTime")
    public AjaxResult exportSomeTime(HeadOfficeProfit headOfficeProfit,HttpServletRequest request, HttpServletResponse response) throws IOException {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        String sheetName = fmt.format(new Date())+"-总公司利润信息";
        headOfficeProfit.setTenantId(getTenantId());
        HeadOfficeProfit headProfit = selectSomeTime(headOfficeProfit);
        return HeadProfitExcel(sheetName,headProfit,request,response);
    }


    public AjaxResult HeadProfitExcel(String filename,HeadOfficeProfit headOfficeProfit,HttpServletRequest request, HttpServletResponse response) throws IOException {
        //创建一个Excel文件
        HSSFWorkbook workbook = new HSSFWorkbook();

        //创建poi导出数据对象
        SXSSFWorkbook sxssfWorkbook = new SXSSFWorkbook();

        //创建sheet页
        SXSSFSheet sheet = sxssfWorkbook.createSheet("总公司利润表");
        sheet.setColumnWidth(0,13*256);
        sheet.setColumnWidth(1,25*256);
        sheet.setColumnWidth(2,20*256);
        sheet.setColumnWidth(3,14*256);
        sheet.setDefaultRowHeight((short)(3*256));

        //合并的单元格样式
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        //设置单元格边框
/*        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
          cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
         cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
         cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);*/
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); //水平居中
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); //垂直居中
        //设置一个边框
        cellStyle.setBorderTop(HSSFBorderFormatting.BORDER_THICK);

        HSSFFont font = workbook.createFont();
        font.setFontName("黑体");
        font.setFontHeightInPoints((short) 16);//设置字体大小
        cellStyle.setFont(font);

        //创建表头
        SXSSFRow headTitle = sheet.createRow(0);
        headTitle.setRowStyle(cellStyle);
        //设置表头信息
        headTitle.createCell(0).setCellValue("收支");
        headTitle.createCell(1).setCellValue("来源");
        headTitle.createCell(2).setCellValue("费用明细");
        headTitle.createCell(3).setCellValue("金额");

        //参数1：起始行 参数2：终止行 参数3：起始列 参数4：终止列
        sheet.addMergedRegion(new CellRangeAddress(1, 6, (short) 0, (short) 0));
        sheet.addMergedRegion(new CellRangeAddress(1, 6, (short) 1, (short) 1));
        sheet.addMergedRegion(new CellRangeAddress(7, 7, (short) 0, (short) 3));
        sheet.addMergedRegion(new CellRangeAddress(8, 19, (short) 0, (short) 0));
        sheet.addMergedRegion(new CellRangeAddress(8, 13, (short) 1, (short) 1));
        sheet.addMergedRegion(new CellRangeAddress(14, 18, (short) 1, (short) 1));
        sheet.addMergedRegion(new CellRangeAddress(20, 20, (short) 0, (short) 3));
        sheet.addMergedRegion(new CellRangeAddress(21, 21, (short) 0, (short) 3));

        SXSSFRow recRow1 = sheet.createRow(1);
        recRow1.setRowStyle(cellStyle);
        recRow1.createCell(0).setCellValue("收入");
        recRow1.createCell(1).setCellValue("运费收入");
        recRow1.createCell(2).setCellValue("运输费");
        recRow1.createCell(3).setCellValue(headOfficeProfit.getRecTransportFee().doubleValue());

        SXSSFRow recRow2 = sheet.createRow(2);
        recRow2.createCell(2).setCellValue("提货费");
        recRow2.createCell(3).setCellValue(headOfficeProfit.getRecPickFee().doubleValue());

        SXSSFRow recRow3 = sheet.createRow(3);
        recRow3.createCell(2).setCellValue("装货费");
        recRow3.createCell(3).setCellValue(headOfficeProfit.getRecPackFee().doubleValue());

        SXSSFRow recRow4 = sheet.createRow(4);
        recRow4.createCell(2).setCellValue("卸货费");
        recRow4.createCell(3).setCellValue(headOfficeProfit.getRecReleaseFee().doubleValue());

        SXSSFRow recRow5 = sheet.createRow(5);
        recRow5.createCell(2).setCellValue("提箱费");
        recRow5.createCell(3).setCellValue(headOfficeProfit.getRecPickcnFee().doubleValue());

        SXSSFRow recRow6 = sheet.createRow(6);
        recRow6.createCell(2).setCellValue("异常费用");
        recRow6.createCell(3).setCellValue(headOfficeProfit.getRecExceptionFee().doubleValue());

        double rec = headOfficeProfit.getRecExceptionFee().doubleValue()
                +headOfficeProfit.getRecTransportFee().doubleValue()+headOfficeProfit.getRecPickFee().doubleValue()+headOfficeProfit.getRecPackFee().doubleValue()
                +headOfficeProfit.getRecReleaseFee().doubleValue()+headOfficeProfit.getRecPickcnFee().doubleValue();
        SXSSFRow recRow7 = sheet.createRow(7);
        recRow7.createCell(0).setCellValue("收入合计:"+rec);

        SXSSFRow recRow8 = sheet.createRow(8);
        recRow8.createCell(0).setCellValue("支出");
        recRow8.createCell(1).setCellValue("承运车队支出");
        recRow8.createCell(2).setCellValue("运输费");
        recRow8.createCell(3).setCellValue(headOfficeProfit.getPayTeamTransportFee().doubleValue());

        SXSSFRow recRow9 = sheet.createRow(9);
        recRow9.createCell(2).setCellValue("装货费");
        recRow9.createCell(3).setCellValue(headOfficeProfit.getPayTeamPackFee().doubleValue());

        SXSSFRow recRow10 = sheet.createRow(10);
        recRow10.createCell(2).setCellValue("卸货费");
        recRow10.createCell(3).setCellValue(headOfficeProfit.getPayTeamReleaseFee().doubleValue());

        SXSSFRow recRow11 = sheet.createRow(11);
        recRow11.createCell(2).setCellValue("油费");
        recRow11.createCell(3).setCellValue(headOfficeProfit.getPayTeamOilFee().doubleValue());

        SXSSFRow recRow12 = sheet.createRow(12);
        recRow12.createCell(2).setCellValue("异常费用");
        recRow12.createCell(3).setCellValue(headOfficeProfit.getPayTeamExceptionFee().doubleValue());

        SXSSFRow recRow13 = sheet.createRow(13);
        recRow13.createCell(2).setCellValue("其他费用");
        recRow13.createCell(3).setCellValue(headOfficeProfit.getPayPersonOtherFee().doubleValue());

        SXSSFRow recRow14 = sheet.createRow(14);
        recRow14.createCell(1).setCellValue("自有车支出");
        recRow14.createCell(2).setCellValue("油费");
        recRow14.createCell(3).setCellValue(headOfficeProfit.getPayPersonOilFee().doubleValue());

        SXSSFRow recRow15 = sheet.createRow(15);
        recRow15.createCell(2).setCellValue("装货费");
        recRow15.createCell(3).setCellValue(headOfficeProfit.getPayPersonPackFee().doubleValue());

        SXSSFRow recRow16 = sheet.createRow(16);
        recRow16.createCell(2).setCellValue("卸货费");
        recRow16.createCell(3).setCellValue(headOfficeProfit.getPayPersonReleaseFee().doubleValue());

        SXSSFRow recRow17 = sheet.createRow(17);
        recRow17.createCell(2).setCellValue("异常费用");
        recRow17.createCell(3).setCellValue(headOfficeProfit.getPayPersonExceptionFee().doubleValue());

        SXSSFRow recRow18 = sheet.createRow(18);
        recRow18.createCell(2).setCellValue("其他费用");
        recRow18.createCell(3).setCellValue(headOfficeProfit.getPayPersonOtherFee().doubleValue());

        SXSSFRow recRow19 = sheet.createRow(19);
        recRow19.createCell(1).setCellValue("开票税率");
        recRow19.createCell(2).setCellValue("开票税率");
        recRow19.createCell(3).setCellValue(headOfficeProfit.getRate().doubleValue());

        double pay = headOfficeProfit.getPayTeamTransportFee().doubleValue() +headOfficeProfit.getPayTeamPackFee().doubleValue()
                +headOfficeProfit.getPayTeamReleaseFee().doubleValue()+headOfficeProfit.getPayTeamExceptionFee().doubleValue()
                +headOfficeProfit.getPayTeamOilFee().doubleValue()+headOfficeProfit.getPayTeamOtherFee().doubleValue()
                +headOfficeProfit.getPayPersonOilFee().doubleValue()+headOfficeProfit.getPayPersonPackFee().doubleValue()
                +headOfficeProfit.getPayPersonReleaseFee().doubleValue()+headOfficeProfit.getPayPersonExceptionFee().doubleValue()
                +headOfficeProfit.getPayPersonOtherFee().doubleValue()+headOfficeProfit.getRate().doubleValue();
        SXSSFRow recRow20 = sheet.createRow(20);
        recRow20.createCell(0).setCellValue("支出合计：" + pay);

        SXSSFRow recRow21 = sheet.createRow(21);
        recRow21.createCell(0).setCellValue("本期合计：" + (rec-pay));

        // 设置头信息
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/vnd.ms-excel");
        //一定要设置成xlsx格式
        response.setHeader("Content-Disposition", "attachment;filename=" +
                java.net.URLEncoder.encode(filename + ".xlsx", "UTF-8"));
        //创建一个输出流
        ServletOutputStream outputStream = response.getOutputStream();
        //写入数据
        sxssfWorkbook.write(outputStream);

        // 关闭
        outputStream.close();
        sxssfWorkbook.close();

        return AjaxResult.success(filename);
    }
}
