package com.school42.rpuccine.hangouts;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditContact extends AppCompatActivity {

    EditText firstName;
    EditText name;
    EditText tel;
    EditText telOffice;
    EditText mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set le theme en fonction du fichier de pref
        String them = PreferenceManager.getDefaultSharedPreferences(this)
                .getString(getApplicationContext().getString(R.string.pref_theme_key), "def");
        if (them.equals("1"))
            setTheme(R.style.AppTheme_NoActionBar);
        else if (them.equals("2"))
            setTheme(R.style.AppTheme_NoActionBar_Blue);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        // Gestion toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Connexion des champs utilisateurs
        firstName = (EditText)findViewById(R.id.editContactFirstName);
        name = (EditText)findViewById(R.id.editContactName);
        tel = (EditText)findViewById(R.id.editContactTel);
        telOffice = (EditText)findViewById(R.id.editContactTelOffice);
        mail = (EditText)findViewById(R.id.editContactMail);

        // Recup de l'uri depuis l'intent, BDD request puis set des champs user grace au data recup en BDD
        Intent i = getIntent();
        Uri myUri = i.getData();

        String[] col = {ContactProvider.KEY_FIRSTNAME,
                ContactProvider.KEY_NAME,
                ContactProvider.KEY_TEL,
                ContactProvider.KEY_TELOFFICE,
                ContactProvider.KEY_MAIL};

        Cursor resCursor = getContentResolver().query(myUri, col, "", null, null);
        resCursor.moveToFirst();
        firstName.setText(resCursor.getString(0), TextView.BufferType.EDITABLE);
        name.setText(resCursor.getString(1), TextView.BufferType.EDITABLE);
        tel.setText(resCursor.getString(2), TextView.BufferType.EDITABLE);
        telOffice.setText(resCursor.getString(3), TextView.BufferType.EDITABLE);
        mail.setText(resCursor.getString(4), TextView.BufferType.EDITABLE);

        resCursor.close();

        // Gestion Bouton done
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.editContactDone);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtention des champs utilisateurs et verif de ceux-ci
                String firstNameStr = firstName.getText().toString();
                String nameStr = name.getText().toString();
                String telStr = tel.getText().toString();
                String telOfficeStr = telOffice.getText().toString();
                String mailStr = mail.getText().toString();
                if (validate(firstNameStr) || validate(nameStr) || validate(telStr)) {
                    Toast.makeText(getApplicationContext(),
                            R.string.toast_contact_validate, Toast.LENGTH_LONG).show();
                    return ;
                }

                // Si verif ok, edit en BDD
                ContentValues values = new ContentValues();
                values.put(ContactProvider.KEY_FIRSTNAME, firstNameStr);
                values.put(ContactProvider.KEY_NAME, nameStr);
                values.put(ContactProvider.KEY_TEL, telStr);
                values.put(ContactProvider.KEY_TELOFFICE, telOfficeStr);
                values.put(ContactProvider.KEY_MAIL, mailStr);

                Uri myUri = getIntent().getData();
                getContentResolver().update(myUri, values, "", null);

                setResult(Activity.RESULT_OK);
                finish();
            }
        });

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                Uri myUri = getIntent().getData();
                upIntent.setData(myUri);
                NavUtils.navigateUpTo(this, upIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Fonction verif des champs obligatoires
    protected boolean validate(String str) {
        if (str == null || str.isEmpty())
            return true;
        return false;
    }

}
