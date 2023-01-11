package io.yeletskyiv.omegaplayer.model.error

sealed class OmegaError {

    object NetworkOmegaError : OmegaError()

    object DatabaseOmegaError : OmegaError()

    data class M3UDownloadOmegaError(val message: String?) : OmegaError()

    data class UnknownOmegaError(val message: String?) : OmegaError()
}

enum class PlayerError {
    REMOTE_ERROR,
    BEHIND_LIVE_WINDOW_ERROR,
    TIMEOUT_ERROR,
    NETWORK_ERROR,
    DECODER_ERROR,
    UNSPECIFIED_ERROR
}
