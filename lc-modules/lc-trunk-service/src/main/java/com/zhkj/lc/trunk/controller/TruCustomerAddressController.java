package com.zhkj.lc.trunk.controller;
import java.util.List;
import java.util.Date;
import com.zhkj.lc.trunk.model.TruCustomer;
import com.zhkj.lc.trunk.model.TruCustomerAddress;
import com.zhkj.lc.trunk.service.ITruCustomerAddressService;
import com.zhkj.lc.trunk.service.ITruCustomerService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.zhkj.lc.common.constant.CommonConstant;
import com.zhkj.lc.common.util.R;
import com.zhkj.lc.common.web.BaseController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wzc
 * @since 2019-02-11
 */
@RestController
@RequestMapping("/truCustomerAddress")
public class TruCustomerAddressController extends BaseController {
    @Autowired private ITruCustomerAddressService truCustomerAddressService;

    @Autowired private ITruCustomerService customerService;

    /**
    * 通过ID查询
    *
    * @param id ID
    * @return TruCustomerAddress
    */
    @ApiOperation(value = "根据id查询客户收发货地址信息")
    @GetMapping("/{id}")
    public R<TruCustomerAddress> get(@PathVariable Integer id) {
        return new R<>(truCustomerAddressService.selectById(id));
    }


    /**
     *
     * 功能描述: 根据客户id查询客户所有的收货地址信息
     *
     * @param customerId
     * @return java.util.List<com.zhkj.lc.trunk.model.TruCustomerAddress>
     * @auther wzc
     * @date 2019/2/11 19:41
     */
    @ApiOperation(value = "根据客户id查询客户所有的收货地址信息")
    @GetMapping("ReceiveList/{customerId}")
    public List<TruCustomerAddress> selectReceiveListByCustomerId(@PathVariable Integer customerId) {
        TruCustomer customer = customerService.selectById(customerId);
        if(customer != null){
            TruCustomerAddress customerAddress = new TruCustomerAddress();
            customerAddress.setCustomerId(customerId);
            customerAddress.setDataType("0");
//            customer.setTenantId(null);  //后期去掉约束
            return truCustomerAddressService.selectListByCustomerId(customerAddress);
        }else {
            return null;
        }
    }

    /**
     *
     * 功能描述: 根据客户id查询客户所有的发货地址信息
     *
     * @param customerId
     * @return java.util.List<com.zhkj.lc.trunk.model.TruCustomerAddress>
     * @auther wzc
     * @date 2019/2/11 19:41
     */
    @ApiOperation(value = "根据客户id查询客户所有的发货地址信息")
    @GetMapping("DeliverList/{customerId}")
    public List<TruCustomerAddress> selectDeliverListByCustomerId(@PathVariable Integer customerId) {
        TruCustomer customer = customerService.selectById(customerId);
        if(customer != null){
            TruCustomerAddress customerAddress = new TruCustomerAddress();
            customerAddress.setCustomerId(customerId);
            customerAddress.setDataType("1");
//            customer.setTenantId(null);  //后期去掉约束
            return truCustomerAddressService.selectListByCustomerId(customerAddress);
        }else {
            return null;
        }
    }

    /**
     * 添加
     * @param  truCustomerAddress  实体
     * @return success/false
     */
    @ApiOperation(value = "添加客户收发货人信息")
    @RequestMapping("add")
    @CrossOrigin
    public R<Boolean> add(TruCustomerAddress truCustomerAddress) {
        truCustomerAddress.setCreateTime(new Date());
        truCustomerAddress.setUpdateTime(new Date());
        truCustomerAddress.setDelFlag("0");
//        truCustomerAddress.setTenantId(getTenantId());
        return new R<>(truCustomerAddressService.insert(truCustomerAddress));
    }

    /**
     * 删除
     * @param id ID
     * @return success/false
     */
    @ApiOperation(value = "根据id删除客户收发货人信息")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable Integer id) {
        TruCustomerAddress truCustomerAddress = new TruCustomerAddress();
        truCustomerAddress.setId(id);
        truCustomerAddress.setUpdateTime(new Date());
        truCustomerAddress.setDelFlag(CommonConstant.STATUS_DEL);
        return new R<>(truCustomerAddressService.updateById(truCustomerAddress));
    }

    /**
     * 编辑
     * @param  truCustomerAddress  实体
     * @return success/false
     */
    @ApiOperation(value = "根据id修改客户收发货人信息")
    @PutMapping
    public R<Boolean> edit(@RequestBody TruCustomerAddress truCustomerAddress) {
        truCustomerAddress.setUpdateTime(new Date());
        return new R<>(truCustomerAddressService.updateById(truCustomerAddress));
    }
}
