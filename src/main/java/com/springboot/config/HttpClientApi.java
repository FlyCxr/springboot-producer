package com.springboot.config;

import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@SuppressWarnings("all")
public class HttpClientApi {

    /**
     * 将required设置为false:
     * 为了避免RequestConfig没被注进来的时候其他方法都不能用,报createbeanfailedexception
     *
     */
    @Autowired(required=false)
    private RequestConfig requestConfig;

    @Autowired
    private HttpClientBuilder httpClientBuilder;

    /**
     * 无参get请求
     * @param url
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public String doGet(String url) throws ClientProtocolException, IOException{
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(requestConfig);
        CloseableHttpResponse response = null;
        try {
            CloseableHttpClient httpClient = httpClientBuilder.build();
            response = httpClient.execute(httpGet);
            return EntityUtils.toString(response.getEntity(), "UTF-8");
        } finally {
            if (response != null) {
                try{ response.close(); }catch (IOException e){e.printStackTrace();};
            }
        }
    }

    /**
     * 有参get请求
     * @param url
     * @param params
     * @return
     * @throws URISyntaxException
     * @throws ClientProtocolException
     * @throws IOException
     */
    public String doGet(String url , Map<String, String> params) throws URISyntaxException, ClientProtocolException, IOException{
        URIBuilder uriBuilder = new URIBuilder(url);
        if(params != null){
            for(String key : params.keySet()){
                uriBuilder.setParameter(key, params.get(key));
            }
        }
        return doGet(uriBuilder.build().toString());
    }

    /**
     * 有参post请求,模拟表单提交
     * @param url
     * @param params
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public String doPost(String url , Map<String, String> params) throws ClientProtocolException, IOException{
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);
        if(params != null){
            List<NameValuePair> parameters = new ArrayList<NameValuePair>(0);
            for(String key : params.keySet()){
                parameters.add(new BasicNameValuePair(key, params.get(key)));
            }
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parameters);
            httpPost.setEntity(formEntity);
        }
        CloseableHttpResponse response = null;
        try {
            CloseableHttpClient httpClient = httpClientBuilder.build();
            response = httpClient.execute(httpPost);
            return EntityUtils.toString(response.getEntity(), "UTF-8");
        } finally {
            if (response != null) {
                try{ response.close(); }catch (IOException e){e.printStackTrace();};
            }
        }
    }

    /**
     * 有参post请求,json交互
     * @param url
     * @param json
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public String doPostJson(String url , String json) throws ClientProtocolException, IOException{
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);
        if(StringUtils.isNotBlank(json)){
            //标识出传递的参数是 application/json
            StringEntity stringEntity = new StringEntity(json, ContentType.APPLICATION_JSON);
            httpPost.setEntity(stringEntity);
        }
        CloseableHttpResponse response = null;
        try {
            CloseableHttpClient httpClient = httpClientBuilder.build();
            response = httpClient.execute(httpPost);
            return EntityUtils.toString(response.getEntity(), "UTF-8");
        } finally {
            if (response != null) {
                try{ response.close(); }catch (IOException e){e.printStackTrace();};
            }
        }
    }

    /**
     * 无参post请求
     * @autho 董杨炀
     * @time 2017年5月8日 下午3:33:27
     * @param url
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public String doPost(String url) throws ClientProtocolException, IOException{
        return doPost(url, null);
    }

    /**
     * 拼接URL参数
     * @param url
     * @param map
     * @return
     */
    public String joinParamToUrl(String url, Map<String, String> map) {
        if(StringUtils.isNotBlank(url) && map != null){
            StringBuffer sb = new StringBuffer();
            sb.append(url);
            Set<String> keySet = map.keySet();
            if (!keySet.isEmpty()){
                sb.append("?");
                for (String str : keySet) {
                    sb.append(str);
                    sb.append("=");
                    sb.append(map.get(str));
                    sb.append("&");
                }
            }
            sb.deleteCharAt(sb.length() - 1);
            url = sb.toString();
        }
        return url;
    }

}
