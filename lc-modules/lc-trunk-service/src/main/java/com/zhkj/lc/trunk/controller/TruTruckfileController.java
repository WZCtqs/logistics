package com.zhkj.lc.trunk.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.zhkj.lc.common.constant.CommonConstant;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.util.R;
import com.zhkj.lc.common.web.BaseController;
import com.zhkj.lc.trunk.model.TruTruckfile;
import com.zhkj.lc.trunk.service.ITruTruckfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

/**
 * <p>
 * 车辆文件表 前端控制器
 * </p>
 *
 * @author wzc
 * @since 2018-12-07
 */
@RestController
@RequestMapping("/truTruckfile")
public class TruTruckfileController extends BaseController {
   @Autowired
   private ITruTruckfileService truTruckfileService;

   /**
    * 通过ID查询
    *
    * @param id ID
    * @return TruTruckfile
    */
    @GetMapping("/{id}")
    public R<TruTruckfile> get(@PathVariable Integer id) {
        return new R<>(truTruckfileService.selectById(id));
    }


    /**
    * 分页查询信息
    *
    * @param params 分页对象
    * @return 分页对象
    */
    @PostMapping("/page")
    public Page<TruTruckfile> page(@RequestParam Map<String, Object> params) {
        params.put(CommonConstant.DEL_FLAG, CommonConstant.STATUS_NORMAL);
        return truTruckfileService.selectPage(new Query<>(params), new EntityWrapper<>());
    }

    /**
     * 添加
     * @param  truTruckfile  实体
     * @return success/false
     */
    @PostMapping
    public R<Boolean> add(@RequestBody TruTruckfile truTruckfile) {
        return new R<>(truTruckfileService.insert(truTruckfile));
    }

    /**
     * 删除
     * @param id ID
     * @return success/false
     */
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable Integer id) {
        TruTruckfile truTruckfile = new TruTruckfile();
        truTruckfile.setTruckFileId(id);
        truTruckfile.setUpdateTime(new Date());
        truTruckfile.setDelFlag(CommonConstant.STATUS_DEL);
        return new R<>(truTruckfileService.updateById(truTruckfile));
    }

    /**
     * 编辑
     * @param  truTruckfile  实体
     * @return success/false
     */
    @PutMapping
    public R<Boolean> edit(@RequestBody TruTruckfile truTruckfile) {
        truTruckfile.setUpdateTime(new Date());
        return new R<>(truTruckfileService.updateById(truTruckfile));
    }
}
