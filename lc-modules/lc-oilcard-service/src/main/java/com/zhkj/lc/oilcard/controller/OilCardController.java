package com.zhkj.lc.oilcard.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.luhuiguo.fastdfs.domain.StorePath;
import com.luhuiguo.fastdfs.service.FastFileStorageClient;
import com.xiaoleilu.hutool.io.FileUtil;
import com.zhkj.lc.common.api.FileUtils;
import com.zhkj.lc.common.bean.config.FdfsPropertiesConfig;
import com.zhkj.lc.common.util.*;
import com.zhkj.lc.common.util.excel.ExcelUtil;
import com.zhkj.lc.common.vo.AnnouncementVO;
import com.zhkj.lc.common.vo.DriverVO;
import com.zhkj.lc.common.vo.TruckVO;
import com.zhkj.lc.common.web.BaseController;
import com.zhkj.lc.oilcard.feign.TruckFeign;
import com.zhkj.lc.oilcard.model.OilCard;
import com.zhkj.lc.oilcard.model.OilMajor;
import com.zhkj.lc.oilcard.service.IOilCardService;
import com.zhkj.lc.oilcard.service.IOilMajorService;
import com.zhkj.lc.oilcard.service.impl.DictService;
import com.zhkj.lc.oilcard.util.BankcardNo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 油卡管理 前端控制器
 * </p>
 *
 * @author wzc
 * @since 2018-12-07
 */
