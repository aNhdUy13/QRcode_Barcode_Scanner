package com.nda.new_qr_barcode_scanner;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import androidx.annotation.Nullable;

public class db extends SQLiteOpenHelper {
    public db(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public void QueryData(String sql)
    {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
        return;
    }
    public Cursor GetData(String sql)
    {
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql,null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void INSERT_OwnCode(String title, byte[] img_own)
    {
        SQLiteDatabase database  = getWritableDatabase();
        String sql = "INSERT INTO CodeTable VALUES(null,?,?)";
        SQLiteStatement statement =database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1,title);
        statement.bindBlob(2,img_own);

        statement.executeInsert();
    }
}
