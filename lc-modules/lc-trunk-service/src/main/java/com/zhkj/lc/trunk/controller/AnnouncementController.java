package com.zhkj.lc.trunk.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.zhkj.lc.common.util.AjaxResult;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.util.R;
import com.zhkj.lc.common.util.UserUtils;
import com.zhkj.lc.common.util.excel.ExcelUtil;
import com.zhkj.lc.common.web.BaseController;
import com.zhkj.lc.trunk.model.Announcement;
import com.zhkj.lc.trunk.service.AnnouncementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.zhkj.lc.common.util.UserUtils.getUser;

/**
 * @Description 公告管理 前端控制器
 * @Author ckj
 * @Date 2019/1/3 16:18
 */
@Api(description = "公告信息接口")
@RestController
@RequestMapping("/announcement")
public class AnnouncementController extends BaseController {

    @Autowired
    private AnnouncementService announcementService;

    /**
     * 通过ID查询
     *
     * @param id 公告id
     * @return 公告信息
     */
    @ApiOperation(value = "查询公告信息", notes = "根据id查询公告信息")
    @ApiImplicitParam(name = "id", value = "公告基础信息id", required = true, dataType = "Integer", paramType = "path")
    @GetMapping("/{id}")
    public R<Announcement> get(@PathVariable Integer id) {
        return new R<>(announcementService.selectById(id));
    }

    /**
     * 查询Top3信息
     *
     * @param driverOwerId 司机id
     * @return 集合
     */
    @ApiOperation(value = "小程序：搜索显示Top3公告基础信息", notes = "driverOwerId")
    @GetMapping("/listTop3/{driverOwerId}")
    public List<Announcement> list(@PathVariable String driverOwerId, @RequestParam(defaultValue = "0") Integer tId) {
        Integer id;
        if (driverOwerId.equals("null")){
            id = null;
        }else {
            id = Integer.valueOf(driverOwerId);
        }
        List<Announcement> list = announcementService.selectTop3(id,tId);
        return list;
    }

    /**
     * 公告历史记录，显示信息
     *
     * @param driverOwerId 司机id
     * @return 个人、公共公告集合
     */
    @ApiOperation(value = "小程序：公告历史记录:搜索显示公共、司机or车主公告基础信息", notes = "announcement参数不写即为获取全部信息")
    @ApiImplicitParam(name = "driverOwerId", value = "搜索条件", paramType = "path", dataType = "String")
    @GetMapping("/listDriverOwer/{driverOwerId}")
    public List<Announcement> pageDriverOwer(@PathVariable String driverOwerId, @RequestParam(defaultValue = "0") Integer tId) {
        Integer id;
        if (driverOwerId.equals("null")){
            id = null;
        }else {
            id = Integer.valueOf(driverOwerId);
        }
        Announcement announcement = new Announcement();
        announcement.setDriverOwerId(id);
        announcement.setTenantId(tId);
        return announcementService.selectAllDriverOwer(announcement);
    }

    /**
     * 公告历史记录，显示信息
     *
     * @param truckOwnId 司机id
     * @return 个人、公共公告集合
     */
    @ApiOperation(value = "小程序：车主借款公告", notes = "announcement参数不写即为获取全部信息")
    @GetMapping("/listTruckOwn")
    public List<Announcement> selectTruckOwnAnnouncement(@RequestParam Integer truckOwnId, @RequestParam(defaultValue = "0") Integer tId) {
        Announcement announcement = new Announcement();
        announcement.setTruckOwnId(truckOwnId);
        announcement.setTenantId(tId);
        return announcementService.selectTruckOwnAnnouncement(announcement);
    }

    /**
     * 获取公共或司机/车主或客服未查看公告数量
     *  @param driverOwerId 司机id
     *  @return 数量
     */
    @ApiOperation(value = "小程序：获取公共、司机/车主公告未查看数量", notes = "")
    @ApiImplicitParam(name = "driverOwerId", value = "公告信息", paramType = "path", dataType = "String")
    @GetMapping("/countDriverOwer/{driverOwerId}")
    public Integer selectDriverOwerCount(@PathVariable String driverOwerId, @RequestParam(defaultValue = "0") Integer tId){
        Integer id;
        if (driverOwerId.equals("null")){
            id = null;
        }else {
            id = Integer.valueOf(driverOwerId);
        }
        return announcementService.selectDriverOwerCount(id, tId);
    }

    @GetMapping("/countTruckOwn")
    public Integer selectTruckOwnCount(@RequestParam Integer truckOwnId, @RequestParam Integer tId){
        return announcementService.selectTruckOwnCount(truckOwnId, tId);
    }


