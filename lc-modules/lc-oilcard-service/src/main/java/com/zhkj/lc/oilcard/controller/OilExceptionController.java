package com.zhkj.lc.oilcard.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.zhkj.lc.common.constant.CommonConstant;
import com.zhkj.lc.common.util.*;
import com.zhkj.lc.common.util.excel.ExcelUtil;
import com.zhkj.lc.common.vo.AnnouncementVO;
import com.zhkj.lc.common.web.BaseController;
import com.zhkj.lc.oilcard.feign.TruckFeign;
import com.zhkj.lc.oilcard.model.OilCard;
import com.zhkj.lc.oilcard.model.OilException;
import com.zhkj.lc.oilcard.service.IOilCardService;
import com.zhkj.lc.oilcard.service.IOilExceptionService;
import com.zhkj.lc.oilcard.service.impl.DictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
 * 油卡异常表 前端控制器
 * </p>
 *
 * @author wzc
 * @since 2018-12-07
 */
@Api(description = "油卡异常接口")
@RestController
@RequestMapping("/oilException")
public class OilExceptionController extends BaseController {

    @Autowired private IOilExceptionService oilExceptionService;
    @Autowired private IOilCardService cardService;
    @Autowired private DictService dictService;
    @Autowired private TruckFeign truckFeign;

   /**
    * 通过ID查询
    *
    * @param exceptionId ID
    * @return OilException
    */
    @ApiOperation(value = "通过id查询某个油卡异常信息")
   /* @ApiImplicitParam(name = "exceptionId",value = "根据油卡异常id搜索",required = true,paramType = "path",dataType = "Integer")*/
    @GetMapping("/{exceptionId}")
    public R<OilException> get(@PathVariable Integer exceptionId) {
        return new R<>(oilExceptionService.selectByExceptionId(exceptionId));
    }

    /**
    * 分页查询信息
    *
    * @param params 分页对象(查询对象)
    * @return 分页对象
    */
    //此方法中的Page为myBatisplus中的page
    @ApiOperation(value = "分页搜索查询所有油卡异常信息")
    @GetMapping("/page")
    public Page page(@RequestParam Map<String, Object> params,OilException oilException) {
        oilException.setTenantId(getTenantId());
        return oilExceptionService.selectPageList(new Query<>(params), oilException);
    }

    /**
     * 添加
     * @param  oilException  实体
     * @return success/false
     */
    @ApiOperation("添加油卡异常信息")
    /*@ApiImplicitParam(name = "oilException",required = true,value = "油卡异常详细实体oilexception",dataType = "OilException")*/
    @Transactional
    @PostMapping
    public R<Boolean> add(@RequestBody OilException oilException) {
        if(null == oilException.getTenantId()){
            oilException.setTenantId(getTenantId());
        }
        OilCard card = cardService.selectById(oilException.getOilCardId());
        //card.setCardStatus(oilException.getExceptionType());
        card.setCardStatus(dictService.getLabel("oilexception_type",oilException.getExceptionType(), oilException.getTenantId()));
        //card.setCardStatus(dictService.getLabel("exception_type",oilException.getExceptionType()));
        cardService.updateById(card);//油卡异常表中添加信息时，油卡基本信息中更改油卡异常状态
        if(null == oilException.getExceptionDate()){
            oilException.setExceptionDate(new Date());
        }
        oilException.setStatus("0");
        oilException.setDelFlag("0");
        oilException.setCreateTime(new Date());
        if (null == oilException.getCreateBy()) {
            oilException.setCreateBy(UserUtils.getUser());
        }
        return new R<>(oilExceptionService.insert(oilException));
    }

    /**
     * 删除（直接删除）
     * @param exceptionId ID
     * @return success/false
     */
    @ApiOperation(value = "删除油卡异常信息(不更改油卡基本表信息)")
    /*@ApiImplicitParam(name = "exceptionId",required = true,value = "油卡异常id",paramType = "path",dataType = "Integer")*/
    @DeleteMapping("/{exceptionId}")
    public R<Boolean> delete(@PathVariable Integer exceptionId) {
        OilException oilException = new OilException();
        oilException.setExceptionId(exceptionId);
        oilException.setUpdateTime(new Date());
        oilException.setDelFlag(CommonConstant.STATUS_DEL);
        oilException.setUpdateBy(UserUtils.getUser());
        return new R<>(oilExceptionService.updateById(oilException));
    }

    /**
     * 删除（同时更改基本信息表状态）
     * @param exceptionId ID
     * @return success/false
     */
    @ApiOperation(value = "删除油卡异常信息(更改油卡基本表信息)")
    @ApiImplicitParam(name = "exceptionId",value = "油卡异常id",paramType = "path",dataType = "Integer",required = true)
    @Transactional
    @PostMapping("delete/{exceptionId}")
    public R<Boolean> deleteOilCard(@PathVariable Integer exceptionId){
        OilException exception = oilExceptionService.selectByExceptionId(exceptionId);
        OilCard card = cardService.selectCardByOilCardNumber(exception.getOilCardNumber(), getTenantId());
        if(null != card){
            return new R<>(false);
        }else{
            card.setCardStatus("正常");
            cardService.updateById(card);
            exception.setUpdateTime(new Date());
            exception.setDelFlag(CommonConstant.STATUS_DEL);
            exception.setUpdateBy(UserUtils.getUser());
            return new R<>(oilExceptionService.updateById(exception));
        }
    }

