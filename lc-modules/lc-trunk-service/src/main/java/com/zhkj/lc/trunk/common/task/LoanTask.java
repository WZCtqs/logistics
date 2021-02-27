package com.zhkj.lc.trunk.common.task;

import com.zhkj.lc.trunk.model.Loan;
import com.zhkj.lc.trunk.service.ILoanService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @description  借款模块定时任务
 * @author yl
 * @date  2019/02/13
 */
@Component
public class LoanTask {
    @Autowired
    private ILoanService loanService;

    private static Logger logger = Logger.getLogger(LoanTask.class);

    /**
     * 月初还款完成状态的变更，还款时间每月递增变更
     */
    @Scheduled(cron = "0 0 1 1 * ?")//每月1号执行
    public void repayment(){
        //查询全部借款信息
        logger.info("还款管理已执行定时任务");
        List<Loan> list = loanService.selectAllRepayment();
        if(list.size() > 0){
            for(Loan loan : list){
                Calendar cale = Calendar.getInstance();
                cale.setTime(loan.getApplyDate());
                int applyMonth = cale.get(Calendar.MONTH) + 1;
                Calendar cale1 = Calendar.getInstance();
                cale1.setTime(new Date());
                int nowMonth = cale1.get(Calendar.MONTH) + 1;
                int d = (nowMonth+12-applyMonth)%12-1;
                //判断是否按照期限还完借款
                if(Integer.valueOf(loan.getRepaymentWay()) <= d){
                    //logger.info("司机"+loan.getApplyMan()+"已完成还款");
                    loan.setStatus("3");
                }else{
                    //logger.info("司机"+loan.getApplyMan()+"未完成还款");
                    Calendar cale2 = Calendar.getInstance();
                    cale2.setTime(loan.getRepaymentDate());
                    cale2.add(Calendar.MONTH,+1);
                    loan.setRepaymentDate(cale2.getTime());
                }
                loanService.updateById(loan);
            }
        }
    }
}
