package com.example.sales_partner_v21.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface AssembliesProductsDao {

    @Query("SELECT COUNT(*) FROM (SELECT assembly_products.assembly_id, products.id FROM assembly_products INNER JOIN products ON assembly_products.product_id = products.id) WHERE assembly_id = :id")
    int getNumberProductsById(int id);

    @Query("SELECT SUM(products.price) FROM products INNER JOIN assembly_products ON assembly_products.product_id = products.id WHERE assembly_products.assembly_id = :assemblyID")
    int getCostByAssemblyID(int assemblyID);

    @Query("SELECT product_id FROM assembly_products WHERE assembly_id = :assemblyID")
    int[] getProductsIDsByAssemblyID(int assemblyID);

    @Query("SELECT * FROM assembly_products WHERE assembly_id = :assemblyID")
    List<AssembliesProducts> getAllAssembliesProducts(int assemblyID);

    @Query("SELECT * FROM assembly_products")
    List<AssembliesProducts> getAllAssemblies();

    @Query("Delete from assembly_products")
    public void DeleteAssemblyProductsTable();

    @Insert
    void InsertAssemblyProducts(AssembliesProducts assembliesProducts);
}
