package com.example.kwesiecommerce.Authetication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kwesiecommerce.R;
import com.example.kwesiecommerce.data.DatabaseHandler;
import com.example.kwesiecommerce.data.DatabaseHelper;

import java.util.Random;

public class SignUpActivity extends AppCompatActivity {
    EditText firstNameEditText, lastNameEditText, emailEditText, passwordEditText, confirmPasswordEditText, memorableWordEditText;
    Button signUpButton;
    TextView alreadyAccountTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firstNameEditText = findViewById(R.id.edit_first_name);
        lastNameEditText = findViewById(R.id.edit_last_name);
        emailEditText = findViewById(R.id.txtEmail_signup);
        passwordEditText = findViewById(R.id.txtPasswordSignup);
        confirmPasswordEditText = findViewById(R.id.txtRepeat_PasswordSignup);
        memorableWordEditText = findViewById(R.id.txtMemorable_Word);
        signUpButton = findViewById(R.id.btnSignupClick);
        alreadyAccountTextView = findViewById(R.id.textView_already_account_from_signup);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUser();
            }
        });

        alreadyAccountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    public void addUser() {
        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();
        String memorableWord = memorableWordEditText.getText().toString();
        DatabaseHandler dbHandler = new DatabaseHandler(this, new DatabaseHelper(this));
        String username = generateUsername(firstName, lastName, dbHandler);

        if (!password.equals(confirmPassword)) {
            Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || memorableWord.isEmpty() || username.isEmpty()) {
            Toast.makeText(getApplicationContext(), "All fields must be filled", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put("first_name", firstName);
        values.put("last_name", lastName);
        values.put("email", email);
        values.put("password", password);
        values.put("memorable_word", memorableWord);
        values.put("username", username);
        values.put("user_type", "user");

        long result = dbHandler.create("users", values);

        if (result == -1) {
            Toast.makeText(getApplicationContext(), "Failed to register. Try again.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Registration successful! Login now", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            intent.putExtra("email", email);
            startActivity(intent);
        }
    }

    public static String generateUsername(String firstName, String lastName, DatabaseHandler dbHandler) {
        String namePart = (firstName.substring(0, Math.min(firstName.length(), 2)) +
                lastName.substring(0, Math.min(lastName.length(), 2))).toLowerCase();

        String username = "";
        boolean isUnique = false;

        while (!isUnique) {
            int uniqueNumber = new Random().nextInt(9000) + 1000;
            username = namePart + uniqueNumber;

            Cursor cursor = dbHandler.read("users", new String[]{"username"}, "username=?", new String[]{username}, null, null, null, null);
            if (cursor.getCount() == 0) {
                isUnique = true;
            }
            cursor.close();
        }

        return username;
    }
}
