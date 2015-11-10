package com.school42.rpuccine.hangouts;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Telephony;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SmsListActivity extends AppCompatActivity {

    static final int MY_PERMISSIONS_REQUEST_READ_SMS = 1;

    static final String EXTRA_PHONE = "com.school42.rpuccine.hangouts.phone";

    final String[] col = {Telephony.TextBasedSmsColumns.ADDRESS,
            Telephony.TextBasedSmsColumns.DATE,
            Telephony.TextBasedSmsColumns.BODY};

    final Uri smsUri = Telephony.Sms.CONTENT_URI;

    String selection = Telephony.TextBasedSmsColumns.ADDRESS + "=";
    String num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_list);

        // Gestion toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent i = getIntent();
        Bundle extra = i.getExtras();
        num = extra.getString(EXTRA_PHONE);

        //selection = selection + num;
        selection = selection + "'+33661684346'";


        // Test permission et request de celle-ci si non.
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_SMS},
                    MY_PERMISSIONS_REQUEST_READ_SMS);

        }
        else {
            Cursor resCursor = getContentResolver().query(
                    smsUri,
                    col,
                    selection,
                    //null,
                    null,
                    Telephony.Sms.DEFAULT_SORT_ORDER);

            LinearLayout container = (LinearLayout) findViewById(R.id.smsListContainer);
            TextView num = new TextView(this);
            TextView date = new TextView(this);
            TextView body = new TextView(this);

            if (resCursor.moveToFirst()) {
                //resCursor.moveToFirst();
                num.setText(resCursor.getString(resCursor.getColumnIndex(Telephony.TextBasedSmsColumns.ADDRESS)));
                date.setText(resCursor.getString(resCursor.getColumnIndex(Telephony.TextBasedSmsColumns.DATE)));
                body.setText(resCursor.getString(resCursor.getColumnIndex(Telephony.TextBasedSmsColumns.BODY)));

                num.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                date.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                body.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));

                container.addView(num);
                container.addView(date);
                container.addView(body);

                resCursor.close();
            }
            else {
                TextView error = new TextView(this);
                error.setText(selection);
                error.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                container.addView(error);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_SMS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Cursor resCursor = getContentResolver().query(
                            smsUri,
                            col,
                            selection,
                            //null,
                            null,
                            Telephony.Sms.DEFAULT_SORT_ORDER);

                    LinearLayout container = (LinearLayout) findViewById(R.id.smsListContainer);
                    TextView num = new TextView(this);
                    TextView date = new TextView(this);
                    TextView body = new TextView(this);

                    if (resCursor.moveToFirst()) {
                        //resCursor.moveToFirst();
                        num.setText(resCursor.getString(resCursor.getColumnIndex(Telephony.TextBasedSmsColumns.ADDRESS)));
                        date.setText(resCursor.getString(resCursor.getColumnIndex(Telephony.TextBasedSmsColumns.DATE)));
                        body.setText(resCursor.getString(resCursor.getColumnIndex(Telephony.TextBasedSmsColumns.BODY)));

                        num.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));
                        date.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));
                        body.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));

                        container.addView(num);
                        container.addView(date);
                        container.addView(body);

                        resCursor.close();
                    }
                    else {
                        TextView error = new TextView(this);
                        error.setText("No contact sms found second");
                        error.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));
                        container.addView(error);
                    }

                }
                else {

                    // permission denied, boo! Disable the
                    LinearLayout container = (LinearLayout) findViewById(R.id.smsListContainer);
                    TextView test = new TextView(getApplicationContext());
                    test.setText("permission denied asshole");
                    //container.setId(255);
                    test.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    container.addView(test);
                }
                return;
            }

        }
    }
}
