package com.core.http;

import com.core.job.JobException;
import com.core.job.JobExecutor;
import com.core.json.JsonMapper;
import com.core.json.SJsonResponse;
import com.core.util.encrypt.AesUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: hongxingshi
 * Date: 14-7-22
 * Time: 下午5:56
 * To change this template use File | Settings | File Templates.
 */
public abstract class HttpOperate {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    public static final String CHARSET_NAME = "UTF-8";

    protected long intervalTime = 0;

    protected <T> T execute(HttpJob<T> httpJob) throws HttpJobException {
        return execute(httpJob, true);
    }
    protected <T> T execute(HttpJob<T> httpJob, boolean repeat) throws HttpJobException {
        try {
            JobExecutor.executeInCurrentThread(httpJob);
            return httpJob.getResult();
        } catch (JobException e) {
            if (e.getCause() instanceof HttpJobException) {
                HttpJobException exe = (HttpJobException) e.getCause();
                if(!repeat){
                    throw exe;
                }
                if (SJsonResponse.ErrorCode.TIME_OUT.getErrorCode() != exe.getErrorCode()) {
                    throw exe;
                }
                return execute(httpJob,false);
            } else {
                throw new HttpJobException(e.getMessage(), e);
            }
        }
    }

    private String handleResponse(HttpResponse response) throws HttpJobException {
        try {
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() != HttpStatus.SC_OK) {
                throw new HttpJobException(String.format("状态码:%d,错误原因:%s", statusLine.getStatusCode(), statusLine.getReasonPhrase()));
            }
            HttpEntity httpEntity = response.getEntity();
            JsonMapper mapper = JsonMapper.getDefault();
            SJsonResponse sJsonResponse = mapper.fromJson(httpEntity.getContent(), SJsonResponse.class);
            if (sJsonResponse == null) {
                throw new HttpJobException("对象流无法转换成SJsonResponse");
            }
            if (SJsonResponse.ErrorCode.TIME_OUT.getErrorCode() == sJsonResponse.getReturnCode()) {
                logger.error(sJsonResponse.getReturnMsg());
                this.intervalTime = Long.valueOf(sJsonResponse.getContent().toString()) - System.currentTimeMillis() / 1000L;
            }
            if (sJsonResponse.getReturnCode() != SJsonResponse.CODE_SUCCESS) {
                throw new HttpJobException(sJsonResponse.getReturnCode(), sJsonResponse.getReturnMsg());
            }
            if (sJsonResponse.getContent() == null) {
                return null;
            }
            if (SJsonResponse.ENCRYPT_TYPE == sJsonResponse.getType()) {
                return AesUtils.decryptBase64(sJsonResponse.getContent().toString(), AesUtils.KEY);
            }
            return sJsonResponse.getContent().toString();
        } catch (Exception e) {
            throw new HttpJobException(e.getMessage(), e);
        }
    }
    protected <T> T handleResponse(HttpResponse response, Class<T> clazz) throws HttpJobException {
        return JsonMapper.getDefault().fromJson(handleResponse(response), clazz);
    }

    protected String handleStringResponse(HttpResponse response) throws HttpJobException {
        return handleResponse(response);
    }

    protected <T> List handleListResponse(HttpResponse response, Class<T> clazz) throws HttpJobException{
        JsonMapper mapper = JsonMapper.getDefault();
        return mapper.fromJson(handleResponse(response), mapper.constructParametricType(List.class, clazz));

    }

    protected HttpPost setHttpPost(String url, Map<String,String> paramMap, boolean safe) throws HttpJobException{
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> paramList = new ArrayList<NameValuePair>();
        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            paramList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        if (safe) {
            addSafeParams(paramList);
        };
        UrlEncodedFormEntity entity = null;
        try {
            entity = new UrlEncodedFormEntity(paramList, CHARSET_NAME);
        } catch (UnsupportedEncodingException e) {
            throw new HttpJobException(e.getMessage(), e);
        }
        httpPost.setEntity(entity);
        return httpPost;
    }

    protected void addSafeParams(List<NameValuePair> paramList) {
        long currentSecond = System.currentTimeMillis() / 1000L + intervalTime;
        paramList.add(new BasicNameValuePair(SafeToken.SECURITY_VERSION_STRING, String.valueOf(SafeToken.SAFE_VERSION_KEY)));
        paramList.add(new BasicNameValuePair(SafeToken.TIME_STAMP, String.valueOf(currentSecond)));
        paramList.add(new BasicNameValuePair(SafeToken.TOKEN, DigestUtils.md5Hex(SafeToken.PREFIX + SafeToken.SAFE_VERSION_KEY + currentSecond)));
    }

    protected HttpPost setHttpPost(String url, Map<String,String> paramMap) throws HttpJobException {
        return setHttpPost(url, paramMap, true);
    }
}
