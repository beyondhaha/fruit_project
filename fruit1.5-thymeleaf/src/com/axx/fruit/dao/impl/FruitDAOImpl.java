package com.axx.fruit.dao.impl;

import com.axx.fruit.dao.FruitDAO;
import com.axx.fruit.pojo.Fruit;
import com.axx.myssm.basedao.BaseDAO;

import java.util.List;

public class FruitDAOImpl extends BaseDAO<Fruit> implements FruitDAO {
    @Override
    public List<Fruit> getFruitList(Integer pageNo) {
        return super.executeQuery("select * from t_fruit limit ?,5", (pageNo - 1) * 5);
    }

    @Override
    public Fruit getFruitByFid(Integer fid) {
        return super.load("select * from t_fruit where fid = ?", fid);
    }

    @Override
    public void updateFruit(Fruit fruit) {
        super.executeUpdate("update t_fruit set fname = ?, price = ?, fcount = ?, remark = ? where fid = ?", fruit.getFname(), fruit.getPrice(), fruit.getFcount(), fruit.getRemark(), fruit.getFid());
    }

    @Override
    public void deleteFruit(int fid) {
        super.executeUpdate("delete from t_fruit where fid = ?", fid);
    }

    @Override
    public void addFruit(Fruit fruit) {
        String sql = "insert into t_fruit values (?,?,?,?,?)";
        super.executeUpdate(sql, fruit.getFid(), fruit.getFname(), fruit.getPrice(), fruit.getFcount(), fruit.getRemark());
    }
}
