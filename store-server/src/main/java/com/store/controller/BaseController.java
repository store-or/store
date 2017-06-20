package com.store.controller;

import com.core.controller.AbstractController;
import com.core.dao.RecordDO;
import com.core.json.JsonResponse;
import com.core.util.ValidatorUtil;
import com.core.web.freemarker.FreeMarkerUtil;
import com.core.web.freemarker.FreemarkerEnumLoader;
import com.core.web.freemarker.FreemarkerParseException;
import com.param.PortalConfigParam;
import com.privilege.security.SecurityContext;
import org.codehaus.jackson.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Created by laizy on 2017/6/7.
 */
public abstract class BaseController extends AbstractController {
    @Autowired
    private FreemarkerEnumLoader freemarkerEnumLoader;

    @ModelAttribute(value="uploadFileContext")
    public String uploadFileContext(){
        return PortalConfigParam.fileDownLoadUrl;
    }

    @ModelAttribute
    public void addEnumToFreemarker(Model model) {
        freemarkerEnumLoader.registerToModel(model);
    }

    protected String jsonResponseView(String view, Model model) throws FreemarkerParseException {
        return new JsonResponse<ObjectNode>(JsonResponse.CODE_SUCCESS, JsonResponse.MSG_SUCCESS, FreeMarkerUtil.render(view, model)).toString();
    }

    protected String jsonValidate(BindingResult result) {
        return ValidatorUtil.errorToJson(JsonResponse.CODE_FAILURE, JsonResponse.MSG_ERROR, result).toString();
    }

    protected void setDefault(RecordDO recordDO) {
        setCreate(recordDO);
        setModify(recordDO);
    }

    protected void setCreate(RecordDO recordDO) {
        recordDO.setCreateTime(System.currentTimeMillis());
        recordDO.setCreateUser(SecurityContext.getUserName());
    }

    protected void setModify(RecordDO recordDO) {
        recordDO.setModifyTime(System.currentTimeMillis());
        recordDO.setModifyUser(SecurityContext.getUserName());
    }
}
