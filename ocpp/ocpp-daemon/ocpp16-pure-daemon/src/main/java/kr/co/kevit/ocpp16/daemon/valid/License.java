/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.daemon.valid;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2021. 12. 4.
 */
public class License {
    
    private static License instance = new License();
    
    public static License getInstance() {
        return instance;
    }
    
    public boolean init() {
        //
        String vendor = getVenderName();
        if(vendor == null || vendor.trim().equals("")) {
            return false;
        }
        String className = "kr.co.kevit.ocpp16.license." + vendor + "License";
        Class<?> cls;
        try {
            cls = Class.forName(className);
            Object obj = cls.newInstance();
            Method method = cls.getMethod("validate");
            Object result = method.invoke(obj, null);
            return (Boolean) result;
        } catch (ClassNotFoundException e) {
            // 
            e.printStackTrace();
        } catch (InstantiationException 
                | IllegalAccessException 
                | NoSuchMethodException 
                | SecurityException
                | IllegalArgumentException
                | InvocationTargetException e) {
            return false;
        }
        
        return false;
    }
    
    private String getVenderName() {
        //
        Resource resource = new ClassPathResource("license.txt");
        try {
            if (resource.exists()) {
                InputStream readInputStream = resource.getInputStream();
                
                String encoding = "UTF-8";

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] byteSize = new byte[1024];

                int length;
                while ((length = readInputStream.read(byteSize)) != -1) {
                    byteArrayOutputStream.write(byteSize, 0, length);
                }
                return byteArrayOutputStream.toString(encoding);
            }
        } catch (IOException e) {
            //
            return null;
        }
        return null;
    }

}
