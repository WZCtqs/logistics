package com.zhkj.lc.trunk.utils.wechat;

import com.zhkj.lc.trunk.utils.wechat.dispatcher.EventDispatcher;
import com.zhkj.lc.trunk.utils.wechat.dispatcher.MsgDispatcher;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;


@Controller
@RequestMapping("logistics")
public class WechatController {
    private static Logger logger = Logger.getLogger(WechatController.class);

    @Autowired
    private EventDispatcher eventDispatcher;

    @Autowired
    private MsgDispatcher msgDispatcher;


    /**
     *
     * @Description: 用于接收get参数，返回验证参数
     * @param request
     * @param response
     * @param signature
     * @param timestamp
     * @param nonce
     * @param echostr
     */
    @RequestMapping(value = "security", method = RequestMethod.GET)
    public void doGet(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(value = "signature", required = true) String signature,
            @RequestParam(value = "timestamp", required = true) String timestamp,
            @RequestParam(value = "nonce", required = true) String nonce,
            @RequestParam(value = "echostr", required = true) String echostr) {
        try {
            if (SignUtil.checkSignature(signature, timestamp, nonce)) {
                PrintWriter out = response.getWriter();
                out.print(echostr);
                out.close();
            } else {
                logger.info("这里存在非法请求！");
            }
        } catch (Exception e) {
            logger.error(e, e);
        }
    }

    @RequestMapping(value = "security", method = RequestMethod.POST)
    // post方法用于接收微信服务端消息
    public void DoPost(HttpServletRequest request,HttpServletResponse response) {
        try{
            Map<String, String> map=MessageUtil.parseXml(request);
            String msgtype=map.get("MsgType");
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            if(MessageUtil.REQ_MESSAGE_TYPE_EVENT.equals(msgtype)){
                String msgrsp=eventDispatcher.processEvent(map); //进入事件处理
                PrintWriter out = response.getWriter();
                out.print(msgrsp);
                out.close();
            }else{
                String msgrsp=msgDispatcher.processMessage(map); //进入消息处理
                PrintWriter out = response.getWriter();
                out.print(msgrsp);
                out.close();
            }
        }catch(Exception e){
            logger.error(e,e);
        }
        System.out.println("这是post方法！");
    }
}
