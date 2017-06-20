package com.privilege.security;

import com.core.system.App;
import com.privilege.service.UserService;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 */
public class CustomAuthenticationFilter extends FormAuthenticationFilter {

    @Override
    public boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        //判断anon
        ResourceCache resourceCache = App.getBean(ResourceCache.class);
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        if(resourceCache.isAnonUri(httpServletRequest.getRequestURI())){
            return true;
        }
        return super.onPreHandle(request, response, mappedValue);
    }

    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        httpServletRequest.getSession(true).setAttribute(SecurityContext.PRINCIPAL, subject.getPrincipal());
        App.getBean(UserService.class).loginSuccess(SecurityContext.getPrincipal().getId());
        return super.onLoginSuccess(token, subject, request, response);
    }
}