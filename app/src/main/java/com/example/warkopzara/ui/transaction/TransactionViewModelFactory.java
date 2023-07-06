package com.example.warkopzara.ui.transaction;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.warkopzara.MainActivity;
import com.example.warkopzara.data.ProductDataSource;
import com.example.warkopzara.data.ProductRepository;
import com.example.warkopzara.ui.home.HomeViewModel;

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
public class TransactionViewModelFactory implements ViewModelProvider.Factory {

    private MainActivity activity;

    public TransactionViewModelFactory(MainActivity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(HomeViewModel.class)) {
            return (T) new HomeViewModel(activity);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}