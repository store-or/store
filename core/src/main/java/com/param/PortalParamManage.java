package com.param;

import com.core.annotation.ConfigParam;
import com.core.annotation.Param;
import com.core.param.ParamManage;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 */
@Component
public class PortalParamManage extends ParamManage<Long> {
    @Autowired
    private PortalParamService portalParcmservice;

    @Autowired
    private ApplicationContext applicationContext;


    @Override
    @Transactional
    public void init() {
        List<PortalParam> parcms = portalParcmservice.find();
        Table<String,String,PortalParam> table = HashBasedTable.create();
        for (PortalParam param : parcms) {
            table.put(param.getType(), param.getName(), param);
        }
        List<PortalParam> toAdd = new ArrayList<PortalParam>();
        Map<String,String> configParcms = new HashMap<String, String>();
        Map<String, Object> beanMap = applicationContext.getBeansWithAnnotation(ConfigParam.class);
        for (Object o : beanMap.values()) {
            ConfigParam configParam = o.getClass().getAnnotation(ConfigParam.class);
            Field[] fields = o.getClass().getFields();
            for (Field field : fields) {
                Param paramA = field.getAnnotation(Param.class);
                if (paramA != null) {
                    String name = paramA.key();
                    PortalParam portalParam = table.get(configParam.type(),name);
                    try {
                        if (portalParam != null) {
                            table.remove(configParam.type(), name);
                            invokeModify(field, portalParam.getValue(), o, paramA);
                            configParcms.put(portalParam.getField() , portalParam.getValue());
                        } else {
                            PortalParam param = new PortalParam();
                            param.setName(name);
                            param.setField(o.getClass().getName() + "." + field.getName());
                            param.setValue(field.get(o).toString());
                            param.setDescription(paramA.description());
                            param.setType(configParam.type());
                            param.setIsSystem(configParam.system());
                            toAdd.add(param);
                            configParcms.put(param.getField() , param.getValue());
                        }
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                        throw new IllegalArgumentException(e);
                    }
                }
            }

        }
        portalParcmservice.batchSave(toAdd);
        for (PortalParam param : table.values()) {
            logger.info("delete param type{}, name:{}, value:{}, description:{}", new Object[]{param.getType(), param.getName(), param.getValue(), param.getDescription()});
            portalParcmservice.delete(param);
        }

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean modify(Long id,String value) {
        PortalParam configParam = portalParcmservice.get(id);
        if (configParam != null && !StringUtils.equals(value , configParam.getValue())) {
            configParam.setValue(value);
            portalParcmservice.saveOrUpdate(configParam);
            invokeModify(configParam.getField(), value);
        }
        return false ;
    }

    public void invokeModify(String fieldKey,String value){
        int index = StringUtils.lastIndexOf(fieldKey , ".");
        if (index != -1) {
            String className = fieldKey.substring(0, index);
            String fieldName = fieldKey.substring(index+1);
            Map<String, Object> beanMap = applicationContext.getBeansWithAnnotation(ConfigParam.class);
            for (Object o : beanMap.values()) {
                if (o.getClass().getName().equals(className)) {
                    try {
                        Field field = o.getClass().getField(fieldName);
                        Param paramA = field.getAnnotation(Param.class);
                        invokeModify(field, value, o, paramA);
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                }
            }
        }
    }

    @Transactional
    public void modifyParams(List<PortalParam> params) {
        for (PortalParam param : params) {
            modify(param.getId(), param.getValue());
        }
    }

    public <T> Map<String, PortalParam> mapByKey(Class<T> clazz) {
        ConfigParam configParam = clazz.getAnnotation(ConfigParam.class);
        Assert.notNull(configParam, "非配置参数类");
        return portalParcmservice.mapByKeyForType(configParam.type());
    }
}
