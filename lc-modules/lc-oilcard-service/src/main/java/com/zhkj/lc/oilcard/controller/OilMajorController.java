package com.zhkj.lc.oilcard.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.zhkj.lc.common.constant.CommonConstant;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.util.R;
import com.zhkj.lc.common.util.UserUtils;
import com.zhkj.lc.common.web.BaseController;
import com.zhkj.lc.oilcard.model.OilMajor;
import com.zhkj.lc.oilcard.service.IOilMajorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  主卡基础信息前端控制器
 * </p>
 *
 * @author wzc
 * @since 2019-02-11
 */
@Api(description = "主卡基础信息接口")
@RestController
@RequestMapping("/oilMajor")
public class OilMajorController extends BaseController {

    @Autowired private IOilMajorService oilMajorService;

    /**
    * 通过ID查询
    *
    * @param id ID
    * @return OilMajor
    */
    @ApiOperation(value = "根据id查询主卡基础信息")
    @ApiImplicitParam(name = "id",value = "主卡id",paramType = "path", dataType = "Integer", required = true)
    @GetMapping("/{id}")
    public R<OilMajor> get(@PathVariable Integer id) {
        return new R<>(oilMajorService.selectById(id));
    }


    /**
     * 获取全部主卡号
     *
     * @return 主卡对象
     */
    @ApiOperation(value = "获取全部主卡号")
    @GetMapping("/OilMajorNumber")
    public List<OilMajor> listOilMajorNumber() {
        return oilMajorService.selectOilMajorNumber(getTenantId());
    }

    /**
    * 分页查询信息
    *
    * @param params 分页对象
    * @return 分页对象
    */
    @ApiIgnore
    @RequestMapping("/page")
    public Page page(@RequestParam Map<String, Object> params) {
        params.put(CommonConstant.DEL_FLAG, CommonConstant.STATUS_NORMAL);
        return oilMajorService.selectPage(new Query<>(params), new EntityWrapper<>());
    }

    /**
     * 添加
     * @param  oilMajor  实体
     * @return success/false
     */
    @ApiOperation(value = "根据id查询主卡基础信息", notes = "主卡id不用写")
    @ApiImplicitParam(name = "oilMajor",value = "主卡信息",paramType = "body", dataType = "OilMajor", required = true)
    @PostMapping
    public R<Boolean> add(@RequestBody OilMajor oilMajor) {
        oilMajor.setCreateBy(UserUtils.getUser());
        oilMajor.setTenantId(getTenantId());
        return new R<>(oilMajorService.insertOilMajor(oilMajor));
    }

    /**
     * 删除
     * @param ids IDs
     * @return success/false
     */
    @ApiOperation(value = "删除", notes = "批量删除：主卡id中间以,相隔")
    @ApiImplicitParam(name = "ids",value = "主卡id",paramType = "path", dataType = "String", required = true)
    @DeleteMapping("/{ids}")
    public R<Boolean> delete(@PathVariable String ids) {
        String updateBy = UserUtils.getUser();
        return new R<>(oilMajorService.deleteOilMajor(ids, updateBy));
    }

    /**
     * 编辑
     * @param  oilMajor  实体
     * @return success/false
     */
    @ApiOperation(value = "编辑", notes = "主卡id是必须的")
    @ApiImplicitParam(name = "oilMajor",value = "主卡信息",paramType = "body", dataType = "OilMajor", required = true)
    @PutMapping
    public R<Boolean> edit(@RequestBody OilMajor oilMajor) {
        oilMajor.setUpdateBy(UserUtils.getUser());
        return new R<>(oilMajorService.updateOilMajor(oilMajor));
    }

    /**
     * 判断主卡号是否重复
     * @param  majorNumber  主卡号
     * @return 主卡信息
     */
    @ApiOperation(value = "判断主卡号重复")
    @ApiImplicitParam(name = "majorNumber",value = "主卡号",paramType = "path", dataType = "String", required = true)
    @GetMapping("/majorNumber2/{majorNumber}")
    public R<Boolean> majorNumber2(@PathVariable("majorNumber") String majorNumber) {

        OilMajor oilMajor = oilMajorService.majorNumber2(majorNumber,getTenantId());
        if (null != oilMajor){
            return new R<>(false);
        }
        return new R<>(true);
    }

}
