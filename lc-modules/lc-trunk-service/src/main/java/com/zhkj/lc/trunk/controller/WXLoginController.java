package com.zhkj.lc.trunk.controller;

import com.zhkj.lc.common.util.http.HttpClientUtil;
import com.zhkj.lc.trunk.dto.WX;
import com.zhkj.lc.trunk.mapper.TruDriverMapper;
import com.zhkj.lc.trunk.model.TruDriver;
import com.zhkj.lc.trunk.model.TruTruckOwn;
import com.zhkj.lc.trunk.service.ITruDriverService;
import com.zhkj.lc.trunk.service.ITruTruckOwnService;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * @Auther: HP
 * @Date: 2018/12/28 11:35
 * @Description:
 */
@RestController
@RequestMapping("/wxLogin")
public class WXLoginController {
    @Autowired
    private TruDriverMapper truDriverMapper;
    @Autowired
    private ITruDriverService truDriverService;
    @Autowired
    private ITruTruckOwnService truTruckOwnService;



    /*private static final String APPID = "wxfbf76d216d72a3dd";
    private static final String SECRET = "c8463ec90b12210b8e6c4de060e15f0e";*/
    private static final String APPID = "wx49fd9919710cc75f";
    //private static final String SECRET = "954b5c593c15229e26ddc93fecf145b9";
    private static final String SECRET = "4bd849ad5b311aebe43232143d45e386";


    private static final String GRANT_TYPE = "authorization_code";

    @GetMapping("getOpenId")
    public WX getOpenId(String code){
        String url = "https://api.weixin.qq.com/sns/jscode2session";
        Map param = new HashMap<>();
        param.put("appid",APPID);
        param.put("secret",SECRET);
        param.put("js_code",code);
        param.put("grant_type",GRANT_TYPE);
        JSONObject object=JSONObject.fromObject(HttpClientUtil.doGet(url,param));
        WX wx = (WX) JSONObject.toBean(object,WX.class);
        return wx;
    }

    @ApiOperation(value = "登录前的判断操作，如果之前登陆过不需再次登录，否则跳转到登录页面")
    @PostMapping("getDriverOpenid")
    public TruDriver getDriverOpenid(String code){
        String openid = getOpenId(code).getOpenid();
        TruDriver driver = truDriverService.selectDriverBygopenId(openid);
        if(null == driver){
            return null;
        }else{
            return driver;
        }
    }

    //车主登陆前验证
    @ApiOperation(value = "车主登录前的判断操作，如果之前登陆过不需再次登录，否则跳转到登录页面")
    @PostMapping("getTruckOwnOpenid")
    public TruTruckOwn getTruckOwnOpenid(String code){
        return truTruckOwnService.selectTruckOwnBygopenId(getOpenId(code).getOpenid());
    }


    /**
     * 用户登录
     * @param phone
     * @param xopenId
     * @return
     */
    @ApiOperation(value = "登录操作")
    @PostMapping("login")
    public TruDriver login(String phone,String xopenId,String code){
        TruDriver driver = truDriverService.selectDriverByPhoneXopenId(phone,xopenId);
        String openid = getOpenId(code).getOpenid();
        if(driver != null){
            driver.setGopenId(openid);
            truDriverService.updateById(driver);
            return driver;
        }else{
            return null;
        }
    }
     //车主登陆
    @ApiOperation(value = "车主登录操作")
    @PostMapping("truckOwnLogin")
    public TruTruckOwn truckOwnLogin(String phone,String xopenId,String code){
        TruTruckOwn truTruckOwn = truTruckOwnService.selectTruckOwnByPhoneXopenId(phone,xopenId);
        String openid = getOpenId(code).getOpenid();
        if(truTruckOwn != null){
            truTruckOwn.setGopenId(openid);
            truTruckOwnService.updateById(truTruckOwn);
        }
            return truTruckOwn;
    }


    /**
     * 退出登录
     *
     */
    @ApiOperation(value = "退出登录")
    @PostMapping("exit")
    public String exit(Integer id){
        TruDriver driver = truDriverService.selectById(id);
        if(null != driver){
            driver.setGopenId("0");
            truDriverService.updateById(driver);
        }
        return "success";
    }

    //车主退出
    @ApiOperation(value = "车主退出登录")
    @PostMapping("truckOwnexit")
    public String truckOwnexit(Integer id){
        TruTruckOwn truTruckOwn = truTruckOwnService.selectById(id);
        if(null != truTruckOwn){
            truTruckOwn.setGopenId("0");
            truTruckOwnService.updateById(truTruckOwn);
        }
        return "success";
    }


    /**
     * 保存小程序用户头像
     */
    @ApiOperation(value = "保存用户头像")
    @PostMapping("saveImage")
    public Boolean saveImage(Integer id,String wxPhoto){
        TruDriver driver = truDriverService.selectById(id);
        if(null != driver){
            driver.setWxPhoto(wxPhoto);
            truDriverService.updateById(driver);
            return true;
        }else{
            return false;
        }
    }

}
