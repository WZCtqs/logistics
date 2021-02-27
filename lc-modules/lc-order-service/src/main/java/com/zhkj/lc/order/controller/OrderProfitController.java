package com.zhkj.lc.order.controller;

import com.zhkj.lc.common.util.AjaxResult;
import com.zhkj.lc.common.util.excel.ExcelUtil;
import com.zhkj.lc.common.web.BaseController;
import com.zhkj.lc.order.dto.HeadOfficeProfit;
import com.zhkj.lc.order.dto.OrderProfit;
import com.zhkj.lc.order.dto.TruckProfit;
import com.zhkj.lc.order.service.OrderProfitService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.hssf.usermodel.HSSFBorderFormatting;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("orderProfit")
@Api(description = "单票利润统计")
public class OrderProfitController extends BaseController {
    @Autowired
    private OrderProfitService orderProfitService;

    @ApiOperation(value = "订单近7天利润",notes = "参数：客户名称customerName,订单号orderId")
    @PostMapping("selectLast7daysOrderProfit")
    public List<OrderProfit> selectLast7daysOrderProfit(@RequestBody OrderProfit orderProfit) {
        orderProfit.setTenantId(getTenantId());
       return orderProfitService.selectLast7daysOrderProfit(orderProfit);
    }

    @ApiOperation(value = "车辆利润统计")
    @PostMapping("selectTruckOrderProfit")
    public List<TruckProfit> selectTruckOrderProfit(@RequestBody OrderProfit orderProfit) {
        orderProfit.setTenantId(getTenantId());
        return orderProfitService.selectTruckOrderProfit(orderProfit);
    }

    @ApiOperation(value = "订单总利润")
    @PostMapping("selectSumProfit")
    public HeadOfficeProfit selectSumProfit(@RequestBody OrderProfit orderProfit) {
        orderProfit.setTenantId(getTenantId());
        return orderProfitService.selectSumProfit(orderProfit);
    }

    @ApiOperation(value = "导出订单利润",notes = "参数：客户名称customerName,订单号orderId")
    @GetMapping("exportLast7daysOrderProfit")
    public AjaxResult exportLast7daysOrderProfit(OrderProfit orderProfit, HttpServletRequest request, HttpServletResponse response) {
        String timeType = getTimeType(orderProfit.getTimeType(),orderProfit.getBeginTime(), orderProfit.getEndTime());
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        String sheetName = fmt.format(new Date())+"-订单"+timeType+"利润信息";
        orderProfit.setTenantId(getTenantId());
        List<OrderProfit> list = selectLast7daysOrderProfit(orderProfit);
        ExcelUtil<OrderProfit> excel = new ExcelUtil<>(OrderProfit.class);
        return excel.exportExcel(request,response,list,sheetName,null);
    }

    @GetMapping("/exportLast7days")
    @ApiOperation(value = "导出车辆利润",notes = "参数：开始时间beginTime,结束时间endTime")
    public AjaxResult exportLast7days(OrderProfit truckProfit, HttpServletRequest request, HttpServletResponse response) {
        String timeType = getTimeType(truckProfit.getTimeType(), truckProfit.getBeginTime(), truckProfit.getEndTime());
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        String sheetName = fmt.format(new Date())+"-车辆"+timeType+"利润信息";
        List<TruckProfit> list = selectTruckOrderProfit(truckProfit);
        ExcelUtil<TruckProfit> util = new ExcelUtil<TruckProfit>(TruckProfit.class);
        return util.exportExcel(request,response, list, sheetName, null);
    }

    @ApiOperation(value = "导出订单总利润")
    @GetMapping("exportSumProfit")
    public AjaxResult exportSumProfit(OrderProfit orderProfit, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String timeType = getTimeType(orderProfit.getTimeType(),orderProfit.getBeginTime(), orderProfit.getEndTime());
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        String sheetName = fmt.format(new Date())+"-订单"+timeType+"利润信息";
        orderProfit.setTenantId(getTenantId());
        HeadOfficeProfit profit = orderProfitService.selectSumProfit(orderProfit);
        return HeadProfitExcel(sheetName, profit, request, response);
    }
    
