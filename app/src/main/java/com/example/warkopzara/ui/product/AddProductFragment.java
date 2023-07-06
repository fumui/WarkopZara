package com.example.warkopzara.ui.product;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.warkopzara.Config;
import com.example.warkopzara.data.model.Product;
import com.example.warkopzara.databinding.FragmentAddProductBinding;
import com.example.warkopzara.providers.MyUrlRequestCallback;
import com.google.gson.JsonObject;

import org.chromium.net.CronetEngine;
import org.chromium.net.UploadDataProviders;
import org.chromium.net.UrlRequest;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AddProductFragment extends Fragment {

    private static final int REQUEST_IMAGE_PICK = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private static final int PERMISSION_REQUEST_CODE = 3;
    private static final String TAG = "AddProductFragment";

    private FragmentAddProductBinding binding;
    private ImageView inputProductImage;
    private EditText inputProductName;
    private EditText inputPrice;
    private EditText inputStock;
    private Button buttonAdd;
    private Uri selectedImageUri;

    private Bitmap productImageBitmap;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AddProductViewModel addProductViewModel =
                new ViewModelProvider(this).get(AddProductViewModel.class);

        binding = FragmentAddProductBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        inputProductImage = binding.inputProductImage;
        inputProductName = binding.productName;
        inputPrice = binding.inputPrice;
        inputStock = binding.inputStock;
        buttonAdd = binding.buttonAdd;

        inputProductImage.setOnClickListener(v -> {
            if (checkPermissions()) {
                showImageSelectionDialog();
            } else {
                requestPermissions();
            }
        });

        buttonAdd.setOnClickListener(v -> {
            String name = binding.productName.getText().toString();
            int stock = Integer.parseInt(binding.inputStock.getText().toString());
            int price = Integer.parseInt(binding.inputPrice.getText().toString());
            Product product = new Product("",name, stock, price);
            createNewProduct(product);
        });

        return root;
    }

    private void createNewProduct(Product product){
        Activity currentActivity = getActivity();
        CronetEngine.Builder myBuilder = new CronetEngine.Builder(currentActivity);
        CronetEngine cronetEngine = myBuilder.build();
        Executor executor = Executors.newSingleThreadExecutor();
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        productImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//        byte[] byteArray = stream.toByteArray();
//        productImageBitmap.recycle();


        JsonObject requestBodyJson = new JsonObject();
        requestBodyJson.addProperty("category_id", 1);
        requestBodyJson.addProperty("name", product.getName());
        requestBodyJson.addProperty("image_url", "202306291688010842036.png");
        requestBodyJson.addProperty("price", product.getPrice());
        requestBodyJson.addProperty("stock", product.getStock());
        String jsonBody = requestBodyJson.toString();

        MyUrlRequestCallback createProductRequestCallback = new MyUrlRequestCallback(result -> {
            try {
                JSONObject data = new JSONObject(result.toString());
                String responseBody = data.getString("body");
                int statusCode = data.getInt("statusCode");
                if (statusCode == 200) { //Some kind of success
                    clearInputs();
                    Toast.makeText(getContext(), "Sukses input produk", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(TAG, "Failed to create product: status "+statusCode+", responseBody: "+responseBody);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        MyUrlRequestCallback imageRequestCallback = new MyUrlRequestCallback(result -> {
            try {
                JSONObject data = new JSONObject(result.toString());
                String responseBody = data.getString("body");
                int statusCode = data.getInt("statusCode");
                if (statusCode == 200) { //Some kind of success
                    JSONObject body = new JSONObject(responseBody);
                    Log.d(TAG, body.getJSONObject("data").getString("filename"));
                    product.setImage(body.getJSONObject("data").getString("filename"));
                } else {
                    Log.e(TAG, "Failed to upload image: status "+statusCode+", responseBody: "+responseBody);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

//        UrlRequest.Builder imageRequestBuilder = cronetEngine
//                .newUrlRequestBuilder(
//                        Config.BE_URL+"/api/v1/upload/product",
//                        imageRequestCallback,
//                        executor
//                )
//                .setHttpMethod("POST")
//                .addHeader("Content-Type","application/json")
//                .setUploadDataProvider(UploadDataProviders.create(byteArray),executor);
//        UrlRequest imageRequest = imageRequestBuilder.build();
//        imageRequest.start();

        UrlRequest.Builder createProductRequestBuilder = cronetEngine
                .newUrlRequestBuilder(
                        Config.BE_URL+"/api/v1/product/create",
                        createProductRequestCallback,
                        executor
                )
                .setHttpMethod("POST")
                .addHeader("Content-Type","application/json")
                .setUploadDataProvider(UploadDataProviders.create(jsonBody.getBytes()),executor);
        UrlRequest createProductRequest = createProductRequestBuilder.build();
        createProductRequest.start();
    }
    private void showImageSelectionDialog() {
        // Create an intent to select an image from the gallery or capture a photo
        Intent intent = new Intent(Intent.ACTION_CAMERA_BUTTON);
        intent.setType("image/*");
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);

        // Check if there is an activity available to handle the intent
        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private boolean checkPermissions() {
        int permissionStatus = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA);
        return permissionStatus == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(requireActivity(),
                new String[]{Manifest.permission.CAMERA},
                PERMISSION_REQUEST_CODE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_PICK) {
                if (data != null) {
                    selectedImageUri = data.getData();
                    try {
                        productImageBitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), selectedImageUri);
                        inputProductImage.setImageBitmap(productImageBitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (requestCode == REQUEST_IMAGE_CAPTURE) {
                // Handle captured image from camera
                productImageBitmap = (Bitmap) data.getExtras().get("data");
                inputProductImage.setImageBitmap(productImageBitmap);
            }
        }
    }

    private void clearInputs(){
        binding.productName.setText("");
        binding.inputPrice.setText("");
        binding.inputStock.setText("");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
