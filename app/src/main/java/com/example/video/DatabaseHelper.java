package com.example.video;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {


    private static final String MY_TABLE = "groups";
    private static final String COLUMN_NAME = "name";

    private static final String TABLE_USER = "User"; // Таблица "юзеры"
    private static final String USER_ID = "id"; // Поле "ид" в таблице "юзеры"
    private static final String USER_FIO = "FIO"; // Поле "ФИО" в таблице "юзеры"
    private static final String USER_GROUP_ID = "id_group"; // Поле "id_group" в таблице "юзеры"


    public static final String DATABASE_NAME = "YourDatabaseName.db"; // Имя вашей базы данных
    public static final int DATABASE_VERSION = 1; // Версия базы данных



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + MY_TABLE + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT);");
        db.execSQL("CREATE TABLE " + TABLE_USER + " (" +
                USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                USER_FIO + " TEXT, " +
                USER_GROUP_ID + " INTEGER, " +
                "FOREIGN KEY (" + USER_GROUP_ID + ") REFERENCES " + MY_TABLE + " (id));");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public static void delete(SQLiteDatabase db, String id) {
        String whereClause = "id=?";
        String[] whereArgs = new String[] { String.valueOf(id) };
        db.delete(MY_TABLE, whereClause, whereArgs);
        db.close();

    }
    public long insertGroup(String groupName) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, groupName);

        long result = db.insert(MY_TABLE, null, values);

        db.close();

        return result;
    }

    public void insertUser(String fio, int groupId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USER_FIO, fio);
        values.put(USER_GROUP_ID, groupId);

       db.insert(TABLE_USER, null, values);

        db.close();
    }
    public List<String> getAllGroups() {
        List<String> groupNames = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null) {

            Cursor cursor = db.rawQuery("SELECT id, " + COLUMN_NAME  + " FROM " + MY_TABLE, null);

            if (cursor.moveToFirst()) {
                do {
                    String groupName = cursor.getString(1); // Получаем название группы из первой колонки
                    int idGroup = cursor.getInt(0); // Получаем название группы из первой колонки
                    groupNames.add(idGroup + " : " + groupName);

                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();
        }

        return groupNames;
    }
    //если вы это читаете , не читайте
    //разрабы дауны
    public List<String> getUserTututu(String id)
    {
        Log.d("tage", String.valueOf(id));
        List<String> users = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT FIO FROM " + TABLE_USER+ " WHERE id_group = " + id , null);
        if (cursor.moveToFirst()) {
            do {
                String names = cursor.getString(0);
                users.add(names);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return users;




    }



    }



