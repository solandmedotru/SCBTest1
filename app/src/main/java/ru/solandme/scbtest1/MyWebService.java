package ru.solandme.scbtest1;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;

public class MyWebService extends IntentService {
    public static final String ACTION = "ru.solandme.scbtest1.MyWebService";

    public MyWebService() {
        super("MyWebService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String webPage = "No data";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ru.wikipedia.org/")
                .build();

        IWiki webService = retrofit.create(IWiki.class);
        Call<ResponseBody> call = webService.getWebpage();
        try {
            webPage = call.execute().body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent in = new Intent(ACTION);
        in.putExtra("resultCode", Activity.RESULT_OK);
        in.putExtra("resultValue", webPage);
        LocalBroadcastManager.getInstance(this).sendBroadcast(in);
    }
}



