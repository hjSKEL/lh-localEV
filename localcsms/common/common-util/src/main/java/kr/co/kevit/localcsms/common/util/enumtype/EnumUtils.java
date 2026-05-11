/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.common.util.enumtype;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.kevit.localcsms.common.util.enumtype.authority.UserRoleType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;

/**
 * 
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2018. 12. 14.
 */
public class EnumUtils {
    //
    private static final Logger LOGGER = LoggerFactory.getLogger(EnumUtils.class);
    
    private final String BASE_PACKAGE = "kr.co.kevit.localcsms.common.util.enumtype";

    private static EnumUtils enumUtils = new EnumUtils();

    private Map<String, List<EnumKeyValue>> enums = new HashMap<String, List<EnumKeyValue>>();
    
    private static Map<UserRoleType, List<String>> roleExclusionList;
    static {
        roleExclusionList = new HashMap<UserRoleType, List<String>>();
        
        roleExclusionList.put(UserRoleType.ADMIN, new ArrayList<String>());
        roleExclusionList.put(UserRoleType.USER, new ArrayList<String>());
    }
    
    public static EnumUtils getInstance() {
        return enumUtils;
    }

    @SuppressWarnings("unchecked")
    EnumUtils() {

        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);

        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + resolveBasePackage(BASE_PACKAGE) + "/*/*.class";

        try {
            Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);
            for (Resource resource : resources) {
                if (resource.isReadable()) {
                    MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
                    Class<?> c = Class.forName(metadataReader.getClassMetadata().getClassName());
                    if (c.isEnum()) {
                        try { 
                            Method getKeyValues = c.getMethod("getKeyValues");
                            List<EnumKeyValue> values = (List<EnumKeyValue>) getKeyValues.invoke(c);
                            
                            enums.put(c.getSimpleName(), values);
                        } catch (NoSuchMethodException e) {
                            LOGGER.error("[{}] Enum NoSuchMethodException!! Please Create Method [getKeyValues()]", c.getSimpleName());
                        } 
                    }
                }
            }
        } catch (IOException | ClassNotFoundException| SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            //
            LOGGER.error(e.getMessage(), e);
        }
    }
    private String resolveBasePackage(String basePackage) {
        return ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(basePackage));
    }

    public Map<String, List<EnumKeyValue>> getAllEnumKeyValues(UserRoleType userRole) {
        //
        Map<String, List<EnumKeyValue>> result = new HashMap<String, List<EnumKeyValue>>();
        for(String keyName : enums.keySet()) {
            if(!isExclusion(userRole, keyName)) {
                result.put(keyName, enums.get(keyName));
            }
        }
        return result;
    }

    public Map<String, List<EnumKeyValue>> getEnumKeyValuesByNameList(UserRoleType userRole, List<String> selectList) {
        //
        Map<String, List<EnumKeyValue>> result = new HashMap<String, List<EnumKeyValue>>();
        for(String keyName : enums.keySet()) {
            if(isExclusion(userRole, keyName)) {
                continue;
            }
            if(selectList.contains(keyName)) {
                result.put(keyName, enums.get(keyName));
            }
        }
        return result;
    }

    public List<EnumKeyValue> getEnumKeyValuesByName(UserRoleType userRole, String name) {
        //
        if(!isExclusion(userRole, name)) {
            return enums.get(name);
        }
        return Collections.<EnumKeyValue>emptyList();
    }
    
    private boolean isExclusion(UserRoleType userRole, String name) {
        //
        List<String> exclusionList = roleExclusionList.get(userRole);
        if(exclusionList.contains(name)) {
            return true;
        }
        return false;
    }

}

