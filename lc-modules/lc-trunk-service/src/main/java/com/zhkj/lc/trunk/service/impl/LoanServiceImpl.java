package com.zhkj.lc.trunk.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.util.support.Convert;
import com.zhkj.lc.common.vo.SysDictVO;
import com.zhkj.lc.trunk.feign.DictFeginServer;
import com.zhkj.lc.trunk.mapper.LoanMapper;
import com.zhkj.lc.trunk.mapper.LoanRateMapper;
import com.zhkj.lc.trunk.model.Loan;
import com.zhkj.lc.trunk.model.LoanRate;
import com.zhkj.lc.trunk.service.ILoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;


/**
 * 借款管理 服务层实现
 * 
 * @author zhkj
 * @date 2018-11-26
 */
@Service
public class LoanServiceImpl extends ServiceImpl<LoanMapper,Loan> implements ILoanService {
    @Autowired
    LoanMapper loanMapper;
   /* @Autowired
    DictFeginServer dictFeginServer;*/
    @Autowired
    LoanRateMapper loanRateMapper;

    /**
     * 批量删除借款/还款信息
     * @param ids
     * @return
     */
    @Override
    public boolean deleteByIds(String ids) {
        String[] str = ids.split(",");
        // 声明一个int类型的数组.数组长度和String类型的数组长度一致
        int[] loanIds = new int[str.length];
        // 对String数组进行遍历循环，并转换成int类型的数组
        for (int i = 0; i < str.length; i++) {
            loanIds[i] = Integer.parseInt(str[i]);
        }
        Loan loan = new Loan();
        loan.setLoanIds(loanIds);
        loan.setUpdateTime(new Date());
        /*oilApply.setUpdateBy("");*/
        Integer result = loanMapper.deleteByIds(loan);
        return null != result && result >= 1;
    }

    /**
     * 分页连表查询借款信息
     * @param query,loan
     * @return 分页对象
     */
    @Override
    public Page selectLoanTruckList(Query query, Loan loan) {
        List<Loan> loans = loanMapper.selectLoanTruckList(query,loan);
        for(Loan loan1 : loans){
            if(null != loan1.getRepaymentWay()){
                //SysDictVO dict = dictFeginServer.dict(loan1.getRepaymentWay());
                LoanRate loanRate = loanRateMapper.selectById(loan1.getRepaymentWay());
                if(loanRate != null){
                    loan1.setRepaymentWay(Integer.valueOf(loanRate.getLabel().replace("期还款利率","")));
                }

            }
        }
        query.setRecords(loans);
        return query;
    }

    /**
     * 分页连表查询还款信息
     * @param query,loan
     * @return 分页对象
     */
    @Override
    public Page repaymentList(Query query, Loan loan) {
        List<Loan> loans = loanMapper.repaymentList(query,loan);
        for(int i = 0; i < loans.size(); i++){
            if(null != loans.get(i).getRepaymentWay()){
                //SysDictVO dict = dictFeginServer.dict(Integer.valueOf(loans.get(i).getRepaymentWay()));
                LoanRate loanRate = loanRateMapper.selectById(loans.get(i).getRepaymentWay());
                if(loanRate != null){
                    loans.get(i).setRepaymentWay(Integer.valueOf(loanRate.getLabel().replace("期还款利率","")));
                }

                DecimalFormat df = new DecimalFormat("#.00");
                double rate = Double.parseDouble(loans.get(i).getRepaymentRate())*0.01;//利率
                double sum = loans.get(i).getApplySum().doubleValue();//借款金额
                double month = loans.get(i).getRepaymentWay();//付款方式
                //double month = Integer.valueOf(loans.get(i).getRepaymentWay());
                /*总金额 = 总额*（1+rate）/month */
//                double d = sum*rate*(Math.pow((1+rate),month))/(Math.pow((1+rate),month)-1);//每月应还金额
                double d = sum*(1+rate)/month;//每月应还金额
                loans.get(i).setRepaymentMoney(Double.valueOf(df.format(d)));
            }
        }
        query.setRecords(loans);
        return query;
    }

    /**
     * 不分页查询全部还款信息
     */
    @Override
    public List<Loan> selectAllRepayment(){
        return loanMapper.selectAllRepayment();
    }

    @Override
    public Loan selectByIoanId(int loanId) {
        return loanMapper.selectByIoanId(loanId);
    }

    //根据司机id和租户id查询借款信息
    @Override
    public List<Loan> selectByApply(Loan loan){
        return loanMapper.selectByApply(loan);
    }

    @Override
    public Integer countNoFinishLoan(String driverIds, Integer tenantId) {
        return loanMapper.countNoFinishLoan(Convert.toStrArray(driverIds), tenantId);
    }

}
