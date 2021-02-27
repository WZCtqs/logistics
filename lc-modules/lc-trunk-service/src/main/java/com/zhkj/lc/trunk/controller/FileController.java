package com.zhkj.lc.trunk.controller;

import com.luhuiguo.fastdfs.domain.StorePath;
import com.luhuiguo.fastdfs.service.FastFileStorageClient;
import com.xiaoleilu.hutool.io.FileUtil;
import com.zhkj.lc.common.bean.config.FdfsPropertiesConfig;
import com.zhkj.lc.common.config.Global;
import com.zhkj.lc.common.util.PDFUtils;
import com.zhkj.lc.common.util.R;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/truckFile")
public class FileController {
    private Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FastFileStorageClient fastFileStorageClient;
    @Autowired
    private FdfsPropertiesConfig fdfsPropertiesConfig;

    @Value("${uploadfile.ippath}")
    public String url;

    @ApiOperation(value = "上传退卡图片", notes = "单张")
    @PostMapping("/upload")
    public R<String> upload(@RequestParam("file")MultipartFile file) {
        if(file != null) {
            System.out.println(file.getOriginalFilename());
            String fileExt = FileUtil.extName(file.getOriginalFilename());
            try {
                StorePath storePath = fastFileStorageClient.uploadFile(file.getBytes(), fileExt);
                return new R<>(fdfsPropertiesConfig.getFileHost() + storePath.getFullPath());
            } catch (IOException e) {
                logger.error("文件上传异常", e);
                throw new RuntimeException(e);
            }
        }else {
            return new R<>("error");
        }
    }

    @PostMapping("/localUpload")
    public R<String> localUpload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return null;
        }
        try {
            PDFUtils.createDirectory(Global.FILE_PATH_TRUCK);
            String filename = UUID.randomUUID().toString().replace("-","") + PDFUtils.getSuffix(file.getOriginalFilename());
            byte[] bytes = file.getBytes();
            Path path = Paths.get(Global.FILE_PATH_TRUCK + filename);
            Files.write(path, bytes);
            return new R<>(url +filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    // 查看本地图片
    @GetMapping("/viewFile")
    public void getImage(
            @RequestParam("imageName") String imageName,
            HttpServletResponse response) {
        try {
            response.setContentType("image/jpg");
            StreamUtils.copy(new FileInputStream(Global.FILE_PATH_TRUCK + imageName), response.getOutputStream());
        } catch (IOException e) {
            logger.error("本地图片查看失败：" + e.getMessage());
        }
    }
}
