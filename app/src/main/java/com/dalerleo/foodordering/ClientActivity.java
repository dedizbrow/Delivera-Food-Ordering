package com.dalerleo.foodordering;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
// CREATED BY DALER ABDULLAEV

// CLIENT ACTIVITY
public class ClientActivity extends AppCompatActivity {

  private TabAdapter adapter;
  private TabLayout tabLayout;
  private ViewPager viewPager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    viewPager = (ViewPager) findViewById(R.id.viewPager);
    tabLayout = (TabLayout) findViewById(R.id.tabLayout);

    // SETTING FRAGMENTS FOR TABULAR
    adapter = new TabAdapter(getSupportFragmentManager());
    adapter.addFragment(new TabFavorites(), "Favorite");
    adapter.addFragment(new TabMenu(), "Menu");
    adapter.addFragment(TabProfile.newInstance("Main"), "Profile");

    viewPager.setAdapter(adapter);
    tabLayout.setupWithViewPager(viewPager);

  }


  public void signOut(View view) {
// SIGN OUT USER FROM FIREBASE AND FORWARD TO LOGIN_ACTIVITY FOR NEXT STEPS
    AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
      @Override
      public void onComplete(@NonNull Task<Void> task) {
        startActivity(new Intent(ClientActivity.this, LoginActivity.class));
      }
    });
  }
}
