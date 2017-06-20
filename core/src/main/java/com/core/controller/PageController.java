package com.core.controller;

import com.core.dao.Page;
import com.core.json.JsonResponse;
import com.core.web.freemarker.RenderUtil;
import org.codehaus.jackson.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by laizy on 2017/6/7.
 */
@Controller
@RequestMapping("page")
public class PageController {

    private Logger logger = LoggerFactory.getLogger(PageController.class);

    @RequestMapping
    @ResponseBody
    public String reset(@ModelAttribute Page page, Model model) {
        try {
            ObjectNode objectNode = RenderUtil.FREEMARKER_RENDER.renderWithLightId("page", model);
            JsonResponse<ObjectNode> jsonResponse = new JsonResponse<ObjectNode>(JsonResponse.CODE_SUCCESS, "success", objectNode);
            return jsonResponse.toString();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResponse.JSON_ERROR;
        }
    }
}
