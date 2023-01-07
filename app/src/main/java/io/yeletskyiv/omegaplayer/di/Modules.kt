package io.yeletskyiv.omegaplayer.di

import android.content.Context
import androidx.room.Room
import com.haroldadmin.cnradapter.NetworkResponseAdapterFactory
import io.yeletskyiv.omegaplayer.database.OmegaDatabase
import io.yeletskyiv.omegaplayer.network.M3ULinkApi
import io.yeletskyiv.omegaplayer.repository.M3URepository
import io.yeletskyiv.omegaplayer.viewmodel.home.HomeViewModel
import io.yeletskyiv.omegaplayer.viewmodel.link.LinkViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

val viewModelModule = module {
    viewModel { LinkViewModel(get()) }
    viewModel { HomeViewModel(get()) }
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