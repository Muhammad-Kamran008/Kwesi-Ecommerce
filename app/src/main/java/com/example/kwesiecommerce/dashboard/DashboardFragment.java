package com.example.kwesiecommerce.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.example.kwesiecommerce.Admin.AdminActivity;
import com.example.kwesiecommerce.Authetication.LoginActivity;
import com.example.kwesiecommerce.Authetication.SessionManager;


import com.example.kwesiecommerce.Model.Category;
import com.example.kwesiecommerce.Model.Data;
import com.example.kwesiecommerce.Model.Product;
import com.example.kwesiecommerce.R;
import com.example.kwesiecommerce.data.DatabaseHandler;
import com.example.kwesiecommerce.data.DatabaseHelper;
import com.example.kwesiecommerce.databinding.FragmentDashboardBinding;
import com.example.kwesiecommerce.view.DashboardOuterRecylerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;


public class DashboardFragment extends Fragment {
    DrawerLayout drawerLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FragmentDashboardBinding binding;
    SessionManager sessionManager;
    DatabaseHandler db;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        db = new DatabaseHandler(getActivity(), new DatabaseHelper(getActivity()));
        db.createDefaultCategories();


        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        generateSampleData();
        sessionManager = new SessionManager(getActivity());
        drawerLayout = binding.drawerLayout;
        FloatingActionButton leftnav = binding.leftNav;
        leftnav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });


        Menu menu = binding.navView.getMenu();
        MenuItem nav_logout = menu.findItem(R.id.navigation_logout);
        nav_logout.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                sessionManager.clear();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
                return false;
            }
        });
        if (sessionManager.getIsLoggedIn()) {
            nav_logout.setVisible(true);
        } else {
            nav_logout.setVisible(false);
        }
        MenuItem nav_login = menu.findItem(R.id.admin_login);
        if (sessionManager.getUserType().equals("admin")) {
            nav_login.setVisible(true);
        } else {
            nav_login.setVisible(false);
        }


        NavigationView navigationView = binding.navView;
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == R.id.admin_login) {
                    Intent intent = new Intent(getActivity(), AdminActivity.class);
                    startActivity(intent);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    menuItem.setChecked(false);
                    return true;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return false;

            }
        });


        swipeRefreshLayout = binding.swipeRefreshLayout;
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh the data here
                generateSampleData();

                swipeRefreshLayout.setRefreshing(false);
            }
        });
        db = new DatabaseHandler(getActivity(), new DatabaseHelper(getActivity()));
        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void generateSampleData() {


        List<String> titles = new ArrayList<>();
        List<List<Data>> innerData = new ArrayList<>();

        titles.add("Categories");
        titles.add("All Products");

        List<Category> categories = db.readAllCategories();
        List<Product> products = db.getAllProducts();


        List<Data> categoryItem = new ArrayList<>();
        for (Category category : categories) {
            categoryItem.add(new Data(category.getName(), category.getImageUrl(), 0, category.getId()));
        }

        innerData.add(categoryItem);

        List<Data> productItem = new ArrayList<>();
        for (Product product : products) {
            productItem.add(new Data(product.getName(), product.getImageUrl(), product.getPrice(), product.getId()));

        }
        innerData.add(productItem);

        DashboardOuterRecylerView dashboardOuterRecylerView = new DashboardOuterRecylerView(titles, innerData, db, getContext());
        RecyclerView outerRecyclerView = binding.recyclerViewOuter;
        outerRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        outerRecyclerView.setAdapter(dashboardOuterRecylerView);
    }

}