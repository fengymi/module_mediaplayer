package lib.kalu.mediaplayer.core.component;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;


import androidx.annotation.StringRes;

import java.io.File;

import lib.kalu.mediaplayer.args.StartArgs;
import lib.kalu.mediaplayer.core.player.video.VideoPlayerApi;
import lib.kalu.mediaplayer.type.PlayerType;
import lib.kalu.mediaplayer.util.LogUtil;
import lib.kalu.mediaplayer.widget.player.PlayerLayout;
import lib.kalu.mediaplayer.widget.player.PlayerView;

public interface ComponentApi {

    default boolean enableDispatchKeyEvent() {
        return false;
    }

    int initLayoutIdComponentRoot();

    default void setComponentVisibility(@IdRes int id, int visibility) {
        try {
            View viewById = ((View) this).findViewById(id);
            if (null == viewById)
                throw new Exception("error: viewById null");
            viewById.setVisibility(visibility);
        } catch (Exception e) {
            LogUtil.log("ComponentApi => setComponentVisibility => " + e.getMessage());
        }
    }

    default boolean isComponentShowing() {
        try {
            int rootId = initLayoutIdComponentRoot();
            View viewById = ((View) this).findViewById(rootId);
            if (null == viewById)
                throw new Exception("error: viewById null");
            return viewById.getVisibility() == View.VISIBLE;
        } catch (Exception e) {
            LogUtil.log("ComponentApi => isComponentShowing => " + e.getMessage());
            return false;
        }
    }

    /******************/

    default void callEventListener(int playState) {
    }

    default void callWindowEvent(int state) {
    }

    /******************/

    @IdRes
    default int initLayoutIdComponentBackground() {
        return -1;
    }

    default void setComponentBackgroundColorInt(@ColorInt int v) {
        try {
            int layoutId = initLayoutIdComponentBackground();
            if (layoutId == -1)
                throw new Exception("error: layoutId = -1");
            View view = ((View) this).findViewById(layoutId);
            view.setBackgroundColor(v);
        } catch (Exception e) {
            LogUtil.log("ComponentApi => setComponentBackgroundColorInt => " + e.getMessage());
        }
    }

    default void setComponentBackgroundColorRes(@ColorRes int v) {
        try {
            int layoutId = initLayoutIdComponentBackground();
            if (layoutId == -1)
                throw new Exception("error: layoutId = -1");
            View view = ((View) this).findViewById(layoutId);
            int color = ((View) this).getResources().getColor(v);
            view.setBackgroundColor(color);
        } catch (Exception e) {
            LogUtil.log("ComponentApi => setComponentBackgroundColorRes => " + e.getMessage());
        }
    }

    default void setComponentBackgroundDrawableRes(@DrawableRes int v) {
        try {
            int layoutId = initLayoutIdComponentBackground();
            if (layoutId == -1)
                throw new Exception("error: layoutId = -1");
            View view = ((View) this).findViewById(layoutId);
            view.setBackgroundResource(v);
        } catch (Exception e) {
            LogUtil.log("ComponentApi => setComponentBackgroundDrawableRes => " + e.getMessage());
        }
    }

    /******************/

    @IdRes
    default int initLayoutIdImage() {
        return -1;
    }

    default void setComponentImageDrawableRes(@DrawableRes int v) {
        try {
            int layoutId = initLayoutIdImage();
            if (layoutId == -1)
                throw new Exception("error: layoutId = -1");
            ImageView imageView = ((View) this).findViewById(layoutId);
            imageView.setImageResource(v);
        } catch (Exception e) {
            LogUtil.log("ComponentApi => setComponentImageDrawableRes => " + e.getMessage());
        }
    }

    default void setComponentImageDrawable(Drawable drawable) {
        try {
            int layoutId = initLayoutIdImage();
            if (layoutId == -1)
                throw new Exception("error: layoutId = -1");
            ImageView imageView = ((View) this).findViewById(layoutId);
            imageView.setImageDrawable(drawable);
        } catch (Exception e) {
            LogUtil.log("ComponentApi => setComponentImageDrawable => " + e.getMessage());
        }
    }

    default void setComponentImageBitmap(Bitmap bitmap) {
        try {
            int layoutId = initLayoutIdImage();
            if (layoutId == -1)
                throw new Exception("error: layoutId = -1");
            ImageView imageView = ((View) this).findViewById(layoutId);
            imageView.setImageBitmap(bitmap);
        } catch (Exception e) {
            LogUtil.log("ComponentApi => setComponentImageBitmap => " + e.getMessage());
        }
    }

    /******************/

    @IdRes
    default int initLayoutIdText() {
        return -1;
    }

    default void setComponentText(String v) {
        try {
            int layoutId = initLayoutIdText();
            if (layoutId == -1)
                throw new Exception("error: layoutId = -1");
            TextView textView = ((View) this).findViewById(layoutId);
            textView.setText(v);
        } catch (Exception e) {
            LogUtil.log("ComponentApi => setComponentText => " + e.getMessage());
        }
    }

