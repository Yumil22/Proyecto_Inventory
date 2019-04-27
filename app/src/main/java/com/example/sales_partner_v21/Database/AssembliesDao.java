package com.example.sales_partner_v21.Database;

import android.arch.persistence.room.Dao;
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

    @Query("SELECT * FROM assemblies WHERE id IN (:ids) ORDER BY description")
    List<Assemblies> getAssembliesByID(int[] ids);

    @Query("SELECT * FROM assemblies WHERE id = :assemblyID")
    Assemblies getAssemblyByID(int assemblyID);

    @Query("SELECT * FROM assemblies WHERE id IN (:ids) ORDER BY description")
    List<Assemblies> getAssembliesAlphabetically(int[] ids);
}