    public String getTimeType(String type, Date beginTime, Date endTime){
        String timeType = null;
        if(type!=null) {
            switch (type) {
                case "WEEK":
                    timeType = "近7天";
                    break;
                case "MONTH":
                    timeType = "本月";
                    break;
                case "SEASON":
                    timeType = "本季度";
                    break;
                case "HALF_A_YEAY":
                    timeType = "半年";
                    break;
            }
        }else if(beginTime != null && endTime != null){
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            timeType = format.format(beginTime) + "-" + format.format(endTime);
        }
        return timeType;
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
        sheet.addMergedRegion(new CellRangeAddress(1, 2, (short) 0, (short) 0));
        sheet.addMergedRegion(new CellRangeAddress(1, 2, (short) 1, (short) 1));
        sheet.addMergedRegion(new CellRangeAddress(3, 3, (short) 0, (short) 3));
        sheet.addMergedRegion(new CellRangeAddress(4, 7, (short) 0, (short) 0));
        sheet.addMergedRegion(new CellRangeAddress(4, 5, (short) 1, (short) 1));
        sheet.addMergedRegion(new CellRangeAddress(8, 8, (short) 0, (short) 3));
        sheet.addMergedRegion(new CellRangeAddress(9, 9, (short) 0, (short) 3));

        SXSSFRow recRow1 = sheet.createRow(1);
        recRow1.setRowStyle(cellStyle);
        recRow1.createCell(0).setCellValue("收入");
        recRow1.createCell(1).setCellValue("运费收入");
        recRow1.createCell(2).setCellValue("运输费");
        recRow1.createCell(3).setCellValue(headOfficeProfit.getRecTransportFee().doubleValue());

        SXSSFRow recRow6 = sheet.createRow(2);
        recRow6.createCell(2).setCellValue("异常费用");
        recRow6.createCell(3).setCellValue(headOfficeProfit.getRecExceptionFee().doubleValue());

        double rec = headOfficeProfit.getRecExceptionFee().doubleValue()
                +headOfficeProfit.getRecTransportFee().doubleValue();
        SXSSFRow recRow7 = sheet.createRow(3);
        recRow7.createCell(0).setCellValue("收入合计:"+rec);

        SXSSFRow recRow8 = sheet.createRow(4);
        recRow8.createCell(0).setCellValue("支出");
        recRow8.createCell(1).setCellValue("承运车队支出");
        recRow8.createCell(2).setCellValue("运输费");
        recRow8.createCell(3).setCellValue(headOfficeProfit.getPayTeamTransportFee().doubleValue());

        SXSSFRow recRow12 = sheet.createRow(5);
        recRow12.createCell(2).setCellValue("异常费用");
        recRow12.createCell(3).setCellValue(headOfficeProfit.getPayTeamExceptionFee().doubleValue());

        SXSSFRow recRow14 = sheet.createRow(6);
        recRow14.createCell(1).setCellValue("自有车支出");
        recRow14.createCell(2).setCellValue("异常费用");
        recRow14.createCell(3).setCellValue(headOfficeProfit.getPayPersonExceptionFee().doubleValue());

        SXSSFRow recRow19 = sheet.createRow(7);
        recRow19.createCell(1).setCellValue("开票税额");
        recRow19.createCell(2).setCellValue("开票税额");
        recRow19.createCell(3).setCellValue(headOfficeProfit.getRate().doubleValue());

        double pay = headOfficeProfit.getPayTeamTransportFee().doubleValue()
                +headOfficeProfit.getPayTeamExceptionFee().doubleValue()
                +headOfficeProfit.getPayPersonExceptionFee().doubleValue()
                +headOfficeProfit.getRate().doubleValue();
        SXSSFRow recRow20 = sheet.createRow(8);
        recRow20.createCell(0).setCellValue("支出合计：" + pay);

        SXSSFRow recRow21 = sheet.createRow(9);
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
