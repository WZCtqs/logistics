package com.zhkj.lc.common.api;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import com.alibaba.fastjson.JSONObject;
import com.zhkj.lc.common.api.common.JSMSConfig;
import com.zhkj.lc.common.api.common.ResultCodeEmnu;
import com.zhkj.lc.common.api.common.SMSClient;
import com.zhkj.lc.common.api.common.YunPianResultCodeEmnu;
import com.zhkj.lc.common.api.common.model.SMSPayload;
import com.zhkj.lc.common.constant.CommonConstant;
import com.zhkj.lc.common.util.R;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 短信http接口的java代码调用示例
 * 基于Apache HttpClient 4.3
 *
 * @author songchao
 * @since 2015-04-03
 */

public class YunPianSMSUtils {

    //查账户信息的http地址
    private static String URI_GET_USER_INFO =
            "https://sms.yunpian.com/v2/user/get.json";

    //智能匹配模板发送接口的http地址
    private static String URI_SEND_SMS =
            "https://sms.yunpian.com/v2/sms/single_send.json";

    //模板发送接口的http地址
    private static String URI_TPL_SEND_SMS =
            "https://sms.yunpian.com/v2/sms/tpl_single_send.json";

    //发送语音验证码接口的http地址
    private static String URI_SEND_VOICE =
            "https://voice.yunpian.com/v2/voice/send.json";

    //绑定主叫、被叫关系的接口http地址
    private static String URI_SEND_BIND =
            "https://call.yunpian.com/v2/call/bind.json";

    //解绑主叫、被叫关系的接口http地址
    private static String URI_SEND_UNBIND =
            "https://call.yunpian.com/v2/call/unbind.json";

    //获取模板信息http地址
    private static String URI_GET_TPL =
            "https://sms.yunpian.com/v2/tpl/get.json";

    //编码格式。发送编码格式统一用UTF-8
    private static String ENCODING = "UTF-8";

    protected static final Logger LOG = LoggerFactory.getLogger(YunPianSMSUtils.class);

    final private static String APIKEY = "422778e446ef62139a521c4ac0d2e260";

    public static void main(String[] args) throws IOException,
            URISyntaxException {
        System.out.println(getTplList());
    }

    public static String getTplList(){
        Map < String, String > params = new HashMap < String, String > ();
        params.put("apikey", APIKEY);
        return post(URI_GET_TPL, params);
    }


    /**
     *
     * 功能描述: 司机接单通知短信方法
     *
     * @param phone 手机号
     * @param orderId 订单编号
     * @return void
     * @auther wzc
     * @date 2019/1/15 16:51
     */
    public static R<Boolean> sendDriverNewOrder(String phone, String orderId) throws IOException {
        /*设置模板id*/
        long tpl_id = CommonConstant.TPL_ID_NEW;
        String orderType = orderId.substring(0,2).equals("CN")?"集装箱":"普货";
        //设置对应的模板变量值
        String tpl_value =
                URLEncoder.encode("#orderType#", ENCODING) + "=" + URLEncoder.encode(orderType, ENCODING)
                        + "&" + URLEncoder.encode("#orderId#", ENCODING) + "=" + URLEncoder.encode(orderId, ENCODING)	;
        String resultCode = YunPianSMSUtils.tplSendSms(APIKEY, tpl_id, tpl_value, phone);
        JSONObject obj = JSONObject.parseObject(resultCode);
        /*获取返回消息*/
        String code = obj.getString("code");
        System.out.println(obj.toString());
        if(code=="0" || code.equals("0")){
            return new R<>(Boolean.TRUE, YunPianResultCodeEmnu.getName(Integer.valueOf(code)));
        }
        return new R<>(Boolean.FALSE, YunPianResultCodeEmnu.getName(Integer.valueOf(code)));
    }

