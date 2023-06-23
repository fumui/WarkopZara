package com.example.warkopzara.ui.checkout;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.warkopzara.R;
import com.example.warkopzara.data.CheckoutProductAdapter;
import com.example.warkopzara.data.ProductDataSource;
import com.example.warkopzara.data.model.Product;
import com.example.warkopzara.databinding.FragmentConfirmCheckoutBinding;

import java.util.List;

public class CheckoutConfirmationFragment extends Fragment {

    private FragmentConfirmCheckoutBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentConfirmCheckoutBinding.inflate(inflater, container, false);
        final ListView listView = binding.listviewConfirmProduct;
        List<Product> data = ProductDataSource.getDummyData();
        data.get(0).setOrderCount(1);
        data.get(1).setOrderCount(2);
        data.get(2).setOrderCount(3);
        data.get(3).setOrderCount(4);
        CheckoutProductAdapter adapter = new CheckoutProductAdapter(getActivity(), data);
        listView.setAdapter(adapter);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonCheckout.setOnClickListener(v -> {
            Toast.makeText(getActivity().getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
            getActivity().finish();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}