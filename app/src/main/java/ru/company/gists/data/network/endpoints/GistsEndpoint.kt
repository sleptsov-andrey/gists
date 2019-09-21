package ru.company.gists.data.network.endpoints

import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import ru.company.gists.data.network.entity.Gist

interface GistsEndpoint {
    @GET("/gists")
    fun getPublicGists(): Single<Response<List<Gist>>>

    @GET("/gists/{id}")
    fun getSingleGist(@Path("id") id: String): Single<Response<Gist>>
}
