package io.yeletskyiv.omegaplayer.model.state

import io.yeletskyiv.omegaplayer.model.relations.CategoryWithVideos
import io.yeletskyiv.omegaplayer.model.error.OmegaError

data class HomeScreenState(
    val isLoading: Boolean,
    val categories: List<CategoryWithVideos>,
    val error: OmegaError?
)
