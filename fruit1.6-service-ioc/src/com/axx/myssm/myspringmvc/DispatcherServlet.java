package com.axx.myssm.myspringmvc;

import com.axx.myssm.ioc.BeanFactory;
import com.axx.myssm.utils.StringUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@WebServlet("*.do")
public class DispatcherServlet extends ViewBaseServlet {

    private BeanFactory beanFactory;

    public DispatcherServlet() {
    }

    @Override
    public void init() throws ServletException {
        super.init();
        //IOC容器在servlet上下文被初始化是就应该被创建,因此通过监听器创建后保存在上下文中，控制器直接获取
        //beanFactory = new ClassPathXmlApplicationContext();
        Object beanFactoryObj = getServletContext().getAttribute("beanFactory");
        if (beanFactoryObj != null) {
            beanFactory = (BeanFactory) beanFactoryObj;
        } else {
            throw new RuntimeException("IOC容器获取失败");
        }
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        //获取servletPath：/hello.do
        String servletPath = req.getServletPath();
        System.out.println(servletPath);
        //切割字符串 /hello.do -> hello.do
        servletPath = servletPath.substring(1);
        int lastDotIndex = servletPath.lastIndexOf(".do");
        //切割字符串 hello.do -> hello
        servletPath = servletPath.substring(0, lastDotIndex);
        System.out.println(servletPath);

        Object controllerBeanObj = beanFactory.getBean(servletPath);

        String operate = req.getParameter("operate");
        if (StringUtil.isEmpty(operate)) {
            operate = "index";
        }

        try {
            //通过operate获取到相应的方法
            Method[] methods = controllerBeanObj.getClass().getDeclaredMethods();
            for (Method method : methods) {
                if (operate.equals(method.getName())) {
                    //获取当前方法的参数，返回参数数组
                    Parameter[] parameters = method.getParameters();
                    //声明一个Object数组用来承载参数的值
                    Object[] parameterValues = new Object[parameters.length];
                    for (int i = 0; i < parameters.length; i++) {
                        //遍历参数的名字，通过参数名获取参数值并保存
                        Parameter parameter = parameters[i];
                        String parameterName = parameter.getName();

                        //判断参数是否为特殊类型
                        if ("req".equals(parameterName)) {
                            parameterValues[i] = req;
                        } else if ("resp".equals(parameterName)) {
                            parameterValues[i] = resp;
                        } else if ("session".equals(parameterName)) {
                            parameterValues[i] = req.getSession();
                        } else {
                            String parameterValue = req.getParameter(parameterName);
                            String typeName = parameter.getType().getTypeName();
                            if ("java.lang.Integer".equals(typeName) && parameterValue != null) {
                                parameterValues[i] = Integer.parseInt(parameterValue);
                            } else {
                                parameterValues[i] = parameterValue;
                            }
                        }
                    }

                    //调用controller组件中对应的方法
                    method.setAccessible(true);
                    Object returnObj = method.invoke(controllerBeanObj, parameterValues);

                    //视图处理
                    String methodReturnStr = (String) returnObj;
                    //重定向
                    if (methodReturnStr.startsWith("redirect:")) {
                        String redirectStr = methodReturnStr.substring("redirect:".length());
                        resp.sendRedirect(redirectStr);
                    } else {
                        //thymeleaf模板渲染
                        super.processTemplate(methodReturnStr, req, resp);
                    }
                }
            }
            /*else {
                throw new RuntimeException("operate值非法");
            }*/
        } catch (Exception e) {
            e.printStackTrace();
            throw new DispatcherServletException("DispatcherServlet出错了");
        }
    }
}
