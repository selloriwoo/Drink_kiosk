package com.example.finaltermproject20224227;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

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

        // 기본 술 아이템 추가
        ArrayList<DrinkItem> drinkItems = new ArrayList<>();
        drinkItems.add(new DrinkItem("블랙라벨", getBytesFromDrawable(context, R.drawable.wisky_black_label), 1, 70000));
        drinkItems.add(new DrinkItem("잭다니엘", getBytesFromDrawable(context, R.drawable.wisky_jackdaniel), 1, 80000));
        drinkItems.add(new DrinkItem("제임슨", getBytesFromDrawable(context, R.drawable.wisky_jameson), 1, 40000));
        drinkItems.add(new DrinkItem("와일드터키", getBytesFromDrawable(context, R.drawable.wisky_wildturkey), 1, 60000));
        drinkItems.add(new DrinkItem("앱솔루트", getBytesFromDrawable(context, R.drawable.bodka_absolut), 2, 40000));
        drinkItems.add(new DrinkItem("밸루가", getBytesFromDrawable(context, R.drawable.bodka_beluga), 2, 100000));
        drinkItems.add(new DrinkItem("에덴", getBytesFromDrawable(context, R.drawable.bodka_eden), 2, 50000));
        drinkItems.add(new DrinkItem("러시아 스텐다드 오리지널", getBytesFromDrawable(context, R.drawable.bodka_russian_standard_original), 2, 50000));


        //todo: 다른 술도 넣기.

        for (DrinkItem item : drinkItems) {
            ContentValues values = new ContentValues();
            values.put("name", item.getName());
            values.put("pic", item.getPic());
            values.put("kindId", item.getKindId());
            values.put("price", item.getPrice());
            db.insert("drinkItem", null, values);
        }

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

    //사진을 바이트로 변환
    public byte[] getBytesFromDrawable(Context context, int drawableId) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), drawableId);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    //술 종류에 따른 술들 불러오기
    public List<DrinkItem> getDrinkItemsByKindId(int kindId) {
        List<DrinkItem> drinkItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("drinkItem",
                new String[]{"name", "pic", "kindId", "price"},
                "kindId=?",
                new String[]{String.valueOf(kindId)},
                null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                byte[] pic = cursor.getBlob(cursor.getColumnIndexOrThrow("pic"));
                int kindIdFromDb = cursor.getInt(cursor.getColumnIndexOrThrow("kindId"));
                int price = cursor.getInt(cursor.getColumnIndexOrThrow("price"));
                drinkItems.add(new DrinkItem(name, pic, kindIdFromDb, price));
            }
            cursor.close();
        }
        db.close();
        return drinkItems;
    }
}
