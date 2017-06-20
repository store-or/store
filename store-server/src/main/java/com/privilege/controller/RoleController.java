package com.privilege.controller;

import com.core.controller.AbstractController;
import com.core.json.JsonMapper;
import com.core.json.JsonResponse;
import com.core.util.ValidatorUtil;
import com.core.util.collection.Tree;
import com.core.web.freemarker.FreeMarkerUtil;
import com.privilege.domain.Role;
import com.privilege.domain.User;
import com.privilege.security.SecurityContext;
import com.privilege.service.RoleService;
import org.codehaus.jackson.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 */
@SuppressWarnings("unchecked")
@Controller
@RequestMapping("/privilege/role")
public class RoleController extends AbstractController {
    public static final String MODEL_ATTR_TREE = "tree";
    @Autowired
    private RoleService roleService;

    @RequestMapping("/list")
    public String list(Model model) {
        Tree<RoleService.RoleNode> tree = roleService.listRole();
        model.addAttribute(MODEL_ATTR_TREE, tree);
        return "/privilege/role/list";
    }

    @RequestMapping("/ajaxList")
    public @ResponseBody
    String ajaxList(Model model) {
        try {
            String render = list(model);
            ObjectNode objectNode = FreeMarkerUtil.renderWithLightId(render, model);
            return new JsonResponse<ObjectNode>(JsonResponse.CODE_SUCCESS, "", objectNode).toString();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResponse.JSON_ERROR;
        }
    }

    @RequestMapping("/toAdd")
    @ResponseBody
    public String toAdd(Model model, @RequestParam Long id, String roleName) {
        Role parentRole = roleService.get(id);
        Role role = new Role();
        role.setParentPath(parentRole.getPath());
        model.addAttribute("role", role);
        Tree<RoleService.ActionNode> tree = roleService.listActionOfRole(id);
        model.addAttribute("roleId", id);
        model.addAttribute("roleName", roleName);
        model.addAttribute(MODEL_ATTR_TREE, tree);
        model.addAttribute("size", tree.size());
        try {
            ObjectNode render = FreeMarkerUtil.render("/privilege/role/pop_role", model);
            return new JsonResponse<ObjectNode>(JsonResponse.CODE_SUCCESS, "", render).toString();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new JsonResponse(JsonResponse.CODE_FAILURE, e.getMessage()).toString();
        }
    }

    @RequestMapping("/toModify")
    @ResponseBody
    public String toModify(Model model, @RequestParam Long id, String roleName) {
        Role role = roleService.get(id);
        model.addAttribute("role", role);
        Tree<RoleService.ActionNode> tree = roleService.listActionForRole(id);
        model.addAttribute("roleId", id);
        model.addAttribute("roleName", roleName);
        model.addAttribute(MODEL_ATTR_TREE, tree);
        model.addAttribute("size", tree.size());
        try {
            ObjectNode render = FreeMarkerUtil.render("/privilege/role/pop_role", model);
            return new JsonResponse<ObjectNode>(JsonResponse.CODE_SUCCESS, "", render).toString();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new JsonResponse(JsonResponse.CODE_FAILURE, e.getMessage()).toString();
        }
    }

    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public @ResponseBody String add(@Valid Role role, BindingResult bindingResult, String[] action) {
        try {
            if (bindingResult.hasErrors()) {
                JsonResponse jsonResponse = ValidatorUtil.errorToJson(JsonResponse.CODE_FAILURE, "", bindingResult);
                return jsonResponse.toString();
            }
            roleService.save(role, action);
            JsonResponse response = new JsonResponse(JsonResponse.CODE_SUCCESS, "添加角色成功！");
            return JsonMapper.getDefault().toJson(response);
        } catch (Exception e) {
            logger.error("添加角色失败，请检查", e);
            return new JsonResponse(JsonResponse.CODE_FAILURE, e.getMessage()).toString();
        }
    }

    @RequestMapping(value = "/modify",method = RequestMethod.POST)
    public @ResponseBody String modify(@Valid Role role, BindingResult bindingResult, String[] action){
        try {
            if (bindingResult.hasErrors()) {
                JsonResponse jsonResponse = ValidatorUtil.errorToJson(JsonResponse.CODE_FAILURE, "", bindingResult);
                return jsonResponse.toString();
            }
            roleService.modify(role, action);
            return JsonResponse.JSON_SUCCESS;
        } catch (Exception e) {
            logger.error("修改角色失败，请检查！",e);
        }
        return JsonResponse.JSON_ERROR;
    }

