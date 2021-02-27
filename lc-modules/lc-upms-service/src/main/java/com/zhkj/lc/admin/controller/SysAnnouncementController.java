package com.zhkj.lc.admin.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.zhkj.lc.admin.feign.DriverFeign;
import com.zhkj.lc.admin.model.entity.SysAnnouncement;
import com.zhkj.lc.admin.service.SysAnnouncementService;
import com.zhkj.lc.common.util.AjaxResult;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.util.R;
import com.zhkj.lc.common.util.UserUtils;
import com.zhkj.lc.common.util.excel.ExcelUtil;
import com.zhkj.lc.common.vo.DriverVO;
import com.zhkj.lc.common.web.BaseController;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description 公告管理 前端控制器
 * @Author ckj
 * @Date 2019/1/3 16:18
 */
@RestController
@RequestMapping("/announcement")
public class SysAnnouncementController extends BaseController {

    @Autowired
    private SysAnnouncementService announcementService;
    @Autowired
    private DriverFeign driverFeign;

    /**
     * 通过ID查询
     *
     * @param id 公告id
     * @return 公告信息
     */
    @ApiOperation(value = "查询公告信息", notes = "根据id查询公告信息")
    @ApiImplicitParam(name = "id", value = "公告基础信息id", required = true, dataType = "Integer", paramType = "path")
    @GetMapping("/{id}")
    public R<SysAnnouncement> get(@PathVariable Integer id) {
        return new R<>(announcementService.selectById(id));
    }

    /**
     * 查询Top3信息
     *
     * @param id 司机id
     * @return 集合
     */
    @ApiOperation(value = "搜索显示Top3公告基础信息", notes = "id参数不写默认获取公共公告")
    @GetMapping("/listTop3/{id}")
    public List<SysAnnouncement> list(@PathVariable Integer id) {
        List<SysAnnouncement> list = announcementService.selectTop3(id);
        return list;
    }

    /**
     * 分页查询信息
     *
     * @param sysAnnouncement 分页对象
     * @param params          分页参数
     * @return 分页集合
     */
    @ApiOperation(value = "搜索分页显示公告基础信息", notes = "params参数不写默认获取第一页10条数据；SysAnnouncement参数不写即为获取全部信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "params", value = "分页参数,可选字段:page,size", paramType = "query", dataType = "Map"),
            @ApiImplicitParam(name = "sysAnnouncement", value = "搜索条件", paramType = "query", dataType = "SysAnnouncement")
    })
    @GetMapping("/page")
    public Page<SysAnnouncement> page(@RequestParam Map<String, Object> params, SysAnnouncement sysAnnouncement) {
        return announcementService.selectAll(new Query(params), sysAnnouncement);
    }

    /**
     * 添加
     *
     * @param sysAnnouncement 实体
     * @return success/false
     */
    @ApiOperation(value = "添加", notes = "type选择个人(1)时，driverOwerId是必要的;为0时，driverOwerId为空")
    @ApiImplicitParam(name = "sysAnnouncement", value = "公告基础信息类", paramType = "body", dataType = "SysAnnouncement", required = true)
    @PostMapping
    public R<Boolean> add(@RequestBody SysAnnouncement sysAnnouncement) {
        sysAnnouncement.setCreateTime(new Date());
        sysAnnouncement.setCreateBy(UserUtils.getUser());
        return new R<>(announcementService.insert(sysAnnouncement));
    }

    /**
     * 删除
     *
     * @param ids ID
     * @return success/false
     */
    @ApiOperation(value = "删除或者批量删除公告信息，id之间用,链接，例1,2")
    @ApiImplicitParam(name = "ids", value = "公告id", paramType = "path", required = true)
    @DeleteMapping("/{ids}")
    public R<Boolean> delete(@PathVariable String ids) {
        String updateBy = "ckj"; /*getUserId().toString();*/
        return new R<>(announcementService.deleteByIds(updateBy, ids));
    }

    /**
     * 编辑
     *
     * @param sysAnnouncement 实体
     * @return success/false
     */
    @ApiOperation(value = "编辑修改公告信息", notes = "announcementId是必要的")
    @ApiImplicitParam(name = "sysAnnouncement", value = "公告信息", dataType = "SysAnnouncement", paramType = "body", required = true)
    @PutMapping
    public R<Boolean> edit(@RequestBody SysAnnouncement sysAnnouncement) {
        sysAnnouncement.setUpdateTime(new Date());
        return new R<>(announcementService.updateById(sysAnnouncement));
    }

    /**
     * 编辑
     *
     * @param sysAnnouncement 公告信息
     * @return success/false 结果
     */
    @ApiOperation(value = "搜索导出", notes = "参数为空时，导出全部;")
    @ApiImplicitParam(name = "sysAnnouncement", value = "公告信息", paramType = "query", dataType = "SysAnnouncement")
    @GetMapping("/export")
    public AjaxResult export(SysAnnouncement sysAnnouncement, HttpServletResponse response) {
        List<SysAnnouncement> list = announcementService.selectAll(sysAnnouncement);

        String type;
        for (int i = 0; i < list.size(); i++) {
            type = list.get(i).getType();
            if (null != type && !type.equals("")) {
                if (type.equals("0")) {
                    list.get(i).setType("全部");
                } else if (type.equals("1")) {
                    {
                        list.get(i).setType("个人");
                    }
                }
            }
        }
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        String sheetName = fmt.format(new Date()) + "-公告基础信息";
        ExcelUtil<SysAnnouncement> util = new ExcelUtil<SysAnnouncement>(SysAnnouncement.class);
        return util.exportExcel(response, list, sheetName, null);
    }


    /**
     * 获取有效的（有opendid的）司机id和名字，调用车辆微服务
     */
    @ApiOperation(value = "获取有效的（有opendid的）司机id和名字，调用车辆微服务")
    @PostMapping("/seleteDriver")
    public List<DriverVO> seleteDriver(){
        return driverFeign.getDriverList();
    }
}
