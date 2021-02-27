package com.zhkj.lc.order.controller;

import com.google.gson.Gson;
import com.zhkj.lc.common.api.YunPianSMSUtils;
import com.zhkj.lc.common.config.Global;
import com.zhkj.lc.common.util.PDFUtils;
import com.zhkj.lc.common.util.R;
import com.zhkj.lc.common.util.StringUtils;
import com.zhkj.lc.common.util.http.HttpClientUtil;
import com.zhkj.lc.common.vo.ContractVO;
import com.zhkj.lc.common.vo.GPSVO;
import com.zhkj.lc.order.dto.CityAddress;
import com.zhkj.lc.order.model.entity.OrdPickupArrivalAdd;
import com.zhkj.lc.order.service.IOrdPickupArrivalAdd;
import lombok.Data;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.concurrent.CountDownLatch;

import static org.springframework.beans.BeanUtils.copyProperties;

/**
 * pdf工具类
 * @author MOSHUNWEI
 * @since 2018-02-01
 */
public class Test {
    private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();

    @Autowired
    IOrdPickupArrivalAdd arrivalAdd;
    /*public static void main(String[] args) {
        //文本内容map
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("name","水电费和湖北省导入一条是的复合弓是大法官杀的人");
        map.put("id","是电饭锅水电费");
        map.put("age","山东分公司");
        map.put("sex","市分公司电饭锅");
        //图片
        Map<String,Object> imgmap = new HashMap<String, Object>();
        imgmap.put("img","D:\\pdf\\img.png");
        //组装map传过去
        Map<String,Object> o=new HashMap<String, Object>();
        o.put("datemap",map);
        o.put("imgmap",imgmap);

        // 模板路径
        String templatePath = "D:\\pdf\\未标题.pdf";
        // 生成的新文件路径
        String newPDFPath = "D:\\pdf\\卡卡卡卡卡卡卡.pdf";
        //执行
        pdfout(templatePath,newPDFPath,o);
    }*/

    public static void date(){
        Calendar calendar = Calendar.getInstance();
        System.out.println("YEAR--" + calendar.get(Calendar.YEAR));
        System.out.println("MONTH--" + calendar.get(Calendar.MONTH));
        System.out.println("DATE--" + calendar.get(Calendar.DATE));
        System.out.println("DAY_OF_MONTH--" + calendar.get(Calendar.DAY_OF_MONTH));
        System.out.println("HOUR--" + calendar.get(Calendar.HOUR));
        System.out.println("HOUR_OF_DAY--" + calendar.get(Calendar.HOUR_OF_DAY));
        System.out.println("MINUTE--" + calendar.get(Calendar.MINUTE));
        System.out.println("SECOND--" + calendar.get(Calendar.SECOND));
        //如何取得从1970 年1 月1 日0 时0 分0 秒到现在的毫秒数？(2017-11-19-wl)
        System.out.println(Calendar.getInstance().getTimeInMillis());
        System.out.println(System.currentTimeMillis());
        System.out.println(Clock.systemDefaultZone().millis());

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        /*如何取得某月的最后一天？*/
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);//amount 0:本月，1：下月，-1：上月
        c.set(Calendar.DAY_OF_MONTH, 1); // 0：上句月份 往前（减）1天，1：上句月份第一天，2：第二天。
        System.out.println(format.format(c.getTime()));

        Calendar ca = Calendar.getInstance();

        /*当月天数*/
        System.out.println("Calendar.DAY_OF_MONTH" + ca.getActualMaximum(Calendar.DAY_OF_MONTH));

        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        System.out.println(format.format(ca.getTime()));

        /*Java 8*/
        LocalDate date = LocalDate.now();
        /*当月第一天*/
        LocalDate firstday = LocalDate.of(date.getYear(), date.getMonth(), 1);
        System.out.println("firstday  -- " + firstday);
        LocalDate lastday = date.with(TemporalAdjusters.lastDayOfMonth());
        System.out.println("lastday -- " + lastday);

        short a = 1;
        // a = a + 1; (错误)

        a = (short) (a + 1);
        /*上下两句等同*/
        a += 1;
        HashSet s = new HashSet();
        s.add(new Object());

    }

    public static List<CityAddress.ResultDetail>cityLimit(String region, String query){
        if(StringUtils.isEmpty(query))return null;
        String ak = "XoWiKlCuvwBwX1GFIo84zF4hQW3P88l8";
        String resultJson = HttpClientUtil
                .doGet("http://api.map.baidu.com/place/v2/suggestion?query="+query+"&region="+region+"&city_limit=true&output=json&ak="+ak);
//        System.out.println(resultJson);
        //GSON直接解析成对象
        CityAddress resultBean = new Gson().fromJson(resultJson,CityAddress.class);
        //对象中拿到集合
        List<CityAddress.ResultDetail> BeanList = resultBean.getResult();
        for(CityAddress.ResultDetail detail : BeanList){
            detail.setName(detail.getProvince()+detail.getCity()+detail.getDistrict()+detail.getName());
        }
        return BeanList;
    }

    public static void testaaa(String[] args) {
        ArrayList<String> list = new ArrayList<String>();
        list.add("one");
        list.add("one");
        list.add("one");
        list.add("two");
        list.add("two");
        list.add("two");
        list.add("three");
        list.add("three");
        list.add("three");
        list.add("three");
        String[]a = {"one","two","three"};
        int x = 0, y = 0, z = 0;
        for(int i = 0; i<a.length; i++){
            Iterator<String> iter = list.iterator();
            while(iter.hasNext()){
                String s = iter.next();
                if(s.equals(a[i])){
                    if(i==0) x++;
                    if(i==1) y++;
                    if(i==2) z++;
                    iter.remove();
                }
            }
        }
        System.out.println(x+"-"+y+"-"+z);
        BigDecimal receivable = new BigDecimal(123);//应收
        BigDecimal re = new BigDecimal(123);//应收
        receivable = receivable.add(re);
        System.out.println(receivable);
    }

    public static void deleteDirectory(String path) {
        try {
            File file = new File(path);
            if (file.delete()) {
                System.out.println("111");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        String a = null;
        System.out.println(a.equals("1"));

    }

    public static void test() {

        List<Object> list = new Vector<>();
        int threadCount = 1000;


        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        for(int i=0; i< threadCount; i++){
            Thread thread = new Thread(new MyThread(list, countDownLatch));
            thread.start();
        }

        try{

            countDownLatch.await();
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println(list.size());
    }

    static class MyThread implements Runnable{

        private List<Object> list;
        private CountDownLatch countDownLatch;

        public MyThread(List<Object> list, CountDownLatch countDownLatch){
            this.countDownLatch = countDownLatch;
            this.list = list;
        }

        @Override
        public void run() {
            for(int i=0;i<100;i++){
                list.add(new Object());
            }
            countDownLatch.countDown();
        }
    }










}
