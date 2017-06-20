package com.core.controller;

import com.core.util.poi.PoiExcelHelper;
import com.core.util.poi.PoiExcelUtil;
import com.core.web.WebContext;
import com.core.web.constants.WebConstants;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 后台管理的基础控制器
 * Date: 12-10-29
 */
@Controller
public abstract class AbstractController {

    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    @Qualifier("validator")
    protected LocalValidatorFactoryBean validator ;

    @ModelAttribute(value = WebConstants.ABSOLUTE_CONTEXT_PATH)
    public String absoluteContextPath() {
        return (String) WebContext.getRequest().getAttribute(WebConstants.ABSOLUTE_CONTEXT_PATH);
    }

    protected void export(HttpServletRequest request ,HttpServletResponse response , String fileName , List<List<String>> data){
        Map<String,List<List<String>>> datas = new HashMap<String, List<List<String>>>();
        datas.put("sheet0" , data);
        PoiExcelUtil.exportExcel(request, response, fileName + "(" + DateFormatUtils.format(Calendar.getInstance(), "yyyyMMddHHmmss") + ")", datas);
    }

    protected void export(HttpServletRequest request ,HttpServletResponse response , String fileName , Map<String,List<List<String>>> data) throws IOException{
        setResponseHeader(request, response, fileName);
        PoiExcelHelper.getHelper(PoiExcelHelper.HSSF).export(response.getOutputStream() , data);
    }

    protected void setResponseHeader(HttpServletRequest request, HttpServletResponse response, String fileName) throws IOException {
       setResponseHeader(request, response, fileName, PoiExcelHelper.HSSF_EXTENSION);
    }
    protected void setResponseHeader(HttpServletRequest request, HttpServletResponse response, String fileName, String subfix) throws IOException {
        fileName = fileName + "(" + DateFormatUtils.format(Calendar.getInstance() , "yyyyMMddHHmmss") + ")" + "." + subfix;
        setFileExportHeader(request, response, fileName);
        //定义输出类型
        response.setContentType("APPLICATION/msexcel");
    }

    protected void setFileExportHeader(HttpServletRequest request, HttpServletResponse response, String fileName) throws IOException {
        String userAgent = request.getHeader("USER-AGENT");
        if (StringUtils.contains(userAgent, "MSIE") || StringUtils.contains(userAgent,"Trident")){
            //IE浏览器
            fileName = URLEncoder.encode(fileName, "UTF8");
        } else if (StringUtils.contains(userAgent, "Mozilla")) {
            //google,火狐浏览器
            fileName = new String(fileName.getBytes(), "ISO8859-1");
        } else {
            fileName = URLEncoder.encode(fileName, "UTF8");
        }
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
    }

}
