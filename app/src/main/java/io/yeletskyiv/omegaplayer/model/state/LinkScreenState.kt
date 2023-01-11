package io.yeletskyiv.omegaplayer.model.state

import io.yeletskyiv.omegaplayer.model.error.OmegaError

data class LinkScreenState(
    val isLoading: Boolean,
    val isSuccess: Boolean,
    val error: OmegaError?
)