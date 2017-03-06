package ru.solandme.scbtest1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import ru.solandme.scbtest1.POJO.WebPage;


public class MyDbHelper extends SQLiteOpenHelper {
    private static final String TAG = MyDbHelper.class.getSimpleName();
    static final String DATABASE_NAME = "database";
    static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "webpage";
    public static final String _ID = "_id";
    public static final String TEXT = "text";
    public static final String TIMESTAMP = "lastUpdate";


    // Creating table query
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TEXT + " TEXT NOT NULL, " + TIMESTAMP + " TEXT" + ");";

    public MyDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }

    public void saveWebPage(WebPage webPage) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(TEXT, webPage.getText());
            values.put(TIMESTAMP, webPage.getLastUpdate());

            db.insertOrThrow(TABLE_NAME, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add date to database");
        } finally {
            db.endTransaction();
        }
    }

    public WebPage getLastWebPageFromDB() {

        WebPage webPage = new WebPage();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME,
                null,
                null, null, null, null, null);

        if (cursor.getCount() > 0) {
            cursor.moveToLast();
            webPage.setText(cursor.getString(cursor.getColumnIndex(TEXT)));
            webPage.setLastUpdate(cursor.getString(cursor.getColumnIndex(TIMESTAMP)));
            cursor.close();
        } else {
            webPage.setLastUpdate("No data");
            webPage.setText("No data");
        }
        return webPage;
    }
}
