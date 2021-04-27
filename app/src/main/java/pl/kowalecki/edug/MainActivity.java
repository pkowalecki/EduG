package pl.kowalecki.edug;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pl.kowalecki.edug.Data.ListGames;
import pl.kowalecki.edug.Data.UserAccount;
import pl.kowalecki.edug.Data.UserLogin;
import pl.kowalecki.edug.Data.WebServiceData;


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
    WebServiceData webServiceData = new WebServiceData();
    UserLogin userLogin = new UserLogin();
    UserAccount userAccount = new UserAccount();
    ListGames listGames = new ListGames();
    SessionManagement sessionManagement;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //mQueue = Volley.newRequestQueue(this);
        spinner=(Spinner) findViewById(R.id.spinner);
        loginInputField=(TextInputLayout) findViewById(R.id.loginField);
        emailaddress=(TextInputEditText) findViewById(R.id.emailaddress);
        emailaddress.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(this,R.drawable.ic_baseline_person_24), null);
        passwordInputField=(TextInputLayout) findViewById(R.id.passwordField);
        submit=(Button)findViewById(R.id.loginButton);
        userService = ApiUtils.getUserService();
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.refreshLayout);
        sessionManagement = new SessionManagement(getApplicationContext());
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
                userLogin.setSys("wp");
                userLogin.setLang("pl");
                userLogin.setGame(spinner.getSelectedItem().toString());
                userLogin.setLogin(loginInputField.getEditText().getText().toString());
                userLogin.setHash(md5Cipher.md5(passwordInputField.getEditText().getText().toString()));
                userLogin.setCrc(md5Cipher.md5(passwordGame+ userLogin.getSys()+ userLogin.getLang()+ userLogin.getGame()+ userLogin.getLogin()+ userLogin.getHash()));

               if(validateLogin() && validatePassword()){
                    doLogin(userLogin.getSys(), userLogin.getLang(), userLogin.getGame(), userLogin.getLogin(), userLogin.getHash(), userLogin.getCrc());
                   webServiceData.userAccountData(userLogin.getSys(), userLogin.getLang(), userLogin.getGame(), userLogin.getLogin(), userLogin.getHash(), userLogin.getCrc());

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
        if (sessionManagement.getLoginToEdug()){
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
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
                        sessionManagement.setLoginToEdug(true);
                        sessionManagement.setSys(sys);
                        sessionManagement.setLang(lang);
                        sessionManagement.setGame(game);
                        sessionManagement.setLogin(login);
                        sessionManagement.setHash(hash);
                        sessionManagement.setCRC(crc);
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        finish();
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
                            JSONObject gameObj = g.getJSONObject("game");
                            listGames.setIdg(gameObj.getString("idg"));
                            listGames.setActive(gameObj.getString("active"));
                            listGames.setMissions(gameObj.getString("missions"));


                            if (listGames.getActive().equals("Y")) {
                                listOfGames.add(listGames.getIdg());
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



