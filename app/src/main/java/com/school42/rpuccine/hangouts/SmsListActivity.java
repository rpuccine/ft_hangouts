package com.school42.rpuccine.hangouts;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Point;
import android.net.Uri;
import android.provider.Telephony;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SmsListActivity extends AppCompatActivity {

    static final int MY_PERMISSIONS_REQUEST_READ_SMS = 1;

    static final String EXTRA_PHONE = "com.school42.rpuccine.hangouts.phone";

    final String[] col = {Telephony.TextBasedSmsColumns.ADDRESS,
            Telephony.TextBasedSmsColumns.TYPE,
            Telephony.TextBasedSmsColumns.DATE,
            Telephony.TextBasedSmsColumns.BODY};

    final Uri smsUri = Telephony.Sms.CONTENT_URI;

    String selection = Telephony.TextBasedSmsColumns.ADDRESS + "=";
    String order = Telephony.TextBasedSmsColumns.DATE + " ASC";
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

        selection = selection + "'" + num + "'";

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
                    order);

            // Construction et remplissage des sms
            buildSmsView(resCursor);
            resCursor.close();
            // Go to the end of the list
            final ScrollView scroll = (ScrollView) findViewById(R.id.smsListScrollView);
            scroll.post(new Runnable() {
                @Override
                public void run() {
                    //scroll.fullScroll(View.FOCUS_DOWN);
                    scroll.scrollTo(0, scroll.getBottom());
                }
            });
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
                            order);

                    // Construction et remplissage des sms
                    buildSmsView(resCursor);
                    resCursor.close();
                    // Go to the end of the list
                    final ScrollView scroll = (ScrollView) findViewById(R.id.smsListScrollView);
                    scroll.post(new Runnable() {
                        @Override
                        public void run() {
                            //scroll.fullScroll(View.FOCUS_DOWN);
                            scroll.scrollTo(0, scroll.getBottom());
                        }
                    });
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

    private void buildSmsView(Cursor resCursor) {

        LinearLayout smsListContainer = (LinearLayout) findViewById(R.id.smsListContainer);
        int nb_sms = 0;

        while (resCursor.moveToNext()) {

            // Creation du smsContainer et de ses textView
            LinearLayout smsContainer = new LinearLayout(this);
            TextView numView = new TextView(this);
            TextView dateView = new TextView(this);
            TextView bodyView = new TextView(this);

            // Set le champs des textView du sms
            numView.setText(resCursor.getString(resCursor.getColumnIndex(Telephony.TextBasedSmsColumns.ADDRESS)));
            Date date_mili = new Date(resCursor.getLong(resCursor.getColumnIndex(Telephony.TextBasedSmsColumns.DATE)));
            String formattedDate = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(date_mili);
            dateView.setText(formattedDate);
            bodyView.setText(resCursor.getString(resCursor.getColumnIndex(Telephony.TextBasedSmsColumns.BODY)));

            // Set color des textView
            numView.setTextColor(getResources().getColor(R.color.red));
            dateView.setTextColor(getResources().getColor(R.color.red));
            bodyView.setTextColor(getResources().getColor(R.color.black));

            // Set les params des textView
            LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            textViewParams.setMargins(15, 0, 15, 0);
            numView.setLayoutParams(textViewParams);
            dateView.setLayoutParams(textViewParams);
            bodyView.setLayoutParams(textViewParams);

            // Set la size et les params du smsContainer
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            LinearLayout.LayoutParams smsViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            smsViewParams.width = size.x - 300;
            smsViewParams.setMargins(10, 10, 10, 10);
            smsContainer.setOrientation(LinearLayout.VERTICAL);
            if (resCursor.getInt(resCursor.getColumnIndex(Telephony.TextBasedSmsColumns.TYPE)) == 1) {
                smsContainer.setBackgroundColor(getResources().getColor(R.color.blueAccent));
                smsViewParams.gravity = Gravity.LEFT;
            }
            else {
                smsContainer.setBackgroundColor(getResources().getColor(R.color.hipsterPrimaryDark));
                smsViewParams.gravity = Gravity.RIGHT;
            }
            smsContainer.setLayoutParams(smsViewParams);

            // Add les textViews au smsContainer
            smsContainer.addView(numView);
            smsContainer.addView(dateView);
            smsContainer.addView(bodyView);

            // Add du smsContainer a la liste
            smsListContainer.addView(smsContainer);

            nb_sms++;
        }
        if (nb_sms == 0) {
            TextView errorView = new TextView(this);
            errorView.setText("No contact sms found second");
            errorView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            smsListContainer.addView(errorView);
        }
    }
}
