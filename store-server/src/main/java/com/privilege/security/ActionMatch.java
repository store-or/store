package com.privilege.security;

import com.privilege.domain.Action;
import com.privilege.service.resource.template.MethodType;
import com.privilege.service.resource.template.Parameter;
import com.privilege.service.resource.template.Parameters;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

/**
 */
public final class ActionMatch {

    private static final String anyValue = "*";

    private ActionMatch(){}

    public static boolean match(HttpServletRequest request, Action action) {

        //判断method
        if (!MethodType.ANY.equals(action.getMethod()) && !action.getMethod().equalsIgnoreCase(request.getMethod())) {
            return false;
        }
        //判断参数
        Parameters parameters = action.getParameters();
        if (parameters != null) {
            Set<Parameter> includes = parameters.getIncludes();
            if (includes != null) {
                for (Parameter include : includes) {
                    if (!includeMatch(request, include)) {
                        return false;
                    }
                }
            }
            Set<Parameter> excludes = parameters.getExcludes();
            if (excludes != null) {
                for (Parameter exclude : excludes) {
                    if (!excludeMatch(request, exclude)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private static boolean includeMatch(HttpServletRequest request, Parameter include) {
        String value = request.getParameter(include.getName());
        if (value != null) {
            //value值不相等，返回false
            if (!anyValue.equals(include.getValue()) && !value.equals(include.getValue())) {
                return false;
            }
        } else {
            //不存在该参数，返回false
            return false;
        }
        return true;
    }

    private static boolean excludeMatch(HttpServletRequest request, Parameter exclude) {
        String value = request.getParameter(exclude.getName());
        if (value != null) {
            //value值相等 返回false
            if (anyValue.equals(exclude.getValue()) || value.equals(exclude.getValue())) {
                return false;
            }
        }
        return true;
    }
}
