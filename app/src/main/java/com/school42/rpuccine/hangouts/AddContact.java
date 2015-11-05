package com.school42.rpuccine.hangouts;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddContact extends AppCompatActivity {

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
        else if (them.equals("3"))
            setTheme(R.style.AppTheme_NoActionBar_Hipster);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        // Gestion toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firstName = (EditText)findViewById(R.id.addContactFirstName);
        name = (EditText)findViewById(R.id.addContactName);
        tel = (EditText)findViewById(R.id.addContactTel);
        telOffice = (EditText)findViewById(R.id.addContactTelOffice);
        mail = (EditText)findViewById(R.id.addContactMail);

        // Gestion bouton Done (Finish activity)
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addContactDone);
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
                    Toast.makeText(getApplicationContext(), R.string.toast_contact_validate, Toast.LENGTH_LONG).show();
                    return ;
                }

                // Si verif ok, insertion en BDD
                ContentValues values = new ContentValues();
                values.put(ContactProvider.KEY_FIRSTNAME, firstNameStr);
                values.put(ContactProvider.KEY_NAME, nameStr);
                values.put(ContactProvider.KEY_TEL, telStr);
                values.put(ContactProvider.KEY_TELOFFICE, telOfficeStr);
                values.put(ContactProvider.KEY_MAIL, mailStr);

                Uri resUri = getContentResolver().insert(ContactProvider.CONTENT_URI, values);
                Intent resultIntent = new Intent();
                setResult(Activity.RESULT_OK, resultIntent);
                resultIntent.setData(resUri);
                finish();
            }
        });

    }

    // Fonction verif des champs obligatoires
    protected boolean validate(String str) {
        if (str == null || str.isEmpty())
            return true;
        return false;
    }

}