    default void setComponentText(@StringRes int v) {
        try {
            int layoutId = initLayoutIdText();
            if (layoutId == -1)
                throw new Exception("error: layoutId = -1");
            TextView textView = ((View) this).findViewById(layoutId);
            textView.setText(v);
        } catch (Exception e) {
            LogUtil.log("ComponentApi => setComponentText => " + e.getMessage());
        }
    }

    default void setComponentTextSize(@DimenRes int v) {
        try {
            int layoutId = initLayoutIdText();
            if (layoutId == -1)
                throw new Exception("error: layoutId = -1");
            TextView textView = ((View) this).findViewById(layoutId);
            int offset = ((View) this).getResources().getDimensionPixelOffset(v);
            textView.setTextSize(offset);
        } catch (Exception e) {
            LogUtil.log("ComponentApi => setComponentTextSize => " + e.getMessage());
        }
    }

    default void setComponentTextSize(float v) {
        try {
            int layoutId = initLayoutIdText();
            if (layoutId == -1)
                throw new Exception("error: layoutId = -1");
            TextView textView = ((View) this).findViewById(layoutId);
            textView.setTextSize(v);
        } catch (Exception e) {
            LogUtil.log("ComponentApi => setComponentTextSize => " + e.getMessage());
        }
    }

    default void setComponentTextColorInt(@ColorInt int v) {
        try {
            int layoutId = initLayoutIdText();
            if (layoutId == -1)
                throw new Exception("error: layoutId = -1");
            TextView textView = ((View) this).findViewById(layoutId);
            textView.setTextColor(v);
        } catch (Exception e) {
            LogUtil.log("ComponentApi => setComponentTextColorInt => " + e.getMessage());
        }
    }

    default void setComponentTextColorRes(@ColorRes int v) {
        try {
            int layoutId = initLayoutIdText();
            if (layoutId == -1)
                throw new Exception("error: layoutId = -1");
            TextView textView = ((View) this).findViewById(layoutId);
            int color = ((View) this).getResources().getColor(v);
            textView.setTextColor(color);
        } catch (Exception e) {
            LogUtil.log("ComponentApi => setComponentTextColorRes => " + e.getMessage());
        }
    }


//    /******************/

    default void show() {
        try {
            boolean componentShowing = isComponentShowing();
            if (componentShowing)
                throw new Exception("warning: componentShowing true");
            int rootId = initLayoutIdComponentRoot();
            setComponentVisibility(rootId, View.VISIBLE);
        } catch (Exception e) {
            LogUtil.log("ComponentApi => show => Exception " + e.getMessage());
        }
    }

    default void hide() {
        try {
            boolean componentShowing = isComponentShowing();
            if (!componentShowing)
                throw new Exception("warning: componentShowing false");
            int rootId = initLayoutIdComponentRoot();
            setComponentVisibility(rootId, View.GONE);
        } catch (Exception e) {
            LogUtil.log("ComponentApi => hide => Exception " + e.getMessage());
        }
    }

    /******************/

    default void onUpdateProgress(boolean isFromUser, long max, long position, long duration) {
    }

    /*******************/

    default <T extends ComponentApi> T findComponent(java.lang.Class<?> cls) {
        try {
            PlayerLayout playerLayout = getPlayerLayout();
            if (null == playerLayout)
                throw new Exception("error: playerLayout null");
            ComponentApi component = playerLayout.findComponent(cls);
            if (null == component)
                throw new Exception("warning: component null");
            return (T) component;
        } catch (Exception e) {
            LogUtil.log("ComponentApi => findComponent => " + e.getMessage());
            return null;
        }
    }

    default boolean isComponentShowing(Class<?> cls) {
        try {
            ComponentApi component = findComponent(cls);
            if (null == component)
                throw new Exception("warning: component null");
            boolean componentShowing = component.isComponentShowing();
            if (!componentShowing)
                throw new Exception("warning: componentShowing false");
            return true;
        } catch (Exception e) {
            LogUtil.log("ComponentApi => isComponentApiShowing => " + e.getMessage());
            return false;
        }
    }

    default PlayerLayout getPlayerLayout() {
        try {
            PlayerView playerView = getPlayerView();
            if (null == playerView)
                new Exception("playerView error: null");
            ViewParent parent = playerView.getParent();
            if (null == parent)
                throw new Exception("parent error: null");
            if (!(parent instanceof PlayerLayout))
                throw new Exception("parent error: not instanceof PlayerLayout");
            return (PlayerLayout) parent;
        } catch (Exception e) {
            LogUtil.log("ComponentApi => getPlayerLayout => " + e.getMessage());
            return null;
        }
    }

    default PlayerView getPlayerView() {
        try {
            PlayerView playerView = null;
            View view = (View) this;
            while (true) {
                ViewParent parent = view.getParent();
                if (null == parent) {
                    break;
                } else if (parent instanceof PlayerView) {
                    playerView = (PlayerView) parent;
                    break;
                } else {
                    view = (View) parent;
                }
            }
            if (null == playerView)
                new Exception("not find");
            return playerView;
        } catch (Exception e) {
            LogUtil.log("ComponentApi => getPlayerView => " + e.getMessage());
            return null;
        }
    }

