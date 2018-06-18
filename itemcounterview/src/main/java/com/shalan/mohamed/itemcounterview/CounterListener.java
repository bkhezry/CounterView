package com.shalan.mohamed.itemcounterview;

/**
 * Created by shalan on 11/18/17.
 */

public interface CounterListener {
  void onIncClick(String value,Integer valueInt);

  void onDecClick(String value,Integer valueInt);
}
