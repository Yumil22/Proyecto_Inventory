package com.example.sales_partner_v21.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.sales_partner_v21.Database.AppRepository;
import com.example.sales_partner_v21.Database.Sellers;

import java.util.List;

public class SellersModel extends AndroidViewModel {

    private AppRepository appRepository;

    public SellersModel(@NonNull Application application){
        super(application);

        appRepository = new AppRepository(application);

    }

    public LiveData<Sellers> getUserByLoginPassword(String user, String password){
        return appRepository.SearchSellersLoginPassword(user, password);
    }

    public LiveData<Sellers> getSellerIdAndName(String seller, String password){
        return appRepository.getSellerIdAndName(seller, password);
    }
}
