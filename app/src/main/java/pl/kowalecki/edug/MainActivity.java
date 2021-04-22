package pl.kowalecki.edug;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MainActivity extends AppCompatActivity {


    private TextView mTextViewResult;
    SwipeRefreshLayout swipeRefreshLayout;
   // private RequestQueue mQueue;
    ArrayList<HashMap<String, String>> gamesList;
    private String TAG = MainActivity.class.getSimpleName();
    private Spinner spinner;
    private TextInputLayout loginInputField, passwordInputField;
    private TextInputEditText emailaddress;
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
        //mQueue = Volley.newRequestQueue(this);
        gamesList = new ArrayList<>();
        spinner=(Spinner) findViewById(R.id.spinner);
        loginInputField=(TextInputLayout) findViewById(R.id.loginField);
        emailaddress=(TextInputEditText) findViewById(R.id.emailaddress);
        emailaddress.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(this,R.drawable.ic_baseline_person_24), null);
        passwordInputField=(TextInputLayout) findViewById(R.id.passwordField);
        submit=(Button)findViewById(R.id.loginButton);
        userService = ApiUtils.getUserService();
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.refreshLayout);
        getGames();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getGames();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        submit.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String passwordGame = "grauman";
                String sys = "wp";
                String lang = "pl";
                String game = spinner.getSelectedItem().toString();
                String login = loginInputField.getEditText().getText().toString();
                String hash = md5Cipher.md5(passwordInputField.getEditText().getText().toString());
                String crc = md5Cipher.md5(passwordGame+sys+lang+game+login+hash);

               if(validateLogin() && validatePassword()){
                    doLogin(sys, lang, game, login, hash, crc);
                }


            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkSession();
    }

    private void checkSession() {
        SessionManagement sessionManagement = new SessionManagement(MainActivity.this);
        String userCRC = sessionManagement.getSession();
        if (!userCRC.equals("false")){
            Intent intent = new Intent(MainActivity.this, Home.class);
            startActivity(intent);
        }else{

        }
    }

    private boolean validateLogin(){
        String emailInput = loginInputField.getEditText().getText().toString().trim();
        if(emailInput.isEmpty()){
            loginInputField.setError("Pole nie może być puste");
            return false;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()){
            loginInputField.setError("Wprowadź poprawny adres e-mail");
            return false;
        }
        else{
            loginInputField.setError(null);
            return true;
        }

    }
    private boolean validatePassword(){
        String passwordInput = passwordInputField.getEditText().getText().toString().trim();
        if(passwordInput.isEmpty()){
            passwordInputField.setError("Pole nie może być puste");
            return false;
        }else{
            passwordInputField.setError(null);
            return true;
        }
    }
    private void doLogin(final String sys,final String lang,final String game,final String login,final String hash,final String crc){
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        executorService.execute(new Runnable() {
            @Override
            public void run() {

                HttpHandler sh = new HttpHandler();
                String url = userService.login(sys, lang, game, login, hash, crc).request().url().toString();
                Log.e(TAG,"URL DO ODPOWIEDZI " + url);
                String jsonStr = sh.makeServiceCall(url);

                Log.e(TAG, "Response from url: " + jsonStr);
                if(jsonStr != null){
                    try{
                        JSONObject jsonObject = new JSONObject(jsonStr);
                        JSONObject user_login = jsonObject.getJSONObject("user_login");

                            String result = user_login.getString("result");
                            String comment = user_login.getString("comment");

                            if (result.equals("true")) {
                        SessionManagement sessionManagement = new SessionManagement(MainActivity.this);
                        sessionManagement.saveSession(crc);

                        Intent intent = new Intent(MainActivity.this, Home.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        //intent.putExtra("username", login);
                        startActivity(intent);
                            } else{
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        builder.setCancelable(true);
                                        builder.setTitle("Błąd logowania");
                                        builder.setMessage("Wprowadzono niepoprawne dane");
                                        builder.setNegativeButton("ANULUJ", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        });
                                        builder.show();
                                    }
                                });

                            }
                        } catch (final JSONException e) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.e(TAG, "JSON parsing error: " + e.getMessage());
                            }
                        });
                    }
                    }
                else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.e(TAG, "Couldn't get json from server");
                        }
                    });
    }}});}

    private void getGames(){
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
                        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, listOfGames);
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(arrayAdapter);
                    }
                });
            }
        });
    }
}

//    private void doLogin(final String sys,final String lang,final String game,final String login,final String hash,final String crc){
//
//        Call call = userService.login(sys, lang, game, login, hash, crc);
//        Call callMovies = testService.allMovies();
//        callMovies.enqueue(new Callback() {
//            @Override
//            public void onResponse(Call call, Response response) {
//                Log.d("Call request", call.request().toString());
//                Log.d("Call request header", call.request().headers().toString());
//                Log.d("Response raw header", response.headers().toString());
//                Log.e(TAG, "response url: "+new Gson().toJson(response.body()) );
////                Log.d("Response raw", String.valueOf(response.raw().body()));
////                Log.d("Response code", String.valueOf(response.code()));
//                if (response.isSuccessful()){
//                    ResObj resObj = (ResObj) response.body();
//                    String result = resObj.getMessage();
//                    System.out.println("Odpowiedź result: " + result);
//                    if (resObj.getMessage().equals("true")){
//                        Intent intent = new Intent(MainActivity.this, Home.class);
//                        intent.putExtra("username", login);
//                        startActivity(intent);
//                    }else{
//                        Toast.makeText(MainActivity.this, "Wprowadziłeś niepoprawne dane", Toast.LENGTH_SHORT).show();
//                    }
//                }else{
//                    Toast.makeText(MainActivity.this, "Coś poszło nie tak", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call call, Throwable t) {
//            Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }


