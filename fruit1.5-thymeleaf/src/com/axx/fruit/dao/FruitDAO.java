package com.axx.fruit.dao;

import com.axx.fruit.pojo.Fruit;

import java.util.List;

public interface FruitDAO {
    //获取指定页码上的库存列表信息，每页显示5条
    List<Fruit> getFruitList(String keyword, Integer pageNo);

    Fruit getFruitByFid(Integer fid);

    void updateFruit(Fruit fruit);

    void deleteFruit(int fid);

    void addFruit(Fruit fruit);

    int getFruitCount(String keyword);
}
