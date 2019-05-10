package cn.cps.util;

import cn.cps.core.ResultGenerator;
import cn.cps.service.UserService;
import cn.cps.service.UsersService;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: _Cps
 * Date: 2019.05.10 10:06
 */
@Log4j
public class FileUtil {

    //单文件上传
    public static Object upLoadFile(String dirPath, HttpServletRequest request) {
        try {
            List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
            if (files.size() < 1) {
                return ResultGenerator.genFailResult("请选择文件!");
            }
            MultipartFile upLoadFile = files.get(0);
            if (upLoadFile.isEmpty()) {
                return ResultGenerator.genFailResult("文件为空");
            }
            if (upLoadFile.getSize() > (3 * 1024 * 1024)) {//如果大于3M的话不上传
                return ResultGenerator.genFailResult("文件大小超过3M");
            }
            // 获取文件名带后缀
            String fileName = upLoadFile.getOriginalFilename();
            // 获取文件名
            String prefixName = fileName.substring(0, fileName.lastIndexOf("."));
            // 获取后缀名
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            // 文件路径
            String realPath = dirPath + File.separator + prefixName + "_" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + suffixName;

            log.info(dirPath);
            log.info("文件大小:" + upLoadFile.getSize() / 1024 + "KB");

            File dest = new File(realPath);
            // 检测是否存在目录
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();// 新建文件夹
            }
            upLoadFile.transferTo(dest);// 文件写入
            return ResultGenerator.genSuccessResult("上传成功!");
        } catch (Exception e) {
            return ResultGenerator.genFailResult("出现异常:" + e);
        }
    }

    //多文件上传
    public static Object upLoadFiles(String dirPath, HttpServletRequest request) {
        try {
            List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
            if (files.size() < 1) {
                return ResultGenerator.genFailResult("请选择文件!");
            }
            //循环上传文件
            for (int i = 0; i < files.size(); i++) {
                MultipartFile upLoadFile = files.get(i);
                if (upLoadFile.isEmpty()) {
                    return ResultGenerator.genFailResult("第" + (i + 1) + "个文件为空");
                }
                if (upLoadFile.getSize() > (3 * 1024 * 1024)) {//如果大于3M的话不上传
                    return ResultGenerator.genFailResult("第" + (i + 1) + "个文件大小超过3M");
                }
                // 获取文件名带后缀
                String fileName = upLoadFile.getOriginalFilename();
                // 获取文件名
                String prefixName = fileName.substring(0, fileName.lastIndexOf("."));
                // 获取后缀名
                String suffixName = fileName.substring(fileName.lastIndexOf("."));
                // 文件路径
                String realPath = dirPath + File.separator + prefixName + "_" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + suffixName;

                log.info(dirPath);
                log.info("文件大小:" + upLoadFile.getSize() / 1024 + "KB");

                File dest = new File(realPath);
                // 检测是否存在目录
                if (!dest.getParentFile().exists()) {
                    dest.getParentFile().mkdirs();// 新建文件夹
                }
                upLoadFile.transferTo(dest);// 文件写入
                log.info("第" + (i + 1) + "个文件上传成功!");
            }
            return ResultGenerator.genSuccessResult("上传成功!");
        } catch (Exception e) {
            return ResultGenerator.genFailResult("出现异常:" + e);
        }
    }

    //下载文件
    public static Object downLoadFile(String fileName, String dirPath, HttpServletRequest request, HttpServletResponse response) {
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        try {
            // 设置文件路径
            String realPath = dirPath + File.separator + fileName;
            File file = new File(realPath);
            if (file.exists()) {
                //响应到客户端
                try {
                    setResponseHeader(response, fileName);
                    byte[] buffer = new byte[1024];
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                    os.flush();
                    os.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return ResultGenerator.genSuccessResult("下载成功!");
            }
            return ResultGenerator.genSuccessResult("文件不存在，下载失败!");
        } catch (Exception e) {
            return ResultGenerator.genFailResult("出现异常:" + e);
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    //删除文件
    public static boolean removeFile(String fileRealUrl) {
        boolean result = false;
        File file = null;
        if (!StringUtils.isEmpty(fileRealUrl)) {
            file = new File(fileRealUrl);
        }
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (!file.delete()) {
                return result;
            }
            result = true;
        }
        return result;
    }

    //发送响应流方法
    public static void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            try {
                fileName = new String(fileName.getBytes("GBK"), "ISO8859-1");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}