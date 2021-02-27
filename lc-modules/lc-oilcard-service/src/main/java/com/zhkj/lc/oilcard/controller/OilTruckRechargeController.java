package com.zhkj.lc.oilcard.controller;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Date;

import com.zhkj.lc.common.util.*;
import com.zhkj.lc.common.util.excel.ExcelUtil;
import com.zhkj.lc.common.vo.OilTruckRechargeVO;
import com.zhkj.lc.oilcard.model.OilTruckMonthRecharge;
import com.zhkj.lc.oilcard.service.IOilTruckMonthRechargeService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.zhkj.lc.common.constant.CommonConstant;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.zhkj.lc.oilcard.model.OilTruckRecharge;
import com.zhkj.lc.oilcard.service.IOilTruckRechargeService;
import com.zhkj.lc.common.web.BaseController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author JHM
 * @since 2019-05-29
 */
@RestController
@RequestMapping("/oilTruckRecharges")
public class OilTruckRechargeController extends BaseController {
    @Autowired private IOilTruckRechargeService oilTruckRechargeService;
    @Autowired private IOilTruckMonthRechargeService oilTruckMonthRechargeService;
    /**
    * 通过ID查询
    *
    * @param id ID
    * @return OilTruckRecharge
    */
    @GetMapping("/{id}")
    public R<OilTruckRecharge> get(@PathVariable Integer id) {
        return new R<>(oilTruckRechargeService.selectById(id));
    }


    /**
    * 分页查询信息
    *
    * @param params 分页对象
    * @return 分页对象
    */
    @RequestMapping("/page")
    public Page page(@RequestParam Map<String, Object> params) {
        params.put(CommonConstant.DEL_FLAG, CommonConstant.STATUS_NORMAL);
        return oilTruckRechargeService.selectPage(new Query<>(params), new EntityWrapper<>());
    }

    /**
     * 添加
     * @param  oilTruckRecharge  实体
     * @return success/false
     */
    @PostMapping
    public R<Boolean> add(@RequestBody OilTruckRecharge oilTruckRecharge) {
        return new R<>(oilTruckRechargeService.insert(oilTruckRecharge));
    }

