package com.privilege.security;

import com.core.system.App;
import com.google.common.collect.HashMultimap;
import com.privilege.domain.Action;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 */
public class Principal implements Serializable {

    private static final String split=";";
    public static final Object PRESENT = new Object();
    private static final long serialVersionUID = -6577595270150512805L;

    private Long id;
    private String userName;
    private String trueName;
    private String email;
    private boolean weakPassword ;
    private transient boolean authentication;
    private transient boolean superuser;
    private transient Map<String,Action> nameActionMap = new HashMap<String, Action>();
    private transient HashMultimap<String,Action> uriActionMap = HashMultimap.create();

    public Principal() {
    }

    public Principal(Long id, String userName, String trueName, String email , boolean weakPassword) {
        this.id = id;
        this.userName = userName;
        this.trueName = trueName;
        this.email = email;
        this.weakPassword = weakPassword;
    }

    public Long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getTrueName() {
        return trueName;
    }
    public void addAction(Action action) {
        nameActionMap.put(action.getName(), action);
        String uris[] = action.getUri().split(split);
        for (String uri:uris){
            uriActionMap.put(uri, action);
        }
    }

    public boolean hasAction(String action) {
        if (isSuperuser()) {
            return true;
        }
        ResourceCache resourceCache = App.getBean(ResourceCache.class);
        return resourceCache.isAuthcAction(action) || nameActionMap.containsKey(action);
    }
    public boolean hasAnyAction(String actions) {
        if (isSuperuser()) {
            return true;
        }
        ResourceCache resourceCache = App.getBean(ResourceCache.class);
        String actionArray[] = StringUtils.split(actions, split);
        for(String action:actionArray){
            String tmpAction = StringUtils.trim(action);
            if(StringUtils.isNotBlank(tmpAction)){
                if(resourceCache.isAuthcAction(tmpAction) || nameActionMap.containsKey(tmpAction)){
                    return true;
                }
            }
        }
        return false;
    }
    public boolean hasAllAction(String actions) {
        if (isSuperuser()) {
            return true;
        }
        ResourceCache resourceCache = App.getBean(ResourceCache.class);
        String actionArray[] = StringUtils.split(actions, split);
        for(String action:actionArray){
            String tmpAction = StringUtils.trim(action);
            if(StringUtils.isNotBlank(tmpAction)){
                if(!resourceCache.isAuthcAction(tmpAction) && !nameActionMap.containsKey(tmpAction)){
                    return false;
                }

            }
        }
        return true;
    }

    public boolean hasAction(HttpServletRequest request) {
        if (isSuperuser()) {
            return true;
        }
        ResourceCache resourceCache = App.getBean(ResourceCache.class);
        String uri = request.getServletPath();
        if(resourceCache.isAuthoUri(uri)){
            if(uri.endsWith("/") && uri.length()>1){
                uri = uri.substring(0,uri.length()-1);
            }
            Set<Action> actions = uriActionMap.get(uri);
            if (actions != null && actions.size() > 0) {
                for (Action action : actions) {
                    if (ActionMatch.match(request, action)){
                        return true;
                    }
                }
            }
            return false;
        }else{
            return true;
        }
    }

    public boolean isAuthentication() {
        return authentication;
    }

    public void setAuthentication(boolean authentication) {
        this.authentication = authentication;
    }

    public boolean isSuperuser() {
        return superuser;
    }

    public void setSuperuser(boolean superuser) {
        this.superuser = superuser;
    }

    public boolean isWeakPassword() {
        return weakPassword;
    }

    public void setWeakPassword(boolean weakPassword) {
        this.weakPassword = weakPassword;
    }
}
