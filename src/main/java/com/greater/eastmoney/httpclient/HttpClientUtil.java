package com.greater.eastmoney.httpclient;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.util.TypeUtils;
import com.greater.eastmoney.common.ResultCodeEnum;
import com.greater.eastmoney.exception.BizException;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.StandardHttpRequestRetryHandler;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.springframework.cglib.beans.BeanMap;

import java.io.File;
import java.util.Map;

/**
 * httpClient静态工具类
 */
@Slf4j
public class HttpClientUtil {
    private static class Holder {
        private static final CloseableHttpClient HTTP_CLIENT = buildHttpClient();
    }

    /**
     * 提交jsons格式请求
     * 
     * @param url
     * @param req
     * @return
     */
    public static <T> T postJSON(String url, Object req, Class<T> resClazz) {
        String res = postJSON(url, req);
        return JSON.parseObject(res, resClazz);
    }

    public static <T> T post(String url, Object req, Class<T> resClazz) {
        String res = post(url, req);
        return JSON.parseObject(res, resClazz);
    }

    /**
     * 提交jsons格式请求
     * 
     * @param url
     * @param req
     * @return
     */
    public static String postJSON(String url, Object req) {
        HttpPost httpPost = new HttpPost(url);
        StringEntity entity = new StringEntity(JSON.toJSONString(req), ContentType.APPLICATION_JSON);
        httpPost.setEntity(entity);

        if (log.isInfoEnabled()) {
            log.info("Http post request to url:{},params:{}", url, JSON.toJSONString(req));
        }
        long t1 = System.currentTimeMillis();
        String res = execute(httpPost);
        if (log.isInfoEnabled()) {
            log.info("Http post result:{},cost:{}ms", res, System.currentTimeMillis() - t1);
        }
        return res;
    }

    public static String post(String url, Object req) {
        HttpPost httpPost = new HttpPost(url);
        MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();

        BeanMap paramMap = BeanMap.create(req);

        for (Object key : paramMap.keySet()) {
            if (key instanceof String) {
                Object value = paramMap.get(key);
                if (value == null) {
                    continue;
                }
                if (value instanceof File) {
                    entityBuilder.addPart((String) key, new FileBody((File) value));
                } else {
                    entityBuilder.addTextBody((String) key, TypeUtils.castToString(value));
                }
            }
        }
        httpPost.setEntity(entityBuilder.build());

        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.96 Safari/537.36");
        httpPost.addHeader("cookie","配置文件取");

        if (log.isInfoEnabled()) {
            log.info("Http post request to url:{},params:{}", url, JSON.toJSONString(req));
        }
        long t1 = System.currentTimeMillis();
        String res = execute(httpPost);
        if (log.isInfoEnabled()) {
            log.info("Http post result:{},cost:{}ms", res, System.currentTimeMillis() - t1);
        }
        return res;
    }


    public static String get(String url ) {
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.96 Safari/537.36");
        httpGet.addHeader("cookie","配置文件取");
        httpGet.addHeader("Referer","http://quote.eastmoney.com/zixuan/?from=home");
        if (log.isInfoEnabled()) {
            log.info("Http get request to url:{}", url);
        }
        long t1 = System.currentTimeMillis();
        String res = execute(httpGet);
        if (log.isInfoEnabled()) {
            log.info("Http post result:{},cost:{}ms", res, System.currentTimeMillis() - t1);
        }
        return res;
    }

    @SneakyThrows
    public static byte[] downloadFile(String url) {
        HttpGet get = new HttpGet(url);

        log.info("Http get request to url:{}", url);
        long t1 = System.currentTimeMillis();
        @Cleanup
        CloseableHttpResponse response = Holder.HTTP_CLIENT.execute(get);

        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            log.info("Http get response success,{},{},Cost: {}ms",
                    response.getLastHeader(HttpHeaders.CONTENT_TYPE),
                    response.getLastHeader(HttpHeaders.CONTENT_LENGTH),
                    System.currentTimeMillis() - t1);

            return EntityUtils.toByteArray(response.getEntity());
        }
        log.error("Http get response failed : {}", response.getStatusLine());

        throw new BizException(ResultCodeEnum.SYSTEM_ERROR, "文件下载异常");
    }

    public static String execute(HttpUriRequest httpPost) {
        HttpEntity httpEntity = null;
        try {
            HttpResponse response = Holder.HTTP_CLIENT.execute(httpPost);
            if (response == null || HttpStatus.SC_OK != response.getStatusLine().getStatusCode()) {
                log.error("Http post error, url:{},statusCode:{},reasonPhrase:{}", httpPost.getURI().toString(),
                        response.getStatusLine().getStatusCode(), response.getStatusLine().getReasonPhrase());
                throw new BizException(ResultCodeEnum.SYSTEM_ERROR, response.getStatusLine().toString());
            }

            return EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            log.error("Http post error url:{} exception:", httpPost.getURI(), e);
            throw new BizException(ResultCodeEnum.SYSTEM_ERROR, ExceptionUtils.getRootCauseMessage(e));
        } finally {
            EntityUtils.consumeQuietly(httpEntity);
        }
    }

    private static final CloseableHttpClient buildHttpClient() {
        PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager();
        connMgr.setMaxTotal(500);
        connMgr.setDefaultMaxPerRoute(200);

        // 由于涉及到文件上传,socketTimeout时间相对较长
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(5000).setConnectTimeout(5000)
                .setSocketTimeout(60000).build();

        CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig)
                .setConnectionManager(connMgr).setRetryHandler(new StandardHttpRequestRetryHandler()).build();
        return httpClient;
    }

    /**
     * 提交jsons格式请求,通过RSA加密方式
     *
     * @param url
     * @return
     */
    public static <T> T postJSONWithRSA(String url, String content, Map<String, String> headerMap, Class<T> resClazz) {
        HttpPost httpPost = new HttpPost(url);
        StringEntity entity = new StringEntity(content, ContentType.APPLICATION_JSON);
        httpPost.setEntity(entity);
        if (headerMap != null) {
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                httpPost.setHeader(entry.getKey(), entry.getValue());
            }
        }

        log.info("Http post request to url:{},params:{}", url, content);
        long t1 = System.currentTimeMillis();
        String res = execute(httpPost);
        log.info("Http post result:{},cost:{}ms", res, System.currentTimeMillis() - t1);

        T result = null;
        try {
            result = JSON.parseObject(res, resClazz);
        } catch (Exception e) {
            throw new BizException(ResultCodeEnum.SYSTEM_ERROR);
        }
        return result;
    }
}
