package pl.kowalecki.edug;


import android.service.autofill.UserData;

import pl.kowalecki.edug.Data.UserAccount;
import pl.kowalecki.edug.Data.UserDataTest;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UserService {

    @GET("user_login.php")
    Call <ResObj> login(@Query("sys") String sys, @Query("lang") String lang, @Query("game") String game, @Query("login") String login, @Query("hash") String hash, @Query("crc") String crc);

    @GET("user_account.php")
    Call <UserDataTest> userAccount(@Query("sys") String sys, @Query("lang") String lang, @Query("game") String game, @Query("login") String login, @Query("hash") String hash, @Query("crc") String crc);

}
