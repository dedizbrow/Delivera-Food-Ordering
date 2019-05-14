package com.dalerleo.foodordering;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.dalerleo.foodordering.prefs.UserData;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
// CREATED BY DALER ABDULLAEV

public class LoginActivity extends AppCompatActivity {
  // Choose an arbitrary request code value
  private static final int RC_SIGN_IN = 123;
  public static String USER_REF = "userRef";
  public static String USER_NAME = "userName";
  UserData prefUser;
  private String username;
  FirebaseAuth mAuth;
  FirebaseAuth.AuthStateListener mAuthStateListerner;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.login_activity);


    mAuth = FirebaseAuth.getInstance();

    mAuthStateListerner = new FirebaseAuth.AuthStateListener() {
      @Override
      public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        // IF USERS EXISTS THEN INITIALIZE USER
        if (user != null) {
          onSignInInit(user.getEmail(), user.getDisplayName());
        }
        else {
          // ELSE FORWARD USER TO SING_UP PAGE BY FIREBASE IU LIBRARY
          onSignOutClean();
          startActivityForResult(
            // Get an instance of AuthUI based on the default app
            AuthUI.getInstance()
              .createSignInIntentBuilder()
              .setIsSmartLockEnabled(false)
              .build(),
            RC_SIGN_IN);
        }
      }
    };

  }

  // CLEANING SHARED PREFERANCES WHEN LOGING OUT
  private void onSignOutClean() {
    prefUser = new UserData();
    prefUser.setName("");
    prefUser.setUsername("");
    username = "";
  }

  // SETTING UP SHARE PREFS FOR FUTURE USAGE
  // AND DEPENDING OF email, FORWARDING THEM TO REQUIRED ACTIVITY
  // IF USER EMAIL CONTAINTS "admin" STRING, THEN IT IS CONSIDERED AS ADMIN USER
  // ELSE SIMPLE CLIENT
  private void onSignInInit(String userName, String name) {
    this.username = userName;
    prefUser = new UserData();
    prefUser.setUsername(username);
    prefUser.setName(name);
    if (userName.contains("admin")) {
      Intent intent = new Intent(this, AdminActivity.class);
      startActivity(intent);
    }else {
      Intent intent = new Intent(this, ClientActivity.class);
      startActivity(intent);

    }

  }
  // CLEANING AND SETTING LISTENERS IS USED FOR EFFICIENCY MEMORY USAGE

  // SET LISTENER FOR FIREBASE - AUTH
  @Override
  protected void onResume() {
    super.onResume();
    mAuth.addAuthStateListener(mAuthStateListerner);
  }

  // CLEAR LISTENERS IF IT IS SET
  @Override
  protected void onPause() {
    super.onPause();
    if(mAuthStateListerner != null) {
      mAuth.removeAuthStateListener(mAuthStateListerner);
    }
  }

// SIGN OUT USERS FROM FIREBASE_UI WHEN REQUIRED
  public void signOut(View view) {
    AuthUI.getInstance().signOut(this);
  }


}
