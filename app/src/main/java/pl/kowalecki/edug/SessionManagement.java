package pl.kowalecki.edug;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManagement {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String SHARED_PREF_NAME= "AppKey";
    String SESSION_SYS = "session_sys";
    String SESSION_LANG = "session_lang";
    String SESSION_GAME = "session_game";
    String SESSION_LOGIN = "session_login";
    String SESSION_HASH = "session_hash";
    String SESSION_CRC = "session_crc";
    public SessionManagement(Context context){
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
    }

    public void setLoginToEdug(boolean loginToEdug){
        editor.putBoolean("KEY_LOGIN", loginToEdug);
        editor.commit();
    }

    public boolean getLoginToEdug(){
        return sharedPreferences.getBoolean("KEY_LOGIN", false);
    }

    public void setSys(String sys){
        editor.putString(SESSION_SYS, sys);
        editor.commit();
    }

    public String getSys(){
        return sharedPreferences.getString(SESSION_SYS, "");
    }

    public void setLang(String lang){
        editor.putString(SESSION_LANG, lang);
        editor.commit();
    }

    public String getLang(){
        return sharedPreferences.getString(SESSION_LANG, "");
    }

    public void setGame(String game){
        editor.putString(SESSION_GAME, game);
        editor.commit();
    }

    public String getGame(){
        return sharedPreferences.getString(SESSION_GAME, "");
    }

    public void setLogin(String login){
        editor.putString(SESSION_LOGIN, login);
        editor.commit();
    }

    public String getLogin(){
        return sharedPreferences.getString(SESSION_LOGIN, "");
    }

    public void setHash(String hash){
        editor.putString(SESSION_HASH, hash);
        editor.commit();
    }

    public String getHash(){
        return sharedPreferences.getString(SESSION_HASH, "");
    }

    public void setCRC(String crc){
        editor.putString(SESSION_CRC, crc);
        editor.commit();
    }

    public String getCRC(){
        return sharedPreferences.getString(SESSION_CRC, "");
    }

    public void setNightModeState(Boolean state){
        editor.putBoolean("NightMode", state);
        editor.commit();
    }
    public boolean loadNightModeState(){
        return sharedPreferences.getBoolean("NightMode", false);
    }

}
