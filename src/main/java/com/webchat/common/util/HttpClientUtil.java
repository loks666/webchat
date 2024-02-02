package com.webchat.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MimeTypeUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.*;

@Slf4j
public class HttpClientUtil {

    public static int defaultRetryTimes = 1;

    public static int defaultReadTimeOut = 100000; // 默认超时时间，毫秒

    public static <T extends Object> T getObjectFromUrl(String url, Class<T> type) throws URISyntaxException {
        return getObjectFromUrl(new URI(url), type);
    }

    public static <T extends Object> T getObjectFromUrl(URI url, Class<T> type ) {
        return getObjectFromUrl(url, type, defaultRetryTimes, defaultReadTimeOut);
    }

    public static ResponseEntity<?> getObjectFromUrl(String url, Map<String, String> headers, Class<?> type) {
        return getObjectFromUrl(url, headers, type, defaultRetryTimes, defaultReadTimeOut);
    }

    public static <T extends Object> T postObjectForUrl(URI url, Object requestBody,  Class<T> type, boolean ...ssl) {
        return postObjectForUrl(url, requestBody, null, type, defaultRetryTimes, defaultReadTimeOut, ssl);
    }

    public static <T extends Object> T postObjectForUrl(String url, Map<String, String> headers, Object requestBody,  Class<T> type, boolean ...ssl)
            throws URISyntaxException {
        return postObjectForUrl(new URI(url), requestBody, headers, type, defaultRetryTimes, defaultReadTimeOut, ssl);
    }

    public static <T extends Object> T postObjectForUrl(String url, Object requestBody, Class<T> type, boolean ...ssl)
            throws URISyntaxException {
        return postObjectForUrl(new URI(url), requestBody, null, type, defaultRetryTimes,
                defaultReadTimeOut, ssl);
    }

    public static <T extends Object> T postObjectForUrl(String url, Object requestBody, Class<T> type,
                                                        int retryTimes, int defaultReadTimeOut)
            throws URISyntaxException {
        return postObjectForUrl(new URI(url), requestBody, null, type, retryTimes, defaultReadTimeOut);
    }

    public static <T extends Object> T postObjectForUrl(String url, Object requestBody, Map<String, String> headers,
                                                        Class<T> type, boolean ...ssl)throws URISyntaxException {
        return postObjectForUrl(new URI(url), requestBody, headers, type, defaultRetryTimes, defaultReadTimeOut, ssl);
    }

    public static void putObjectFormUrl(String url, Object requestBody, Map<String, String> headers)
            throws URISyntaxException {
        putObjectFormUrl(new URI(url), requestBody, headers, defaultRetryTimes, defaultReadTimeOut);
    }

    public static ResponseEntity<?> putObjectFormUrl(String url, Object requestBody, Map<String, String> headers,
                                                     Class<?> type) throws URISyntaxException {
        return putObjectFormUrl(new URI(url), requestBody, headers, type, defaultRetryTimes, defaultReadTimeOut);
    }

    public static ResponseEntity<?> deleteObjectFormUrl(String url, Object requestBody, Map<String, String> headers,
                                                        Class<?> type) throws URISyntaxException {
        return deleteObjectFormUrl(url, requestBody, headers, type, defaultRetryTimes, defaultReadTimeOut);
    }


    public static <T extends Object> T getObjectFromUrl(URI url, Class<T> type, int retryTimes, int readTimeOut) {
        RestTemplate restTemplate =  getRestTemplate(readTimeOut);
        boolean success = false;
        T result = null;
        while (!success && retryTimes >= 0) {
            try {
                result = restTemplate.getForObject(url, type);
                success = true;
            } catch (Exception ex) {
                log.error("rest template getObjectFromUrl error: ", ex);
                retryTimes--;
                if (retryTimes == 0) {
                    throw ex;
                }
            }
        }
        return result;
    }

    public static ResponseEntity<?> getObjectFromUrl(String url,  Map<String, String> headerMap,
                                                     Class<?> responseType, int retryTimes, int readTimeOut) {
        HttpHeaders httpHeaders = setHttpHeaders(headerMap);
        if (!httpHeaders.containsKey(HttpHeaders.CONTENT_TYPE)) {
            httpHeaders.add(HttpHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON_VALUE);

        }
        HttpEntity<Object> formEntity = new HttpEntity<>(null, httpHeaders);
        RestTemplate restTemplate =  getRestTemplate(readTimeOut);
        boolean success = false;
        ResponseEntity<?> result = null;
        while (!success && retryTimes >= 0) {
            try {
                result = restTemplate.exchange(url, HttpMethod.GET, formEntity, responseType);
                success = true;
            } catch (Exception ex) {
                log.error("rest template postObjectForUrl error: ", ex);
                retryTimes--;
                if (retryTimes == 0) {
                    throw ex;
                }
            }
        }
        return result;
    }