   /**
     * 编辑
     * @param  oilException  实体
     * @return success/false
     */
    @ApiOperation(value = "编辑油卡异常信息(编辑时不能更改油卡号)")
    /*@ApiImplicitParam(name = "oilException",required = true,value = "油卡异常详细实体oilexception",dataType = "OilException")*/
    @PutMapping
    public R<Boolean> edit(@RequestBody OilException oilException) {//编辑时不能修改卡号
        oilException.setUpdateTime(new Date());
        oilException.setUpdateBy(UserUtils.getUser());
        return new R<>(oilExceptionService.updateById(oilException));
    }


    /**
     * 添加修改后的油卡号，修改油卡状态(处理操作)
     */
    @ApiOperation(value = "油卡处理操作", notes = "/editStatus/1?cardNumber=123（1是油卡异常id，123是修改后的油卡号）")
    @Transactional
    @PostMapping("/editStatus/{exceptionId}")
    public R<Boolean> editStatus(@PathVariable("exceptionId") Integer exceptionId, @RequestParam("oilCardId") Integer oilCardId,@RequestParam("cardNumber") String cardNumber){

        OilException oilexception = new OilException();
        if (null != cardNumber && !"null".equals(cardNumber)){
            oilexception.setMakeupCardNumber(cardNumber);
        }
        boolean updateOldCard = false, insertNewCard = false;
        OilException oilexception1 = oilExceptionService.selectByExceptionId(exceptionId);
        OilCard oldCard = cardService.selectById(oilexception1.getOilCardId());
        if(null != oldCard && null != oilexception1 && oilexception1.getExceptionType().equals("2")){
            oldCard.setCardStatus("正常");
            oldCard.setUpdateBy(UserUtils.getUser());
            updateOldCard = cardService.updateOilCard(oldCard);
        } else if (null != oilexception1 && !oilexception1.getExceptionType().equals("2")){

            oldCard.setApplyId(null);
            OilCard newCard = new OilCard();
            newCard.setOilCardId(oilCardId);
            newCard.setOilCardNumber(cardNumber);
            newCard.setOpenCardPlace(oilexception1.getMakeupPlace());
            newCard.setOpenDate(new Date());
            newCard.setCardStatus("正常");
            newCard.setCreateBy(UserUtils.getUser());
            cardService.updateOilCard(newCard);
           // insertNewCard = cardService.insertOilCard(newCard);
        }

        // 审核情况以公告的方式发送给司机
        AnnouncementVO announcementVO = new AnnouncementVO();
        announcementVO.setTitle("油卡异常处理发送");
        announcementVO.setType("0"); // 个人
        announcementVO.setDriverOwerId(oilexception1.getOwnerDriverId());
        announcementVO.setCreateBy(UserUtils.getUser());
        announcementVO.setTenantId(getTenantId());
        String content = "";
        if (oilexception1.getExceptionType().equals('2') && updateOldCard){
            content = "油卡异常已处理，"+(oilexception1.getOilCardNumber()==null?"":oilexception1.getOilCardNumber())+" 已恢复正常！";
        }else if (insertNewCard && !oilexception1.getExceptionType().equals('2')){
            content = "油卡异常已处理，并分配新的油卡"+(cardNumber == null?"": cardNumber)+"！";
        }
        if (null != content && content != "") {
            announcementVO.setContent(content);
            truckFeign.addFeign(announcementVO);
        }

        oilexception.setExceptionId(exceptionId);
        oilexception.setStatus("1");
        oilexception.setUpdateBy(UserUtils.getUser());
        oilexception.setUpdateTime(new Date());
        return new R<>(oilExceptionService.updateById(oilexception));
    }

    @ApiOperation(value = "油卡异常信息导出")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "油卡异常信息ids", paramType = "path", dataType = "String", required = true),
            @ApiImplicitParam(name = "oilException", value = "油卡异常详细实体oilexception(不进行条件搜索导出时不传参)", dataType = "OilException")
    })
    @PostMapping("/export/{ids}")
    public AjaxResult export(@PathVariable("ids") String ids, OilException oilException, HttpServletResponse response, HttpServletRequest request)
    {
        List<OilException> list;
        if ("all".equals(ids)) {
            oilException.setTenantId(getTenantId());
            list = oilExceptionService.selectPageList(oilException);
        }else {
            list = oilExceptionService.selectListByIds(ids);
        }
        ExcelUtil<OilException> util = new ExcelUtil<OilException>(OilException.class);
        return util.exportExcel(request, response,list, DateUtils.getDate() + "油卡异常信息",null);
    }

    /**
     * 删除油卡异常（批量）
     */
    @ApiOperation(value = "油卡批量删除(直接删除，不操作油卡基本信息表)")
   /* @ApiImplicitParam(name = "exceptionIds",required = true,value = "需要删除的多个油卡异常id",paramType = "path",dataType = "String")*/
    @PostMapping( "/remove/{ids}")
    public R<Boolean> remove(@PathVariable String ids) {
        return new R<>(oilExceptionService.deleteExceptionByIds(ids));
    }
}
