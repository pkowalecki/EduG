package pl.kowalecki.edug;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    private TextView mTextViewResult;
    private RequestQueue mQueue;
    ArrayList<HashMap<String, String>> gamesList;
    private String TAG = MainActivity.class.getSimpleName();
    private Spinner spinner;
    private EditText loginText, passwordText;
    private Button submit;
    MD5Cipher md5Cipher = new MD5Cipher();
    ArrayList<String> listOfGames = new ArrayList<>();
    UserService userService;
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mQueue = Volley.newRequestQueue(this);
        gamesList = new ArrayList<>();
        spinner=(Spinner) findViewById(R.id.spinner);
        loginText=(EditText) findViewById(R.id.loginField);
        passwordText=(EditText) findViewById(R.id.passwordField);
        submit=(Button)findViewById(R.id.loginButton);
        userService = ApiUtils.getUserService();
        getGamesTest();


        submit.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String passwordGame = "grauman";
                String sys = "wp";
                String lang = "pl";
                String game = spinner.getSelectedItem().toString();
                String login = loginText.getText().toString();
                String hash = md5Cipher.md5(passwordText.getText().toString());
                String crc = md5Cipher.md5(passwordGame+sys+lang+game+login+hash);
                if(validateLogin(login, hash)){
                    doLogin(sys, lang, game, login, hash, crc);
                }

            }
        });
    }

    private boolean validateLogin(String login, String hash){
        if (login == null || login.trim().length() == 0){
            Toast.makeText(this,"Login jest wymagany", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (hash == null || hash.trim().length() == 0){
            Toast.makeText(this, "Hasło jest wymagane", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private void doLogin(final String sys,final String lang,final String game,final String login,final String hash,final String crc){
        Call call = userService.login(sys, lang, game, login, hash, crc);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()){
                    ResObj resObj = (ResObj) response.body();
                    if (resObj.getMessage().equals("true")){
                        Intent intent = new Intent(MainActivity.this, Home.class);
                        intent.putExtra("username", login);
                        startActivity(intent);
                    } else{
                        Toast.makeText(MainActivity.this, "Wprowadziłeś niepoprawne dane", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(MainActivity.this, "Coś poszło nie tak", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
            Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }



    private void getGamesTest(){
        executorService.execute(new Runnable() {
            @Override
            public void run() {

                HttpHandler sh = new HttpHandler();

            String url = "https://www.edug.pl/_webservices/list_games.php";
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if(jsonStr != null){
                try{
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    JSONArray list_games = jsonObject.getJSONArray("list_games");

                    for(int i = 0; i < list_games.length(); i++){
                        JSONObject g = list_games.getJSONObject(i);
                        JSONObject game = g.getJSONObject("game");
                        String idg = game.getString("idg");
                        String active = game.getString("active");

                       // HashMap<String, String> hashMapActiveGames = new HashMap<>();

                        if (active.equals("Y")) {
                            listOfGames.add(idg);
                        //hashMapActiveGames.put("idg", idg);
                        //gamesList.add(hashMapActiveGames);
                        }
                    }
                } catch (final JSONException e){
                    Log.e(TAG, "JSON parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"JSON parsing error" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        else{
            Log.e(TAG, "Couldn't get json from server");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Couldnt get json from server. Check logcat", Toast.LENGTH_LONG).show();
                }
            });

        }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
// SpinnerAdapter spinnerAdapter = new SimpleAdapter(MainActivity.this, gamesList, R.layout.list_item, new String[]{"idg"},
//                    new int[]{R.id.idg});
//            spinner.setAdapter(spinnerAdapter);

            ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, listOfGames);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(arrayAdapter);
                    }
                });
            }
        });
}
}