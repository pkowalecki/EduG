package pl.kowalecki.edug.Model;

import android.os.Looper;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.os.Handler;


import pl.kowalecki.edug.ApiUtils;
import pl.kowalecki.edug.Activities.HomeActivity;
import pl.kowalecki.edug.HttpHandler;
import pl.kowalecki.edug.Model.User.UserAccount;
import pl.kowalecki.edug.Model.User.UserLogin;
import pl.kowalecki.edug.Retrofit.ApiRequest;

public class WebServiceData {

    ExecutorService executorService = Executors.newSingleThreadExecutor();
    HomeActivity homeActivity = new HomeActivity();
    ApiRequest apiRequest = ApiUtils.getUserService();
    UserAccount userAccount = new UserAccount();
    UserLogin userLogin = new UserLogin();
    private String TAG = WebServiceData.class.getSimpleName();

    public void userAccountData(final String sys, final String lang, final String game, final String login, final String hash, final String crc){
        final Handler handler = new Handler(Looper.getMainLooper());
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                HttpHandler sh = new HttpHandler();
                String url = apiRequest.userAccount(sys, lang, game, login, hash, crc).request().url().toString();
                final String jsonStr = sh.makeServiceCall(url);

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        Log.e(TAG, "Odpowiedź USER ACCOUNT:" + jsonStr);
                        if (jsonStr != null){
                            try{
                                JSONObject jsonObject = new JSONObject(jsonStr);
                                JSONObject userAccountJSON = jsonObject.getJSONObject("user_account");

                                userAccount.setResult(userAccountJSON.getBoolean("result"));
                                userAccount.setAgentNumber(userAccountJSON.getString("agent_number"));
                                userAccount.setAgentName(userAccountJSON.getString("agent_name"));
                                userAccount.setAgentEmail(userAccountJSON.getString("agent_email"));
                                userAccount.setGroupName(userAccountJSON.getString("group_name"));
                                userAccount.setCountBitcoin(userAccountJSON.getInt("count_bitcoin"));
                                userAccount.setCountAvatar(userAccountJSON.getInt("count_avatar"));
                                userAccount.setCountExacoin(userAccountJSON.getInt("count_exacoin"));
                                userAccount.setCountMission(userAccountJSON.getInt("count_mission"));
                                userAccount.setCountPoint(userAccountJSON.getInt("count_point"));
                                userAccount.setCountBadgesStyle(userAccountJSON.getInt("count_badges_style"));
                                userAccount.setLang(userAccountJSON.getString("lang"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        });


    }


}
