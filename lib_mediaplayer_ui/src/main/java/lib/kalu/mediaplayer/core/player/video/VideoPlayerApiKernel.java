package lib.kalu.mediaplayer.core.player.video;

import android.annotation.SuppressLint;
import android.view.View;

import androidx.annotation.FloatRange;

import org.json.JSONArray;

import lib.kalu.mediaplayer.R;
import lib.kalu.mediaplayer.args.PlayerArgs;
import lib.kalu.mediaplayer.PlayerSDK;
import lib.kalu.mediaplayer.type.PlayerType;
import lib.kalu.mediaplayer.args.StartArgs;
import lib.kalu.mediaplayer.core.kernel.video.VideoKernelApi;
import lib.kalu.mediaplayer.core.kernel.video.VideoKernelApiEvent;
import lib.kalu.mediaplayer.core.kernel.video.VideoKernelFactoryManager;
import lib.kalu.mediaplayer.util.LogUtil;

interface VideoPlayerApiKernel extends VideoPlayerApiListener,
        VideoPlayerApiBuriedEvent,
        VideoPlayerApiComponent,
        VideoPlayerApiRender,
        VideoPlayerApiDevice {

    default boolean isDoWindowing() {
        try {
            VideoKernelApi videoKernel = getVideoKernel();
            if (null == videoKernel)
                throw new Exception("error: videoKernel null");
            return videoKernel.isDoWindowing();
        } catch (Exception e) {
            return false;
        }
    }

    default void setDoWindowing(boolean v) {
        try {
            VideoKernelApi videoKernel = getVideoKernel();
            if (null == videoKernel)
                throw new Exception("error: videoKernel null");
            videoKernel.setDoWindowing(v);
        } catch (Exception e) {
        }
    }

    default void setTags(StartArgs args) {
        try {
            ((View) this).setTag(R.id.module_mediaplayer_id_startargs, args);
        } catch (Exception e) {
            LogUtil.log("VideoPlayerApiKernel => setTags => " + e.getMessage());
        }
    }

    default StartArgs getTags() {
        try {
            Object tag = ((View) this).getTag(R.id.module_mediaplayer_id_startargs);
            if (null == tag)
                throw new Exception("warning: tag null");
            return (StartArgs) tag;
        } catch (Exception e) {
            LogUtil.log("VideoPlayerApiKernel => getTags => " + e.getMessage());
            return null;
        }
    }

    default void start(String url) {
        StartArgs build = new StartArgs.Builder().setMediaUrl(url).build();
        start(build);
    }

    @Override
    default void start(StartArgs args) {
        try {
            if (null == args)
                throw new Exception("error: args null");
            String url = args.getMediaUrl();
            if (null == url)
                throw new Exception("error: url null");
            // 1
            boolean log = args.isLog();
            LogUtil.setLogger(log);
            // 2
            boolean initRelease = args.isInitRelease();
            if (initRelease) {
                release(true, false, false);
            } else {
                stop(true, false);
            }
            // 3
            setTags(args);
            setScreenKeep(true);
            callEventListener(PlayerType.StateType.STATE_INIT);
            // 4
            int kernelType = args.getKernelType();
            checkKernelNull(kernelType, false);
            // 5
            int renderType = args.getRenderType();
            checkRenderNull(renderType, false);
            // 6
            attachRenderKernel();
            // 7
            setKernelEvent(args);
            // 8
            startDecoder(args);
        } catch (Exception e) {
            LogUtil.log("VideoPlayerApiKernel => start => " + e.getMessage());
        }
    }

    default long getDuration() {
        try {
            VideoKernelApi kernel = getVideoKernel();
            long duration = kernel.getDuration();
            if (duration < 0L) {
                duration = 0L;
            }
            return duration;
        } catch (Exception e) {
            LogUtil.log("VideoPlayerApiKernel => getDuration => " + e.getMessage());
            return 0L;
        }
    }

    default long getPosition() {
        try {
            VideoKernelApi kernel = getVideoKernel();
            long position = kernel.getPosition();
            if (position < 0L) {
                position = 0L;
            }
            return position;
        } catch (Exception e) {
            return 0L;
        }
    }

    default void setVolume(@FloatRange(from = 0f, to = 1f) float left, @FloatRange(from = 0f, to = 1f) float right) {
        try {
            LogUtil.log("VideoPlayerApiKernel => setVolume => left = " + left + ", right = " + right);
            VideoKernelApi kernel = getVideoKernel();
            kernel.setVolume(left, right);
        } catch (Exception e) {
        }
    }

    default void setMute(boolean enable) {
        try {
            LogUtil.log("VideoPlayerApiKernel => setMute => enable = " + enable);
            VideoKernelApi kernel = getVideoKernel();
            kernel.setMute(enable);
        } catch (Exception e) {
        }
    }

    default void setLooping(boolean looping) {
        try {
            LogUtil.log("VideoPlayerApiKernel => setLooping => looping = " + looping);
            VideoKernelApi kernel = getVideoKernel();
            kernel.setLooping(looping);
        } catch (Exception e) {
        }
    }


    default void toggle() {
        toggle(true);
    }

    default void toggle(boolean callEvent) {
        try {
            boolean playing = isPlaying();
            if (playing) {
                // 埋点
                onBuriedEventPause();
                pause(callEvent);
            } else {
                // 埋点
                onBuriedEventResume();
                resume();
            }
        } catch (Exception e) {
            LogUtil.log("VideoPlayerApiKernel => toggle => " + e.getMessage());
        }
    }

    default void pause() {
        pause(true);
    }

    default void pause(boolean callEvent) {
        setScreenKeep(false);
        pauseKernel(callEvent);
    }

    default void stop() {
        stop(true, true);
    }

    default void stop(boolean clearTag, boolean callEvent) {
        if (clearTag) {
            setTags(null);
        }
        setScreenKeep(false);
        stopKernel(callEvent);
    }

    default void release() {
        release(true, true, true);
    }

    default void release(boolean clearTag, boolean callEvent, boolean isFromUser) {
        try {
            if (clearTag) {
                setTags(null);
            }
            releaseRender();
            releaseKernel(isFromUser);
            if (!callEvent)
                throw new Exception("warning: callEvent false");
            callEventListener(PlayerType.StateType.STATE_RELEASE);
        } catch (Exception e) {
            callEventListener(PlayerType.StateType.STATE_RELEASE_EXCEPTION);
            LogUtil.log("VideoPlayerApiKernel => release => " + e.getMessage());
        }
    }

    default void restart() {
        restart(0L, true);
    }

    default void restart(long position, boolean callEvent) {
        try {
            StartArgs args = getTags();
            if (null == args)
                throw new Exception("error: args null");
            String url = args.getMediaUrl();
            if (null == url)
                throw new Exception("error: url null");
            if (callEvent) {
                callEventListener(PlayerType.StateType.STATE_RESTAER);
            }
            StartArgs build = args.newBuilder().setSeek(position).build();
            start(build);
        } catch (Exception e) {
            LogUtil.log("VideoPlayerApiKernel => restart => " + e.getMessage());
        }
    }

    default void resume() {
        resume(true);
    }

    default void resume(boolean callEvent) {
        try {
            VideoKernelApi kernel = getVideoKernel();
            if (null == kernel)
                throw new Exception("warning: kernel null");
            resumeVideoKernel(callEvent);
        } catch (Exception e) {
            LogUtil.log("VideoPlayerApiKernel => resume => " + e.getMessage());
        }
    }

    default long getMaxDuration() {
        try {
            VideoKernelApi kernel = getVideoKernel();
            return kernel.getMaxDuration();
        } catch (Exception e) {
            return 0L;
        }
    }

    default void seekTo(long position) {
        try {
            if (position < 0) {
                position = 0;
            }
            VideoKernelApi kernel = getVideoKernel();
            if (null == kernel)
                throw new Exception("warning: kernel null");
            kernel.seekTo(position);
            setScreenKeep(true);
        } catch (Exception e) {
            LogUtil.log("VideoPlayerApiKernel => seekToVideoKernel => " + e.getMessage());
        }
    }

    default boolean isLive() {
        try {
            VideoKernelApi kernel = getVideoKernel();
            return kernel.isLive();
        } catch (Exception e) {
            return false;
        }
    }

    default boolean isPlaying() {
        try {
            VideoKernelApi kernel = getVideoKernel();
            return kernel.isPlaying();
        } catch (Exception e) {
            return false;
        }
    }

    default boolean isPrepared() {
        try {
            VideoKernelApi kernel = getVideoKernel();
            return kernel.isPrepared();
        } catch (Exception e) {
            return false;
        }
    }

    default boolean isMute() {
        try {
            VideoKernelApi kernel = getVideoKernel();
            return kernel.isMute();
        } catch (Exception e) {
            return false;
        }
    }

    default void setSpeed(float speed) {
        try {
            VideoKernelApi kernel = getVideoKernel();
            kernel.setSpeed(speed);
        } catch (Exception e) {
        }
    }

    default float getSpeed() {
        try {
            VideoKernelApi kernel = getVideoKernel();
            return kernel.getSpeed();
        } catch (Exception e) {
            return 1F;
        }
    }

    /*********************/

    default void resumeVideoKernel(boolean callEvent) {
        try {
            VideoKernelApi kernel = getVideoKernel();
            if (null == kernel)
                throw new Exception("warning: kernel null");
            kernel.start();
            setScreenKeep(true);
            if (callEvent) {
                callEventListener(PlayerType.StateType.STATE_RESUME);
                callEventListener(PlayerType.StateType.STATE_KERNEL_RESUME);
            }
        } catch (Exception e) {
            LogUtil.log("VideoPlayerApiKernel => resumeVideoKernel => " + e.getMessage());
        }
    }

    default void stopKernel(boolean callEvent) {
        try {
            VideoKernelApi kernel = getVideoKernel();
            if (null == kernel)
                throw new Exception("warning: kernel null");
            kernel.stop();
            if (!callEvent)
                throw new Exception("warning: callEvent false");
            callEventListener(PlayerType.StateType.STATE_KERNEL_STOP);
            callEventListener(PlayerType.StateType.STATE_CLOSE);
        } catch (Exception e) {
            LogUtil.log("VideoPlayerApiKernel => stopKernel => " + e.getMessage());
        }
    }

    default void pauseKernel(boolean callEvent) {
        try {
            VideoKernelApi kernel = getVideoKernel();
            if (null == kernel)
                throw new Exception("warning: kernel null");
            kernel.pause();
            if (!callEvent)
                throw new Exception("warning: callEvent false");
            callEventListener(PlayerType.StateType.STATE_PAUSE);
        } catch (Exception e) {
            LogUtil.log("VideoPlayerApiKernel => pauseKernel => " + e.getMessage());
        }
    }

//    default void updateVideoKernel(StartArgs builder) {
//        try {
//            VideoKernelApi kernel = getVideoKernel();
//            if (null == kernel)
//                throw new Exception("warning: kernel null");
//            long seek = builder.getSeek();
//            long max = builder.getMax();
//            boolean loop = builder.isLooping();
//            kernel.update(seek, max, loop);
//        } catch (Exception e) {
//            LogUtil.log("VideoPlayerApiKernel => updateVideoKernel => " + e.getMessage());
//        }
//    }

    default void startDecoder(StartArgs args) {
        try {
            VideoKernelApi kernel = getVideoKernel();
            if (null == kernel)
                throw new Exception("warning: kernel null");
            kernel.initDecoder(getBaseContext(), args);
        } catch (Exception e) {
            LogUtil.log("VideoPlayerApiKernel => startDecoder => " + e.getMessage());
        }
    }

    /***************************/

    default void releaseKernel(boolean isFromUser) {
        try {
            VideoKernelApi kernel = getVideoKernel();
            if (null == kernel)
                throw new Exception("warning: kernel null");
            kernel.releaseDecoder(isFromUser);
            setVideoKernel(null);
            setScreenKeep(false);
        } catch (Exception e) {
            LogUtil.log("VideoPlayerApiKernel => releaseKernel => " + e.getMessage());
        }
    }

    default void checkKernelNull(int kernelType, boolean release) {
        try {
            if (release) {
                releaseKernel(false);
            }
            VideoKernelApi videoKernel = getVideoKernel();
            if (null != videoKernel)
                throw new Exception("warning: null != videoKernel");
            VideoKernelApi kernelApi = VideoKernelFactoryManager.getKernel(kernelType);
            kernelApi.createDecoder(getBaseContext());
            setVideoKernel(kernelApi);
        } catch (Exception e) {
            LogUtil.log("VideoPlayerApiKernel => checkKernelNull => " + e.getMessage());
        }
    }

    default void setKernelEvent(StartArgs args) {

        try {
            VideoKernelApi videoKernel = getVideoKernel();
            if (null == videoKernel)
                throw new Exception("");
            videoKernel.setPlayerApi((VideoPlayerApi) this);
            videoKernel.setKernelApi(new VideoKernelApiEvent() {

                @Override
                public void onUpdateProgress(long position, long duration) {

                    try {
                        long maxDuration = args.getMaxDuration();
                        callProgressListener(maxDuration, position, duration);
                    } catch (Exception e) {
                    }

                    try {
                        boolean trySee = args.isTrySee();
                        LogUtil.log("VideoPlayerApiKernel => setKernelEvent => onUpdateProgress => trySee = " + trySee + ", maxDuration = " + args.getMaxDuration() + ", position = " + position);
                        // 试看
                        if (trySee) {
                            long maxDuration = args.getMaxDuration();
                            if (maxDuration <= 0)
                                throw new Exception("warning: maxDuration <= 0");
                            if (position <= maxDuration)
                                throw new Exception("warning: position <= maxDuration");
                            pause(false);
                            callEventListener(PlayerType.StateType.STATE_TRY_TO_SEE_FINISH);
                        }
                        // 默认播放结束
                        else {
                            boolean looping = args.isLooping();
                            if (looping) {
                                restart();
                            }
                        }
                    } catch (Exception e) {
                    }
                }

                @Override
                public void onEvent(int kernel, int event) {
                    LogUtil.log("VideoPlayerApiKernel => setKernelEvent => onEvent = " + kernel + ", event = " + event);
                    switch (event) {
                        // 网络拉流开始
//                        case PlayerType.EventType.EVENT_OPEN_INPUT:
//                            hideVideoView();
//                            break;
                        // 初始化开始 => loading start
                        case PlayerType.EventType.EVENT_LOADING_START:
                            callEventListener(PlayerType.StateType.STATE_LOADING_START);
                            break;
                        // 初始化完成 => loading stop
                        case PlayerType.EventType.EVENT_LOADING_STOP:
                            callEventListener(PlayerType.StateType.STATE_LOADING_STOP);
                            break;
                        // 缓冲开始
                        case PlayerType.EventType.EVENT_BUFFERING_START:
                            callEventListener(PlayerType.StateType.STATE_BUFFERING_START);
                            // 0: url
                            // 1: connentTimeout
                            // 2: log
                            // 3: seekParams
                            // 4: bufferingTimeoutRetry
                            // 5: kernelAlwaysRelease
                            // 6: live
//                            boolean bufferingTimeoutRetry = (boolean) o[4];
//                            int connentTimeout = (int) o[1];
//                            getVideoKernel().startCheckBufferingTimeout(bufferingTimeoutRetry, connentTimeout);
                            break;
                        // 缓冲结束
                        case PlayerType.EventType.EVENT_BUFFERING_STOP:
                            callEventListener(PlayerType.StateType.STATE_BUFFERING_STOP);
//                            getVideoKernel().stopCheckPreparedPlaying();
                            break;
//                        // 播放开始
//                        case PlayerType.EventType.EVENT_VIDEO_START_903:
//                            // step1
//                            showVideoReal();
//                            // step2
//                            checkVideoReal();
//                            break;
//                        // 播放开始
//                        case PlayerType.EventType.EVENT_VIDEO_START_RETRY:
//                            callEventListener(PlayerType.StateType.STATE_START_RETRY);
//                            // step2
//                            showVideoReal();
//                            // step3
//                            checkVideoReal();
//                            // step5
//                            MPLogUtil.log("VideoPlayerApiKernel => onEvent => event_video_start_retry => playWhenReady = " + isPlayWhenReady());
//                            break;
//                        // 播放开始-快进
//                        case PlayerType.EventType.EVENT_VIDEO_START_SEEK:
//                            // 试看
//                            if (null != builder) {
//                                long max = builder.getMax();
//                                if (max > 0) {
//                                    callEventListener(PlayerType.StateType.STATE_TRY_BEGIN);
//                                }
//                            }
//                            // step1
//                            callEventListener(PlayerType.StateType.STATE_START_SEEK);
//                            // step2
//                            showVideoReal();
//                            // step4
//                            resume(true);
//                            // step6
//                            boolean playWhenReady1 = isPlayWhenReady();
//                            MPLogUtil.log("VideoPlayerApiKernel => onEvent => event_video_start_seek => playWhenReady = " + playWhenReady1);
//                            if (!playWhenReady1) {
//                                pause();
//                                setPlayWhenReady(true);
//                                callEventListener(PlayerType.StateType.STATE_START_PLAY_WHEN_READY_PAUSE);
//                            }
//                            break;
                        // 视频首帧
                        case PlayerType.EventType.EVENT_VIDEO_RENDERING_START:
                            callEventListener(PlayerType.StateType.STATE_VIDEO_RENDERING_START);
                            break;
                        // 播放开始-默认
                        case PlayerType.EventType.EVENT_VIDEO_START:
//                        case PlayerType.EventType.EVENT_VIDEO_SEEK_RENDERING_START: // 视频开始渲染
//            case PlayerType.MediaType.MEDIA_INFO_AUDIO_SEEK_RENDERING_START: // 视频开始渲染
                            // 埋点
                            onBuriedEventPlaying();
                            callEventListener(PlayerType.StateType.STATE_START);
                            // ijk需要刷新RenderView
                            int kernelType = args.getKernelType();
                            int renderType = args.getRenderType();
                            initRenderView(kernelType, renderType);
//                            // step3
                            checkVideoView();
                            // step4
                            boolean playWhenReady = args.isPlayWhenReady();
                            if (!playWhenReady) {
                                pause();
                                callEventListener(PlayerType.StateType.STATE_START_PLAY_WHEN_READY_PAUSE);
                            }
                            break;
                        // 快进
                        case PlayerType.EventType.EVENT_SEEK_START:
                            callEventListener(PlayerType.StateType.STATE_SEEK_START);
                            break;
                        // 快进
                        case PlayerType.EventType.EVENT_SEEK_FINISH:
                            callEventListener(PlayerType.StateType.STATE_SEEK_FINISH);
                            break;

                        // 播放错误
                        case PlayerType.EventType.EVENT_ERROR_URL:
                        case PlayerType.EventType.EVENT_ERROR_RETRY:
                        case PlayerType.EventType.EVENT_ERROR_SOURCE:
                        case PlayerType.EventType.EVENT_ERROR_PARSE:
                        case PlayerType.EventType.EVENT_ERROR_NET:
                        case PlayerType.EventType.EVENT_ERROR_BUFFERING_TIMEOUT:
                            // 埋点
                            onBuriedEventError();
                            setScreenKeep(false);
                            callEventListener(PlayerType.StateType.STATE_ERROR);
                            break;
                        // 播放结束
                        case PlayerType.EventType.EVENT_VIDEO_END:
                            callEventListener(PlayerType.StateType.STATE_END);
                            // 埋点
                            onBuriedEventCompletion();

                            boolean looping = args.isLooping();
                            if (looping) {
                                restart();
                            } else {
                                setScreenKeep(false);
                            }
                            break;
                    }
                }

                @Override
                public void onUpdateSizeChanged(int kernel, int videoWidth, int videoHeight, @PlayerType.RotationType.Value int rotation) {
                    LogUtil.log("VideoPlayerApiKernel => setKernelEvent => onUpdateSizeChanged = kernel = " + kernel + ", videoWidth = " + videoWidth + ", videoHeight = " + videoHeight + ", rotation = " + rotation);
                    setVideoFormat(videoWidth, videoHeight, rotation);
                }
            });
        } catch (Exception e) {
            LogUtil.log("VideoPlayerApiKernel => setKernelEvent => " + e.getMessage());
        }
    }

    default JSONArray getTrackInfo() {
        try {
            VideoKernelApi kernel = getVideoKernel();
            if (null == kernel)
                throw new Exception("warning: kernel null");
            JSONArray trackInfo = kernel.getTrackInfo();
            if (null == trackInfo)
                throw new Exception("trackInfo error: null");
            return trackInfo;
        } catch (Exception e) {
            LogUtil.log("VideoPlayerApiKernel => getTrackInfo => " + e.getMessage());
            return null;
        }
    }

    default boolean switchTrack(int trackId) {
        try {
            VideoKernelApi kernel = getVideoKernel();
            if (null == kernel)
                throw new Exception("warning: kernel null");
            kernel.switchTrack(trackId);
            return true;
        } catch (Exception e) {
            LogUtil.log("VideoPlayerApiKernel => switchTrack => " + e.getMessage());
            return false;
        }
    }

//    default String getScreenshotPath() {
//        try {
//            String url = getUrl();
//            MPLogUtil.log("VideoPlayerApiKernel => getScreenshotPath => url => " + url);
//            if (null == url || url.length() == 0)
//                throw new Exception("url error: " + url);
//            long position = getPosition();
//            if (position < 0)
//                throw new Exception("position error: " + position);
//            long duration = getDuration();
//            if (duration < 0)
//                throw new Exception("duration error: " + duration);
//            // android 2.3及其以上版本使用
//            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
////            retriever.setDataSource(getBaseContext(), Uri.parse(url));
//            retriever.setDataSource(url, new HashMap<String, String>());
//            // 这一句是必须的
//            String timeString = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
//            // 获取总长度,这一句也是必须的
//            long titalTime = Long.parseLong(timeString) * 1000;
//            // 通过这个计算出想截取的画面所在的时间
//            long videoPosition = titalTime * position / duration;
//            if (videoPosition < 0)
//                throw new Exception("videoPosition error: " + videoPosition);
//            Bitmap bitmap = retriever.getFrameAtTime(videoPosition, MediaMetadataRetriever.OPTION_CLOSEST);
//            return BitmapUtil.saveScreenshot(getBaseContext(), bitmap);
//        } catch (Exception e) {
//            MPLogUtil.log("VideoPlayerApiKernel => getScreenshotPath => " + e.getMessage());
//            return null;
//        }
//    }
}
