package com.zhkj.lc.order.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.zhkj.lc.common.util.*;
import com.zhkj.lc.common.util.excel.ExcelUtil;
import com.zhkj.lc.common.web.BaseController;
import com.zhkj.lc.order.dto.OrdCommonGoodsVo;
import com.zhkj.lc.order.dto.OrdCommonTruckVO;
import com.zhkj.lc.order.model.entity.OrdExceptionFee;
import com.zhkj.lc.order.model.entity.OrdOrder;
import com.zhkj.lc.order.service.IOrdCommonGoodsService;
import com.zhkj.lc.order.service.IOrdCommonTruckService;
import com.zhkj.lc.order.service.IOrdExceptionFeeService;
import com.zhkj.lc.order.service.IOrdOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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

import static com.zhkj.lc.common.util.UserUtils.getUser;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wzc
 * @since 2019-01-11
 */
@Api(value = "订单异常费用接口")
@RestController
@RequestMapping("/ordExceptionFee")
public class OrdExceptionFeeController extends BaseController {

    @Autowired
    private IOrdExceptionFeeService ordExceptionFeeService;

    @Autowired
    private IOrdOrderService ordOrderService;
    @Autowired
    private IOrdCommonGoodsService commonGoodsService;
    @Autowired
    private IOrdCommonTruckService commonTruckService;

    /**
     * 通过ID查询
     *
     * @param id ID
     * @return OrdExceptionFee
     */
    @ApiOperation(value = "根据id获取订单异常费用信息")
    @ApiImplicitParam(name = "id", value = "异常费用id", required = true, dataType = "Integer", paramType = "path")
    @GetMapping("/{id}")
    public R<OrdExceptionFee> get(@PathVariable Integer id) {
        return new R<>(ordExceptionFeeService.getByid(id));
    }

    /**
     * 查询信息
     *
     * @param ordExceptionFee 搜索条件
     * @return
     */
    @ApiOperation(value = "小程序端显示全部订单异常费用信息", notes = "ordExceptionFee必写:orderId,tenantId")
    @ApiImplicitParam(name = "ordExceptionFee", value = "搜索条件", dataType = "OrdExceptionFee", paramType = "query")
    @GetMapping("/list")
    public List<OrdExceptionFee> list(OrdExceptionFee ordExceptionFee) {
        return ordExceptionFeeService.selectOrdExceptionFeeList(ordExceptionFee);
    }

