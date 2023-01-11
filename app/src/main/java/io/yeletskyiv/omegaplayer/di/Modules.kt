package io.yeletskyiv.omegaplayer.di

import android.content.Context
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.exoplayer.ExoPlayer
import androidx.room.Room
import com.haroldadmin.cnradapter.NetworkResponseAdapterFactory
import io.yeletskyiv.omegaplayer.database.OmegaDatabase
import io.yeletskyiv.omegaplayer.manager.VideoPlayerManager
import io.yeletskyiv.omegaplayer.network.M3ULinkApi
import io.yeletskyiv.omegaplayer.repository.M3URepository
import io.yeletskyiv.omegaplayer.service.VideoPlayerService
import io.yeletskyiv.omegaplayer.usecase.GetVideosByCategoryUseCase
import io.yeletskyiv.omegaplayer.viewmodel.home.HomeViewModel
import io.yeletskyiv.omegaplayer.viewmodel.link.LinkViewModel
import io.yeletskyiv.omegaplayer.viewmodel.player.VideoPlayerViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

val viewModelModule = module {
    viewModel { LinkViewModel(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { VideoPlayerViewModel(get()) }
}

val networkModule = module {
    single { createM3URetrofit() }
    single { get<Retrofit>().create(M3ULinkApi::class.java) }
}

val databaseModule = module {
    single { createDatabase(androidContext()) }
    single { get<OmegaDatabase>().m3uCategoryDao() }
}

val repositoryModule = module {
    single { M3URepository(get(), get()) }
}

val playerModule = module {
    scope<VideoPlayerService> {
        scoped { createExoPlayer(androidContext(), createAudioAttributes()) }
        scoped { GetVideosByCategoryUseCase(get()) }
    }
    factory { VideoPlayerManager() }
}

private fun createM3URetrofit() = Retrofit.Builder()
    .baseUrl("http://localhost/")
    .addCallAdapterFactory(NetworkResponseAdapterFactory())
    .addConverterFactory(ScalarsConverterFactory.create())
    .build()

private fun createDatabase(context: Context) =
    Room.databaseBuilder(
        context,
        OmegaDatabase::class.java,
        "omega_database"
    )
        .fallbackToDestructiveMigration()
        .build()

private fun createExoPlayer(
    context: Context,
    audioAttributes: AudioAttributes
) = ExoPlayer.Builder(context)
    .setAudioAttributes(audioAttributes, true)
    .build()

private fun createAudioAttributes() = AudioAttributes.Builder()
    .setUsage(C.USAGE_MEDIA)
    .setContentType(C.AUDIO_CONTENT_TYPE_MOVIE)
    .build()