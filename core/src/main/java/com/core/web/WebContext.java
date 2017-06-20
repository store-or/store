package com.core.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by laizy on 2017/6/7.
 */
public final class WebContext {

    private static ThreadLocal<HttpServletRequest> requestThreadLocal = new ThreadLocal<HttpServletRequest>();
    private static ThreadLocal<HttpServletResponse> responseThreadLocal = new ThreadLocal<HttpServletResponse>();

    private WebContext() {}

    static void unRegister() {
        requestThreadLocal.remove();
        responseThreadLocal.remove();
    }

    static void register(HttpServletRequest request, HttpServletResponse response) {
        requestThreadLocal.set(request);
        responseThreadLocal.set(response);
    }

    public static HttpServletRequest getRequest() {
        HttpServletRequest request = requestThreadLocal.get();
        if (request == null) {
            throw new RuntimeException("the error seems will not occur . HttpServletRequest null. ");
        }
        return requestThreadLocal.get();
    }

    public static HttpServletResponse getResponse() {
        HttpServletResponse response = responseThreadLocal.get();
        if (response == null) {
            throw new RuntimeException("the error seems will not occur . HttpServletResponse null. ");
        }
        return response;
    }

}
