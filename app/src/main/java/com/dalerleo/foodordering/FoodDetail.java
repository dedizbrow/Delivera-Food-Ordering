package com.dalerleo.foodordering;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.dalerleo.foodordering.models.Order;
import com.dalerleo.foodordering.prefs.UserData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
// CREATED BY JAVOKHIR ABDIRASHIDOV


// FOOD DETAILS AND ORDERING IN THEN SAME ACTIVITY FOR EASE OF USE
public class FoodDetail extends AppCompatActivity {

  TextView detailName;
  TextView totalPrice;
  TextView detailContent;
  TextView detailPrice;
  TextView detailAmount;
  EditText detailAddress;
  ImageView detailImage;
  MaterialButton onOrder;
  DatabaseReference orderRef;
  UserData userData;
  long priceNum;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_food_detail);
    // GETTING REFERENCE FOR ORDERS TABLE
    orderRef = FirebaseDatabase.getInstance().getReference().child("orders");
    // INITIALIZING USER_DATA HELPER
    userData = new UserData();
    detailAmount = findViewById(R.id.detailAmount);
    detailName = findViewById(R.id.detailName);
    totalPrice = findViewById(R.id.totalPrice);
    detailAddress = findViewById(R.id.detailAddress);
    detailContent = findViewById(R.id.detailContent);
    detailPrice = findViewById(R.id.detailPrice);
    detailImage = findViewById(R.id.detailImage);
    onOrder = findViewById(R.id.onOrder);
    Intent intent = getIntent();

    // GETTING ALL DATA PASSED FROM LIST OF FOOD
    String name = intent.getStringExtra("name");
    String price = intent.getStringExtra("price");
    priceNum = intent.getLongExtra("priceNum", 0);
    String imageUrl = intent.getStringExtra("image_url");
    String content = intent.getStringExtra("content");

    // SETTING PASSED DATA TO LAYOUT COMPONENTS
    detailName.setText(name);
    detailContent.setText(content);
    detailPrice.setText(price);
    totalPrice.setText(String.format("%s SUM", priceNum));
    Glide.with(this).load(imageUrl).into(detailImage);

    // LISTEN FOR ORDER CREATION
    onOrder.setOnClickListener(new android.view.View.OnClickListener() {
      @Override
      public void onClick(android.view.View v) {
        DatabaseReference newOrder = orderRef.push();
        long amount = Long.parseLong(detailAmount.getText().toString());
        // SETTING DATA IN DATABASE
        newOrder.setValue(new Order(
          userData.getUsername(),
          detailName.getText().toString(),
          detailAddress.getText().toString(),
          (int) amount,
          amount * priceNum,
          1
        )).addOnCompleteListener(new OnCompleteListener<Void>() {
          @Override
          public void onComplete(@NonNull Task<Void> task) {
            Toast.makeText(FoodDetail.this, "Successfully ordered", Toast.LENGTH_SHORT).show();
            // AFTER SUCCESS FORWARDING USER TO MAIN PAGE
            startActivity(new Intent(FoodDetail.this, ClientActivity.class));
          }
        });

      }
    });
  }

  // AMOUNT ADDER AND TOTAL PRICE CALCULATOR AT THE SAME TIME
  public void onAddAmount(View view) {
    int amount = Integer.parseInt(detailAmount.getText().toString());
    amount = amount + 1;
    totalPrice.setText(String.format("%s SUM", amount * priceNum));
    detailAmount.setText(String.format("%s", amount));
  }

  // AMOUNT sUBTRACTOR AND TOTAL PRICE CALCULATOR AT THE SAME TIME
  public void onSubtractAmount(View view) {

    int amount = Integer.parseInt(detailAmount.getText().toString());
    // USERS ARE NOT ALLOWED TO MAKE ORDERS' AMOUNT LOWER THAN 1
    if (amount > 1) {
      amount = amount - 1;
      totalPrice.setText(String.format("%s SUM", amount * priceNum));
      detailAmount.setText(String.format("%s", amount));

    }

  }
}
