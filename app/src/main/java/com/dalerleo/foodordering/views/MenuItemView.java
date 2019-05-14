package com.dalerleo.foodordering.views;

import android.content.Context;
import android.content.Intent;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.dalerleo.foodordering.FoodDetail;
import com.dalerleo.foodordering.R;
import com.dalerleo.foodordering.models.Food;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
// CREATED BY DALER ABDULLAEV


// DEFINE MENU_VIEW FOR EASY MANIPULATION
@Layout(R.layout.menu_item_view)
public class MenuItemView {

  DatabaseReference favsRef;
  private boolean isFav = false;
  private boolean isPressed = false;

  // INITIALIZE ALL LAYOUT COMPONENTS
  @View(R.id.titleTxt)
  protected TextView titleTxt;

  @View(R.id.priceTxt)
  protected TextView priceTxt;

  @View(R.id.imageView)
  protected ImageView imageView;

  @View(R.id.animation_view)
  protected LottieAnimationView lottieFav;

  @View(R.id.cardItem)
  protected LinearLayout cardItem;

  public Food mInfo;
  protected Context mContext;
  private  String username;
  private  boolean isFood;

  public MenuItemView(Context context, Food info, String username) {
    // MENU ITEM CONSTRUCTOR TAB_MENU
    mContext = context;
    mInfo = info;
    this.username = username;
  }

  public MenuItemView(Context context, Food info, boolean isFood) {
    // FOOD ITEM CONSTRUCTOR FOR FOOD ITEM VIEW
    mContext = context;
    mInfo = info;
    this.isFood = isFood;
  }

  public MenuItemView(Context context, Food info,String username, boolean isFav) {
    // FOOD ITEM CONSTRUCTOR FOR FAVORITE TAB
    mContext = context;
    mInfo = info;
    this.username = username;
    this.isFav = isFav;
    this.isPressed = true;
  }

  @Resolve
  protected void onResolved() {
    // GET FAVS OBJECTS REFERENCE
    favsRef = FirebaseDatabase.getInstance().getReference().child("favs");
    if(!isFood) {
      // IF NOT FOOD/ADMIN PAGE, MAKE CARD AVAILABLE FOR DETAILED ACCESS
      onCartClick();
    }
    if (isFood) {
      // IF FOOD HIDE FAVORITE ICON IN ADMIN
      lottieFav.setVisibility(android.view.View.GONE);
    }
    // SET REQUIRED INITIAL FRAME
    lottieFav.setMinProgress(0.6f);
    if(isFav) {
      // IF FAVORITE ITEM, MAKE ICON FAVORITE
      lottieFav.playAnimation();
    }

    lottieFav.setOnClickListener(new android.view.View.OnClickListener() {
      @Override
      public void onClick(android.view.View v) {
// HANDLE ON CLICK ON FAV_ICON
        // PREVENT CLICKING WHEN ITEM IS ANIMATING
        if (!lottieFav.isAnimating() && !username.isEmpty()) {
          if (!isPressed) {
            // IF PRESSED ADD IT TO FAV TABLE IN DATABAASE,
            // PLAY ANIMATION
            setFav();

            lottieFav.playAnimation();
            isPressed = true;
          } else {
            // IF UN_FAVED, REMOVE FROM DATABASE
            unsetFav();
            // SET ICON TO INITIAL STATE
            lottieFav.setProgress(0.6f);
            isPressed = false;
          }
        }
      }
    });

    // SET LAYOUT COMPOENTNS
    titleTxt.setText(mInfo.getName());
    priceTxt.setText(mInfo.getPriceCurrency());
    Glide.with(mContext).load(mInfo.getImage_url()).into(imageView);
  }

  void setFav() {
    // GET USERNAME AND SET FOOD HE LIKES TO FAVS_TABLE
    DatabaseReference newFav = favsRef.child(username).child(mInfo.getName());

    newFav.setValue(new Food(
      mInfo.getName(),
      mInfo.getImage_url(),
      mInfo.getPrice()
    ));
  }
  // REMOVE FOOD FROM FAV_TABLE
  void unsetFav() {
    favsRef.child(username).child(mInfo.getName()).removeValue();
  }

  // BY CLICKING ON MENU ITEM OPEN DETAILS, AND PASS DETAILS OF INFORAMION FOR FUTURE USE IN COMPONENT
  void onCartClick() {
    cardItem.setOnClickListener(new android.view.View.OnClickListener() {
      @Override
      public void onClick(android.view.View v) {
        Intent intent = new Intent(mContext, FoodDetail.class);
        intent.putExtra("name", mInfo.getName());
        intent.putExtra("image_url", mInfo.getImage_url());
        intent.putExtra("price", mInfo.getPriceCurrency());
        intent.putExtra("priceNum", mInfo.getPrice());
        intent.putExtra("content", mInfo.getContent());
        mContext.startActivity(intent);
      }
    });
  }
}



