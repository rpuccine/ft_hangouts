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
import android.widget.LinearLayout;
import android.widget.TextView;

public class SmsListActivity extends AppCompatActivity {

    static final int MY_PERMISSIONS_REQUEST_READ_SMS = 1;

    static final String EXTRA_PHONE = "com.school42.rpuccine.hangouts.phone";

    final String[] col = {Telephony.TextBasedSmsColumns.ADDRESS,
            Telephony.TextBasedSmsColumns.DATE_SENT,
            Telephony.TextBasedSmsColumns.BODY};

    final Uri smsUri = Telephony.Sms.CONTENT_URI;

    String selection = Telephony.TextBasedSmsColumns.ADDRESS + "=";
    String num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_list);

        Intent i = getIntent();
        Bundle extra = i.getExtras();
        num = extra.getString(EXTRA_PHONE);

        selection += num;


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
                    null,
                    Telephony.Sms.DEFAULT_SORT_ORDER);

            LinearLayout container = (LinearLayout) findViewById(R.id.smsListContainer);
            TextView test = new TextView(getApplicationContext());

            if (resCursor != null) {
                resCursor.moveToFirst();
                test.setText(resCursor.getString(2));
                resCursor.close();
            }
            else {
                test.setText("No contact found");
            }
            container.addView(test);
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
                            null,
                            Telephony.Sms.DEFAULT_SORT_ORDER);

                    LinearLayout container = (LinearLayout) findViewById(R.id.smsListContainer);
                    TextView test = new TextView(getApplicationContext());

                    if (resCursor != null) {
                        resCursor.moveToFirst();
                        test.setText(resCursor.getString(2));
                        resCursor.close();
                    }
                    else {
                        test.setText("No contact found");
                    }
                    container.addView(test);

                }
                else {

                    // permission denied, boo! Disable the
                    LinearLayout container = (LinearLayout) findViewById(R.id.smsListContainer);
                    TextView test = new TextView(getApplicationContext());
                    container.addView(test);
                }
                return;
            }

        }
    }
}
