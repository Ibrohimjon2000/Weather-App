package uz.mobiler.lesson71.networking

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import uz.mobiler.lesson71.models.WeatherModel

interface ApiService {

    @GET("data/2.5/weather?")
    fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") APIKEY: String
    ): Call<WeatherModel>
}