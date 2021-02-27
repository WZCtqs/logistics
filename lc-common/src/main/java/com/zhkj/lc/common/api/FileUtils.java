package com.zhkj.lc.common.api;

import com.zhkj.lc.common.config.FastDFSClient;
import com.zhkj.lc.common.config.FastDFSFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * fastdfs文件处理工具类
 */
public class FileUtils {

    private static Logger logger = LoggerFactory.getLogger(FileUtils.class);

    /**
     * 上传多个文件，（小程序端使用）
     * @param multipartFile
     * @return
     * @throws IOException
     */
    public static String[] saveFiles(MultipartFile[] multipartFile) throws IOException {
        String[] paths = new String[multipartFile.length];
        try {
            if (multipartFile.length > 0) {
                for (int i = 0; i < multipartFile.length; i++) {
                    String[] fileAbsolutePath = {};
                    String fileName = multipartFile[i].getOriginalFilename();
                    String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
                    byte[] file_buff = null;
                    InputStream inputStream = multipartFile[i].getInputStream();
                    if (inputStream != null) {
                        int len1 = inputStream.available();
                        file_buff = new byte[len1];
                        inputStream.read(file_buff);
                    }
                    inputStream.close();
                    FastDFSFile file = new FastDFSFile(fileName, file_buff, ext);
                    fileAbsolutePath = FastDFSClient.upload(file);  //upload to fastdfs

                    if (fileAbsolutePath == null) {
                        logger.error("上传失败，请重新上传!");
                    }
                    String path = FastDFSClient.getTrackerUrl() + fileAbsolutePath[0] + "/" + fileAbsolutePath[1];
                    paths[i] = path;
                }
            }
        } catch (Exception e) {
            logger.info("上传失败！", e);
            return null;
        }

        return paths;

    }

    /**
     * 上传单个图片（pc端使用）
     * @param multipartFile
     * @return
     * @throws IOException
     */
    public static String saveFile(MultipartFile multipartFile) throws IOException {


        String[] fileAbsolutePath = {};
        String fileName = multipartFile.getOriginalFilename();
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
        byte[] file_buff = null;
        InputStream inputStream = multipartFile.getInputStream();
        if (inputStream != null) {
            int len1 = inputStream.available();
            file_buff = new byte[len1];
            inputStream.read(file_buff);
        }
        inputStream.close();
        FastDFSFile file = new FastDFSFile(fileName, file_buff, ext);
        fileAbsolutePath = FastDFSClient.upload(file);  //upload to fastdfs

        if (fileAbsolutePath == null) {
            logger.error("上传失败，请重新上传!");
        }
        String path = FastDFSClient.getTrackerUrl() + fileAbsolutePath[0] + "/" + fileAbsolutePath[1];

        return path;


    }

    public static void download(String url, HttpServletRequest request, HttpServletResponse response){
        //后缀名
        String extName =url.substring(url.indexOf(".")+1);

    }
}
