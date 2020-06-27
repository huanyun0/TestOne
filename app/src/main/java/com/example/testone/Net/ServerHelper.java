package com.example.testone.Net;

public class ServerHelper {

    private static final String HOST = "123.57.108.23";

    private static final String PORT = "8080";

    private static final String HTTP_METHOD = "http://";

    private static final String SERVER_URL = HTTP_METHOD + HOST + ":" + PORT;

    private static final String URL_LOGIN = SERVER_URL + "/AndroidService/SignIn?name=%s&password=%s";

    private static final String URL_REGISTER = SERVER_URL + "/AndroidService/SignUp?name=%s&password=%s";

    private static final String URL_SIMPLEQUESTION = SERVER_URL + "/AndroidService/GetSimpleQuestion?id=%s&type=%s";

    private static final String URL_SEND_IMAGE = SERVER_URL + "/AndroidService/SendImage";

//
    private static final String URL_QUESTION = SERVER_URL + "/AndroidService/Question";

    private static final String URL_SET_EMAIL = SERVER_URL + "/user/email?phone=%s&email=%s";

    private static final String URL_SEND_CODE = SERVER_URL + "/user/code?phone=%s";

    private static final String URL_SET_PWD = SERVER_URL + "/user/pwd?phone=%s&pwd=%s";

    private static final String URL_SET_NAME = SERVER_URL + "/user/name?phone=%s&name=%s";



    public static String getUrlLogin(String... args) {
        return String.format(URL_LOGIN, (Object[]) args);
    }

    public static String getUrlRegister(String ... args) {
        return String.format(URL_REGISTER, (Object[]) args);
    }

    public static String getUrlSimpleQuestion(int id,int type) {
        return String.format(URL_SIMPLEQUESTION, id, type);
    }

    public static String getUrlSendImage(){
        return URL_SEND_IMAGE;
    }


  //
    public static String getUrlQuestion() {
        return URL_QUESTION;
    }

    public static String getUrlSetEmail(String... args) {
        return String.format(URL_SET_EMAIL, (Object[]) args);
    }

    public static String getUrlSendCode(String phone) {
        return String.format(URL_SEND_CODE, phone);
    }

    public static String getUrlSetPwd(String phone, String pwd) {
        return String.format(URL_SET_PWD, phone, pwd);
    }

    public static String getUrlSetName(String phone, String name) {
        return String.format(URL_SET_NAME, phone, name);
    }

}