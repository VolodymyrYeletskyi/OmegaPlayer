package io.yeletskyiv.omegaplayer.network

import com.haroldadmin.cnradapter.NetworkResponse
import retrofit2.http.GET
import retrofit2.http.Url

interface M3ULinkApi {

    @GET
    suspend fun getM3UResponse(@Url url: String): NetworkResponse<String, String>
}