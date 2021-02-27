package com.zhkj.lc.trunk.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.gson.Gson;
import com.zhkj.lc.common.api.YunPianSMSUtils;
import com.zhkj.lc.common.constant.CommonConstant;
import com.zhkj.lc.common.constant.SecurityConstants;
import com.zhkj.lc.common.util.AjaxResult;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.util.R;
import com.zhkj.lc.common.util.UserUtils;
import com.zhkj.lc.common.util.excel.ExcelUtil;
import com.zhkj.lc.common.util.support.Convert;
import com.zhkj.lc.common.vo.DriverVO;
import com.zhkj.lc.common.web.BaseController;
import com.zhkj.lc.trunk.feign.OilCardFeign;
import com.zhkj.lc.trunk.model.FreightRoute;
import com.zhkj.lc.trunk.model.TruDriver;
import com.zhkj.lc.trunk.model.TruTruck;
import com.zhkj.lc.trunk.model.TruTruckTeam;
import com.zhkj.lc.trunk.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

/**
 * <p>
 * 司机信息表 前端控制器
 * </p>
 *
 * @author wzc
 * @since 2018-12-07
 */
@Api(description = "司机信息管理接口")
@RestController

@AllArgsConstructor
@RequestMapping("/truDriver")
public class TruDriverController extends BaseController {
    @Autowired private ITruDriverService truDriverService;
    @Autowired private ITruTruckService truTruckService;
    @Autowired private IFreightRouteService freightRouteService;
    @Autowired private ITruTruckTeamService truTruckTeamService;
    @Autowired private OilCardFeign oilCardFeign;
    @Autowired private ILoanService loanService;
    @Autowired HttpServletRequest request;

    private final RedisTemplate redisTemplate;

    @PostMapping("/selectAllDriverByTenantId")
    public List<TruDriver> selectAllDriverByTenantId(@RequestBody TruDriver truDriver){
        return truDriverService.selectAllDriverByTenantId(truDriver);
    }

    /**
     * 查找所有司机车牌号信息
     * @return
     */
    @ApiOperation(value = "查找所有司机车牌号信息")
    @GetMapping("/selectDriverPlateNumber")
    public List<TruDriver> selectDriverPlateNumber() {
        TruDriver truDriver = new TruDriver();
        truDriver.setTenantId(getTenantId());
        return truDriverService.selectDriverPlateNumber(truDriver);
    }

    /**
     * 查找所有白名单车队司机信息
     * @param truDriver
     * @return
     */
    @ApiOperation(value = "查找所有司机信息")
    @PostMapping("/selectDriverByWhiteList")
    public List<TruDriver> selectDriverByWhiteList(TruDriver truDriver){
        TruTruckTeam truTruckTeam = new TruTruckTeam();
        truTruckTeam.setTenantId(getTenantId());
        truTruckTeam.setIsTrust("0");
        //查找白名单车队
        List<TruTruckTeam> truTruckTeams = truTruckTeamService.selectAll(truTruckTeam);
        if (truTruckTeams.size() > 0){
            int[] truckTeamIds = new int[truTruckTeams.size()];
            for (int i=0 ;i< truTruckTeams.size(); i++) {
                truckTeamIds[i] = truTruckTeams.get(i).getTruckTeamId();
            }
            truDriver.setTruckTeamIds(truckTeamIds);
        }
        truDriver.setTenantId(getTenantId());
        return truDriverService.selectDriverByWhiteList(truDriver);
    }
    /**
     * 查找所有司机信息
     * @param truDriver
     * @return
     */
    @ApiOperation(value = "查找所有司机信息")
    @PostMapping("/selectAllDriver")
    public List<TruDriver> selectAllDriver(TruDriver truDriver){
        if(null == truDriver.getTenantId()){
            truDriver.setTenantId(getTenantId());
        }
        return truDriverService.selectAllDriver(truDriver);
    }

    /**
     * 跨服务
     * 查找所有司机信息
     * @param truDriver
     * @return
     */
    @ApiOperation(value = "查找所有司机信息")
    @PostMapping("/selectAllDrivers")
    public List<TruDriver> selectAllDrivers(@RequestBody TruDriver truDriver){
        return truDriverService.selectAllDriver(truDriver);
    }

