package com.axx.fruit.servlet;

import com.axx.fruit.dao.FruitDAO;
import com.axx.fruit.dao.impl.FruitDAOImpl;
import com.axx.myssm.myspringmvc.ViewBaseServlet;
import com.axx.myssm.utils.StringUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/delete.do")
public class DeleteServlet extends ViewBaseServlet {
    private FruitDAO fruitDAO = new FruitDAOImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fidstr = req.getParameter("fid");
        System.out.println(fidstr);
        if (StringUtil.isNotEmpty(fidstr)) {
            int fid = Integer.parseInt(fidstr);
            fruitDAO.deleteFruit(fid);
        }

        resp.sendRedirect("index");
    }
}
