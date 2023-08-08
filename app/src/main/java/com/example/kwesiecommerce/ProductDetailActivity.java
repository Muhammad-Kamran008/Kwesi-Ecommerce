package com.example.kwesiecommerce;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import com.example.kwesiecommerce.Authetication.SessionManager;
import com.example.kwesiecommerce.data.DatabaseHandler;
import com.example.kwesiecommerce.data.DatabaseHelper;
import com.google.android.material.snackbar.Snackbar;

public class ProductDetailActivity extends AppCompatActivity {

    private TextView productNameTextView;
    private TextView productDescriptionTextView, productNameTitleTextView;
    private TextView productPriceTextView;
    private TextView productQuantityTextView;
    private ImageView productImageView;
    SessionManager sessionManager;
    DatabaseHandler db;
    ImageView increment,decrement;
    int qnt=1;
    String productQuantity;

    private Button addToCartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        sessionManager = new SessionManager(getApplicationContext());
        productNameTitleTextView = findViewById(R.id.product_name_title_text_view);
        productNameTextView = findViewById(R.id.product_name_text_view);
        productDescriptionTextView = findViewById(R.id.product_description_text_view);
        productPriceTextView = findViewById(R.id.product_price_text_view);
        productQuantityTextView = findViewById(R.id.product_quantity);
        productImageView = findViewById(R.id.product_image_view);
        addToCartButton = findViewById(R.id.btnAddToCart);
        increment=findViewById(R.id.incbtn);
        decrement=findViewById(R.id.decbtn);

        ImageView btnBackToDashboard = findViewById(R.id.back_button_user_page);
        btnBackToDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Intent intent = getIntent();

        int productId = intent.getIntExtra("Product_id", -1);

        if (productId != -1) {
            db = new DatabaseHandler(getApplicationContext(),new DatabaseHelper(getApplicationContext()));

            Cursor cursor = db.read("products", null, "id = ?", new String[]{String.valueOf(productId)}, null, null, null, null);

            if (cursor.moveToFirst()) {
                String productName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String productDescription = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                String productPrice = cursor.getString(cursor.getColumnIndexOrThrow("price"));
                productQuantity = cursor.getString(cursor.getColumnIndexOrThrow("quantity"));
                String productImageUrl = cursor.getString(cursor.getColumnIndexOrThrow("image_url"));

                productNameTextView.setText(productName);
                productDescriptionTextView.setText(productDescription);
                productPriceTextView.setText(productPrice);
                productNameTitleTextView.setText(productName);

                Glide.with(this)
                        .load(productImageUrl)
                        .into(productImageView);
            }

            cursor.close();
        } else {
            Toast.makeText(this, "Product ID not found", Toast.LENGTH_LONG).show();
        }
        increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qnt=Integer.parseInt(productQuantityTextView.getText().toString());
                qnt++;
                productQuantityTextView.setText(String.valueOf(qnt));
            }
        });

        decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (qnt > 0) {
                    qnt--;
                    productQuantityTextView.setText(String.valueOf(qnt));
                }

            }
        });




        addToCartButton.setOnClickListener(v -> {

            int user_id = sessionManager.getUserId();
            int product_id = productId;

            if (db.isInCart(user_id, product_id)) {
                Snackbar.make(v, "Product already present in cart", Snackbar.LENGTH_SHORT).show();
            } else {
                if(Integer.parseInt(productQuantity)>=qnt){
                    if (db.insertCart(user_id, product_id,qnt)) {
                        Toast.makeText(v.getContext(), "Item added to Cart", Toast.LENGTH_SHORT).show();
                        recreate();
                    } else {
                        //Toast.makeText(v.getContext(), "Failed to add to Cart", Toast.LENGTH_SHORT).show();
                        Snackbar.make(v, "Failed to add to Cart", Snackbar.LENGTH_SHORT).show();
                    }

                }
                else{
                    Snackbar.make(v, "Only "+productQuantity+" items are present in the store.", Snackbar.LENGTH_SHORT).show();

                }

        }});

    }





}
