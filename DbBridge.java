package com.salesdashboard.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.webkit.JavascriptInterface;

import org.json.JSONObject;

/** App ka saara data SQLite (sales.db) me save hota hai. */
public class DbBridge {
    private static final String KEY = "salesAppV1";
    private final Helper helper;

    public DbBridge(Context context) {
        helper = new Helper(context.getApplicationContext());
    }

    private static class Helper extends SQLiteOpenHelper {
        Helper(Context c) {
            super(c, "sales.db", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE kv (k TEXT PRIMARY KEY, v TEXT)");
            db.execSQL("CREATE TABLE voice (k TEXT PRIMARY KEY, mime TEXT, data TEXT)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }

    @JavascriptInterface
    public synchronized String load() {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT v FROM kv WHERE k = ?", new String[]{KEY});
        try {
            return c.moveToFirst() ? c.getString(0) : "";
        } finally {
            c.close();
        }
    }

    @JavascriptInterface
    public synchronized boolean save(String json) {
        ContentValues cv = new ContentValues();
        cv.put("k", KEY);
        cv.put("v", json);
        helper.getWritableDatabase().insertWithOnConflict("kv", null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        return true;
    }

    @JavascriptInterface
    public synchronized boolean putVoice(String key, String mime, String b64) {
        ContentValues cv = new ContentValues();
        cv.put("k", key);
        cv.put("mime", mime);
        cv.put("data", b64);
        helper.getWritableDatabase().insertWithOnConflict("voice", null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        return true;
    }

    @JavascriptInterface
    public synchronized String getVoice(String key) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT mime, data FROM voice WHERE k = ?", new String[]{key});
        try {
            if (!c.moveToFirst()) return "";
            JSONObject o = new JSONObject();
            o.put("mime", c.getString(0));
            o.put("data", c.getString(1));
            return o.toString();
        } catch (Exception e) {
            return "";
        } finally {
            c.close();
        }
    }

    @JavascriptInterface
    public synchronized boolean delVoice(String key) {
        helper.getWritableDatabase().delete("voice", "k = ?", new String[]{key});
        return true;
    }
}
