package com.school42.rpuccine.hangouts;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.UriMatcher;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import java.lang.String;
import android.content.UriMatcher;
import android.net.Uri;
import android.text.TextUtils;

/**
 * Created by 5h55 on 12/10/15.
 */
public class ContactProvider extends ContentProvider {

    /**
     * Static String attributes
     */
    public static final String KEY_ROWID = "_id";
    public static final String KEY_FIRSTNAME = "first_name";
    public static final String KEY_NAME = "name";
    public static final String KEY_TEL = "tel";
    public static final String KEY_TELOFFICE = "tel_office";
    public static final String KEY_MAIL = "mail";
    public static final String DATABASE_NAME = "contacts_db";
    public static final String DATABASE_TABLE = "contacts";
    public static final int DATABASE_VERSION = 2;
    private static final String DATABASE_CREATE =
        "create table " + DATABASE_TABLE + " (" + KEY_ROWID + " integer primary key autoincrement, "
            + KEY_FIRSTNAME + " text not null, "
                + KEY_NAME + " text not null, "
                + KEY_TEL + " text not null, "
                + KEY_TELOFFICE + " text, "
                + KEY_MAIL + " text);";

    /**
     * URI String attributes
     */
    public static final String AUTHORITY = "contact_provider";
    public static final String CONTENT_URI_STRING = "content://"
        + AUTHORITY + "/" + DATABASE_TABLE;
    public static  final Uri CONTENT_URI = Uri.parse(CONTENT_URI_STRING);
    private static final int PEOPLE = 1;
    private static final int PEOPLE_ID = 2;

    /**
     * Object attributes
     */
    public ContactDbHelper myDbHelper;
    public SQLiteDatabase myDb;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(AUTHORITY, DATABASE_TABLE, PEOPLE);
        sUriMatcher.addURI(AUTHORITY, DATABASE_TABLE + "/#", PEOPLE_ID);
    }

    /**
     * DbHelper Class
     */
    private static class ContactDbHelper extends SQLiteOpenHelper {

        ContactDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(db);
        }
    }

    /**
     * Class Methods
     */

    public boolean onCreate() {
        myDbHelper = new ContactDbHelper(getContext());
        return  true;
    }

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        myDb = myDbHelper.getWritableDatabase();
        switch (sUriMatcher.match(uri)) {
            case 1:
                break;

            case 2:
                selection = selection + " _id=" + uri.getLastPathSegment();
                break;
        }
        return myDb.query(DATABASE_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
    }

    public Uri insert (Uri url, ContentValues values) {
        myDb = myDbHelper.getWritableDatabase();
        long insert_id = myDb.insert(DATABASE_TABLE, null, values);
        return ContentUris.withAppendedId(CONTENT_URI, insert_id);
    }

    public int delete (Uri uri, String where, String[] selectionArgs) {
        myDb = myDbHelper.getWritableDatabase();
        switch (sUriMatcher.match(uri)) {
            case 1:
                break;

            case 2:
                where = where + " _id=" + uri.getLastPathSegment();
                break;
        }
        return myDb.delete(DATABASE_TABLE, where, selectionArgs);
    }

    public int update (Uri uri, ContentValues values, String where, String[] selectionArgs) {
        myDb = myDbHelper.getWritableDatabase();
        switch (sUriMatcher.match(uri)) {
            case 1:
                break;

            case 2:
                where = where + " _id=" + uri.getLastPathSegment();
                break;
        }
        return myDb.update(DATABASE_TABLE, values, where, selectionArgs);
    }

    public String getType (Uri uri) {
        String ret;

        switch (sUriMatcher.match(uri)) {
            case 1:
                ret = "vnd.android.cursor.dir/vnd.";
                break;

            case 2:
                ret = "vnd.android.cursor.item/vnd.";
                break;
            default:
                return null;
        }
        return (ret + AUTHORITY + "." + DATABASE_TABLE);
    }

    public long createContact(String name, String tel) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_TEL, tel);

        return myDb.insert(DATABASE_TABLE, null, initialValues);
    }

    public boolean deleteContact(long rowId) {

        return myDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public boolean deleteAllContact() {

        return myDb.delete(DATABASE_TABLE, null, null) > 0;
    }


    public Cursor getAllContact() {

        return myDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_NAME,
                KEY_TEL}, null, null, null, null, null);
    }

    public Cursor getContact(long rowId) {

        Cursor mCursor =

                myDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                                KEY_NAME, KEY_TEL}, KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    public boolean updateContact(long rowId, String name, String tel) {
        ContentValues args = new ContentValues();
        args.put(KEY_NAME, name);
        args.put(KEY_TEL, tel);

        return myDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }
}
