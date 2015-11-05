package com.school42.rpuccine.hangouts;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by 5h55 on 03/11/15.
 */
public class ToolsClass {

    static final String KEY_TIME = "key_time";

    static public void setTheme(Context ctx) {
        String them = PreferenceManager.getDefaultSharedPreferences(ctx)
                .getString(ctx.getString(R.string.pref_theme_key), "def");
        if (them.equals("1"))
            ctx.setTheme(R.style.AppTheme_NoActionBar);
        else if (them.equals("2"))
            ctx.setTheme(R.style.AppTheme_NoActionBar_Blue);
        else if (them.equals("3"))
            ctx.setTheme(R.style.AppTheme_NoActionBar_Hipster);
        else if (them.equals("4"))
            ctx.setTheme(R.style.MatTheme_NoActionBar_Hipster);
    }

    static public void saveTime(Context ctx) {
        Calendar calendar = new GregorianCalendar();
        String time = "" + calendar.get(Calendar.HOUR_OF_DAY) + "h" + calendar.get(Calendar.MINUTE);

        SharedPreferences sharedPref =  PreferenceManager.getDefaultSharedPreferences(ctx);
        sharedPref.edit().putString(KEY_TIME, time).commit();
    }

    static public String getTime(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx)
                .getString(KEY_TIME, "End of Time");
    }
}
