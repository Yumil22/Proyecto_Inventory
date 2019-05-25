package com.example.sales_partner_v21.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface ProductsCategoriesDao {
    @Query("SELECT product_categories.description FROM product_categories INNER JOIN products ON products.category_id = product_categories.id WHERE products.id = :id")
    String getCategoryByProductCategoryID(int id);

    @Query("SELECT description FROM product_categories")
    List<String> getAllProductsDescriptionCategories();

    @Query("SELECT * FROM product_categories WHERE description LIKE :description")
    ProductsCategories getProductCategoryByDescription(String description);

    @Query("SELECT * FROM product_categories")
    List<ProductsCategories> getAllProductsCategories();

    @Query("Delete from product_categories")
    public void DeleteProductCategoriesTable();

    @Insert
    void InsertProductCategories(ProductsCategories productsCategories);
}