    public static <T extends Object> T postObjectForUrl(URI  url, Object requestBody, Map<String, String> headerMap,
                                                        Class<T> type, int retryTimes, int readTimeOut,
                                                        boolean ...ssl) {
        HttpHeaders httpHeaders = setHttpHeaders(headerMap);
        if (!httpHeaders.containsKey(HttpHeaders.CONTENT_TYPE)) {
            httpHeaders.add(HttpHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON_VALUE);

        }
        HttpEntity<Object> formEntity = new HttpEntity<Object>(requestBody, httpHeaders);
        RestTemplate restTemplate =  getRestTemplate(readTimeOut, ssl);
        boolean success = false;
        T result = null;
        while (!success && retryTimes >= 0) {
            try {
                result = restTemplate.postForObject(url, formEntity, type);
                success = true;
            } catch (Exception ex) {
                log.error("rest template postObjectForUrl error: ", ex);
                retryTimes--;
                if (retryTimes == 0) {
                    throw ex;
                }
            }
        }
        return result;
    }

    /**
     * @param url
     * @param formData
     * @param type
     * @param <T>
     *
     * @return
     */
    public static <T extends Object> T  postFormForUrl(String url, Map<String, String> formData, Class<T> type ) {
        return postFormForUrl(url, formData, null, type);
    }

    /**
     * @param url
     * @param formData
     * @param type
     * @return
     *
     */
    public static <T extends Object> T postFormForUrl(String url, Map<String, String> formData,  Map<String, String>
            headerMap, Class<T> type) {
        HttpHeaders headers = setHttpHeaders(headerMap);
        if (!headers.containsKey(HttpHeaders.CONTENT_TYPE)) {
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        }
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        for (String key : formData.keySet()) {
            map.add(key, formData.get(key));
        }
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        RestTemplate restTemplate =  getRestTemplate(defaultReadTimeOut);
        return  restTemplate.postForObject(url, request , type);
    }

    /**
     * @param url
     * @param requestBody
     * @param headerMap
     * @param retryTimes
     * @param readTimeOut
     */
    public static void putObjectFormUrl(URI url, Object requestBody, Map<String, String> headerMap, int retryTimes,
                                        int readTimeOut) {
        HttpHeaders httpHeaders = setHttpHeaders(headerMap);
        if (!httpHeaders.containsKey(HttpHeaders.CONTENT_TYPE)) {
            httpHeaders.add(HttpHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON_VALUE);

        }
        HttpEntity<Object> formEntity = new HttpEntity<>(requestBody, httpHeaders);
        RestTemplate restTemplate =  getRestTemplate(readTimeOut);
        boolean success = false;
        while (!success && retryTimes >= 0) {
            try {
                restTemplate.put(url, formEntity);
                success = true;
            } catch (Exception ex) {
                log.error("rest template postObjectForUrl error: ", ex);
                retryTimes--;
                if (retryTimes == 0) {
                    throw ex;
                }
            }
        }
    }

    /**
     * @param url
     * @param requestBody
     * @param headerMap
     * @param responseType
     * @param retryTimes
     * @param readTimeOut
     *
     * @return
     */
    public static ResponseEntity<?> deleteObjectFormUrl(String url, Object requestBody, Map<String, String> headerMap,
                                                        Class<?> responseType, int retryTimes, int readTimeOut) {
        HttpHeaders httpHeaders = setHttpHeaders(headerMap);
        if (!httpHeaders.containsKey(HttpHeaders.CONTENT_TYPE)) {
            httpHeaders.add(HttpHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON_VALUE);

        }
        HttpEntity<Object> formEntity = new HttpEntity<>(requestBody, httpHeaders);
        RestTemplate restTemplate =  getRestTemplate(readTimeOut);
        boolean success = false;
        ResponseEntity<?> result = null;
        while (!success && retryTimes >= 0) {
            try {
                result = restTemplate.exchange(url, HttpMethod.DELETE, formEntity, responseType);
                success = true;
            } catch (Exception ex) {
                log.error("rest template postObjectForUrl error: ", ex);
                retryTimes--;
                if (retryTimes == 0) {
                    throw ex;
                }
            }
        }
        return result;
    }

    /**
     * @param url
     * @param requestBody
     * @param headerMap
     * @param responseType
     * @param retryTimes
     * @param readTimeOut
     *
     * @return
     */
    public static ResponseEntity<?> putObjectFormUrl(URI url, Object requestBody, Map<String, String> headerMap,
                                                     Class<?> responseType, int retryTimes, int readTimeOut) {
        HttpHeaders httpHeaders = setHttpHeaders(headerMap);
        if (!httpHeaders.containsKey(HttpHeaders.CONTENT_TYPE)) {
            httpHeaders.add(HttpHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON_VALUE);

        }
        HttpEntity<Object> formEntity = new HttpEntity<Object>(requestBody, httpHeaders);
        RestTemplate restTemplate =  getRestTemplate(readTimeOut);
        boolean success = false;
        ResponseEntity<?> result = null;
        while (!success && retryTimes >= 0) {
            try {
                result = restTemplate.exchange(url, HttpMethod.PUT, formEntity, responseType);
                success = true;
            } catch (Exception ex) {
                log.error("rest template postObjectForUrl error: ", ex);
                retryTimes--;
                if (retryTimes == 0) {
                    throw ex;
                }
            }
        }
        return result;
    }

