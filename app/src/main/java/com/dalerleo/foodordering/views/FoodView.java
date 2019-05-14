package com.dalerleo.foodordering.views;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dalerleo.foodordering.R;
import com.dalerleo.foodordering.models.Food;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
// CREATED BY JAVOKHIR ABDIRASHIDOV

// DEFINE FOOD_VIEW FOR EASY MANIPULATION
@Layout(R.layout.food_item_view)
public class FoodView {
// INITIALIZE ALL LAYOUT COMPONENTS
  @View(R.id.foodName)
  protected TextView titleTxt;

  @View(R.id.foodPrice)
  protected TextView priceTxt;

  @View(R.id.foodImage)
  protected ImageView imageView;

  protected Food mInfo;
  protected Context mContext;

  public FoodView(Context context, Food info) {
    // GET CONTEXT AND FOOD OBJECT
    mContext = context;
    mInfo = info;
  }

  @Resolve
  protected void onResolved() {
// SET FOOD INFORMATION TO LAYOUT COMPONENTS
    titleTxt.setText(mInfo.getName());
    priceTxt.setText(Long.toString(mInfo.getPrice()));
    // USE GLIDE LIBRARY FOR EASY USE OF IMAGES FROM NETWORK
    Glide.with(mContext).load(mInfo.getImage_url()).into(imageView);
  }
}
