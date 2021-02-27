package com.zhkj.lc.trunk.controller;

import com.google.gson.Gson;
import com.zhkj.lc.common.api.FileUtils;
import com.zhkj.lc.common.api.YunPianSMSUtils;
import com.zhkj.lc.common.constant.SecurityConstants;
import com.zhkj.lc.common.util.R;
import com.zhkj.lc.trunk.model.TruCustomer;
import com.zhkj.lc.trunk.service.ITruCustomerService;
import com.zhkj.lc.trunk.service.RedisService;
import com.zhkj.lc.trunk.utils.wechat.GetUseInfo;
import com.zhkj.lc.trunk.utils.wechat.HttpUtil;
import com.zhkj.lc.trunk.utils.wechat.MessageUtil;
import com.zhkj.lc.trunk.utils.wechat.getOpenidUtil;
import com.zhkj.lc.trunk.utils.wechat.start.GlobalConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

@Api(description = "微信公众号接口")
@Controller
@AllArgsConstructor
@RequestMapping("/wechatFront")
public class WechatFrontController {

//    @Value("${wxpublic.tomcaturl}")
//    public String url;

    @Autowired
    private ITruCustomerService truCustomerService;
    @Autowired
    private getOpenidUtil getOpenidUtil;

    private final RedisTemplate redisTemplate;

    @Autowired
    private RedisService redisService;


    final private String ccurl = "http://zitms.zih718.com:9000/wechat/";
//    final private String url = "http://192.168.16.114:8080/wechat/";

    /**
     * 从自定义菜单进入下单查询页面
     */
    @RequestMapping("showOrder")
    public void showOrder(Integer tenantId,HttpServletRequest request, HttpServletResponse response) throws IOException {
        String openid = getOpenidUtil.getOpenId(tenantId,request, response);
        System.out.println(openid);
        HttpSession session = request.getSession();
        session.setAttribute("openid",openid);
        TruCustomer customer = truCustomerService.selectByOpenid(openid);
        if(null != customer){
            if(customer != null && customer.getContact() == null){
                response.sendRedirect(ccurl+"information.html?tenantId="+tenantId+"&customerId="+customer.getCustomerId()+"&phone="+customer.getPhone());
            }else{
                //跳转到下单页面
                response.sendRedirect(ccurl+"order.html?tenantId="+tenantId+"&customerId="+customer.getCustomerId()+"&phone="+customer.getPhone());
            }
        } else{
            //跳转到登陆页面
            response.sendRedirect(ccurl+"bind.html?tenantId="+tenantId+"&openid="+openid);
        }
    }

    /**
     * 从自定义菜单进入订单页面
     */
    @RequestMapping("showWaybill")
    public void showWaybill(Integer tenantId,HttpServletRequest request, HttpServletResponse response) throws IOException {
        String openid = getOpenidUtil.getOpenId(tenantId,request, response);
        System.out.println(openid);
        HttpSession session = request.getSession();
        session.setAttribute("openid",openid);
        TruCustomer customer = truCustomerService.selectByOpenid(openid);
        if(null != customer){
            if(customer.getContact() == null){
                response.sendRedirect(ccurl+"information.html?tenantId="+tenantId+"&customerId="+customer.getCustomerId()+"&phone="+customer.getPhone());
            }else{
                //跳转到订单页面
                response.sendRedirect(ccurl+"waybill.html?tenantId="+tenantId+"&customerId="+customer.getCustomerId()+"&phone="+customer.getPhone());
            }
        }else{
            //跳转到登陆页面
            response.sendRedirect(ccurl+"bind.html?tenantId="+tenantId+"&openid="+openid);
        }
    }

    /**
     * 从自定义菜单进入个人中心
     */
    @RequestMapping("showPersonal")
    public void showPersonal(Integer tenantId,HttpServletRequest request, HttpServletResponse response) throws IOException {
        String openid = getOpenidUtil.getOpenId(tenantId,request, response);
        System.out.println(openid);
        HttpSession session = request.getSession();
        session.setAttribute("openid",openid);
        TruCustomer customer = truCustomerService.selectByOpenid(openid);
        if(null != customer){
            if(customer.getContact() == null){
                response.sendRedirect(ccurl+"information.html?tenantId="+tenantId+"&customerId="+customer.getCustomerId()+"&phone="+customer.getPhone());
            }else{
                //跳转到个人中心页面
                response.sendRedirect(ccurl+"account.html?tenantId="+tenantId+"&customerId="+customer.getCustomerId()+"&phone="+customer.getPhone());
            }
        }else{
            //跳转到登陆页面
            response.sendRedirect(ccurl+"bind.html?tenantId="+tenantId+"&openid="+openid);
        }
    }

