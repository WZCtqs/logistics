package com.zhkj.lc.trunk.controller;
import java.util.Map;
import java.util.Date;

import com.zhkj.lc.trunk.model.FreightRoute;
import com.zhkj.lc.trunk.service.IFreightRouteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.zhkj.lc.common.constant.CommonConstant;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.util.R;
import com.zhkj.lc.common.web.BaseController;

/**
 * <p>
 * 司机常跑路线 前端控制器
 * </p>
 *
 * @author wzc
 * @since 2019-01-03
 */
@Api(description = "司机常跑路线管理接口")
@RestController
@RequestMapping("/freightRoute")
public class FreightRouteController extends BaseController {
    @Autowired private IFreightRouteService freightRouteService;

    /**
    * 通过ID查询
    *
    * @param id ID
    * @return FreightRoute
    */
    @GetMapping("/{id}")
    public R<FreightRoute> get(@PathVariable Integer id) {
        return new R<>(freightRouteService.selectById(id));
    }


    /**
    * 分页查询信息
    *
    * @param params 分页对象
    * @return 分页对象
    */
    @PostMapping("/page")
    public Page page(@RequestParam Map<String, Object> params) {
        params.put(CommonConstant.DEL_FLAG, CommonConstant.STATUS_NORMAL);
        return freightRouteService.selectPage(new Query<>(params), new EntityWrapper<>());
    }

    /**
     * 添加
     * @param  freightRoute  实体
     * @return success/false
     */
    @ApiOperation(value = "添加")
    @ApiImplicitParam(name = "freightRoute",value = "司机常跑路线信息类",paramType = "body",dataType = "FreightRoute",required = true)
    @PostMapping
    public R<Boolean> add(@RequestBody FreightRoute freightRoute) {
        return new R<>(freightRouteService.insert(freightRoute));
    }

    /**
     * 删除
     * @param id ID
     * @return success/false
     */
    @ApiOperation(value = "删除")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable Integer id) {
        FreightRoute freightRoute = new FreightRoute();
        freightRoute.setId(id);
        freightRoute.setUpdateTime(new Date());
        freightRoute.setDelFlag(CommonConstant.STATUS_DEL);
        return new R<>(freightRouteService.updateById(freightRoute));
    }

    /**
     * 编辑
     * @param  freightRoute  实体
     * @return success/false
     */
    @PutMapping
    public R<Boolean> edit(@RequestBody FreightRoute freightRoute) {
        freightRoute.setUpdateTime(new Date());
        return new R<>(freightRouteService.updateById(freightRoute));
    }
}
