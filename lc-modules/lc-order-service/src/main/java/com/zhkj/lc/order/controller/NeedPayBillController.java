package com.zhkj.lc.order.controller;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Date;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.zhkj.lc.common.config.Global;
import com.zhkj.lc.common.util.*;
import com.zhkj.lc.common.util.support.Convert;
import com.zhkj.lc.common.vo.DriverVO;
import com.zhkj.lc.order.dto.*;
import com.zhkj.lc.order.feign.TrunkFeign;
import com.zhkj.lc.order.mapper.OrdExceptionFeeMapper;
import com.zhkj.lc.order.model.entity.*;
import com.zhkj.lc.order.service.IBillMiddleService;
import com.zhkj.lc.order.service.INeedPayBillService;
import com.zhkj.lc.order.service.IOrdCommonTruckService;
import com.zhkj.lc.order.service.IOrdSettlementService;
import com.zhkj.lc.order.service.impl.OrdOrderServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.zhkj.lc.common.constant.CommonConstant;
import com.baomidou.mybatisplus.plugins.Page;
import com.zhkj.lc.common.web.BaseController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author cb
 * @since 2019-02-19
 */
@Api(description = "应付对账单、应付费用")
@RestController
@RequestMapping("/needPayBill")
public class NeedPayBillController extends BaseController {
    @Autowired
    private INeedPayBillService needPayBillService;

    @Autowired
    private IBillMiddleService billMiddleService;

    @Autowired
    private TrunkFeign trunkFeign;
    @Autowired
    private OrdOrderServiceImpl orderServiceImpl;

    @Autowired
    private IOrdCommonTruckService commonTruckService;

    @Autowired
    private IOrdSettlementService settlementService;

    /**
     * 通过ID查询
     *
     * @param id ID
     * @return NeedPayBill
     */
    @GetMapping("/{id}")
    public R<NeedPayBill> get(@PathVariable Integer id) {
        return new R<>(needPayBillService.selectById(id));
    }

    @ApiOperation(value = "（----对账单页面）应付对账单分页查询")
    @GetMapping("/page")
    public Page<NeedPayBill> needPayBillList(@RequestParam Map<String, Object> params, FinanceQueryDTO dto) {
        dto.setTenantId(getTenantId());
        return needPayBillService.needPayBillList(new Query<>(params), dto);
    }

    @ApiOperation(value = "（----对账单页面）导出对账单accountPayId")
    @GetMapping("excel")
    public boolean excel(HttpServletRequest request, HttpServletResponse response, String accountPayId, String orderType) {
        BillMiddle billMiddle = new BillMiddle();
        billMiddle.setTenantId(getTenantId());
        billMiddle.setAccountPayNumber(accountPayId);
        List<BillMiddle> billMiddles = billMiddleService.selectByAccountPayNumber(billMiddle);
        String[] orderIds = new String[billMiddles.size()];
        for (int i = 0; i < billMiddles.size(); i++) {
            orderIds[i] = billMiddles.get(i).getOrderNumber();
        }
        FinanceQueryDTO dto = new FinanceQueryDTO();
        dto.setTenantId(getTenantId());
        dto.setOrderIds(orderIds);
        dto.setOrderType(orderType);
        return needPayBillService.exportBillByQuery(request, response, dto);
    }

    @ApiOperation(value = "（----对账单页面）发送对账单接口,id 对账单id")
    @Transactional
    @GetMapping("send")
    public boolean send(Integer id) {
        NeedPayBill needPayBill = new NeedPayBill();
        needPayBill.setId(id);
        needPayBill.setSettlementStatus(CommonConstant.YFS);
        needPayBill.setUpdateBy(UserUtils.getUser());
        needPayBill.setUpdateTime(new Date());
        return needPayBillService.updateById(needPayBill);
    }

    @ApiOperation(value = "（----对账单页面）删除对账单,id;accountPayId")
    @Transactional
    @DeleteMapping("/{accountPayIds}")
    public R<Boolean> delete(@PathVariable String[] accountPayIds) {
        return needPayBillService.updateOrderStatus(accountPayIds, UserUtils.getUser(), getTenantId());
    }

    @ApiOperation(value = "（----对账单页面）费用分配接口")
    @Transactional
    @PutMapping
    public R<Boolean> edit(@RequestBody NeedPayBill needPayBill) {
        needPayBill.setUpdateTime(new Date());
        needPayBill.setSettlementStatus(CommonConstant.WFS);
        return new R<>(needPayBillService.updateById(needPayBill));
    }

    @ApiOperation(value = "（----对账单页面）")
    @GetMapping("confirm")
    public R<Boolean> confirm(Integer id, String accountPayId, String flag,String repaynumber) {
        return new R<>(needPayBillService.confirm(id, accountPayId, flag, repaynumber, getTenantId(), UserUtils.getUser()));
    }

