package com.hd.message.util;

import com.hd.message.dto.OKHttpResponseDTO;
import okhttp3.*;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class HttpClientUtil {

    /**
     * 利用OKHttp Client做客户端发送Get请求
     *
     * @param url
     * @return
     */
    public static ResponseEntity<Object> doGetRequest(String url) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().get().url(url).build();
        Call call = client.newCall(request);
        Response response = null;
        //同步调用,返回Response,会抛出IO异常
        try {
            response = call.execute();
            return new ResponseEntity(response.body().string(), HttpStatus.valueOf(response.code()));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 利用OKHttp Client做客户端发送POST请求
     *
     * @param url
     * @param param
     * @return
     */
    public static ResponseEntity<Object> doPostRequest(String url, String param) {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), param);
        Request request = new Request.Builder().post(requestBody).url(url).build();
        Call call = client.newCall(request);
        Response response = null;
        //同步调用,返回Response,会抛出IO异常
        try {
            response = call.execute();
            if (response.body().contentLength() != 0) {
                return new ResponseEntity(response.body().string(), HttpStatus.valueOf(response.code()));
            } else {
                return new ResponseEntity(response.body(), HttpStatus.valueOf(response.code()));
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 利用HttpClient做客户端发送Post请求
     *
     * @param url
     * @param param
     * @return
     */
    public static ResponseEntity<Object> postRequest(String url, String param) {
        String resStr = "";
        //创建默认的httpClient实例.
        CloseableHttpClient httpclient = null;
        //接收响应结果
        CloseableHttpResponse response = null;
        try {
            //创建httppost
            httpclient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader(HTTP.CONTENT_TYPE, "application/json;charset=utf-8");
            //参数
            StringEntity se = new StringEntity(param);
            httpPost.setEntity(se);
            response = httpclient.execute(httpPost);
            //解析返结果
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                resStr = EntityUtils.toString(entity, "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return new ResponseEntity(resStr, HttpStatus.valueOf(response.getStatusLine().getStatusCode()));
        }
    }

    public static OKHttpResponseDTO doDeleteRequest() {
        return null;
    }

    public static OKHttpResponseDTO doPutRequest() {
        return null;
    }


    /**
     * 获取全部的cookie信息
     *
     * @param httpServletRequest
     * @return
     */
    public static String getCookieStr(HttpServletRequest httpServletRequest) {
        javax.servlet.http.Cookie[] cookies = httpServletRequest.getCookies();
        StringBuffer sb = new StringBuffer();
        String token = "";
        for (javax.servlet.http.Cookie c : cookies) {
            sb.append(c.getName());
            sb.append("=");
            sb.append(c.getValue());
            sb.append(";");
            if (c.getName().equals("Token")) {
                token = c.getValue();
            }
        }
        return sb.toString();
    }


    /**
     * 获取全部的cookie信息
     *
     * @param httpServletRequest
     * @return
     */
    public static String getCookie(String cookieKey,HttpServletRequest httpServletRequest) {
        javax.servlet.http.Cookie[] cookies = httpServletRequest.getCookies();
        for (Cookie c : cookies) {
            if(c.getName().equalsIgnoreCase(cookieKey)){
                return c.getValue();
            }
        }
        return "";
    }

}
