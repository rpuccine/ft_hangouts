package com.school42.rpuccine.hangouts;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class ContactDetails extends AppCompatActivity {

    static final int EDIT_CONTACT_CODE = 3;

    TextView firstName;
    TextView name;
    TextView tel;
    TextView telOffice;
    TextView mail;

    Uri myUri;

    private void setAttributes() {
        if (firstName == null) {
            firstName = (TextView)findViewById(R.id.contactDetailsFirstName);
            name = (TextView)findViewById(R.id.contactDetailsName);
            tel = (TextView)findViewById(R.id.contactDetailsTel);
            telOffice = (TextView)findViewById(R.id.contactDetailsTelOffice);
            mail = (TextView)findViewById(R.id.contactDetailsMail);
        }
        if (myUri == null) {
            Intent i = getIntent();
            myUri = i.getData();
        }
    }

    private void setContactField() {
        setAttributes();
        String[] col = {ContactProvider.KEY_FIRSTNAME,
                ContactProvider.KEY_NAME,
                ContactProvider.KEY_TEL,
                ContactProvider.KEY_TELOFFICE,
                ContactProvider.KEY_MAIL};
        Cursor resCursor = getContentResolver().query(myUri, col, "", null, null);
        resCursor.moveToFirst();
        firstName.setText(resCursor.getString(0));
        name.setText(resCursor.getString(1));
        tel.setText(resCursor.getString(2));
        telOffice.setText(resCursor.getString(3));
        mail.setText(resCursor.getString(4));
        resCursor.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set le theme en fonction du fichier de pref
        String them = PreferenceManager.getDefaultSharedPreferences(this)
                .getString(getApplicationContext().getString(R.string.pref_theme_key), "def");
        if (them.equals("1"))
            setTheme(R.style.AppTheme_NoActionBar);
        else if (them.equals("2"))
            setTheme(R.style.AppTheme_NoActionBar_Blue);
        else if (them.equals("3"))
            setTheme(R.style.AppTheme_NoActionBar_Hipster);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);

        // Gestion toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Gestion Bouton SMS
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.contactDetailsEdit);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*Snackbar.make(view, "Replace with your own action " + h + "h" + m + "m",
                        Snackbar.LENGTH_LONG).setAction("Action", null).show();*/

                Intent smsIntent = new Intent(getApplicationContext(), SmsListActivity.class);
                smsIntent.putExtra(SmsListActivity.EXTRA_PHONE, tel.getText());
                smsIntent.putExtra(SmsListActivity.EXTRA_FIRSTNAME, firstName.getText());
                startActivity(smsIntent);

            }
        });

        setContactField();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDIT_CONTACT_CODE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                setContactField();
                Toast.makeText(getApplicationContext(), R.string.toast_contact_edited, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent i = getIntent();
        Uri myUri = i.getData();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menuContactDetailsEdit) {
            Intent ContactEditIntent = new Intent(getApplicationContext(), EditContact.class);
            ContactEditIntent.setData(myUri);
            startActivityForResult(ContactEditIntent, EDIT_CONTACT_CODE);
            return true;
        }
        if (id == R.id.menuContactDetailsDel) {
            getContentResolver().delete(myUri, "", null);
            setResult(Activity.RESULT_FIRST_USER);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
