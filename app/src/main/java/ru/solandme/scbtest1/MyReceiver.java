package ru.solandme.scbtest1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyReceiver extends BroadcastReceiver {

    public static final int REQUEST_CODE = 12345;
    public static final String ACTION = "ru.solandme.scbtest1.MyWebService";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, MyWebService.class);
        context.startService(i);
    }
}