    @ApiOperation(value = "小程序：将未查看转为已查看", notes = "announcement可选有效字段：driverOwerId")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "未被查看的公告信息ids;ids写为all则为将所有未查看转为已查看", required = true, paramType = "path", dataType = "String"),
            @ApiImplicitParam(name = "id", value = "司机id,不传值默认为0,只转化公共公告", paramType = "query", dataType = "Integer")
    })
    @GetMapping("/checkoutDriverOwer/{ids}")
    public R<Boolean> updateCheckoutById(@PathVariable("ids") String ids, @RequestParam(defaultValue = "0") Integer id, @RequestParam(defaultValue = "0") Integer tId){

        String updateBy = getUser();
        return new R<>(announcementService.updateCheckoutById(updateBy,id,ids, tId));
    }

    @GetMapping("/checkoutTruckOwn")
    public R<Boolean> updateCheckoutByTruckOwnId( @RequestParam Integer announcementId){
        Announcement announcement=new Announcement();
        announcement.setAnnouncementId(announcementId);
        announcement.setUpdateBy(getUser());
        announcement.setCheckout("0");
        return new R<>(announcementService.updateById(announcement));
    }



    /**
     * 查询所有未查看的公告基础信息
     *
     * @return 公告集合
     */
    @ApiOperation(value = "后台：查询所有未查看的公告基础信息")
    @GetMapping("/listAllNoChecked")
    public List<Announcement> listAllNoChecked() {
        Announcement announcement = new Announcement();
        announcement.setTenantId(getTenantId());
        announcement.setCheckout("1");
        announcement.setType("2");
        return announcementService.selectAll(announcement);
    }
    /**
     * 将客服公告未查看转为已查看
     * @param ids IDs
     * @return 结果
     */
    @ApiOperation(value = "后台：查询所有未查看的公告基础信息")
    @GetMapping("/2Checked/{ids}")
    public R<Boolean> update2CheckedById(@PathVariable("ids") String ids) {
        return new R<>(announcementService.update2CheckedById(ids, getUser()));
    }

    /**
     * 分页查询信息
     *
     * @param announcement 分页对象
     * @param params          分页参数
     * @return 分页集合
     */
    @ApiOperation(value = "搜索分页显示公告基础信息", notes = "params参数不写默认获取第一页10条数据；announcement参数不写即为获取全部信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "params", value = "分页参数,可选字段:page,size", paramType = "query", dataType = "Map"),
            @ApiImplicitParam(name = "announcement", value = "搜索条件", paramType = "query", dataType = "Announcement")
    })
    @GetMapping("/page")
    public Page<Announcement> page(@RequestParam Map<String, Object> params, Announcement announcement) {
        announcement.setTenantId(getTenantId());
        return announcementService.selectAll(new Query(params), announcement);
    }

    /**
     * 添加
     *
     * @param announcement 实体
     * @return success/false
     */
    @ApiOperation(value = "添加", notes = "type选择个人(0)时，driverOwerId是必要的;为1或2时，driverOwerId为空")
    @ApiImplicitParam(name = "announcement", value = "公告基础信息类", paramType = "body", dataType = "Announcement", required = true)
    @PostMapping
    public R<Boolean> add(@RequestBody Announcement announcement) {
        announcement.setCreateBy(UserUtils.getUser());
        announcement.setTenantId(getTenantId());
        announcement.setCreateTime(new Date());
        return new R<>(announcementService.insertAnnouncement(announcement));
    }

    /**
     * 添加
     *
     * @param announcement 实体
     * @return success/false
     */
    @ApiOperation(value = "外调服务：添加", notes = "type选择个人(0)时，driverOwerId是必要的;为1或2时，driverOwerId为空")
    @ApiImplicitParam(name = "announcement", value = "公告基础信息类", paramType = "body", dataType = "Announcement", required = true)
    @PostMapping("/add/feign")
    public R<Boolean> addFeign(@RequestBody Announcement announcement) {
        announcement.setCreateTime(new Date());
        return new R<>(announcementService.insertAnnouncement(announcement));
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
        String updateBy = getUser();
        return new R<>(announcementService.deleteByIds(updateBy, ids));
    }

    /**
     * 编辑
     *
     * @param announcement 实体
     * @return success/false
     */
    @ApiOperation(value = "编辑修改公告信息", notes = "announcementId是必要的")
    @ApiImplicitParam(name = "announcement", value = "公告信息", dataType = "Announcement", paramType = "body", required = true)
    @PutMapping
    public R<Boolean> edit(@RequestBody Announcement announcement) {
        announcement.setUpdateBy(getUser());
        return new R<>(announcementService.updateAnnouncementById(announcement));
    }

    /**
     * 导出
     *
     * @param announcement 公告信息
     * @return success/false 结果
     */
    @ApiOperation(value = "搜索导出", notes = "参数为空时，导出全部;")
    @ApiImplicitParam(name = "announcement", value = "公告信息", paramType = "query", dataType = "Announcement")
    @GetMapping("/export")
    public AjaxResult export(Announcement announcement, HttpServletRequest request, HttpServletResponse response) {

        announcement.setTenantId(getTenantId());
        List<Announcement> list = announcementService.selectAll(announcement);

        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        String sheetName = fmt.format(new Date()) + "-公告基础信息";
        ExcelUtil<Announcement> util = new ExcelUtil<Announcement>(Announcement.class);
        return util.exportExcel(request, response, list, sheetName, null);
    }

    /**
     * 获取有效的（有opendid的）司机id和名字，调用车辆微服务 /truDriver/selectAllDriver
     */

}
