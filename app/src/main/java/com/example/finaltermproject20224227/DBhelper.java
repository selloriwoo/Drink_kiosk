package com.example.finaltermproject20224227;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
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
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "kind TEXT NOT NULL);");

        // Create drinkItem table
        db.execSQL("CREATE TABLE drinkItem (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "pic TEXT," + // 파일 이름을 저장하기 위한 TEXT 타입
                "kindId INTEGER," +
                "price INTEGER," +
                "FOREIGN KEY (kindId) REFERENCES drinkKind(id));");

        // Create review table
        db.execSQL("CREATE TABLE review (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "review TEXT," +
                "rating REAL," +
                "drinkItem INTEGER," +
                "FOREIGN KEY (drinkItem) REFERENCES drinkItem(id));");

        // Create order table
        db.execSQL("CREATE TABLE `order` (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
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
        drinkItems.add(new DrinkItem("블랙라벨", saveImageToLocal(getBitmapFromDrawable(context, R.drawable.wisky_black_label), "블랙라벨"), 1, 70000));
        drinkItems.add(new DrinkItem("잭다니엘", saveImageToLocal(getBitmapFromDrawable(context, R.drawable.wisky_jackdaniel), "잭다니엘"), 1, 80000));
        drinkItems.add(new DrinkItem("제임슨", saveImageToLocal(getBitmapFromDrawable(context, R.drawable.wisky_jameson), "제임슨"), 1, 40000));
        drinkItems.add(new DrinkItem("와일드터키", saveImageToLocal(getBitmapFromDrawable(context, R.drawable.wisky_wildturkey), "와일드터키"), 1, 60000));
        drinkItems.add(new DrinkItem("앱솔루트", saveImageToLocal(getBitmapFromDrawable(context, R.drawable.vodka_absolut), "앱솔루트"), 2, 40000));
        drinkItems.add(new DrinkItem("밸루가", saveImageToLocal(getBitmapFromDrawable(context, R.drawable.vodka_beluga), "밸루가"), 2, 100000));
        drinkItems.add(new DrinkItem("에덴", saveImageToLocal(getBitmapFromDrawable(context, R.drawable.vodka_eden), "에덴"), 2, 50000));
        drinkItems.add(new DrinkItem("러시아 스텐다드 오리지널", saveImageToLocal(getBitmapFromDrawable(context, R.drawable.vodka_russian_standard_original), "러시아 스텐다드 오리지널"), 2, 50000));


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

    public void addItem(String imageName, String name, Long kindId, Integer price) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("pic", imageName);
        values.put("name", name);
        values.put("kindId", kindId);
        values.put("price", price);
        db.insert("drinkItem", null, values);
        db.close();
    }

    // Bitmap을 Drawable에서 가져오기
    private Bitmap getBitmapFromDrawable(Context context, int drawableId) {
        return BitmapFactory.decodeResource(context.getResources(), drawableId);
    }

    //todo 불로올 때 사진 말고 다른값도 보내주는 메서드도 추가
    //이미지를 로컬 저장소에 저장
    private String saveImageToLocal(Bitmap image, String imageName) {
        File directory = context.getDir("images", Context.MODE_PRIVATE);
        File imageFile = new File(directory, imageName + ".png");
        try (FileOutputStream fos = new FileOutputStream(imageFile)) {
            image.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageFile.getName(); // 파일 이름 반환
    }

    private Bitmap getImage(String imageName) {
        File directory = context.getDir("images", Context.MODE_PRIVATE);
        File imageFile = new File(directory, imageName + ".png");
        if (imageFile.exists()) {
            return BitmapFactory.decodeFile(imageFile.getAbsolutePath());
        } else {
            Log.e("getImage", "Image file not found: " + imageFile.getAbsolutePath());
            return null;
        }
    }

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
                String pic = cursor.getString(cursor.getColumnIndexOrThrow("pic"));
                int kindIdFromDb = cursor.getInt(cursor.getColumnIndexOrThrow("kindId"));
                int price = cursor.getInt(cursor.getColumnIndexOrThrow("price"));
                drinkItems.add(new DrinkItem(name, pic, kindIdFromDb, price)); // 파일 이름을 사용하여 DrinkItem 객체 생성
            }
            cursor.close();
        }
        db.close();
        return drinkItems;
    }

}