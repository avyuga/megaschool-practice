package ru.sample.duckapp.data

import retrofit2.Call
import retrofit2.http.GET
import ru.sample.duckapp.domain.Duck
import okhttp3.ResponseBody
import retrofit2.http.Url


interface DucksApi {
    @GET("random")
    fun getRandomDuck(): Call<Duck>

    @GET
    fun getHttpDuckApi(@Url url: String) : Call<ResponseBody>
}