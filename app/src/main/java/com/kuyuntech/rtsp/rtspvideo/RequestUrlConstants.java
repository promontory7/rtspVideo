package com.kuyuntech.rtsp.rtspvideo;

/**
 * Created by hehe on 2017/6/6.
 */

public class RequestUrlConstants {
//    public static String URL_SERVER = "http://promontory7.cn:8088/VideoProject-0.1/";
    public static String URL_SERVER = "http://192.168.4.11:18080/VideoProject-0.1/";


    //登录
    public static String URL_LOGIN = URL_SERVER + "user/login";

    //退出登录
    public static String URL_LOGOUT = URL_SERVER + "user/logout";


    //获取监控视频列表
    public static String URL_LIST_ITEM = URL_SERVER + "video/videoList";
}