@Api(description = "油卡基础、车辆、司机接口")
@RestController
@RequestMapping("/oilCard")
public class OilCardController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(OilCard.class);
    @Autowired
    private FastFileStorageClient fastFileStorageClient;
    @Autowired
    private FdfsPropertiesConfig fdfsPropertiesConfig;
    @Autowired private IOilCardService oilCardService;
    @Autowired private IOilMajorService oilMajorService;
    @Autowired private TruckFeign truckFeign;
    @Autowired private DictService dictService;

    /**
    * 通过ID查询
    *
    * @param id ID
    * @return OilCard
    */
    @ApiOperation(value = "查询油卡信息", notes = "根据id查询油卡信息")
    @ApiImplicitParam(name = "id", value = "油卡基础信息id", required = true, dataType = "Integer", paramType = "path")
    @GetMapping("/{id}")
    public R<OilCard> get(@PathVariable Integer id) {
        return new R<>(oilCardService.selectByOilCardId(id));
    }

    /**
     * 分页查询信息
     *
     * @param oilCard 分页对象
     * @param params 分页参数
     * @return 分页对象
     */
    @ApiOperation(value = "搜索分页显示油卡基础信息",notes = "params参数不写默认获取第一页10条数据；oilCard参数不写即为获取全部信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "params",value = "分页参数,可选字段:page,limit",paramType = "query",dataType = "Map"),
            @ApiImplicitParam(name = "oilCard",value = "搜索条件,可选字段:cardType,openCardPlace,beginTime,endTime(申请时间的范围),attribute,truckId(根据车牌号plateNumber获取的),ownerDriverId等",paramType = "query",dataType = "OilCard")
    })
    @GetMapping("/page")
    public Page<OilCard> page(@RequestParam Map<String, Object> params,OilCard oilCard) {
        oilCard.setTenantId(getTenantId());
        return oilCardService.selectCardListPage(new Query(params), oilCard);
    }

    /**
     * 添加
     * @param  oilCard  实体
     * @return success/false
     */
    @ApiOperation(value = "添加",notes = "oilCardNumber是必要的")
    @ApiImplicitParam(name = "oilCard",value = "油卡基础信息类",paramType = "body",dataType = "OilCard",required = true)
    @Transactional
    @PostMapping
    public R<Boolean> add(@RequestBody OilCard oilCard) {
        OilMajor o = null;
        if(!StringUtils.isEmpty(oilCard.getMajorNumber())){
            o = oilMajorService.selectOne(new EntityWrapper<OilMajor>().eq("major_number",oilCard.getMajorNumber()));
        }
        if (o !=null){
            oilCard.setMajorId(o.getMajorId());
        }
        oilCard.setCreateBy(UserUtils.getUser());
        oilCard.setTenantId(getTenantId());
        oilCard.setCardStatus("未使用");
        return new R<>(oilCardService.insertOilCard(oilCard));
    }

    /**
     * 删除
     * @param ids ID
     * @return success/false
     */
    @ApiOperation(value = "删除或者批量删除油卡信息")
    @ApiImplicitParam(name = "ids",value = "油卡id",paramType = "path",required = true)
    @DeleteMapping("/{ids}")
    public R<Boolean> delete(@PathVariable String ids) {
        return new R<>(oilCardService.deleteCardByIds(ids,UserUtils.getUser()));
    }

    /**
     * 编辑
     * @param  oilCard  实体
     * @return success/false
     */
    @ApiOperation(value = "编辑修改油卡信息",notes = "oilCardId是必要的")
    @ApiImplicitParam(name = "oilCard",value = "油卡信息",dataType = "OilCard",paramType = "body",required = true)
    @PutMapping
    public R<Boolean> edit(@RequestBody OilCard oilCard) {
        if (null == oilCard.getUpdateBy()) {
            oilCard.setUpdateBy(UserUtils.getUser());
        }
        return new R<>(oilCardService.updateOilCard(oilCard));
    }

    @ApiOperation(value = "编辑油卡退卡",notes = "oilCardId, majorId，returnRecord，ownerDriverId，oilCardNumber")
    @ApiImplicitParam(name = "oilCard",value = "油卡信息",dataType = "OilCard",paramType = "body",required = true)
    @Transactional
    @PutMapping("/returnCard")
    public R<Boolean> editReturn(@RequestBody OilCard oilCard) {
        oilCard.setReturnDate(new Date());
        oilCard.setUpdateBy(UserUtils.getUser());
        oilCard.setCardStatus("退卡");
        boolean flag = oilCardService.updateOilCard(oilCard);
        if (flag) {
            OilMajor oilMajor = oilMajorService.selectById(oilCard.getMajorId());
            oilMajor.setCardNum(oilMajor.getCardNum() - 1);
            oilMajorService.updateOilMajor(oilMajor);
            // 审核情况以公告的方式发送给司机
            AnnouncementVO announcementVO = new AnnouncementVO();
            announcementVO.setTitle("油卡退卡");
            announcementVO.setType("0"); // 个人
            announcementVO.setDriverOwerId(oilCard.getOwnerDriverId());
            announcementVO.setCreateBy(UserUtils.getUser());
            announcementVO.setTenantId(getTenantId());
            announcementVO.setContent("油卡 " + (oilCard.getOilCardNumber() == null ? "" : oilCard.getOilCardNumber()) + " 退卡成功！");
            truckFeign.addFeign(announcementVO);
        }
        return new R<>(flag);
    }

    @ApiOperation(value = "搜索导出",notes = "参数为空时，导出全部;")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "油卡基础信息ids", paramType = "path", dataType = "String", required = true),
            @ApiImplicitParam(name = "oilCard", value = "油卡信息参数OilCard,字段可选:cardType,openCardPlace,beginTime,endTime(申请时间的范围),attribute,truckId(根据车牌号plateNumber获取的),ownerDriverId等", paramType = "query", dataType = "OilCard")
    })
    @PostMapping("/export/{ids}")
    public AjaxResult export(@PathVariable("ids") String ids, OilCard card, HttpServletRequest request, HttpServletResponse response)
    {
        List<OilCard> list;
        if ("all".equals(ids)) {
            card.setTenantId(getTenantId());
            list = oilCardService.selectCardList(card);
        }else {
            list = oilCardService.selectCardListByIds(ids);
        }
        String sheetName = DateUtils.getDate()+"-油卡基础信息";
        ExcelUtil<OilCard> util = new ExcelUtil<OilCard>(OilCard.class);
        return util.exportExcel( request, response, list,sheetName,null);
    }

    @ApiOperation(value = "判断银行卡号是否正确，并获取开户行",notes = "OilCard中bankNumber银行卡号是必须的")
    @ApiImplicitParam(name = "oilCard",value = "油卡信息",dataType = "OilCard",paramType = "body",required = true)
    @PostMapping("/testGet")
    public Map<String,String> testBankNumberGet(@RequestBody OilCard oilCard)
    {
        return BankcardNo.getCardDetail(oilCard.getBankNumber());
    }

    @ApiOperation(value = "测试油卡号是否重复")
    @ApiImplicitParam(name = "oilCardNumber",value = "油卡号",dataType = "String",paramType = "path",required = true)
    @GetMapping("/testOilCardNumber2/{oilCardNumber}")
    public R<Boolean> testOilCardNumber2(@PathVariable("oilCardNumber") String oilCardNumber)
    {
//        OilCard card1 = oilCardService.selectCardByOilCardNumber(oilCardNumber, getTenantId());
        OilCard card1 = oilCardService.selectOilCardByOilCardNumber(oilCardNumber, getTenantId());
        if (null == card1){
            return new R<>(false);
        }
        return new R<>(true);
    }

    @ApiOperation(value = "获取全部油卡号",notes = "结果集合中单个油卡基础类包含字段为{oilCardId,oilCardNumber}")
    @PostMapping("/allOilCardNumber")
    public List<OilCard> selectOilCardNumber()
    {
        List<OilCard> card1 = oilCardService.selectOilCardNumber(getTenantId());//返回油卡id
        return card1;
    }

    @ApiOperation(value = "获取全部办卡地点")
    @PostMapping("/allOpenCardPlace")
    public List<String> selectOpenCardPlace()
    {
        List<String> openCardPlaces = oilCardService.selectOpenCardPlace(getTenantId());
        return openCardPlaces;
    }

    @ApiOperation(value = "根据车辆id获取车辆信息")
    @ApiImplicitParam(name = "truckId",value = "车辆id",paramType = "path",dataType = "Integer",required = true)
    @GetMapping("/selectByTruckId/{id}")
    public TruckVO selectByTruckId(@PathVariable Integer id)
    {
        TruckVO truck = truckFeign.getTruckByid(id);
        truck.setAttribute(dictService.getLabel("truck_attribute",truck.getAttribute(), truck.getTenantId()));
        return truck;
    }

    @ApiOperation(value = "获取全部车辆信息",notes="车辆信息(truckId,plateNumber,truckOwner,truckOwnerPhone)")
    @PostMapping("/selectPlateNumberlistIsTrust")
    public List<TruckVO> listTruckIsTrust()
    {
        TruckVO truckVO = new TruckVO();
        truckVO.setIsTrust("0");
        truckVO.setTenantId(getTenantId());
        List<TruckVO> listTruck = truckFeign.listTruck(truckVO);
        return listTruck;
    }

    @ApiOperation(value = "获取全部车辆信息",notes="车辆信息(truckId,plateNumber,truckOwner,truckOwnerPhone)")
    @PostMapping("/selectPlateNumberlist")
    public List<TruckVO> listTruck()
    {
        TruckVO truckVO = new TruckVO();
        truckVO.setTenantId(getTenantId());
        List<TruckVO> listTruck = truckFeign.listTruck(truckVO);
        return listTruck;
    }

    @ApiOperation(value = "根据车辆id获取司机信息", notes = "/selectDriverByPlateId/1?ttId=1234（1是车辆id，1234车队id）")
    @GetMapping("/selectDriverByPlateId/{plateId}")
    public List<DriverVO> selectDriverByplateId(@PathVariable("plateId") Integer plateId, @RequestParam("ttId") Integer ttId){
        DriverVO driverVO = new DriverVO();
        driverVO.setTenantId(getTenantId());
        driverVO.setPlateId(plateId);
        driverVO.setTruckTeamId(ttId);
        return truckFeign.selectDriverByplateId(driverVO);
    }

    @ApiOperation(value = "根据司机id获取司机信息")
    @ApiImplicitParam(name = "id",value = "司机id",paramType = "path",dataType = "Integer",required = true)
    @GetMapping("/selectByDriverId/{id}")
    public DriverVO getDriverByid(@PathVariable("id") Integer id)
    {
        return truckFeign.getDriverByid(id);
    }

    @ApiOperation(value = "获取全部司机信息")
    @ApiImplicitParam(name = "driverVO",value = "司机信息",paramType = "param",dataType = "DriverVO")
    @GetMapping("/selectAllDriver")
    public List<DriverVO> selectAllDriver(DriverVO driverVO)
    {
        driverVO.setTenantId(getTenantId());
        return truckFeign.selectAllDrivers(driverVO);
    }

    @GetMapping("/countOilCardByTruckIds")
    public Integer countOilCard(@RequestParam("driverIds") Integer[] driverIds, @RequestParam("tenantId") Integer tenantId){
        return oilCardService.countOilCard(driverIds, tenantId);
    }

    /**
     * 小程序根据油卡申请人查询全部油卡信息
     */
    @GetMapping("/selectAllCardByOwnerId/{ownerDriverId}")
    @ApiOperation(value = "根据油卡申请人(司机id)、租户id查询非退卡，异常卡未办理的油卡信息")
    public List<OilCard> selectAllCardByOwnerId(@PathVariable("ownerDriverId") Integer ownerDriverId, @RequestParam("td") Integer td){
        return oilCardService.selectAllCardByOwnerId(ownerDriverId, td);
    }

    /**
     * 小程序根据油卡申请人查询正常油卡信息
     */
    @GetMapping("/selectCardByOwnerId/{ownerDriverId}")
    @ApiOperation(value = "根据油卡申请人(司机id)、租户id查询正常油卡信息")
    public List<OilCard> selectCardByOwnerId(@PathVariable("ownerDriverId") Integer ownerDriverId, @RequestParam("td") Integer td){
        return oilCardService.selectCardByOwnerId(ownerDriverId, td);
    }

    @GetMapping("driversCardNum")
    public Integer driversCardNum (Integer driverId, Integer tenantId){
        return oilCardService.driversCardNum(driverId, tenantId);
    }



      @ApiOperation(value = "根据车牌号查询该车辆下的油卡号")
      @ApiImplicitParam(name = "plateNumber",value = "车牌号",dataType = "String",paramType = "query",required = true)
      @GetMapping("/getTruckOilCard")
      public List<OilCard> getAllOilCardByPlateNum(@RequestParam("plateNumber") String plateNumber){
        List<OilCard> oilCardList = oilCardService.findAllCardsByPlateNum(plateNumber,getTenantId());
        return oilCardList;
    }

    //该主卡下面所有未使用的油卡号
    @GetMapping("/getOilCardNum/{majorId}")
    public List<OilCard> getAllOilCardByMajorId(@PathVariable ("majorId") Integer majorId){
        List<OilCard> oilCardList = oilCardService.findAllCardsByMajorId(majorId,getTenantId());
        return oilCardList;
    }


}
