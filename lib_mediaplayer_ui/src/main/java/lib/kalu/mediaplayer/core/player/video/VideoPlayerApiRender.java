package lib.kalu.mediaplayer.core.player.video;

import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import lib.kalu.mediaplayer.R;
import lib.kalu.mediaplayer.config.player.PlayerBuilder;
import lib.kalu.mediaplayer.config.player.PlayerManager;
import lib.kalu.mediaplayer.config.player.PlayerType;
import lib.kalu.mediaplayer.core.render.VideoRenderApi;
import lib.kalu.mediaplayer.core.render.VideoRenderFactoryManager;
import lib.kalu.mediaplayer.util.MPLogUtil;

interface VideoPlayerApiRender extends VideoPlayerApiBase {

    default String screenshot() {
        try {
            VideoRenderApi render = getVideoRender();
            return render.screenshot();
        } catch (Exception e) {
            return null;
        }
    }

    default void requestFocusFull() {
        try {
            ViewGroup decorView = findDecorView((View) this);
            if (null == decorView)
                throw new Exception("decorView error: null");
            View focus = decorView.findFocus();
            if (null == focus)
                throw new Exception("focus error: null");
            ((View) this).setTag(R.id.module_mediaplayer_window_id, focus);
            ((View) ((View) this).getParent()).setFocusable(true);
            ((View) this).setFocusable(true);
            ((View) this).requestFocus();
        } catch (Exception e) {
            MPLogUtil.log("VideoPlayerApiRender => requestFocusFull => " + e.getMessage());
        }
    }

    default void cleanFocusFull() {
        try {
            Object tag = ((View) this).getTag(R.id.module_mediaplayer_window_id);
            if (null == tag)
                throw new Exception("tag error: null");
            ((View) this).setTag(R.id.module_mediaplayer_window_id, null);
            ((View) ((View) this).getParent()).setFocusable(false);
            ((View) this).setFocusable(false);
            ((View) tag).requestFocus();
        } catch (Exception e) {
            MPLogUtil.log("VideoPlayerApiRender => cleanFocusFull => " + e.getMessage());
        }
    }

    default boolean resetVideoRender() {
        try {
            PlayerBuilder config = PlayerManager.getInstance().getConfig();
            if (null == config)
                throw new Exception("config warning: null");
            int videoRender = config.getRender();
            if (videoRender != PlayerType.RenderType.SURFACE_VIEW)
                throw new Exception("videoRender warning: not SURFACE_VIEW");
            int videoKernel = config.getKernel();
            if (videoKernel != PlayerType.KernelType.IJK_MEDIACODEC && videoKernel != PlayerType.KernelType.EXO_V1 && videoKernel != PlayerType.KernelType.EXO_V2)
                throw new Exception("videoKernel waring: not ijk_mediacodec or exo_v1 or exo_v2");
            if (!(this instanceof VideoPlayerApiKernel))
                throw new Exception("videoRender warning: not instanceof PlayerApiKernel");
            createVideoRender();
            ((VideoPlayerApiKernel) this).attachVideoRender();
            updateVideoRenderBuffer(videoKernel == PlayerType.KernelType.IJK_MEDIACODEC ? 4000 : 400);
            return true;
        } catch (Exception e) {
            MPLogUtil.log("VideoPlayerApiRender => resetVideoRender => " + e.getMessage());
            return false;
        }
    }

    default void checkPlaying() {
        try {
            if (!(this instanceof VideoPlayerApiKernel))
                throw new Exception("this error: not instanceof PlayerApiKernel");
            boolean playing = ((VideoPlayerApiKernel) this).isPlaying();
            ((View) this).setTag(R.id.module_mediaplayer_id_player_switch_window_check_playing, playing);
        } catch (Exception e) {
            MPLogUtil.log("VideoPlayerApiRender => checkPlaying => " + e.getMessage());
        }
    }

    default void switchPlaying() {
        try {
            if (!(this instanceof VideoPlayerApiKernel))
                throw new Exception("this error: not instanceof PlayerApiKernel");
            Object tag = ((View) this).getTag(R.id.module_mediaplayer_id_player_switch_window_check_playing);
            ((View) this).setTag(R.id.module_mediaplayer_id_player_switch_window_check_playing, null);
            if (null == tag || ((boolean) tag))
                throw new Exception("tag warning: null");
            ((VideoPlayerApiKernel) this).pause(true);
        } catch (Exception e) {
            MPLogUtil.log("VideoPlayerApiRender => switchPlaying => " + e.getMessage());
        }
    }

