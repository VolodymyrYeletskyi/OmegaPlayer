package io.yeletskyiv.omegaplayer.ui.link

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import io.yeletskyiv.omegaplayer.R
import io.yeletskyiv.omegaplayer.databinding.FragmentLinkBinding
import io.yeletskyiv.omegaplayer.mapper.mapError
import io.yeletskyiv.omegaplayer.model.state.LinkScreenState
import io.yeletskyiv.omegaplayer.viewmodel.link.LinkViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class LinkFragment : Fragment(R.layout.fragment_link) {

    private val viewBinding: FragmentLinkBinding by viewBinding(FragmentLinkBinding::bind)

    private val linkViewModel by viewModel<LinkViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()

        with(viewBinding) {
            enterLinkText.setText("https://iptv-org.github.io/iptv/index.m3u")
            downloadLinkButton.setOnClickListener {
                linkViewModel.loadUrl(enterLinkText.text.toString())
            }
        }
    }

    private fun initViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                linkViewModel.linkScreenStateFlow.collectLatest { updateLinkScreenUI(it) }
            }
        }
    }

    private fun updateLinkScreenUI(state: LinkScreenState) {
        when {
            state.isLoading -> {
                handleLoading(true)
            }
            state.isSuccess -> {
                handleLoading(false)
                findNavController()
                    .navigate(LinkFragmentDirections.actionLinkFragmentToHomeFragment())
            }
            state.error != null -> {
                handleLoading(false)
                context?.let {
                    Toast.makeText(it, mapError(it, state.error), Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun handleLoading(isLoading: Boolean) {
        viewBinding.loader.isVisible = isLoading
    }
}