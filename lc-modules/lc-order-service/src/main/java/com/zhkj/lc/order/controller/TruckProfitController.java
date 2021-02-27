package com.zhkj.lc.order.controller;

import com.zhkj.lc.common.util.AjaxResult;
import com.zhkj.lc.common.util.excel.ExcelUtil;
import com.zhkj.lc.common.vo.DriverVO;
import com.zhkj.lc.common.vo.FeeVO;
import com.zhkj.lc.common.vo.TruckTeamFeeVO;
import com.zhkj.lc.common.vo.TruckVO;
import com.zhkj.lc.common.web.BaseController;
import com.zhkj.lc.order.dto.TruckProfit;
import com.zhkj.lc.order.feign.TrunkFeign;
import com.zhkj.lc.order.service.TruckProfitService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/truckProfit")
@Api(description = "车辆利润统计")
public class TruckProfitController extends BaseController {
    @Autowired
    private TruckProfitService truckProfitService;
    @Autowired
    private TrunkFeign trunkFeign;

    /**
     * 车辆近7天总利润
     * @param truckProfit
     * @return
     */
    @GetMapping("selectLast7daysTruckProfit")
    @ApiOperation(value = "车辆近7天利润",notes = "参数：车牌号plateNumber")
    public List<TruckProfit> selectLast7daysTruckProfit(TruckProfit truckProfit){
        TruckVO truckVO = new TruckVO();
        truckVO.setTenantId(getTenantId());
        truckVO.setPlateNumber(truckProfit.getPlateNumber());
        List<TruckVO> truckList = trunkFeign.selectTruckList(truckVO);
        List<TruckProfit> truckProfitList = new ArrayList<>();
        BigDecimal b = new BigDecimal(0);
        for(TruckVO truck : truckList){
            List<DriverVO> driverList = trunkFeign.selectDriverByPlateNumber(truck.getTruckId(),getTenantId());
            TruckProfit profits = new TruckProfit();
            profits.setPlateNumber(truck.getPlateNumber());
            profits.setKilometre(0);
            profits.setOrderSum(0);
            profits.setReceivable(b);
            profits.setExpensesPay(b);
            profits.setRate(b);
            for(DriverVO driver : driverList){
                TruckProfit profit = truckProfitService.selectLast7daysProfit(driver.getDriverId(),getTenantId());
                if(profit != null && profit.getOrderSum() != 0){
                    if(driver.getTeamType().equals("运输车队")){
//                        profit.setExpensesPay((profit.getNeedFee().subtract(profit.getNeedFee().multiply(driver.getRate()))).add(profit.getExceptionFee()));
                    }else if(driver.getTeamType().equals("个体车队")){
                        List<DriverVO> list = new ArrayList<>();
                        list.add(driver);
                        FeeVO feeVO = new FeeVO();
                        feeVO.setList(list);
                        feeVO.setTenantId(getTenantId());
                        TruckTeamFeeVO teamFeeVO = trunkFeign.selectTruckFeeByDriverLast7days(feeVO);
                        if(null != teamFeeVO){
                            profit.setExpensesPay(profit.getExceptionFee().add(teamFeeVO.getOtherFee()));
                        }else{
                            profit.setExpensesPay(profit.getExceptionFee());
                        }
                    }
                    profits.setExpensesPay(profits.getExpensesPay().add(profit.getExpensesPay()));
                    profits.setOrderSum(profits.getOrderSum()+profit.getOrderSum());
                    profits.setKilometre(profits.getKilometre()+profit.getKilometre());
                    profits.setReceivable(profits.getReceivable().add(profit.getReceivable()));
                    BigDecimal t = truckProfitService.selectLast7daysRate(driver.getDriverId(),getTenantId());
                    if(null != t){
//                        profits.setRate(profits.getRate().add(t.multiply(driver.getRate())));
                    }
                }
            }
            if(profits != null && profits.getOrderSum() != 0){
                truckProfitList.add(profits);
            }
        }
        return truckProfitList;
    }


