package com.dalerleo.foodordering;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import com.dalerleo.foodordering.models.Order;
import com.dalerleo.foodordering.prefs.UserData;
import com.dalerleo.foodordering.views.OrderView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.mindorks.placeholderview.InfinitePlaceHolderView;

// CREATED BY JAVOKHIR ABDIRASHIDOV

public class OrderList extends AppCompatActivity {
  private InfinitePlaceHolderView mLoadMoreView;
  Query orderRef;
  ChildEventListener childEventListener;

  ProgressBar progressBar;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_order_list);
    // SETTING UP IU COMPOENENTS
    mLoadMoreView = findViewById(R.id.orderItems);
    progressBar = findViewById(R.id.progress);
    // SETTING UP LIST VIEW
    setupView();
  }

  private void setupView(){
    // GET REFERENCE OF ORDERS WHICH RELATES TO CURRENTLY LOGGED IN USER
    orderRef = FirebaseDatabase
      .getInstance()
      .getReference()
      .child("orders")
      .orderByChild("username")
      .equalTo(new UserData().getUsername());// GET USERNAME FROM SHARED PREFERENCES

    childEventListener = new ChildEventListener() {
      @Override
      public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        // DISABLE LOADING WHEN DATA REACHES THE DEVICE
        progressBar.setVisibility(View.GONE);
        // GET DATA FROM FIREBASE DATABASE AND CREATE ORDER INSTANCE
        Order order = dataSnapshot.getValue(Order.class);
        // ADD EVERY CHILD TO placeholderview LIST-VIEW
        // AS ORDER ORDER VIEW IS USED IN BOTH ADMIN AND CLIENT SIDE, NOTIFY ABOUT IT FO FUTURE USAGE
        mLoadMoreView.addView(new OrderView(OrderList.this, order, true));
      }

      @Override
      public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

      }

      @Override
      public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

      }

      @Override
      public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    };

    // SETTING LISTENER FOR DATABASE CHANGES
    orderRef.addChildEventListener(childEventListener);
  }
}
