package com.param;

import com.core.controller.AbstractController;
import com.core.json.JsonResponse;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: hongxingshi
 * Date: 14-7-18
 * Time: 下午1:33
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/param")
public class PortalParamController extends AbstractController {

    @Autowired
    private PortalParamService portalParamService;
    @Autowired
    private PortalParamManage portalParamManage;


    @RequestMapping("/list")
    public String list(ModelMap modelMap) {
        List<PortalParam> parcms = portalParamService.find(Restrictions.eq("isSystem", 1));
        ListMultimap<String,PortalParam> paramMap = ArrayListMultimap.create();
        for (PortalParam param : parcms) {
            paramMap.put(param.getType(), param);
        }
        modelMap.addAttribute("paramMap", paramMap);
        return "/param/list";
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST)
    public String add(@RequestParam(value = "id") Long id,
                      @RequestParam(value = "value") String value) {
        if (id != null) {
            boolean success = portalParamManage.modify(id, value);
            if (success) {
                return JsonResponse.JSON_SUCCESS;
            }
        }
        return JsonResponse.JSON_ERROR;
    }

}