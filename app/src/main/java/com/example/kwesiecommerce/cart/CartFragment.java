package com.example.kwesiecommerce.cart;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.kwesiecommerce.Authetication.SessionManager;

import com.example.kwesiecommerce.Model.Cart;
import com.example.kwesiecommerce.Model.Order;
import com.example.kwesiecommerce.R;
import com.example.kwesiecommerce.data.DatabaseHandler;
import com.example.kwesiecommerce.data.DatabaseHelper;
import com.example.kwesiecommerce.databinding.FragmentCartBinding;
import com.example.kwesiecommerce.ui.cartAdapter;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment implements cartAdapter.OnCartClickListener {

    private FragmentCartBinding binding;
    SessionManager sessionManager;
    private List<Cart> carts;
    DatabaseHandler db;
    private cartAdapter cartAdapter;
    RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentCartBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = binding.cartRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        sessionManager = new SessionManager(getContext());
        db = new DatabaseHandler(getContext(), new DatabaseHelper(getContext()));
        carts = new ArrayList<>();

        loadCartItems(sessionManager.getUserId());

        Button btnCheckout = binding.cartStartShopping;
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_navigation_cart_to_navigation_dashboard);
            }
        });

        binding.cartBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                long orderId = db.insertOrder(sessionManager.getUserId());


                for (Cart item : carts) {

                    db.insertOrderItem(orderId, item.getProductId(), item.getQuantity());


                    String[] whereArgs = {String.valueOf(item.getId())};
                    db.delete("cart", "id = ?", whereArgs);
                }


                carts.clear();
                cartAdapter.notifyDataSetChanged();

                loadCartItems(sessionManager.getUserId());

                new AlertDialog.Builder(getContext())
                        .setTitle("Order placed")
                        .setMessage("Your order has been successfully placed!")
                        .setPositiveButton(android.R.string.ok, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        binding.cartOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Order> orders = db.getAllOrders(sessionManager.getUserId());
                StringBuilder sb = new StringBuilder();

                for (Order order : orders) {
                    sb.append("\n\n\nOrder ID: ");
                    sb.append(order.getId());
                    sb.append("\n\nProduct Name: ");
                    sb.append(order.getProductName());
                    sb.append("\n\nQuantity: ");
                    sb.append(order.getQuantity());
                    sb.append("\n\nOrder Date: ");
                    sb.append(order.getOrderDate());
                    sb.append("\n\nOrder Status: ");
                    sb.append(order.getOrderStatus());
                    sb.append("\n\n");

                }

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Orders");
                builder.setMessage(sb.toString());
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });


        return root;
    }

    private void loadCartItems(int userId) {
        if (db.isInCartByUser(userId)) {
            carts = db.getAllCartItems(userId);
            cartAdapter = new cartAdapter(getContext(), carts, this);
            recyclerView.setAdapter(cartAdapter);
            binding.cartRecyclerView.setVisibility(View.VISIBLE);
            binding.cartEmpty.setVisibility(View.INVISIBLE);
            binding.cartBottomBar.setVisibility(View.VISIBLE);

            refreshCartTotal();
        } else {
            binding.cartRecyclerView.setVisibility(View.INVISIBLE);
            binding.cartBottomBar.setVisibility(View.INVISIBLE);
            binding.cartEmpty.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onDeleteClick(Cart cart) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Delete Cart Item");
        builder.setMessage("Are you sure you want to delete this ?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String[] whereArgs = {String.valueOf(cart.getId())}; // Assuming you have a method getId in your Cart class to get cart item's id
                int result = db.delete("cart", "id = ?", whereArgs);

                if (result > 0) {
                    Toast.makeText(getContext(), "Product deleted successfully", Toast.LENGTH_SHORT).show();
                    carts.remove(cart);
                    cartAdapter.notifyDataSetChanged();

                    refreshCartTotal();

                } else {
                    Toast.makeText(getContext(), "Failed to delete product", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public int calculateTotalPrice(List<Cart> carts) {
        int totalPrice = 0;


        for (Cart item : carts) {
            totalPrice += item.getPrice() * item.getQuantity();
        }

        return totalPrice;
    }

    private void refreshCartTotal() {
        int totalPrice = calculateTotalPrice(carts);
        binding.cartTotalPrice.setText(String.valueOf(totalPrice) + "$");
    }



}
