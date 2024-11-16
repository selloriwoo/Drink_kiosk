package com.example.finaltermproject20224227;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBhelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "DrinkCafe.db";
    private static final int DATABASE_VERSION = 1;

    public DBhelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create drinkKind table
        db.execSQL("CREATE TABLE drinkKind (" +
                "id INTEGER PRIMARY KEY," +
                "kind TEXT NOT NULL);");

        // Create drinkItem table
        db.execSQL("CREATE TABLE drinkItem (" +
                "id INTEGER PRIMARY KEY," +
                "name TEXT NOT NULL," +
                "pic TEXT," +
                "kindId INTEGER," +
                "price INTEGER," +
                "FOREIGN KEY (kindId) REFERENCES drinkKind(id));");

        // Create review table
        db.execSQL("CREATE TABLE review (" +
                "id INTEGER PRIMARY KEY," +
                "review TEXT," +
                "rating REAL," +
                "drinkItem INTEGER," +
                "FOREIGN KEY (drinkItem) REFERENCES drinkItem(id));");

        // Create order table
        db.execSQL("CREATE TABLE `order` (" +
                "id INTEGER PRIMARY KEY," +
                "orderDate DATE," +
                "drinkItemId INTEGER," +
                "FOREIGN KEY (drinkItemId) REFERENCES drinkItem(id));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS drinkKind;");
        db.execSQL("DROP TABLE IF EXISTS drinkItem;");
        db.execSQL("DROP TABLE IF EXISTS review;");
        db.execSQL("DROP TABLE IF EXISTS `order`;");
        onCreate(db);
    }
}
