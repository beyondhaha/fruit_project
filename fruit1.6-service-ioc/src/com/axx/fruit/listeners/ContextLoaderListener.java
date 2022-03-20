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
        //1、获取servletContext对象
        ServletContext servletContext = sce.getServletContext();
        //2、获取上下文的初始化参数
        String path = servletContext.getInitParameter("contextConfigLocation");
        //3、创建IOC容器
        BeanFactory beanFactory = new ClassPathXmlApplicationContext();
        //4、将IOC容器保存到application作用域
        servletContext.setAttribute("beanFactory", beanFactory);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
