package com.zhkj.lc.oilcard.controller;
import java.util.Map;
import java.util.Date;

import com.zhkj.lc.oilcard.model.OilTruckMonthRecharge;
import com.zhkj.lc.oilcard.service.IOilTruckMonthRechargeService;
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
 *  前端控制器
 * </p>
 *
 * @author JHM
 * @since 2019-05-29
 */
@RestController
@RequestMapping("/oilTruckMonthRecharge")
public class OilTruckMonthRechargeController extends BaseController {
    @Autowired private IOilTruckMonthRechargeService oilTruckMonthRechargeService;

    /**
    * 通过ID查询
    *
    * @param id ID
    * @return OilTruckMonthRecharge
    */
    @GetMapping("/{id}")
    public R<OilTruckMonthRecharge> get(@PathVariable Integer id) {
        return new R<>(oilTruckMonthRechargeService.selectById(id));
    }


    /**
    * 分页查询信息
    *
    * @param params 分页对象
    * @return 分页对象
    */
    @RequestMapping("/page")
    public Page page(@RequestParam Map<String, Object> params) {
        params.put(CommonConstant.DEL_FLAG, CommonConstant.STATUS_NORMAL);
        return oilTruckMonthRechargeService.selectPage(new Query<>(params), new EntityWrapper<>());
    }

    /**
     * 添加
     * @param  oilTruckMonthRecharge  实体
     * @return success/false
     */
    @PostMapping
    public R<Boolean> add(@RequestBody OilTruckMonthRecharge oilTruckMonthRecharge) {
        return new R<>(oilTruckMonthRechargeService.insert(oilTruckMonthRecharge));
    }

    /**
     * 删除
     * @param id ID
     * @return success/false
     */
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable Integer id) {
        OilTruckMonthRecharge oilTruckMonthRecharge = new OilTruckMonthRecharge();
        oilTruckMonthRecharge.setTruckId(id);
        oilTruckMonthRecharge.setUpdateTime(new Date());
        oilTruckMonthRecharge.setDelFlag(CommonConstant.STATUS_DEL);
        return new R<>(oilTruckMonthRechargeService.updateById(oilTruckMonthRecharge));
    }

    /**
     * 编辑
     * @param  oilTruckMonthRecharge  实体
     * @return success/false
     */
    @PutMapping
    public R<Boolean> edit(@RequestBody OilTruckMonthRecharge oilTruckMonthRecharge) {
        oilTruckMonthRecharge.setUpdateTime(new Date());
        return new R<>(oilTruckMonthRechargeService.updateById(oilTruckMonthRecharge));
    }
}
