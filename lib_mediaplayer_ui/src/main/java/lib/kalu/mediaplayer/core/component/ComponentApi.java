package lib.kalu.mediaplayer.core.component;

import android.net.Uri;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;


import androidx.annotation.StringRes;

import lib.kalu.mediaplayer.util.MPLogUtil;


public interface ComponentApi extends ComponentApiLinkerPlayer {

    default void callPlayerEvent(int playState) {
    }

    default void callWindowEvent(int state) {
    }

    /*************/

    default void setImageResource(View layout, @IdRes int id, @DrawableRes int value) {
        try {
            ImageView imageView = layout.findViewById(id);
            imageView.setImageResource(value);
        } catch (Exception e) {
            MPLogUtil.log("ComponentApi => setImageResource => " + e.getMessage());
        }
    }

    default void setImageUrl(View layout, @IdRes int id, String url) {
        try {
            ImageView imageView = layout.findViewById(id);
            imageView.setImageURI(Uri.parse(url));
        } catch (Exception e) {
            MPLogUtil.log("ComponentApi => setImageUrl => " + e.getMessage());
        }
    }

    default void setTextColor(View layout, @IdRes int id, @ColorInt int value) {
        try {
            TextView view = layout.findViewById(id);
            view.setTextColor(value);
        } catch (Exception e) {
            MPLogUtil.log("ComponentApi => setTextColor => " + e.getMessage());
        }
    }

    default void setTextSize(View layout, @IdRes int id, @DimenRes int value) {
        try {
            TextView view = layout.findViewById(id);
            int offset = layout.getResources().getDimensionPixelOffset(value);
            view.setTextSize(TypedValue.COMPLEX_UNIT_PX, offset);
        } catch (Exception e) {
            MPLogUtil.log("ComponentApi => setTextSize => " + e.getMessage());
        }
    }

    default void setText(View layout, @IdRes int id, @StringRes int value) {
        try {
            TextView view = layout.findViewById(id);
            view.setText(value);
        } catch (Exception e) {
            MPLogUtil.log("ComponentApi => setText => " + e.getMessage());
        }
    }

    default void setText(View layout, @IdRes int id, String value) {
        try {
            TextView view = layout.findViewById(id);
            view.setText(value);
        } catch (Exception e) {
            MPLogUtil.log("ComponentApi => setText => " + e.getMessage());
        }
    }

    default void setCompoundDrawablesWithIntrinsicBounds(View layout, @IdRes int id, @DrawableRes int left, @DrawableRes int top, @DrawableRes int right, @DrawableRes int bottom) {
        try {
            TextView view = layout.findViewById(id);
            view.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
        } catch (Exception e) {
            MPLogUtil.log("ComponentApi => setCompoundDrawablesWithIntrinsicBounds => " + e.getMessage());
        }
    }

    default void setDimens(View layout, @IdRes int id, @DimenRes int value) {
        try {
            View view = layout.findViewById(id);
            ViewGroup.LayoutParams layoutParams = layout.getLayoutParams();
            int offset = layout.getResources().getDimensionPixelOffset(value);
            layoutParams.width = offset;
            layoutParams.height = offset;
            view.setLayoutParams(layoutParams);
        } catch (Exception e) {
            MPLogUtil.log("ComponentApi => setDimens => " + e.getMessage());
        }
    }

    default void setBackgroundColorInt(View layout, @IdRes int id, @ColorInt int value) {
        try {
            View view = layout.findViewById(id);
            view.setBackgroundColor(value);
        } catch (Exception e) {
            MPLogUtil.log("ComponentApi => setBackgroundColorInt => " + e.getMessage());
        }
    }

    default void setBackgroundColorRes(View layout, @IdRes int id, @ColorRes int resId) {
        try {
            View view = layout.findViewById(id);
            int color = view.getResources().getColor(resId);
            view.setBackgroundColor(color);
        } catch (Exception e) {
            MPLogUtil.log("ComponentApi => setBackgroundColorRes => " + e.getMessage());
        }
    }

    default void setBackgroundDrawableRes(View layout, @IdRes int id, @DrawableRes int resId) {
        try {
            View view = layout.findViewById(id);
            view.setBackgroundResource(resId);
        } catch (Exception e) {
            MPLogUtil.log("ComponentApi => setBackgroundDrawableRes => " + e.getMessage());
        }
    }

    /******************/

    default boolean isComponentShowing() {
        return false;
    }

    /******************/

    default void setComponentBackgroundColorInt(@ColorInt int value) {
    }

    default void setComponentBackgroundResource(@DrawableRes int resid) {
    }

    default void setComponentImageResource(@DrawableRes int resid) {
    }

    default void setComponentImageUrl(String url) {
    }

    /******************/

    default void setComponentText(String value) {
    }

    default void setComponentText(@StringRes int value) {
    }

    default void setComponentTextSize(@DimenRes int value) {
    }

    default void setComponentTextColor(@ColorInt int color) {
    }

    /******************/

    default void show() {
    }

    default void gone() {
    }

    /******************/

    default void onUpdateSeekProgress(boolean updateTime, long position, long duration, long max) {
    }

    default void onUpdateTimeMillis(long seek, long position, long duration, long max) {
    }

    default boolean dispatchKeyEventComponent(KeyEvent event) {
        return false;
    }
}