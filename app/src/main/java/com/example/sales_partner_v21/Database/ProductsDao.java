package com.example.sales_partner_v21.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ProductsDao {
    @Query("SELECT * FROM products WHERE id IN (:productsIDs)")
    List<Products> getProductsByIDs(int[] productsIDs);

    @Query("SELECT * FROM products WHERE id = :id")
    Products getProductByID(int id);
}
