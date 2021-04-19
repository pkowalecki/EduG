package pl.kowalecki.edug;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface UserService {
    //NAPRAWIĆ, ŻEBY "?" BYŁ PRZED LINKIEM

    //@POST("sys={sys}&lang={lang}&game={game}&login={login}&hash={hash}&crc={crc}")
    @GET("user_login.php")
    Call <ResObj> login(@Query("sys") String sys, @Query("lang") String lang, @Query("game") String game, @Query("login") String login, @Query("hash") String hash, @Query("crc") String crc);

}
