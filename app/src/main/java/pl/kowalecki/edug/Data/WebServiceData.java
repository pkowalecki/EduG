package pl.kowalecki.edug.Data;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pl.kowalecki.edug.ApiUtils;
import pl.kowalecki.edug.HttpHandler;
import pl.kowalecki.edug.MainActivity;
import pl.kowalecki.edug.UserService;

public class WebServiceData {

    ExecutorService executorService = Executors.newSingleThreadExecutor();
    UserService userService;
    UserAccount userAccount;
    private String TAG = MainActivity.class.getSimpleName();

    public WebServiceData() {
        userService = ApiUtils.getUserService();
        userAccount = new UserAccount();
    }

    public void userAccountData(final String sys, final String lang, final String game, final String login, final String hash, final String crc){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                HttpHandler sh = new HttpHandler();
                String url = userService.userAccount(sys, lang, game, login, hash, crc).request().url().toString();
                String jsonStr = sh.makeServiceCall(url);

                Log.e(TAG, "Odpowiedź USER ACCOUNT:" + jsonStr);
                if (jsonStr != null){
                    try{
                        JSONObject jsonObject = new JSONObject(jsonStr);
                        JSONObject userAccountJSON = jsonObject.getJSONObject("user_account");

                        userAccount.setResult(userAccountJSON.getString("result"));
                        userAccount.setAgentNumber(userAccountJSON.getString("agent_number"));
                        userAccount.setAgentName(userAccountJSON.getString("agent_name"));
                        userAccount.setAgentEmail(userAccountJSON.getString("agent_email"));
                        userAccount.setGroupName(userAccountJSON.getString("group_name"));
                        userAccount.setCountBitcoin(userAccountJSON.getString("count_bitcoin"));
                        userAccount.setCountAvatar(userAccountJSON.getString("count_avatar"));
                        userAccount.setCountExacoin(userAccountJSON.getString("count_exacoin"));
                        userAccount.setCountMission(userAccountJSON.getString("count_mission"));
                        userAccount.setCountPoint(userAccountJSON.getString("count_point"));
                        userAccount.setCountBadges_style(userAccountJSON.getString("count_badges_style"));
                        userAccount.setLang(userAccountJSON.getString("lang"));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }}

        });
    }
}
