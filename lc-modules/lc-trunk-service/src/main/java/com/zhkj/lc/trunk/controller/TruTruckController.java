package com.zhkj.lc.trunk.controller;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.zhkj.lc.common.constant.CommonConstant;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.util.R;
import com.zhkj.lc.common.util.UserUtils;
import com.zhkj.lc.common.util.http.HttpClientUtil;
import com.zhkj.lc.common.util.support.Convert;
import com.zhkj.lc.common.vo.TruckVO;
import com.zhkj.lc.common.web.BaseController;
import com.zhkj.lc.trunk.dto.DriverMessage;
import com.zhkj.lc.trunk.dto.PolylinePath;
import com.zhkj.lc.trunk.model.TruTruck;
import com.zhkj.lc.trunk.model.TruTruckfile;
import com.zhkj.lc.trunk.service.ITruTruckService;
import com.zhkj.lc.trunk.service.ITruTruckfileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 车辆信息表 前端控制器
 * </p>
 *
 * @author wzc
 * @since 2018-12-07
 */
@Api(description = "车辆信息管理接口")
@RestController
@RequestMapping("/truTruck")
public class TruTruckController extends BaseController {
    @Autowired private ITruTruckService truTruckService;
    @Autowired private ITruTruckfileService truTruckfileService;

    /**
     * 车牌号查询
     * @param  plateNumber
     * @return
     */
    @GetMapping("/selectTruckByplateNumber")
    public TruTruck selectTruckByplateNumber(String plateNumber){
        TruTruck truck = new TruTruck();
        truck.setTenantId(getTenantId());
        truck.setPlateNumber(plateNumber);
        truck.setIsTrust("0");
        TruTruck truTruck = truTruckService.selectTruckByplateNumber(truck);
        return truTruck;
    }
    /**
     * 多条件查询
     * @param truTruck
     * @return
     */
    @PostMapping("/selectTruckList")
    public List<TruTruck> selectTruckList(@RequestBody TruTruck truTruck){
        truTruck.setTenantId(getTenantId());
        truTruck.setIsTrust("0");
        List<TruTruck> truTruckList = truTruckService.selectTruckList(truTruck);
        return truTruckList;
    }
    /**
     * 通过ID查询
     *
     * @param id ID
     * @return TruTruck
     */
    @ApiOperation(value = "通过ID查询车辆信息", notes = "通过ID查询车辆信息")
    @ApiImplicitParam(name = "id", value = "车辆id", required = true, dataType = "Integer", paramType = "path")
    @GetMapping("/{id}")
    public TruTruck get(@PathVariable Integer id)
    {
        return truTruckService.selectTruckById(id);
    }

    /**
     * 跨服务
     *
     * @param id ID
     * @return TruTruck
     */
    @ApiIgnore
    @GetMapping("/selectTruckById")
    public TruTruck selectTruckById(@RequestParam("id") Integer id)
    {
        return truTruckService.selectTruckById(id);
    }

