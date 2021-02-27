package com.zhkj.lc.trunk.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.zhkj.lc.common.constant.CommonConstant;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.util.R;
import com.zhkj.lc.common.util.UserUtils;
import com.zhkj.lc.common.web.BaseController;
import com.zhkj.lc.trunk.model.Announcement;
import com.zhkj.lc.trunk.model.Loan;
import com.zhkj.lc.trunk.model.LoanRate;
import com.zhkj.lc.trunk.service.AnnouncementService;
import com.zhkj.lc.trunk.service.ILoanService;
import com.zhkj.lc.trunk.service.LoanRateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 借款管理 前端控制器
 * </p>
 *
 * @author wzc
 * @since 2018-12-07
 */
@Api(description = "借款信息管理接口")
@RestController
@RequestMapping("/loan")
public class LoanController extends BaseController {
    @Autowired private ILoanService loanService;
    //@Autowired private DictFeginServer dictFeginServer;
    @Autowired private AnnouncementService announcementService;
    @Autowired private LoanRateService loanRateService;
    /**
     * 分页连表查询信息
     *
     * @param params 分页对象
     * @return 分页对象
     */
    @ApiOperation(value = "分页查询借款信息",notes = "搜索条件：plateNumber车牌号,driverName借款人，beginTime开始时间endTime结束时间")
    @PostMapping("/selectLoanTruckList")
    public Page selectLoanTruckList(@RequestParam Map<String, Object> params,Loan loan) {
        params.put(CommonConstant.DEL_FLAG, CommonConstant.STATUS_NORMAL);
        //loan.setTenantId(0);
        loan.setTenantId(getTenantId());
        return loanService.selectLoanTruckList(new Query<>(params), loan);
    }

    /**
     * 分页连表查询还款信息
     *
     * @param params 分页对象
     * @return 分页对象
     */
    @ApiOperation(value = "分页查询还款信息",notes = "搜索条件：plateNumber车牌号,driverName借款人，beginTime开始时间endTime结束时间")
    @PostMapping("/repaymentList")
    public Page repaymentList(@RequestParam Map<String, Object> params,Loan loan) {
        //params.put(CommonConstant.DEL_FLAG, CommonConstant.STATUS_NORMAL);
        //loan.setTenantId(0);
        if(null == loan.getTenantId()){
            loan.setTenantId(getTenantId());
        }
        return loanService.repaymentList(new Query<>(params), loan);
    }

    /**
     * 批量删除
     * @param ids
     * @return success/false
     */
    @ApiOperation(value = "删除或者批量删除借款信息")
    @ApiImplicitParam(name = "ids",value = "ID数组",paramType = "path",required = true,example = "1,2")
    @DeleteMapping("/delectIds/{ids}")
    public R<Boolean> deleteIds(@PathVariable String ids) {
        return new R<>(loanService.deleteByIds(ids));
    }

    /**
     * 借款审核
     * 修改借款状态
     * @param loan1
     * @return
     */
    @ApiOperation(value = "借款审核",notes = "0待审核 1拒绝 2通过 3还款完成")
    @PostMapping("/changeStatus")
    public R<Boolean> changeStatus(@RequestBody Loan loan1){
        Loan loan = loanService.selectByIoanId(loan1.getLoanId());
        Calendar cale = Calendar.getInstance();
        cale.setTime(new Date());
        Announcement announcement = new Announcement();
        if(0==loan.getIsDriver()){
            announcement.setDriverOwerId(loan.getApplyMan());
        }else if(1==loan.getIsDriver()){
            announcement.setTruckOwnId(loan.getApplyMan());
        }
        announcement.setTenantId(loan.getTenantId());
        announcement.setTitle("借款反馈信息");
        announcement.setType("0");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        switch (loan1.getStatus()){
            case "0": loan.setStatus("0"); break;//待审核
            case "1":
                loan.setStatus("1");
                announcement.setContent("您申请日期为："+format.format(loan.getApplyDate())+"借款金额为"+loan.getApplySum()+"的申请已被拒绝");
                break;//拒绝
            case "2":
                loan.setStatus("2");
                cale.add(Calendar.MONTH,+1);
                loan.setRepaymentDate(cale.getTime());
                announcement.setContent("您申请日期为："+format.format(loan.getApplyDate())+"借款金额为"+loan.getApplySum()+"的申请已通过");
                break;//通过
            case "3":
                loan.setStatus("3");
                announcement.setContent("您申请日期为："+format.format(loan.getApplyDate())+"借款金额为"+loan.getApplySum()+"已还清");
                break;//还款完成
        }
        loan.setUpdateTime(new Date());
        loan.setCreateTime(new Date());
        loan.setCreateBy(UserUtils.getUser());
        announcement.setCreateTime(new Date());
        announcementService.insert(announcement);
        return new R<>(loanService.updateById(loan));
    }

    /**
     * 通过ID查询
     *
     * @param id ID
     * @return Loan
     */
    @ApiOperation(value = "通过ID查询（借款）")
    @GetMapping("/{id}")
    public Loan get(@PathVariable Integer id) {
        return loanService.selectById(id);
    }