    /**
     * 分页查询信息
     *
     * @param params          分页参数
     * @param ordExceptionFee 搜索条件
     * @return
     */
    @ApiOperation(value = "分页查询订单异常费用信息", notes = "params默认page为1，limit为10；ordExceptionFee不写即为搜索全部；")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "params", value = "分页参数", dataType = "Map", paramType = "query"),
            @ApiImplicitParam(name = "ordExceptionFee", value = "搜索条件", dataType = "OrdExceptionFee", paramType = "query")
    })
    @GetMapping("/page")
    public Page page(@RequestParam Map<String, Object> params, OrdExceptionFee ordExceptionFee) {
        ordExceptionFee.setTenantId(getTenantId());
        return ordExceptionFeeService.selectOrdExceptionFeeList(new Query<>(params), ordExceptionFee);
    }

    /**
     * 添加
     *
     * @param ordExceptionFee 实体
     * @return success/false
     */
    @ApiOperation(value = "添加订单异常费用信息", notes = "id不用设值;订单id值必写")
    //@ApiImplicitParam(name = "ordExceptionFee",value = "订单异常费用信息",required = true,dataType = "OrdExceptionFee",paramType = "body")
    @PostMapping
    public R<Boolean> add(@RequestBody OrdExceptionFee ordExceptionFee) {
        if(ordExceptionFee.getOrderId().startsWith("CN")){
        ordExceptionFee.setOrderStatus(ordOrderService.selectOrderByOrderId(ordExceptionFee.getOrderId(),getTenantId()).getStatus());
        }
        else if (ordExceptionFee.getOrderId().startsWith("PH")){
            OrdCommonGoodsVo ordCommonGoodsVo=new OrdCommonGoodsVo();
            ordCommonGoodsVo.setMorderId(ordExceptionFee.getOrderId());
            ordCommonGoodsVo.setMtenantId(getTenantId());
            ordExceptionFee.setOrderStatus(commonGoodsService.selectOneByOrderId(ordCommonGoodsVo).getStatus());
        }
        ordExceptionFee.setTenantId(getTenantId());
        String[] strings = ordExceptionFee.getImgUrlFile();
        if (null != strings && strings.length > 0) {
            ordExceptionFee.setImgUrl(StringUtils.join(strings,","));
        }
        ordExceptionFee.setApplyBy(UserUtils.getUser());
        return new R<>(ordExceptionFeeService.insertOrdExceptionFee(ordExceptionFee));
    }

    /**
     * 小程序添加
     *
     * @param ordExceptionFee 实体
     * @return success/false
     */
    @ApiOperation(value = "小程序：添加订单异常费用信息", notes = "id不用设值;订单id值必写")
    //@ApiImplicitParam(name = "ordExceptionFee",value = "订单异常费用信息",required = true,dataType = "OrdExceptionFee",paramType = "body")
    @PostMapping("/addApp")
    public R<Boolean> addApp(@RequestBody OrdExceptionFee ordExceptionFee, HttpServletRequest request) {

        Integer tenantId = Integer.parseInt(request.getHeader("tenantId"));
        ordExceptionFee.setOrderStatus(ordOrderService.selectOrderByOrderId(ordExceptionFee.getOrderId(),tenantId ).getStatus());

        ordExceptionFee.setTenantId(tenantId);
        String[] strings = ordExceptionFee.getImgUrlFile();
        if (null != strings && strings.length > 0) {
            ordExceptionFee.setImgUrl(StringUtils.join(strings, ","));
        }
        return new R<>(ordExceptionFeeService.insertOrdExceptionFee(ordExceptionFee));
    }

    /**
     * 删除
     *
     * @param ids ID
     * @return success/false
     */
    @ApiOperation(value = "删除订单异常费用信息")
    @ApiImplicitParam(name = "ids", value = "要删除的id", required = true, dataType = "String", paramType = "path")
    @DeleteMapping("/{ids}")
    public R<Boolean> delete(@PathVariable String ids) {
        Integer tenantId = getTenantId();
        String updateBy = getUser();
        return new R<>(ordExceptionFeeService.deleteOrdExceptionFeeByIds(tenantId, updateBy, ids));
    }

    /**
     * 删除
     *
     * @param ids ID
     * @return success/false
     */
    @ApiOperation(value = "小程序删除订单异常费用信息")
    @ApiImplicitParam(name = "ids", value = "要删除的id", required = true, dataType = "String", paramType = "path")
    @DeleteMapping("/app/{ids}")
    public R<Boolean> deleteApp(@PathVariable String ids, HttpServletRequest request) {
        Integer tenantId = Integer.parseInt(request.getHeader("tenantId"));
        String updateBy = request.getHeader("driverId");
        return new R<>(ordExceptionFeeService.deleteOrdExceptionFeeByIds(tenantId, updateBy, ids));
    }

    /**
     * 编辑
     *
     * @param ordExceptionFee 实体
     * @return success/false
     */
    @ApiOperation(value = "更新订单异常费用信息")
    //@ApiImplicitParam(name = "ordExceptionFee",value = "订单异常信息",required = true,dataType = "OrdExceptionFee",paramType = "body")
    @PutMapping
    public R<Boolean> edit(@RequestBody OrdExceptionFee ordExceptionFee) {

        ordExceptionFee.setUpdateBy(getUser());
        String[] strings = ordExceptionFee.getImgUrlFile();
        if (null != strings && strings.length > 0) {
            ordExceptionFee.setImgUrl(StringUtils.join(strings,","));
        }
        return new R<>(ordExceptionFeeService.updateOrdExceptionFee(ordExceptionFee, null, null));
    }

    /**
     * 导出
     *
     * @param ordExceptionFee 分页对象
     * @return
     */
    @ApiOperation(value = "导出", notes = "选中导出传选中id,/export/1,2;导出全部的时候传all,/export/all")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "订单基础信息ids", paramType = "path", dataType = "String", required = true),
            @ApiImplicitParam(name = "ordExceptionFee", value = "订单异常信息", dataType = "OrdExceptionFee", paramType = "query")
    })
    @GetMapping("/export/{ids}")
    public AjaxResult export(@PathVariable("ids") String ids, OrdExceptionFee ordExceptionFee, HttpServletRequest request, HttpServletResponse response) {

        List<OrdExceptionFee> list;
        if ("all".equals(ids)){
            ordExceptionFee.setTenantId(getTenantId());
            list = ordExceptionFeeService.selectOrdExceptionFeeList(ordExceptionFee);
        }else {
            list = ordExceptionFeeService.selectOrdExceptionFeeListByIds(ids);
        }

        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        String sheetName = fmt.format(new Date())+"-订单异常费用信息";
        ExcelUtil<OrdExceptionFee> util = new ExcelUtil<OrdExceptionFee>(OrdExceptionFee.class);
        return util.exportExcel(request,response, list, sheetName, null);
    }

    /**
     * 分页查询信息
     *
     * @param ordExceptionFee 分页对象
     * @return
     */
    @ApiOperation(value = "审核状态",notes = "ordExceptionFee包含id和审核状态(1通过，2拒绝)")
    //@ApiImplicitParam(name = "ordExceptionFee",value = "订单异常信息",required = true,dataType = "OrdExceptionFee",paramType = "body")
    @PostMapping("/isPassed")
    public R<Boolean> isPassed(@RequestBody OrdExceptionFee ordExceptionFee, HttpServletRequest request) {
        OrdOrder ordOrder = null;
        OrdCommonTruckVO commonTruckVO = null;

        /*判断订单*/
        switch (ordExceptionFee.getOrderId().substring(0,2)){
            case "CN":
                ordOrder = ordOrderService.selectOrderBaseById(ordExceptionFee.getOrderId(), getTenantId());
                if(Integer.valueOf(ordOrder.getNeedPayStatus()) > 4){
                    return new R<>(Boolean.FALSE,"该订单对应的账单费用已结算，不能审核！");
                }
                break;
            case "PH":
                commonTruckVO = commonTruckService.selectCommonTruck(ordExceptionFee.getOrderId());
                if(Integer.valueOf(commonTruckVO.getNeedPayStatus()) > 4) {
                    return new R<>(Boolean.FALSE,"该订单对应的账单费用已结算，不能审核！");
                }
                break;
        }
        ordExceptionFee.setTenantId(getTenantId());
        ordExceptionFee.setUpdateBy(getUser());
        ordExceptionFee.setTransactor(getUserId());
        ordExceptionFee.setHandleTime(new Date());
        return new R<>(ordExceptionFeeService.updateOrdExceptionFee(ordExceptionFee, ordOrder, commonTruckVO));
    }

    /**
     * 根据订单id查询该订单下所有异常费用
     * @param orderId
     * @return
     */
    @PostMapping("selectExFee/{orderId}")
    public List<OrdExceptionFee> selectExFee(@PathVariable String orderId){
        return ordExceptionFeeService.selectExFee(orderId);
    }
}
