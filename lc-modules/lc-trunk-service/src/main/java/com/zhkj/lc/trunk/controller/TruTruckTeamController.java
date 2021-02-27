package com.zhkj.lc.trunk.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.zhkj.lc.common.constant.CommonConstant;
import com.zhkj.lc.common.util.AjaxResult;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.util.R;
import com.zhkj.lc.common.util.UserUtils;
import com.zhkj.lc.common.util.excel.ExcelUtil;
import com.zhkj.lc.common.util.support.Convert;
import com.zhkj.lc.common.web.BaseController;
import com.zhkj.lc.trunk.feign.OilCardFeign;
import com.zhkj.lc.trunk.feign.OrderFeign;
import com.zhkj.lc.trunk.model.TruDriver;
import com.zhkj.lc.trunk.model.TruTruckTeam;
import com.zhkj.lc.trunk.service.IContractService;
import com.zhkj.lc.trunk.service.ITruDriverService;
import com.zhkj.lc.trunk.service.ITruTruckTeamService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 车队信息表 前端控制器
 * </p>
 *
 * @author wzc
 * @since 2018-12-07
 */
@Api(description = "承运商管理模块")
@RestController
@RequestMapping("/truTruckTeam")
public class TruTruckTeamController extends BaseController {
    @Autowired private ITruTruckTeamService truTruckTeamService;
    @Autowired private ITruDriverService truDriverService;
    @Autowired private OilCardFeign oilCardFeign;
    @Autowired private OrderFeign orderFeign;
    @Autowired private IContractService contractService;

    /**
     * 批量删除
     * @param ids
     * @return success/false
     */
    @ApiOperation(value="批量删除承运商")
    @DeleteMapping("/delectIds/{ids}")
    public R<Boolean> deleteIds(@PathVariable String ids) {

        if (contractService.countNoFinishContract(ids, getTenantId())>0){
            return new R<>(false, "批量删除的承运商中部分承运商合同未到期，不能进行删除！");
        }

        String[] idArray = Convert.toStrArray(ids);
        if (null != idArray && idArray.length > 0) {
            for (String id : idArray) {
                if (truTruckTeamService.countDriver(Integer.parseInt(id)) > 0 || truTruckTeamService.countTruck(Integer.parseInt(id)) > 0) {
                    return new R<>(false, "批量删除的承运商中部分承运商仍有车辆和司机，请将承运商下的车辆和司机删除之后再进行删除！");
                }
            }
        }
        return new R<>(truTruckTeamService.deleteByIds(ids));
    }

    /**
     * 批量设置白名单/黑名单
     * @param ids,sTrust
     * @return success/false
     */
    @ApiOperation(value = "批量设置白名单/黑名单",notes = "isTrust传0为黑名单转白名单，传1为白名单转黑名单")
    @PostMapping("/setTrustById")
    public R<Boolean> setTrustByIds(String ids,String isTrust) {
        if ("1".equals(isTrust)) {
            String[] idsArray = Convert.toStrArray(ids);
            if (null != idsArray) {
                int[] ints = new int[idsArray.length];
                for (int i = 0; i < idsArray.length; i++) {
                    ints[i] = Integer.valueOf(idsArray[i]);
                }
                TruDriver truDriver = new TruDriver();
                truDriver.setTruckTeamIds(ints);
                List<TruDriver> list = truDriverService.selectDriverByWhiteList(truDriver);
                if (null != list && list.size() > 0) {
                    Integer[] driverIds = new Integer[list.size()];
                    for (int i = 0; i < list.size(); i++) {
                        driverIds[i] = list.get(i).getDriverId();
                    }
                    Integer countOrd = orderFeign.selectProcOrdByDriverIds(driverIds, getTenantId());
                    if (countOrd > 0){
                        return new R<>(false, "车队下有正在进行中的订单，无法移入黑名单！");
                    }
                    Integer countOilCard = oilCardFeign.countOilCard(driverIds, getTenantId());
                    if (countOilCard > 0) {
                        return new R<>(false, "车队下有油卡未进行退卡操作，无法移入黑名单！");
                    }
                }
            }
        }
        return new R<>(truTruckTeamService.setTrustByIds(ids,isTrust));
    }