    @RequestMapping("/delete")
    public @ResponseBody String delete(Long id){
        try {
            roleService.delete(id);
            JsonResponse response = new JsonResponse(JsonResponse.CODE_SUCCESS,"删除角色成功!");
            return JsonMapper.getDefault().toJson(response);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new JsonResponse(JsonResponse.CODE_FAILURE, "删除角色失败！" + e.getMessage()).toString();
        }
    }

    @RequestMapping("/popUser")
    public @ResponseBody String popUser( Model model, Long roleId, String roleName){
        try {
            List<User> assignedUsers = new ArrayList<User>();
            List<User> unassignedUsers = new ArrayList<User>();
            roleService.listUserForRole(roleId, assignedUsers, unassignedUsers);
            model.addAttribute("roleId", roleId);
            model.addAttribute("roleName", roleName);
            model.addAttribute("users", unassignedUsers);
            ObjectNode node = FreeMarkerUtil.render("/privilege/role/pop_user", model);
            return new JsonResponse<ObjectNode>(JsonResponse.CODE_SUCCESS,"",node).toString();
        } catch (Exception e) {
            logger.error("popUser failed.", e);
            return new JsonResponse(JsonResponse.CODE_FAILURE, "选择用户列表时异常!").toString();
        }
    }

    @RequestMapping("/listUser")
    @ResponseBody
    public String listUser(Model model, Long roleId, String type){
        try {
            List<User> assignedUsers = new ArrayList<User>();
            List<User> unassignedUsers = new ArrayList<User>();
            roleService.listUserForRole(roleId, assignedUsers, unassignedUsers);
            if (type.equals("assigned")){
                model.addAttribute("users", assignedUsers);
            }else {
                model.addAttribute("users", unassignedUsers);
            }
            ObjectNode node = FreeMarkerUtil.render("/privilege/role/assignUserTable", model);
            return new JsonResponse<ObjectNode>(JsonResponse.CODE_SUCCESS,"",node).toString();
        } catch (Exception e) {
            logger.error("listUser failed.", e);
            return new JsonResponse(JsonResponse.CODE_FAILURE, "选择用户列表时异常!").toString();
        }
    }

    @RequestMapping("/assignUser")
    public @ResponseBody String assignUser(Long[] userId,Long roleId,@RequestParam(defaultValue = "") String type){
        try {
            if (type.equals("assign")) {
                roleService.assignUser(userId, roleId);
            } else {
                roleService.unassignUser(userId, roleId);
            }
            return JsonResponse.JSON_SUCCESS;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new JsonResponse(JsonResponse.CODE_FAILURE, "分配用户失败").toString();
        }
    }

    @RequestMapping("/popResource")
    @ResponseBody
    public String popResource(Model model){
        try {
            Tree<RoleService.ActionNode> tree = roleService.listActionOfRole(SecurityContext.ROLE_SUPERUSER_ID);
            model.addAttribute(MODEL_ATTR_TREE, tree);
            ObjectNode node = FreeMarkerUtil.render("/privilege/role/pop_resource", model);
            return new JsonResponse<ObjectNode>(JsonResponse.CODE_SUCCESS,"",node).toString();
        } catch (Exception e) {
            logger.error("popResource failed.", e);
            return new JsonResponse(JsonResponse.CODE_FAILURE, "提取权限列表时异常!").toString();
        }
    }

    @RequestMapping("/listResource")
    @ResponseBody
    public String listResource(Model model){
        try {
            Tree<RoleService.ActionNode> tree = roleService.listActionOfRole(SecurityContext.ROLE_SUPERUSER_ID);
            model.addAttribute(MODEL_ATTR_TREE, tree);
            ObjectNode node = FreeMarkerUtil.render("/privilege/role/resource", model);
            return new JsonResponse<ObjectNode>(JsonResponse.CODE_SUCCESS,"",node).toString();
        } catch (Exception e) {
            logger.error("listResource failed.", e);
            return new JsonResponse(JsonResponse.CODE_FAILURE, "提取权限列表时异常!").toString();
        }
    }
}
