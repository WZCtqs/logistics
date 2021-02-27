package com.zhkj.lc.oilcard.util;

import com.zhkj.lc.common.config.Global;
import com.zhkj.lc.oilcard.exception.FileNameLengthLimitExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.apache.tomcat.util.http.fileupload.FileUploadBase.FileSizeLimitExceededException;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 文件上传工具类
 * 
 * @author zhkj
 */
public class FileUploadUtils
{
    /**
     * 默认大小 50M
     */
    public static final long DEFAULT_MAX_SIZE = 52428800;

    /**
     * 默认上传的地址
     */
    private static String defaultBaseDir = Global.getProfile();

    /**
     * 默认的文件名最大长度
     */
    public static final int DEFAULT_FILE_NAME_LENGTH = 200;

    /**
     * 默认文件类型jpg
     */
    public static final String IMAGE_JPG_EXTENSION = ".jpg";

    private static int counter = 0;

    public static void setDefaultBaseDir(String defaultBaseDir)
    {
        FileUploadUtils.defaultBaseDir = defaultBaseDir;
    }
    public static String getDefaultBaseDir()
    {
        return defaultBaseDir;
    }

    /**
     *
     * 功能描述: 创建文件夹
     *
     * @param: uploadDir路径，fileName 文件名
     * @return:
     * @auther: HP
     * @date: 2018/11/26 9:02
     */
    private static final File getAbsoluteFile(String uploadDir, String filename) throws IOException
    {
        File desc = new File(File.separator + filename);

        if (!desc.getParentFile().exists())
        {
            desc.getParentFile().mkdirs();
        }
        if (!desc.exists())
        {
            desc.createNewFile();
        }
        return desc;
    }

    /**
     * 文件大小校验
     *
     * @param file 上传的文件
     * @return
     * @throws FileSizeLimitExceededException 如果超出最大大小
     */
    public static final void assertAllowed(MultipartFile file) throws FileSizeLimitExceededException
    {
        long size = file.getSize();
        if (DEFAULT_MAX_SIZE != -1 && size > DEFAULT_MAX_SIZE)
        {
            throw new FileSizeLimitExceededException("not allowed upload upload", size, DEFAULT_MAX_SIZE);
        }
    }

    /**
     *
     * 功能描述:
     *
     * @param: 上传文件路径，文件，文件名前缀(数据id+原始文件名)
     * @return: fileName 文件名
     * @auther: HP
     * @date: 2018/11/26 8:47
     */
    public static final String uploadFileOfOverName(String baseDir, MultipartFile file, String dateId) throws IOException {
        try {
            int fileNamelength = file.getOriginalFilename().length();
            if (fileNamelength > FileUploadUtils.DEFAULT_FILE_NAME_LENGTH) {
                throw new FileNameLengthLimitExceededException(file.getOriginalFilename(), fileNamelength,
                        FileUploadUtils.DEFAULT_FILE_NAME_LENGTH);
            }
            assertAllowed(file);
            DateFormat fmt = new SimpleDateFormat("yyyyMMdd");
            String fileName = file.getOriginalFilename()+"_"+dateId+"_"+fmt.format(new Date());
            File desc = getAbsoluteFile(baseDir, baseDir+fileName);
            file.transferTo(desc);
            return fileName;
        }
        catch (Exception e) {
            throw new IOException(e);
        }
    }
}
