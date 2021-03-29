package pl.kowalecki.edug;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface UserService {

    @GET("sys={sys}&lang={lang}&game={game}&login={login}&hash={hash}&crc={crc}")
    Call <ResObj> login(@Path("sys") String sys, @Path("lang") String lang,@Path("game") String game,@Path("login") String login,@Path("hash") String hash,@Path("crc") String crc);
}
