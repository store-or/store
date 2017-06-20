package com.core.web.freemarker;

import com.google.common.collect.Maps;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModelException;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.ui.Model;
import org.springframework.util.ClassUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Map;

/**
 * Created by laizy on 2017/4/17.
 */
public final class FreemarkerEnumLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(FreemarkerEnumLoader.class);
    private static final String PACKAGE_SEPARATOR = ",";
    public static final String ALL_CLASS_SUFFIX = "**/*.class";
    public static final String PATH_SEPARATOR = "/";
    private static String annotationType = FreemarkerEnum.class.getName();
    private static Map<String, String> enumMap = Maps.newHashMap();
    private final PathMatchingResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
    private MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(this.resourceResolver);
    private String basicPackage;

    public FreemarkerEnumLoader(String basicPackage) {
        this.basicPackage = basicPackage;
    }

    @PostConstruct
    public void init() throws IOException {
        if (StringUtils.isBlank(this.basicPackage)) {
            throw new IllegalArgumentException("basePackage不能为空");
        }
        for (String packagePath : StringUtils.split(this.basicPackage, PACKAGE_SEPARATOR)) {
            String path = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
                    + ClassUtils.convertClassNameToResourcePath(packagePath) + PATH_SEPARATOR + ALL_CLASS_SUFFIX;
            Resource[] resources = resourceResolver.getResources(path);
            if (ArrayUtils.isEmpty(resources)) {
                continue;
            }
            for (Resource resource : resources) {
                dealResource(resource);
            }
        }
    }

    private void dealResource(Resource resource) throws IOException {
        if (resource.isReadable()) {
            MetadataReader metadataReader = this.metadataReaderFactory.getMetadataReader(resource);
            if (metadataReader.getAnnotationMetadata().hasAnnotation(annotationType)) {
                String className = metadataReader.getClassMetadata().getClassName();
                String name = (String) metadataReader.getAnnotationMetadata().getAnnotationAttributes(annotationType)
                        .get("name");
                if (enumMap.containsKey(name)) {
                    throw new IllegalArgumentException(String.format("(%s)已经被(%s)占用", name, enumMap.get(name)));
                }
                enumMap.put(name, className);
            }
        }
    }

    public void registerToModel(Model model) {
        try {
            TemplateHashModel enumModel = BeansWrapper.getDefaultInstance().getStaticModels();
            for (Map.Entry<String, String> entry : enumMap.entrySet()) {
                model.addAttribute(entry.getKey(), enumModel.get(entry.getValue()));
            }
        } catch (TemplateModelException e) {
            LOGGER.error("ADD_ENUM_FAILED", e);
        }
    }

}
