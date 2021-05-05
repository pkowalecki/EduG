package pl.kowalecki.edug;


import java.util.ArrayList;
import java.util.List;

import pl.kowalecki.edug.Model.Files.FileData;
import pl.kowalecki.edug.Model.Files.FilesList;
import pl.kowalecki.edug.Model.Files.ListFile;
import pl.kowalecki.edug.Model.Leaderboards.ListLeaderboards;
import pl.kowalecki.edug.Model.User.UserData;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UserService {

    @GET("user_login.php")
    Call <ResObj> login(@Query("sys") String sys, @Query("lang") String lang, @Query("game") String game, @Query("login") String login, @Query("hash") String hash, @Query("crc") String crc);

    @GET("user_account.php")
    Call <UserData> userAccount(@Query("sys") String sys, @Query("lang") String lang, @Query("game") String game, @Query("login") String login, @Query("hash") String hash, @Query("crc") String crc);

    @GET("list_files.php")
    Call <FilesList> filesDataArray(@Query("idg") String idg);

    @GET("extra_leaderboards.php")
    Call <ListLeaderboards> extraLeaderboards(@Query("idg") String idg);






}