    /**
     * 还款记录查询
     */
    @ApiOperation(value = "查询某借款记录还款记录详情")
    @PostMapping("selectRepaymentById/{id}")
    public Loan selectRepaymentById(@PathVariable Integer id){
        Loan loan = loanService.selectById(id);
        if(null != loan.getApplyDate() && null != loan.getRepaymentWay()){
            LoanRate loanRate = loanRateService.selectById(loan.getRepaymentWay());
            loan.setReturnPeriodSum(Integer.valueOf(loanRate.getLabel().replace("期还款利率","")));
            DecimalFormat df = new DecimalFormat("#.00");
            double rate = Double.parseDouble(loan.getRepaymentRate())*0.01; //利率
            double sum = loan.getApplySum().doubleValue();                  //金额
            double month = loan.getReturnPeriodSum();                       //期份
            double d = sum*(rate+1)/month;                                  //每期费用
            loan.setRepaymentMoney(Double.valueOf(df.format(d)));

            Calendar cale = Calendar.getInstance();
            cale.setTime(new Date());
            int nowMonth = cale.get(Calendar.MONTH) + 1;
            Calendar cale1 = Calendar.getInstance();
            cale1.setTime(loan.getApplyDate());
            int applyMonth = cale1.get(Calendar.MONTH) + 1;
            int period = (nowMonth+12-applyMonth)%12-1;
            if(period >= 0){
                loan.setReturnPeriod(period);
            }else{
                loan.setReturnPeriod(0);
            }
        }else{
            loan.setReturnPeriod(0);
        }
        return loan;
    }

    /**
     * 添加
     * @param  loan  实体
     * @return success/false
     */
    @ApiOperation(value = "添加",notes = "车牌号字段不保存，申请人字段存司机id,还款方式只保存分期的期数")
    @ApiImplicitParam(name = "loan",value = "借款信息类",paramType = "body",dataType = "Loan",required = true)
    @PostMapping
    public R<Boolean> add(@RequestBody Loan loan) {
        System.out.println(loan.toString());
        if(null == loan.getTenantId()){
            loan.setTenantId(getTenantId());
        }
        if(null == loan.getCreateBy()){
            loan.setCreateBy(UserUtils.getUser());
        }
        if(null == loan.getRepaymentWay() || null== loan.getRepaymentRate()){
            return new R<>(false);
        }
        /*
        // 借款账号申请判断，支付宝号、银行卡号
        }*/
        loan.setCreateTime(new Date());
        loan.setApplyDate(new Date());
        return new R<>(loanService.insert(loan));
    }

    /**
     * 删除
     * @param id ID
     * @return success/false
     */
    @ApiOperation(value = "通过id删除",notes = "通过某个id删除")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable Integer id) {
        Loan loan = new Loan();
        loan.setLoanId(id);
        loan.setUpdateTime(new Date());
        loan.setDelFlag(CommonConstant.STATUS_DEL);
        return new R<>(loanService.updateById(loan));
    }

    /**
     * 编辑
     * @param  loan  实体
     * @return success/false
     */
    @ApiOperation(value = "编辑修改借款信息",notes = "id是必要的")
    @ApiImplicitParam(name = "loan",value = "借款信息",dataType = "Loan",paramType = "body",required = true)
    @PutMapping
    public R<Boolean> edit(@RequestBody Loan loan) {
        loan.setUpdateTime(new Date());
        loan.setUpdateBy(UserUtils.getUser());
        return new R<>(loanService.updateById(loan));
    }

    /**
     * 小程序端借款利率查询
     */
    @ApiOperation(value = "借款利率查询")
    @PostMapping("wechatRate")
    public List<LoanRate> repaymentWechatRate(Integer tenantId){
        return loanRateService.selectAllBytenantId(tenantId);
        //return dictFeginServer.wechatSelectDictByType("repayment_rate",tenantId);
    }

    /**
     * 借款利率查询
     */
    @ApiOperation(value = "借款利率查询")
    @PostMapping("repaymentRate")
    public List<LoanRate> repaymentRate(){
        return loanRateService.selectAllBytenantId(getTenantId());
        //return dictFeginServer.findDictByTypeForFegin("repayment_rate");
    }

    /**
     * 借款利率修改
     */
    @ApiOperation(value = "单个借款利率修改",notes = "id,rate利率值,这两个个字段是必须的")
    @PostMapping("EditRepaymentRate")
    public Boolean EditRepaymentRate(@RequestBody LoanRate loanRate){
        return loanRateService.updateById(loanRate);
        //return dictFeginServer.editDict(sysDictVO);
    }

    /**
     * 小程序端借款历史查询
     */
    @ApiOperation("小程序端借款历史查询")
    @PostMapping("selectByApply")
    public List<Loan> selectByApply(Loan loan){
        List<Loan> list = loanService.selectByApply(loan);
        return list;
    }
}
