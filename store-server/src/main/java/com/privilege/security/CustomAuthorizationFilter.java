package com.privilege.security;

import com.google.common.collect.Iterators;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 */
public class CustomAuthorizationFilter extends AuthorizationFilter {

    @Autowired
    private DefaultSecurityManager securityManager;

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        Subject subject = getSubject(request, response);
        Principal principal = (Principal) subject.getPrincipal();
        if (!principal.isAuthentication()) {
            ShiroDbRealm realm = (ShiroDbRealm) Iterators.getLast(securityManager.getRealms().iterator());
            realm.doGetAuthorizationInfo(new SimplePrincipalCollection(principal, "shiroDbRealm"));
        }
        return principal.hasAction((HttpServletRequest) request);
    }
}

