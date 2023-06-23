package com.example.warkopzara.data;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.warkopzara.R;
import com.example.warkopzara.data.model.Product;

import java.util.List;

public class CheckoutProductAdapter extends BaseAdapter {
    public List<Product> list;
    Activity activity;

    public CheckoutProductAdapter(Activity activity, List<Product> list) {
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
            convertView = inflater.inflate(R.layout.layout_checkout_list_item, null);
        }
        Product product = list.get(position);
        TextView productName = (TextView) convertView.findViewById(R.id.inputProductName);
        TextView productTotalPrice = (TextView) convertView.findViewById(R.id.productTotalPrice);
        TextView productOrderCount = (TextView) convertView.findViewById(R.id.productOrderCount);

        String priceText = "{{productTotalPrice}}";
        priceText = priceText.replace("{{productTotalPrice}}", ""+(product.getPrice()*product.getOrderCount()));

        String orderCountText = "Dipesan: {{productOrderCount}}";
        orderCountText = orderCountText.replace("{{productOrderCount}}", ""+product.getOrderCount());

        productName.setText((CharSequence) product.getName());
        productTotalPrice.setText((CharSequence) priceText);
        productOrderCount.setText((CharSequence) orderCountText);


        return convertView;
    }

}