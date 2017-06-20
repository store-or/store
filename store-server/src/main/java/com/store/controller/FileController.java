package com.store.controller;

import com.core.controller.AbstractController;
import com.core.json.JsonResponse;
import com.param.PortalConfigParam;
import com.store.domain.FileInfo;
import com.store.service.FileService;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 */
@Controller
@RequestMapping("/file")
public class FileController extends AbstractController {
    @Autowired
    private FileService fileService;

    @ResponseBody
    @RequestMapping(value = "upload" )
    public String upload(@RequestParam(value = "file") MultipartFile[] files,Integer total) throws IOException {
        int count = 0 ;
        int max = (Integer) ObjectUtils.defaultIfNull(total, 1);
        List<FileInfo> fileInfoList = new LinkedList<FileInfo>();
        for(MultipartFile file : files){
            FileInfo fileInfo = upload(file);
            if(fileInfo == null){
                continue;
            }
            fileInfoList.add(fileInfo);
            if(max <= ++count){
                break;
            }
        }
        return new JsonResponse(JsonResponse.CODE_SUCCESS, "", fileInfoList).toString();
    }

    private FileInfo upload(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        fileName = DateFormatUtils.format(new Date(), "yyyyMMddHHmmssSSS") + "." + StringUtils.substringAfterLast(fileName, ".");
        String url = fileService.uploadFile(file.getInputStream(), fileName);
        FileInfo fileInfo = new FileInfo();
        fileInfo.setUrl(url);
        fileInfo.setVisitUrl(PortalConfigParam.fileDownLoadUrl + url);
        return fileInfo;
    }
}