package com.zhkj.lc.trunk.utils.wechat.start;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class InterfaceUrlInti {
    private final static String tokenUrl = "https://api.weixin.qq.com/cgi-bin/token";
    private final static String OpenidUserinfoUrl = "https://api.weixin.qq.com/cgi-bin/user/info";
    public synchronized static void init(){
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        Properties props = new Properties();
//        if(GlobalConstants.interfaceUrlProperties==null){
//            GlobalConstants.interfaceUrlProperties = new Properties();
//        }
        InputStream in = null;
        try {
//            in = cl.getResourceAsStream("interface_url.properties");
//            props.load(in);
//            for(Object key : props.keySet()){
//                GlobalConstants.interfaceUrlProperties.put("tokenUrl", tokenUrl);
//                GlobalConstants.interfaceUrlProperties.put("key", OpenidUserinfoUrl);
//            }

            /*props = new Properties();
            in = cl.getResourceAsStream("wechat.properties");
            props.load(in);
            for(Object key : props.keySet()){
                GlobalConstants.interfaceUrlProperties.put(key, props.get(key));
            }*/

        } finally{
            if(in!=null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return;
    }
}
