package com.core.util;

import com.core.json.JsonMapper;
import com.core.json.JsonResponse;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import javax.validation.ConstraintViolation;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created by laizy on 2017/6/7.
 */
public final class ValidatorUtil {
    private static Logger logger = LoggerFactory.getLogger(ValidatorUtil.class);

    private ValidatorUtil() {
    }

    public static JsonResponse errorToJson(int returnCode, String message, List<ObjectError> errors) {
        ObjectMapper mapper = JsonMapper.getDefault().getMapper();
        ArrayNode array = mapper.createArrayNode();
        for (ObjectError error : errors) {
            ObjectNode node = mapper.createObjectNode();
            if (error instanceof FieldError) {
                FieldError fieldError = (FieldError) error;
                node.put(fieldError.getField(), error.getDefaultMessage());
            } else {
                node.put(error.getObjectName(), error.getDefaultMessage());
            }
            array.add(node);
        }
        return new JsonResponse<ArrayNode>(returnCode, message, array);
    }

    public static JsonResponse errorToJson(int returnCode, String message, BindingResult result){
        ObjectMapper mapper = JsonMapper.getDefault().getMapper();
        ArrayNode array = mapper.createArrayNode();
        for (ObjectError error : result.getAllErrors()) {
            ObjectNode node = mapper.createObjectNode();
            if (error instanceof FieldError) {
                FieldError fieldError = (FieldError) error;
                if(fieldError.isBindingFailure()){
                    try{
                        PropertyDescriptor propertyDescriptor = new PropertyDescriptor(((FieldError) error).getField(),result.getTarget().getClass());
                        Annotation[] annotations =   propertyDescriptor.getReadMethod().getAnnotations();
                        node.put(fieldError.getField(),annotations[0].getClass().getDeclaredMethod("message").invoke(annotations[0]).toString());
                    }catch(Exception exc){
                        logger.error(exc.getMessage(),exc);
                        //node.put(fieldError.getField(), error.getDefaultMessage());
                    }
                }else{
                    node.put(fieldError.getField(), error.getDefaultMessage());
                }
            } else {
                node.put(error.getObjectName(), error.getDefaultMessage());
            }
            array.add(node);
        }
        return new JsonResponse<ArrayNode>(returnCode, message, array);

    }

    /**
     * 验证密码强度是否符合要求 (数字、大小写字母和特殊字符(支持的特殊字符~!@#$.%^&*-+_=)的组合，至少8位)
     *
     * @param content
     * @return
     */
    public static boolean checkPasswordComplexity(String content) {
        String regex = "(?=^.{8,}$)(?=.*[.~!@#$%^&*\\-+_=])(?![.\\n])(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9]).*$";
        return isMatchRegex(regex, content);
    }

    /**
     * 根据正则表达式来验证内容是否符合
     *
     * @param regex
     * @param content
     * @return
     */
    public static boolean isMatchRegex(String regex, String content) {
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(content).matches();
    }

    public static <T> String firstErrorString(Class<T> clazz, Errors errors) {
        if (clazz == null || !errors.hasErrors()) {
            return "";
        }
        Field[] fields = clazz.getDeclaredFields();
        if (ArrayUtils.isEmpty(fields)) {
            return "";
        }
        for (Field field : fields) {
            FieldError fieldError = errors.getFieldError(field.getName());
            if (fieldError != null) {
                return fieldError.getDefaultMessage();
            }
        }
        return "";
    }

    public static <T> String firstErrorString(Class<T> clazz, Set<ConstraintViolation<T>> violations) {
        if (clazz == null || CollectionUtils.isEmpty(violations)) {
            return "";
        }
        Field[] fields = clazz.getDeclaredFields();
        if (ArrayUtils.isEmpty(fields)) {
            return "";
        }
        for (Field field : fields) {
            String name = field.getName();
            for (ConstraintViolation<T> violation : violations) {
                if (StringUtils.equals(violation.getPropertyPath().toString(), name)) {
                    return violation.getMessage();
                }
            }
        }
        return "";
    }
}
