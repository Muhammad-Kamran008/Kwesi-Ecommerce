package com.example.kwesiecommerce;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.kwesiecommerce.Authetication.SessionManager;
import com.example.kwesiecommerce.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(MainActivity.this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.navView, navController);

        Menu menu = binding.navView.getMenu();
        MenuItem nav_cart = menu.findItem(R.id.navigation_cart);
        if(sessionManager.getUserType().equals("admin")){
            nav_cart.setVisible(false);
        }
        else{
            nav_cart.setVisible(true);
        }
    }
}