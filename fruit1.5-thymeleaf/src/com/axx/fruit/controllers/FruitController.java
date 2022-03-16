package com.axx.fruit.controllers;

import com.axx.fruit.dao.FruitDAO;
import com.axx.fruit.dao.impl.FruitDAOImpl;
import com.axx.fruit.pojo.Fruit;
import com.axx.myssm.utils.StringUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

public class FruitController {

    private FruitDAO fruitDAO = new FruitDAOImpl();

    private String index(String oper, String keyword, Integer pageNo, HttpServletRequest req) {
        HttpSession session = req.getSession();
        if (pageNo == null) {
            pageNo = 1;
        }

        if (StringUtil.isNotEmpty(oper) && "search".equals(oper)) {
            //表单查询发出的请求，包含查询条件
            pageNo = 1;
            if (StringUtil.isEmpty(keyword)) {
                keyword = "";
            }
            session.setAttribute("keyword", keyword);
        } else {
            //不是表单查询发出的请求
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

        //super.processTemplate("index", req, resp);
        return "index";
    }

    private String add(String fname, Integer price, Integer fcount, String remark) {

        fruitDAO.addFruit(new Fruit(0, fname, price, fcount, remark));

        //resp.sendRedirect("fruit.do");
        return "redirect:fruit.do";
    }

    private String update(String fname, Integer price, Integer fcount, String remark, Integer fid) {
        fruitDAO.updateFruit(new Fruit(fid, fname, price, fcount, remark));

        //资源跳转
        //resp.sendRedirect("fruit.do");
        return "redirect:fruit.do";
    }

    private String delete(Integer fid) {
        if (fid != null) {
            fruitDAO.deleteFruit(fid);
            //resp.sendRedirect("fruit.do");
            return "redirect:fruit.do";
        }
        return "error";
    }

    private String edit(Integer fid, HttpServletRequest req) {
        if (fid != null) {
            Fruit fruit = fruitDAO.getFruitByFid(fid);
            req.setAttribute("fruit", fruit);
            //super.processTemplate("edit", req, resp);
            return "edit";
        }
        return "error";
    }
}
