package com.core.json;


import com.core.util.encrypt.AesUtils;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: laizy
 * To change this template use File | Settings | File Templates.
 */
public class SJsonResponse<T> implements Serializable {

    private static final long serialVersionUID = 3617154470380165248L;

    private int returnCode;
    private String returnMsg;
    private T content;
    //默认不加密content
    private int type = 0;
    public static final int ENCRYPT_TYPE = 1;
    public static final int CODE_SUCCESS = 1;
    public static final String JSON_ERROR = "{\"returnCode\":0,\"returnMsg\":\"error\",\"type\":0}";
    public static final String JSON_SUCCESS = "{\"returnCode\":1,\"returnMsg\":\"success\",\"type\":0}";
    private boolean isJson = true;

    public SJsonResponse() {
    }

    public SJsonResponse(int returnCode, String returnMsg, T content, int type) {
        super();
        this.returnCode = returnCode;
        this.returnMsg = returnMsg;
        this.content = content;
        this.type = type;
    }

    public SJsonResponse(int returnCode, String returnMsg) {
        super();
        this.returnCode = returnCode;
        this.returnMsg = returnMsg;
    }

    public SJsonResponse(int returnCode, String returnMsg, T content) {
        super();
        this.returnCode = returnCode;
        this.returnMsg = returnMsg;
        this.content = content;
    }

    public SJsonResponse(int returnCode, String returnMsg, T content, boolean isJson) {
        this(returnCode , returnMsg , content);
        this.isJson = isJson;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    @Override
    public String toString() {
        String json;
        if (type == ENCRYPT_TYPE && content != null) {
            try {
                String encryptContent = AesUtils.encryptBase64((content instanceof String ? content.toString() : JsonMapper.getDefault().toJson(content)), AesUtils.KEY);
                SJsonResponse<String> encryptJsonResponse = new SJsonResponse<String>(this.returnCode, this.returnMsg, encryptContent,this.type);
                json = JsonMapper.getDefault().toJson(encryptJsonResponse);
            } catch (Exception exc) {
                return new JsonResponse(ErrorCode.SYSTEM_ERROR.getErrorCode(), ErrorCode.SYSTEM_ERROR.getErrorMessage()).toString();
            }
        } else {
            if(content != null && !(content instanceof String) && isJson){
                return new SJsonResponse<String>(returnCode , returnMsg , JsonMapper.getDefault().toJson(content) , type).toString();
            }
            json = JsonMapper.getDefault().toJson(this);
        }
        return json == null ? JSON_ERROR : json;
    }


    public enum ErrorCode {
        SYSTEM_ERROR(0, "系统出错"),
        SIGN_FAILED(-100, "无效的请求，签名失败"),
        TIME_OUT(-101, "客户端请求超时"),
        REQUEST_NOT_VALID(-102, "缺少参数或者参数不合法"),
        ;


        private int errorCode;
        private String errorMessage;

        ErrorCode(int errorCode, String errorMessage){
            this.errorCode = errorCode;
            this.errorMessage = errorMessage;
        }

        public int getErrorCode() {
            return errorCode;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }
}
