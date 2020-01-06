package com.couragehe.gmall.manage.util;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class PmsUploadUtils {
    public static String uploadImage(MultipartFile multipartFile) {
        String imgUrl = "http://192.168.10.100";
        //上传图片服务器

        //配置fdfs全局连接地址
        String tracker = PmsUploadUtils.class.getResource("/tracker.conf").getPath();
        try {
            ClientGlobal.init(tracker);
        } catch (Exception e) {
            e.printStackTrace();
        }
        TrackerClient trackerClient = new TrackerClient();

        //获得一个trackerServer实例
        TrackerServer trackerServer = null;
        try {
            trackerServer = trackerClient.getConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //通过tracker获得一个Storage连接客户端
        StorageClient storageClient = new StorageClient(trackerServer, null);

        try {
            //获得上传的二进制对象
            byte[] bytes = multipartFile.getBytes();

            //获得文件后缀名
            String orginalFilename = multipartFile.getOriginalFilename();
            String extName = orginalFilename.substring(orginalFilename.lastIndexOf(".") + 1);

            //组合服务器返回的图片url地址
            String[] uploadInfos = storageClient.upload_file(bytes, extName, null);
            for (String uploadInfo : uploadInfos) {
                imgUrl += "/" + uploadInfo;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return imgUrl;
    }
}
