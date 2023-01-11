package io.yeletskyiv.omegaplayer.mapper

import android.content.Context
import io.yeletskyiv.omegaplayer.R
import io.yeletskyiv.omegaplayer.model.error.OmegaError

fun mapError(context: Context, omegaError: OmegaError) =
    when (omegaError) {
        is OmegaError.NetworkOmegaError -> context.getString(R.string.no_network_message)
        is OmegaError.DatabaseOmegaError -> context.getString(R.string.database_error_message)
        is OmegaError.M3UDownloadOmegaError -> omegaError.message
        is OmegaError.UnknownOmegaError -> omegaError.message
    }