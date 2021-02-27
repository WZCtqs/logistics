package com.zhkj.lc.trunk.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.trunk.model.Loan;
import com.zhkj.lc.trunk.model.LoanRate;

import java.util.List;


/**
 * 借款管理 服务层
 * 
 * @author zhkj
 * @date 2018-11-26
 */
public interface ILoanService extends IService<Loan>
{
    public boolean deleteByIds(String ids);
    /**
     * 分页连表查询借款信息
     *
     * @return 分页对象
     */
    public Page selectLoanTruckList(Query query, Loan loan);

    /**
     * 分页连表查询还款信息
     *
     * @return 分页对象
     */
    public Page repaymentList(Query query, Loan loan);

    /**
     * 不分页查询全部还款信息
     */
    public List<Loan> selectAllRepayment();

    Loan selectByIoanId(int loanId);

    //根据司机id和租户id查询借款信息
    List<Loan> selectByApply(Loan loan);

    Integer countNoFinishLoan(String driverIds, Integer tenantId);

}
