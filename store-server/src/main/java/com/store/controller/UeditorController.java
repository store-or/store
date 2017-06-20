package com.store.controller;

import com.baidu.ueditor.ActionEnter;
import com.core.json.JsonMapper;
import com.core.json.JsonResponse;
import com.core.json.SJsonResponse;
import com.param.PortalConfigParam;
import com.store.domain.UeditorInfoDO;
import com.store.service.FileService;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Date;

/**
 * Created by wangmj on 2015/12/2.
 */
@Controller
@RequestMapping("/js/ueditor")
public class UeditorController extends BaseController {

    @Autowired
    private FileService fileService;

    @RequestMapping("/init")
    @ResponseBody
    public String init(HttpServletRequest request, HttpServletResponse response, String action) {
        try {
            if (StringUtils.equalsIgnoreCase(action, "uploadImage") || StringUtils.equalsIgnoreCase(action,"uploadFile")) {
                boolean isMultipart = ServletFileUpload.isMultipartContent(request);
                if (isMultipart){
                    request.getRequestDispatcher("/js/ueditor/ueditorUpload").forward(request,response);
                }
            } else {
                request.setCharacterEncoding( "utf-8" );
                response.setHeader("Content-Type" , "text/html");
                String rootPath = request.getSession().getServletContext().getRealPath("/");
                String exec = new ActionEnter(request, rootPath).exec();
                PrintWriter writer = response.getWriter();
                writer.write(exec);
                writer.flush();
                writer.close();
            }
            return null;
        } catch (Exception e) {
            logger.error("ueditor init error!");
            return new SJsonResponse(JsonResponse.CODE_FAILURE,e.getMessage()).toString();
        }
    }

    @RequestMapping("/ueditorUpload")
    @ResponseBody
    public String ueditorUpload(@RequestParam(value = "upfile", required = true) MultipartFile upfile,
                               HttpServletRequest request, HttpServletResponse response) {
        try {
            String fileName = upfile.getOriginalFilename();
            String newName = DateFormatUtils.format(new Date(), "yyyyMMddHHmmssSSS") + "." + StringUtils.substringAfterLast(fileName, ".");
            String url = fileService.uploadFile(upfile.getInputStream(), newName);
            UeditorInfoDO ueditorInfo = new UeditorInfoDO();
            ueditorInfo.setTitle(fileName);
            ueditorInfo.setOriginal(fileName);
            ueditorInfo.setSize(upfile.getSize());
            ueditorInfo.setState(UeditorInfoDO.STATE_SUCCESS);
            ueditorInfo.setType(upfile.getContentType());
            ueditorInfo.setUrl(PortalConfigParam.fileDownLoadUrl + url);
            return JsonMapper.getDefault().toJson(ueditorInfo);
        } catch (Exception e) {
            logger.error("ueditorUpload error!");
            return new JsonResponse(JsonResponse.CODE_FAILURE,e.getMessage()).toString();
        }
    }

}
