package io.yeletskyiv.omegaplayer.ui.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import io.yeletskyiv.omegaplayer.R
import io.yeletskyiv.omegaplayer.databinding.FragmentHomeBinding
import io.yeletskyiv.omegaplayer.mapper.mapError
import io.yeletskyiv.omegaplayer.model.entity.M3UVideoItem
import io.yeletskyiv.omegaplayer.model.state.HomeScreenState
import io.yeletskyiv.omegaplayer.ui.home.adapter.CategoryWithVideosAdapter
import io.yeletskyiv.omegaplayer.viewmodel.home.HomeViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val viewBinding: FragmentHomeBinding by viewBinding(FragmentHomeBinding::bind)

    private val homeViewModel by viewModel<HomeViewModel>()

    private val categoriesAdapter by lazy { CategoryWithVideosAdapter(::onVideoClick) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()

        context?.let { context ->
            with(viewBinding) {
                changeLinkButton.setOnClickListener { changeLink() }
                categoriesRecyclerView.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                categoriesRecyclerView.adapter = categoriesAdapter
            }
            homeViewModel.fetchVideos()
        }
    }

    private fun initViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                homeViewModel.homeScreenStateFlow.collectLatest { updateHomeScreenUI(it) }
            }
        }
    }

    private fun updateHomeScreenUI(state: HomeScreenState) {
        when {
            state.isLoading -> {
                handleLoading(true)
            }
            state.error != null -> {
                handleLoading(false)
                context?.let { Toast.makeText(it, mapError(it, state.error), Toast.LENGTH_LONG).show() }
            }
            else -> {
                handleLoading(false)
                categoriesAdapter.submitList(state.categories)
            }
        }
    }

    private fun onVideoClick(video: M3UVideoItem) {
        homeViewModel.setVideoCoordinates(video.videoCategoryId, video.id)
        findNavController()
            .navigate(HomeFragmentDirections.actionHomeFragmentToVideoPlayerFragment())
    }

    private fun handleLoading(isLoading: Boolean) {
        viewBinding.loader.isVisible = isLoading
    }

    private fun changeLink() {
        homeViewModel.changeLink()
        findNavController()
            .navigate(HomeFragmentDirections.actionHomeFragmentToLinkFragment())
    }
}