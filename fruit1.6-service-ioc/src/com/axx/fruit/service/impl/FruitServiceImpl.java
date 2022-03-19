package com.axx.fruit.service.impl;

import com.axx.fruit.service.FruitService;
import com.axx.fruit.dao.FruitDAO;
import com.axx.fruit.pojo.Fruit;
import com.axx.myssm.utils.ConnUtil;

import java.util.List;

public class FruitServiceImpl implements FruitService {

    private FruitDAO fruitDAO = null;

    @Override
    public List<Fruit> getFruitList(String keyword, Integer pageNo) {
        System.out.println("getFruitList -> " + ConnUtil.getConn());
        return fruitDAO.getFruitList(keyword, pageNo);
    }

    @Override
    public void addFruit(Fruit fruit) {
        fruitDAO.addFruit(fruit);
        fruitDAO.updateFruit(fruit);
    }

    @Override
    public Fruit getFruitByFid(Integer fid) {
        return fruitDAO.getFruitByFid(fid);
    }

    @Override
    public void deleteFruit(Integer fid) {
        fruitDAO.deleteFruit(fid);
    }

    @Override
    public Integer getPageCount(String keyword) {
        System.out.println("getPageCount -> " + ConnUtil.getConn());
        Integer fruitCount = fruitDAO.getFruitCount(keyword);
        Integer pageCount = (fruitCount + 4) / 5;
        return pageCount;
    }

    @Override
    public void updateFruit(Fruit fruit) {
        fruitDAO.updateFruit(fruit);
    }
}
