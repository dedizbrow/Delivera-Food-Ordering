package com.dalerleo.foodordering;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dalerleo.foodordering.prefs.UserData;

// CREATED BY JAVOKHIR ABDIRASHIDOV
public class TabProfile extends Fragment {

 public static String PAGE_NAME="pageName";
 private String ADMIN = "Admin";
  TextView profileEmail;
  TextView profileName;
  Button onOrderList;
  UserData user;
  @Nullable
  @Override
  public View onCreateView(
    @NonNull LayoutInflater inflater,
    @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState) {
      //INITIALIZING LAYOUT COMPONENTS AND INFLATING PROFILE FRAGMENT

    final View view = inflater.inflate(R.layout.fragment_profile, container, false);
    user = new UserData();
    onOrderList = view.findViewById(R.id.onOrderList);
    profileEmail = view.findViewById(R.id.profileEmail);
    profileName = view.findViewById(R.id.profileName);
// IF ADMIN HIDE ORDERLIST BUTTON
    if(getArguments().getString(PAGE_NAME,"").equals(ADMIN)) {
      onOrderList.setVisibility(View.GONE);
    }

    //SETTING EMAIL AND NAME OF THE USER
    profileEmail.setText(user.getUsername());
    profileName.setText(user.getName());

    //IF ORDER LIST BUTTON IS CLICKED, THE NEW ORDER LIST ACTIVITY WILL START
    onOrderList.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(view.getContext(), OrderList.class));
      }
    });
    return view;
  }

  // IN ORDER TO  DIFFIRENTIATE FROM WHE TAB PROFILE CALLED, STATIC METHOD IS USER TO PASS THE ARGUMENT FROM PARENT CLASS
  public static TabProfile newInstance(String pageName) {
// SET PASSED ARGUMENT TO NEW OBJECTS
    Bundle args = new Bundle();
    args.putString(PAGE_NAME,pageName);
    // AND CREATED NEW OBJECT OF TAB_PROFILE
    TabProfile fragment = new TabProfile();
    fragment.setArguments(args);
    return fragment;
  }
}
