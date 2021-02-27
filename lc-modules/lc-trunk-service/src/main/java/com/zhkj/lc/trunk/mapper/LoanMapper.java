package com.zhkj.lc.trunk.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.trunk.model.Loan;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 借款管理 数据层
 * 
 * @author zhkj
 * @date 2018-11-26
 */
public interface LoanMapper extends BaseMapper<Loan>
{

    Integer deleteByIds(Loan loan);

    List<Loan> selectLoanTruckList(Query query, Loan loan);

    //分页查询还款列表
    List<Loan> repaymentList(Query query,Loan loan);

    //不分页查询还款列表
    List<Loan> selectAllRepayment();

    Loan selectByIoanId(@Param("loanId") int loanId);

    //根据司机id和租户id查询借款信息
    List<Loan> selectByApply(Loan loan);

    // 根据司机id和租户id查询借款未完成的信息
    Integer countNoFinishLoan(@Param("driverIds") String[] driverIds, @Param("tenantId") Integer tenantId);
}