package io.yeletskyiv.omegaplayer.model.state

import io.yeletskyiv.omegaplayer.model.error.PlayerError

data class VideoPlayerScreenState(
    val idle: Boolean,
    val buffering: Boolean,
    val ready: Boolean,
    val ended: Boolean,
    val error: PlayerError?
)