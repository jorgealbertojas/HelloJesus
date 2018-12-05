package com.example.jorge.hellojesus.util.download;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface InterfaceDownload {
    @GET()
    @Streaming
    Call<ResponseBody> downloadFile(@Url String url);
}
