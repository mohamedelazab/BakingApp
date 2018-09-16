package mohamed.com.bakingapp.retrofit;

import android.content.Context;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {
    private static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/";
    private static Retrofit retrofit;
    public static Retrofit getApiClient(Context context){
        if(retrofit ==null){
            int cacheSize = 10 * 1024 * 1024; // 10 MB
            Cache cache = new Cache(context.getCacheDir(), cacheSize);

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .cache(cache)
                    .build();

            retrofit =new Retrofit.Builder().baseUrl(BASE_URL).client(okHttpClient).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }
}