package com.example.kwesiecommerce.Authetication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kwesiecommerce.MainActivity;
import com.example.kwesiecommerce.Model.User;
import com.example.kwesiecommerce.R;
import com.example.kwesiecommerce.data.DatabaseHandler;
import com.example.kwesiecommerce.data.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {
    private EditText emailEditText, passwordEditText;
    private TextView signUpPromptTextView;
    private Button loginButton;
    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(this);
        String email = sessionManager.getUserEmail();

        if (!email.isEmpty()) {
            Intent dashboardIntent = new Intent(this, MainActivity.class);
            startActivity(dashboardIntent);
            finish();
        }

        emailEditText = findViewById(R.id.txtEmail);
        passwordEditText = findViewById(R.id.txtPassword);
        loginButton = findViewById(R.id.btnlogin);
        signUpPromptTextView = findViewById(R.id.txtVSignUpPrompt);

        String preEmail = getIntent().getStringExtra("email");
        emailEditText.setText(preEmail);

        dbHelper = new DatabaseHelper(this);

        DatabaseHandler db = new DatabaseHandler(this, DatabaseHelper.getInstance(this));
        db.createDefaultAdmin();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        signUpPromptTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUpIntent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(signUpIntent);
            }
        });
    }

    private void loginUser() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        DatabaseHandler db = new DatabaseHandler(this, DatabaseHelper.getInstance(this));
        User user = db.authenticateUser(email, password);

        if (user != null) {
            sessionManager.setUserEmail(user.getEmail());
            sessionManager.setUserPassword(user.getPassword());
            sessionManager.setUserType(user.getUsertype());
            sessionManager.setUserId(user.getId());
            sessionManager.setIsLoggedIn(true);

            Intent dashboardIntent = new Intent(this, MainActivity.class);
            dashboardIntent.putExtra("email", email);
            startActivity(dashboardIntent);
            finish();
        } else {
            Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
        }
    }
}
