package com.dalerleo.foodordering.app;
// CREATED BY DALER ABDULLAEV

// CLASS TO GET CONTEXT ANY WHERE NEEDED
import android.app.Application;

public  class App extends Application {
  private static App sInstance;

  @Override
  public void onCreate() {
    super.onCreate();
    this.sInstance = this;
  }

  public static App getsInstance() {
    if (sInstance == null) {
      synchronized (App.class) {
        if (sInstance == null) {
          sInstance = new App();
        }
      }
    }
    return  sInstance;
  }
}
