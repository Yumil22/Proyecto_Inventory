package com.example.sales_partner_v21.Database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

@Database(entities = {ProductsCategories.class,
                      Products.class,
                      Assemblies.class,
                      AssembliesProducts.class,
                      Customers.class,
                      OrderStatus.class,
                      Orders.class,
                      OrderAssemblies.class},version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE = null;

    // Singleton
    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context,
                    AppDatabase.class, "salesPartner.db")
                    .allowMainThreadQueries()
                    .addCallback(new Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);

                            // PRODUCTS CATEGORIES
                            db.execSQL("INSERT INTO product_categories (id, description) VALUES (0, 'Disco duro')");
                            db.execSQL("INSERT INTO product_categories (id, description) VALUES (1, 'Memoria')");
                            db.execSQL("INSERT INTO product_categories (id, description) VALUES (2, 'Monitor')");
                            db.execSQL("INSERT INTO product_categories (id, description) VALUES (3, 'Procesador')");
                            db.execSQL("INSERT INTO product_categories (id, description) VALUES (4, 'Tarjeta madre')");
                            db.execSQL("INSERT INTO product_categories (id, description) VALUES (5, 'Tarjeta de video')");
                            db.execSQL("INSERT INTO product_categories (id, description) VALUES (6, 'Tarjeta de sonido')");
                        }
                    })
                    .build();
        }
        return INSTANCE;
    }
}
