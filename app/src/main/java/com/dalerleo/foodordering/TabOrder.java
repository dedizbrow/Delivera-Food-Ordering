package com.dalerleo.foodordering;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.dalerleo.foodordering.models.Food;
import com.dalerleo.foodordering.models.Order;
import com.dalerleo.foodordering.views.OrderView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mindorks.placeholderview.InfinitePlaceHolderView;

import java.util.ArrayList;
import java.util.List;
// CREATED BY JAVOKHIR ABDIRASHIDOV

public class TabOrder extends Fragment {
  private InfinitePlaceHolderView mLoadMoreView;
  DatabaseReference orderRef;
  ChildEventListener childEventListener;
  ProgressBar pb;
  FirebaseStorage mStore;
  StorageReference imageRef;
  @Nullable
  @Override
  public View onCreateView(
    @NonNull LayoutInflater inflater,
    @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState) {

    //INITIALIZING LAYOUT COMPONENTS AND INFLATING THE ORDER FRAGMENT
    View view = inflater.inflate(R.layout.fragment_orders, container, false);
    mLoadMoreView = view.findViewById(R.id.orderItems);
    pb = view.findViewById(R.id.progress);
    setupView();
    return view;
  }


  private void setupView(){
    //GETTING ORDERS THAT A CLIENT HAVE MADE TO ORDER FRAGMENT FROM FIREBASE
    orderRef = FirebaseDatabase.getInstance().getReference().child("orders");

    //APPEND NEW ORDERED FOODS TO THE FRAGMENT LAYOUT
    childEventListener = new ChildEventListener() {
      @Override
      public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        Order order = dataSnapshot.getValue(Order.class);
        String orderId = dataSnapshot.getKey();
        mLoadMoreView.addView(new OrderView(TabOrder.this.getContext(), order, orderId));
        pb.setVisibility(View.GONE);
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

    orderRef.addChildEventListener(childEventListener);

  }
}
