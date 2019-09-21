package ru.company.gists.data.network

import java.util.concurrent.TimeUnit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.company.gists.data.network.endpoints.GistsEndpoint

class RestApi {

    private val gistsEndpoint: GistsEndpoint

    init {
        val httpClient = buildOkHttpClient()
        val retrofit = buildRetrofitClient(httpClient)
        gistsEndpoint = retrofit.create(GistsEndpoint::class.java)
    }

    private fun buildRetrofitClient(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .baseUrl(URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    private fun buildOkHttpClient(): OkHttpClient {
        val networkLogInterceptor = HttpLoggingInterceptor()
        networkLogInterceptor.level = HttpLoggingInterceptor.Level.BASIC

        return OkHttpClient.Builder()
                .addInterceptor(networkLogInterceptor)
                .connectTimeout(TIMEOUT_IN_SECONDS.toLong(), TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT_IN_SECONDS.toLong(), TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_IN_SECONDS.toLong(), TimeUnit.SECONDS)
                .build()
    }

    fun gists(): GistsEndpoint {
        return gistsEndpoint
    }

    companion object {
        private const val URL = "https://api.github.com"
        private const val TIMEOUT_IN_SECONDS = 2
        var instance: RestApi = RestApi()
    }

}