    default boolean startFull(boolean rememberPlaying) {
        return startFull(rememberPlaying, true);
    }

    default boolean startFull(boolean rememberPlaying, boolean resetSurface) {
        try {
            boolean isPhoneWindow = isParentEqualsPhoneWindow();
            if (isPhoneWindow)
                throw new Exception("always full");
            requestFocusFull();
            boolean b = switchToDecorView(true);
            if (b) {
                if (resetSurface) {
                    resetVideoRender();
                }
                callPlayerEvent(PlayerType.StateType.STATE_FULL_START);
                callWindowEvent(PlayerType.WindowType.FULL);
            }
            if (rememberPlaying) {
                checkPlaying();
            }
            return b;
        } catch (Exception e) {
            MPLogUtil.log("VideoPlayerApiRender => startFull => " + e.getMessage());
            return false;
        }
    }

    default boolean stopFull() {
        return stopFull(true);
    }

    default boolean stopFull(boolean resetSurface) {
        try {
            boolean isFull = isFull();
            if (!isFull)
                throw new Exception("not full");
            boolean b = switchToPlayerLayout();
            switchPlaying();
            if (b) {
                if (resetSurface) {
                    resetVideoRender();
                }
                cleanFocusFull();
                callPlayerEvent(PlayerType.StateType.STATE_FULL_STOP);
                callWindowEvent(PlayerType.WindowType.NORMAL);
            }
            return b;
        } catch (Exception e) {
            MPLogUtil.log("VideoPlayerApiRender => stopFull => " + e.getMessage());
            return false;
        }
    }

    default boolean startFloat(boolean rememberPlaying) {
        try {
            boolean isPhoneWindow = isParentEqualsPhoneWindow();
            if (isPhoneWindow)
                throw new Exception("always Float");
            boolean switchToDecorView = switchToDecorView(false);
            if (switchToDecorView) {
                resetVideoRender();
                callPlayerEvent(PlayerType.StateType.STATE_FLOAT_START);
                callWindowEvent(PlayerType.WindowType.FLOAT);
            }
            if (rememberPlaying) {
                checkPlaying();
            }
            return switchToDecorView;
        } catch (Exception e) {
            MPLogUtil.log("VideoPlayerApiRender => startFloat => " + e.getMessage());
            return false;
        }
    }

    default boolean stopFloat() {
        try {
            boolean isFloat = isFloat();
            if (!isFloat)
                throw new Exception("not Float");
            boolean switchToPlayerLayout = switchToPlayerLayout();
            switchPlaying();
            if (switchToPlayerLayout) {
                resetVideoRender();
                callPlayerEvent(PlayerType.StateType.STATE_FLOAT_STOP);
                callWindowEvent(PlayerType.WindowType.NORMAL);
            }
            return switchToPlayerLayout;
        } catch (Exception e) {
            MPLogUtil.log("VideoPlayerApiRender => stopFloat => " + e.getMessage());
            return false;
        }
    }

    default void setVideoScaleType(@PlayerType.ScaleType.Value int scaleType) {
        try {
            VideoRenderApi render = getVideoRender();
            if (null == render)
                throw new Exception("render error: null");
            render.setVideoScaleType(scaleType);
            PlayerManager.getInstance().setScaleType(scaleType);
        } catch (Exception e) {
            MPLogUtil.log("VideoPlayerApiRender => setVideoScaleType => " + e.getMessage());
        }
    }

    default void setVideoSize(int width, int height) {
        try {
            VideoRenderApi render = getVideoRender();
            render.setVideoSize(width, height);
        } catch (Exception e) {
            MPLogUtil.log("VideoPlayerApiRender => setVideoSize => " + e.getMessage());
        }
    }

    default void setVideoRotation(@PlayerType.RotationType.Value int rotation) {
        try {
            if (rotation == -1)
                return;
            VideoRenderApi render = getVideoRender();
            render.setVideoRotation(rotation);
        } catch (Exception e) {
            MPLogUtil.log("VideoPlayerApiRender => setVideoRotation => " + e.getMessage());
        }
    }

    /**
     * 设置镜像旋转，暂不支持SurfaceView
     *
     * @param enable
     */
    default void setMirrorRotation(boolean enable) {
        try {
            VideoRenderApi render = getVideoRender();
            render.setScaleX(enable ? -1 : 1);
        } catch (Exception e) {
            MPLogUtil.log("VideoPlayerApiRender => setMirrorRotation => " + e.getMessage());
        }
    }

