package com.privilege.controller;

import com.core.controller.AbstractController;
import com.core.dao.Page;
import com.core.json.JsonMapper;
import com.core.json.JsonResponse;
import com.core.util.ValidatorUtil;
import com.core.web.freemarker.FreeMarkerUtil;
import com.privilege.domain.User;
import com.privilege.security.Principal;
import com.privilege.security.SecurityContext;
import com.privilege.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: hongxingshi
 * Date: 14-7-17
 * Time: 下午2:01
 * To change this template use File | Settings | File Templates.
 */
@SuppressWarnings("unchecked")
@RequestMapping("/privilege/user")
public @Controller
class UserController extends AbstractController {
    @Autowired
    private UserService userService;

    @RequestMapping("/list")
    public String list(Model model) {
        List<User> users = userService.listWithoutSuperuser();
        Page<User> page = new Page<User>();
        page.setTotalCount(users.size());
        page.setResult(users);
        model.addAttribute("page", page);
        model.addAttribute("currentUser", SecurityContext.getPrincipal());
        return "/privilege/user/list";
    }

    @RequestMapping("/ajaxList")
    public @ResponseBody
    String ajaxList(Model model) {
        try {
            String render = list(model);
            ObjectNode node = FreeMarkerUtil.renderWithLightId(render, model);
            JsonResponse response = new JsonResponse<ObjectNode>(JsonResponse.CODE_SUCCESS, "添加用户成功！", node);
            return JsonMapper.getDefault().toJson(response);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResponse.JSON_ERROR;
        }
    }

    @RequestMapping("/toAdd")
    @ResponseBody
    public String toAdd(Model model) {
        User user = new User();
        model.addAttribute("user",user);
        try {
            ObjectNode objectNode = FreeMarkerUtil.render("/privilege/user/form", model);
            return new JsonResponse<ObjectNode>(JsonResponse.CODE_SUCCESS, "", objectNode).toString();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new JsonResponse(JsonResponse.CODE_FAILURE, e.getMessage()).toString();
        }
    }

    @RequestMapping("/toModify")
    @ResponseBody
    public String toModify(@RequestParam Long id, Model model) {
        if (SecurityContext.SUPERUSER_ID.equals(id)) {
            return new JsonResponse(JsonResponse.CODE_SUCCESS, "您正尝试修改超管用户，不允许次行为").toString();
        }
        User user = userService.get(id);
        model.addAttribute("user", user);
        try {
            ObjectNode objectNode = FreeMarkerUtil.render("/privilege/user/form", model);
            return new JsonResponse<ObjectNode>(JsonResponse.CODE_SUCCESS, "", objectNode).toString();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new JsonResponse(JsonResponse.CODE_FAILURE, e.getMessage()).toString();
        }
    }

    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public @ResponseBody String save(@Valid User user, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                return ValidatorUtil.errorToJson(JsonResponse.CODE_FAILURE, "", bindingResult).toString();
            }
            if (!userService.isPropertyUnique("loginName", user.getLoginName(), null)) {
                bindingResult.addError(new FieldError("user", "loginName", "登录名重复了"));
            }
            if (bindingResult.hasErrors()) {
                return ValidatorUtil.errorToJson(JsonResponse.CODE_FAILURE, "", bindingResult).toString();
            }
            userService.persist(user);
            return JsonResponse.JSON_SUCCESS;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new JsonResponse(JsonResponse.CODE_FAILURE, e.getMessage()).toString();
        }
    }

    @RequestMapping(value = "/modify",method = RequestMethod.POST)
    public @ResponseBody String modify(Long id,String trueName,@RequestParam(defaultValue = User.USER_ENABLE+"")Integer status,String email) {

        try {
            if (StringUtils.isBlank(trueName)){
                throw new IllegalArgumentException("真实姓名不能为空");
            }
            Pattern pattern = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
            if (!pattern.matcher(email).matches()){
                throw new IllegalArgumentException("邮箱格式不符合");
            }
            userService.modify(trueName, email, status, id);
            return JsonResponse.JSON_SUCCESS;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new JsonResponse(JsonResponse.CODE_FAILURE, e.getMessage()).toString();
        }
    }

    @RequestMapping(value = "/toModifySuperPassword")
    public String toModifySuperPassword(Model model , HttpServletResponse response)throws IOException{
        Principal principal = SecurityContext.getPrincipal();
        if (!SecurityContext.SUPERUSER_ID.equals(principal.getId())) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
        return "/privilege/user/modifySuperPassword";
    }

    @RequestMapping("/modifySelfPassword")
    @ResponseBody
    public String modifySelfPassword(String password, String confirmPassword) {
        return modifyPassword(SecurityContext.getPrincipal().getId(), password, confirmPassword);
    }

    @ResponseBody
    @RequestMapping(value = "/toModifyPassword")
    public String toModifyPassword(@RequestParam Long id, Model model) {
        model.addAttribute("id", id);
        try {
            return new JsonResponse(JsonResponse.CODE_SUCCESS, "SUCCESS", FreeMarkerUtil.render("/privilege/user/modifyPassword", model)).toString();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResponse.JSON_ERROR;
        }
    }

    @RequestMapping("/modifyPassword")
    @ResponseBody
    public String modifyPassword(@RequestParam Long id, String password, String confirmPassword) {
        try{
            if(!ValidatorUtil.checkPasswordComplexity(password)){
                return new JsonResponse(JsonResponse.CODE_FAILURE, "数字、大小写字母和特殊字符组成,至少8位").toString();
            }
            if (!password.equals(confirmPassword)) {
                return new JsonResponse(JsonResponse.CODE_FAILURE, "密码不一致").toString();
            }
            userService.modifyPassword(id, password);
            return JsonResponse.JSON_SUCCESS;
        } catch (Exception e){
            logger.error(e.getMessage());
            return new JsonResponse(JsonResponse.CODE_FAILURE, e.getMessage()).toString();
        }
    }

    @RequestMapping("/modifySuperPassword")
    @ResponseBody
    public String modifySuperPassword(String password, String confirmPassword  , HttpServletResponse response) {
        try{
            Principal principal = SecurityContext.getPrincipal();
            if (!SecurityContext.SUPERUSER_ID.equals(principal.getId())) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
            if(!ValidatorUtil.checkPasswordComplexity(password)){
                return new JsonResponse(JsonResponse.CODE_FAILURE, "数字、大小写字母和特殊字符组成,至少8位").toString();
            }
            if (!password.equals(confirmPassword)) {
                return new JsonResponse(JsonResponse.CODE_FAILURE, "密码不一致").toString();
            }
            userService.changePassword(principal.getId(), password);
            return JsonResponse.JSON_SUCCESS;
        } catch (Exception e){
            logger.error(e.getMessage());
            return new JsonResponse(JsonResponse.CODE_FAILURE, e.getMessage()).toString();
        }
    }

    @RequestMapping(value = "/del", method = RequestMethod.POST)
    @ResponseBody
    public String remove(@RequestParam Long id) {
        try {
            userService.delete(id);
            return JsonResponse.JSON_SUCCESS;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new JsonResponse(JsonResponse.CODE_FAILURE, e.getMessage()).toString();
        }
    }

}