package io.yeletskyiv.omegaplayer.viewmodel.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.yeletskyiv.omegaplayer.model.state.HomeScreenState
import io.yeletskyiv.omegaplayer.repository.M3URepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val m3URepository: M3URepository
) : ViewModel() {

    private val _homeScreenStateFlow = MutableStateFlow(
        HomeScreenState(
            isLoading = false,
            categories = emptyList(),
            error = null
        )
    )
    val homeScreenStateFlow = _homeScreenStateFlow.asStateFlow()

    fun fetchVideos() = viewModelScope.launch(Dispatchers.IO) {
        _homeScreenStateFlow.emit(
            HomeScreenState(
                true,
                _homeScreenStateFlow.value.categories,
                null
            )
        )
        val result = m3URepository.getVideos()
        _homeScreenStateFlow.emit(result)
    }

    fun setVideoCoordinates(categoryId: Int, startVideoId: Int) {
        m3URepository.setVideoCoordinates(categoryId, startVideoId)
    }

    fun changeLink() = viewModelScope.launch(Dispatchers.IO) {
        m3URepository.clearM3UTables()
    }
}