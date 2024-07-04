package lib.kalu.mediaplayer.core.component;

import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;


import lib.kalu.mediaplayer.R;
import lib.kalu.mediaplayer.args.StartArgs;
import lib.kalu.mediaplayer.type.PlayerType;
import lib.kalu.mediaplayer.util.LogUtil;
import lib.kalu.mediaplayer.util.TimeUtil;
import lib.kalu.mediaplayer.widget.player.PlayerView;
import lib.kalu.mediaplayer.widget.seek.SeekBar;
import lib.kalu.mediaplayer.widget.subtitle.model.Time;

public class ComponentPause extends RelativeLayout implements ComponentApiPause {

    public ComponentPause(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.module_mediaplayer_component_pause, this, true);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // toggle
        if (event.getAction() == KeyEvent.ACTION_UP && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            boolean isShowing = isComponentMenuShowing();
            if (!isShowing) {
                toggle();
                return true;
            }
        }
        // toggle
        else if (event.getAction() == KeyEvent.ACTION_UP && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER) {
            boolean isShowing = isComponentMenuShowing();
            if (!isShowing) {
                toggle();
                return true;
            }
        }
        return false;
    }

    @Override
    public void callEventListener(int playState) {
        switch (playState) {
            case PlayerType.StateType.STATE_PAUSE:
                LogUtil.log("ComponentPause[show] => playState = " + playState);
                show();
                break;
            case PlayerType.StateType.STATE_INIT:
            case PlayerType.StateType.STATE_RESUME:
            case PlayerType.StateType.STATE_FAST_FORWARD_START:
            case PlayerType.StateType.STATE_FAST_REWIND_START:
                LogUtil.log("ComponentPause[gone] => playState = " + playState);
                hide();
                break;
        }
    }

    @Override
    public boolean isComponentShowing() {
        return findViewById(R.id.module_mediaplayer_component_pause_root).getVisibility() == View.VISIBLE;
    }

    @Override
    public final void hide() {

        try {
            setComponentText("");
        } catch (Exception e) {
        }

        try {
            PlayerView playerView = getPlayerView();
            if (null == playerView)
                throw new Exception("error: playerView null");
            StartArgs tags = playerView.getTags();
            if (null == tags)
                throw new Exception("error: tags null");
            boolean trySee = tags.isTrySee();
            if (trySee)
                throw new Exception("warning: trySee true");
            findViewById(R.id.module_mediaplayer_component_pause_root).setVisibility(View.GONE);
        } catch (Exception e) {
            LogUtil.log("ComponentPause => hide => Exception " + e.getMessage());
        }
    }

    @Override
    public final void show() {

        try {
            for (int i = 0; i < 2; i++) {
                long duration = getDuration();
                if (duration <= 0)
                    throw new Exception("warning: duration<=0");
                long position = getPosition();
                long maxDuration = getMaxDuration();
                SeekBar seekBar = findViewById(R.id.module_mediaplayer_component_pause_sb);
                seekBar.setProgress((int) position);
                seekBar.setMax((int) (maxDuration > 0 ? maxDuration : duration));
            }
        } catch (Exception e) {
        }

        try {
            PlayerView playerView = getPlayerView();
            if (null == playerView)
                throw new Exception("error: playerView null");
            StartArgs tags = playerView.getTags();
            if (null == tags)
                throw new Exception("error: tags null");
            String mediaTitle = tags.getTitle();
            setComponentText(mediaTitle);
        } catch (Exception e) {
        }

        try {
            PlayerView playerView = getPlayerView();
            if (null == playerView)
                throw new Exception("error: playerView null");
            StartArgs tags = playerView.getTags();
            if (null == tags)
                throw new Exception("error: tags null");
            boolean trySee = tags.isTrySee();
            if (trySee)
                throw new Exception("warning: trySee true");
            findViewById(R.id.module_mediaplayer_component_pause_root).setVisibility(View.VISIBLE);
        } catch (Exception e) {
            LogUtil.log("ComponentPause => show => Exception " + e.getMessage());
        }
    }

    /*************/

    @Override
    public int initLayoutIdComponentBackground() {
        return R.id.module_mediaplayer_component_pause_bg;
    }

    @Override
    public int initLayoutIdText() {
        return R.id.module_mediaplayer_component_pause_title;
    }
}