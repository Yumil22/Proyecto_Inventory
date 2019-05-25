package com.example.sales_partner_v21.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

@Dao
public interface AssembliesDao {
    @Query("SELECT * FROM assemblies ORDER BY description")
    List<Assemblies> getAllAssemblies();

    @Query("SELECT id FROM assemblies")
    List<Integer> getAllAssembliesIDs();

    @Query("SELECT * FROM assemblies WHERE description LIKE '%' || :searchText  || '%' ORDER BY description")
    List<Assemblies> getAllAssembliesByText(String searchText);

    @Query("SELECT * FROM assemblies WHERE id = :assemblyID")
    Assemblies getAssemblyByID(int assemblyID);

    @Query("SELECT * FROM assemblies WHERE id IN (:ids) ORDER BY description")
    List<Assemblies> getAssembliesAlphabetically(int[] ids);

    @Query("SELECT SUM(p.price) FROM assemblies a " +
            "INNER JOIN assembly_products ap ON a.id = ap.assembly_id " +
            "INNER JOIN products p ON p.id = ap.product_id " +
            " WHERE a.id = :asid")
    public int getPriceyAssembled(int asid);

    @Query("Delete from assemblies")
    public void DeleteAssembliesTable();

    @Insert
    void InsertAssemblies(Assemblies assemblies);
}
