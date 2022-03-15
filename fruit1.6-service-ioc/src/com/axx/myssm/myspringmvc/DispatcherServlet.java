package com.axx.myssm.myspringmvc;

import com.axx.myssm.utils.StringUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

@WebServlet("*.do")
public class DispatcherServlet extends ViewBaseServlet {
    private Map<String, Object> beanMap = new HashMap<>();

    public DispatcherServlet() {
    }

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("applicationContext.xml");
            //1、创建DocumentBuilderFactory
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            //2、创建DocumentBuilder对象
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            //3、创建Document对象
            Document document = documentBuilder.parse(inputStream);
            //4、获取所有的bean元素
            NodeList beanNodeList = document.getElementsByTagName("bean");
            for (int i = 0; i < beanNodeList.getLength(); i++) {
                Node beanNode = beanNodeList.item(i);
                if (beanNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element beanElement = (Element) beanNode;
                    String beanId = beanElement.getAttribute("id");
                    String className = beanElement.getAttribute("class");
                    Object beanObj = Class.forName(className).newInstance();
                    beanMap.put(beanId, beanObj);
                }
            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=utf-8");

        //获取servletPath：/hello.do
        String servletPath = req.getServletPath();
        System.out.println(servletPath);
        //切割字符串 /hello.do -> hello.do
        servletPath = servletPath.substring(1);
        int lastDotIndex = servletPath.lastIndexOf(".do");
        //切割字符串 hello.do -> hello
        servletPath = servletPath.substring(0, lastDotIndex);
        System.out.println(servletPath);

        Object controllerBeanObj = beanMap.get(servletPath);

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
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
