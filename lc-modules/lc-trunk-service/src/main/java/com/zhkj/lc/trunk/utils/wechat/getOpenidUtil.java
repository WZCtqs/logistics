package com.zhkj.lc.trunk.utils.wechat;

import com.zhkj.lc.common.util.R;
import com.zhkj.lc.common.vo.TanentVo;
import com.zhkj.lc.trunk.feign.TanentFegin;
import com.zhkj.lc.trunk.utils.wechat.start.GlobalConstants;
import net.sf.json.JSONObject;
import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.ConnectException;
import java.net.URL;
import java.net.URLEncoder;

@Component
public class getOpenidUtil {
    private static Logger logger = Logger.getLogger(getOpenidUtil.class);

    @Autowired
    private TanentFegin tanentFegin;

    // 微信公众号的appId以及secret
    //public static String appId = GlobalConstants.getInterfaceUrl("appid");
    //public static String secret = GlobalConstants.getInterfaceUrl("AppSecret");
    //获取code的url
    public static String getCodeUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI?response_type=code&scope=snsapi_base&state=1&connect_redirect=1#wechat_redirect";
    // 获取网页授权access_token的Url，和基础服务access_token不同，记得区分
    public static String getAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";

    /**
     * 获取code值
     * @throws IOException
     */
    public String getCode(Integer tenantId,HttpServletRequest request, HttpServletResponse response) throws IOException {
        R<TanentVo> tanent = tanentFegin.get(tenantId);

        String redirectUrl = request.getRequestURL().toString();
        redirectUrl = URLEncoder.encode(redirectUrl,"UTF-8");
        //System.out.println("redirectUrl："+redirectUrl);
        String url = getOpenidUtil.getCodeUrl.replace("APPID", tanent.getData().getAppid()).replace("REDIRECT_URI", redirectUrl);
        //System.out.println("url"+url);
        //String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx36fe818c5b126c59&redirect_uri=http%3A%2F%2F21x91010v5.iok.la%2FwechatFront%2Flogin?response_type=code&scope=snsapi_base&state=1&connect_redirect=1#wechat_redirect";
        return url;
    }

    /**
     * 获取openid
     * @param request
     * @param response
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public String getOpenId(Integer tenantId, HttpServletRequest request, HttpServletResponse response) throws ClientProtocolException, IOException {
        R<TanentVo> tanent = tanentFegin.get(tenantId);

        //根据code获取openid
        String code = request.getParameter("code");//微信会返回code值，用code获取openid
        logger.debug("get openid start" + code);
        if(code == null) {
            return "";
        }else {
            String url = getOpenidUtil.getAccessTokenUrl.replace("APPID", tanent.getData().getAppid()).replace("SECRET", tanent.getData().getAppsecret())
                    .replace("CODE", code);
            JSONObject jsonObj=JSONObject.fromObject(httpRequest(url, "POST", null));
            WeChatAccessToken wechat = (WeChatAccessToken) JSONObject.toBean(jsonObj,WeChatAccessToken.class);
            String openid = wechat.getOpenid();
            System.out.println("getopenid:"+openid);
            return openid;
        }
    }

    /**
     * get或者post请求
     *
     * @param requestUrl
     * @param requestMethod
     *            GET or POST 需要大写*
     * @param outputStr
     * @return
     */
    public static String httpRequest(String requestUrl, String requestMethod, String outputStr) {
        StringBuffer buffer = new StringBuffer();
        try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = { new MyX509TrustManager() };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            URL url = new URL(requestUrl);
            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
            httpUrlConn.setSSLSocketFactory(ssf);
            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            httpUrlConn.setRequestMethod(requestMethod);
            if ("GET".equalsIgnoreCase(requestMethod))
                httpUrlConn.connect();
            // 当有数据需要提交时
            if (null != outputStr) {
                OutputStream outputStream = httpUrlConn.getOutputStream();
                // 注意编码格式，防止中文乱码
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }
            // 将返回的输入流转换成字符串
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            inputStream = null;
            httpUrlConn.disconnect();
        } catch (ConnectException ce) {
            System.out.println("Weixin server connection timed out." + ce.getMessage());
        } catch (Exception e) {
            System.out.println("https request error:{}" + e.getMessage());
        }
        return buffer.toString();
    }



}
