package io.yeletskyiv.omegaplayer.mapper

import android.content.Context
import io.yeletskyiv.omegaplayer.R
import io.yeletskyiv.omegaplayer.model.error.Error

fun mapError(context: Context, error: Error) =
    when (error) {
        is Error.NetworkError -> context.getString(R.string.no_network_message)
        is Error.DatabaseError -> context.getString(R.string.database_error_message)
        is Error.M3UDownloadError -> error.message
        is Error.UnknownError -> error.message
    }