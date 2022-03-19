package com.axx.fruit.listeners;

import com.axx.myssm.ioc.BeanFactory;
import com.axx.myssm.ioc.ClassPathXmlApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ContextLoaderListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        BeanFactory beanFactory = new ClassPathXmlApplicationContext();
        ServletContext servletContext = sce.getServletContext();
        servletContext.setAttribute("beanFactory", beanFactory);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
