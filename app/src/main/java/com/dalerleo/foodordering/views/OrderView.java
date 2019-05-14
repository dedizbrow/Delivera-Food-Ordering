package com.dalerleo.foodordering.views;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.dalerleo.foodordering.R;
import com.dalerleo.foodordering.models.Order;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
// CREATED BY JAVOKHIR ABDIRASHIDOV


// ORDER VIEW MANIPULATOR
@Layout(R.layout.order_item_view)
public class OrderView {

  // INITIALIZING LAYOUT COMPONENTS
  @View(R.id.orderUser)
  protected TextView orderUser;

  @View(R.id.orderName)
  protected TextView orderName;

  @View(R.id.orderPrice)
  protected TextView orderPrice;

  @View(R.id.orderAmount)
  protected TextView orderAmount;

  @View(R.id.orderAddress)
  protected TextView orderAddress;

  @View(R.id.acceptBtn)
  protected MaterialButton acceptBtn;

  @View(R.id.actionButtons)
  protected LinearLayout actionButtons;

  @View(R.id.orderStatus)
  protected android.view.View orderStatus;

  @View(R.id.cancelBtn)
  protected MaterialButton cancelBtn;

  protected String id;
  protected boolean isClient;
  protected Order mInfo;
  protected Context mContext;
  private DatabaseReference orderItemRef;

 final int CANCELED = 2;
 final int ACCEPTED = 3;
 // CONSTRUCTOR FOR CLIENT ORDER LIST USAGE
  public OrderView(Context context, Order info, boolean isClient) {
    mContext = context;
    mInfo = info;
    this.isClient = isClient;
  }
  // CONSTRUCTOR FOR ORDER TABS USAGE
  public OrderView(Context context, Order info, String id) {
    mContext = context;
    mInfo = info;
    this.id = id;
  }

  @Resolve
  protected void onResolved() {

// IF CLIENT SIDE, HIDE ACTIONS BUTTONS, USERNAME, ORDER_AMOUNT
    if(isClient) {
      actionButtons.setVisibility(android.view.View.GONE);
      orderUser.setVisibility(android.view.View.GONE);
      orderAmount.setVisibility(android.view.View.GONE);
    }
    else {
      // GET EXACT ORDER FOR UPDATE ACTION
      orderItemRef = FirebaseDatabase
        .getInstance()
        .getReference()
        .child("orders")
        .child(id);
    }

    cancelBtn.setOnClickListener(new android.view.View.OnClickListener() {
      @Override
      public void onClick(android.view.View v) {
        // SET STATUS TO REJECTED AND UPDATE DATABASE
        mInfo.setStatus(2);
        orderItemRef.setValue(mInfo)
        .addOnCompleteListener(new OnCompleteListener<Void>() {
          @Override
          public void onComplete(@NonNull Task<Void> task) {
            orderStatus.setBackgroundColor(Color.parseColor(getColor(mInfo.getStatus())));
            Toast.makeText(mContext, "Order Cancelled", Toast.LENGTH_LONG).show();
          }
        });
      }
    });

    acceptBtn.setOnClickListener(new android.view.View.OnClickListener() {
      @Override
      public void onClick(android.view.View v) {
        // SET STATUS ACCEPTED AND UPDATE DATA_BASE
        mInfo.setStatus(3);
        orderItemRef.setValue(mInfo)
        .addOnCompleteListener(new OnCompleteListener<Void>() {
          @Override
          public void onComplete(@NonNull Task<Void> task) {
            orderStatus.setBackgroundColor(Color.parseColor(getColor(mInfo.getStatus())));
            Toast.makeText(mContext, "Order Accepted", Toast.LENGTH_LONG).show();
          }
        });
      }
    });

  // SET STATUS DEPENDING OF STATUS IN DATABASE
    orderStatus.setBackgroundColor(Color.parseColor(getColor(mInfo.getStatus())));
    // SET OTHER ELEMENTS
    orderPrice.setText(mInfo.getPriceCurrency());
    orderAddress.setText(mInfo.getAddress());
    // IF CLIENT SHOW NAME WITH THE AMOUNT OF ORDERED FOOD
    if(isClient) {
      orderName.setText(String.format("%d - %s", mInfo.getAmount(), mInfo.getName()));
    }else {
      // ELSE SHOW ALL INFORMATION IN SEQUENCE
      orderUser.setText(mInfo.getUsername());
      orderName.setText(mInfo.getName());
      orderAmount.setText(mInfo.getAmountText());
    }
  }

  // HELPER FUNCTION TO GET COLOR FOR INDICATOR
  private String getColor(int status){
    if (status == ACCEPTED) {
      return "#81c784";
    }
    if(status == CANCELED) return "#e57373";

    return "#f0ad4e";
  }
}
