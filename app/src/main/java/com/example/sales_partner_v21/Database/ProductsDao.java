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

    @Query("SELECT * FROM products WHERE category_id = :categoryID ORDER BY description")
    List<Products> getProductsByCategoryID(int categoryID);

    @Query("SELECT * FROM products ORDER BY description")
    List<Products> getAllProducts();

    @Query("SELECT * FROM products WHERE category_id = :categoryID AND description LIKE '%' || :descriptionSearch  || '%' ORDER BY description")
    List<Products> getProductsByCategoryIDAndDescription(int categoryID, String descriptionSearch);

    @Query("SELECT * FROM products WHERE description LIKE '%' || :descriptionSearch  || '%' ORDER BY description")
    List<Products> getProductsByDescription(String descriptionSearch);
}