    /**
     * 车辆本月利润
     * @param truckProfit
     * @return
     */
    @GetMapping("selectMonthdaysTruckProfit")
    @ApiOperation(value = "车辆本月利润",notes = "参数：车牌号plateNumbers")
    public List<TruckProfit> selectMonthdaysTruckProfit(TruckProfit truckProfit){
        TruckVO truckVO = new TruckVO();
        truckVO.setTenantId(getTenantId());
        truckVO.setPlateNumber(truckProfit.getPlateNumber());
        List<TruckVO> truckList = trunkFeign.selectTruckList(truckVO);
        List<TruckProfit> truckProfitList = new ArrayList<>();
        BigDecimal b = new BigDecimal(0);
        for(TruckVO truck : truckList){
            List<DriverVO> driverList = trunkFeign.selectDriverByPlateNumber(truck.getTruckId(),getTenantId());
            TruckProfit profits = new TruckProfit();
            profits.setPlateNumber(truck.getPlateNumber());
            profits.setKilometre(0);
            profits.setOrderSum(0);
            profits.setReceivable(b);
            profits.setExpensesPay(b);
            profits.setRate(b);
            for(DriverVO driver : driverList){
                TruckProfit profit = truckProfitService.selectMonthdaysProfit(driver.getDriverId(),getTenantId());
                if(profit != null && profit.getOrderSum() != 0){
                    if(driver.getTeamType().equals("运输车队")){
//                        profit.setExpensesPay(profit.getNeedFee().subtract(profit.getNeedFee().multiply(driver.getRate())).add(profit.getExceptionFee()));
                    }else if(driver.getTeamType().equals("个体车队")){
                        List<DriverVO> list = new ArrayList<>();
                        list.add(driver);
                        FeeVO feeVO = new FeeVO();
                        feeVO.setList(list);
                        feeVO.setTenantId(getTenantId());
                        TruckTeamFeeVO teamFeeVO = trunkFeign.selectTruckFeeByDriverMonthdays(feeVO);
                        if(null != teamFeeVO){
                            profit.setExpensesPay(profit.getExceptionFee().add(teamFeeVO.getOtherFee()));
                        }else{
                            profit.setExpensesPay(profit.getExceptionFee());
                        }
                    }
                    profits.setExpensesPay(profits.getExpensesPay().add(profit.getExpensesPay()));
                    profits.setOrderSum(profits.getOrderSum()+profit.getOrderSum());
                    profits.setKilometre(profits.getKilometre()+profit.getKilometre());
                    profits.setReceivable(profits.getReceivable().add(profit.getReceivable()));
                    BigDecimal t = truckProfitService.selectLast7daysRate(driver.getDriverId(),getTenantId());
                    if(null != t){
//                        profits.setRate(profits.getRate().add(t.multiply(driver.getRate())));
                    }
                }
            }
            if(profits != null && profits.getOrderSum() != 0){
                truckProfitList.add(profits);
            }
        }
        return truckProfitList;
    }

