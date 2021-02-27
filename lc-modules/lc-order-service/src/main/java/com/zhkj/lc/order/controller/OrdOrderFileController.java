package com.zhkj.lc.order.controller;
import java.util.Map;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.zhkj.lc.common.constant.CommonConstant;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.util.R;
import com.zhkj.lc.order.model.entity.OrdOrderFile;
import com.zhkj.lc.order.service.IOrdOrderFileService;
import com.zhkj.lc.common.web.BaseController;

/**
 * <p>
 * 订单文件表(派车单、附件、运输拍照) 前端控制器
 * </p>
 *
 * @author wzc
 * @since 2018-12-07
 */
@RestController
@RequestMapping("/ordOrderFile")
public class OrdOrderFileController extends BaseController {
    @Autowired private IOrdOrderFileService ordOrderFileService;

    /**
    * 通过ID查询
    *
    * @param orderId orderId
    * @return OrdOrderFile
    */
    @GetMapping("/{orderId}")
    public OrdOrderFile get(@PathVariable String orderId) {
        return ordOrderFileService.selectOrderFileById(orderId);
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
        return ordOrderFileService.selectPage(new Query<>(params), new EntityWrapper<>());
    }

    /**
     * 添加
     * @param  ordOrderFile  实体
     * @return success/false
     */
    @PostMapping
    public Boolean add(@RequestBody OrdOrderFile ordOrderFile) {
        return ordOrderFileService.insertOrderFile(ordOrderFile);
    }

    /**
     * 编辑
     * @param  ordOrderFile  实体
     * @return success/false
     */
    @PutMapping
    public Boolean edit(@RequestBody OrdOrderFile ordOrderFile) {
        //ordOrderFile.setUpdateTime(new Date());
        return ordOrderFileService.updateOrderFile(ordOrderFile);
    }
}