    @ApiOperation(value = "（----对账单页面）根据对账单号查询订单")
    @ApiImplicitParam(value = "accountPayId 对账单id，orderType：订单类型")
    @GetMapping("selectOrderByAccountPayId")
    public NeedPayBill selectOrderByAccountPayId(String accountPayId, String orderType) {
        Integer tenantId = getTenantId();
        /*查询对账单*/
        NeedPayBill needPayBill = needPayBillService.needPayBillList(accountPayId, tenantId);
        if (needPayBill != null) {
            /*查找出该对账单编号下的订单号*/
            BillMiddle billMiddle = new BillMiddle();
            billMiddle.setTenantId(tenantId);
            billMiddle.setAccountPayNumber(accountPayId);
            List<BillMiddle> billMiddles = billMiddleService.selectByAccountPayNumber(billMiddle);
            String orders[] = new String[billMiddles.size()];
            for (int i = 0; i < orders.length; i++) {
                orders[i] = billMiddles.get(i).getOrderNumber();
            }
            /*如果不存在订单，则结束。防止查询出多余订单*/
            if (orders.length == 0) {
                return null;
            }
            /*根据订单编号查询*/
            switch (orderType) {
                case "1":
                    needPayBill.setOrderType("普货");
                    List<PhNeedPayDetail> ppdList = needPayBillService.selectPhBillToDZD(orders, tenantId);
                    for (PhNeedPayDetail pd : ppdList) {
                        DriverVO driverVO = trunkFeign.getDriverByid(pd.getDriverId());
                        if(driverVO != null) {
                            pd.setDriverName(driverVO.getDriverName() + driverVO.getPhone());
                        }
                        pd.setZdStatus(needPayBill.getSettlementStatus());
                        pd.setNeedPay(pd.getTransportFee().add(pd.getExFee().add(pd.getYcFee())));
                    }
                    needPayBill.setPpdList(ppdList);
                    break;
                case "0":
                    needPayBill.setOrderType("集装箱");
                    List<CnNeedPayDetail> cnpdList = needPayBillService.selectCnBillToDZD(orders, tenantId);
                    for (CnNeedPayDetail pd : cnpdList) {
                        DriverVO driverVO = trunkFeign.getDriverByid(pd.getDriverId());
                        if(driverVO != null) {
                            pd.setDriverName(driverVO.getDriverName() + driverVO.getPhone());
                        }
                        pd.setZdStatus(needPayBill.getSettlementStatus());
                        pd.setNeedPay(pd.getTransportFee().add(pd.getExFee().add(pd.getYcFee())));
                    }
                    needPayBill.setCnpdList(cnpdList);
                    break;
            }
        }
        return needPayBill;
    }

    @ApiOperation(value = "（----对账单页面）移除对账单中的订单")
    @GetMapping("removeOrder/{accountPayId}/{orderId}")
    public Boolean removeOrderFromAccount(@PathVariable String accountPayId, @PathVariable String orderId) {
        return needPayBillService.removeOrderFromAccount(accountPayId, orderId, getTenantId());
    }

    /****************************************应付费用列表接口****************************************/

    @GetMapping("/needPay")
    @ApiOperation(value = "分页显示每单应付费用信息")
    public Page<NeedPayBaseModel> getNeedPayPage(@RequestParam Map<String, Object> params, FinanceQueryDTO financeQueryDTO) {
        System.out.println("-------------------------------------------------------");
        System.out.println(financeQueryDTO);
        financeQueryDTO.setTenantId(getTenantId());
        /*获取车辆类型下的车牌号并加入查询条件*/
        ArrayList<String> trucks = orderServiceImpl.getPlates(financeQueryDTO.getTruckAttribute());
        String[] plates = new String[trucks.size()];
        trucks.toArray(plates);
        System.out.println("-------------------------------------------------------");
        System.out.println(financeQueryDTO);
        System.out.println("-------------------------------------------------------");
        System.out.println(params);
        //如果是集装箱类型
        if (financeQueryDTO.getOrderType().equals("0")) {
            return needPayBillService.getCnNeedPayPage(new Query<>(params), financeQueryDTO);
        } else if (financeQueryDTO.getOrderType().equals("1")) {
            return needPayBillService.getPhNeedPayPage(new Query<>(params), financeQueryDTO);
        } else {
            return needPayBillService.getPdNeedPayPage(new Query<>(params), financeQueryDTO);
        }
    }

