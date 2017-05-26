package com.app.me.kapooktest.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.app.me.kapooktest.modelclass.ConstantModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SuRiV on 4/4/2560.
 */

public class TabsSqLiteHandle extends SQLiteOpenHelper {
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "db_kapook";

    // Contacts table name
    private static final String TABLE_TABS = "tabs";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";

    public TabsSqLiteHandle(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABS_TABLE = "CREATE TABLE " + TABLE_TABS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT"
                + ")";
        db.execSQL(CREATE_TABS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TABS);
        onCreate(db);
    }
    // Adding new contact
    public long addTabs(ConstantModel.TabsModel contact) {
        long new_id = -1;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getNameTabs()); // Contact Name

        // Inserting Row
        try {
            new_id = db.insert(TABLE_TABS, null, values);
        }catch (Exception sqlE){

            Log.e("Exception sqlE", "addTabs: ", sqlE);
        }


        db.close(); // Closing database connection
        return new_id;
    }

    // Getting single contact
    public ConstantModel.TabsModel getTabs(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_TABS, new String[] { KEY_ID,
                        KEY_NAME}, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        ConstantModel.TabsModel tabModel = new ConstantModel.TabsModel(Integer.parseInt(cursor.getString(0)), cursor.getString(1));
        // return contact
        return tabModel;

    }

    // Getting All Contacts
    public ArrayList<ConstantModel.TabsModel> getAllTabs() {
        ArrayList<ConstantModel.TabsModel> tabModelList = new ArrayList<ConstantModel.TabsModel>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TABS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ConstantModel.TabsModel tabModel = new ConstantModel.TabsModel(Integer.parseInt(cursor.getString(0)),cursor.getString(1).toString());

                // Adding contact to list
                tabModelList.add(tabModel);
            } while (cursor.moveToNext());
        }

        // return contact list
        return tabModelList;
    }

    // Getting contacts Count
    public int getTabsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_TABS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }
    // Updating single contact
    public int updateTabs( ConstantModel.TabsModel tab) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, tab.getNameTabs());


        // updating row
        return db.update(TABLE_TABS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(tab.getID()) });
    }

    // Deleting single contact
    public void deleteTabs( ConstantModel.TabsModel tab) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TABS, KEY_ID + " = ?",
                new String[] { String.valueOf(tab.getID()) });
        db.close();
    }


}
