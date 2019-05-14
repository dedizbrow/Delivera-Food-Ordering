package com.dalerleo.foodordering;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.dalerleo.foodordering.models.Order;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
// CREATED BY DALER ABDULLAEV


// MAIN ADMIN ACTIVITY FOR ADMIN PAGE
public class AdminActivity extends AppCompatActivity {
  private TabAdapter adapter;
  private TabLayout tabLayout;
  private ViewPager viewPager;
  DatabaseReference orderRef;
  ValueEventListener valueEventListener;
  Query lastQuery;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_admin);
    viewPager = findViewById(R.id.adminViewPager);
    tabLayout = findViewById(R.id.adminTabLayout);

    adapter = new TabAdapter(getSupportFragmentManager());
    // SETTING FRAGMENTS FOR TABULAR VIEW IN ADAPTER
    adapter.addFragment(new TabFoods(), "Food");
    adapter.addFragment(new TabOrder(), "Orders");
    adapter.addFragment(TabProfile.newInstance("Admin"), "Profile");
    // SETTING ADAPTER TO VIEW PAGER FOR TAB VIEW
    viewPager.setAdapter(adapter);
    tabLayout.setupWithViewPager(viewPager);
  }

  @Override
  protected void onPause() {
    super.onPause();
    // REMOVE LISTENER WHEN ACTIVITY IS DESTROYED
    lastQuery.removeEventListener(valueEventListener);
  }

  @Override
  protected void onResume() {

    super.onResume();
    // SET NOTIFICATION LISTENER WHEN ACTIVITY IS CREATED
    setNotification();
  }

  private void setNotification() {
    orderRef = FirebaseDatabase.getInstance().getReference();
    // GET LATEST CHANGE FROM ORDERS TABLE
    lastQuery = orderRef.child("orders").orderByKey().limitToLast(1);

    // LISTEN FOR CHANGE IN FIREBASE DATABASE
    valueEventListener = new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        Order lastOrder = new Order();
        for(DataSnapshot data : dataSnapshot.getChildren()) {
          lastOrder = data.getValue(Order.class);
        }
        // WITHIN THE SUCCESSFUL CHANGE IN DATABASE, SETUP NOTIFICATION FOR ADMIN
        int NOTIFICATION_ID = 234;

        if(!lastOrder.getName().isEmpty()) {
          NotificationManager notificationManager = (NotificationManager) AdminActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);

          String CHANNEL_ID = "delivera_channel_id";

          if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            // SETTING UP NOTIFICATION BUILDER FOR NOTIFICATION
            CharSequence name = "delivera_channel";
            String Description = "It is delivery notification channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(false);
            notificationManager.createNotificationChannel(mChannel);
          }

          NotificationCompat.Builder builder = new NotificationCompat.Builder(AdminActivity.this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle(lastOrder.getAmount() + " - " + lastOrder.getName()) // SETTING AMOUNT AND FOOD NAME FOR NOTIFICATION TITLE
            .setContentText("Address: " + lastOrder.getAddress()); // SETTING ADDRESS FOR DESCRIPTION

          Intent resultIntent = new Intent(AdminActivity.this, ClientActivity.class);
          TaskStackBuilder stackBuilder = TaskStackBuilder.create(AdminActivity.this);
          stackBuilder.addParentStack(ClientActivity.class);
          stackBuilder.addNextIntent(resultIntent);
          PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

          builder.setContentIntent(resultPendingIntent);

          notificationManager.notify(NOTIFICATION_ID, builder.build());
        }

      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    };
    lastQuery.addValueEventListener(valueEventListener);

  }

  public void AddFood(View view) {
    startActivity(new Intent(this, FoodCreate.class));
  }

  public void signOut(View view) {
    // SIGN OUT
    AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
      @Override
      public void onComplete(@NonNull Task<Void> task) {
        startActivity(new Intent(AdminActivity.this, LoginActivity.class));
      }
    });
  }



}
