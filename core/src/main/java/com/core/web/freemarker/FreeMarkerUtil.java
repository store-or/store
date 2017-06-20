package com.core.web.freemarker;

import com.core.json.JsonMapper;
import com.core.system.App;
import com.core.web.WebContext;
import com.core.web.constants.WebConstants;
import freemarker.template.Template;
import org.codehaus.jackson.node.ObjectNode;
import org.springframework.ui.Model;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.util.Map;

/**
 * User: laizy
 */
public final class FreeMarkerUtil {

    private FreeMarkerUtil() {
    }

    public static ObjectNode renderWithLightId(String name, Model model) throws FreemarkerParseException {
        try {
            return RenderUtil.FREEMARKER_RENDER.renderWithLightId(name, model);
        } catch (Exception e) {
            throw new FreemarkerParseException(e.getMessage(), e);
        }
    }

    public static ObjectNode render(String name, Model model) throws FreemarkerParseException {
        model.addAttribute(WebConstants.ABSOLUTE_CONTEXT_PATH, WebContext.getRequest().getAttribute(WebConstants.ABSOLUTE_CONTEXT_PATH));
        try {
            return RenderUtil.FREEMARKER_RENDER.render(name, model);
        } catch (Exception e) {
            throw new FreemarkerParseException(e.getMessage(), e);
        }
    }

    public static ObjectNode render(String name, Map<String, Object> map) throws FreemarkerParseException {
        String result = renderStr(name, map);
        ObjectNode node = JsonMapper.getDefault().getMapper().createObjectNode();
        node.put("data", result);
        return node;
    }

    public static String renderStr(String name, Map<String, Object> map) throws FreemarkerParseException {
        try {
            FreeMarkerConfigurer configurer = App.getBean(FreeMarkerConfigurer.class);
            Template template = configurer.getConfiguration().getTemplate(name + ".ftl");
            return FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
        } catch (Exception e) {
            throw new FreemarkerParseException(e.getMessage(), e);
        }
    }

}
