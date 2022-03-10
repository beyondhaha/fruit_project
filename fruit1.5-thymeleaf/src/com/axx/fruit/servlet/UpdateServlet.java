package com.axx.fruit.servlet;

import com.axx.fruit.dao.FruitDAO;
import com.axx.fruit.dao.impl.FruitDAOImpl;
import com.axx.fruit.pojo.Fruit;
import com.axx.myssm.myspringmvc.ViewBaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/update.do")
public class UpdateServlet extends ViewBaseServlet {
    FruitDAO fruitDAO=new FruitDAOImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String f_name = req.getParameter("fname");
        String price = req.getParameter("price");
        int f_price=Integer.parseInt(price);
        String fcount = req.getParameter("fcount");
        int f_count=Integer.parseInt(fcount);
        String remark = req.getParameter("remark");
        String fid = req.getParameter("fid");
        int f_id = Integer.parseInt(fid);

        fruitDAO.updateFruit(new Fruit(f_id,f_name,f_price,f_count,remark));

        resp.sendRedirect("index");
    }
}
