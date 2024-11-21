package com.example.finaltermproject20224227;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;

public class DBhelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "DrinkCafe.db";
    private static final int DATABASE_VERSION = 1;
    private final Context context;
    public DBhelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create drinkKind table
        db.execSQL("CREATE TABLE drinkKind (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," + // 자동 증가 설정
                "kind TEXT NOT NULL);");

        // Create drinkItem table
        db.execSQL("CREATE TABLE drinkItem (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," + // 자동 증가 설정
                "name TEXT NOT NULL," +
                "pic BLOB," + // Bitmap을 저장하기 위한 BLOB 타입
                "kindId INTEGER," +
                "price INTEGER," +
                "FOREIGN KEY (kindId) REFERENCES drinkKind(id));");

        // Create review table
        db.execSQL("CREATE TABLE review (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," + // 자동 증가 설정
                "review TEXT," +
                "rating REAL," +
                "drinkItem INTEGER," +
                "FOREIGN KEY (drinkItem) REFERENCES drinkItem(id));");

        // Create order table
        db.execSQL("CREATE TABLE `order` (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," + // 자동 증가 설정
                "orderDate DATE," +
                "drinkItemId INTEGER," +
                "FOREIGN KEY (drinkItemId) REFERENCES drinkItem(id));");

        //기본 술 종류
        for (DrinkCategory kind : DrinkCategory.values()) {
            ContentValues values = new ContentValues();
            values.put("kind", kind.toString());
            db.insert("drinkKind", null, values);
        }
        
        //todo:기본 술 추가

    }

    public boolean checkDatabase() {
        File dbFile = context.getDatabasePath(DATABASE_NAME);
        return dbFile.exists();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS drinkKind;");
        db.execSQL("DROP TABLE IF EXISTS drinkItem;");
        db.execSQL("DROP TABLE IF EXISTS review;");
        db.execSQL("DROP TABLE IF EXISTS `order`;");
        onCreate(db);
    }
    public void addItem(byte[] image, String name, Long kindId, Integer price) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("pic", image);
        values.put("name", name);
        values.put("kindId", kindId);
        values.put("price", price);
        //todo 인서트시 다른 속성 값 추가
        db.insert("drinkItem", null, values);
        db.close();
    }

    //todo 불로올 때 사진 말고 다른값도 보내주는 메서드도 추가
    public byte[] getImage(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("drinkItem", new String[]{"pic"}, "id" + "=?",
                new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            byte[] image = cursor.getBlob(0);
            cursor.close();
            return image;
        }
        return null;
    }
}