    /**
     * 发送给司机账单
     * @param phone
     * @param orderId
     * @return
     * @throws IOException
     */
    public static R<Boolean> sendDriverNewBill(String phone,String driverName, String orderId) throws IOException {
        /*设置模板id*/
        long tpl_id = CommonConstant.TPL_ID_BILL;

        //设置对应的模板变量值
        String tpl_value =
                URLEncoder.encode("#driverName#", ENCODING) + "=" + URLEncoder.encode(driverName, ENCODING)
                        + "&" + URLEncoder.encode("#orderId#", ENCODING) + "=" + URLEncoder.encode(orderId, ENCODING)	;
        String resultCode = YunPianSMSUtils.tplSendSms(APIKEY, tpl_id, tpl_value, phone);
        JSONObject obj = JSONObject.parseObject(resultCode);
        /*获取返回消息*/
        String code = obj.getString("code");
        System.out.println(obj.toString());
        if(code=="0" || code.equals("0")){
            return new R<>(Boolean.TRUE, YunPianResultCodeEmnu.getName(Integer.valueOf(code)));
        }
        return new R<>(Boolean.FALSE, YunPianResultCodeEmnu.getName(Integer.valueOf(code)));
    }
    /**
     *
     * 功能描述: 提货后发送给收货人及签收码
     *
     * @param receiptCode 签收码
     * @param phone 收货人手机号
     * @param orderId 订单编号
     * @return void
     * @auther wzc
     * @date 2019/1/15 17:51
     */
    public static R<Boolean> sendConsigneeReceiptCode(String phone, String receiptCode, String orderId, Integer sort, String plate) throws IOException {
        /*设置模板id*/
        long tpl_id = CommonConstant.TPL_ID_RCODE;
        //设置对应的模板变量值
        String tpl_value =
                URLEncoder.encode("#orderId#", ENCODING) + "=" + URLEncoder.encode(orderId, ENCODING)
                        + "&" + URLEncoder.encode("#number#", ENCODING) + "=" + URLEncoder.encode(receiptCode, ENCODING)
                        + "&" + URLEncoder.encode("#plate#", ENCODING) + "=" + URLEncoder.encode(plate, ENCODING)
                        + "&" + URLEncoder.encode("#sort#", ENCODING) + "=" + URLEncoder.encode(sort.toString(), ENCODING);
        String resultCode = YunPianSMSUtils.tplSendSms(APIKEY, tpl_id, tpl_value, phone);
        JSONObject obj = JSONObject.parseObject(resultCode);
        /*获取返回消息*/
        String code = obj.getString("code");
        System.out.println(obj.toString());
        if(code=="0" || code.equals("0")){
            return new R<>(Boolean.TRUE, YunPianResultCodeEmnu.getName(Integer.valueOf(code)));
        }
        return new R<>(Boolean.FALSE, YunPianResultCodeEmnu.getName(Integer.valueOf(code)));
    }
    /**
     *
     * 功能描述: 司机接单后发送给收发货人
     *
     * @param tanentName 运输公司
     * @param phone 收货人手机号
     * @param plateNumber 车牌号
     * @param driverName 司机姓名
     * @param driverPhone 司机手机号
     * @return void
     * @auther wzc
     * @date 2019/1/15 17:51
     */
    public static R<Boolean> sendStartTrans(String phone, String tanentName, String plateNumber, String driverName,String driverPhone) throws IOException {
        /*设置模板id*/
        long tpl_id = CommonConstant.TPL_ID_SEND;
        //设置对应的模板变量值
        String tpl_value =
                URLEncoder.encode("#tanentName#", ENCODING) + "=" + URLEncoder.encode(tanentName, ENCODING)
                        + "&" + URLEncoder.encode("#plateNumber#", ENCODING) + "=" + URLEncoder.encode(plateNumber, ENCODING)
                        + "&" + URLEncoder.encode("#driverName#", ENCODING) + "=" + URLEncoder.encode(driverName, ENCODING)
                        + "&" + URLEncoder.encode("#phone#", ENCODING) + "=" + URLEncoder.encode(driverPhone, ENCODING)	;
        String resultCode = YunPianSMSUtils.tplSendSms(APIKEY, tpl_id, tpl_value, phone);
        JSONObject obj = JSONObject.parseObject(resultCode);
        /*获取返回消息*/
        String code = obj.getString("code");
        System.out.println(obj.toString());
        if(code=="0" || code.equals("0")){
            return new R<>(Boolean.TRUE, YunPianResultCodeEmnu.getName(Integer.valueOf(code)));
        }
        return new R<>(Boolean.FALSE, YunPianResultCodeEmnu.getName(Integer.valueOf(code)));
    }

