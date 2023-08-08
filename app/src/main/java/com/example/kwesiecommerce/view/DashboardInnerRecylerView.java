package com.example.kwesiecommerce.view;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

import com.bumptech.glide.Glide;
import com.example.kwesiecommerce.Authetication.SessionManager;
import com.example.kwesiecommerce.Model.Data;
import com.example.kwesiecommerce.ProductDetailActivity;
import com.example.kwesiecommerce.R;
import com.example.kwesiecommerce.data.DatabaseHandler;


public class DashboardInnerRecylerView extends RecyclerView.Adapter<DashboardInnerRecylerView.ViewHolder> {
    private List<Data> dataList;
    private List<String> titles;
    private boolean isShopByCategory;
    DatabaseHandler db;
    private Context context;

    public DashboardInnerRecylerView(List<Data> dataList, boolean isShopByCategory, DatabaseHandler db, Context context) {
        this.dataList = dataList;
        this.isShopByCategory = isShopByCategory;
        this.db = db;
        this.context = context;
    }

    @NonNull
    @Override
    public DashboardInnerRecylerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dashboard_body_recycler_body_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DashboardInnerRecylerView.ViewHolder holder, int position) {
        Data data = dataList.get(position);
        holder.product_textView.setText(data.getText());

        if (isShopByCategory) {
            holder.product_price_textView.setVisibility(View.GONE);

        } else {
            holder.product_price_textView.setVisibility(View.VISIBLE);
            holder.product_price_textView.setText(String.valueOf(data.getPrice()));
        }


        Glide.with(holder.imageView.getContext())
                .load(data.getImageUrl())
                .error(R.drawable.no_pictures)
                .placeholder(R.drawable.no_pictures)
                .into(holder.imageView);


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        ImageView imageView;
        TextView product_textView, product_price_textView;
        private SessionManager sessionManager;

        ViewHolder(View itemView) {
            super(itemView);
            sessionManager = new SessionManager(context);
            linearLayout = itemView.findViewById(R.id.product_card_view);
            imageView = itemView.findViewById(R.id.product_image_view);
            product_textView = itemView.findViewById(R.id.product_name_text_view);
            product_price_textView = itemView.findViewById(R.id.product_price_text_view);
            if (!sessionManager.getUserType().equals("admin")) {
                linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Get the clicked item
                        Data clickedItem = dataList.get(getBindingAdapterPosition());
                        int id = clickedItem.getId();

                        // Check if it's a category
                        if (!isShopByCategory) {
                            Intent intent = new Intent(v.getContext(), ProductDetailActivity.class);
                            intent.putExtra("Product_id", id);
                            v.getContext().startActivity(intent);

                        } else {

                        }
                    }
                });

            } else {

            }


        }
    }


}