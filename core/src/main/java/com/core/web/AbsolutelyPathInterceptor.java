package com.core.web;

import com.core.web.constants.WebConstants;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by laizy on 2017/6/8.
 */
public class AbsolutelyPathInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        request.setAttribute(WebConstants.ABSOLUTE_CONTEXT_PATH, getAbsolutePath(request));
        return true;
    }

    public static String getAbsolutePath(HttpServletRequest request) {
        String absoluteContextPath;
        if (request.getServerPort() == 80) {
            absoluteContextPath = request.getScheme() + "://" + request.getServerName() + request.getContextPath();
        } else if (request.getServerPort() == 443) {
            absoluteContextPath = request.getScheme() + "://" + request.getServerName() + request.getContextPath();
        } else {
            absoluteContextPath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        }
        return absoluteContextPath;
    }
}
