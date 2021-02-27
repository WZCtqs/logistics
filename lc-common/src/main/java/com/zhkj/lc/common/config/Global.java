package com.zhkj.lc.common.config;

import com.zhkj.lc.common.util.StringUtils;
import com.zhkj.lc.common.util.YamlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局配置类
 * 
 * @author zhkj
 */
public class Global
{
    private static final Logger log = LoggerFactory.getLogger(Global.class);

    private static String NAME = "application.yml";

    /*派车单本地保存路径*/
    public static final String PDFPATH = "C:\\logistics\\sendTruckpdf\\pdf\\";

    /*派车单转图片保存路径*/
    public static final String IMGPATH = "C:\\logistics\\sendTruckpdf\\img\\";

    /*提箱单保存路径*/
    public static final String TXDPATH = "C:\\logistics\\txd\\txd\\";

    /*公章保存路径*/
    public static final String GZPATH = "C:\\logistics\\txd\\gz\\";

    /*公章保存路径*/
    public static final String GZTXDPATH = "C:\\logistics\\txd\\gztxd\\";

    public static final String LINSHI = "C:\\logistics\\linshi\\gztxd\\";

    /**
     * 系统模块文件
     */
    public static final String FILE_PATH_SYSTEM = "D:\\LOGISTICS\\SYSTEM\\";
    /**
     * 订单模块文件
     */
    public static final String FILE_PATH_ORDER = "D:\\LOGISTICS\\ORDER\\";
    /**
     * 车辆模块文件
     */
    public static final String FILE_PATH_TRUCK = "D:\\LOGISTICS\\TRUCK\\";
    /**
     * 油卡模块文件
     */
    public static final String FILE_PATH_OILCARD = "D:\\LOGISTICS\\OILCARD\\";


    /**
     * 当前对象实例
     */
    private static Global global = null;

    /**
     * 保存全局属性值
     */
    private static Map<String, String> map = new HashMap<String, String>();

    private Global()
    {
    }

    /**
     * 静态工厂方法 获取当前对象实例 多线程安全单例模式(使用双重同步锁)
     */

    public static synchronized Global getInstance()
    {
        if (global == null)
        {
            synchronized (Global.class)
            {
                if (global == null)
                    global = new Global();
            }
        }
        return global;
    }

    /**
     * 获取配置
     */
    public static String getConfig(String key)
    {
        String value = map.get(key);
        if (value == null)
        {
            Map<?, ?> yamlMap = null;
            try
            {
                yamlMap = YamlUtil.loadYaml(NAME);
                value = String.valueOf(YamlUtil.getProperty(yamlMap, key));
                map.put(key, value != null ? value : StringUtils.EMPTY);
            }
            catch (FileNotFoundException e)
            {
                log.error("获取全局配置异常 {}", key);
            }
        }
        return value;
    }

    /**
     * 获取项目名称
     */
    public static String getName()
    {
        return StringUtils.nvl(getConfig("zhkj.name"), "zhkj");
    }

    /**
     * 获取项目版本
     */
    public static String getVersion()
    {
        return StringUtils.nvl(getConfig("zhkj.version"), "3.0.0");
    }

    /**
     * 获取版权年份
     */
    public static String getCopyrightYear()
    {
        return StringUtils.nvl(getConfig("zhkj.copyrightYear"), "2018");
    }

    /**
     * 获取ip地址开关
     */
    public static Boolean isAddressEnabled()
    {
        return Boolean.valueOf(getConfig("zhkj.addressEnabled"));
    }

    /**
     * 获取文件上传路径
     */
    public static String getProfile()
    {
        return getConfig("zhkj.profile");
    }

    /**
     * 获取头像上传路径
     */
    public static String getAvatarPath()
    {
        return getConfig("zhkj.profile") + "avatar/";
    }

    /**
     * 获取车辆相关文件上传路径
     */
    public static String getTrunkFilePath(){
        return getConfig("zhkj.profile")+ "trunk/";
    }
   /**
     * 获取司机相关文件上传路径
     */
    public static String getDriveFilePath(){
        return getConfig("zhkj.profile")+ "drive/";
    }
   /**
     * 获取订单相关文件上传路径
     */
    public static String getOrderFilePath(){
        return getConfig("zhkj.profile")+ "order/";
    }
    /**
     * 获取订单去程派车单路径
     */
    public static String getSendTruckListPdfPath(){
        return getConfig("zhkj.profile")+ "SendTruckList/";
    }
    /**
     * 获取合同相关文件上传路径
     */
    public static String getContractFilePath(){
        return getConfig("zhkj.profile")+ "contract/";
    }
    /**
     * 获取油卡相关文件上传路径
     */
    public static String getOilCardFilePath(){
        return getConfig("zhkj.profile")+ "oilcard/";
    }
    /**
     * 获取财务相关文件上传路径
     */
    public static String getFinanceFilePath(){
        return getConfig("zhkj.profile")+ "finance/";
    }

    /**
     * 获取下载上传路径
     */
    public static String getDownloadPath()
    {
        return getConfig("zhkj.profile") + "download/";
    }

    /**
     * 获取作者
     */
    public static String getAuthor()
    {
        return StringUtils.nvl(getConfig("gen.author"), "zhkj");
    }

    /**
     * 生成包路径
     */
    public static String getPackageName()
    {
        return StringUtils.nvl(getConfig("gen.packageName"), "com.zhkj.project.module");
    }

    /**
     * 是否自动去除表前缀
     */
    public static String getAutoRemovePre()
    {
        return StringUtils.nvl(getConfig("gen.autoRemovePre"), "true");
    }

    /**
     * 表前缀(类名不会包含表前缀)
     */
    public static String getTablePrefix()
    {
        return StringUtils.nvl(getConfig("gen.tablePrefix"), "sys_");
    }
}
