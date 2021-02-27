package com.zhkj.lc.common.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.zhkj.lc.common.config.Global;
import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.Map;
import java.util.UUID;

/**
 * @Auther: wzc
 * @Date: 2019/1/17 08:58
 * @Description:
 */
public class PDFUtils {

    /***
     * PDF文件转PNG图片，全部页数
     *
     * @param PdfFilePath pdf完整路径
     * @param dstImgFolder 图片存放的文件夹
     * @return
     */
    public static boolean pdf2Image(String PdfFilePath, String dstImgFolder) {
        File file = new File(PdfFilePath);
        PDDocument pdDocument;
        createDirectory(Global.IMGPATH);
        try {
            String imgPDFPath = file.getParent();
            int dot = file.getName().lastIndexOf('.');
            String imagePDFName = file.getName().substring(0, dot); // 获取图片文件名
            String imgFolderPath = null;
            if (dstImgFolder.equals("")) {
                imgFolderPath = imgPDFPath + File.separator ;// 获取图片存放的文件夹路径
            } else {
                imgFolderPath = dstImgFolder + File.separator ;
            }
            pdDocument = PDDocument.load(file);
            PDFRenderer renderer = new PDFRenderer(pdDocument);
            /* dpi越大转换后越清晰，相对转换速度越慢 */
            StringBuffer imgFilePath = null;
            String imgFilePathPrefix = imgFolderPath + File.separator + imagePDFName;
            imgFilePath = new StringBuffer();
            imgFilePath.append(imgFilePathPrefix);
            imgFilePath.append(".jpg");
            File dstFile = new File(imgFilePath.toString());
            BufferedImage image = renderer.renderImageWithDPI(0, 300);
            ImageIO.write(image, "jpg", dstFile);
            System.out.println("PDF文档转jpg图片成功！");
            pdDocument.close();
            image.flush();
//            renderer = new PDFRenderer(null);
            file.delete();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean createDirectory(String folder) {
        File dir = new File(folder);
        if (dir.exists()) {
            return true;
        } else {
            return dir.mkdirs();
        }
    }

    public static boolean deleteDirectory(String folder) {
        File dir = new File(folder);
        if (dir.exists()) {
            return dir.delete();
        } else {
            return true;
        }
    }
    /*
     * 获取文件后缀（.pdf/.jpg/.png）
     */
    public static String getSuffix(String fileName){
        return fileName.substring(fileName.lastIndexOf("."));
    }
    /************************************************************************************/
    /**
     *
     * 功能描述: 利用模板生成pdf
     *
     * @param templatePath	模板地址
     * @param reader
     * @param newPDFPath	生成文件地址
     * @param map	数据参数：文本表单数据("dateMap",map)，图片数据("dateMap",map)
     * @return void
     * @auther wzc
     * @date 2019/1/17 18:03
     */
    /************************************************************************************/
    // 利用模板生成pdf
    public static void pdfout(PdfReader reader,String newPDFPath, Map<String,Object> map) {
        File file = new File("C:\\pdf\\sendTruckpdf");
        if(!file.exists()){
            file.mkdirs();
        }
//      PdfReader reader;
        FileOutputStream out;
        ByteArrayOutputStream bos;
        PdfStamper stamper;
        try {
            // 输出流
            out = new FileOutputStream(newPDFPath);
            // 读取pdf模板
//            reader = new PdfReader(templatePath);
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            AcroFields form = stamper.getAcroFields();
            //遍历数据
            Map<String,Object> datemap = (Map<String,Object>)map.get("datemap");
            if(datemap!=null){
                for(String key : datemap.keySet()){
                    String value = datemap.get(key)!=null?datemap.get(key).toString():"";
                    form.setField(key,value);
                }
            }
            //图片类的内容处理
            Map<String,String> imgmap = (Map<String,String>)map.get("imgmap");
            if(imgmap!=null){
                for(String key : imgmap.keySet()) {
                    String value = imgmap.get(key);
                    String imgpath = value;
                    int pageNo = form.getFieldPositions(key).get(0).page;
                    Rectangle signRect = form.getFieldPositions(key).get(0).position;
                    float x = signRect.getLeft();
                    float y = signRect.getBottom();
                    //根据路径读取图片
                    Image image = Image.getInstance(imgpath);
                    //获取图片页面
                    PdfContentByte under = stamper.getOverContent(pageNo);
                    //图片大小自适应
                    image.scaleToFit(signRect.getWidth(), signRect.getHeight());
                    //添加图片
                    image.setAbsolutePosition(x, y);
                    under.addImage(image);
                }
            }
            // 如果为false，生成的PDF文件可以编辑，如果为true，生成的PDF文件不可以编辑
            stamper.setFormFlattening(true);
            stamper.close();
            Document doc = new Document();
            PdfCopy copy = new PdfCopy(doc, out);
            doc.open();
            PdfImportedPage importPage = copy.getImportedPage(new PdfReader(bos.toByteArray()), 1);
            copy.addPage(importPage);
            doc.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * 功能描述: 根据上传的PDF和公章图片进行公章加盖
     *
      * @param templatePath	源PDF文件地址
     * @param newPDFPath	新PDF存放地址
     * @param signImgPath	公章png地址
     * @return boolean
     * @auther wzc
     * @date 2019/1/18 11:43
     */
    public static boolean createOficeSign(String templatePath,String newPDFPath,String signImgPath){
        try {
            File file = new File(newPDFPath);
            PdfReader reader = new PdfReader(templatePath);// 创建“tempWatermark.pdf”的PdfReader对象
            PdfStamper stamp = new PdfStamper(reader, new FileOutputStream(
                    newPDFPath));// 创建PdfStamper对象
            Image img = Image.getInstance(signImgPath);// 创建图像对象
            img.setAbsolutePosition(350, 50);// 定位图片对象
            img.setAlignment(Image.MIDDLE);// 居中显示图片
            img.scaleAbsolute(180, 180);// 设置图片新的宽度和高度
            PdfContentByte under = stamp.getUnderContent(1);// 获得第一页的内容
            under.addImage(img);// 添加图片,完成水印功能
            stamp.close();// PdfStamper对象，将从“tempWatermark.pdf”中读取的文档添加水印后写入“添加水印.pdf”
            reader.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (DocumentException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String downloadFromUrl(String url,String dir) {
        String fileName = null;
        try {
            URL httpurl = new URL(url);
            fileName = getFileNameFromUrl(url);
            System.out.println(fileName);
            createDirectory(dir);
            File f = new File(dir + fileName);
            FileUtils.copyURLToFile(httpurl, f);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return fileName;
    }

    public static String getFileNameFromUrl(String url){
        String name = new Long(System.currentTimeMillis()).toString() + ".X";
        int index = url.lastIndexOf("/");
        if(index > 0){
            name = url.substring(index + 1);
            if(name.trim().length()>0){
                return name;
            }
        }
        return name;
    }

    public static boolean copyFile(String oldFilePath, String newFilePath, String newFileName){
        createDirectory(newFilePath);
        File fromFile = new File(oldFilePath);
        File toFile = new File(newFilePath+newFileName);
        InputStream is = null;
        OutputStream os = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            is = new FileInputStream(fromFile);
            os = new FileOutputStream(toFile);
            // 创建缓冲流
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(os);
            byte[]buffer = new byte[8192];
            int count = bis.read(buffer);
            while(count != -1){
                //使用缓冲流写数据
                bos.write(buffer,0,count);
                //刷新
                bos.flush();
                count = bis.read(buffer);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public static void main(String[] args) {
        createOficeSign("C:\\Users\\HP\\Desktop\\pdf\\sendTruckList_return.pdf",Global.GZTXDPATH+"aaa.pdf","C:\\Users\\HP\\Desktop\\timg.png");
    }
}
