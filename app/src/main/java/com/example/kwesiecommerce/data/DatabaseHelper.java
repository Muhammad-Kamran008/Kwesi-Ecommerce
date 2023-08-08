package com.example.kwesiecommerce.data;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static DatabaseHelper instance;
    private SQLiteDatabase db;
    private Context context;

    public DatabaseHelper(Context context) {
        super(context, "kwesiEcommerce.db", null, 1);
        this.context = context;
        this.db = this.getWritableDatabase();
    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    public SQLiteDatabase getDatabase() {
        return this.db;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "first_name TEXT, " +
                "last_name TEXT, " +
                "username TEXT UNIQUE, " +
                "password TEXT, " +
                "memorable_word TEXT, " +
                "user_type TEXT, " +
                "date_created DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "last_login DATETIME, " +
                "email TEXT UNIQUE, " +
                "phone_number TEXT, " +
                "address TEXT," +
                "profile_picture TEXT," +
                "last_updated DATETIME DEFAULT CURRENT_TIMESTAMP," +
                "hobbies TEXT)";

        String CREATE_CATEGORIES_TABLE = "CREATE TABLE categories (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL UNIQUE, " +
                "description TEXT, " +
                "parent_id INTEGER, " +
                "image_url TEXT, " +
                "FOREIGN KEY(parent_id) REFERENCES categories(id))";

        String CREATE_PRODUCTS_TABLE = "CREATE TABLE products (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "description TEXT, " +
                "price REAL NOT NULL, " +
                "currency TEXT DEFAULT '£', " +
                "sku TEXT UNIQUE, " +
                "quantity INTEGER NOT NULL DEFAULT 0, " +
                "category_id INTEGER, " +
                "image_url TEXT, " +
                "date_added DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "last_updated DATETIME, " +
                "active INTEGER NOT NULL DEFAULT 1, " +
                "FOREIGN KEY(category_id) REFERENCES categories(id))";

        String CREATE_CART_TABLE = "CREATE TABLE cart (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER NOT NULL, " +
                "product_id INTEGER NOT NULL, " +
                "quantity INTEGER NOT NULL DEFAULT 1, " +
                "date_added DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY(user_id) REFERENCES user(id), " +
                "FOREIGN KEY(product_id) REFERENCES products(id))";

        String CREATE_ORDERS_TABLE = "CREATE TABLE orders (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER NOT NULL, " +
                "order_date DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "order_status TEXT NOT NULL, " +
                "FOREIGN KEY(user_id) REFERENCES user(id))";

        String CREATE_ORDER_ITEMS_TABLE = "CREATE TABLE order_items (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "order_id INTEGER NOT NULL, " +
                "product_id INTEGER NOT NULL, " +
                "quantity INTEGER NOT NULL, " +
                "FOREIGN KEY(order_id) REFERENCES orders(id), " +
                "FOREIGN KEY(product_id) REFERENCES products(id))";

        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_CATEGORIES_TABLE);
        db.execSQL(CREATE_PRODUCTS_TABLE);
        db.execSQL(CREATE_CART_TABLE);
        db.execSQL(CREATE_ORDERS_TABLE);
        db.execSQL(CREATE_ORDER_ITEMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Upgrade database schema if needed
    }
}
