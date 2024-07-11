package lib.kalu.mediaplayer.core.component;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;

import lib.kalu.mediaplayer.R;
import lib.kalu.mediaplayer.args.StartArgs;
import lib.kalu.mediaplayer.listener.OnPlayerEpisodeListener;
import lib.kalu.mediaplayer.util.LogUtil;
import lib.kalu.mediaplayer.widget.player.PlayerView;

public interface ComponentApiMenu extends ComponentApi {

    @Override
    default boolean enableDispatchKeyEvent() {
        return true;
    }

    default int getEpisodeItemCount() {
        try {
            PlayerView playerView = getPlayerView();
            if (null == playerView)
                throw new Exception("error: playerView null");
            StartArgs tags = playerView.getTags();
            if (null == tags)
                throw new Exception("error: tags null");
            int itemCount = tags.getEpisodeItemCount();
            if (itemCount <= 0)
                throw new Exception("warning: itemCount " + itemCount);
            return itemCount;
        } catch (Exception e) {
            LogUtil.log("ComponentApiMenu => getEpisodeItemCount => " + e.getMessage());
            return -1;
        }
    }

    default int getEpisodePlayingIndex() {
        try {
            PlayerView playerView = getPlayerView();
            if (null == playerView)
                throw new Exception("error: playerView null");
            StartArgs tags = playerView.getTags();
            if (null == tags)
                throw new Exception("error: tags null");
            int playingIndex = tags.getEpisodePlayingIndex();
            if (playingIndex < 0)
                throw new Exception("warning: playingIndex " + playingIndex);
            return playingIndex;
        } catch (Exception e) {
            LogUtil.log("ComponentApiMenu => getEpisodePlayingIndex => " + e.getMessage());
            return -1;
        }
    }

    default void scrollNextItem(int action) {
    }

    default void updateTabCheckedChange(boolean requestFocus) {
    }

    default void updateTabCheckedStyle(int id) {
    }

    default void toggleScale(int focusId) {
    }

    default void toggleSpeed(int focusId) {

    }

    default void toggleEpisode(int focusId) {

    }

    default void callEpisodeClickListener(int pos) {
        try {
            PlayerView playerView = getPlayerView();
            if (null == playerView)
                throw new Exception("error: null == playerView");
            OnPlayerEpisodeListener listener = playerView.getOnPlayerEpisodeListener();
            if (null == listener)
                throw new Exception("error: null == listener");
            listener.onEpisode(pos);
        } catch (Exception e) {
            LogUtil.log("ComponentApiMenu => callEpisodeClickListener => " + e.getMessage());
        }
    }

    Handler[] mHandlerDelayedMsg = new Handler[]{null};

    default void stopDelayedMsg() {
        if (null != mHandlerDelayedMsg[0]) {
            mHandlerDelayedMsg[0].removeMessages(1000);
            mHandlerDelayedMsg[0] = null;
        }
    }

    default void startDelayedMsg() {
        if (null == mHandlerDelayedMsg[0]) {
            mHandlerDelayedMsg[0] = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(@NonNull Message msg) {
                    if (msg.what == 1000) {
                        hide();
                    }
                }
            };
        }
        mHandlerDelayedMsg[0].removeMessages(1000);
        mHandlerDelayedMsg[0].sendEmptyMessageDelayed(1000, 4000);
    }
}