package com.example.arihantjain.hitlist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

/**
 * Created by Arihant Jain on 12/29/2016.
 */

public class EnemyDatabaseHelper extends SQLiteOpenHelper{
    private static String TABLE_NAME = "Enemies_table";
    private static String DB_PATH = "/data/data/com.example.arihantjain.hitlist/databases/";
    private static String DB_NAME = "hit_list.sqlite";
    Context context;
    SQLiteDatabase database;

    public EnemyDatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    private boolean checkDatabase(){
        SQLiteDatabase checkDB = null;
        try {
            String dataPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(dataPath, null, SQLiteDatabase.OPEN_READWRITE);
        }catch (SQLiteException e){
            System.out.println("error in opening");
        }
        if(checkDB!=null){
            checkDB.close();
        }
        return checkDB!=null?true:false;
    }
    public void copyDatabse()throws IOException {
        InputStream inputStream = context.getAssets().open(DB_NAME);
        String outFile = DB_PATH + DB_NAME;
        OutputStream outputStream = new FileOutputStream(outFile);
        int length;
        byte[] buffer = new byte[1024];
        while ((length = inputStream.read(buffer))>0){
            outputStream.write(buffer,0,length);
        }
        outputStream.flush();
        outputStream.close();
        inputStream.close();
        System.out.println("Succes in copy");
    }
    public void openDataBase()throws SQLException {
        String dataPath = DB_PATH + DB_NAME;
        database = SQLiteDatabase.openDatabase(dataPath,null,SQLiteDatabase.OPEN_READWRITE);
        System.out.print("Database Opened Succesfully");
    }
    public void ExeSQLData(String sql)throws SQLException{
        database.execSQL(sql);
    }
    public Cursor QueryData(String query) throws SQLException{
        return database.rawQuery(query,null);
    }
    @Override
    public synchronized void close(){
        if(database!=null){
            database.close();
        }
        super.close();
    }
    public void checkAndCopyDatabase(){
        boolean dbExist = checkDatabase();
        if(dbExist){
            Log.d("Data","DataBase Already exist");

        }
        else {
            this.getReadableDatabase();
            try {
                copyDatabse();
            }catch (IOException e){
                Log.d("Data","error");
            }
        }
    }

    public boolean insertData(EnemyModel enemyModel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("NAME",enemyModel.getName());
        contentValues.put("HOUSE",enemyModel.getHouse());
        contentValues.put("PLACE",enemyModel.getPlaceToFind());
        contentValues.put("REASON",enemyModel.getReason());
        contentValues.put("IMAGE",enemyModel.getImage());
        contentValues.put("ALIVE",enemyModel.getAlive());
        long result = db.insert(TABLE_NAME, null, contentValues);
        if(result ==-1)
            return false;
        else
            return true;
    }

    public boolean updateData(EnemyModel enemyModel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("NAME",enemyModel.getName());
        contentValues.put("HOUSE",enemyModel.getHouse());
        contentValues.put("PLACE",enemyModel.getPlaceToFind());
        contentValues.put("REASON",enemyModel.getReason());
        contentValues.put("IMAGE",enemyModel.getImage());
        contentValues.put("ALIVE",enemyModel.getAlive());
        System.out.println("ID : "+enemyModel.getID());
        db.update(TABLE_NAME,contentValues,"id = ?",new String[]{enemyModel.getID()});
        return true;
    }
    public Integer delete(String Id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,"ID = ?",new String[]{Id});
    }
}