    /**
     * 查找所有车队信息
     * @param truTruckTeam
     * @return
     */
    @ApiOperation(value = "查找所有承运商信息")
    @PostMapping("/selectAllTruckTeam")
    public Page selectAllTruckTeam(@RequestParam Map<String, Object> params,TruTruckTeam truTruckTeam){
        if(null == truTruckTeam.getTenantId()){
            truTruckTeam.setTenantId(getTenantId());
        }
        System.out.println("tenantId:"+getTenantId());
        return truTruckTeamService.selectAllTruckTeam(new Query<>(params),truTruckTeam);
    }
    /**
     * 通过ID查询
     *
     * @param id ID
     * @return TruTruckTeam
     */
    @ApiOperation(value = "通过ID查询承运商")
    @GetMapping("/{id}")
    public TruTruckTeam get(@PathVariable Integer id) {
        return truTruckTeamService.selectById(id);
    }


    /**
     * 添加
     * @param  truTruckTeam  实体
     * @return success/false
     */
    @ApiOperation(value = "添加承运商")
    @PostMapping
    public R<Boolean> add(@RequestBody TruTruckTeam truTruckTeam) {
        if(null == truTruckTeam.getTenantId()){
            truTruckTeam.setTenantId(getTenantId());
        }
        Integer flag = truTruckTeamService.checkTruckTeamName(truTruckTeam.getTeamName(),truTruckTeam.getTenantId());
        if(null != flag && flag ==1){
            return new R<>(Boolean.FALSE,"该车队名称已经存在！");
        }
        truTruckTeam.setCreateTime(new Date());
        truTruckTeam.setCreateBy(UserUtils.getUser());
        return new R<>(truTruckTeamService.insert(truTruckTeam));
    }

