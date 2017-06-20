package com.privilege.security;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

/**
 */
public final class SecurityContext {

    public static final String PRINCIPAL = "principal";
    public static final String ROLE_SUPERUSER = "ROLE_SUPERUSER";
    public static final Long ROLE_SUPERUSER_ID = 1l;
    public static final String ROLE_SUPERUSER_PATH = "/";
    public static final String GROUP_SUPERUSER = "GROUP_SUPERUSER";
    public static final Long GROUP_SUPERUSER_ID = 1l;
    public static final String GROUP_SUPERUSER_PATH = "/";
    public static final Long SUPERUSER_ID = 1l;

    private SecurityContext(){}

    public static Principal getPrincipal() {
        Subject subject = SecurityUtils.getSubject();
        return  (Principal) subject.getPrincipal();
    }

    public static boolean isAuthenticate() {
        Principal principal = getPrincipal();
        return principal != null && principal.isAuthentication();
    }

    public static boolean hasAction(String action) {
        Principal principal = getPrincipal();
        if (principal == null) {
            return false;
        }
        return principal.hasAction(action);
    }

    /**
     * JOB任务调用函数中使用这个获取username
     * @return
     */
    public static String getUserName() {
        try {
            Principal principal = getPrincipal();
            if (null == principal) {
                return "";
            }
            return principal.getUserName();
        } catch (Exception e) {
            return "";
        }
    }
}
