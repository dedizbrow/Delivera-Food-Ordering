package com.dalerleo.foodordering;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;
// CREATED BY DALER ABDULLAEV

// TAB ADAPTER FOR USAGE OF TABULAR VIEW
public class TabAdapter extends FragmentStatePagerAdapter {
  private final List<Fragment> mFragmentList = new ArrayList<>();
  private final List<String> mFragmentTitleList = new ArrayList<>();

  public TabAdapter(FragmentManager fm) {
    super(fm);
  }

  @Override
  public Fragment getItem(int i) {
    return mFragmentList.get(i);
  }

  public void addFragment(Fragment fragment, String title) {
    // SETTING FRAGMENTS AND TITLE OF TABS
    mFragmentList.add(fragment);
    mFragmentTitleList.add(title);
  }
// OVERRIDE PAGER ADAPTER FUNCTIONS
  @Nullable
  @Override
  public CharSequence getPageTitle(int position) {
    return mFragmentTitleList.get(position);
  }


  @Override
  public int getCount() {
    return mFragmentList.size();
  }
}
