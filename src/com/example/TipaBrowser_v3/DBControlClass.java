package com.example.TipaBrowser_v3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Dos on 24.06.2014.
 */
public class DBControlClass extends SQLiteOpenHelper {

    final String LOG_TAG = "myLogs";

    public DBControlClass(Context context) {
        super(context, "myDatabase", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d(LOG_TAG, "--- onCreate database ---");

        // create table
        sqLiteDatabase.execSQL("create table session ("
                + "id integer primary key, "
                + "pic text" + ");");

        sqLiteDatabase.execSQL("create table url ("
                + "id integer primary key, "
                + "ref_session integer, " + "url text, " +
                "url_no integer" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }

    public void insertSessionIntoDB(int id, String pic){
        // create object for data
        ContentValues contentValues = new ContentValues();

        // connect to database
        SQLiteDatabase database = this.getWritableDatabase();

        Log.d(LOG_TAG, "--- Insert in Session table: ---");

        contentValues.put("id", id);
        contentValues.put("pic", pic);

        long rowID = database.insert("session", null, contentValues);
        Log.d(LOG_TAG, "row inserted, ID = " + rowID);
    }

    public void insertUrlIntoDB(String url, int sessionNo, int urlNo){
        // create object for data
        ContentValues contentValues = new ContentValues();

        // connect to database
        SQLiteDatabase database = this.getWritableDatabase();

        Log.d(LOG_TAG, "--- Insert in Url table: ---");

        contentValues.put("url", url);
        contentValues.put("ref_session", sessionNo);
        contentValues.put("url_no", urlNo);

        long rowID = database.insert("url", null, contentValues);
        Log.d(LOG_TAG, "row inserted, ID = " + rowID);
    }

    public String readSessionFromDB(int sessionIndex){

        String tempPic = "";

        // connect to database
        SQLiteDatabase database = this.getWritableDatabase();

        Log.d(LOG_TAG, "--- Rows in Session Table: ---");
        Cursor cursor = database.query("session", null, null, null, null, null, null);

        if(cursor.moveToFirst()){
            int idColIndex = cursor.getColumnIndex("id");
            int picColIndex = cursor.getColumnIndex("pic");

            do{
                if(cursor.getInt(idColIndex) == sessionIndex){
                    tempPic = cursor.getString(picColIndex);
                    Log.d(LOG_TAG,
                            "ID = " + cursor.getInt(idColIndex) +
                                    ", pic = " + cursor.getString(picColIndex));
                    break;
                }

            } while (cursor.moveToNext());
        } else {
            Log.d(LOG_TAG, "0 rows");
        }
        cursor.close();
        return tempPic;
    }

    public String readUrlFromDB(int urlIndex, int sessionNo){

        String tempUrl = "";

        // connect to database
        SQLiteDatabase database = this.getWritableDatabase();

        Log.d(LOG_TAG, "--- INNER JOIN with query---");
        String table = "url as U inner join session as S on U.ref_session = S.id";
        String columns[] = {"U.url_no as Url_no", "S.pic as Pic", "U.url as Url", "S.id as Session_id"};
        String selection = "S.id = ?";
        String[] selectionArgs = {sessionNo + ""};
        Cursor cursor = database.query(table, columns, selection, selectionArgs, null, null, null);

        if(cursor.moveToFirst()){
            int idColIndex = cursor.getColumnIndex("Url_no");
            int picColIndex = cursor.getColumnIndex("Pic");
            int urlColIndex = cursor.getColumnIndex("Url");

            do{
                if(cursor.getString(idColIndex).equals(urlIndex+"")){
                    tempUrl = cursor.getString(urlColIndex);
                    Log.d(LOG_TAG,
                            "ID = " + cursor.getInt(idColIndex) +
                                    ", pic = " + cursor.getString(picColIndex) +
                                    ", url = " + cursor.getString(urlColIndex));
                    break;
                }

            } while (cursor.moveToNext());
        } else {
            Log.d(LOG_TAG, "0 rows");
        }
        cursor.close();
        return tempUrl;
    }

    public void deleteSessionDB(int sessionNo){
        // подключаемся к БД
        SQLiteDatabase database = this.getWritableDatabase();

        Log.d(LOG_TAG, "--- Delete from Session table: ---");

        // удаляем по id
        int delCount = database.delete("session", "id = " + sessionNo, null);
        int delCount2 = database.delete("url", "ref_session = " + sessionNo, null);
        Log.d(LOG_TAG, "deleted rows count = " + delCount);
    }

    public int findEmptySession(){

        int returnValue = 0;

        // connect to database
        SQLiteDatabase database = this.getWritableDatabase();

        Cursor cursor = database.query("session", null, null, null, null, null, null);

        if(cursor.moveToFirst()){
            int idColIndex = cursor.getColumnIndex("id");

            do{
                returnValue++;
                if(cursor.getInt(idColIndex) != returnValue){
                    Log.d(LOG_TAG, cursor.getInt(idColIndex) + " " + returnValue);
                    returnValue--;
                    break;
                }

            } while (cursor.moveToNext());
        } else {
            Log.d(LOG_TAG, " -- Empty session is 1 -- ");

        }
        Log.d(LOG_TAG, " -- Empty session is " + (returnValue+1) + " -- ");
        cursor.close();
        return returnValue+1;
    }

    public void readAllSessionsFromDB(){

        // connect to database
        SQLiteDatabase database = this.getWritableDatabase();

        Log.d(LOG_TAG, "--- Rows in Session Table: ---");
        Cursor cursor = database.query("session", null, null, null, null, null, null);

        if(cursor.moveToFirst()){
            int idColIndex = cursor.getColumnIndex("id");
            int picColIndex = cursor.getColumnIndex("pic");

            do{
                    Log.d(LOG_TAG,
                            "ID = " + cursor.getInt(idColIndex) +
                                    ", pic = " + cursor.getString(picColIndex));


            } while (cursor.moveToNext());
        } else {
            Log.d(LOG_TAG, "0 rows");
        }
        cursor.close();
    }

    public void readAllUrlsFromDB(){

        // connect to database
        SQLiteDatabase database = this.getWritableDatabase();

        Log.d(LOG_TAG, "--- Rows in Url Table: ---");
        Cursor cursor = database.query("url", null, null, null, null, null, null);

        if(cursor.moveToFirst()){
            int idColIndex = cursor.getColumnIndex("id");
            int urlColIndex = cursor.getColumnIndex("url");
            int sessionColIndex = cursor.getColumnIndex("ref_session");
            int urlNoColIndex = cursor.getColumnIndex("url_no");

            do{
                Log.d(LOG_TAG,
                        "ID = " + cursor.getInt(idColIndex) +
                                ", url = " + cursor.getString(urlColIndex) +
                                ", session = " + cursor.getString(sessionColIndex) +
                                ", url # = " + cursor.getString(urlNoColIndex));


            } while (cursor.moveToNext());
        } else {
            Log.d(LOG_TAG, "0 rows");
        }
        cursor.close();
    }
}
