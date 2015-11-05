package com.school42.rpuccine.hangouts;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.database.Cursor;
import android.widget.AdapterView;
import android.widget.ListView;
import android.database.DatabaseUtils;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.preference.PreferenceManager;

public class ContactListActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    SimpleCursorAdapter contactAdapter;
    private long nbContact;
    static final int ADD_CONTACT_CODE = 1;
    static final int DETAILS_CONTACT_CODE = 2;
    static final int SETTINGS_CODE = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set le fichier de preference av les valeurs par defaut si jamais set
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        // Set le theme en fonction du fichier de pref
        /*String them = PreferenceManager.getDefaultSharedPreferences(this)
                .getString(getApplicationContext().getString(R.string.pref_theme_key), "def");
        if (them.equals("1"))
            setTheme(R.style.AppTheme_NoActionBar);
        else if (them.equals("2"))
            setTheme(R.style.AppTheme_NoActionBar_Blue);
        else if (them.equals("3"))
            setTheme(R.style.AppTheme_NoActionBar_Hipster);*/
        ToolsClass.setTheme(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        // Set le context pour lancement activity
        final Context myCtx = this;

        // Gestion toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Gestion Bouton Add Contact (Start AddDetails_Activity)
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.contactListaddContact);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addContactIntent = new Intent(myCtx, AddContact.class);
                //startActivity(addContactIntent);
                startActivityForResult(addContactIntent, ADD_CONTACT_CODE);
            }
        });

        // Gestion Adapter pour la ListView
        String[] from = new String[] { ContactProvider.KEY_FIRSTNAME,
                ContactProvider.KEY_NAME,
                ContactProvider.KEY_TEL};
        int[] to = new int[] { R.id.contactRowFirstName,
                R.id.contactRowName,
                R.id.contactRowTel};
        contactAdapter =
                new SimpleCursorAdapter(this, R.layout.contact_row, null, from, to, 0);

        // Gestion ListView, connection a l'adapteur, init loader manager
        ListView cList = (ListView)findViewById(R.id.contactList);
        cList.setAdapter(contactAdapter);
        getLoaderManager().initLoader(0, null, this);
        nbContact = getNbContact();

        // Gestion click sur Item de la List (Start ContactDetails_Activity)
        cList.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View view,
                                            int position, long id) {
                        Intent detailContactIntent = new Intent(myCtx, ContactDetails.class);
                        detailContactIntent.setData(ContentUris.withAppendedId(ContactProvider.CONTENT_URI, id));
                        //startActivity(detailContactIntent);
                        startActivityForResult(detailContactIntent, DETAILS_CONTACT_CODE);
                    }
                }
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_CONTACT_CODE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                getLoaderManager().restartLoader(0, null, this);
                Toast.makeText(getApplicationContext(), R.string.toast_contact_added, Toast.LENGTH_LONG).show();

                Intent detailContactIntent = new Intent(getApplicationContext(), ContactDetails.class);
                detailContactIntent.setData(data.getData());
                startActivity(detailContactIntent);
            }
        }
        else if (requestCode == DETAILS_CONTACT_CODE) {
            // Make sure the request was successful
            if (resultCode == RESULT_FIRST_USER) {
                getLoaderManager().restartLoader(0, null, this);
                Toast.makeText(getApplicationContext(), R.string.toast_contact_deleted, Toast.LENGTH_LONG).show();
            }
        }
        else if (requestCode == SETTINGS_CODE) {
            // Make sure the request was successful
            recreate();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        String time = ToolsClass.getTime(this);
        Toast.makeText(getApplicationContext(), time, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        ToolsClass.saveTime(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.menuAddContact) {
            addContact();
            return true;
        }
        if (id == R.id.menuDelAllContact) {
            delAllContact();
            return true;
        }*/
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivityForResult(settingsIntent, SETTINGS_CODE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, ContactProvider.CONTENT_URI,
                new String[] { ContactProvider.KEY_ROWID,
                        ContactProvider.KEY_FIRSTNAME,
                        ContactProvider.KEY_NAME,
                        ContactProvider.KEY_TEL }, null, null, null);
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        contactAdapter.swapCursor(data);
    }

    public void onLoaderReset(Loader<Cursor> loader) {
        contactAdapter.swapCursor(null);
    }

    private void addContact() {
        ContentValues values = new ContentValues();
        values.put(ContactProvider.KEY_FIRSTNAME, "titi");
        values.put(ContactProvider.KEY_NAME, "toto " + nbContact);
        values.put(ContactProvider.KEY_TEL, "060606");
        values.put(ContactProvider.KEY_TELOFFICE, "020202");
        values.put(ContactProvider.KEY_MAIL, "prout@gmail.com");
        getContentResolver().insert(ContactProvider.CONTENT_URI, values);
        getLoaderManager().restartLoader(0, null, this);
        nbContact++;
    }

    private void delAllContact() {
        getContentResolver().delete(ContactProvider.CONTENT_URI, null, null);
        getLoaderManager().restartLoader(0, null, this);
        nbContact = 0;
    }

    private int getNbContact() {

        Cursor mCursor = getContentResolver().query(ContactProvider.CONTENT_URI, null, null, null, null);
        int res = mCursor.getCount();
        mCursor.close();
        return res;
    }
}