    /**
     * 获取短信验证码接口
     */
    @GetMapping("getSmsCode")
    @ResponseBody
    public String getSmsCode(@RequestParam("phone")String phone,HttpServletRequest request,@RequestParam("callback")String callback) throws IOException {
        Gson g=new Gson();
        HttpSession session = request.getSession();
        String smsCode;
        if(null == session.getAttribute(phone+"smsCode")){
            smsCode = Integer.toString((int)((Math.random()*9+1)*100000));
            session.setAttribute(phone+"smsCode",smsCode);
            System.out.println("session获取："+smsCode);
        }else{
            smsCode = session.getAttribute(phone+"smsCode").toString();
            session.setAttribute(phone+"smsCode",smsCode);
            System.out.println("重新获取："+smsCode);
        }
        YunPianSMSUtils.sendWechatCode(phone,smsCode);
        //TimerTask实现5分钟后从session中删除smsCode
        final Timer timer=new Timer();
        try{
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    session.removeAttribute(phone+"smsCode");
                    System.out.println("smsCode删除成功");
                    timer.cancel();
                }
            },5*60*1000);
        }catch (Exception e){
            e.printStackTrace();
            return callback+"("+g.toJson("error")+")";
        }
        return callback+"("+g.toJson("success")+")";
    }

    /**
     * 客户登录微信公众号接口
     */
    @ApiOperation(value = "公众号登录/绑定")
    @GetMapping("login")
    @ResponseBody
    public String login(@RequestParam("phone")String phone,@RequestParam("isTrust")String isTrust,@RequestParam("openid") String openid,
                        @RequestParam("tenantId") Integer tenantId,@RequestParam("smsCode") String smsCode,
                        HttpServletRequest request, HttpServletResponse response,@RequestParam("callback")String callback) throws Exception {
        Gson g=new Gson();
        Object tempCode = redisTemplate.opsForValue().get(SecurityConstants.DEFAULT_CODE_KEY + phone);
        //手机验证码
        if(tempCode == null ){
            return callback+"("+g.toJson("error")+")";
        }else{
            if(!smsCode.equals(tempCode)){
                return callback+"("+g.toJson("error")+")";
            }
        }
        //是否存在某一用户
        TruCustomer cos = new TruCustomer();
        cos.setPhone(phone);
        cos.setTenantId(tenantId);
        TruCustomer customer = truCustomerService.selectByPhone(cos);
        //保存openid
        /*if(null != session.getAttribute("openid")){
            openid = session.getAttribute("openid").toString();
            cos.setGopenId(openid);
        }*/
        /*如果存在但是被拉黑*/
        if(null != customer && customer.getIsTrust().equals("1")){
            return callback+"("+g.toJson("black")+")";
        }
        else if(null == customer){
            try {
                HashMap<String, String> map = getUserInfo(openid, tenantId);
                cos.setPhoto(map.get("headimgurl"));
                cos.setSex(map.get("sex").equals("1")?"男":"女");
            }catch (Exception e){
                System.out.println("公众号注册、修改信息-------未获取到微信用户头像");
            }
            /*新用户*/
            cos.setGopenId(openid);
            cos.setIsTrust(isTrust);
            truCustomerService.insertCustomer(cos);
            customer = cos;
        }else{
            /*再次关注*/
            try {
                HashMap<String, String> map = getUserInfo(openid, tenantId);
                customer.setPhoto(map.get("headimgurl"));
                customer.setSex(map.get("sex").equals("1")?"男":"女");
            }catch (Exception e){
                System.out.println("公众号注册、修改信息-------未获取到微信用户头像");
            }
            customer.setGopenId(openid);
            truCustomerService.updateById(customer);
        }
        return callback+"("+g.toJson(customer)+")";
    }

    /**
     * 公众号查看个人信息
     */
    @GetMapping("selectCustomer")
    @ResponseBody
    public String selectCustomer(@RequestParam("callback")String callback , @RequestParam("customerId")Integer customerId){
        TruCustomer customer = truCustomerService.selectById(customerId);
        Gson g=new Gson();
        return callback+"("+g.toJson(customer)+")";
    }

    /**
     * 公众号个人信息修改
     */
    @GetMapping("updateCustomer")
    @ResponseBody
    public String update(@RequestParam("callback")String callback , TruCustomer truCustomer){
        Gson g=new Gson();
        try {
            TruCustomer customer = truCustomerService.selectById(truCustomer.getCustomerId());
            if(customer != null){
                HashMap<String, String> map = getUserInfo(customer.getGopenId(), truCustomer.getTenantId());
                truCustomer.setPhoto(map.get("headimgurl"));
                truCustomer.setSex(map.get("sex").equals("1")?"男":"女");
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }finally {
            truCustomerService.updateById(truCustomer);
            return callback+"("+g.toJson("success")+")";
        }
    }

    /**
     * 客户登录微信公众号接口
     */
    @ApiOperation(value = "更换绑定手机号")
    @GetMapping("bindUpdate")
    @ResponseBody
    public String bindUpdate(@RequestParam("phone")String phone,@RequestParam("customerId") Integer customerId,
                             @RequestParam("smsCode") String smsCode,HttpServletRequest request,
                             HttpServletResponse response,@RequestParam("callback")String callback) throws IOException {
        Object tempCode = redisTemplate.opsForValue().get(SecurityConstants.DEFAULT_CODE_KEY + phone);
        if(tempCode == null){
            return callback+"验证码已失效，请重新发送！";
        }else {
            if(!smsCode.equals(tempCode.toString())){
                return callback+"验证码错误！";
            }
        }
        Gson g = new Gson();
        //是否存在某一用户
        TruCustomer customer = truCustomerService.selectById(customerId);
        if(null == customer){
            return callback+"(nothingness)";
        }else {
            customer.setPhone(phone);
            truCustomerService.updateById(customer);
        }
        return callback+"("+g.toJson(customer)+")";
    }

    /**
     * 新增/修改头像
     */
    @GetMapping("updatePhoto")
    @ResponseBody
    public String updatePhoto(@RequestParam("callback")String callback,@RequestParam("costomerId")Integer costomerId,@RequestParam("file") MultipartFile file){
        TruCustomer customer = new TruCustomer();
        customer.setCustomerId(costomerId);
        Gson g=new Gson();
        try {
            if (file != null) {
                String path = FileUtils.saveFile(file);
                customer.setPhoto(path);
                truCustomerService.updateById(customer);
                return callback+"("+g.toJson("success")+")";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return callback+"("+g.toJson("error")+")";
    }

    /**
     * 退出登录
     */
    @GetMapping("exit")
    @ResponseBody
    public String exit(@RequestParam("customerId") Integer customerId,@RequestParam("callback")String callback){
        TruCustomer customer = truCustomerService.selectById(customerId);
        customer.setGopenId("0");
        truCustomerService.updateById(customer);
        Gson g=new Gson();
        return callback+"("+g.toJson("success")+")";
    }


    @GetMapping("sendSmsCode")
    @ResponseBody
    public String sendSmsCode(@RequestParam("phone")String phone,@RequestParam("callback")String callback) throws IOException {
        R<Boolean>result = redisService.sendSmsCode(phone);
        Gson g=new Gson();
        return callback+"("+g.toJson(result.getData())+")";
    }

    public  HashMap<String, String> getUserInfo(String openid, Integer tenantId) throws Exception {
        HashMap<String, String> params = new HashMap<String, String>();
        String access_token = redisTemplate.opsForValue().get("access_token" + tenantId).toString();
        params.put("access_token", access_token);  //定时器中获取到的token
        params.put("openid", openid);  //需要获取的用户的openid
        params.put("lang", "zh_CN");
        String subscribers = HttpUtil.sendGet(
                "https://api.weixin.qq.com/cgi-bin/user/info", params);
        System.out.println(subscribers);
        params.clear();
        //这里返回参数只取了昵称、头像、和性别
        params.put("nickname",
                JSONObject.fromObject(subscribers).getString("nickname")); //昵称
        params.put("headimgurl",
                JSONObject.fromObject(subscribers).getString("headimgurl"));  //图像
        params.put("sex", JSONObject.fromObject(subscribers).getString("sex"));  //性别
        return params;
    }

}