    /**
     * 分页搜索查询信息,车辆司机车队表联合查询
     * @param params 分页对象
     * @return 分页对象
     */
    @ApiOperation(value = "搜索分页显示车辆司机车队表联合查询信息",notes = "params参数不写默认获取第一页10条数据；contract参数不写即为获取全部信息:")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "params",value = "分页参数：page页,size页数据量",paramType = "query",dataType = "Map"),
            @ApiImplicitParam(name = "truTruck",value = "搜索条件,字段可选:plateNumber(车牌号),truckOwner(车主),beginTime,endTime(时间的范围)等",paramType = "query",dataType = "TruTruck")
    })
    @GetMapping("/selectTruckDriverTeamList")
    public Page selectTruckDriverTeamList(@RequestParam Map<String, Object> params,TruTruck truTruck) {
        params.put(CommonConstant.DEL_FLAG, CommonConstant.STATUS_NORMAL);
        truTruck.setTenantId(getTenantId());
        truTruck.setIsTrust("0");
        return truTruckService.selectTruckDriverTeamList(new Query<>(params), truTruck);
    }

    /**
     * 添加
     * @param  truTruck  实体
     * @return success/false
     */
    @ApiOperation(value = "添加")
    @ApiImplicitParam(name = "truTruck",value = "车辆信息类",paramType = "body",dataType = "TruTruck",required = true)
    @PostMapping
    @Transactional
    public R<Boolean> add(@RequestBody TruTruck truTruck) {
        truTruck.setTenantId(getTenantId());
        truTruck.setCreateTime(new Date());
        truTruck.setCreateBy(UserUtils.getUser());
        truTruck.setDelFlag(CommonConstant.STATUS_NORMAL);
        TruTruckfile truckfile = truTruck.getTruTruckfile();

        Integer flag = truTruckService.checkPlateNumber(truTruck.getPlateNumber(), getTenantId());
        if(null!= flag && flag == 1){
            return new R<>(Boolean.FALSE, "该车牌号已存在！");
        }
        if (null != truckfile){

            truckfile = executeFile(truckfile);
            truTruckService.addTruck(truTruck);
            truckfile.setTruckFileId(truTruck.getTruckId());
            truckfile.setCreateTime(new Date());
            truckfile.setCreateBy(UserUtils.getUser());
            truTruckfileService.insertTruckFile(truckfile);
            return new R<>(Boolean.TRUE);
        }
        return new R<>(truTruckService.insert(truTruck));
    }

    /**
     * 批量删除
     * @param ids
     * @return success/false
     */
    @ApiOperation(value = "删除或者批量删除车辆信息")
    @ApiImplicitParam(name = "ids",value = "ID数组",paramType = "path",required = true,example = "1,2")
    @DeleteMapping("/delectIds/{ids}")
    public R<Boolean> deleteIds(@PathVariable String ids) {
        String[] idArray = Convert.toStrArray(ids);
        List<Integer> listTruckId = truTruckService.getTruckIdListByProc(getTenantId());
        if (null != idArray && idArray.length > 0) {
            for (String id: idArray) {
                if (truTruckService.countDrivers(Integer.parseInt(id)) > 0) {
                    return new R<>(false, "批量删除的车辆中部分车辆仍有司机，请将车辆下的司机删除之后再进行删除！");
                }
                if (null != listTruckId && listTruckId.size() > 0){
                    if (listTruckId.contains(id)){
                        return new R<>(false, "批量删除的车辆中部分车辆仍在途使用中，不能进行删除！");
                    }
                }
            }
        }
        return new R<>(truTruckService.deleteTruckByIds(ids,UserUtils.getUser()));
    }

    /**
     * 删除
     * @param id ID
     * @return success/false
     */
    //@ApiOperation(value = "删除单个车辆信息")
    //@ApiImplicitParam(name = "id",value = "id",paramType = "path",required = true)
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable Integer id) {
        if (truTruckService.countDrivers(id) > 0){
            return new R<>(false, "该车辆仍有司机，请将该车辆下的司机删除之后再进行删除！");
        }
        List<Integer> listTruckId = truTruckService.getTruckIdListByProc(getTenantId());
        if (null != listTruckId && listTruckId.size() > 0){
            if (listTruckId.contains(id)){
                return new R<>(false, "批量删除的车辆中部分车辆仍在途使用中，不能进行删除！");
            }
        }
        TruTruck truTruck = new TruTruck();
        truTruck.setTruckId(id);
        truTruck.setUpdateTime(new Date());
        truTruck.setDelFlag(CommonConstant.STATUS_DEL);
        return new R<>(truTruckService.updateById(truTruck));
    }

    /**
     * 编辑
     * @param  truTruck  实体
     * @return success/false
     */
    @ApiOperation(value = "编辑修改车辆信息",notes = "id是必要的")
    @ApiImplicitParam(name = "truTruck",value = "车辆信息",dataType = "TruTruck",paramType = "body",required = true)
    @PutMapping
    public R<Boolean> edit(@RequestBody TruTruck truTruck) {
        truTruck.setUpdateTime(new Date());
        truTruck.setUpdateBy(UserUtils.getUser());
        TruTruckfile truckfile = truTruck.getTruTruckfile();

        /*Integer flag = truTruckService.checkPlateNumberById(truTruck.getPlateNumber(), getTenantId());
        if(null!= flag && flag != truTruck.getTruckId()){
            return new R<>(Boolean.FALSE, "该车牌号已存在！");
        }*/

        if (null != truckfile){
            truckfile = executeFile(truckfile);
            if (null != truckfile.getTruckFileId()){
                TruTruckfile param = new TruTruckfile();
                param.setTruckFileId(truckfile.getTruckFileId());
                truTruckfileService.update(truckfile, new EntityWrapper<>(param));
                return new R<>(truTruckService.updateById(truTruck));
            }
            truckfile.setTruckFileId(truTruck.getTruckId());
            truckfile.setUpdateTime(new Date());
            truckfile.setUpdateBy(UserUtils.getUser());
            truTruckfileService.insert(truckfile);
        }
        return new R<>(truTruckService.updateById(truTruck));
    }

    /**
     * 查询所有车牌号
     */
    @ApiOperation(value = "查询所有车牌号")
    @GetMapping("/selectPlateNumberList")
    public List<TruTruck> selectPlateNumberList() {
        TruTruck truTruck = new TruTruck();
        truTruck.setTenantId(getTenantId());
        truTruck.setIsTrust("0");
        return truTruckService.selectPlateNumberList(truTruck);
    }

    /**
     * 跨服务
     * 查询所有车牌号
     */
    @ApiOperation(value = "查询所有车牌号")
    @PostMapping("/selectPlateNumberLists")
    public List<TruTruck> selectPlateNumberLists(@RequestBody TruTruck truTruck) {
        return truTruckService.selectPlateNumberList(truTruck);
    }

    /**
     * 根据车主id(自有,挂靠,外调)查询所有车牌号
     */
    @ApiOperation(value = "根据车主id(自有,挂靠,外调)查询所有车牌号")
    @ApiImplicitParam(name = "truckTeamId",value = "车队id",paramType = "path",required = true,example = "1")
    @PostMapping("/selectPlateNumberByTruckTeamId")
    public List<TruTruck> selectPlateNumberByTruckTeamId(@RequestBody TruTruck truTruck) {
        return truTruckService.selectPlateNumberByTruckTeamId(truTruck);
    }

    /**
     * 根据车辆类型(自有,挂靠,外调)查询所有车牌号
     */
    @ApiOperation(value = "根据车辆类型(自有,挂靠,外调)查询所有车牌号")
    @ApiImplicitParam(name = "attribute",value = "车辆类型",paramType = "path",required = true,example = "0")
    @PostMapping("/selectPlateNumberByAttribute")
    public List<TruTruck> selectPlateNumberByAttribute(@RequestBody  TruTruck truTruck) {
        return truTruckService.selectPlateNumberByAttribute(truTruck);
    }

    @ApiOperation(value = "根据车辆类型(自有,挂靠,外调)查询所有车牌号")
    @GetMapping("/selectTruckIdBy")
    public Integer selectTruckIdBy(@RequestParam("driverId") Integer driverId, @RequestParam("plateNumber") String plateNumber) {
        return truTruckService.selectTruckIdBy(driverId, plateNumber);
    }

    public TruTruckfile executeFile(TruTruckfile truckfile){
            truckfile.setCertificateCopy(StringUtils.join(truckfile.getCertificateCopyArr()));
            truckfile.setDrivingLicenseCopy(StringUtils.join(truckfile.getDrivingLicenseCopyArr()));
            truckfile.setOperationCertificateCopy(StringUtils.join(truckfile.getOperationCertificateCopyArr()));
            truckfile.setEnregisterOriginal(StringUtils.join(truckfile.getEnregisterOriginalArr()));
            truckfile.setVehicleRoadOriginal(StringUtils.join(truckfile.getVehicleRoadOriginalArr()));
        return  truckfile;
    }


    /**
     * 获取最新记录的车辆GPS信息
     * http://192.168.16.153:8080/RoadGPS/road/getNewLngLat.do?plateNum=%E5%86%80DW9513
     *
     */
    @GetMapping("/getTruckGPS")
    public Map<String,Object> getTruckGPS(@RequestParam("plateNumber") String plateNumber) throws Exception {
        return truTruckService.getTruckGPS(plateNumber);
    }

    @ApiOperation(value = "地图显示所有车辆信息", notes = "[i][0]车牌号、[i][1]司机名、[i][2]手机号、[i][3]状态、[i][4]地址、[i][5]经度、[i][6]纬度")
    @GetMapping("/allTruckGPS")
    public String[][] allTruckGPS(){
        return truTruckService.allTruckGPS(getTenantId());
    }

    @ApiOperation(value = "根据车牌号查询该车辆下非空闲司机个数")
    @GetMapping("checkTruckDriverStatus")
    public Integer checkTruckDriverStatus (String plateNumber, Integer tenantId){
        return truTruckService.checkTruckDriverStatus(plateNumber, tenantId);
    }

    @ApiOperation(value = "查询所有车牌号")
    @GetMapping("/selectPlateNumbers")
    public List<TruTruck> selectPlateNumbers() {
        TruTruck truTruck = new TruTruck();
        truTruck.setTenantId(getTenantId());
        truTruck.setDelFlag("0");
        return truTruckService.selectList(new EntityWrapper<>(truTruck));
    }

    /*远程服务调用*/
    @GetMapping("selectTruckByPlateNumber/{plateNumber}/{tenantId}")
    public TruTruck selectTruckByPlateNumber(@PathVariable("plateNumber")String plateNumber, @PathVariable("tenantId") Integer tenantId){
        return truTruckService.selectTruckByPlateNumber(plateNumber, tenantId);
    }

    /**
     * 更改车辆状态 远程服务调用
     */
    /*远程服务调用*/
    @PostMapping("updateTruckStatus")
    public Boolean updateTruckStatus(@RequestBody TruckVO truckVO){
        return truTruckService.updateTruckStatus(truckVO);
    }

}