    /**
     * 删除
     * @param id ID
     * @return success/false
     */
    @ApiOperation(value = "单个删除")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable Integer id) {

        if (truTruckTeamService.countTruck(id) > 0 || truTruckTeamService.countDriver(id) > 0) {
            return new R<>(false, "该承运商仍有车辆和司机，请将该承运商下的车辆和司机删除之后再进行删除！");
        }
        if (contractService.countNoFinishContract(id.toString(), getTenantId())>0){
            return new R<>(false, "该承运商合同未到期，不能进行删除！");
        }

        TruTruckTeam truTruckTeam = new TruTruckTeam();
        truTruckTeam.setTruckTeamId(id);
        truTruckTeam.setUpdateTime(new Date());
        truTruckTeam.setDelFlag(CommonConstant.STATUS_DEL);
        return new R<>(truTruckTeamService.updateById(truTruckTeam));
    }

    /**
     * 编辑
     * @param  truTruckTeam  实体
     * @return success/false
     */
    @ApiOperation(value = "编辑")
    @PutMapping
    public R<Boolean> edit(@RequestBody TruTruckTeam truTruckTeam) {
        Integer flag = truTruckTeamService.checkTruckTeamNameById(truTruckTeam.getTeamName(),getTenantId());
        if((flag != null)  && flag !=truTruckTeam.getTruckTeamId()){
            return new R<>(Boolean.FALSE,"该车队名称已经存在！");
        }
        truTruckTeam.setUpdateTime(new Date());
        truTruckTeam.setUpdateBy(UserUtils.getUser());
        return new R<>(truTruckTeamService.updateById(truTruckTeam));
    }

    /**
     *不分页获取全部（白名单）车队
     */
    @ApiOperation(value = "不分页搜索全部（白名单）承运商")
    @PostMapping("selectAllTruct")
    public List<TruTruckTeam> selectAllTruct(){
        TruTruckTeam truTruckTeam = new TruTruckTeam();
        truTruckTeam.setTenantId(getTenantId());
        truTruckTeam.setIsTrust("0");
        return truTruckTeamService.selectAll(truTruckTeam);
    }

    /**
     *不分页获取全部（黑名单）车队
     */
    @ApiOperation(value = "不分页搜索全部（黑名单）承运商")
    @PostMapping("selectAllTructBlack")
    public List<TruTruckTeam> selectAllTructBlack(){
        TruTruckTeam truTruckTeam = new TruTruckTeam();
        truTruckTeam.setTenantId(getTenantId());
        truTruckTeam.setIsTrust("1");
        return truTruckTeamService.selectAll(truTruckTeam);
    }

    /**
     *不分页根据条件全部车队
     */
    @ApiOperation(value = "不分页根据条件全部车队")
    @PostMapping("selectTructList")
    public List<TruTruckTeam> selectAllTructTeams(@RequestBody TruTruckTeam truTruckTeam){
        return truTruckTeamService.selectAll(truTruckTeam);
    }

    /**
     * 导出车队信息
     * @param ids
     * @return
     */
    @ApiOperation(value = "导出车队信息",notes = "传'allH'导出所有黑名单，'allB'导出所有白名单，传选中id则导出选中记录")
    @GetMapping("/export/{ids}")
    public AjaxResult export(HttpServletRequest request, HttpServletResponse response, @PathVariable String ids) {
        List<TruTruckTeam> list = null;
        TruTruckTeam truTruckTeam = new TruTruckTeam();
        if("allH".equals(ids)){
            truTruckTeam.setTenantId(getTenantId());
            truTruckTeam.setIsTrust("1");
            list = truTruckTeamService.selectAll(truTruckTeam);
        }else if("allB".equals(ids)){
            truTruckTeam.setTenantId(getTenantId());
            truTruckTeam.setIsTrust("0");
            list = truTruckTeamService.selectAll(truTruckTeam);
        }else{
            list = truTruckTeamService.selectByIds(ids);
        }
        for(int i = 0; i < list.size(); i++) {
            /*if (list.get(i).getIsPassed() == null || list.get(i).getIsPassed().equals("")) {
                list.get(i).setIsPassed("未知");
            } else {
                if (list.get(i).getIsPassed().equals("0")) {
                    list.get(i).setIsPassed("通过");
                } else {
                    list.get(i).setIsPassed("未通过");
                }
            }*/
            if (list.get(i).getIsPartner() == null || list.get(i).getIsPartner().equals("")) {
                list.get(i).setIsPartner("未知");
            } else {
                if (list.get(i).getIsPartner().equals("0")) {
                    list.get(i).setIsPartner("是");
                } else {
                    list.get(i).setIsPartner("否");
                }
            }
            if (list.get(i).getIsTrust() == null || list.get(i).getIsTrust().equals("")) {
                list.get(i).setIsTrust("未知");
            } else {
                if (list.get(i).getIsTrust().equals("0")) {
                    list.get(i).setIsTrust("是");
                } else {
                    list.get(i).setIsTrust("否");
                }
            }
            list.get(i).setDriverSum(truTruckTeamService.countDriver(list.get(i).getTruckTeamId()));
            list.get(i).setTruckSum(truTruckTeamService.countTruck(list.get(i).getTruckTeamId()));
           /* if (list.get(i).getStatus() == null || list.get(i).getStatus().equals("")) {
                list.get(i).setStatus("未知");
            } else {
                if (list.get(i).getStatus().equals("0")) {
                    list.get(i).setStatus("启用");
                } else {
                    list.get(i).setStatus("禁用");
                }
            }*/
        }
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        String excelName = fmt.format(new Date())+"-车队信息";
        ExcelUtil<TruTruckTeam> util = new ExcelUtil<TruTruckTeam>(TruTruckTeam.class);
        //return util.exportExcel(response,list, "车队信息",null);
        return util.exportExcel(request,response, list,excelName,null);
    }

    /**
     * 统计某车队车辆总数
     * @param  truckTeamId
     * @return truckSum
     */
    @ApiOperation(value = "统计某车队车辆总数")
    @PostMapping("countTruck/{truckTeamId}")
    public R<Integer> countTruck(@PathVariable Integer truckTeamId) {
        return new R<>(truTruckTeamService.countTruck(truckTeamId));
    }

    /**
     * 统计某车队司机总数
     * @param  truckTeamId
     * @return driverSum
     */
    @ApiOperation(value = "统计某车队司机总数")
    @PostMapping("countDriver/{truckTeamId}")
    public R<Integer> countDriver(@PathVariable Integer truckTeamId) {
        return new R<>(truTruckTeamService.countDriver(truckTeamId));
    }

    /**
     * 查询受信任的不同类型的车队信息
     */
    @GetMapping("selectByTeamType/{teamType}")
    public List<TruTruckTeam> selectByTeamType(@PathVariable("teamType") String teamType){
        TruTruckTeam truTruckTeam = new TruTruckTeam();
        if ("0".equals(teamType)) {
            truTruckTeam.setTeamType("个体车队");
        }else {
            truTruckTeam.setTeamType("运输车队");
        }
        truTruckTeam.setIsTrust("0");
        truTruckTeam.setTenantId(getTenantId());
        return truTruckTeamService.selectAll(truTruckTeam);
    }
}
