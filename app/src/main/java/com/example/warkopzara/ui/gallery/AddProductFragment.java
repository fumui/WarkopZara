package com.example.warkopzara.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.warkopzara.databinding.FragmentAddProductBinding;

public class AddProductFragment extends Fragment {

    private FragmentAddProductBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AddProductViewModel addProductViewModel =
                new ViewModelProvider(this).get(AddProductViewModel.class);

        binding = FragmentAddProductBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final ImageView inputProductImage = binding.inputProductImage;
        final EditText inputProductName = binding.inputProductName;
        final EditText inputPrice = binding.inputPrice;
        final EditText inputStock = binding.inputStock;
        final Button buttonAdd = binding.buttonAdd;

        buttonAdd.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Sukses input produk", Toast.LENGTH_SHORT);

        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}