package com.zhkj.lc.common.api;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.ServiceHelper;
import cn.jiguang.common.connection.ApacheHttpClient;
import cn.jiguang.common.resp.ResponseWrapper;

import com.zhkj.lc.common.api.common.JSMSConfig;
import com.zhkj.lc.common.api.common.ResultCodeEmnu;
import com.zhkj.lc.common.api.common.SMSClient;
import com.zhkj.lc.common.api.common.model.SMSPayload;
import com.zhkj.lc.common.util.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;

import com.zhkj.lc.common.api.account.AccountBalanceResult;
import com.zhkj.lc.common.api.account.AppBalanceResult;
import com.zhkj.lc.common.api.common.model.BatchSMSPayload;
import com.zhkj.lc.common.api.common.model.BatchSMSResult;
import com.zhkj.lc.common.api.common.model.RecipientPayload;
import com.zhkj.lc.common.api.schedule.model.ScheduleResult;
import com.zhkj.lc.common.api.schedule.model.ScheduleSMSPayload;
import com.zhkj.lc.common.api.template.SendTempSMSResult;
import com.zhkj.lc.common.api.template.TempSMSResult;
import com.zhkj.lc.common.api.template.TemplatePayload;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * 功能描述: 短信发送工具
 *
 * @auther wzc
 * @date 2019/1/15 11:18
 */
public class SMSUtils {
    protected static final Logger LOG = LoggerFactory.getLogger(SMSUtils.class);
    private static final String devKey = "242780bfdd7315dc1989fedb";
    private static final String devSecret = "2f5ced2bef64167950e63d13";

