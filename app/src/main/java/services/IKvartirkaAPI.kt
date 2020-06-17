package services

import models.Cities
import models.Flats
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface IKvartirkaAPI {

    @GET("https://api.beta.kvartirka.pro/client/1.4/cities")
    fun getCity(@Query("empty") empty: Boolean, @Query("lat") lat: Double, @Query("lng") lng: Double): Call<Cities>

    @GET("https://api.beta.kvartirka.pro/client/1.4/cities")
    fun getCities(@Query("empty") empty: Boolean): Call<Cities>

    @GET("https://api.beta.kvartirka.pro/client/1.4/flats")
    fun getFlats(@Query("city_id") city_id: Long): Call<Flats>



}