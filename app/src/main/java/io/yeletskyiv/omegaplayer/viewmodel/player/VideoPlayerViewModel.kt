package io.yeletskyiv.omegaplayer.viewmodel.player

import androidx.lifecycle.ViewModel
import androidx.media3.session.MediaController
import io.yeletskyiv.omegaplayer.manager.VideoPlayerManager

class VideoPlayerViewModel(
    private val videoPlayerManager: VideoPlayerManager
) : ViewModel() {

    val videoPlayerScreenStateFlow = videoPlayerManager.videoPlayerScreenStateFlow

    fun initController(controller: MediaController) {
        videoPlayerManager.initController(controller)
    }

    fun play() {
        videoPlayerManager.play()
    }

    fun resume() {
        videoPlayerManager.resume()
    }

    fun pause() {
        videoPlayerManager.pause()
    }

    fun stop() {
        videoPlayerManager.stop()
    }
}