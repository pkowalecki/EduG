package pl.kowalecki.edug;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManagement {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String SHARED_PREF_NAME= "session";
    String SESSION_CRC = "session_crc";
    public SessionManagement(Context context){
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveSession(String crc){

        editor.putString(SESSION_CRC, crc);
        editor.commit();
    }

    public String getSession(){

        return sharedPreferences.getString(SESSION_CRC, "false");
    }

    public void removeSession(){
        editor.putString(SESSION_CRC, "false").commit();
    }
}
