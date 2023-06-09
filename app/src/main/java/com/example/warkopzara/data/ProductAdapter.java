package com.example.warkopzara.data;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


import com.example.warkopzara.R;
import com.example.warkopzara.data.model.Product;

import java.util.List;

public class ProductAdapter extends BaseAdapter {
    public List<Product> list;
    Activity activity;

    public ProductAdapter(Activity activity, List<Product> list) {
        super();
        this.activity = activity;
        this.list = list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            convertView = inflater.inflate(R.layout.layout_product_list_item, null);
        }
        Product product = list.get(position);
        TextView productName = (TextView) convertView.findViewById(R.id.productName);
        TextView productStock = (TextView) convertView.findViewById(R.id.productStock);
        TextView productPrice = (TextView) convertView.findViewById(R.id.productPrice);
        TextView productOrderCount = (TextView) convertView.findViewById(R.id.productOrderCount);
        ImageButton addToCartButton = (ImageButton) convertView.findViewById(R.id.addToCartButton);

        String stockText = "Stok: {{productStock}}";
        stockText = stockText.replace("{{productStock}}", ""+product.getStock());

        String priceText = "{{productPrice}}";
        priceText = priceText.replace("{{productPrice}}", ""+product.getPrice());

        String orderCountText = "Dipesan: {{productOrderCount}}";
        orderCountText = orderCountText.replace("{{productOrderCount}}", ""+product.getOrderCount());

        productName.setText((CharSequence) product.getName());
        productStock.setText((CharSequence) stockText);
        productPrice.setText((CharSequence) priceText);
        productOrderCount.setText((CharSequence) orderCountText);

        addToCartButton.setOnClickListener(v -> {
            product.addOrderCount();
            String updatedOrderCountText = "Dipesan: {{productOrderCount}}";
            updatedOrderCountText = updatedOrderCountText.replace("{{productOrderCount}}", ""+product.getOrderCount());
            productOrderCount.setText((CharSequence) updatedOrderCountText);
        });

        return convertView;
    }

}