    default void showVideoReal() {
        try {
            ViewGroup viewGroup = getBaseVideoViewGroup();
            viewGroup.setVisibility(View.VISIBLE);
            int count = viewGroup.getChildCount();
            for (int i = 0; i < count; i++) {
                View child = viewGroup.getChildAt(i);
                child.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            MPLogUtil.log("VideoPlayerApiRender => showVideoReal => " + e.getMessage());
        }
    }

    default void hideReal() {
        try {
            ViewGroup viewGroup = getBaseVideoViewGroup();
            int count = viewGroup.getChildCount();
            for (int i = 0; i < count; i++) {
                View child = viewGroup.getChildAt(i);
                child.setVisibility(View.INVISIBLE);
            }
            viewGroup.setVisibility(View.INVISIBLE);
        } catch (Exception e) {
            MPLogUtil.log("VideoPlayerApiRender => hideReal => " + e.getMessage());
        }
    }

    default VideoRenderApi searchVideoRender() {
        try {
            ViewGroup group = getBaseVideoViewGroup();
            int count = group.getChildCount();
            for (int i = 0; i < count; i++) {
                View view = group.getChildAt(i);
                if (null == view)
                    continue;
                if (!(view instanceof VideoRenderApi))
                    continue;
                return (VideoRenderApi) view;
            }
            throw new Exception("not find");
        } catch (Exception e) {
            MPLogUtil.log("VideoPlayerApiRender => searchVideoRender => " + e.getMessage());
            return null;
        }
    }

    default void setVideoRender(@PlayerType.RenderType int v) {
        try {
            PlayerManager.getInstance().setRender(v);
        } catch (Exception e) {
        }
    }

    default void createVideoRender() {
        try {
            releaseVideoRender();
            int videoRender = PlayerManager.getInstance().getConfig().getRender();
            setVideoRender(VideoRenderFactoryManager.createRender(getBaseContext(), videoRender));
            addVideoRender();
        } catch (Exception e) {
            MPLogUtil.log("VideoPlayerApiRender => createRender => " + e.getMessage());
        }
    }

    default void updateVideoRenderBuffer(int delayMillis) {
        try {
            VideoRenderApi render = searchVideoRender();
            if (null == render)
                throw new Exception("render warning: null");
            render.updateBuffer(delayMillis);
        } catch (Exception e) {
            MPLogUtil.log("VideoPlayerApiRender => updateVideoRenderBuffer => " + e.getMessage());
        }
    }

    default void addVideoRenderListener() {
        try {
            VideoRenderApi render = searchVideoRender();
            if (null == render)
                throw new Exception("render warning: null");
            render.addListener();
        } catch (Exception e) {
            MPLogUtil.log("VideoPlayerApiRender => addVideoRenderListener => " + e.getMessage());
        }
    }

    default void addVideoRender() {
        try {
            VideoRenderApi render = getVideoRender();
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            render.setLayoutParams(params);
            ViewGroup viewGroup = getBaseVideoViewGroup();
            viewGroup.addView((View) render, 0);
            MPLogUtil.log("VideoPlayerApiRender => addRender => succ");
        } catch (Exception e) {
            MPLogUtil.log("VideoPlayerApiRender => addVideoRender => " + e.getMessage());
        }
    }

    default void releaseVideoRender() {
        try {
            ViewGroup group = getBaseVideoViewGroup();
            if (null == group)
                throw new Exception("group error: null");
            int childCount = group.getChildCount();
            if (childCount <= 0)
                throw new Exception("childCount warning: " + childCount);
            for (int i = 0; i < childCount; i++) {
                View childAt = group.getChildAt(i);
                if (null == childAt)
                    continue;
                if (!(childAt instanceof VideoRenderApi))
                    continue;
                ((VideoRenderApi) childAt).release();
            }
            group.removeAllViews();
            MPLogUtil.log("VideoPlayerApiRender => releaseVideoRender => succ");
        } catch (Exception e) {
            MPLogUtil.log("VideoPlayerApiRender => releaseVideoRender => " + e.getMessage());
        }
    }

    VideoRenderApi getVideoRender();

    void setVideoRender(@NonNull VideoRenderApi render);

    void checkVideoReal();
}
