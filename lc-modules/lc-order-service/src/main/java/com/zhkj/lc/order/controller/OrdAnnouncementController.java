package com.zhkj.lc.order.controller;

import com.zhkj.lc.common.util.R;
import com.zhkj.lc.common.util.UserUtils;
import com.zhkj.lc.common.vo.AnnouncementVO;
import com.zhkj.lc.common.web.BaseController;
import com.zhkj.lc.order.feign.TrunkFeign;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description TODO
 * @Author ckj
 * @Date 2019/2/14 14:59
 */
@Api(description = "首页公告")
@RestController
@RequestMapping("/ordAnnouncement")
public class OrdAnnouncementController  extends BaseController {

    @Autowired
    private TrunkFeign trunkFeign;

    /**
     * 后台：查询所有未查看的公告基础信息
     *
     * @return 公告集合
     */
    @ApiOperation(value = "后台首页：查询所有未查看的公告基础信息")
    @GetMapping("/listAllNoChecked")
    public List<AnnouncementVO> ordListAllNoChecked(){
        Integer tenantId = getTenantId();
        return trunkFeign.listAllNoChecked(tenantId);
    }

    /**
     * 将客服公告通知转为已读
     *
     * @param ids 公告ids
     * @return 结果
     */
    @ApiOperation(value = "后台首页：将客服公告通知转为已读")
    @GetMapping("/2Checked/{ids}")
    public R<Boolean> ordUpdate2CheckedById(@PathVariable("ids") String ids){
        return trunkFeign.update2CheckedById(ids);
    }
}
