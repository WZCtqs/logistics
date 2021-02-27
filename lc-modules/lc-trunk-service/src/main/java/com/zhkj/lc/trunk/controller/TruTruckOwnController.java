package com.zhkj.lc.trunk.controller;
import com.baomidou.mybatisplus.plugins.Page;
import com.zhkj.lc.common.constant.CommonConstant;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.util.R;
import com.zhkj.lc.common.util.UserUtils;
import com.zhkj.lc.common.util.support.Convert;
import com.zhkj.lc.common.web.BaseController;
import com.zhkj.lc.trunk.model.TruTruck;
import com.zhkj.lc.trunk.model.TruTruckOwn;
import com.zhkj.lc.trunk.service.ITruTruckOwnService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * 车主信息表 前端控制器
 * </p>
 *
 * @author lzd
 * @since 2018-12-07
 */
@Api(description = "车主信息管理接口")
@RestController
@RequestMapping("/truTruckOwn")
public class TruTruckOwnController extends BaseController {

    @Autowired
    private ITruTruckOwnService truTruckOwnService;

    /**
     * 添加车主信息
     * @param truTruckOwn
     * @return
     */
    @PostMapping("/addTruckOwn")
    public R<Boolean> insertTruTruckOwn(@RequestBody TruTruckOwn truTruckOwn){
        TruTruckOwn checkTruckOwn=new TruTruckOwn();
        checkTruckOwn.setTruckownPhone(truTruckOwn.getTruckownPhone());
        checkTruckOwn.setTenantId(getTenantId());
        List<TruTruckOwn> checkTruckOwnList=truTruckOwnService.allTruTruckOwn(checkTruckOwn);
        if(checkTruckOwnList.size()==0){
            truTruckOwn.setCreateBy(UserUtils.getUser());
            truTruckOwn.setCreateTime(new Date());
            truTruckOwn.setDelFlag(CommonConstant.STATUS_NORMAL);
            truTruckOwn.setTenantId(getTenantId());
            truTruckOwn.setXopenId("234567");
            return new R<>(truTruckOwnService.insert(truTruckOwn));
        }else{
            return new R<>(Boolean.FALSE,"车主手机号已注册！");
        }
    }

    /**
     * 更新车主信息
     * @param truTruckOwn
     * @return
     */
    @PutMapping("/updateTruckOwn")
    public R<Boolean> updateTruTruckOwn(@RequestBody TruTruckOwn truTruckOwn){
        TruTruckOwn checkTruckOwn=new TruTruckOwn();
        checkTruckOwn.setTruckownPhone(truTruckOwn.getTruckownPhone());
        if(truTruckOwn.getTenantId()!=null){
            checkTruckOwn.setTenantId(truTruckOwn.getTenantId());
        }else{
            checkTruckOwn.setTenantId(getTenantId());
        }
        List<TruTruckOwn> checkTruckOwnList=truTruckOwnService.allTruTruckOwn(checkTruckOwn);
        if(checkTruckOwnList.size()!=0&&checkTruckOwnList.get(0).getId().intValue()!=truTruckOwn.getId().intValue()){
            return new R<>(Boolean.FALSE,"车主手机号已注册!");
        }else{
            truTruckOwn.setUpdateBy(UserUtils.getUser());
            truTruckOwn.setUpdateTime(new Date());
            if(truTruckOwn.getTenantId()!=null){
                truTruckOwn.setTenantId(truTruckOwn.getTenantId());
            }else{
                truTruckOwn.setTenantId(getTenantId());
            }
            return new R<>(truTruckOwnService.updateById(truTruckOwn));
        }
    }


    /**
     * 删除车主信息
     * @param ids
     * @return
     */
    @DeleteMapping("/deleteTruTruckOwn")
    public R<Boolean> deleteTruTruckOwn(@RequestParam("ids") String ids){
        String[] a = Convert.toStrArray(ids);
        TruTruckOwn truTruckOwn=new TruTruckOwn();
        if (null != a && a.length > 0) {
            int [] in = new int [a.length];
            for (int i = 0; i < a.length; i++) {
                in[i] = Integer.valueOf(a[i]);
            }
            truTruckOwn.setTenantId(getTenantId());
            truTruckOwn.setTruckOwnIds(in);
            List<String> plateNmberList=truTruckOwnService.getPlateNumbersByTruckOwnId(truTruckOwn);
            if(plateNmberList.size()>0){
                return new R<>(Boolean.FALSE,"删除车主中含有绑定车辆，刪除失败");
            }
            truTruckOwn.setUpdateTime(new Date());
            truTruckOwn.setUpdateBy(UserUtils.getUser());
            int n= truTruckOwnService.deleteByTruTruckOwn(truTruckOwn);
            if(n>0){
                return new R<>(Boolean.TRUE,"刪除成功");
            }else{
                return new R<>(Boolean.FALSE,"刪除失败");
            }
        }
        return new R<>(false, "请选中信息再进行删除操作！");
    }
    /**
     * 查询车主信息
     * @return
     */
    @GetMapping("/allTruTruckOwn")
    public List<TruTruckOwn> allTruTruckOwn(){
        TruTruckOwn truTruckOwn=new TruTruckOwn();
        truTruckOwn.setTenantId(getTenantId());
        return truTruckOwnService.allTruTruckOwn(truTruckOwn);
    }