    /**
     * 车辆本季度利润
     * @param truckProfit
     * @return
     */
    @GetMapping("selectCurrentSeasonTruckProfit")
    @ApiOperation(value = "车辆本季度利润",notes = "参数：车牌号plateNumbers")
    public List<TruckProfit> selectCurrentSeasonTruckProfit(TruckProfit truckProfit){
        TruckVO truckVO = new TruckVO();
        truckVO.setTenantId(getTenantId());
        truckVO.setPlateNumber(truckProfit.getPlateNumber());
        List<TruckVO> truckList = trunkFeign.selectTruckList(truckVO);
        List<TruckProfit> truckProfitList = new ArrayList<>();
        BigDecimal b = new BigDecimal(0);
        for(TruckVO truck : truckList){
            List<DriverVO> driverList = trunkFeign.selectDriverByPlateNumber(truck.getTruckId(),getTenantId());
            TruckProfit profits = new TruckProfit();
            profits.setPlateNumber(truck.getPlateNumber());
            profits.setKilometre(0);
            profits.setOrderSum(0);
            profits.setReceivable(b);
            profits.setExpensesPay(b);
            profits.setRate(b);
            for(DriverVO driver : driverList){
                TruckProfit profit = truckProfitService.selectCurrentSeasonProfit(driver.getDriverId(),getTenantId());
                if(profit != null && profit.getOrderSum() != 0){
                    if(driver.getTeamType().equals("运输车队")){
//                        profit.setExpensesPay(profit.getNeedFee().subtract(profit.getNeedFee().multiply(driver.getRate())).add(profit.getExceptionFee()));
                    }else if(driver.getTeamType().equals("个体车队")){
                        List<DriverVO> list = new ArrayList<>();
                        list.add(driver);
                        FeeVO feeVO = new FeeVO();
                        feeVO.setList(list);
                        feeVO.setTenantId(getTenantId());
                        TruckTeamFeeVO teamFeeVO = trunkFeign.selectTruckFeeByDriverCurrentSeason(feeVO);
                        if(null != teamFeeVO){
                            profit.setExpensesPay(profit.getExceptionFee().add(teamFeeVO.getOtherFee()));
                        }else{
                            profit.setExpensesPay(profit.getExceptionFee());
                        }
                    }
                    profits.setExpensesPay(profits.getExpensesPay().add(profit.getExpensesPay()));
                    profits.setOrderSum(profits.getOrderSum()+profit.getOrderSum());
                    profits.setKilometre(profits.getKilometre()+profit.getKilometre());
                    profits.setReceivable(profits.getReceivable().add(profit.getReceivable()));
                    BigDecimal t = truckProfitService.selectLast7daysRate(driver.getDriverId(),getTenantId());
                    if(null != t){
//                        profits.setRate(profits.getRate().add(t.multiply(driver.getRate())));
                    }
                }
            }
            if(profits != null && profits.getOrderSum() != 0){
                truckProfitList.add(profits);
            }
        }
        return truckProfitList;
    }

    /**
     * 车辆近半年利润
     * @param truckProfit
     * @return
     */
    @GetMapping("selectLast6MonthsTruckProfit")
    @ApiOperation(value = "车辆近半年利润",notes = "参数：车牌号plateNumbers")
    public List<TruckProfit> selectLast6MonthsTruckProfit(TruckProfit truckProfit){
        TruckVO truckVO = new TruckVO();
        truckVO.setTenantId(getTenantId());
        truckVO.setPlateNumber(truckProfit.getPlateNumber());
        List<TruckVO> truckList = trunkFeign.selectTruckList(truckVO);
        List<TruckProfit> truckProfitList = new ArrayList<>();
        BigDecimal b = new BigDecimal(0);
        for(TruckVO truck : truckList){
            List<DriverVO> driverList = trunkFeign.selectDriverByPlateNumber(truck.getTruckId(),getTenantId());
            TruckProfit profits = new TruckProfit();
            profits.setPlateNumber(truck.getPlateNumber());
            profits.setKilometre(0);
            profits.setOrderSum(0);
            profits.setReceivable(b);
            profits.setExpensesPay(b);
            profits.setRate(b);
            for(DriverVO driver : driverList){
                TruckProfit profit = truckProfitService.selectLast6MonthsProfit(driver.getDriverId(),getTenantId());
                if(profit != null && profit.getOrderSum() != 0){
                    if(driver.getTeamType().equals("运输车队")){
//                        profit.setExpensesPay(profit.getNeedFee().subtract(profit.getNeedFee().multiply(driver.getRate())).add(profit.getExceptionFee()));
                    }else if(driver.getTeamType().equals("个体车队")){
                        List<DriverVO> list = new ArrayList<>();
                        list.add(driver);
                        FeeVO feeVO = new FeeVO();
                        feeVO.setList(list);
                        feeVO.setTenantId(getTenantId());
                        TruckTeamFeeVO teamFeeVO = trunkFeign.selectTruckFeeByDriverLast6Months(feeVO);
                        if(null != teamFeeVO){
                            profit.setExpensesPay(profit.getExceptionFee().add(teamFeeVO.getOtherFee()));
                        }else{
                            profit.setExpensesPay(profit.getExceptionFee());
                        }
                    }
                    profits.setExpensesPay(profits.getExpensesPay().add(profit.getExpensesPay()));
                    profits.setOrderSum(profits.getOrderSum()+profit.getOrderSum());
                    profits.setKilometre(profits.getKilometre()+profit.getKilometre());
                    profits.setReceivable(profits.getReceivable().add(profit.getReceivable()));
                    BigDecimal t = truckProfitService.selectLast7daysRate(driver.getDriverId(),getTenantId());
                    if(null != t){
//                        profits.setRate(profits.getRate().add(t.multiply(driver.getRate())));
                    }
                }
            }
            if(profits != null && profits.getOrderSum() != 0){
                truckProfitList.add(profits);
            }
        }
        return truckProfitList;
    }

