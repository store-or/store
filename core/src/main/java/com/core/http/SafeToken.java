package com.core.http;


import com.core.json.SJsonResponse;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Created with IntelliJ IDEA.
 * User: hongxingshi
 * Date: 14-11-20
 * Time: 下午5:37
 * To change this template use File | Settings | File Templates.
 */
public class SafeToken {
    public static final String INPUT_CHARSET = "utf-8";
    public static final String SECURITY_VERSION_STRING = "v";
    public static final String TIME_STAMP = "t";
    public static final String TOKEN = "token";

    public final static String PREFIX = "281031764343127189610010480095250822367";// md5 prefix
    public final static int SAFE_VERSION_KEY = 1;//安全协议版本
    public final static int TIME_LINE = 1800;// 超时限度,30分钟

    private Integer v;
    private String t;
    private String token;

    public SafeToken() {
    }

    public SafeToken(Integer v, String t, String token) {
        this.v = v;
        this.t = t;
        this.token = token;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    public String toString() {
        return "&v=" + (v == null ? "" : v) + "&t=" + (t == null ? "" : t) + "&token=" + (token == null ? "" : token);
    }

    public SJsonResponse validate(){
        if (StringUtils.isBlank(t)){
            return new SJsonResponse(SJsonResponse.ErrorCode.REQUEST_NOT_VALID.getErrorCode(), "Missing Timestamp!");
        }
        if (StringUtils.isBlank(token)){
            return new SJsonResponse(SJsonResponse.ErrorCode.REQUEST_NOT_VALID.getErrorCode(), "Missing Token!");
        }
        if (v == null){
            return new SJsonResponse(SJsonResponse.ErrorCode.REQUEST_NOT_VALID.getErrorCode(), "Missing Security Version of The Agreement!");
        }
        if (v.intValue() != SAFE_VERSION_KEY){
            return new SJsonResponse(SJsonResponse.ErrorCode.REQUEST_NOT_VALID.getErrorCode(), "Security Version of The Agreement is not Correct!");
        }
        long now = System.currentTimeMillis()/1000;
        if ((Math.abs(now - Long.parseLong(t)) >= TIME_LINE)){
            return new SJsonResponse<Long>(SJsonResponse.ErrorCode.TIME_OUT.getErrorCode(), "The Request has failed!", now ,false);
        }
        if (!StringUtils.equalsIgnoreCase(token, sign())){
            return new SJsonResponse(SJsonResponse.ErrorCode.SIGN_FAILED.getErrorCode(), "Illegal Request!");
        }
        return null;
    }

    public String sign() {
        return DigestUtils.md5Hex(PREFIX + v + t);
    }
}
