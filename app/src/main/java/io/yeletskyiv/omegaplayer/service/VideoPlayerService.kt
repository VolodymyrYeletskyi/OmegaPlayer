package io.yeletskyiv.omegaplayer.service

import android.net.Uri
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.util.EventLogger
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import io.yeletskyiv.omegaplayer.model.entity.M3UVideoItem
import io.yeletskyiv.omegaplayer.usecase.GetVideosByCategoryUseCase
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import org.koin.core.component.KoinScopeComponent
import org.koin.core.component.createScope
import org.koin.core.scope.Scope

class VideoPlayerService : MediaSessionService(), KoinScopeComponent {

    override val scope: Scope by lazy { createScope(this) }

    private val player: ExoPlayer by inject()
    private var session: MediaSession? = null

    private val getVideosByCategoryUseCase: GetVideosByCategoryUseCase by inject()

    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo) = session

    override fun onCreate() {
        super.onCreate()

        session = player.let {
            MediaSession.Builder(this, it)
                .build()
        }
        player.addAnalyticsListener(EventLogger())
        serviceScope.launch {
            val data = getVideosByCategoryUseCase()
            val mediaItems = createMediaItems(data.first)
            withContext(Dispatchers.Main) {
                player.setMediaItems(mediaItems, data.second, C.TIME_UNSET)
            }
        }
    }

    override fun onDestroy() {
        player.release()
        session?.release()
        session = null

        super.onDestroy()
    }

    private fun createMediaItems(items: List<M3UVideoItem>): List<MediaItem> {
        val mediaItems = mutableListOf<MediaItem>()
        items.forEach {
            val mediaMetadata = MediaMetadata.Builder()
                .setTitle(it.title)
                .setArtworkUri(Uri.parse(it.imageUrl ?: ""))
                .build()
            val url = it.mediaUrl ?: ""
            mediaItems.add(
                MediaItem.Builder()
                    .setUri(url)
                    .setMediaMetadata(mediaMetadata)
                    .build()
            )
        }
        return mediaItems
    }
}