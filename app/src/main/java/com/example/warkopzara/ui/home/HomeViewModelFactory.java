package com.example.warkopzara.ui.home;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.warkopzara.MainActivity;
import com.example.warkopzara.data.ProductDataSource;
import com.example.warkopzara.data.ProductRepository;

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
public class HomeViewModelFactory implements ViewModelProvider.Factory {

    private MainActivity activity;

    public HomeViewModelFactory(MainActivity activity) {
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