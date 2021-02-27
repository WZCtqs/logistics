package com.zhkj.lc.order.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.zhkj.lc.common.constant.CommonConstant;
import com.zhkj.lc.common.util.R;
import com.zhkj.lc.common.util.UserUtils;
import com.zhkj.lc.common.util.support.Convert;
import com.zhkj.lc.common.web.BaseController;
import com.zhkj.lc.order.model.entity.OrdContainerType;
import com.zhkj.lc.order.service.IOrdContainerTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Auther: HP
 * @Date: 2019/6/18 08:50
 * @Description:
 */
@Api(description = "集装箱箱型")
@RestController
@RequestMapping("container")
public class OrdContainerTypeController extends BaseController {

    @Autowired private IOrdContainerTypeService containerTypeService;

    @ApiOperation(value = "获取集装箱箱型")
    @GetMapping("list")
    public List<OrdContainerType> getList(){
        OrdContainerType type = new OrdContainerType();
        type.setTenantId(getTenantId());
        type.setDelFlag(CommonConstant.STATUS_NORMAL);
        return containerTypeService.selectList(new EntityWrapper<>(type));
    }
    @ApiOperation(value = "新增")
    @PostMapping
    public R<Boolean> addType(@RequestBody OrdContainerType type){
        type.setTenantId(getTenantId());
        type.setCreateBy(UserUtils.getUser());
        type.setDelFlag(CommonConstant.STATUS_NORMAL);
        type.setCreateTime(new Date());
        return new R<>(containerTypeService.insert(type));
    }
    @ApiOperation(value = "修改")
    @PutMapping
    public R<Boolean> updateType(@RequestBody OrdContainerType type){
        type.setUpdateBy(UserUtils.getUser());
        type.setUpdateTime(new Date());
        return new R<>(containerTypeService.updateById(type));
    }

    @ApiOperation(value = "删除")
    @DeleteMapping
    public R<Boolean>delete(@RequestParam String ids){
        Integer[] id_arr = Convert.toIntArray(ids);
        List<OrdContainerType> list = new ArrayList<>();
        for (int i = 0; i < id_arr.length; i++) {
            OrdContainerType type = new OrdContainerType();
            type.setId(id_arr[i]);
            type.setDelFlag(CommonConstant.STATUS_DEL);
            list.add(type);
        }
        return new R<>(containerTypeService.updateBatchById(list));
    }

}
