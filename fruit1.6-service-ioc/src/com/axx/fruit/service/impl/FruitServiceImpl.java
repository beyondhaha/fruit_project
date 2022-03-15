package com.axx.fruit.service.impl;

import com.axx.fruit.service.FruitService;
import com.axx.fruit.dao.FruitDAO;
import com.axx.fruit.dao.impl.FruitDAOImpl;
import com.axx.fruit.pojo.Fruit;

import java.util.List;

public class FruitServiceImpl implements FruitService {

    FruitDAO fruitDAO = new FruitDAOImpl();

    @Override
    public List<Fruit> getFruitList(String keyword, Integer pageNo) {
        return fruitDAO.getFruitList(keyword, pageNo);
    }

    @Override
    public void addFruit(Fruit fruit) {
        fruitDAO.addFruit(fruit);
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
        Integer fruitCount = fruitDAO.getFruitCount(keyword);
        Integer pageCount = (fruitCount + 4) / 5;
        return pageCount;
    }

    @Override
    public void updateFruit(Fruit fruit) {
        fruitDAO.updateFruit(fruit);
    }
}
