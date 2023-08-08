package com.example.kwesiecommerce.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kwesiecommerce.Model.Order;
import com.example.kwesiecommerce.Model.Product;
import com.example.kwesiecommerce.Model.User;
import com.example.kwesiecommerce.R;
import com.example.kwesiecommerce.data.DatabaseHandler;
import com.example.kwesiecommerce.data.DatabaseHelper;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ManageOrdersAdapter extends RecyclerView.Adapter<ManageOrdersAdapter.OrdersViewHolder> {

    private Context context;
    private OnOrdersClickListener onOrdersClickListener;
    private List<Order> orders;

    public ManageOrdersAdapter(Context context, List<Order> orders, OnOrdersClickListener onOrdersClickListener) {
        this.context = context;
        this.orders = orders;
        this.onOrdersClickListener = onOrdersClickListener;
    }


    @NonNull
    @Override
    public OrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.manage_orders_item, parent, false);
        return new ManageOrdersAdapter.OrdersViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersViewHolder holder, int position) {

        Order order = orders.get(position);
        holder.textViewOrderId.setText(String.valueOf(order.getId()));
        holder.textViewCustomerName.setText(holder.db.getUser(order.getUserId()).getUsername());
        // Extracting only the date part from the order date
        String orderDate = order.getOrderDate().split(" ")[0];
        holder.textViewDate.setText(orderDate);
        holder.textViewStatus.setText(order.getOrderStatus());

//        holder.buttonEditUser.setOnClickListener(v -> {
//            onUserClickListener.onEditClick(user);
//        });
//
//        holder.buttonDeleteUser.setOnClickListener(v -> {
//            onUserClickListener.onDeleteClick(user);
//        });
        holder.orderItemLayout.setOnClickListener(v -> {
            onOrdersClickListener.onOrderClick(order);
        });
    }

    @Override
    public int getItemCount() {
        return orders != null ? orders.size() : 0;
    }

    public class OrdersViewHolder extends RecyclerView.ViewHolder {
        TextView textViewOrderId, textViewCustomerName, textViewDate, textViewStatus;
        DatabaseHandler db;
        RelativeLayout orderItemLayout;

        public OrdersViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewOrderId = itemView.findViewById(R.id.textView_order_id);
            textViewCustomerName = itemView.findViewById(R.id.textView_customer_order_name);
            textViewDate = itemView.findViewById(R.id.textView_order_date);
            textViewStatus = itemView.findViewById(R.id.textView_order_status);
            db = new DatabaseHandler(context, new DatabaseHelper(context));
            // Set click listener on the parent RelativeLayout
            orderItemLayout = itemView.findViewById(R.id.order_item_layout);

        }


    }

    public interface OnOrdersClickListener {

        void onOrderClick(Order order);

    }
}
