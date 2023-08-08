package com.example.kwesiecommerce.Admin;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.kwesiecommerce.Authetication.SessionManager;
import com.example.kwesiecommerce.Model.Order;

import com.example.kwesiecommerce.Model.User;
import com.example.kwesiecommerce.R;
import com.example.kwesiecommerce.data.DatabaseHandler;
import com.example.kwesiecommerce.data.DatabaseHelper;
import com.example.kwesiecommerce.ui.ManageOrdersAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ManageOrdersFragment extends Fragment implements ManageOrdersAdapter.OnOrdersClickListener {
    private FloatingActionButton fabSaveFile;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private List<User> usersList;
    private List<Order> orderList;
    private DatabaseHandler db;
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private ManageOrdersAdapter manageOrdersAdapter;
    SessionManager sessionManager;
    public ManageOrdersFragment() {
        // Required empty public constructor
    }


    public static ManageOrdersFragment newInstance(String param1, String param2) {
        ManageOrdersFragment fragment = new ManageOrdersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_manage_orders, container, false);
        recyclerView = view.findViewById(R.id.recyclerView_display_orders);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        sessionManager = new SessionManager(getContext());
        db = new DatabaseHandler(getActivity(), new DatabaseHelper(getActivity()));

        orderList = new ArrayList<>();
        orderList = db.getAllOrders();
        Log.d("size", "onCreateView: "+orderList.size());
        fabSaveFile = view.findViewById(R.id.fab_save_file);
        fabSaveFile.setOnClickListener(v -> {
            List<User> users=db.getAllUsers();
            StringBuilder sb = null;
            for (User user:users){
                List<Order> orders = db.getAllOrders(user.getId());
                sb = new StringBuilder();

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


            }

           
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Orders");
            builder.setMessage(sb.toString());
            builder.setPositiveButton("Save as file", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    saveOrderAsTextFile();
                    dialog.dismiss();
                }
            });
            builder.show();
        });


        manageOrdersAdapter = new ManageOrdersAdapter(getContext(), orderList, this);
        recyclerView.setAdapter(manageOrdersAdapter);
        manageOrdersAdapter.notifyDataSetChanged();

        return  view;
    }

    @Override
    public void onOrderClick(Order o) {
        List<Order> orders = db.getAllOrders(o.getUserId());
        StringBuilder sb = new StringBuilder();

        for (Order order : orders) {
            if(o.getId()==order.getId()){
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


        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Orders");
        builder.setMessage(sb.toString());
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Dismiss the dialog
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void saveOrderAsTextFile() {
        List<Order> orders = db.getAllOrders(sessionManager.getUserId());

        StringBuilder contentBuilder = new StringBuilder();
        for (Order order : orders) {
            contentBuilder.append("Order ID: ").append(order.getId()).append("\n");
            contentBuilder.append("Product Name: ").append(order.getProductName()).append("\n");
            contentBuilder.append("Quantity: ").append(order.getQuantity()).append("\n");
            contentBuilder.append("Order Date: ").append(order.getOrderDate()).append("\n");
            contentBuilder.append("Order Status: ").append(order.getOrderStatus()).append("\n\n");
        }


        String content = contentBuilder.toString();

        File file = new File(getContext().getFilesDir(), "order_details.txt");
        try {
            FileWriter writer = new FileWriter(file);
            writer.append(content);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Snackbar.make(getView(), "Details for the order are saved as order_details.txt", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

}