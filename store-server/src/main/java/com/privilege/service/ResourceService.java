package com.privilege.service;

import com.core.dao.TransactionBaseService;
import com.core.util.JaxbMapper;
import com.privilege.domain.Action;
import com.privilege.domain.Resource;
import com.privilege.service.resource.template.ActionTemplate;
import com.privilege.service.resource.template.ResourceTemplate;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 */
@Service
public class ResourceService extends TransactionBaseService<Resource,Long> {

    @Autowired
    private ActionService actionService;

    private void findAllXmlFile(List<File> fileList,File dir){
        if(dir.isDirectory()){
            for(File file:dir.listFiles()){
                if(file.isDirectory()){
                    findAllXmlFile(fileList,file);
                }else {
                    if(file.getName().endsWith(".xml")){
                        fileList.add(file);
                    }
                }
            }
        }else {
            if(dir.getName().endsWith(".xml")){
                fileList.add(dir);
            }
        }
    }

    @Transactional
    public void refresh(String path) throws IOException {
        Map<String,ResourceTemplate> resourceTemplates = new HashMap<String, ResourceTemplate>();
        if (StringUtils.isNotEmpty(path)) {
            File dir = new File(path);
            ArrayList<File> resourceFileList = new ArrayList<File>();
            findAllXmlFile(resourceFileList,dir);
            if (resourceFileList != null && resourceFileList.size() > 0) {
                JaxbMapper jaxbMapper = new JaxbMapper(ResourceTemplate.class);
                for (File resource : resourceFileList) {
                    FileInputStream fis = new FileInputStream(resource);
                    try {
                        ResourceTemplate resourceTemplate = jaxbMapper.fromXml(fis);
                        resourceTemplates.put(resourceTemplate.getName(), resourceTemplate);
                    } finally {
                        fis.close();
                    }
                }
            }
            if (resourceTemplates.size() > 0) {
                //删除原配置
                executeHql("delete from Action");
                executeHql("delete from Resource");

                for (ResourceTemplate resourceTemplate : resourceTemplates.values()) {
                    Resource resource = new Resource(resourceTemplate.getName(), resourceTemplate.path(resourceTemplates),resourceTemplate.getFetchClass(), resourceTemplate.getDescription());
                    saveOrUpdate(resource);
                    List<Action> actions = new ArrayList<Action>();
                    List<ActionTemplate> actionTemplates = resourceTemplate.getActionTemplates();
                    for (ActionTemplate actionTemplate : actionTemplates) {
                        Action action = new Action(actionTemplate);
                        action.setResourceId(resource.getId());
                        actions.add(action);
                    }
                    actionService.batchSave(actions);
                }
            }
        }

    }
    @Transactional
    public void refresh() throws IOException {
        URL url = this.getClass().getClassLoader().getResource("com/oms/portal/resources");
        if (url != null) {
            Map<String,ResourceTemplate> resourceTemplates = new HashMap<String, ResourceTemplate>();
            JaxbMapper jaxbMapper = new JaxbMapper(ResourceTemplate.class);
            String path = url.getPath();
            File dir = new File(path);
            File[] resourceFiles = dir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(".xml");
                }
            });
            if (resourceFiles != null && resourceFiles.length > 0) {
                for (File resource : resourceFiles) {
                    FileInputStream fis = new FileInputStream(resource);
                    try {
                        ResourceTemplate resourceTemplate = jaxbMapper.fromXml(fis);
                        resourceTemplates.put(resourceTemplate.getName(), resourceTemplate);
                    } finally {
                        fis.close();
                    }
                }
            }
            if (resourceTemplates.size() > 0) {
                //删除原配置
                executeHql("delete from Action");
                executeHql("delete from Resource");

                for (ResourceTemplate resourceTemplate : resourceTemplates.values()) {
                    Resource resource = new Resource(resourceTemplate.getName(), resourceTemplate.path(resourceTemplates),resourceTemplate.getFetchClass(), resourceTemplate.getDescription());
                    saveOrUpdate(resource);
                    List<Action> actions = new ArrayList<Action>();
                    List<ActionTemplate> actionTemplates = resourceTemplate.getActionTemplates();
                    for (ActionTemplate actionTemplate : actionTemplates) {
                        Action action = new Action(actionTemplate);
                        action.setResourceId(resource.getId());
                        actions.add(action);
                    }
                    actionService.batchSave(actions);
                }
            }
        }

    }
}
