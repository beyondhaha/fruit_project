package com.axx.fruit.controllers;

import com.axx.fruit.dao.FruitDAO;
import com.axx.fruit.dao.impl.FruitDAOImpl;
import com.axx.fruit.pojo.Fruit;
import com.axx.myssm.myspringmvc.ViewBaseServlet;
import com.axx.myssm.utils.StringUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class FruitController extends ViewBaseServlet {

    private FruitDAO fruitDAO = new FruitDAOImpl();

    private void index(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Integer pageNo = 1;

        String oper = req.getParameter("oper");

        String keyword = null;
        if (StringUtil.isNotEmpty(oper) && "search".equals(oper)) {
            //表单查询发出的请求，包含查询条件
            pageNo = 1;
            keyword = req.getParameter("keyword");
            if (StringUtil.isEmpty(keyword)) {
                keyword = "";
            }
            session.setAttribute("keyword", keyword);
        } else {
            //不是表单查询发出的请求
            String pageStr = req.getParameter("pageNo");
            if (StringUtil.isNotEmpty(pageStr)) {
                pageNo = Integer.parseInt(pageStr);
            }
            Object keywordobj = session.getAttribute("keyword");
            if (keywordobj != null) {
                keyword = (String) keywordobj;
            } else {
                keyword = "";
            }
        }


        int fruitCount = fruitDAO.getFruitCount(keyword);
        List<Fruit> fruitList = fruitDAO.getFruitList(keyword, pageNo);

        session.setAttribute("pageNo", pageNo);
        session.setAttribute("pageCount", (fruitCount + 4) / 5);
        session.setAttribute("fruitList", fruitList);

        super.processTemplate("index", req, resp);
    }

    private void add(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String f_name = req.getParameter("fname");
        String price = req.getParameter("price");
        int f_price = Integer.parseInt(price);
        String fcount = req.getParameter("fcount");
        int f_count = Integer.parseInt(fcount);
        String remark = req.getParameter("remark");

        fruitDAO.addFruit(new Fruit(0, f_name, f_price, f_count, remark));

        resp.sendRedirect("fruit.do");
    }

    private void update(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String f_name = req.getParameter("fname");
        String price = req.getParameter("price");
        int f_price = Integer.parseInt(price);
        String fcount = req.getParameter("fcount");
        int f_count = Integer.parseInt(fcount);
        String remark = req.getParameter("remark");
        String fid = req.getParameter("fid");
        int f_id = Integer.parseInt(fid);

        fruitDAO.updateFruit(new Fruit(f_id, f_name, f_price, f_count, remark));

        resp.sendRedirect("fruit.do");
    }

    private void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fidstr = req.getParameter("fid");
        System.out.println(fidstr);
        if (StringUtil.isNotEmpty(fidstr)) {
            int fid = Integer.parseInt(fidstr);
            fruitDAO.deleteFruit(fid);
        }

        resp.sendRedirect("fruit.do");
    }

    private void edit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fid = req.getParameter("fid");

        if (StringUtil.isNotEmpty(fid)) {
            int f_id = Integer.parseInt(fid);
            Fruit fruit = fruitDAO.getFruitByFid(f_id);
            req.setAttribute("fruit", fruit);
            super.processTemplate("edit", req, resp);
        }
    }
}