    /**
     * 删除
     * @param id ID
     * @return success/false
     */
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable Integer id) {
        OilTruckRecharge oilTruckRecharge = new OilTruckRecharge();
        oilTruckRecharge.setId(id);
        return new R<>(oilTruckRechargeService.updateById(oilTruckRecharge));
    }

    /**
     * 编辑
     * @param  oilTruckRecharge  实体
     * @return success/false
     */
    @PutMapping
    public R<Boolean> edit(@RequestBody OilTruckRecharge oilTruckRecharge) {
        return new R<>(oilTruckRechargeService.updateById(oilTruckRecharge));
    }


    @ApiOperation(value = "运费油卡汇总列表", notes = "运费油卡汇总列表;")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "params",value = "分页参数,可选字段:page,limit",paramType = "query",dataType = "Map"),
            @ApiImplicitParam(name = "OilTruckRechargeVO", value = "rechargeTime（日期），plateNumber（车牌号）,attribute（车辆类型）", paramType = "query", dataType = "OilTruckRechargeVO")
    })
    @PostMapping("/getAllTruckRecharges")
    public Page<OilTruckRechargeVO> getAllTruckRecharges(@RequestParam Map<String, Object> params,@RequestBody OilTruckRechargeVO oilTruckRecharge) {
        System.out.println("----"+oilTruckRecharge);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM");
        if(oilTruckRecharge.getRechargeTime() == null || ("").equals(oilTruckRecharge.getRechargeTime())){
            oilTruckRecharge.setRechargeTime(sf.format(new Date()));
        }
        oilTruckRecharge.setTenantId(getTenantId());
       return oilTruckMonthRechargeService.selectOilTruckRecharge(new Query(params),oilTruckRecharge);

    }



    @ApiOperation(value = "搜索导出", notes = "导出全部;")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "OilTruckRecharges", value = "rechargeTime（日期），plateNumber（车牌号）,attribute（车辆类型）r", paramType = "query", dataType = "OilTruckRecharges")
    })
    @PostMapping("/export/{ids}")
    public AjaxResult export(@PathVariable("ids") String ids,OilTruckRechargeVO oilTruckRechargeVO, HttpServletRequest request, HttpServletResponse response) {

        List<OilTruckRechargeVO> list;
        if ("all".equals(ids)) {
            oilTruckRechargeVO.setTenantId(getTenantId());
            list = oilTruckMonthRechargeService.selectOilTruckRechargeList(oilTruckRechargeVO);
        }else {
            list = oilTruckMonthRechargeService.selectOilTruckRechargeByIds(ids);
        }
        String sheetName = DateUtils.getDate() + "-车辆运费油卡汇总";
        ExcelUtil<OilTruckRechargeVO> util = new ExcelUtil<>(OilTruckRechargeVO.class);
        return util.exportExcel(request, response, list, sheetName, null);
    }


    //查询对应车辆所有详细充值记录
    @ApiOperation(value = "查询对应车辆所有详细充值记录详情", notes = "查询对应车辆所有详细充值记录详情;")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "truckId", value = "truckId（车辆类型）", paramType = "truckId", dataType = "Integer")
    })
    @GetMapping("/details")
    public Page<OilTruckRecharge> findDetailOilTruckRechargesVOList(@RequestParam Map<String, Object> params,@RequestParam Integer truckId){
        return oilTruckRechargeService.findAllTruckRechargeRecordsByTruckId(new Query(params),truckId,getTenantId());

    }



    /*插入一条充值记录*/
    @ApiOperation(value = "外调服务：添加", notes = "")
    @ApiImplicitParam(name = "oilTruckRecharges", value ="添加分配充值记录" , paramType = "body", dataType = "OilTruckRecharges", required = true)
    @PostMapping("/add/feign")
    public boolean addOilTruckRecharge (@RequestBody OilTruckRecharge oilTruckRecharge) {
        Integer truckId = oilTruckRecharge.getTruckId();
//        String userId = UserUtils.getUser();
//        if (truckId == null || oilTruckRecharge.getTenantId()== null) {
//            return false;
//        }
//        System.out.println("kongma" + oilTruckRecharge.);
        oilTruckRecharge.setCreateBy(oilTruckRecharge.getCreateBy());
        //oilTruckRecharge.setTenantId(getTenantId());
        oilTruckRecharge.setRechargeTime(new Date());
        OilTruckMonthRecharge oilTruckMonthRecharge = oilTruckMonthRechargeService.selectByTruckIdWithYearMonth(truckId, oilTruckRecharge.getTenantId(), DateUtils.getDateYM());
            if (oilTruckMonthRecharge != null) {
                System.out.println("jinlaill1==="+oilTruckMonthRecharge);
                oilTruckMonthRecharge.setBalance(oilTruckMonthRecharge.getBalance().add(oilTruckRecharge.getRecharge()));//本月余额
                oilTruckMonthRecharge.setRechargeSum(oilTruckMonthRecharge.getRechargeSum().add(oilTruckRecharge.getRecharge()));//充值总金额
                oilTruckMonthRechargeService.updateTruckMonthRecharge(oilTruckMonthRecharge);
            }else{
                oilTruckMonthRecharge = new OilTruckMonthRecharge();
                oilTruckMonthRecharge.setTruckId(truckId);
                oilTruckMonthRecharge.setYearMonth(DateUtils.getDateYM());
                oilTruckMonthRecharge.setTenantId(oilTruckRecharge.getTenantId());
                oilTruckMonthRecharge.setRechargeSum(oilTruckRecharge.getRecharge());
                oilTruckMonthRecharge.setBalance(oilTruckRecharge.getRecharge());//新的一条余额和充值金额相等
                oilTruckMonthRecharge.setCreateBy(oilTruckRecharge.getCreateBy());
                oilTruckMonthRechargeService.insertTruckMonthRecharge(oilTruckMonthRecharge);

            }
        oilTruckRecharge.setBalance(oilTruckMonthRecharge.getBalance());//车辆油卡充值记录表当前余额
        return  oilTruckRechargeService.insertOilTruckRecharge(oilTruckRecharge);

    }


    @GetMapping("/getBalance/{truckId}/{tenantId}")
    public OilTruckMonthRecharge findBalanceByNowMonth(@PathVariable("truckId") Integer truckId,@PathVariable("tenantId") Integer tenantId){
       String nowDate =  DateUtils.getDateYM();
        OilTruckMonthRecharge oilTruckMonthRecharge = oilTruckMonthRechargeService.selectByTruckIdWithYearMonth(truckId,tenantId,nowDate);
        return oilTruckMonthRecharge;
    }
}
