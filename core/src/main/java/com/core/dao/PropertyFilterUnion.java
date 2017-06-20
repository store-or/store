package com.core.dao;

import java.util.List;

/**
 * Created by laizy on 2017/6/7.
 */
public class PropertyFilterUnion extends PropertyFilter {

    public static final String OR = "or";
    public static final String AND = "and";

    private List<PropertyFilter> propertyFilters;
    private String[] unions;

    public void addPropertyFilter(PropertyFilter propertyFilter, String union) {

    }
}

