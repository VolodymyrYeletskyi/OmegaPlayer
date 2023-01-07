package io.yeletskyiv.omegaplayer.model.state

import io.yeletskyiv.omegaplayer.model.error.Error

data class LinkScreenState(
    val isLoading: Boolean,
    val isSuccess: Boolean,
    val error: Error?
)