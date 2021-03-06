/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.calendar;

import android.app.Application;
import android.content.Context;
import android.preference.PreferenceManager;

//chenyulong 20120222 add start
import java.util.TimeZone;
import android.content.ContentValues;
import android.database.Cursor;
import android.provider.Calendar.Calendars;
import android.content.ContentResolver;
import com.sttm.charge.KsoExceptionHandler;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Calendar;
import android.provider.Settings;
import android.text.format.Time;
import android.util.Log;
//chenyulong 20120222 add end

public class CalendarApplication extends Application {

    // TODO: get rid of this global member.
    public Event currentEvent = null;

    /**
     * The Screen class defines a node in a linked list.  This list contains
     * the screens that were visited, with the more recently visited screens
     * coming earlier in the list.  The "next" pointer of the head node
     * points to the first element in the list (the most recently visited
     * screen).
     */
    static class Screen {
        public int id;
        public Screen next;
        public Screen previous;

        public Screen(int id) {
            this.id = id;
            next = this;
            previous = this;
        }

        // Adds the given node to the list after this one
        public void insert(Screen node) {
            node.next = next;
            node.previous = this;
            next.previous = node;
            next = node;
        }

        // Removes this node from the list it is in.
        public void unlink() {
            next.previous = previous;
            previous.next = next;
        }
    }

    public static final int MONTH_VIEW_ID = 0;
    public static final int WEEK_VIEW_ID = 1;
    public static final int DAY_VIEW_ID = 2;
    public static final int AGENDA_VIEW_ID = 3;

    public static final String[] ACTIVITY_NAMES = new String[] {
        MonthActivity.class.getName(),
        WeekActivity.class.getName(),
        DayActivity.class.getName(),
        AgendaActivity.class.getName(),
    };

    @Override
    public void onCreate() {
        super.onCreate();
        CalendarPreferenceActivity.setDefaultValues(this);
        addLocalAccount();//chenyulong 20120222 add
        KsoExceptionHandler crashHandler = KsoExceptionHandler.getInstance();    
        crashHandler.init(getApplicationContext());    
    }

//chenyulong 20120222 add start
    private void addLocalAccount() {
        ContentResolver cr = getContentResolver();
        Cursor cursor = cr.query(Calendars.CONTENT_URI, null, null, null, null);
        boolean createLocalAccount = true;
        if (cursor != null && cursor.getCount() != 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                String accountTypeStr = cursor.getString(cursor.getColumnIndexOrThrow(Calendars._SYNC_ACCOUNT_TYPE));
                if ("com.android.local".equals(accountTypeStr)){
                    createLocalAccount = false;
                    break;
                    }
                cursor.moveToNext();
                }
        }

        if ((cursor != null && cursor.getCount() == 0) || createLocalAccount) {
            ContentValues values = new ContentValues();
            values.put(Calendars._SYNC_ACCOUNT, "local@android.com");
            values.put(Calendars._SYNC_ACCOUNT_TYPE, "com.android.local");
            values.put(Calendars.NAME, "Local calendar");
            values.put(Calendars.DISPLAY_NAME, "Local");
            values.put(Calendars.HIDDEN, 0);
            values.put(Calendars.COLOR, 0xFF5691ea);
            values.put(Calendars.ACCESS_LEVEL, Calendars.ROOT_ACCESS);
            values.put(Calendars.SELECTED, 1);
            values.put(Calendars.SYNC_EVENTS, 1);
            values.put(Calendars.TIMEZONE, Time.getCurrentTimezone());
            values.put(Calendars.OWNER_ACCOUNT, "local@android.com");
            values.put(Calendars.ORGANIZER_CAN_RESPOND, 0);
            Uri uri =cr.insert(Calendars.CONTENT_URI, values);
            Log.d("chenyulong ", "CalendarApplication  134");
            if (uri != null) {
            	Log.d("chenyulong ", "CalendarApplication  136");
            }
        }
        Log.d("chenyulong ", "CalendarApplication  139");
        if (cursor != null) {
            cursor.close();
        }
}
//chenyulong 20120222 add end

}
