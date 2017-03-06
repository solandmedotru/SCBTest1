package ru.solandme.scbtest1;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

public interface IWikipediaApi {
    @GET("wiki/Hello, world!")
    Call<ResponseBody> getWebpage();
}
