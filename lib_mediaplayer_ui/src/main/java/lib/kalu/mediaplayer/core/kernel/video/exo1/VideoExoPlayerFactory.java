package lib.kalu.mediaplayer.core.kernel.video.exo1;


import lib.kalu.mediaplayer.core.kernel.video.VideoKernelFactory;


public final class VideoExoPlayerFactory implements VideoKernelFactory<VideoExoPlayer> {

    private VideoExoPlayerFactory() {
    }

    public static VideoExoPlayerFactory build() {
        return new VideoExoPlayerFactory();
    }

    @Override
    public VideoExoPlayer createKernel() {
        return new VideoExoPlayer();
    }
}
