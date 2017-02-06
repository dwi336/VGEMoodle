package de.baumann.hhsmoodle.data_Bookmarks;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;

import de.baumann.hhsmoodle.R;

public class Bookmarks_DbAdapter {

    //define static variable
    private static final int dbVersion =6;
    private static final String dbName = "bookmarks_DB_v01.db";
    private static final String dbTable = "bookmarks";

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context,dbName,null, dbVersion);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS "+dbTable+" (_id INTEGER PRIMARY KEY autoincrement, bookmarks_title, bookmarks_content, bookmarks_icon, bookmarks_attachment, bookmarks_creation, UNIQUE(bookmarks_title))");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+dbTable);
            onCreate(db);
        }
    }

    //establish connection with SQLiteDataBase
    private final Context c;
    private SQLiteDatabase sqlDb;

    public Bookmarks_DbAdapter(Context context) {
        this.c = context;
    }
    public void open() throws SQLException {
        DatabaseHelper dbHelper = new DatabaseHelper(c);
        sqlDb = dbHelper.getWritableDatabase();
    }

    //insert data
    @SuppressWarnings("SameParameterValue")
    public void insert(String bookmarks_title, String bookmarks_content, String bookmarks_icon, String bookmarks_attachment, String bookmarks_creation) {
        if(!isExist(bookmarks_title)) {
            sqlDb.execSQL("INSERT INTO bookmarks (bookmarks_title, bookmarks_content, bookmarks_icon, bookmarks_attachment, bookmarks_creation) VALUES('" + bookmarks_title + "','" + bookmarks_content + "','" + bookmarks_icon + "','" + bookmarks_attachment + "','" + bookmarks_creation + "')");
        }
    }
    //check entry already in database or not
    public boolean isExist(String bookmarks_title){
        String query = "SELECT bookmarks_title FROM bookmarks WHERE bookmarks_title='"+bookmarks_title+"' LIMIT 1";
        @SuppressLint("Recycle") Cursor row = sqlDb.rawQuery(query, null);
        return row.moveToFirst();
    }
    //check entry already in database or not
    @SuppressWarnings("SameParameterValue")
    boolean isExistFav(String bookmarks_attachment){
        String query = "SELECT bookmarks_attachment FROM bookmarks WHERE bookmarks_attachment='"+bookmarks_attachment+"' LIMIT 1";
        @SuppressLint("Recycle") Cursor row = sqlDb.rawQuery(query, null);
        return row.moveToFirst();
    }
    //edit data
    public void update(int id,String bookmarks_title,String bookmarks_content,String bookmarks_icon,String bookmarks_attachment, String bookmarks_creation) {
        sqlDb.execSQL("UPDATE "+dbTable+" SET bookmarks_title='"+bookmarks_title+"', bookmarks_content='"+bookmarks_content+"', bookmarks_icon='"+bookmarks_icon+"', bookmarks_attachment='"+bookmarks_attachment+"', bookmarks_creation='"+bookmarks_creation+"'   WHERE _id=" + id);
    }

    //delete data
    public void delete(int id) {
        sqlDb.execSQL("DELETE FROM "+dbTable+" WHERE _id="+id);
    }


    //fetch data
    public Cursor fetchAllData(Context context) {

        PreferenceManager.setDefaultValues(context, R.xml.user_settings, false);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        String[] columns = new String[]{"_id", "bookmarks_title", "bookmarks_content", "bookmarks_icon","bookmarks_attachment","bookmarks_creation"};

        if (sp.getString("sortDBB", "title").equals("title")) {
            return sqlDb.query(dbTable, columns, null, null, null, null, "bookmarks_title");

        } else if (sp.getString("sortDBB", "title").equals("icon")) {
            return sqlDb.query(dbTable, columns, null, null, null, null, "bookmarks_icon");

        } else if (sp.getString("sortDBB", "title").equals("create")) {
            return sqlDb.query(dbTable, columns, null, null, null, null, "bookmarks_creation");
        }

        return null;
    }

    //fetch data by filter
    Cursor fetchDataByFilter(String inputText,String filterColumn) throws SQLException {
        Cursor row;
        String query = "SELECT * FROM "+dbTable;
        if (inputText == null  ||  inputText.length () == 0)  {
            row = sqlDb.rawQuery(query, null);
        }else {
            query = "SELECT * FROM "+dbTable+" WHERE "+filterColumn+" like '%"+inputText+"%'";
            row = sqlDb.rawQuery(query, null);
        }
        if (row != null) {
            row.moveToFirst();
        }
        return row;
    }
}