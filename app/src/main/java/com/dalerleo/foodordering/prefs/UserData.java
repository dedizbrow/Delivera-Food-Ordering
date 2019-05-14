package com.dalerleo.foodordering.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.dalerleo.foodordering.app.App;
// CREATED BY DALER ABDULLAEV


// THIS HELPER CLASS IS USED TO EASELY MANIPULATE PREFERENCES NEEDED FOR US
public class UserData {
  private String PREF_NAME = "prefs";
  private String USER_NAME = "username";
  private String NAME = "name";
  private Context context;

  public UserData(){
    this.context = App.getsInstance().getApplicationContext();
  }

  // SETTING AND GETTING EMAIL ADDRESS
  public void setUsername(String username) {
    getSharedPrefs().edit().putString(USER_NAME, username).apply();
  }

  public String getUsername() {
    return getSharedPrefs().getString(USER_NAME, "");
  }

  // GETTING AND SETTING FULL NAME
  public void setName(String name) {
    getSharedPrefs().edit().putString(NAME, name).apply();
  }

  public String getName() {
    return getSharedPrefs().getString(NAME, "");
  }

  public String getUserPath() {
    String strArray[] = getUsername().split("@");
    return strArray[0];
  }



  public SharedPreferences getSharedPrefs() {
    return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
  }

}
