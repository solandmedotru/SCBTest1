package ru.solandme.scbtest1;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import ru.solandme.scbtest1.POJO.WebPage;

public class MainActivity extends AppCompatActivity {
    WebView webView;
    TextView lastUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = (WebView) findViewById(R.id.webView);
        lastUpdate = (TextView) findViewById(R.id.lastUpdate);

        updateViews();
        scheduleAlarm();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_stop:
                cancelAlarm();
                return true;
            case R.id.action_start:
                scheduleAlarm();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateViews() {
        WebPage webPage;
        webPage = getWebPage();
        webView.getSettings().setDefaultTextEncodingName("utf-8");
        webView.loadData(webPage.getText(), "text/html; charset=utf-8", "utf-8");
        lastUpdate.setText(webPage.getLastUpdate());
    }

    public void scheduleAlarm() {
        Intent intent = new Intent(getApplicationContext(), MyReceiver.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, MyReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long firstMillis = System.currentTimeMillis();
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis,
                AlarmManager.INTERVAL_FIFTEEN_MINUTES, pIntent);
    }

    public void cancelAlarm() {
        Intent intent = new Intent(getApplicationContext(), MyReceiver.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, MyReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(MyWebService.ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(MyResultReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(MyResultReceiver);
    }

    private BroadcastReceiver MyResultReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int resultCode = intent.getIntExtra("resultCode", RESULT_CANCELED);
            if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), "New Date received", Toast.LENGTH_SHORT).show();
                updateViews();
            }
        }
    };

    private WebPage getWebPage() {
        MyDbHelper helper = new MyDbHelper(getApplicationContext());
        return helper.getLastWebPageFromDB();
    }
}
