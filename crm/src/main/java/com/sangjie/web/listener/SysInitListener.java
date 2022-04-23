package com.sangjie.web.listener;

import com.sangjie.setting.domain.DicValue;
import com.sangjie.setting.service.DicService;
import com.sangjie.setting.service.impl.DicServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;

public class SysInitListener implements ServletContextListener {

    //@Resource
    //private DicServiceImpl dicService;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext application = servletContextEvent.getServletContext();

        DicService dicService =  WebApplicationContextUtils.getWebApplicationContext(application).getBean(DicService.class);
        Map<String, List<DicValue>> map = dicService.getAll(application);
        Set<String> set = map.keySet();

        for (String key : set) {
            List<DicValue> value = map.get(key);
            application.setAttribute(key, value);
        }

        //处理properties文件
        /*
        解析该文件，将该属性文件处理成java键值对关系
        pMap保存值后，放在服务器缓存中
         */
        ResourceBundle rb = ResourceBundle.getBundle("Stage2Possibility");
        Enumeration<String> enumeration = rb.getKeys();

        Map<String, String> pMap = new HashMap<>();

        while (enumeration.hasMoreElements()) {
            String key = enumeration.nextElement();
            String value = rb.getString(key);
            pMap.put(key, value);
        }
        application.setAttribute("pMap", pMap);

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
