package io.yeletskyiv.omegaplayer.model.error

sealed class Error {

    object NetworkError : Error()

    object DatabaseError : Error()

    data class M3UDownloadError(val message: String?) : Error()

    data class UnknownError(val message: String?) : Error()
}