    /**
     * 通过姓名查询
     *
     * @param driverName 司机名
     * @return TruDriver
     */
    @GetMapping("/selectDriverByName/{driverName}")
    public TruDriver getDriverByName(@PathVariable String driverName) {
        TruDriver truDriver = new TruDriver();
        truDriver.setTenantId(getTenantId());
        truDriver.setDriverName(driverName);
        return truDriverService.selectDriverByName(truDriver);
    }
    /**
     * 通过姓名查询的又一版本
     *
     * @param driverName 司机名
     * @return TruDriver
     */
    @GetMapping("/selectDriverByName/{driverName}/{tenantId}")
    public TruDriver getDriverByName(@PathVariable String driverName,@PathVariable Integer tenantId) {
        TruDriver truDriver = new TruDriver();
        truDriver.setTenantId(tenantId);
        truDriver.setDriverName(driverName);
        return truDriverService.selectDriverByName(truDriver);
    }
    /**
     * 通过手机号查询
     * @return boolean
     */
    @PostMapping("/selectDriverPhone")
    public boolean selectDriverPhone(@RequestBody TruDriver truDriver) {
        List<TruDriver> truDrivers = truDriverService.selectDriverPhone(truDriver);
        boolean b = true;
        if (truDrivers.size() > 0) {
            b = false;
        }
        return b;
    }

    /**
     * 通过司机id查询司机详细信息
     * @return boolean
     */
    @GetMapping("/selectDriverDetailById/{id}")
    public TruDriver selectDriverDetailById(@PathVariable Integer id) {
        TruDriver truDriver = new TruDriver();
       // truDriver.setTenantId(getTenantId());
        truDriver.setDriverId(id);
        TruDriver truDrivers = truDriverService.selectDriverDetailById(truDriver);
        return truDrivers;
    }

    /**
     * 跨服务：通过司机id查询司机详细信息
     * @return boolean
     */
    @ApiIgnore
    @GetMapping("/selectDriverById")
    public TruDriver selectDriverDetailByIdFeign(@RequestParam("id") Integer id) {
        TruDriver truDriver = new TruDriver();
        truDriver.setDriverId(id);
        TruDriver truDrivers = truDriverService.selectDriverDetailById(truDriver);
        return truDrivers;
    }


    /**
     * 根据车辆id查询所有司机
     */
    @ApiOperation(value = "根据车辆id查询所有司机")
    @ApiImplicitParam(name = "attribute",value = "车辆类型",paramType = "path",required = true,example = "0")
    @PostMapping("/selectPlateNumberByplateId")
    public List<TruDriver> selectDriverByplateId(@RequestBody  TruDriver truDriver) {
       // TruDriver truDriver = new TruDriver();
        //truDriver.setTenantId(getTenantId());
       // truDriver.setPlateId(plateId);
        return truDriverService.selectDriverByplateId(truDriver);
    }

    /**
     * 根据车辆id查询所有司机
     */
    @ApiOperation(value = "根据车辆id查询所有司机")
    @ApiImplicitParam(name = "attribute",value = "车辆类型",paramType = "path",required = true,example = "0")
    @PostMapping("/selectDriverByplateId")
    public List<TruDriver> selectDriverByTruck(@RequestBody  TruDriver truDriver) {
        // TruDriver truDriver = new TruDriver();
        //truDriver.setTenantId(getTenantId());
        // truDriver.setPlateId(plateId);
        return truDriverService.selectDriverByTruck(truDriver);
    }
    /**
     * 连表查询车主信息
     * @param params
     * @return
     */
    @PostMapping("/selectDriverTruck")
    public Page selectDriverTruck(@RequestParam Map<String, Object> params) {
        params.put(CommonConstant.DEL_FLAG, CommonConstant.STATUS_NORMAL);
        return truDriverService.selectDriverTruck(new Query<>(params),new EntityWrapper<>());
    }


    /**
     * 通过ID查询
     *
     * @param id ID
     * @return TruDriver
     */
    @GetMapping("/{id}")
    public TruDriver get(@PathVariable Integer id) {
        return truDriverService.selectById(id);
    }


