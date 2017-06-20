package com.core.dao;

import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by laizy on 2017/6/7.
 */
public final class HibernateTypeExchange {
    private HibernateTypeExchange() {
    }

    static final Map<String,Type> cache = new HashMap<String, Type>();
    static {
        cache.put(PropertyFilter.STRING_TYPE, StandardBasicTypes.STRING);
        cache.put(PropertyFilter.LONG_TYPE, StandardBasicTypes.LONG);
        cache.put(PropertyFilter.INTEGER_TYPE, StandardBasicTypes.INTEGER);
        cache.put(PropertyFilter.SHORT_TYPE, StandardBasicTypes.SHORT);
        cache.put(PropertyFilter.FLOAT_TYPE, StandardBasicTypes.FLOAT);
        cache.put(PropertyFilter.DOUBLE_TYPE, StandardBasicTypes.DOUBLE);
        cache.put(PropertyFilter.TEXT_TO_ARRAY, StringToArrayType.textToArray);
        cache.put(PropertyFilter.VARCHAR_TO_ARRAY, StringToArrayType.varcharToArray);
        cache.put(PropertyFilter.BOOLEAN_TYPE, StandardBasicTypes.BOOLEAN);
    }


    public static Type exchange(String propertyFilterType) {
        if (propertyFilterType.startsWith(PropertyFilter.DATE_START_TYPE)) {
            return StandardBasicTypes.DATE;
        } else {
            return cache.get(propertyFilterType);
        }
    }
}

