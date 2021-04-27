package pl.kowalecki.edug.Retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

    private static Retrofit retrofit;
    private static Gson gson;
    public static final String WEB_SERVICE_URL = "https://www.edug.pl/_webservices/";

    public static Retrofit getRetrofit(){
        gson = new GsonBuilder().create();


        if (retrofit==null){
            retrofit = new Retrofit.Builder().
                    baseUrl(WEB_SERVICE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}
