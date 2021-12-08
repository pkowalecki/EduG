package pl.kowalecki.edug.Retrofit;


import pl.kowalecki.edug.Model.Achievements.ListAchievements;
import pl.kowalecki.edug.Model.Attendances.ListAttendances;
import pl.kowalecki.edug.Model.Badges.ListBadge;
import pl.kowalecki.edug.Model.Files.FilesList;
import pl.kowalecki.edug.Model.Games.GamesList;
import pl.kowalecki.edug.Model.Leaderboards.ListLeaderboards;
import pl.kowalecki.edug.Model.MissionFast.MissionFast;
import pl.kowalecki.edug.Model.MissionLabo.MissionLabo;
import pl.kowalecki.edug.Model.MissionSpec.MissionSpec;
import pl.kowalecki.edug.Model.Missions.Missions;
import pl.kowalecki.edug.Model.User.LoginResult;
import pl.kowalecki.edug.Model.User.UserData;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiRequest {

    @GET("list_games.php")
    Call<GamesList> listGames();

//    @GET("user_login.php")
//    Call <ResObj> login(@Query("sys") String sys, @Query("lang") String lang, @Query("game") String game, @Query("login") String login, @Query("hash") String hash, @Query("crc") String crc);
    @GET("user_login.php")
    Call <LoginResult> login(@Query("sys") String sys, @Query("lang") String lang, @Query("game") String game, @Query("login") String login, @Query("hash") String hash, @Query("crc") String crc);

    @GET("user_account.php")
    Call <UserData> userAccount(@Query("sys") String sys, @Query("lang") String lang, @Query("game") String game, @Query("login") String login, @Query("hash") String hash, @Query("crc") String crc);

    @GET("list_files.php")
    Call <FilesList> filesDataArray(@Query("idg") String idg);

    @GET("extra_leaderboards.php")
    Call <ListLeaderboards> extraLeaderboards(@Query("idg") String idg);

    @GET("extra_attendances.php")
    Call <ListAttendances> extraAttendances(@Query("idg") String idg, @Query("idu") String idu);

    @GET("extra_achievements.php")
    Call <ListAchievements> extraAchievements(@Query("idg") String idg, @Query("idu") String idu);

    @GET("extra_badges.php")
    Call <ListBadge> extraBadges(@Query("lang") String lang, @Query("idg") String idg, @Query("idu") String idu);

    @GET("list_missions.php")
    Call <Missions> listMissions(@Query("idg") String idg);

    @GET("mission_fast.php")
    Call <MissionFast> getFastMissionData(@Query("sys") String sys, @Query("lang") String lang, @Query("game") String game, @Query("mission") String mission, @Query("login") String login, @Query("hash") String hash, @Query("crc") String crc);

    @POST("mission_fast.php")
    Call <MissionFast> setFastMissionData(@Query("sys") String sys, @Query("lang") String lang, @Query("game") String game, @Query("mission") String mission, @Query("answer") String answer,  @Query("login") String login, @Query("hash") String hash, @Query("crc") String crc);

    @GET("mission_spec.php")
    Call <MissionSpec> getSpecMissionData(@Query("sys") String sys, @Query("lang") String lang, @Query("game") String game, @Query("mission") String mission, @Query("login") String login, @Query("hash") String hash, @Query("crc") String crc);

    @POST("mission_spec.php")
    Call <MissionSpec> setSpecMissionData(@Query("sys") String sys, @Query("lang") String lang, @Query("game") String game, @Query("mission") String mission, @Query("answer1") String answer1, @Query("answer2") String answer2, @Query("answer3") String answer3 , @Query("answer4") String answer4, @Query("login") String login, @Query("hash") String hash, @Query("crc") String crc);

    @GET("mission_labo.php")
    Call <MissionLabo> getLaboMissionData(@Query("sys") String sys, @Query("lang") String lang, @Query("game") String game, @Query("mission") String mission, @Query("login") String login, @Query("hash") String hash, @Query("crc") String crc);


}
