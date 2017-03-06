package ru.solandme.scbtest1;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import ru.solandme.scbtest1.POJO.WebPage;

public class MyWebService extends IntentService {
    public static final String ACTION = "ru.solandme.scbtest1.MyWebService";

    public MyWebService() {
        super("MyWebService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        WebPage webPage = new WebPage();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ru.wikipedia.org/")
                .build();

        IWiki webService = retrofit.create(IWiki.class);
        Call<ResponseBody> call = webService.getWebpage();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM dd yyyy HH:mm:ss");
            String currentDataTime = sdf.format(new Date());

            webPage.setText(call.execute().body().string());
            webPage.setLastUpdate(currentDataTime);

        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent in = new Intent(ACTION);
        in.putExtra("resultCode", Activity.RESULT_OK);

        MyDbHelper helper = new MyDbHelper(getApplicationContext());
        helper.addWebPage(webPage);
        helper.close();

        LocalBroadcastManager.getInstance(this).sendBroadcast(in);
    }
}



