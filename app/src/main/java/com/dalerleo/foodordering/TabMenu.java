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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mindorks.placeholderview.InfinitePlaceHolderView;
// CREATED BY JAVOKHIR ABDIRASHIDOV

import java.util.ArrayList;
import java.util.List;

public class TabMenu extends Fragment {
  private InfinitePlaceHolderView mLoadMoreView;
  private List<Food> foodList = new ArrayList<>();
  DatabaseReference foodsRef;
  ChildEventListener childEventListener;
  UserData userData;
  ProgressBar progressBar;
  @Nullable
  @Override
  public View onCreateView(
    @NonNull LayoutInflater inflater,
    @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState) {
    //INITIALIZING LAYOUT COMPONENTS AND INFLATING MENU FRAGMENT
    View view = inflater.inflate(R.layout.fragment_menu, container, false);
    mLoadMoreView = view.findViewById(R.id.loadMore);
    progressBar = view.findViewById(R.id.progress);

    return view;
  }


  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    setupView();
    userData = new UserData();
  }

  private void setupView(){
    //GET REFERENCE FOR FOOD OBJECT
    foodsRef = FirebaseDatabase.getInstance().getReference().child("foods");
    // IF NEW FOODS ARE ADDED BY ADMIN, THEY APPEAR ON MENU FRAGMENT
    childEventListener = new ChildEventListener() {
      @Override
      public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        progressBar.setVisibility(View.INVISIBLE);

        Food food = dataSnapshot.getValue(Food.class);
        //LOADING NEW MEALS IN TO THE MENU
        mLoadMoreView.addView(new MenuItemView(
          TabMenu.this.getContext(),
          food,
          userData.getUserPath()
        ));

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
    foodsRef.addChildEventListener(childEventListener);

  }
}
