package com.shalan.mohamed.itemcounterview;

import android.app.Service;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by mohamed on 11/15/17.
 */

public class IncDecView extends RelativeLayout implements View.OnClickListener {
  private static final int VIBRATE_DELAY_MS = 125;
  private static final int VIBRATE_LENGTH_MS = 50;
  private Vibrator mVibrator;
  private long mLastVibrate;
  public static final String TAG = IncDecView.class.getSimpleName();
  private TextView itemCounterValue;
  private FloatingActionButton incButton;
  private FloatingActionButton decButton;
  private Drawable incIcon;
  private Drawable decIcon;
  private Drawable viewBackground;
  private RelativeLayout rootView;
  private int strokeWidthRef = 0;
  private float incDecViewBorderWidth;
  private int incDecViewBorderColor;
  private String incDecViewStartValue;
  private int incDecViewCounterValueColor;
  private float defWidthValue;
  private CounterListener listener;
  private Integer value = 0;
  private boolean persianNumber = false;
  private Integer maxValue = Integer.MAX_VALUE;
  private Integer minValue = Integer.MIN_VALUE;
  private int incDecButtonColor;

  public IncDecView(Context context) {
    super(context);
    def(context);
    init(context, null, 0);
  }

  public IncDecView(Context context, AttributeSet attrs) {
    super(context, attrs);
    def(context);
    init(context, attrs, 0);
  }

