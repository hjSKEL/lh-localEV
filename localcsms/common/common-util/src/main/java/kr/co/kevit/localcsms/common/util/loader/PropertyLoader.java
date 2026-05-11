package kr.co.kevit.localcsms.common.util.loader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import kr.co.kevit.localcsms.common.util.enumtype.PropertyKey;

public class PropertyLoader {
    //
    private static final String DEFAULT_PROP_FILE = "kevit.properties";
    
    private static Map<PropertyKey, String> propertiyMap = null;
    
    private static PropertyLoader loader = new PropertyLoader();
    /**
     * 
     */
    private PropertyLoader() {
        //
        ClassLoader classLoader = this.getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(DEFAULT_PROP_FILE);
        
        Properties properties = new Properties();
        
        try {
            properties.load(inputStream);
        } catch (FileNotFoundException e) {
            // T
            throw new RuntimeException(e.getMessage());
        } catch (IOException e) {
            // 
            throw new RuntimeException(e.getMessage());
        }
        
        propertiyMap = new HashMap<PropertyKey, String>();
        for(PropertyKey key : PropertyKey.values()) {
            propertiyMap.put(key, properties.getProperty(key.getKey()));
        }
    }
    
    public static PropertyLoader getInstance() {
        return loader;
    }
    
    public String getProperty(PropertyKey key) {
        return propertiyMap.get(key);
    }
}
