package com.example.sales_partner_v21.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

@Dao
public interface ProductsCategoriesDao {
    @Query("SELECT product_categories.description FROM product_categories INNER JOIN products ON products.category_id = product_categories.id WHERE products.id = :id")
    String getCategoryByProductCategoryID(int id);
}
