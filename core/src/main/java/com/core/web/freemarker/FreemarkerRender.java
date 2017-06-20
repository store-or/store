package com.core.web.freemarker;

import com.core.json.JsonMapper;
import com.core.system.App;
import com.core.web.WebContext;
import freemarker.ext.servlet.FreemarkerServlet;
import freemarker.ext.servlet.HttpRequestHashModel;
import freemarker.ext.servlet.IncludePage;
import freemarker.ext.servlet.ServletContextHashModel;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.node.ObjectNode;
import org.springframework.ui.Model;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.StringWriter;
import java.util.Map;

/**
 * Created by laizy on 2017/6/7.
 */
public class FreemarkerRender extends RenderUtil {

    public ObjectNode renderWithLightId(String name, Model model) throws Exception {
        String lightTableId = WebContext.getRequest().getParameter("lightId");
        if (StringUtils.isBlank(lightTableId)) {
            return null;
        }
        //render html
        FreeMarkerConfigurer configurer =  App.getBean(FreeMarkerConfigurer.class);
        setModelInfo(model);
        Template template =  configurer.getConfiguration().getTemplate( name + ".ftl");
        StringWriter writer =new StringWriter();
        template.process(model,writer);
        String data = between(writer.toString(),lightTableId);
        ObjectNode node = JsonMapper.getDefault().getMapper().createObjectNode();
        Map<String,Object> map = model.asMap();
        node.put("order",(String) map.get("order"));
        node.put("orderBy",(String) map.get("orderBy"));
        node.put("data",data);
        return node;
    }

    public ObjectNode render(String name,Model model) throws Exception{
        FreeMarkerConfigurer configurer =  App.getBean(FreeMarkerConfigurer.class);
        setModelInfo(model);
        Template template =  configurer.getConfiguration().getTemplate( name + ".ftl");
        StringWriter writer =new StringWriter();
        template.process(model,writer);
        String result = writer.toString();
        ObjectNode node = JsonMapper.getDefault().getMapper().createObjectNode();
        node.put("data",result);
        return node;
    }

    private void setModelInfo(Model model) {
        if (WebContext.getRequest() != null) {
            ServletContext servletContext = WebContext.getRequest().getSession().getServletContext();
            model.addAttribute(FreemarkerServlet.KEY_REQUEST,WebContext.getRequest()) ;
            model.addAttribute(FreemarkerServlet.KEY_INCLUDE,new IncludePage(WebContext.getRequest(), WebContext.getResponse())) ;
            model.addAttribute(FreemarkerServlet.KEY_REQUEST_PRIVATE,(HttpRequestHashModel) WebContext.getRequest().getAttribute(".freemarker.Request")) ;
            model.addAttribute(FreemarkerServlet.KEY_REQUEST_PARAMETERS,servletContext.getAttribute(FreemarkerServlet.KEY_REQUEST_PARAMETERS)) ;
            model.addAttribute(FreemarkerServlet.KEY_SESSION,WebContext.getRequest().getSession()) ;
            model.addAttribute(FreemarkerServlet.KEY_APPLICATION,servletContext.getAttribute(FreemarkerServlet.KEY_APPLICATION)) ;
            model.addAttribute(FreemarkerServlet.KEY_APPLICATION_PRIVATE,servletContext.getAttribute(FreemarkerServlet.KEY_APPLICATION_PRIVATE)) ;
            FreeMarkerConfigurer bean = App.getBean(FreeMarkerConfigurer.class);
            if (bean.getTaglibFactory() == null) {
                System.out.println("tag lib factorty null");
            }
            model.addAttribute(FreemarkerServlet.KEY_JSP_TAGLIBS,bean.getTaglibFactory()) ;
            HttpServletRequest request  = WebContext.getRequest();
            ServletContextHashModel servletContextModel =(ServletContextHashModel) servletContext.getAttribute(".freemarker.Application");
            model.addAttribute(".freemarker.Application", servletContextModel);
            model.addAttribute("__FreeMarkerServlet.Application__",servletContextModel);
            HttpRequestHashModel requestModel = new HttpRequestHashModel(request, WebContext.getResponse(), ObjectWrapper.DEFAULT_WRAPPER);
            model.addAttribute("__FreeMarkerServlet.Request__", requestModel);
        }
    }



}