    public static void main(String[] args) {
//    	  sendDriverNewOrder("1763856812", "CN20180115170002");
//        sendConsigneeReceiptCode("17638568129","25579315","CN20180115170002");
//        sendStartTrans("17638568129","豫鑫物流","豫A52d25","王志超","17638568129");
//        testSendTemplateSMS();
//        String code = "50008";
//        System.out.println(ResultCodeEmnu.getName(Integer.parseInt(code)));
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
    public static R<Boolean> sendDriverNewOrder(String phone, String orderId) {
        SMSClient client = new SMSClient(JSMSConfig.MASTER_SECRET, JSMSConfig.APP_KEY);
        String orderType = orderId.substring(0,2).equals("CN")?"普货":"集装箱";
        SMSPayload payload = SMSPayload.newBuilder()
                .setMobileNumber(phone)
                .setTempId(159567)
                .addTempPara("orderId", orderId)
                .addTempPara("orderType", orderType)
                .build();
        try {
            SendSMSResult res = client.sendTemplateSMS(payload);
            LOG.info(res.toString());
            System.out.println(res.toString());
            return new R<>(Boolean.TRUE);
        } catch (APIConnectionException e) {
            LOG.error("Connection error. Should retry later. ", e);
            return new R<>(Boolean.FALSE,"网络连接失败");
        } catch (APIRequestException e) {
            LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Message: " + e.getMessage());
            String code = e.getMessage().substring(17,22);
            return new R<>(Boolean.FALSE,ResultCodeEmnu.getName(Integer.parseInt(code)));
        }
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
    public static R<Boolean> sendConsigneeReceiptCode(String phone, String receiptCode, String orderId) {
        SMSClient client = new SMSClient(JSMSConfig.MASTER_SECRET, JSMSConfig.APP_KEY);
        SMSPayload payload = SMSPayload.newBuilder()
                .setMobileNumber(phone)
                .setTempId(159581)
                .addTempPara("orderId", orderId)
                .addTempPara("number", receiptCode)
                .build();
        try {
            SendSMSResult res = client.sendTemplateSMS(payload);
            LOG.info(res.toString());
            return new R<>(Boolean.TRUE);
        } catch (APIConnectionException e) {
            LOG.error("Connection error. Should retry later. ", e);
            return new R<>(Boolean.FALSE,"网络连接失败");
        } catch (APIRequestException e) {
            LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Message: " + e.getMessage());
            String code = e.getMessage().substring(17,22);
            return new R<>(Boolean.FALSE,ResultCodeEmnu.getName(Integer.parseInt(code)));
        }
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
    public static R<Boolean> sendStartTrans(String phone, String tanentName, String plateNumber, String driverName,String driverPhone) {
        SMSClient client = new SMSClient(JSMSConfig.MASTER_SECRET, JSMSConfig.APP_KEY);
        SMSPayload payload = SMSPayload.newBuilder()
                .setMobileNumber(phone)
                .setTempId(159566)
                .addTempPara("tanentName", tanentName)
                .addTempPara("plateNumber", plateNumber)
                .addTempPara("driverName", driverName)
                .addTempPara("phone", driverPhone)
                .build();
        try {
            SendSMSResult res = client.sendTemplateSMS(payload);
            System.out.println(res.toString());
            LOG.info(res.toString());
            return new R<>(Boolean.TRUE);
        } catch (APIConnectionException e) {
            LOG.error("Connection error. Should retry later. ", e);
            return new R<>(Boolean.FALSE,"网络连接失败");
        } catch (APIRequestException e) {
            LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Message: " + e.getMessage());
            String code = e.getMessage().substring(17,22);
            return new R<>(Boolean.FALSE,ResultCodeEmnu.getName(Integer.parseInt(code)));
        }
    }

    public static void testSendSMSWithIHttpClient() {
        SMSClient client = new SMSClient(JSMSConfig.MASTER_SECRET, JSMSConfig.APP_KEY);
        String authCode = ServiceHelper.getBasicAuthorization(JSMSConfig.APP_KEY, JSMSConfig.MASTER_SECRET);
        ApacheHttpClient httpClient = new ApacheHttpClient(authCode, null, ClientConfig.getInstance());
        // NettyHttpClient httpClient = new NettyHttpClient(authCode, null, ClientConfig.getInstance());
        // ApacheHttpClient httpClient = new ApacheHttpClient(authCode, null, ClientConfig.getInstance());
        // 可以切换 HttpClient，默认使用的是 NativeHttpClient
        client.setHttpClient(httpClient);
        // 如果使用 NettyHttpClient，发送完请求后要调用 close 方法
        // client.close();
        SMSPayload payload = SMSPayload.newBuilder()
                .setMobileNumber("13800138000")
                .setTempId(1)
                .build();
        try {
            SendSMSResult res = client.sendSMSCode(payload);
            System.out.println(res.toString());
            LOG.info(res.toString());
        } catch (APIConnectionException e) {
            LOG.error("Connection error. Should retry later. ", e);
        } catch (APIRequestException e) {
            LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Message: " + e.getMessage());
        }
    }

    public static void testSendValidSMSCode() {
        SMSClient client = new SMSClient(JSMSConfig.MASTER_SECRET, JSMSConfig.APP_KEY);
        try {
            ValidSMSResult res = client.sendValidSMSCode("01658697-45d9-4644-996d-69a1b14e2bb8", "556618");
            System.out.println(res.toString());
            LOG.info(res.toString());
        } catch (APIConnectionException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            LOG.error("Connection error. Should retry later. ", e);
        } catch (APIRequestException e) {
            e.printStackTrace();
            if (e.getErrorCode() == 50010) {
                // do something
            }
            System.out.println(e.getStatus() + " errorCode: " + e.getErrorCode() + " " + e.getErrorMessage());
            LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Message: " + e.getMessage());
        }
    }

    /**
     *  The default value of ttl is 60 seconds.
     */
    public static void testSendVoiceSMSCode() {
        SMSClient client = new SMSClient(JSMSConfig.MASTER_SECRET, JSMSConfig.APP_KEY);
        SMSPayload payload = SMSPayload.newBuilder()
                .setMobileNumber("13800138000")
                .setTTL(90)
                .build();
        try {
            SendSMSResult res = client.sendVoiceSMSCode(payload);
            LOG.info(res.toString());
        } catch (APIRequestException e) {
            LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Message: " + e.getMessage());
        } catch (APIConnectionException e) {
            LOG.error("Connection error. Should retry later. ", e);
        }
    }

    public static void testSendTemplateSMS() {
        SMSClient client = new SMSClient(JSMSConfig.MASTER_SECRET, JSMSConfig.APP_KEY);
        SMSPayload payload = SMSPayload.newBuilder()
                .setMobileNumber("13800138000")
                .setTempId(1)
                .addTempPara("test", "jpush")
                .build();
        try {
            SendSMSResult res = client.sendTemplateSMS(payload);
            LOG.info(res.toString());
        } catch (APIRequestException e) {
            LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Message: " + e.getMessage());
        } catch (APIConnectionException e) {
            LOG.error("Connection error. Should retry later. ", e);
        }
    }

    public static void testSendBatchTemplateSMS() {
        SMSClient client = new SMSClient(JSMSConfig.MASTER_SECRET, JSMSConfig.APP_KEY);
        List<RecipientPayload> list = new ArrayList<RecipientPayload>();
        RecipientPayload recipientPayload1 = new RecipientPayload.Builder()
                .setMobile("13800138000")
                .addTempPara("code", "638938")
                .build();
        RecipientPayload recipientPayload2 = new RecipientPayload.Builder()
                .setMobile("13800138000")
                .addTempPara("code", "829302")
                .build();
        list.add(recipientPayload1);
        list.add(recipientPayload2);
        RecipientPayload[] recipientPayloads = new RecipientPayload[list.size()];
        BatchSMSPayload smsPayload = BatchSMSPayload.newBuilder()
                .setTempId(1)
                .setRecipients(list.toArray(recipientPayloads))
                .build();
        try {
            BatchSMSResult result = client.sendBatchTemplateSMS(smsPayload);
            LOG.info("Got result: " + result);
        } catch (APIConnectionException e) {
            LOG.error("Connection error. Should retry later. ", e);
        } catch (APIRequestException e) {
            LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Message: " + e.getMessage());
        }
    }

    public static void testSendScheduleSMS() {
        SMSClient client = new SMSClient(JSMSConfig.MASTER_SECRET, JSMSConfig.APP_KEY);
        ScheduleSMSPayload payload = ScheduleSMSPayload.newBuilder()
                .setMobileNumber("13800138000")
                .setTempId(1111)
                .setSendTime("2017-07-31 16:17:00")
                .addTempPara("number", "798560")
                .build();
        try {
            ScheduleResult result = client.sendScheduleSMS(payload);
            LOG.info(result.toString());
        } catch (APIConnectionException e) {
            LOG.error("Connection error. Should retry later. ", e);
        } catch (APIRequestException e) {
            LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Message: " + e.getMessage());
        }
    }

    public static void testUpdateScheduleSMS() {
        SMSClient client = new SMSClient(JSMSConfig.MASTER_SECRET, JSMSConfig.APP_KEY);
        ScheduleSMSPayload payload = ScheduleSMSPayload.newBuilder()
                .setMobileNumber("13800138000")
                .setTempId(1111)
                .setSendTime("2017-07-31 15:00:00")
                .addTempPara("number", "110110")
                .build();
        try {
            ScheduleResult result = client.updateScheduleSMS(payload, "dsfd");
            LOG.info(result.toString());
        } catch (APIConnectionException e) {
            LOG.error("Connection error. Should retry later. ", e);
        } catch (APIRequestException e) {
            LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Message: " + e.getMessage());
        }
    }

    public static void testSendBatchScheduleSMS() {
        SMSClient client = new SMSClient(JSMSConfig.MASTER_SECRET, JSMSConfig.APP_KEY);
        List<RecipientPayload> list = new ArrayList<RecipientPayload>();
        RecipientPayload recipientPayload1 = new RecipientPayload.Builder()
                .setMobile("13800138000")
                .addTempPara("number", "638938")
                .build();
        RecipientPayload recipientPayload2 = new RecipientPayload.Builder()
                .setMobile("13800138001")
                .addTempPara("number", "829302")
                .build();
        list.add(recipientPayload1);
        list.add(recipientPayload2);
        RecipientPayload[] recipientPayloads = new RecipientPayload[list.size()];
        ScheduleSMSPayload smsPayload = ScheduleSMSPayload.newBuilder()
                .setSendTime("2017-07-31 16:00:00")
                .setTempId(1245)
                .setRecipients(list.toArray(recipientPayloads))
                .build();
        try {
            BatchSMSResult result = client.sendBatchScheduleSMS(smsPayload);
            LOG.info(result.toString());
        } catch (APIConnectionException e) {
            LOG.error("Connection error. Should retry later. ", e);
        } catch (APIRequestException e) {
            LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Message: " + e.getMessage());
        }
    }

    public static void testUpdateBatchScheduleSMS() {
        SMSClient client = new SMSClient(JSMSConfig.MASTER_SECRET, JSMSConfig.APP_KEY);
        List<RecipientPayload> list = new ArrayList<RecipientPayload>();
        RecipientPayload recipientPayload1 = new RecipientPayload.Builder()
                .setMobile("13800138000")
                .addTempPara("number", "328393")
                .build();
        RecipientPayload recipientPayload2 = new RecipientPayload.Builder()
                .setMobile("13800138001")
                .addTempPara("number", "489042")
                .build();
        list.add(recipientPayload1);
        list.add(recipientPayload2);
        RecipientPayload[] recipientPayloads = new RecipientPayload[list.size()];
        ScheduleSMSPayload smsPayload = ScheduleSMSPayload.newBuilder()
                .setSendTime("2017-07-31 16:00:00")
                .setTempId(1245)
                .setRecipients(list.toArray(recipientPayloads))
                .build();
        try {
            BatchSMSResult result = client.updateBatchScheduleSMS(smsPayload, "dfs");
            LOG.info(result.toString());
        } catch (APIConnectionException e) {
            LOG.error("Connection error. Should retry later. ", e);
        } catch (APIRequestException e) {
            LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Message: " + e.getMessage());
        }
    }

    public static void testDeleteScheduleSMS() {
        SMSClient client = new SMSClient(JSMSConfig.MASTER_SECRET, JSMSConfig.APP_KEY);
        try {
            ResponseWrapper result = client.deleteScheduleSMS("sd");
            LOG.info("Response content: " + result.responseContent + " response code: " + result.responseCode);
        } catch (APIConnectionException e) {
            LOG.error("Connection error. Should retry later. ", e);
        } catch (APIRequestException e) {
            LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Message: " + e.getMessage());
        }
    }

    public static void testGetAccountSMSBalance() {
        SMSClient client = new SMSClient(devSecret, devKey);
        try {
            AccountBalanceResult result = client.getSMSBalance();
            LOG.info(result.toString());
        } catch (APIConnectionException e) {
            LOG.error("Connection error. Should retry later. ", e);
        } catch (APIRequestException e) {
            LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Message: " + e.getMessage());
        }
    }

    public static void testGetAppSMSBalance() {
        SMSClient client = new SMSClient(JSMSConfig.MASTER_SECRET, JSMSConfig.APP_KEY);
        try {
            AppBalanceResult result = client.getAppSMSBalance();
            LOG.info(result.toString());
        } catch (APIConnectionException e) {
            LOG.error("Connection error. Should retry later. ", e);
        } catch (APIRequestException e) {
            LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Message: " + e.getMessage());
        }
    }


    public void testCreateTemplate() {
        try {
            SMSClient client = new SMSClient(JSMSConfig.MASTER_SECRET, JSMSConfig.APP_KEY);
            TemplatePayload payload = TemplatePayload.newBuilder()
                    .setTemplate("您好，您的验证码是{{code}}，2分钟内有效！")
                    .setType(1)
                    .setTTL(120)
                    .setRemark("验证短信")
                    .build();
            SendTempSMSResult result = client.createTemplate(payload);
            LOG.info(result.toString());
        } catch (APIConnectionException e) {
            LOG.error("Connection error. Should retry later. ", e);
        } catch (APIRequestException e) {
            LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Message: " + e.getMessage());
        }
    }

    // 只有审核不通过状态的模板才允许修改
    public void testUpdateTemplate() {
        try {
            SMSClient client = new SMSClient(JSMSConfig.MASTER_SECRET, JSMSConfig.APP_KEY);
            TemplatePayload payload = TemplatePayload.newBuilder()
                    .setTempId(12345)
                    .setTemplate("您好，您的验证码是{{code}}，2分钟内有效！")
                    .setType(1)
                    .setTTL(120)
                    .setRemark("验证短信")
                    .build();
            SendTempSMSResult result = client.updateTemplate(payload, 12345);
            LOG.info(result.toString());
        } catch (APIConnectionException e) {
            LOG.error("Connection error. Should retry later. ", e);
        } catch (APIRequestException e) {
            LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Message: " + e.getMessage());
        }
    }

    public void testCheckTemplate() {
        try {
            SMSClient client = new SMSClient(JSMSConfig.MASTER_SECRET, JSMSConfig.APP_KEY);
            TempSMSResult result = client.checkTemplate(144923);
            LOG.info(result.toString());
        } catch (APIConnectionException e) {
            LOG.error("Connection error. Should retry later. ", e);
        } catch (APIRequestException e) {
            LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Message: " + e.getMessage());
        }
    }

    public void testDeleteTemplate() {
        try {
            SMSClient client = new SMSClient(JSMSConfig.MASTER_SECRET, JSMSConfig.APP_KEY);
            ResponseWrapper result = client.deleteTemplate(144923);
            LOG.info(result.toString());
        } catch (APIConnectionException e) {
            LOG.error("Connection error. Should retry later. ", e);
        } catch (APIRequestException e) {
            LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Message: " + e.getMessage());
        }
    }
}