    @GetMapping("/total")
    public BigDecimal getTotalFee(FinanceQueryDTO financeQueryDTO){
        financeQueryDTO.setTenantId(getTenantId());
        /*获取车辆类型下的车牌号并加入查询条件*/
        ArrayList<String> trucks = orderServiceImpl.getPlates(financeQueryDTO.getTruckAttribute());
        String[] plates = new String[trucks.size()];
        trucks.toArray(plates);
        financeQueryDTO.setPlates(plates);
        return needPayBillService.getTotalFee(financeQueryDTO);
    }

    @GetMapping("/BillConfirm")
    @ApiOperation(value = "确认账单")
    public R<Boolean> confirmBillonPc(@RequestParam("orderId") String orderId,
                                      @RequestParam("repaynumber") String repaynumber) {
        return new R<>(needPayBillService.pcConfirmBill(orderId, repaynumber, getTenantId(), UserUtils.getUser()));
    }
    @GetMapping("/confirmAccountBill")
    @ApiOperation(value = "新增功能 -- 确认对账单 同时记录后台确认账单的操作人及时间")
    public R<Boolean> confirmAccountBill(@RequestParam("id")Integer id){
        NeedPayBill needPayBill = new NeedPayBill();
        needPayBill.setId(id);
        //确认对账单
        needPayBill.setSettlementStatus(CommonConstant.YQR);
        //记录时间和人 不想加字段了 就这样吧--
        needPayBill.setUpdateTime(new Date());
        needPayBill.setUpdateBy(UserUtils.getUser());
        return new R<>(needPayBillService.updateById(needPayBill));
    }
    @GetMapping("/sss")
    @ApiOperation(value = "请忽略")
    public CnNeedPayDetail gersss() {
        return new CnNeedPayDetail();
    }

    @GetMapping("/ss")
    @ApiOperation(value = "请忽略+1")
    public PhNeedPayDetail gesrsss() {
        return new PhNeedPayDetail();
    }

    @GetMapping("/ssss")
    @ApiOperation(value = "请忽略+2")
    public FinanceQueryDTO gesrssss() {
        return new FinanceQueryDTO();
    }

    /**
     * 给司机发送对账单
     */
    @GetMapping("/sendBillToDriver")
    @ApiOperation(value = "给司机发送对账单")
    public R<Boolean> sendBill(@RequestParam("orderId") String orderId, @RequestParam("plateNumber") String plateNumber) {
        //修改订单的应付结算状态为已发送
        return new R<>(needPayBillService.sendBill(orderId, plateNumber, getTenantId()));
    }

    @GetMapping("/designCash")
    @ApiOperation(value = "分配费用")
    @ApiImplicitParam(value = "orderId订单编号，payCash应付现金，isYFInvoice是否开票，cash现金分配")
    public R<Boolean> design(@RequestParam("orderId") String orderId,
                             @RequestParam("isYFInvoice") String isYFInvoice,
                             @RequestParam("payCash") BigDecimal payCash,
                             @RequestParam("cash") BigDecimal cash,
                             @RequestParam("transOilFee") BigDecimal transOilFee,
                             @RequestParam("etcFee") BigDecimal etcFee,
                             @RequestParam("rate") BigDecimal rate,
                             @RequestParam("oilPledge") BigDecimal oilPledge) {
        return new R<>(needPayBillService.designCash(orderId, isYFInvoice, payCash, cash, etcFee, oilPledge, transOilFee, rate));
    }

    /**
     * 查询司机接口
     */
    @PostMapping("/getDriverList")
    @ApiOperation(value = "查询司机接口")
    public List<DriverVO> getDrivers(@RequestBody DriverVO driverVO) {
        driverVO.setTenantId(getTenantId());
        return trunkFeign.selectAllDriver(driverVO);
    }

    /**
     * pc段查看账单详情
     */
    @GetMapping("/getBillDetailPc")
    @ApiOperation(value = "pc段根据订单编号查看账单详情")
    public NeedPayBaseModel getBillDetailPc(@RequestParam("orderId") String orderId) {
        return needPayBillService.getBillDetailInfoForPc(orderId, getTenantId());
    }

    /**
     * 导出应付帐单
     *
     * @param request
     * @param response
     * @param queryDTO
     * @return
     */
    @GetMapping("/exportNeedPayBill")
    @ApiOperation(value = "导出应付账单")
    public R<Boolean> exportBill(HttpServletRequest request, HttpServletResponse response, FinanceQueryDTO queryDTO) {
        FinanceQueryDTO query = new FinanceQueryDTO();
        query.setOrderType(queryDTO.getOrderType());
        if (queryDTO.getIds() != null && queryDTO.getIds() != "") {
            query.setOrderIds(Convert.toStrArray(queryDTO.getIds()));
        }
        query.setTenantId(getTenantId());
        return new R<>(needPayBillService.exportBillByQuery(request, response, query));
    }

