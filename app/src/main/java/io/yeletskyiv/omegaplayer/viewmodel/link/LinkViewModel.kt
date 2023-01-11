package io.yeletskyiv.omegaplayer.viewmodel.link

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.yeletskyiv.omegaplayer.model.state.LinkScreenState
import io.yeletskyiv.omegaplayer.repository.M3URepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LinkViewModel(
    private val m3URepository: M3URepository
) : ViewModel() {

    private val _linkScreenStateFlow = MutableStateFlow(
        LinkScreenState(
            isLoading = false,
            isSuccess = false,
            error = null
        )
    )
    val linkScreenStateFlow = _linkScreenStateFlow.asStateFlow()

    fun loadUrl(url: String) = viewModelScope.launch(Dispatchers.IO) {
        _linkScreenStateFlow.emit(
            LinkScreenState(
                isLoading = true,
                isSuccess = false,
                error = null
            )
        )
        val result = m3URepository.getM3UContent(url)
        _linkScreenStateFlow.emit(result)
    }
}