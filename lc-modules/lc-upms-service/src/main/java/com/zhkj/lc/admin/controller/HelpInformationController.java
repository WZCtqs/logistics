package com.zhkj.lc.admin.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.google.gson.Gson;
import com.zhkj.lc.admin.model.entity.HelpInformation;
import com.zhkj.lc.admin.service.IHelpInformationService;
import com.zhkj.lc.common.constant.CommonConstant;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.util.R;
import com.zhkj.lc.common.web.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yl
 * @since 2019-02-19
 */
@RestController
@Api(description = "帮助信息接口")
@RequestMapping("/helpInformation")
public class HelpInformationController extends BaseController {
    @Autowired private IHelpInformationService helpInformationService;

    /**
    * 通过ID查询
    *
    * @param id ID
    * @return HelpInformation
    */
    @ApiOperation(value = "通过ID查询某一帮助信息")
    @GetMapping("/{id}")
    public R<HelpInformation> get(@PathVariable Integer id) {
        HelpInformation helpInformation = helpInformationService.selectInformationById(id);
        String contentString = StringEscapeUtils.unescapeHtml4(helpInformation.getContent());
        helpInformation.setContent(contentString);
        return new R<>(helpInformation);
    }

    /**
    * 分页查询信息
    *
    * @param params 分页对象
    * @return 分页对象
    */
    @ApiOperation(value = "分页查询信息")
    @GetMapping("/page")
    public Page page(@RequestParam Map<String, Object> params,HelpInformation helpInformation) {
        helpInformation.setTenantId(getTenantId());
        //params.put(CommonConstant.DEL_FLAG, CommonConstant.STATUS_NORMAL);
        return helpInformationService.selectAll(new Query<>(params),helpInformation);
    }

    @ApiOperation(value = "通过ID查询某一帮助信息")
    @GetMapping("/wechat")
    public String getWechat(@RequestParam("id") Integer id,@RequestParam("callback")String callback) {
        HelpInformation helpInformation = helpInformationService.selectInformationById(id);
        String contentString = StringEscapeUtils.unescapeHtml4(helpInformation.getContent());
        helpInformation.setContent(contentString);
        Gson g=new Gson();
        return callback+"("+g.toJson(helpInformation)+")";
    }

    /**
     * 公众号查询全部帮助信息
     */
    @ApiOperation(value = "公众号查询信息")
    @GetMapping("selectAll")
    public String selectAll(HelpInformation information,@RequestParam("callback")String callback){
        List<HelpInformation> list = helpInformationService.selectAll(information);
        Gson g=new Gson();
        return callback+"("+g.toJson(list)+")";
    }

    /**
     * 添加
     * @param  helpInformation  实体
     * @return success/false
     */
    @ApiOperation(value = "添加")
    @PostMapping
    public R<Boolean> add(@RequestBody HelpInformation helpInformation) {
        helpInformation.setTenantId(getTenantId());
        helpInformation.setCreateBy(getUserId().toString());
        helpInformation.setTenantId(getTenantId());
        return new R<>(helpInformationService.insertInformation(helpInformation));
    }

    /**
     * 删除
     * @param id ID
     * @return success/false
     */
    @ApiOperation(value = "单个删除")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable Integer id) {
        HelpInformation helpInformation = new HelpInformation();
        helpInformation.setInformationId(id);
        helpInformation.setUpdateTime(new Date());
        helpInformation.setDelFlag(CommonConstant.STATUS_DEL);
        return new R<>(helpInformationService.updateById(helpInformation));
    }

    /**
     * 编辑
     * @param  helpInformation  实体
     * @return success/false
     */
    @ApiOperation(value = "编辑")
    @PutMapping
    public R<Boolean> edit(@RequestBody HelpInformation helpInformation) {
        helpInformation.setUpdateTime(new Date());
        return new R<>(helpInformationService.updateById(helpInformation));
    }

    /**
     * 批量删除
     */
    @ApiOperation(value = "批量删除")
    @DeleteMapping("deleteIds/{ids}")
    public R<Boolean> deleteIds(@PathVariable String ids){
        HelpInformation information = new HelpInformation();
        String[] informations = ids.split(",");
        information.setUpdateBy(getUserId().toString());
        information.setInformationIds(informations);
        return new R<>(helpInformationService.deleteIds(information));
    }
}
