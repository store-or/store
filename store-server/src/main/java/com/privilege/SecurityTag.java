package com.privilege;
import com.privilege.security.Principal;
import com.privilege.security.SecurityContext;
import org.apache.commons.lang.StringUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * Created by IntelliJ IDEA.
 * User: hongxingshi
 * Date: 12-11-9
 * Time: 下午4:33
 */
public class SecurityTag  extends BodyTagSupport {

    private static final long serialVersionUID = -3961103709254010950L;
    private String action;
    private String anyAction;
    private String allAction;

    public String getAllAction() {
        return allAction;
    }

    public void setAllAction(String allAction) {
        this.allAction = allAction;
    }

    public String getAnyAction() {
        return anyAction;
    }

    public void setAnyAction(String anyAction) {
        this.anyAction = anyAction;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public int doStartTag() throws JspException {
        return  isPermissive()?EVAL_BODY_INCLUDE:SKIP_BODY;
    }
    private boolean isPermissive(){
        boolean ret = false;
        boolean hasAction = true;
        boolean hasAnyAction = true;
        boolean hasAllAction = true;
        Principal principal = SecurityContext.getPrincipal();
        if (principal != null) {
            if (StringUtils.isNotBlank(action)) {
                hasAction = principal.hasAction(action);
            }
            if (StringUtils.isNotBlank(anyAction)) {
                hasAnyAction = principal.hasAnyAction(anyAction);
            }
            if (StringUtils.isNotBlank(allAction)) {
                hasAllAction  = principal.hasAllAction(allAction);
            }
            if(hasAction && hasAllAction && hasAnyAction){
                ret = true;
            }
        }
        return ret;
    }
}
