package com.core.dao;

import com.core.json.JsonMapper;
import org.apache.commons.lang.StringUtils;

import java.util.*;

/**
 * Created by laizy on 2017/6/7.
 */
public class PropertyFilterUtil {
    /**
     *
     * @param json :
     * @return :
     */
    public static List<PropertyFilter> buildPropertyFilters(final String json) {
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        if (!StringUtils.isBlank(json)) {
            filters = JsonMapper.getDefault().fromJson(json,JsonMapper.getDefault().constructParametricType(ArrayList.class,PropertyFilter.class));
            if (filters == null) {
                filters = new ArrayList<PropertyFilter>();
            } else {
                //处理in
                filters = mergePropertyFilter(filters);
            }
        }
        return filters;
    }

    public static List<PropertyFilter> mergePropertyFilter(List<PropertyFilter> filters) {
        if (filters == null) {
            filters = new ArrayList<PropertyFilter>();
        } else {
            //处理in
            Map<String,PropertyFilter> inMap = new HashMap<String, PropertyFilter>();
            for (Iterator<PropertyFilter> iterator = filters.iterator(); iterator.hasNext(); ) {
                PropertyFilter filter = iterator.next();
                if (filter.getMatchType() == PropertyFilter.MatchType.IN ||
                        filter.getMatchType() == PropertyFilter.MatchType.CONTAINS) {
                    iterator.remove();
                    PropertyFilter preFilter = inMap.get(filter.getPropertyName());
                    if (preFilter == null) {
                        List<Object> list = new ArrayList<Object>();
                        list.add(filter.getValue());
                        filter.setValue(list);
                        inMap.put(filter.getPropertyName(), filter);
                    } else {
                        @SuppressWarnings("unchecked")
                        List<Object> list = (List<Object>) preFilter.getValue();
                        list.add(filter.getValue());
                    }
                }
            }
            filters.addAll(inMap.values());
        }
        return filters;
    }
}
