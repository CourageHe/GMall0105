package com.couragehe.gmall.manage;


import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GmallManageWebApplicationTests {

    @Test
     public static void contextLoads() throws Exception {
        String imgUrl = "192.168.10.100";
        //配置fdfs全局连接地址
        //最好不要起奇怪的文件名
        String tracker = GmallManageWebApplicationTests.class.getResource("/tracker.conf").getPath();//获取配置文件路径
        ClientGlobal.init(tracker);
        TrackerClient trackerClient = new TrackerClient();

        //获得一个trackerServer实例
        TrackerServer trackerServer = trackerClient.getConnection();

        //通过tracker获得一个Storage连接客户端
        StorageClient storageClient = new StorageClient(trackerServer,null);
        String[]uploadInfos = storageClient.upload_file("F:/123.png","png",null);
        for(String uploadInfo : uploadInfos){

            imgUrl+="/"+uploadInfo;
        }
        System.out.println(imgUrl);
    }
    public static void main(String[] args) throws Exception {
        GmallManageWebApplicationTests.contextLoads();
    }


}
