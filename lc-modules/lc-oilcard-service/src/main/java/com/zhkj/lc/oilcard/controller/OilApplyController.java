package com.zhkj.lc.oilcard.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.zhkj.lc.common.util.*;
import com.zhkj.lc.common.util.excel.ExcelUtil;
import com.zhkj.lc.common.vo.AnnouncementVO;
import com.zhkj.lc.common.vo.DriverVO;
import com.zhkj.lc.common.web.BaseController;
import com.zhkj.lc.oilcard.feign.TruckFeign;
import com.zhkj.lc.oilcard.model.OilApply;
import com.zhkj.lc.oilcard.model.OilCard;
import com.zhkj.lc.oilcard.model.OilMajor;
import com.zhkj.lc.oilcard.service.IOilApplyService;
import com.zhkj.lc.oilcard.service.IOilCardService;
import com.zhkj.lc.oilcard.service.IOilMajorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 油卡申请表 前端控制器
 * </p>
 *
 * @author wzc
 * @since 2018-12-07
 */
@Api(description = "油卡申请发放接口")
@RestController
@RequestMapping("/oilApply")
public class OilApplyController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(OilApply.class);

    @Autowired private IOilApplyService oilApplyService;
    @Autowired private IOilCardService oilCardService;
    @Autowired private IOilMajorService oilMajorService;
    @Autowired private TruckFeign truckFeign;

    /**
    * 通过ID查询
    *
    * @param id ID
    * @return OilApply
    */
    @ApiOperation(value = "查询油卡申请信息", notes = "根据id查询油卡申请信息")
    @ApiImplicitParam(name = "id", value = "油卡申请ID", required = true, dataType = "Integer", paramType = "path")
    @GetMapping("/{id}")
    public R<OilApply> get(@PathVariable Integer id) {
        return new R<>(oilApplyService.selectByApplyId(id));
    }

    /**
     * 分页查询信息
     *
     * @param oilApply 分页对象
     * @param params 分页参数
     * @return 分页对象
     */
    @ApiOperation(value = "搜索分页显示油卡申请信息",notes = "params参数不写默认获取第一页10条数据；oilApply参数不写即为获取全部信息:")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "params",value = "分页参数：page页,limit页数据量",paramType = "query",dataType = "Map"),
            @ApiImplicitParam(name = "oilApply",value = "搜索条件,字段可选:cardType,openCardPlace,beginTime,endTime(申请时间的范围),attribute,truckId(根据车牌号plateNumber获取的),ownerDriverId等",paramType = "query",dataType = "OilApply")
    })
    @GetMapping("/page")
    public Page<OilApply> page(@RequestParam Map<String, Object> params, OilApply oilApply) {
        oilApply.setTenantId(getTenantId());
        return oilApplyService.selectApplyListPage(new Query(params),oilApply);
    }

    /**
     * 添加
     * @param  oilApply  实体
     * @return success/false
     */
    @ApiOperation(value = "添加")
    @ApiImplicitParam(name = "oilApply",value = "油卡申请信息类",paramType = "body",dataType = "OilApply",required = true)
    @Transactional
    @PostMapping
    public R<Boolean> add(@RequestBody OilApply oilApply) {

        AnnouncementVO announcementVO = new AnnouncementVO();
        if (null == oilApply.getCreateBy()) {
            oilApply.setCreateBy(UserUtils.getUser());
            announcementVO.setCreateBy(UserUtils.getUser());
        }else {
            announcementVO.setCreateBy(oilApply.getCreateBy());
        }
        if (null == oilApply.getTenantId()) {
            oilApply.setTenantId(getTenantId());
            announcementVO.setTenantId(getTenantId());
        }else {
            announcementVO.setTenantId(oilApply.getTenantId());
        }
        if (null == oilApply.getTruckId()){
            if (null != oilApply.getOwnerDriverId()){
                DriverVO driverVO = truckFeign.getDriverByid(oilApply.getOwnerDriverId());
                if (null != driverVO) {
                    oilApply.setTruckId(driverVO.getPlateId());
                }
            }
        }

        announcementVO.setTitle("油卡申请提交");
        announcementVO.setType("0"); // 个人
        announcementVO.setDriverOwerId(oilApply.getOwnerDriverId());
        announcementVO.setContent("油卡申请审核提交，待审核！");
        truckFeign.addFeign(announcementVO);
        return new R<>(oilApplyService.insertOilApply(oilApply));
    }

    /**
     * 批量删除
     * @param ids
     * @return success/false
     */
    @ApiOperation(value = "删除或者批量删除油卡申请信息")
    @ApiImplicitParam(name = "ids",value = "油卡申请id",paramType = "path",required = true,example = "1,2")
    @DeleteMapping("/{ids}")
    public R<Boolean> deleteIds(@PathVariable String ids) {
        String updateBy = UserUtils.getUser();
        return new R<>(oilApplyService.deleteApplyByIds(ids, updateBy));
    }

    /**
     * 编辑
     * @param  oilApply  实体
     * @return success/false
     */
    @ApiOperation(value = "编辑修改油卡申请信息",notes = "applyId是必要的")
    @ApiImplicitParam(name = "oilApply",value = "油卡申请信息",dataType = "OilApply",paramType = "body",required = true)
    @PutMapping
    public R<Boolean> edit(@RequestBody OilApply oilApply) {

        if (null == oilApply.getUpdateBy()) {
            oilApply.setUpdateBy(UserUtils.getUser());
        }
        if (null == oilApply.getTenantId()) {
            oilApply.setTenantId(getTenantId());
        }
        return new R<>(oilApplyService.updateApply(oilApply));
    }

    /**
     * 导出油卡申请
     * @param apply
     * @return
     */
    @ApiOperation(value = "搜索导出",notes = "参数为空时，导出全部；")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "油卡申请ids", paramType = "path", dataType = "String", required = true),
            @ApiImplicitParam(name = "apply", value = "油卡申请信息,字段可选:cardType,openCardPlace,beginTime,endTime(申请时间的范围),attribute,truckId(根据车牌号plateNumber获取的),ownerDriverId等", paramType = "query", dataType = "OilApply")
    })
    @PostMapping("/export/{ids}")
    public AjaxResult export(@PathVariable("ids") String ids, OilApply apply, HttpServletRequest request, HttpServletResponse response)
    {
        List<OilApply> list;
        if ("all".equals(ids)) {
            apply.setTenantId(getTenantId());
            list = oilApplyService.selectApplyList(apply);
        }else {
            list = oilApplyService.selectApplyLisIds(ids);
        }

        String sheetName = DateUtils.getDate()+"-油卡申请信息";
        ExcelUtil<OilApply> util = new ExcelUtil<OilApply>(OilApply.class);
        return util.exportExcel(request, response, list, sheetName,null);
    }

    @ApiOperation(value = "点击审核通过按钮,调的接口", notes = "参数OilApply中是只需{applyId,isPassed,ownerDriverId(不通过时另外需要的字段isPassedRemark)}")
    @ApiImplicitParam(name = "apply",value = "必要的字段值在数据类型中有体现",dataType = "OilApply",paramType = "body",required = true)
    @Transactional
    @PostMapping("/isPassed")
    public R<Boolean> isPassedUpdate(@RequestBody OilApply apply)
    {
        // 审核情况以公告的方式发送给司机
        AnnouncementVO announcementVO = new AnnouncementVO();
        announcementVO.setTitle("油卡申请审核");
        announcementVO.setType("0"); // 个人
        announcementVO.setDriverOwerId(apply.getOwnerDriverId());
        announcementVO.setCreateBy(UserUtils.getUser());
        announcementVO.setTenantId(getTenantId());
        if ("2".equals(apply.getIsPassed())){
            announcementVO.setContent("油卡申请审核不通过，原因："+(apply.getIsPassedRemark()==null ? "" : apply.getIsPassedRemark()));
        }else {
            announcementVO.setContent("油卡申请审核已通过，请等待油卡的分配发送");
        }
        truckFeign.addFeign(announcementVO);

        apply.setUpdateBy(UserUtils.getUser());
        return new R<>(oilApplyService.updateApply(apply));
    }

    @ApiOperation(value = "点击主卡绑定,调的接口", notes = "参数OilApply中是只需{applyId,oilCardNumber,majorId,deposit}")
    @ApiImplicitParam(name = "apply",value = "必要的字段值在数据类型中有体现",dataType = "OilApply",paramType = "body",required = true)
    @PostMapping("/bindingCard")
    public R<Boolean> bindingCardUpdate(@RequestBody OilApply apply)
    {
        apply.setUpdateBy(UserUtils.getUser());
        return new R<>(oilApplyService.updateApply(apply));
    }

    @ApiOperation(value = "点击发送,调的接口", notes = "参数OilApply中是只需{applyId,status}")
    @ApiImplicitParam(name = "apply",value = "必要的字段值在数据类型中有体现",dataType = "OilApply",paramType = "body",required = true)
    @Transactional
    @PostMapping("/isSend")
    public R<Boolean> isSendUpdate(@RequestBody OilApply apply)
    {
        if("1".equals(apply.getGetStatus())){
            apply.setUpdateBy(UserUtils.getUser());
            apply.setGetDate(new Date()); // 点击已发送按钮，设置发送时间

            OilApply oilApply = oilApplyService.selectByApplyId(apply.getApplyId());
            OilCard card = new OilCard();
            card.setOilCardNumber(oilApply.getOilCardNumber());
            card.setMajorId(oilApply.getMajorId());
            card.setApplyId(oilApply.getApplyId());
            card.setOwnerDriverId(oilApply.getOwnerDriverId());
            card.setTruckId(oilApply.getTruckId());
            card.setCardType(oilApply.getApplyCardType());
            Integer a = oilApply.getTruckApplied()==null?0:oilApply.getTruckApplied();
            card.setCardQuantity(a+1);
            card.setOpenCardPlace(oilApply.getOpenCardPlace());
            card.setOpenDate(oilApply.getUpdateTime());
            card.setDeposit(oilApply.getDeposit());
            card.setCreateBy(UserUtils.getUser());
            card.setTenantId(getTenantId());
            card.setCardStatus("正常");
            OilCard c1 = oilCardService.selectOne(new EntityWrapper<OilCard>().eq("oil_card_number",card.getOilCardNumber()).eq("tenant_id",getTenantId()));
            if (null != c1){
                card.setOilCardId(c1.getOilCardId());
                logger.info("油卡申请发送后更新油卡基础信息，更新情况："+ (new R<>(oilCardService.updateOilCard(card))).toString());
            }else {
                logger.info("油卡申请发送后添加油卡基础信息，添加情况："+ (new R<>(oilCardService.insertOilCard(card))).toString());
            }

            OilMajor oilMajor = oilMajorService.selectById(card.getMajorId());
            oilMajor.setCardNum(oilMajor.getCardNum() + 1);
            oilMajorService.updateOilMajor(oilMajor);

            // 审核情况以公告的方式发送给司机
            AnnouncementVO announcementVO = new AnnouncementVO();
            announcementVO.setTitle("油卡发送");
            announcementVO.setType("0"); // 个人
            announcementVO.setDriverOwerId(oilApply.getOwnerDriverId());
            announcementVO.setCreateBy(UserUtils.getUser());
            announcementVO.setTenantId(getTenantId());
            announcementVO.setContent("油卡 "+(oilApply.getOilCardNumber()==null?"":oilApply.getOilCardNumber())+" 已正在发送，请等待接收！");
            truckFeign.addFeign(announcementVO);

            return new R<>(oilApplyService.updateApply(apply));
        }
        return new R<>(false);
    }

    @ApiOperation(value = "测试油卡号是否重复")
    @ApiImplicitParam(name = "oilCardNumber",value = "油卡号",dataType = "String",paramType = "path",required = true)
    @GetMapping("/testOilCardNumber2/{oilCardNumber}")
    public R<Boolean> testOilCardNumber2(@PathVariable("oilCardNumber") String oilCardNumber)
    {
        OilApply oilApply = oilApplyService.selectByOilCardNumber(oilCardNumber, getTenantId()); // 已发送和未发送
        if (null == oilApply){
            return new R<>(true);
        }
        return new R<>(false);
    }

    @ApiOperation(value = "获取全部办卡地点")
    @PostMapping("/allOpenCardPlace")
    public List<String> selectOpenCardPlace()
    {
        List<String> openCardPlaces = oilApplyService.selectOpenCardPlace(getTenantId());
        return openCardPlaces;
    }
}
