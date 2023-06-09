package com.example.warkopzara.ui.home;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.warkopzara.data.LoginDataSource;
import com.example.warkopzara.data.LoginRepository;
import com.example.warkopzara.data.ProductDataSource;
import com.example.warkopzara.data.ProductRepository;
import com.example.warkopzara.ui.login.LoginViewModel;

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
public class HomeViewModelFactory implements ViewModelProvider.Factory {

    private Activity activity;

    public HomeViewModelFactory(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(HomeViewModel.class)) {
            return (T) new HomeViewModel(activity, ProductRepository.getInstance(new ProductDataSource()));
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}