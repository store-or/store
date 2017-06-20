package com.core.system;

import com.core.annotation.Env;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by laizy on 2017/6/7.
 */
public class AppChain implements ApplicationContextAware {

    private ApplicationContext applicationContext;
    private List<App> apps = new ArrayList<App>();
    private Logger logger = LoggerFactory.getLogger(AppChain.class);

    public void addApp(App app) {
        apps.add(app);
    }

    public List<App> getApps() {
        return apps;
    }

    public void setApps(List<App> apps) {
        this.apps = apps;
    }

    @PostConstruct
    private void init() {
        try {
            App.context = this.applicationContext;
            Map<String,Object> jobTypeMap = App.getBeansWithAnnotation(Env.class);
            for (Object o : jobTypeMap.values()) {
                Class<?> c = o.getClass();
                Env env = c.getAnnotation(Env.class);
                App.table.put(env.name(), env.superClass(), o);
            }
            List<String> basePackages = new ArrayList<String>();
            for (App app : apps) {
                if (!StringUtils.isBlank(app.basePackage)) {
                    basePackages.add(app.basePackage);
                }
            }
            if (basePackages.size() > 0) {
                App.reflections = new Reflections(basePackages);
            }

            for (App app : apps) {
                app.contextInitialize();
            }
        } catch (Throwable throwable) {
            logger.error(throwable.getMessage(), throwable);
            System.exit(1);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
