package kr.co.kevit.localcsms.common.util.loader;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ApplicationContextProvider implements ApplicationContextAware {

    private static ApplicationContext ctx;
    
    
    @SuppressWarnings("static-access")
    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        //
        this.ctx = ctx;
    }
    
    public static ApplicationContext getApplicationContext() {
        return ctx;
    }
    
    public static Object getBean(String beanName) {
        return ctx.getBean(beanName);
    }
    
}
