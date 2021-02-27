package com.zhkj.lc.trunk.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.zhkj.lc.common.constant.CommonConstant;
import com.zhkj.lc.common.util.AjaxResult;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.util.R;
import com.zhkj.lc.common.util.UserUtils;
import com.zhkj.lc.common.util.excel.ExcelUtil;
import com.zhkj.lc.common.vo.CustomerVO;
import com.zhkj.lc.common.web.BaseController;
import com.zhkj.lc.trunk.model.TruCustomer;
import com.zhkj.lc.trunk.service.ITruCustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Date;

import static com.zhkj.lc.common.util.UserUtils.getUser;

/**
 * <p>
 * 客户信息表 前端控制器
 * </p>
 *
 * @author wzc
 * @since 2018-12-07
 */
@Api(description = "发货方管理模块")
@RestController
@RequestMapping("/truCustomer")
public class TruCustomerController extends BaseController {
    @Autowired
    private ITruCustomerService truCustomerService;

    /**
     * 导出客户信息
     * @param ids
     * @return
     */
    @ApiOperation(value = "导出客户信息")
    @GetMapping("/export/{ids}")
    public AjaxResult export(HttpServletRequest request, HttpServletResponse response, @PathVariable String ids) {
        List<TruCustomer> list = null;
        TruCustomer truCustomer = new TruCustomer();
        if("allH".equals(ids)){
            truCustomer.setTenantId(getTenantId());
            truCustomer.setIsTrust("1");
            list = truCustomerService.selectAll(truCustomer);
        }else if("allB".equals(ids)){
            truCustomer.setTenantId(getTenantId());
            truCustomer.setIsTrust("0");
            list = truCustomerService.selectAll(truCustomer);
        }else {
            list = truCustomerService.selectByIds(ids);
        }
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getCustomerType() == null || list.get(i).getCustomerType().equals("")) {
                list.get(i).setCustomerType("未知");
            } else {
                if (list.get(i).getCustomerType().equals("0")) {
                    list.get(i).setCustomerType("集装箱客户");
                } else {
                    list.get(i).setCustomerType("普货客户");
                }
            }
            if (list.get(i).getPayWay() == null || list.get(i).getPayWay().equals("")) {
                list.get(i).setPayWay("未知");
            } else {
                if (list.get(i).getPayWay().equals("0")) {
                    list.get(i).setPayWay("单结");
                } else {
                    list.get(i).setPayWay("月结");
                }
            }
    }
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        String excelName = fmt.format(new Date())+"-客户信息";
        ExcelUtil<TruCustomer> util = new ExcelUtil<TruCustomer>(TruCustomer.class);
        //return util.exportExcel(response,list, "客户信息",null);
        return util.exportExcel(request,response, list,excelName,null);
    }

    /**
     * 分页搜索查询信息
     *
     * @param params 分页对象
     * @return 分页对象
     */
    @ApiOperation(value = "分页搜索查询信息")
    @PostMapping("/pageSearch")
    public Page pageSearch(@RequestParam Map<String, Object> params,TruCustomer truCustomer) {
        params.put(CommonConstant.DEL_FLAG, CommonConstant.STATUS_NORMAL);
        if(null == truCustomer.getTenantId()){
            truCustomer.setTenantId(getTenantId());
        }
        return truCustomerService.pageSearch(new Query<>(params),truCustomer);
    }

    /**
     * 批量删除
     * @param ids
     * @return success/false
     */
    @ApiOperation(value = "批量删除")
    @DeleteMapping("/delectIds/{ids}")
    public R<Boolean> deleteIds(@PathVariable String ids) {
        return new R<>(truCustomerService.deleteByIds(ids));
    }

    /**
     * 查找所有客户信息
     * @param truCustomer
     * @return
     */
    @ApiOperation(value = "查找所有客户信息")
    @PostMapping("/selectAllCustomer")
    public List<TruCustomer> selectAllCustomer(@RequestBody TruCustomer truCustomer){
        if(null == truCustomer.getTenantId()){
            truCustomer.setTenantId(getTenantId());
        }
        return truCustomerService.selectAllCustomer(truCustomer);
    }

    /**
     * 普货订单创建查找所有客户信息（不失信）
     * @return
     */
    @ApiOperation(value = "查找所有客户信息")
    @PostMapping("/selectCustomerForPh")
    public List<TruCustomer> selectCustomerForPh(@RequestBody  TruCustomer truCustomer) {
        if(null == truCustomer.getTenantId()){
//            truCustomer.setTenantId(getTenantId());
        }
        return truCustomerService.selectAllCustomerForPh(truCustomer);
    }

    @ApiOperation(value = "查找所有客户信息")
    @PostMapping("/selectAllForFegin")
    public List<TruCustomer> selectAllForFegin(@RequestBody  TruCustomer truCustomer) {
        return truCustomerService.selectAllForFegin(truCustomer);
    }

    @ApiOperation(value = "查找所有客户信息(模糊查询)")
    @PostMapping("/selectLikeAllForFegin")
    public List<TruCustomer> selectLikeAllForFegin(@RequestBody  TruCustomer truCustomer) {
        return truCustomerService.selectLikeAllForFegin(truCustomer);
    }

    /**
     * 通过ID查询
     *
     * @param id ID
     * @return TruCustomer
     */
    @ApiOperation("通过ID查询")
    @GetMapping("/{id}")
    public TruCustomer get(@PathVariable Integer id) {
        return truCustomerService.selectById(id);
    }


    /**
     * 添加
     * @param  truCustomer  实体
     * @return success/false
     */
    @ApiOperation(value = "添加",notes = "data返回false则已存在相同手机号")
    @PostMapping
    public R<Boolean> add(@RequestBody TruCustomer truCustomer) {
        if(null == truCustomer.getTenantId()){
            truCustomer.setTenantId(getTenantId());
        }
        Integer flag = truCustomerService.checkCustomerName(truCustomer.getCustomerName(),truCustomer.getTenantId());
        if(null != flag && flag == 1){
            return new R<>(Boolean.FALSE,"客户名称已存在！");
        }
        Boolean f = truCustomerService.findByPhone(truCustomer.getPhone(), truCustomer.getTenantId());
        if(f){
            truCustomer.setCreateBy(getUser());
            truCustomer.setCreateTime(new Date());
            return new R<>(truCustomerService.insert(truCustomer));
        }else{
            return new R<>(Boolean.FALSE,"客户手机号已经存在于其他客户，不能重复！");
        }
    }

    /**
     * 删除
     * @param id ID
     * @return success/false
     */
    @ApiOperation("单个删除")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable Integer id) {
        TruCustomer truCustomer = new TruCustomer();
        truCustomer.setCustomerId(id);
        truCustomer.setUpdateTime(new Date());
        truCustomer.setDelFlag(CommonConstant.STATUS_DEL);
        return new R<>(truCustomerService.updateById(truCustomer));
    }

    /**
     * 编辑
     * @param  truCustomer  实体
     * @return success/false
     */
    @ApiOperation(value = "编辑")
    @PutMapping
    public R<Boolean> edit(@RequestBody TruCustomer truCustomer) {
        Integer id = truCustomerService.checkCustomerNameById(truCustomer.getCustomerName(),getTenantId());
        if((id != null) && (id.intValue() != truCustomer.getCustomerId().intValue())){
            return new R<>(Boolean.FALSE,"客户名称已存在！");
        }
        Integer c = truCustomerService.findByPhoneById(truCustomer.getPhone(), getTenantId());
        if((c!=null) && (c.intValue() != truCustomer.getCustomerId().intValue())){
            return new R<>(Boolean.FALSE,"客户手机号已经存在于其他客户，不能重复！");
        }
        truCustomer.setUpdateTime(new Date());
        truCustomer.setUpdateBy(getUser());
        return new R<>(truCustomerService.updateById(truCustomer));
    }

    /**
     * 批量设置白名单/黑名单
     * @param ids,sTrust
     * @return success/false
     */
    @ApiOperation("批量设置白名单/黑名单")
    @PostMapping("/setTrustByIds")
    public R<Boolean> setTrustByIds(String ids,String isTrust) {
        return new R<>(truCustomerService.setTrustByIds(ids,isTrust));
    }

    /**
     * 供租户新增时调用，新增一个租户的客户为郑州国际陆港
     */
    @PostMapping("/addCustomerForTenant")
    public Boolean addCustomerLG(@RequestBody CustomerVO customerVO){
        TruCustomer customer = new TruCustomer();
        BeanUtils.copyProperties(customerVO,customer);
        customer.setCreateTime(new Date());
        customer.setDelFlag(CommonConstant.STATUS_NORMAL);
        return truCustomerService.insert(customer);
    }


    /**
     * 跨服务
     * 查找所有客户信息
     * @param truCustomer
     * @return
     */
    @ApiOperation(value = "查找所有客户信息")
    @PostMapping("/selectAllCustomers")
    public List<TruCustomer> selectAllCustomers(@RequestBody TruCustomer truCustomer){
        return truCustomerService.selectAllCustomer(truCustomer);
    }

    /**
     * 跨服务
     * 根据客户id查找客户信息
     * @param customer
     * @return
     */
    @ApiOperation(value = "根据客户id查找客户信息")
    @PostMapping("/selectCustomerById")
    public TruCustomer selectCustomerById(@RequestBody TruCustomer customer){
        return truCustomerService.selectCustomerById(customer);
    }


    /**
     * 查找所有受信任客户信息
     * @return
     */
    @ApiOperation(value = "查找所有受信任客户信息")
    @GetMapping("/selectTrustCustomer")
    public List<TruCustomer> selectTrustCustomer(){
        TruCustomer truCustomer = new TruCustomer();
            truCustomer.setTenantId(getTenantId());
            truCustomer.setIsTrust("0");
        return truCustomerService.selectAllCustomer(truCustomer);
    }

    /**
     * 查找所有黑名单客户信息
     * @return
     */
    @ApiOperation(value = "查找所有黑名单客户信息")
        @GetMapping("/selectTrustCustomerBlack")
    public List<TruCustomer> selectTrustCustomerBlack(){
        TruCustomer truCustomer = new TruCustomer();
        truCustomer.setTenantId(getTenantId());
        truCustomer.setIsTrust("1");
        return truCustomerService.selectAllCustomer(truCustomer);
    }
}