    /**
     * 订单模块调取车主信息
     * @param truTruckOwn
     * @return
     */
    @PostMapping("/getAllTruTruckOwn")
    public List<TruTruckOwn> getAllTruTruckOwn(TruTruckOwn truTruckOwn){
        return truTruckOwnService.allTruTruckOwn(truTruckOwn);
    }
    /**
     * 订单模块根据车牌号查询车主信息
     * @param plateNumber
     * @param tenantId
     * @return
     */
    @PostMapping("/selectOwnerByPlateNumber")
    public TruTruckOwn selectOwnerByPlateNumber(@RequestParam("plateNumber")String plateNumber,
                                                @RequestParam("tenantId")Integer tenantId){
        return truTruckOwnService.selectOwnerByPlateNumber(plateNumber,tenantId);
    }

    /**
     * 订单模块根据车主id查询车牌号
     * @param truckOwnId
     * @param tenantId
     * @return
     */
    @PostMapping("/getPlateNumbersByTruckOwnId")
    public List<String> getPlateNumbersByTruckOwnId(@RequestParam("truckOwnId")Integer truckOwnId,
                                                     @RequestParam("tenantId")Integer tenantId){
        TruTruckOwn truTruckOwn=new TruTruckOwn();
        truTruckOwn.setTenantId(tenantId);
        truTruckOwn.setId(truckOwnId);
        return truTruckOwnService.getPlateNumbersByTruckOwnId(truTruckOwn);
    }

    @ApiOperation(value = "分页查询车主详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "params",value = "分页参数：page页,size页数据量",paramType = "query",dataType = "Map"),
            @ApiImplicitParam(name = "truTruckOwn",value = "搜索条件,字段可选:plateNumber(车牌号的id),truckownName,truckownPhone",paramType = "query",dataType = "TruTruckOwn")
    })
    @PostMapping("/selectTruTruckOwnByPage")
    public Page selectTruTruckOwnByPage(@RequestParam Map<String, Object> params, @RequestBody TruTruckOwn truTruckOwn) {
        truTruckOwn.setTenantId(getTenantId());
        return truTruckOwnService.selectTruTruckOwnByPage(new Query<>(params),truTruckOwn);
    }

    //根据车主id查询车辆信息
    @GetMapping("/getCarDataByTruckOwnId")
    public List<TruTruck> getCarDataByTruckOwnId(@RequestParam("truckOwnId")Integer truckOwnId){
        return truTruckOwnService.getCarDataByTruckOwnId(truckOwnId);
    }
    //根据车主id查看车主信息
    @GetMapping("/getTruckOwnByTruckOwnId")
    public TruTruckOwn getTruckOwnByTruckOwnId(@RequestParam("truckOwnId")Integer truckOwnId){
        return truTruckOwnService.selectById(truckOwnId);
    }

    //修改车主密码
    @GetMapping("/updatePassword")
    public R<Boolean> updatePassword(@RequestParam("truckOwnId")Integer truckOwnId,
                                     @RequestParam("oldPassword")String oldPassword,@RequestParam("newPassword")String newPassword){
        TruTruckOwn truTruckOwn=truTruckOwnService.selectById(truckOwnId);
        if(!truTruckOwn.getXopenId().equals(oldPassword)){
            return new R<>(Boolean.FALSE,"原密码输入错误");
        }else {
            if(oldPassword.equals(newPassword)){
                return new R<>(Boolean.FALSE,"新旧密码不能相同");
            }else{
                TruTruckOwn newTruckOwn=new TruTruckOwn();
                newTruckOwn.setId(truckOwnId);
                newTruckOwn.setXopenId(newPassword);
                return new R<>(truTruckOwnService.updateById(newTruckOwn));
            }
        }
    }
}
