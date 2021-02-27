package com.zhkj.lc.trunk.controller;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Date;

import com.zhkj.lc.common.util.UserUtils;
import com.zhkj.lc.common.vo.FeeVO;
import com.zhkj.lc.common.vo.TruckTeamFeeVO;
import com.zhkj.lc.trunk.model.TruTruckFee;
import com.zhkj.lc.trunk.service.ITruTruckFeeService;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.zhkj.lc.common.constant.CommonConstant;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.util.R;
import com.zhkj.lc.common.web.BaseController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author cb
 * @since 2019-02-11
 */
@RestController
@RequestMapping("/truTruckFee")
public class TruTruckFeeController extends BaseController {
    @Autowired private ITruTruckFeeService truTruckFeeService;

    /**
    * 通过ID查询
    *
    * @param id ID
    * @return TruTruckFee
    */
    @GetMapping("/{id}")
    @ApiOperation(value = "根据id获取车辆费用信息")
    public R<TruTruckFee> get(@PathVariable Integer id) {
        return new R<>(truTruckFeeService.selectById(id));
    }


    /**
    * 分页查询信息
    *
    * @param params 分页对象
    * @return 分页对象
    */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询车辆费用信息")
    public Page page(@RequestParam Map<String, Object> params,@RequestParam(required = false) Integer truckId,@RequestParam(required = false) String feeMonth) {
        return truTruckFeeService.selectTruckFeePage(new Query<>(params), truckId,feeMonth, getTenantId());
    }

    /**
     * 添加
     * @param  truTruckFee  实体
     * @return success/false
     */
    @PostMapping
    @ApiOperation(value = "新增车辆费用信息")
    public R<Boolean> add(@RequestBody TruTruckFee truTruckFee) {
        truTruckFee.setCreateTime(new Date());
        truTruckFee.setCreateBy(UserUtils.getUser());
        truTruckFee.setDelFlag(CommonConstant.STATUS_NORMAL);
        truTruckFee.setTenantId(getTenantId());
        return new R<>(truTruckFeeService.insert(truTruckFee));
    }

    /**
     * 删除
     * @param ids
     * @return success/false
     */
    @DeleteMapping("/{ids}")
    @ApiOperation(value = "批量删除车辆费用信息")
    public R<Boolean> delete(@PathVariable String ids) {
        List<TruTruckFee> truTruckFees = new ArrayList<>();
        String []tids = ids.split(",");
        for (int i = 0;i<tids.length;i++){
            TruTruckFee truTruckFee = new TruTruckFee();
            truTruckFee.setId(Integer.parseInt(tids[i]));
            truTruckFee.setUpdateTime(new Date());
            truTruckFee.setUpdateBy(UserUtils.getUser());
            truTruckFee.setDelFlag(CommonConstant.STATUS_DEL);
            truTruckFees.add(truTruckFee);
        }

        return new R<>(truTruckFeeService.updateBatchById(truTruckFees));
    }

    /**
     * 编辑
     * @param  truTruckFee  实体
     * @return success/false
     */
    @PutMapping
    @ApiOperation(value = "编辑保存车辆费用信息")
    public R<Boolean> edit(@RequestBody TruTruckFee truTruckFee) {
        truTruckFee.setUpdateTime(new Date());
        truTruckFee.setUpdateBy(UserUtils.getUser());
        return new R<>(truTruckFeeService.updateById(truTruckFee));
    }

    @PostMapping("/sumOtherFeeLast7days")
    public TruckTeamFeeVO selectTruckFeeByDriverLast7days(FeeVO feeVO){
        return truTruckFeeService.selectTruckFeeByDriverLast7days(feeVO);
    }

    @PostMapping("/sumOtherFeeMonthdays")
    public TruckTeamFeeVO selectTruckFeeByDriverMonthdays(FeeVO feeVO){
        return truTruckFeeService.selectTruckFeeByDriverMonthdays(feeVO);
    }

    @PostMapping("/sumOtherFeeCurrentSeason")
    public TruckTeamFeeVO selectTruckFeeByDriverCurrentSeason(FeeVO feeVO){
        return truTruckFeeService.selectTruckFeeByDriverCurrentSeason(feeVO);
    }

    @PostMapping("/sumOtherFeeLast6Months")
    public TruckTeamFeeVO selectTruckFeeByDriverLast6Months(FeeVO feeVO){
        return truTruckFeeService.selectTruckFeeByDriverLast6Months(feeVO);
    }

    @PostMapping("/sumOtherFeeSometime")
    public TruckTeamFeeVO selectTruckFeeByDriverSometime(FeeVO feeVO){
        return truTruckFeeService.selectTruckFeeByDriverSometime(feeVO);
    }
}
