package io.yeletskyiv.omegaplayer.manager

import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import io.yeletskyiv.omegaplayer.model.error.PlayerError
import io.yeletskyiv.omegaplayer.model.state.VideoPlayerScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class VideoPlayerManager {

    private var mediaController: MediaController? = null

    private val _videoPlayerScreenState = MutableStateFlow(
        VideoPlayerScreenState(
            idle = true,
            buffering = false,
            ready = false,
            ended = false,
            error = null
        )
    )
    val videoPlayerScreenStateFlow = _videoPlayerScreenState.asStateFlow()

    private val playerListener = object : Player.Listener {

        override fun onPlaybackStateChanged(playbackState: Int) {
            when(playbackState) {
                Player.STATE_IDLE -> {
                    _videoPlayerScreenState.tryEmit(
                        VideoPlayerScreenState(
                            idle = true,
                            buffering = false,
                            ready = false,
                            ended = false,
                            error = null
                        )
                    )
                }
                Player.STATE_BUFFERING -> {
                    _videoPlayerScreenState.tryEmit(
                        VideoPlayerScreenState(
                            idle = false,
                            buffering = true,
                            ready = false,
                            ended = false,
                            error = null
                        )
                    )
                }
                Player.STATE_READY -> {
                    _videoPlayerScreenState.tryEmit(
                        VideoPlayerScreenState(
                            idle = false,
                            buffering = false,
                            ready = true,
                            ended = false,
                            error = null
                        )
                    )
                }
                Player.STATE_ENDED -> {
                    _videoPlayerScreenState.tryEmit(
                        VideoPlayerScreenState(
                            idle = false,
                            buffering = false,
                            ready = false,
                            ended = true,
                            error = null
                        )
                    )
                }
            }
        }

        override fun onPlayerError(error: PlaybackException) {
            when(error.errorCode) {
                PlaybackException.ERROR_CODE_REMOTE_ERROR -> {
                    _videoPlayerScreenState.tryEmit(
                        _videoPlayerScreenState.value.copy(error = PlayerError.REMOTE_ERROR)
                    )
                }
                PlaybackException.ERROR_CODE_BEHIND_LIVE_WINDOW -> {
                    _videoPlayerScreenState.tryEmit(
                        _videoPlayerScreenState.value.copy(error = PlayerError.BEHIND_LIVE_WINDOW_ERROR)
                    )
                }
                PlaybackException.ERROR_CODE_TIMEOUT,
                    PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_TIMEOUT -> {
                    _videoPlayerScreenState.tryEmit(
                        _videoPlayerScreenState.value.copy(error = PlayerError.TIMEOUT_ERROR)
                    )
                }
                PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED -> {
                    _videoPlayerScreenState.tryEmit(
                        _videoPlayerScreenState.value.copy(error = PlayerError.NETWORK_ERROR)
                    )
                }
                PlaybackException.ERROR_CODE_DECODER_INIT_FAILED,
                    PlaybackException.ERROR_CODE_DECODER_QUERY_FAILED,
                    PlaybackException.ERROR_CODE_DECODING_FAILED,
                    PlaybackException.ERROR_CODE_DECODING_FORMAT_EXCEEDS_CAPABILITIES,
                    PlaybackException.ERROR_CODE_DECODING_FORMAT_UNSUPPORTED -> {
                    _videoPlayerScreenState.tryEmit(
                        _videoPlayerScreenState.value.copy(error = PlayerError.DECODER_ERROR)
                    )
                    }
                else -> {
                    _videoPlayerScreenState.tryEmit(
                        _videoPlayerScreenState.value.copy(error = PlayerError.UNSPECIFIED_ERROR)
                    )
                }
            }
        }
    }

    fun initController(controller: MediaController) {
        if (mediaController == null) {
            mediaController = controller
            mediaController?.addListener(playerListener)
            play()
        }
    }

    fun play() {
        mediaController?.prepare()
        mediaController?.play()
    }

    fun resume() {
        mediaController?.play()
    }

    fun pause() {
        mediaController?.pause()
    }

    fun stop() {
        mediaController?.stop()
    }
}