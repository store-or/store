package com.core.json;

/**
 * Created by laizy on 2017/6/7.
 */
public class JsonResponse<T> {
    private static final long serialVersionUID = 3617154470380165248L;

    private int returnCode;
    private String returnMsg;
    private T content;

    public static final int CODE_SUCCESS = 1;
    public static final int CODE_FAILURE = 0;
    public static final String MSG_SUCCESS = "SUCCESS";
    public static final String MSG_ERROR = "error";
    public static final String MSG_FAILED = "failed";
    public static final String JSON_ERROR = "{\"returnCode\":0,\"returnMsg\":\"error\"}";
    public static final String JSON_SUCCESS = "{\"returnCode\":1,\"returnMsg\":\"success\"}";

    public JsonResponse() {
    }

    public JsonResponse(int returnCode, String returnMsg, T content) {
        super();
        this.returnCode = returnCode;
        this.returnMsg = returnMsg;
        this.content = content;
    }

    public JsonResponse(int returnCode, String returnMsg) {
        super();
        this.returnCode = returnCode;
        this.returnMsg = returnMsg;
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
        String json = JsonMapper.getDefault().toJson(this);
        return json == null ? JSON_ERROR : json;

    }
}
