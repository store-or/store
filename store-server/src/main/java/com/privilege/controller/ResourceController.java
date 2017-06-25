package com.privilege.controller;

import com.core.controller.AbstractController;
import com.core.json.JsonResponse;
import com.core.system.App;
import com.core.util.ValidatorUtil;
import com.core.util.ZipUtil;
import com.privilege.security.ResourceCache;
import com.privilege.service.ResourceService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 */
@Controller
@RequestMapping("/privilege/resource")
public class ResourceController extends AbstractController {


    @Autowired
    private ResourceService resourceService;

    @RequestMapping(value = "/upload", produces = "text/plain;charset=utf-8")
    @ResponseBody
    public String upload(@RequestParam(value = "file", required = false) MultipartFile file) {
        List<ObjectError> list = new ArrayList<ObjectError>();
        if (file == null || file.isEmpty() || !file.getOriginalFilename().endsWith(".zip")) {
            list.add(new FieldError("", "file", "请选择zip文件"));
            return ValidatorUtil.errorToJson(JsonResponse.CODE_FAILURE, JsonResponse.MSG_SUCCESS, list).toString();
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String tmpDirPath =  SystemUtils.getJavaIoTmpDir().getAbsolutePath() + "/" + simpleDateFormat.format(new Date());
        try {
            File tmpZipFile = new File(tmpDirPath, file.getOriginalFilename());
            if (!tmpZipFile.exists()) {
                tmpZipFile.mkdirs();
            }
            file.transferTo(tmpZipFile);
            String tmpFilePath = tmpDirPath+"/"+file.getOriginalFilename().substring(0,file.getOriginalFilename().lastIndexOf("."));
            File tmpFile = new File(tmpFilePath);
            ZipUtil.decompress(tmpZipFile, tmpFile);
            resourceService.refresh(tmpFilePath);
            ResourceCache resourceCache = App.getBean(ResourceCache.class);
            resourceCache.refresh();
            FileUtils.deleteQuietly(tmpFile);
            return JsonResponse.JSON_SUCCESS;
        }  catch (Exception e) {
            FileUtils.deleteQuietly(new File(tmpDirPath));
            logger.error(e.getMessage(), e);
            return ValidatorUtil.errorToJson(JsonResponse.CODE_FAILURE, "导入失败", list).toString();
        }
    }
    @RequestMapping("/showUpload")
    public String showUpload() {
        return "/privilege/resource/upload";
    }

}