    /**
     * 车辆某段时间内利润
     * @param truckProfit
     * @return
     */
    @GetMapping("selectSomeTimeTruckProfit")
    @ApiOperation(value = "车辆某段时间内利润",notes = "参数：车牌号plateNumbers")
    public List<TruckProfit> selectSomeTimeTruckProfit(TruckProfit truckProfit){
        TruckVO truckVO = new TruckVO();
        truckVO.setTenantId(getTenantId());
        truckVO.setPlateNumber(truckProfit.getPlateNumber());
        List<TruckVO> truckList = trunkFeign.selectTruckList(truckVO);
        List<TruckProfit> truckProfitList = new ArrayList<>();
        BigDecimal b = new BigDecimal(0);
        for(TruckVO truck : truckList){
            List<DriverVO> driverList = trunkFeign.selectDriverByPlateNumber(truck.getTruckId(),getTenantId());
            TruckProfit profits = new TruckProfit();
            profits.setPlateNumber(truck.getPlateNumber());
            profits.setKilometre(0);
            profits.setOrderSum(0);
            profits.setReceivable(b);
            profits.setExpensesPay(b);
            profits.setRate(b);
            for(DriverVO driver : driverList){
                truckProfit.setDriverId(driver.getDriverId());
                TruckProfit profit = truckProfitService.selectSomeTimeProfit(truckProfit);
                if(profit != null && profit.getOrderSum() != 0){
                    if(driver.getTeamType().equals("运输车队")){
//                        profit.setExpensesPay(profit.getNeedFee().subtract(profit.getNeedFee().multiply(driver.getRate())).add(profit.getExceptionFee()));
                    }else if(driver.getTeamType().equals("个体车队")){
                        List<DriverVO> list = new ArrayList<>();
                        list.add(driver);
                        FeeVO feeVO = new FeeVO();
                        feeVO.setList(list);
                        feeVO.setTenantId(getTenantId());
                        TruckTeamFeeVO teamFeeVO = trunkFeign.selectTruckFeeByDriverSometime(feeVO);
                        if(null != teamFeeVO){
                            profit.setExpensesPay(profit.getExceptionFee().add(teamFeeVO.getOtherFee()));
                        }else{
                            profit.setExpensesPay(profit.getExceptionFee());
                        }
                    }
                    profits.setExpensesPay(profits.getExpensesPay().add(profit.getExpensesPay()));
                    profits.setOrderSum(profits.getOrderSum()+profit.getOrderSum());
                    profits.setKilometre(profits.getKilometre()+profit.getKilometre());
                    profits.setReceivable(profits.getReceivable().add(profit.getReceivable()));
                    BigDecimal t = truckProfitService.selectLast7daysRate(driver.getDriverId(),getTenantId());
                    if(null != t){
//                        profits.setRate(profits.getRate().add(t.multiply(driver.getRate())));
                    }
                }
            }
            if(profits != null && profits.getOrderSum() != 0){
                truckProfitList.add(profits);
            }
        }
        return truckProfitList;
    }


