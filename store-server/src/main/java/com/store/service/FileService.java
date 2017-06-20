package com.store.service;

import com.param.PortalConfigParam;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by laizy on 2017/6/9.
 */
@Service
public class FileService {
    // 静态资源服务器保存cms图片资源的地址
    public static final String FILE_UPLOAD_DIR = "/store/";

    public void deleteFile(String fileName) {
        FileUtils.deleteQuietly(new File(PortalConfigParam.fileOptDir + FILE_UPLOAD_DIR, fileName));
    }

    public void deleteFiles(List<String> fileNames)  {
        if(CollectionUtils.isEmpty(fileNames)){
            return;
        }
        for(String fileName : fileNames){
            deleteFile(fileName);
        }
    }

    public String uploadFile(InputStream is, String fileName) throws IOException {
        File file = new File(PortalConfigParam.fileOptDir + FILE_UPLOAD_DIR, fileName);
        FileUtils.copyInputStreamToFile(is, file);
        return FILE_UPLOAD_DIR + fileName;
    }
}