  public IncDecView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    def(context);
    init(context, attrs, defStyleAttr);
  }

  private void def(Context context) {
    this.incIcon = context.getResources().getDrawable(R.drawable.ic_add);
    this.decIcon = context.getResources().getDrawable(R.drawable.ic_remove);
    this.viewBackground = context.getResources().getDrawable(R.drawable.inc_dec_counter_view_background);
  }

  private void init(Context context, AttributeSet attrs, int defStyle) {
    mVibrator = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
    inflate(context, R.layout.item_counter_view, this);
    this.rootView = findViewById(R.id.root_view);
    this.itemCounterValue = findViewById(R.id.item_counter_value);
    this.incButton = findViewById(R.id.inc_button);
    this.decButton = findViewById(R.id.dec_button);
    this.incButton.setOnClickListener(this);
    this.decButton.setOnClickListener(this);
    if (attrs != null) {
      TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.IncDecView, defStyle, 0);
      try {
        this.incIcon = a.getDrawable(R.styleable.IncDecView_inc_icon);
        this.decIcon = a.getDrawable(R.styleable.IncDecView_dec_icon);
        this.viewBackground = a.getDrawable(R.styleable.IncDecView_view_background);
        this.incDecViewBorderWidth = a.getDimension(R.styleable.IncDecView_border_width, 0);
        this.incDecViewBorderColor = a.getColor(R.styleable.IncDecView_border_color, 0);
        this.incDecViewStartValue = a.getString(R.styleable.IncDecView_startCounterValue);
        this.incDecViewCounterValueColor = a.getColor(R.styleable.IncDecView_counterValueColor, 0);
        this.persianNumber = a.getBoolean(R.styleable.IncDecView_persianNumber, false);
        this.minValue = a.getInteger(R.styleable.IncDecView_minNumber, Integer.MIN_VALUE);
        this.maxValue = a.getInteger(R.styleable.IncDecView_maxNumber, Integer.MAX_VALUE);
        this.incDecButtonColor = a.getColor(R.styleable.IncDecView_inc_dec_button_color, 0);
      } catch (Exception e) {
        Log.i(TAG, "init: " + e.getLocalizedMessage());
      } finally {
        a.recycle();
      }
      if (this.incIcon != null) {
        this.incButton.setImageDrawable(this.incIcon);
      }
      if (this.decIcon != null) {
        this.decButton.setImageDrawable(this.decIcon);
      }
      if (this.incDecButtonColor != 0) {
        this.decButton.setBackgroundTintList(ColorStateList.valueOf(incDecButtonColor));
        this.incButton.setBackgroundTintList(ColorStateList.valueOf(incDecButtonColor));
      }
      if (this.viewBackground != null) {
        this.rootView.setBackgroundDrawable(this.viewBackground);
      }
      if (this.incDecViewBorderWidth != 0) {
        this.setBorderWidth(this.incDecViewBorderWidth);
      }
      if (this.incDecViewBorderColor != 0) {
        this.setBorderColor_(this.incDecViewBorderColor);
      }
      if (this.incDecViewStartValue != null) {
        if (this.persianNumber) {
          this.itemCounterValue.setText(PersianUtil.getPersianNumbers(this.incDecViewStartValue));
        } else {
          this.itemCounterValue.setText(this.incDecViewStartValue);
        }
        this.value = Integer.valueOf(this.incDecViewStartValue);
      } else {
        if (this.persianNumber) {
          this.itemCounterValue.setText(PersianUtil.getPersianNumbers(String.valueOf(this.value)));
        } else {
          this.itemCounterValue.setText(String.valueOf(this.value));
        }
      }
      if (this.incDecViewCounterValueColor != 0) {
        this.itemCounterValue.setTextColor(this.incDecViewCounterValueColor);
      }
    }
  }

  public IncDecView setStartCounterValue(String startValue) {
    if (this.itemCounterValue != null) {
      if (this.persianNumber) {
        this.itemCounterValue.setText(PersianUtil.getPersianNumbers(this.incDecViewStartValue));
      } else {
        this.itemCounterValue.setText(this.incDecViewStartValue);
      }
      this.value = Integer.valueOf(startValue);
    }
    return this;
  }

  public IncDecView setStartCounterValue(@StringRes int startValue) {
    if (this.itemCounterValue != null) {
      if (this.persianNumber) {
        this.itemCounterValue.setText(PersianUtil.getPersianNumbers(this.incDecViewStartValue));
      } else {
        this.itemCounterValue.setText(this.incDecViewStartValue);
      }
      this.value = Integer.valueOf(getString(startValue));
    }
    return this;
  }

  public IncDecView setCounterListener(CounterListener counterListener) {
    listener = counterListener;
    return this;
  }

  public String getCounterValue() {
    String text = "";
    if (this.itemCounterValue != null) {
      text = this.itemCounterValue.getText().toString();
    }
    return text;
  }

  public Integer getCounterValueInt() {
    return value;
  }

  public IncDecView setIncButtonIcon(@DrawableRes int incButtonIcon) {
    if (this.incButton != null) {
      this.incButton.setImageDrawable(getDrawable(incButtonIcon));
    }
    return this;
  }

  public IncDecView setDecButtonIcon(@DrawableRes int decButtonIcon) {
    if (this.decButton != null) {
      this.decButton.setImageDrawable(getDrawable(decButtonIcon));
    }
    return this;
  }

  public IncDecView setBorderWidth(@DimenRes int strokeWidth) {
    this.strokeWidthRef = strokeWidth;
    GradientDrawable drawable = new GradientDrawable();
    drawable.setCornerRadius(getDimension(R.dimen.inc_dec_counter_view_corner_radius));
    drawable.setStroke((int) getDimension(strokeWidth), getResources().getColor(android.R.color.darker_gray));
    if (rootView != null) {
      rootView.setBackgroundDrawable(drawable);
    }
    return this;
  }

  private IncDecView setBorderWidth(float value) {
    this.defWidthValue = value;
    GradientDrawable drawable = new GradientDrawable();
    drawable.setCornerRadius(getDimension(R.dimen.inc_dec_counter_view_corner_radius));
    drawable.setStroke((int) value, getResources().getColor(android.R.color.darker_gray));
    if (rootView != null) {
      rootView.setBackgroundDrawable(drawable);
    }
    return this;
  }

  public IncDecView setBorderColor(@ColorRes int strokeColor) {
    GradientDrawable drawable = new GradientDrawable();
    drawable.setCornerRadius(getDimension(R.dimen.inc_dec_counter_view_corner_radius));
    if (this.strokeWidthRef != 0) {
      drawable.setStroke((int) getDimension(this.strokeWidthRef), getColor(strokeColor));
    } else {
      drawable.setStroke((int) getDimension(R.dimen.inc_dec_counter_view_stroke_width), getColor(strokeColor));
    }
    if (rootView != null) {
      rootView.setBackgroundDrawable(drawable);
    }
    return this;
  }

  private IncDecView setBorderColor_(int color) {
    GradientDrawable drawable = new GradientDrawable();
    drawable.setCornerRadius(getDimension(R.dimen.inc_dec_counter_view_corner_radius));
    if (this.defWidthValue != 0) {
      drawable.setStroke((int) defWidthValue, color);
    } else {
      drawable.setStroke((int) getDimension(R.dimen.inc_dec_counter_view_stroke_width), color);
    }
    if (rootView != null) {
      rootView.setBackgroundDrawable(drawable);
    }
    return this;
  }

  public IncDecView setPersianNumber(boolean enable) {
    this.persianNumber = enable;
    return this;
  }

  private int getColor(@ColorRes int colorRes) {
    return getContext().getResources().getColor(colorRes);
  }

  private float getDimension(@DimenRes int dimenID) {
    return getContext().getResources().getDimension(dimenID);
  }

  private String getString(@StringRes int textResourceValue) {
    return getContext().getString(textResourceValue);
  }

  private Drawable getDrawable(@DrawableRes int drawableResource) {
    return getContext().getResources().getDrawable(drawableResource);
  }

  public void setMaxValue(Integer maxValue) {
    this.maxValue = maxValue;
  }

  public void setMinValue(Integer minValue) {
    this.maxValue = minValue;
  }

  @Override
  public void onClick(View view) {
    int i = view.getId();
    if (i == R.id.inc_button && !maxValue.equals(value)) {
      tryVibrate();
      value++;
      if (this.persianNumber) {
        this.itemCounterValue.setText(PersianUtil.getPersianNumbers(String.valueOf(value)));
      } else {
        this.itemCounterValue.setText(String.valueOf(value));
      }
      if (this.listener != null) {
        this.listener.onIncClick(this.itemCounterValue.getText().toString(), value);
      }
    } else if (i == R.id.dec_button && !minValue.equals(value)) {
      tryVibrate();
      value--;
      if (value <= 0) {
        value = 0;
      }
      if (this.persianNumber) {
        this.itemCounterValue.setText(PersianUtil.getPersianNumbers(String.valueOf(value)));
      } else {
        this.itemCounterValue.setText(String.valueOf(value));
      }
      if (this.listener != null) {
        this.listener.onDecClick(this.itemCounterValue.getText().toString(), value);
      }
    }
  }

  /**
   * Try to vibrate. To prevent this becoming a single continuous vibration, nothing will
   * happen if we have vibrated very recently.
   */
  public void tryVibrate() {
    if (mVibrator != null) {
      long now = SystemClock.uptimeMillis();
      if (now - mLastVibrate >= VIBRATE_DELAY_MS) {
        mVibrator.vibrate(VIBRATE_LENGTH_MS);
        mLastVibrate = now;
      }
    }
  }
}
