package com.dalerleo.foodordering;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.dalerleo.foodordering.models.Food;
import com.dalerleo.foodordering.prefs.UserData;
import com.dalerleo.foodordering.views.MenuItemView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.mindorks.placeholderview.InfinitePlaceHolderView;

import java.util.List;
// CREATED BY DALER ABDULLAEV

public class TabFavorites extends Fragment {
  ChildEventListener childEventListener;
  private InfinitePlaceHolderView mLoadMoreView;
  String userName;
  ProgressBar progressBar;

  @Nullable
  @Override
  public View onCreateView(
    @NonNull LayoutInflater inflater,
    @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_favorites, container, false);

    // INITIALIZING LAYOUT COMPONENTS
    mLoadMoreView = view.findViewById(R.id.favList);
    progressBar = view.findViewById(R.id.progress);
    userName = new UserData().getUserPath(); // GET USERNAME PATH FOR DATABASE ACCESS
    setupView();
    return view;
  }

  private void setupView() {
    // GETTING ONLY FOODS WHICH HAS BEEN LIKED BY USER
    Query userFavFood = FirebaseDatabase
      .getInstance()
      .getReference()
      .child("favs")
      .child(userName); // GETTING USER FROM SHARED PREFERENCES

    childEventListener = new ChildEventListener() {
      @Override
      public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        // TURN LOADER OFF AS DATA REACHES TO THE DEVICE
        progressBar.setVisibility(View.INVISIBLE);
        // SET DATA TO FOOD OBJECT FROM FIREBASE NODE
        Food food = dataSnapshot.getValue(Food.class);
        // ADDING EVERY OBJECT TO LIST
        // PASS IS_FAV FLAG FOR AS FOOD CLASS USED MANY TIMES
        mLoadMoreView.addView(new MenuItemView(
          TabFavorites.this.getContext(),
          food,
          userName,
          true
        ));
      }

      @Override
      public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

      }

      @Override
      public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
        // IF OBJECT REMOVED FROM FAV LIST, REMOVE IT FROM LIST_VIEWS
        List<Object> list = mLoadMoreView.getAllViewResolvers();
        Food food = dataSnapshot.getValue(Food.class);
        if (food == null)
          return;

        for (Object ob : list) {
          if (ob instanceof MenuItemView)            // IF REMOVED OBJECT NAME EQUALS TO EXISTING OBJECT REMOVE FROM LIST
            if (((MenuItemView) ob).mInfo.getName().equals(food.getName()))
              mLoadMoreView.removeView(ob);
        };
      }

      @Override
      public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    };

    userFavFood.addChildEventListener(childEventListener);
  }
}