    default void superCallEventListener(boolean callPlayer, boolean callComponent, @PlayerType.StateType.Value int state) {
        try {
            PlayerView playerView = getPlayerView();
            if (null == playerView)
                throw new Exception("error: playerView null");
            playerView.callEventListener(callPlayer, callComponent, state);
        } catch (Exception e) {
            LogUtil.log("ComponentApi => superCallEventListener => " + e.getMessage());
        }
    }

    default boolean isFull() {
        try {
            PlayerView playerView = getPlayerView();
            if (null == playerView)
                throw new Exception("playerView error: null");
            return playerView.isFull();
        } catch (Exception e) {
            LogUtil.log("ComponentApi => isFull => " + e.getMessage());
            return false;
        }
    }

    default boolean isPlaying() {
        try {
            PlayerView playerView = getPlayerView();
            if (null == playerView)
                throw new Exception("playerView error: null");
            return playerView.isPlaying();
        } catch (Exception e) {
            LogUtil.log("ComponentApi => isPlaying => " + e.getMessage());
            return false;
        }
    }

    default String getNetSpeed() {
        try {
            PlayerView playerView = getPlayerView();
            if (null == playerView)
                throw new Exception("playerView error: null");
            return playerView.getNetSpeed();
        } catch (Exception e) {
            LogUtil.log("ComponentApi => isFull => " + e.getMessage());
            return "0kb/s";
        }
    }

    default void resume() {
        try {
            PlayerView playerView = getPlayerView();
            if (null == playerView)
                throw new Exception("playerView error: null");
            playerView.resume();
        } catch (Exception e) {
            LogUtil.log("ComponentApi => resume => " + e.getMessage());
        }
    }

    default void pause() {
        try {
            PlayerView playerView = getPlayerView();
            if (null == playerView)
                throw new Exception("playerView error: null");
            playerView.pause();
        } catch (Exception e) {
            LogUtil.log("ComponentApi => pause => " + e.getMessage());
        }
    }

    default void toggle() {
        try {
            PlayerView playerView = getPlayerView();
            if (null == playerView)
                throw new Exception("playerView error: null");
            playerView.toggle();
        } catch (Exception e) {
            LogUtil.log("ComponentApi => toggle => " + e.getMessage());
        }
    }

    default void stop() {
        try {
            PlayerView playerView = getPlayerView();
            if (null == playerView)
                throw new Exception("playerView error: null");
            playerView.stop(true, true);
        } catch (Exception e) {
            LogUtil.log("ComponentApi => toggle => " + e.getMessage());
        }
    }

    default long getDuration() {
        try {
            PlayerView playerView = getPlayerView();
            if (null == playerView)
                throw new Exception("playerView error: null");
            long duration = playerView.getDuration();
            if (duration < 0)
                throw new Exception("warning: duration<0");
            return duration;
        } catch (Exception e) {
            LogUtil.log("ComponentApi => getDuration => " + e.getMessage());
            return 0L;
        }
    }

    default long getPosition() {
        try {
            PlayerView playerView = getPlayerView();
            if (null == playerView)
                throw new Exception("playerView error: null");
            long position = playerView.getPosition();
            if (position < 0)
                throw new Exception("warning: position<0");
            return position;
        } catch (Exception e) {
            LogUtil.log("ComponentApi => getPosition => " + e.getMessage());
            return 0L;
        }
    }

    default long getMaxDuration() {
        try {
            PlayerView playerView = getPlayerView();
            if (null == playerView)
                throw new Exception("playerView error: null");
            StartArgs tags = playerView.getTags();
            long maxDuration = tags.getMaxDuration();
            if (maxDuration < 0)
                throw new Exception("warning: maxDuration<0");
            return maxDuration;
        } catch (Exception e) {
            LogUtil.log("ComponentApi => getMaxDuration => " + e.getMessage());
            return 0L;
        }
    }

    default boolean isShowNetSpeed() {
        try {
            PlayerView playerView = getPlayerView();
            if (null == playerView)
                throw new Exception("playerView error: null");
            StartArgs tags = playerView.getTags();
            if (null == tags)
                throw new Exception("warning: tags null");
            return tags.isShowNetSpeed();
        } catch (Exception e) {
            LogUtil.log("ComponentApi => isShowNetSpeed => " + e.getMessage());
            return false;
        }
    }

    default void setSpeed(float v) {
        try {
            PlayerView playerView = getPlayerView();
            if (null == playerView)
                throw new Exception("playerView error: null");
            playerView.setSpeed(v);
        } catch (Exception e) {
            LogUtil.log("ComponentApi => setSpeed => " + e.getMessage());
        }
    }

    default void setVideoScaleType(@PlayerType.ScaleType.Value int scaleType) {
        try {
            PlayerView playerView = getPlayerView();
            if (null == playerView)
                throw new Exception("playerView error: null");
            playerView.setVideoScaleType(scaleType);
        } catch (Exception e) {
            LogUtil.log("ComponentApi => setVideoScaleType => " + e.getMessage());
        }
    }
}