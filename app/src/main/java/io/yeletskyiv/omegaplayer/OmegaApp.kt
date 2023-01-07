package io.yeletskyiv.omegaplayer

import android.app.Application
import io.yeletskyiv.omegaplayer.di.databaseModule
import io.yeletskyiv.omegaplayer.di.networkModule
import io.yeletskyiv.omegaplayer.di.repositoryModule
import io.yeletskyiv.omegaplayer.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class OmegaApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@OmegaApp)
            modules(
                listOf(
                    networkModule,
                    databaseModule,
                    repositoryModule,
                    viewModelModule
                )
            )
        }
    }
}