    /**
     * 导出车辆近7天时间利润
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/exportLast7days")
    @ApiOperation(value = "导出车辆近7天利润",notes = "参数：开始时间beginTime,结束时间endTime")
    public AjaxResult exportLast7days(TruckProfit truckProfit, HttpServletRequest request, HttpServletResponse response) {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        String sheetName = fmt.format(new Date())+"-车辆近7天利润信息";
        //truckProfit.setTenantId(getTenantId());
        List<TruckProfit> list = selectLast7daysTruckProfit(truckProfit);
        ExcelUtil<TruckProfit> util = new ExcelUtil<TruckProfit>(TruckProfit.class);
        return util.exportExcel(request,response, list, sheetName, null);
    }

    /**
     * 导出车辆本月时间利润
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/exportMonthdays")
    @ApiOperation(value = "导出车辆本月利润",notes = "参数：开始时间beginTime,结束时间endTime")
    public AjaxResult exportMonthdays(TruckProfit truckProfit, HttpServletRequest request, HttpServletResponse response) {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        String sheetName = fmt.format(new Date())+"-车辆本月利润信息";
//        truckProfit.setTenantId(getTenantId());
        List<TruckProfit> list = selectMonthdaysTruckProfit(truckProfit);
        ExcelUtil<TruckProfit> util = new ExcelUtil<TruckProfit>(TruckProfit.class);
        return util.exportExcel(request,response, list, sheetName, null);
    }

    /**
     * 导出车辆本季度时间利润
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/exportCurrentSeason")
    @ApiOperation(value = "导出车辆本季度利润",notes = "参数：开始时间beginTime,结束时间endTime")
    public AjaxResult exportCurrentSeason(TruckProfit truckProfit, HttpServletRequest request, HttpServletResponse response) {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        String sheetName = fmt.format(new Date())+"-车辆本季度利润信息";
//        truckProfit.setTenantId(getTenantId());
        List<TruckProfit> list = selectCurrentSeasonTruckProfit(truckProfit);
        ExcelUtil<TruckProfit> util = new ExcelUtil<TruckProfit>(TruckProfit.class);
        return util.exportExcel(request,response, list, sheetName, null);
    }

    /**
     * 导出车辆近半年时间利润
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/exportLast6Months")
    @ApiOperation(value = "导出车辆近半年利润",notes = "参数：开始时间beginTime,结束时间endTime")
    public AjaxResult exportLast6Months(TruckProfit truckProfit, HttpServletRequest request, HttpServletResponse response) {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        String sheetName = fmt.format(new Date())+"-车辆近半年利润信息";
//        truckProfit.setTenantId(getTenantId());
        List<TruckProfit> list = selectLast6MonthsTruckProfit(truckProfit);
        ExcelUtil<TruckProfit> util = new ExcelUtil<TruckProfit>(TruckProfit.class);
        return util.exportExcel(request,response, list, sheetName, null);
    }

    /**
     * 导出车辆某段时间利润
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/exportSomeTime")
    @ApiOperation(value = "导出车辆某段时间利润",notes = "参数：开始时间beginTime,结束时间endTime")
    public AjaxResult exportSomeTime(TruckProfit truckProfit, HttpServletRequest request, HttpServletResponse response) {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        String sheetName = fmt.format(new Date())+"-车辆利润信息";
//        truckProfit.setTenantId(getTenantId());
        List<TruckProfit> list = selectSomeTimeTruckProfit(truckProfit);
        ExcelUtil<TruckProfit> util = new ExcelUtil<TruckProfit>(TruckProfit.class);
        return util.exportExcel(request,response, list, sheetName, null);
    }
}
