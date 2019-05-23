package com.example.sales_partner_v21.Database;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class AppRepository {

    private SellersDao sellersDao;

    public AppRepository(Application application){

        AppDatabase app = AppDatabase.getAppDatabase(application);

        sellersDao = app.sellersDao();
    }

    public LiveData<Sellers> SearchSellersLoginPassword(String seller, String password){
        return  sellersDao.LoginSearch(seller, password);
    }

    public LiveData<Sellers> getSellerIdAndName(String seller, String password){
        return sellersDao.GetNameId(seller, password);
    }
}