    /**
     * 生成应付对账单
     */
    @PostMapping("/createYFDZD")
    @ApiOperation(value = "生成应付对账单")
    public R<Boolean> createDZD(@RequestBody FinanceQueryDTO queryDTO) {
        queryDTO.setTenantId(getTenantId());
        return new R<>(needPayBillService.addToDZD(queryDTO));
    }

    /**
     * 调账（弃用）
     */
    @GetMapping("/updateBill")
    @ApiOperation(value = "调账")
    public R<Boolean> updateBillByOrderId(@RequestParam("orderId") String orderId, @RequestParam(value = "transportFee", required = false) BigDecimal transportFee,
                                          @RequestParam(value = "oilFee", required = false) BigDecimal oilFee, @RequestParam(value = "pickcnFee", required = false) BigDecimal pickcnFee,
                                          @RequestParam(value = "packFee", required = false) BigDecimal packFee, @RequestParam(value = "releaseFee", required = false) BigDecimal releaseFee) {
        return new R<>(needPayBillService.updateBill(orderId, transportFee, pickcnFee, oilFee, packFee, releaseFee));
    }

    /**
     * 调账
     */
    @PutMapping("/updateBill2")
    @ApiOperation(value = "调账")
    public R<Boolean> updateBillByOrderId2(@RequestBody UpdateForNeedPayBill orderList) {
        return new R<>(needPayBillService.updateNeedPayBill(orderList));
    }

    @GetMapping("pdf")
    public boolean pdf() {
        String pdfName = "pdftest.pdf";// PDF文件名
        PdfReader pdfReader = null;
        PdfStamper pdfStamper;
        PDFUtils.createDirectory(Global.PDFPATH);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            ClassPathResource resource = new ClassPathResource("static/pdf/Finance.pdf");
            pdfReader = new PdfReader(resource.getInputStream());// 读取pdf模板

            pdfStamper = new PdfStamper(pdfReader, new FileOutputStream(
                    Global.PDFPATH + pdfName));
            AcroFields form = pdfStamper.getAcroFields();
            form.setField("printTime", simpleDateFormat.format(new Date()));
            form.setField("plateNumber", "豫A2s323");
            form.setField("driver", "老司机");
            form.setField("orderDate", "2019-05-21");
            form.setField("orderType", "集装箱");
            form.setField("totalFee", "52300");
            form.setField("cashFee","26000");
            form.setField("orderId", "CN20190521100023");
            form.setField("type", "去程");
            form.setField("addStartEnd", "郑州市-新乡市");
            pdfStamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
            pdfStamper.close();
            pdfReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return true;
    }

    @ApiOperation("月结订单ETC费用录入接口")
    @ApiImplicitParam("orderId-订单编号，etcFee- ETC费用")
    @GetMapping("setEtcFee")
    public R<Boolean> setEtcFee(String orderId, BigDecimal etcFee){
        if(orderId.substring(0,2).equals("CN")){
            OrdOrder ordOrder = new OrdOrder();
            ordOrder.setOrderId(orderId);
            ordOrder.setEtcFee(etcFee);
            ordOrder.setUpdateTime(new Date());
            ordOrder.setUpdateBy(UserUtils.getUser());
            OrdOrder param = new OrdOrder();
            param.setOrderId(orderId);
            param.setTenantId(getTenantId());
            return new R<>(orderServiceImpl.update(ordOrder,new EntityWrapper<>(param)));
        }else if(orderId.substring(0,2).equals("PH")){
            OrdCommonTruck commonTruck = new OrdCommonTruck();
            commonTruck.setEtcFee(etcFee);
            commonTruck.setOrderId(orderId);
            OrdCommonTruck param = new OrdCommonTruck();
            param.setOrderId(orderId);
            return new R<>(commonTruckService.update(commonTruck, new EntityWrapper<>(param)));
        }
        return new R<>(Boolean.FALSE);
    }

    @ApiOperation("外调车单结账单--申请运费油卡充值接口")
    @ApiImplicitParam("orderId-订单编号")
    @PostMapping("setSettlementStatus/{orderId}")
    public boolean setSettlementStatus(@PathVariable("orderId") String orderId){
        return needPayBillService.setSettlementStatus(orderId);
    }
    @ApiOperation("外调车单结账单--运费油卡充值完成接口")
    @ApiImplicitParam("orderId-订单编号,rechargeBy 充值人")
    @PostMapping("rechargeCompleted/{orderId}/{rechargeBy}")
    public boolean rechargeCompleted(@PathVariable("orderId") String orderId, @PathVariable("rechargeBy") String rechargeBy){
        return needPayBillService.rechargeCompleted(orderId, rechargeBy);
    }
}