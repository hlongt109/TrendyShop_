package com.trendyshopteam.trendyshop.utilities;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePreferencesManage {
    private static final String KEY_TABLE = "PositionUser";
    private static final String KEY_USERID = "UserID";
    private static final String KEY_POSITION = "Position";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public SharePreferencesManage(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(KEY_TABLE,Context.MODE_PRIVATE);
        editor  = sharedPreferences.edit();
    }

    public void setPosition(String userId, String position){
        editor.putString(KEY_USERID, userId);
        editor.putString(KEY_POSITION,position);
        editor.apply();
    }
    public String getUserId(){
        return sharedPreferences.getString(KEY_USERID,"");
    }
    public String getPosition() {
        return sharedPreferences.getString(KEY_POSITION,"");
    }

}