    /**
     * 分页查询司机详细信息
     * @param params 分页对象
     * @return 分页对象
     */
    @ApiOperation(value = "分页查询司机详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "params",value = "分页参数：page页,size页数据量",paramType = "query",dataType = "Map"),
            @ApiImplicitParam(name = "truDriver",value = "搜索条件,字段可选:plateId(车牌号的id),status,driverName,phone",paramType = "query",dataType = "TruDriver")
    })
    @PostMapping("/selectDriverDetailList")
    public Page selectDriverDetailList(@RequestParam Map<String, Object> params,TruDriver truDriver) {
        params.put(CommonConstant.DEL_FLAG, CommonConstant.STATUS_NORMAL);
        truDriver.setIsTrust("0");
        truDriver.setTenantId(getTenantId());
        return truDriverService.selectDriverDetailList(new Query<>(params),truDriver);
    }

    /**
     * 添加司机信息
     * 返回新添加的信息的id
     * @param  truDriver  实体
     * @return success/false
     */
    @ApiOperation(value = "添加司机信息和常跑路线")
    @ApiImplicitParam(name = "truDriver",value = "司机信息类",paramType = "body",dataType = "TruDriver",required = true)
    @Transactional
    @PostMapping("/addDriver")
    public R<Boolean> addDriver(@RequestBody TruDriver truDriver) {
        if (null == truDriver.getTenantId()) {
            truDriver.setTenantId(getTenantId());
        }
        // 车辆下不超过2个司机
        if (null != truDriver.getPlateId()) {
            Integer count = truTruckService.countDrivers(truDriver.getPlateId());
            if (count >= 2) {
                return new R<>(false, "车辆下对应的司机人数不能超过2个！");
            }
            /*如果车辆没司机，则司机状态为空闲*/
            if (count == 0) {
                truDriver.setStatus(CommonConstant.SJKX);
            }
            /*如果有一个司机，则状态安照该司机状态*/
            if(count == 1){
                List<TruDriver> status = truDriverService.selectDriverStatusByTruckId(truDriver.getPlateId());
                if(status !=null){
                    if(status.get(0).getStatus().equals("1")){
                        return new R<>(false, "车辆为在途状态，不能添加司机！");
                    }
                    truDriver.setStatus(status.get(0).getStatus());
                }
            }
        }
        truDriver.setCreateTime(new Date());
        truDriver.setCreateBy(UserUtils.getUser());
        List<TruDriver> truDrivers = truDriverService.selectDriverPhone(truDriver);
        if (truDrivers.size() > 0) {
            return new R<>(false, "手机号不能与其他司机的手机号一致！");
        } else {
            if (truDriver.getLicensePhotoArr().length > 0) {
                truDriver.setLicensePhoto(StringUtils.join(truDriver.getLicensePhotoArr()));
            }
            if (truDriver.getIdcardPhotoUpArr().length > 0) {
                truDriver.setIdcardPhotoUp(StringUtils.join(truDriver.getIdcardPhotoUpArr()));
            }
            if (truDriver.getIdcardPhotoDownArr().length > 0) {
                truDriver.setIdcardPhotoDown(StringUtils.join(truDriver.getIdcardPhotoDownArr()));
            }
            if (truDriver.getQualificationArr().length > 0) {
                truDriver.setQualification(StringUtils.join(truDriver.getQualificationArr()));
            }
            truDriver.setDelFlag("0");
            truDriver.setXopenId("456789");
            try {
                int result = truDriverService.addDriver(truDriver);
                if (result != 1) {
                    return new R<>(false, "司机添加失败！");
                }
                System.out.println("66666" + truDriver.getDriverId());
                if (truDriver.getFreightRoute().length > 0) {
                    for (FreightRoute freightRoute : truDriver.getFreightRoute()) {
                        freightRoute.setDriverId(truDriver.getDriverId());
                        freightRoute.setOrigin(StringUtils.join(freightRoute.getOriginArray(), "/"));
                        freightRoute.setDestination(StringUtils.join(freightRoute.getDestinationArray(), "/"));
                        if (!freightRouteService.insert(freightRoute)) {
                            return new R<>(false, "司机常跑路线添加失败！");
                        }
                    }
                }
                return new R<>(true);
            } catch (Exception e) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                logger.info("添加问题：" + e.getMessage());
                return new R<>(false, "信息添加失败！");
            }
        }
    }
    /**
     * 删除
     * @param id ID
     * @return success/false
     */
    @ApiOperation(value = "删除司机", notes = "根据ID删除司机")
    @ApiImplicitParam(name = "id", value = "司机ID", required = true, dataType = "int", paramType = "path")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable Integer id) {

        Integer countNoFinishLoan = loanService.countNoFinishLoan(id.toString(), getTenantId());
        if (countNoFinishLoan > 0){
            return new R<>(false, "司机下有未还清或者未审核的借款，无法删除！");
        }
        Integer countOilCard = oilCardFeign.countOilCard(new Integer[]{id}, getTenantId());
        if (countOilCard > 0) {
            return new R<>(false, "司机下有油卡未进行退卡操作，无法删除！");
        }

        TruDriver truDriver = new TruDriver();
        truDriver.setDriverId(id);
        truDriver.setUpdateTime(new Date());
        truDriver.setDelFlag(CommonConstant.STATUS_DEL);
        return new R<>(truDriverService.updateById(truDriver));
    }

    /**
     * 批量删除
     * @param ids
     * @return success/false
     */
    @ApiOperation(value = "删除或者批量删除司机信息")
    @ApiImplicitParam(name = "ids",value = "ID数组",paramType = "path",required = true,example = "1,2")
    @DeleteMapping("/delectIds/{ids}")
    public R<Boolean> deleteIds(@PathVariable String ids) {

        Integer countNoFinishLoan = loanService.countNoFinishLoan(ids, getTenantId());
        if (countNoFinishLoan > 0){
            return new R<>(false, "司机下有未还清或者未审核的借款，无法删除！");
        }

        String[] a = Convert.toStrArray(ids);
        if (null != a && a.length > 0) {
            Integer[] in = new Integer[a.length];
            for (int i = 0; i < a.length; i++) {
                in[i] = Integer.valueOf(a[i]);
            }
            Integer countOilCard = oilCardFeign.countOilCard(in, getTenantId());
            if (countOilCard > 0) {
                return new R<>(false, "司机下有油卡未进行退卡操作，无法删除！");
            }

            return new R<>(truDriverService.deleteDriverByIds(ids));
        }
        return new R<>(false, "请选中信息再进行删除操作！");
    }

    /**
     * 编辑
     * @param  truDriver  实体
     * @return success/false
     */
    @ApiOperation(value = "编辑修改司机信息",notes = "id是必要的")
    @ApiImplicitParam(name = "truDriver",value = "司机信息",dataType = "TruDriver",paramType = "body",required = true)
    @Transactional
    @PutMapping
    public R<Boolean> edit(@RequestBody TruDriver truDriver) {

        // 修改后的车辆id是否一致，车辆下不超过2个司机
        if (null != truDriver.getPlateId()) {
            TruDriver truDriverTest =  truDriverService.selectById(truDriver.getDriverId());
            if(!truDriverTest.getStatus().equals("1") && truDriver.getStatus().equals("1")){
                return new R<>(false,"司机状态不能直接改为在途！");
            }
            if(truDriverTest.getStatus().equals("1") && !truDriver.getStatus().equals("1")){
                return new R<>(false,"司机状态为在途！不能修改为其他状态！");
            }
            if((truDriver.getPlateId() != truDriverTest.getPlateId()) && (truDriver.getStatus().equals("1"))){
                return new R<>(false,"司机状态为在途！不能修改车辆信息！");
            }

            /*查询目前车辆下的司机信息*/
            List<TruDriver> status = truDriverService.selectDriverStatusByTruckId(truDriver.getPlateId());
            /*如果更改司机的车量信息*/
            if (!truDriver.getPlateId().equals(truDriverTest.getPlateId())) {
                /*新车辆下的司机个数*/
                Integer count = truTruckService.countDrivers(truDriver.getPlateId());
                if (count >= 2) {
                    return new R<>(false,"车辆下对应的司机人数不能超过2个！");
                }else if (count == 1){
                    if(status.get(0).getStatus().equals("1")){
                        return new R<>(false,"车辆为在途状态，不能添加司机！");
                    }
                    truDriver.setStatus(status.get(0).getStatus());
                }
            }else {
                /*司机车辆没有改变*/
                if(status!=null){
                    for(TruDriver t :status) {
                        if(!t.getDriverId().equals(truDriver.getDriverId())){
                            t.setStatus(truDriver.getStatus());
                            truDriverService.updateById(t);
                            break;
                        }
                    }
                }
            }
        }
        truDriver.setUpdateTime(new Date());
        truDriver.setUpdateBy(UserUtils.getUser());
        List<TruDriver> truDrivers = truDriverService.selectDriverPhone(truDriver);
        if (null != truDrivers && truDrivers.size() > 0){
            return new R<>(false, "手机号不能与其他司机的手机号一致！");
        }else {
            try {
                truDriver.setLicensePhoto(StringUtils.join(truDriver.getLicensePhotoArr()));
                truDriver.setIdcardPhotoUp(StringUtils.join(truDriver.getIdcardPhotoUpArr()));
                truDriver.setIdcardPhotoDown(StringUtils.join(truDriver.getIdcardPhotoDownArr()));
                truDriver.setQualification(StringUtils.join(truDriver.getQualificationArr()));
                truDriver.setUpdateTime(new Date());
                truDriverService.updateById(truDriver);
                FreightRoute[] freightRoutes = truDriver.getFreightRoute();
                if (null != freightRoutes && freightRoutes.length > 0) {
                    for (FreightRoute freightRoute : freightRoutes) {
                        if (freightRoute.getId() == null) {
                            freightRoute.setDriverId(truDriver.getDriverId());
                            freightRoute.setOrigin(StringUtils.join(freightRoute.getOriginArray(), "/"));
                            freightRoute.setDestination(StringUtils.join(freightRoute.getDestinationArray(), "/"));
                            freightRouteService.insert(freightRoute);
                        } else {
                            freightRoute.setOrigin(StringUtils.join(freightRoute.getOriginArray(), "/"));
                            freightRoute.setDestination(StringUtils.join(freightRoute.getDestinationArray(), "/"));
                            freightRouteService.updateById(freightRoute);
                        }
                    }
                }
                return new R<>(true);
            } catch (Exception e) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                logger.info("司机更新问题：" + e.getMessage());
                return new R<>(false, "信息更新失败！");
            }
        }
    }

    /**
     * 修改司机信息
     * @param
     * @return success/false
     */
    @ApiOperation(value = "编辑修改司机信息",notes = "id是必要的")
    @ApiImplicitParam(name = "truDriver",value = "司机信息",dataType = "TruDriver",paramType = "body",required = true)
    @Transactional
    @GetMapping("updateDriver")
    public R<Boolean> updateDriver(HttpServletRequest request) {

        JSONObject objperson = JSONObject.fromObject(request.getParameter("truDriver"));
        TruDriver truDriver=(TruDriver)JSONObject.toBean(objperson,TruDriver.class);
        // 修改后的车辆id是否一致，不一致车辆下不超过2个司机
        if (null != truDriver.getPlateId()) {
            TruDriver truDriverTest =  truDriverService.selectById(truDriver.getDriverId());

            /*查询目前车辆下的司机信息*/
            List<TruDriver> status = truDriverService.selectDriverStatusByTruckId(truDriver.getPlateId());
            /*如果更改司机的车量信息*/
            if (!truDriver.getPlateId().equals(truDriverTest.getPlateId())) {
                /*新车辆下的司机个数*/
                Integer count = truTruckService.countDrivers(truDriver.getPlateId());
                if (count >= 2) {
                    return new R<>(false,"车辆下对应的司机人数不能超过2个！");
                }else if (count == 1){
                    truDriver.setStatus(status.get(0).getStatus());
                }
            }else {
                if(status!=null){
                    for(TruDriver t :status) {
                        if(!t.getDriverId().equals(truDriver.getDriverId())){
                            t.setStatus(truDriver.getStatus());
                            truDriverService.updateById(t);
                            break;
                        }
                    }
                }
            }
        }
        try {
            truDriver.setUpdateBy(UserUtils.getUser());
            if (null != truDriver.getPhone() || "".equals(truDriver.getPhone())) {
                List<TruDriver> truDrivers = truDriverService.selectDriverPhone(truDriver);
                System.out.println(truDrivers.size());
                if (null != truDrivers && truDrivers.size() > 0) {
                    return new R<>(false, "手机号不能与其他司机的手机号一致！");
                } else {
                    truDriver.setLicensePhoto(StringUtils.join(truDriver.getLicensePhotoArr()));
                    truDriver.setIdcardPhotoUp(StringUtils.join(truDriver.getIdcardPhotoUpArr()));
                    truDriver.setIdcardPhotoDown(StringUtils.join(truDriver.getIdcardPhotoDownArr()));
                    truDriver.setQualification(StringUtils.join(truDriver.getQualificationArr()));
                    truDriver.setUpdateTime(new Date());
                    truDriverService.updateById(truDriver);
                    FreightRoute[] freightRoutes = truDriver.getFreightRoute();
                    if (null != freightRoutes && freightRoutes.length > 0) {
                        for (FreightRoute freightRoute : freightRoutes) {
                            if (freightRoute.getId() == null) {
                                freightRoute.setDriverId(truDriver.getDriverId());
                                freightRoute.setOrigin(StringUtils.join(freightRoute.getOriginArray(), "/"));
                                freightRoute.setDestination(StringUtils.join(freightRoute.getDestinationArray(), "/"));
                                freightRouteService.insert(freightRoute);
                            } else {
                                freightRoute.setOrigin(StringUtils.join(freightRoute.getOriginArray(), "/"));
                                freightRoute.setDestination(StringUtils.join(freightRoute.getDestinationArray(), "/"));
                                freightRouteService.updateById(freightRoute);
                            }
                        }
                    }
                    return new R<>(true);
                }
            }
            truDriver.setUpdateTime(new Date());
            truDriverService.updateById(truDriver);
            return new R<>(true);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            logger.info("司机更新问题：" + e.getMessage());
            return new R<>(false, "信息更新失败！");
        }
    }

    /**
     * 查询所有车牌号
     */
    @GetMapping("/selectPlateNumberList")
    public List<TruTruck> selectPlateNumberList(TruTruck truTruck) {
       truTruck.setTenantId(getTenantId());
        return truTruckService.selectPlateNumberList(truTruck);
    }

    /**
     * 导出司机信息
     * @param ids
     * @return
     */
    @ApiOperation(value = "搜索导出",notes = "参数为空时，导出全部；")
    @ApiImplicitParam(name = "ids",value = "id数组", paramType = "query",dataType = "String",example = "1,2")
    @GetMapping("/export/{ids}")
    public AjaxResult export(HttpServletResponse response, @PathVariable String ids) {
        List<TruDriver> list = null;
        TruDriver truDriver = new TruDriver();
        if("all".equals(ids)){
            list = truDriverService.selectAllDriver(truDriver);
        }else {
            list = truDriverService.selectByIds(ids);
        }
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getDriverName() == null || list.get(i).getDriverName().equals("")) {
                list.get(i).setDriverName("暂无");
            }
            if (list.get(i).getIdcardNumber() == null || list.get(i).getIdcardNumber().equals("")) {
                list.get(i).setIdcardNumber("暂无");
            }
            if (list.get(i).getPhone() == null || list.get(i).getPhone().equals("")) {
                list.get(i).setPhone("暂无");
            }
            if (list.get(i).getUrgentPhone() == null || list.get(i).getUrgentPhone().equals("")) {
                list.get(i).setUrgentPhone("暂无");
            }
            if (list.get(i).getLicenseLevel() == null || list.get(i).getLicenseLevel().equals("")) {
                list.get(i).setLicenseLevel("暂无");
            }
            if (list.get(i).getBankNumber() == null || list.get(i).getBankNumber().equals("")) {
                list.get(i).setBankNumber("暂无");
            }
            if (list.get(i).getCreateBy() == null || list.get(i).getCreateBy().equals("")) {
                list.get(i).setCreateBy("暂无");
            }
            switch (list.get(i).getLicenseLevel()){
                case "0" : list.get(i).setLicenseLevel("A1");
                    break;
                case "1" : list.get(i).setLicenseLevel("B2");
                    break;
                case "2" : list.get(i).setLicenseLevel("C1");
                    break;
            }
        }
        ExcelUtil<TruDriver> util = new ExcelUtil<TruDriver>(TruDriver.class);
        return util.exportExcel(response,list, "车辆信息",null);
    }

    /**
     * 根据车牌号和司机姓名查询是不是匹配
     */
    @ApiOperation(value = "根据车牌号和司机姓名查询是不是匹配",notes = "参数为车牌号和司机姓名;")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "driverName",value = "司机姓名", dataType = "String",example = "1,2"),
            @ApiImplicitParam(name = "plateNumber",value = "车牌号", dataType = "String",example = "1,2")
    })
    @GetMapping("/checkDriverTruck")
    public int checkDriverTruck(String driverName, String plateNumber, String tenantId){
        return truDriverService.checkDriverTruck(driverName,plateNumber,tenantId);
    }

    /**
     * 供普货查询主辅司机调用
     * @param zfId
     * @return
     */
    @GetMapping("/getZFDriver/{zfId}")
    public List<DriverVO> getdriverList(@PathVariable("zfId") String zfId){
        return truDriverService.getZFDriverInfo(zfId);
    }

    /**
     * 根据openid(gopenId)查询司机信息
     */
    @PostMapping("selectDriverBygopenId")
    public TruDriver selectDriverBygopenId(String gopenId){
        return truDriverService.selectDriverBygopenId(gopenId);
    }

    /**
     * 更新司机状态
     */
    @PostMapping("/updateDriverStatus")
    @ApiOperation(value = "供订单模块更新司机信息")
    public Boolean updateStatus(@RequestBody DriverVO driverVO){
        return truDriverService.updateDriverStatus(driverVO);
    }

    @GetMapping("getDriverPayType")
    public String getDriverPayType(Integer driverId, Integer tenantId){
        return truDriverService.getDriverPayType(driverId, tenantId);
    }

    @GetMapping("getDriverTeamType")
    public List<DriverVO> getDriverTeamType(String teamType, Integer tenantId){
        return truDriverService.getDriverTeamType(teamType,tenantId);
    }

    @GetMapping("selectDriverByPlateNumber")
    public List<DriverVO> selectDriverByPlateNumber(Integer plateId,Integer tenantId){
        return truDriverService.selectDriverByPlateNumber(plateId,tenantId);
    }

    @GetMapping("selectDriverByDriverId")
    public DriverVO selectDriverByDriverId(Integer driverId,Integer tenantId){
        return truDriverService.selectDriverByDriverId(driverId,tenantId);
    }

    @GetMapping("selectDriverStatus")
    public DriverVO selectDriverStatus(Integer driverId,Integer tenantId){
        return truDriverService.selectDriverStatus(driverId,tenantId);
    }

    @GetMapping("/selectDriverStatusByPlateNumber/{plateNumber}")
    public List<TruDriver> selectDriverStatusByPlateNumber(@PathVariable("plateNumber") String plateNumber){
        return truDriverService.selectDriverStatusByPlateNumber(plateNumber);
    }

    @ApiOperation(value = "小程序修改密码")
    @ApiImplicitParams({@ApiImplicitParam(value = "phone" ,name = "手机号"),
            @ApiImplicitParam(value = "newPassword" ,name = "新密码"),
            @ApiImplicitParam(value = "smsCode" ,name = "验证码"),
            @ApiImplicitParam(value = "callback" ,name = "")})
    @GetMapping("updateDriverPass")
    public String updateDriverPass(@RequestParam("phone")String phone,
                                   @RequestParam("newPassword")String newPassword,
                                   @RequestParam("smsCode") String smsCode,
                                   HttpServletRequest request,
                                   @RequestParam("callback")String callback){
//        HttpSession session = request.getSession();
        System.out.println("==tempCode==");
        Object tempCode = redisTemplate.opsForValue().get(SecurityConstants.DEFAULT_CODE_KEY + phone);
        System.out.println(phone+"==tempCode=="+tempCode);
        Gson g=new Gson();
        //手机验证码
        if(null == tempCode){
            return callback+"("+g.toJson("error")+")";
        }else{
            if(!smsCode.equals(tempCode)){
                return callback+"("+g.toJson("error")+")";
            }
        }
        //校验手机号是否存在
       Boolean f = truDriverService.findPhone(phone);
        if(f){
            return callback+g.toJson(1);//返回结果为1，手机号不存在
        }
        TruDriver driver = new TruDriver();
        TruDriver param = new TruDriver();
        param.setPhone(phone);
        driver.setXopenId(newPassword);
        driver.setUpdateTime(new Date());
        boolean flag = truDriverService.update(driver,new EntityWrapper<>(param));
        if(flag){
            return callback+g.toJson(0);
        }
        return callback+"("+g.toJson("error")+")";
    }

}