    /**
     * 微信公众号短信绑定手机短信验证码
     */
    public static R<Boolean> sendWechatCode(String phone,String smsCode) throws IOException {
        /*设置模板id*/
        long tpl_id = 2859844;
        //设置对应的模板变量值
        String tpl_value =
                URLEncoder.encode("#code#",ENCODING)+"="+URLEncoder.encode(smsCode,ENCODING);
        String resultCode = YunPianSMSUtils.tplSendSms(APIKEY, tpl_id, tpl_value, phone);
        JSONObject obj = JSONObject.parseObject(resultCode);
        /*获取返回消息*/
        String code = obj.getString("code");
        System.out.println(obj.toString());
        if(code=="0" || code.equals("0")){
            return new R<>(Boolean.TRUE, YunPianResultCodeEmnu.getName(Integer.valueOf(code)));
        }
        return new R<>(Boolean.FALSE, YunPianResultCodeEmnu.getName(Integer.valueOf(code)));
    }

    /**
     * 取账户信息
     *
     * @return json格式字符串
     * @throws java.io.IOException
     */
    public static String getUserInfo(String apikey) throws IOException,
            URISyntaxException {
        Map < String, String > params = new HashMap < String, String > ();
        params.put("apikey", apikey);
        return post(URI_GET_USER_INFO, params);
    }

    /**
     * 智能匹配模板接口发短信
     *
     * @param apikey apikey
     * @param text   　短信内容
     * @param mobile 　接受的手机号
     * @return json格式字符串
     * @throws IOException
     */

    public static String sendSms(String apikey, String text,
                                 String mobile) throws IOException {
        Map < String, String > params = new HashMap < String, String > ();
        params.put("apikey", apikey);
        params.put("text", text);
        params.put("mobile", mobile);
        return post(URI_SEND_SMS, params);
    }

    /**
     * 通过模板发送短信(不推荐)
     *
     * @param apikey    apikey
     * @param tpl_id    　模板id
     * @param tpl_value 　模板变量值
     * @param mobile    　接受的手机号
     * @return json格式字符串
     * @throws IOException
     */

    public static String tplSendSms(String apikey, long tpl_id, String tpl_value,
                                    String mobile) throws IOException {
        Map < String, String > params = new HashMap < String, String > ();
        params.put("apikey", apikey);
        params.put("tpl_id", String.valueOf(tpl_id));
        params.put("tpl_value", tpl_value);
        params.put("mobile", mobile);
        return post(URI_TPL_SEND_SMS, params);
    }

    /**
     * 通过接口发送语音验证码
     * @param apikey apikey
     * @param mobile 接收的手机号
     * @param code   验证码
     * @return
     */

    public static String sendVoice(String apikey, String mobile, String code) {
        Map < String, String > params = new HashMap < String, String > ();
        params.put("apikey", apikey);
        params.put("mobile", mobile);
        params.put("code", code);
        return post(URI_SEND_VOICE, params);
    }

    /**
     * 通过接口绑定主被叫号码
     * @param apikey apikey
     * @param from 主叫
     * @param to   被叫
     * @param duration 有效时长，单位：秒
     * @return


    public static String bindCall(String apikey, String from, String to,
    Integer duration) {
    Map < String, String > params = new HashMap < String, String > ();
    params.put("apikey", apikey);
    params.put("from", from);
    params.put("to", to);
    params.put("duration", String.valueOf(duration));
    return post(URI_SEND_BIND, params);
    }


     * 通过接口解绑绑定主被叫号码
     * @param apikey apikey
     * @param from 主叫
     * @param to   被叫
     * @return

    public static String unbindCall(String apikey, String from, String to) {
    Map < String, String > params = new HashMap < String, String > ();
    params.put("apikey", apikey);
    params.put("from", from);
    params.put("to", to);
    return post(URI_SEND_UNBIND, params);
    }
     */

    /**
     * 基于HttpClient 4.3的通用POST方法
     *
     * @param url       提交的URL
     * @param paramsMap 提交<参数，值>Map
     * @return 提交响应
     */

    public static String post(String url, Map < String, String > paramsMap) {
        CloseableHttpClient client = HttpClients.createDefault();
        String responseText = "";
        CloseableHttpResponse response = null;
        try {
            HttpPost method = new HttpPost(url);
            if (paramsMap != null) {
                List < NameValuePair > paramList = new ArrayList <
                        NameValuePair > ();
                for (Map.Entry < String, String > param: paramsMap.entrySet()) {
                    NameValuePair pair = new BasicNameValuePair(param.getKey(),
                            param.getValue());
                    paramList.add(pair);
                }
                method.setEntity(new UrlEncodedFormEntity(paramList,
                        ENCODING));
            }
            response = client.execute(method);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                responseText = EntityUtils.toString(entity, ENCODING);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return responseText;
    }
}