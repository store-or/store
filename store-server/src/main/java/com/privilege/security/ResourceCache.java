package com.privilege.security;

import com.core.system.InitialException;
import com.core.system.Initialize;
import com.google.common.collect.HashMultimap;
import com.privilege.domain.Action;
import com.privilege.domain.Resource;
import com.privilege.service.ResourceService;
import com.privilege.service.resource.template.FilterType;
import org.hibernate.criterion.CriteriaSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 */
@Component
public class ResourceCache implements Initialize {
    public static final String RESOURCE_CACHE = "RESOURCE_CACHE";
    private static final String split=";";
    @Autowired
    private ResourceService resourceService;

    private Map<String, Action> anonActionCache = Collections.emptyMap();
    private Map<String, Action> authcActionCache = Collections.emptyMap();
    private Map<String, Action> authoActionCache = Collections.emptyMap();

    private HashMultimap<String,Action> anonUriActionMap = HashMultimap.create();
    private HashMultimap<String,Action> authcUriActionMap = HashMultimap.create();
    private HashMultimap<String,Action> authoUriActionMap = HashMultimap.create();

    @Override
    public void init() throws InitialException {
        List<Resource> resources = resourceService.find(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        load(resources);
    }
    public void refresh(){
        List<Resource> resources = resourceService.find(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        load(resources);
    }
    public synchronized void load(List<Resource> resources) {
        Map<String,Action> anonActionTmp = new HashMap<String, Action>();
        Map<String,Action> authcActionTmp = new HashMap<String, Action>();
        Map<String,Action> authoActionTmp = new HashMap<String, Action>();
        anonUriActionMap.clear();
        authcUriActionMap.clear();
        authoUriActionMap.clear();
        for (Resource resource : resources) {
            List<Action> actions = resource.getActions();
            for (Action action : actions) {
                if (FilterType.ANONYMOUS_FILTER.equals(action.getFilter())) {
                    anonActionTmp.put(action.getName(), action);
                    String uris[] = action.getUri().split(split);
                    for (String uri : uris) {
                        anonUriActionMap.put(uri, action);
                    }
                } else if (FilterType.AUTHENTICATE_FILTER.equals(action.getFilter())) {
                    authcActionTmp.put(action.getName(), action);
                    String uris[] = action.getUri().split(split);
                    for (String uri : uris) {
                        authcUriActionMap.put(uri, action);
                    }
                } else if (FilterType.AUTHORIZATION_FILTER.equals(action.getFilter())) {
                    authoActionTmp.put(action.getName(), action);
                    String uris[] = action.getUri().split(split);
                    for (String uri : uris) {
                        authoUriActionMap.put(uri, action);
                    }
                }
            }
        }
        this.anonActionCache = Collections.unmodifiableMap(anonActionTmp);
        this.authcActionCache = Collections.unmodifiableMap(authcActionTmp);
        this.authoActionCache = Collections.unmodifiableMap(authoActionTmp);
    }

    public Set<Action> getAction(String... names) {
        if (names != null && names.length > 0) {
            Set<Action> actions = new HashSet<Action>();
            for (String name : names) {
                Action action = anonActionCache.get(name);
                if (action != null) {
                    actions.add(action);
                    continue;
                }
                action = authcActionCache.get(name);
                if (action != null) {
                    actions.add(action);
                    continue;
                }
                action = authoActionCache.get(name);
                if (action != null) {
                    actions.add(action);
                }
            }
            return actions;
        }
        return Collections.emptySet();
    }

    public boolean isAnonAction(String action) {
        return anonActionCache.containsKey(action);
    }

    public boolean isAuthcAction(String action) {
        return authcActionCache.containsKey(action);
    }

    public Collection<Action> anonAction() {
        return anonActionCache.values();
    }

    public Collection<Action> authcAction() {
        return authcActionCache.values();
    }

    public Collection<Action> authoAction() {
        return authoActionCache.values();
    }

    public boolean isAnonUri(String uri) {
        return anonUriActionMap.containsKey(processUri(uri));
    }

    public boolean isAuthoUri(String uri) {
        return authoUriActionMap.containsKey(processUri(uri));
    }

    private String processUri(String uri) {
        if (uri.endsWith("/") && uri.length() > 1) {
            return uri.substring(0, uri.length() - 1);
        } else {
            return uri;
        }
    }
}
