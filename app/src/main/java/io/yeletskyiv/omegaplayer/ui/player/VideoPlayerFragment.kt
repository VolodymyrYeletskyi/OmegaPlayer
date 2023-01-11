package io.yeletskyiv.omegaplayer.ui.player

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import io.yeletskyiv.omegaplayer.R
import io.yeletskyiv.omegaplayer.databinding.FragmentVideoPlayerBinding
import io.yeletskyiv.omegaplayer.model.state.VideoPlayerScreenState
import io.yeletskyiv.omegaplayer.service.VideoPlayerService
import io.yeletskyiv.omegaplayer.viewmodel.player.VideoPlayerViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class VideoPlayerFragment : Fragment(R.layout.fragment_video_player) {

    private val viewBinding: FragmentVideoPlayerBinding by viewBinding(FragmentVideoPlayerBinding::bind)

    private val videoPlayerViewModel by viewModel<VideoPlayerViewModel>()

    private var controllerFuture: ListenableFuture<MediaController>? = null
    private val controller: MediaController?
        get() = if (controllerFuture?.isDone == true) controllerFuture?.get() else null

    override fun onStart() {
        super.onStart()

        context?.let { context ->
            val sessionToken =
                SessionToken(context, ComponentName(context, VideoPlayerService::class.java))

            controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
            controllerFuture?.addListener(
                { setController() },
                MoreExecutors.directExecutor()
            )
        }
    }

    override fun onResume() {
        super.onResume()

        hideSystemUi()
    }

    override fun onStop() {
        super.onStop()

        controllerFuture?.let { MediaController.releaseFuture(it) }
    }

    override fun onDestroy() {
        super.onDestroy()

        activity?.let { it.stopService(Intent(it, VideoPlayerService::class.java)) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
    }

    private fun initViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                videoPlayerViewModel.videoPlayerScreenStateFlow.collectLatest {
                    updateVideoPlayerScreenUI(it)
                }
            }
        }
    }

    private fun updateVideoPlayerScreenUI(state: VideoPlayerScreenState) {
        when {
            state.error != null -> {}
            state.idle -> {
                handleLoading(true)
            }
            state.buffering -> {
                handleLoading(true)
            }
            state.ready -> {
                handleLoading(false)
            }
            state.ended -> {}
        }
    }

    private fun setController() {
        val controller = this.controller ?: return
        viewBinding.playerView.player = controller
        videoPlayerViewModel.initController(controller)
    }

    private fun hideSystemUi() {
        activity?.window?.let {
            WindowCompat.setDecorFitsSystemWindows(it, false)
            WindowInsetsControllerCompat(it, viewBinding.playerView).let { controller ->
                controller.hide(WindowInsetsCompat.Type.systemBars())
                controller.systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }
    }

    private fun handleLoading(isLoading: Boolean) {
        viewBinding.loader.isVisible = isLoading
    }
}