    /**
     * @param headerMap
     *
     * @return
     */
    private static HttpHeaders setHttpHeaders(Map<String, String> headerMap) {
        HttpHeaders httpHeaders = new HttpHeaders();
        if (headerMap != null) {
            Set<String> keySet = headerMap.keySet();
            for (Iterator<String> it = keySet.iterator(); it.hasNext();) {
                String key = it.next();
                String value = headerMap.get(key);
                httpHeaders.add(key, value);
            }
        }
        return  httpHeaders;
    }

    /**
     * @param timeout
     *
     * @return
     */
    public static RestTemplate getRestTemplate(int timeout, boolean ...ssl) {
        RestTemplate restTemplate = null;
        if (ssl.length > 0 && ssl[0]) {
            HttpsSSLRequestFactory factory = new HttpsSSLRequestFactory();
            factory.setReadTimeout(timeout);
            factory.setConnectTimeout(defaultReadTimeOut);
            restTemplate = new RestTemplate(factory);
        } else {
            HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
            factory.setReadTimeout(timeout);
            factory.setConnectTimeout(defaultReadTimeOut);
            restTemplate = new RestTemplate(factory);
        }
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
        return  restTemplate;
    }

    public static List<String> getAsStringList(String url) {
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        for (int i = 0; i < defaultRetryTimes; ++i) {
            HttpResponse response = null;

            try {
                response = httpClient.execute(httpGet);
            } catch (IOException e) {
                log.error("Failed to download file,url: " + url, e);
            }

            if (response != null && response.getStatusLine().getStatusCode() == 200) {
                InputStreamReader reader = null;

                try {
                    reader = new InputStreamReader(response.getEntity().getContent());
                } catch (IOException var23) {
                    continue;
                }
                BufferedReader bufferedReader = new BufferedReader(reader);
                String line = "";
                ArrayList list = new ArrayList();
                try {
                    while ((line = bufferedReader.readLine()) != null) {
                        list.add(line.trim());
                    }
                } catch (IOException e) {
                    log.error("bufferedReader.readLine error", e);
                } finally {
                    try {
                        reader.close();
                        bufferedReader.close();
                    } catch (IOException var19) {
                        var19.printStackTrace();
                    }
                }
                return list;
            }
        }
        return null;
    }

    /**
     * 根据URL下载文件
     * @param url
     * @param headers
     * @param paramMap
     * @param file
     */
    public static void downloadByResourceUrl(String url, Map<String, String> headers, Map<String, String> paramMap,
                                             File file) {
        HttpClient httpClient = HttpClients.createDefault();
        InputStream in = null;
        try {
            if (paramMap != null) {
                StringBuilder buffer = new StringBuilder(url);
                for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                    appendParam(buffer, entry.getKey(), entry.getValue());
                }
                url = buffer.toString();
            }
            HttpGet httpGet = new HttpGet(url);
            if (headers != null && headers.size() > 0) {
                for (String key : headers.keySet()) {
                    httpGet.addHeader(key, headers.get(key));
                }
            }
            HttpResponse response = httpClient.execute(httpGet);
            FileOutputStream out = new FileOutputStream(file);
            int code = response.getStatusLine().getStatusCode();
            org.apache.http.HttpEntity entity = response.getEntity();
            in = entity == null ? null : entity.getContent();
            if (HttpStatus.SC_OK == code && in != null && entity != null) {
                byte[] data = readInputStream(entity.getContent());
                out.write(data);
                out.close();
            } else {
                log.error("downloadByResourceUrl error code. code:{}, url:{}", code, url);
                return;
            }
        } catch (Exception e) {
            log.error("downloadByResourceUrl error. url:{} ", url, e);
            return;
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    private static void appendParam(StringBuilder buffer, String name, String value) {
        if (!buffer.toString().contains("?")) {
            buffer.append("?");
        }
        if (!(buffer.charAt(buffer.length() - 1) == '?')) {
            buffer.append("&");
        }
        try {
            buffer.append(URLEncoder.encode(name, "UTF-8"));
            buffer.append("=");
            buffer.append(URLEncoder.encode(value, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
        }
    }

    /***
     * 读取输入流
     * @param inputStream
     * @return
     * @throws IOException
     */
    private static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }
}
