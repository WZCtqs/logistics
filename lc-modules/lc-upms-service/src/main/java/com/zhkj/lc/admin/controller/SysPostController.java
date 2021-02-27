package com.zhkj.lc.admin.controller;
import java.util.List;
import java.util.Map;
import java.util.Date;

import com.zhkj.lc.admin.model.entity.SysPost;
import com.zhkj.lc.common.util.UserUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.zhkj.lc.common.constant.CommonConstant;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.util.R;
import com.zhkj.lc.admin.service.SysPostService;
import com.zhkj.lc.common.web.BaseController;

/**
 * <p>
 * 岗位信息表 前端控制器
 * </p>
 *
 * @author cb
 * @since 2018-12-19
 */
@RestController
@RequestMapping("/sysPost")
public class SysPostController extends BaseController {
    @Autowired private SysPostService sysPostService;

    /**
    * 通过ID查询
    *
    * @param id ID
    * @return SysPost
    */
    @ApiOperation(value = "查询岗位", notes = "根据ID查询岗位")
    @ApiImplicitParam(name = "id", value = "岗位ID", required = true, dataType = "int", paramType = "path")
    @GetMapping("/{id}")
    public R<SysPost> get(@PathVariable Integer id) {
        return new R<>(sysPostService.selectById(id));
    }


    /**
    * 分页查询信息
    *
    * @param params 分页对象
    * @return 分页对象
    */
    @ApiOperation(value = "分页查询岗位", notes = "分页查询岗位数据")
    @GetMapping("/page")
    public Page page(@RequestParam Map<String, Object> params) {
        params.put(CommonConstant.DEL_FLAG, CommonConstant.STATUS_NORMAL);
        //加入租户的筛选
        params.put("tenant_id", getTenantId());
        return sysPostService.selectPage(new Query<>(params), new EntityWrapper<>());
    }

    @ApiOperation(value = "查询岗位数据集合",notes = "查询岗位数据集合")
    @GetMapping("/getPostName")
    public List<SysPost> getPostName(){
        SysPost sysPost = new SysPost();
        sysPost.setDelFlag(CommonConstant.STATUS_NORMAL);
        sysPost.setTenantId(getTenantId());
        return sysPostService.selectList(new EntityWrapper<>(sysPost));
    }
    /**
     * 添加
     * @param
     * @return success/false
     */
    @ApiOperation(value = "添加岗位", notes = "添加岗位")
    @PostMapping("/addPost")
    public R<Boolean> add(@RequestBody SysPost sysPost) {

        sysPost.setCreateBy(UserUtils.getUser());
        sysPost.setCreateTime(new Date());
        //租户的设置
        sysPost.setTenantId(getTenantId());
        return new R<>(sysPostService.insert(sysPost));
    }

    /**
     * 删除
     * @param id ID
     * @return success/false
     */
    @ApiOperation(value = "删除岗位", notes = "根据ID删除岗位")
    @ApiImplicitParam(name = "id", value = "岗位ID", required = true, dataType = "int", paramType = "path")

    @DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable Integer id) {
        SysPost sysPost = new SysPost();
        sysPost.setPostId(id);
        sysPost.setUpdateTime(new Date());
        sysPost.setUpdateBy(UserUtils.getUser());
        sysPost.setDelFlag(CommonConstant.STATUS_DEL);
        return new R<>(sysPostService.updateById(sysPost));
    }

    /**
     * 编辑
     * @param  sysPost  实体
     * @return success/false
     */
    @ApiOperation(value = "编辑岗位", notes = "编辑岗位信息")
    @PutMapping
    public R<Boolean> edit(@RequestBody SysPost sysPost) {
        sysPost.setUpdateTime(new Date());
        sysPost.setUpdateBy(UserUtils.getUser());
        return new R<>(sysPostService.updateById(sysPost));
    }
}
