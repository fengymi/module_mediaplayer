package lib.kalu.mediaplayer.listener;

import lib.kalu.mediaplayer.type.PlayerType;


public interface OnPlayerEventListener {

    void onComplete();

    void onRestart();

    void onStart();

    void onPause();

    void onResume();

    default void onBufferingStart(){
    }

    default  void onBufferingStop(){
    }

    default  void onLoadingStart(){
    }

    default  void onLoadingStop(){
    }

    /**
     * 播放状态
     *
     * @param state 播放状态，主要是指播放器的各种状态
     */
    default void onEvent(@PlayerType.StateType.Value int state){
    }
}
