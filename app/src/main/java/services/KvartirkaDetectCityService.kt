package services

import com.google.gson.Gson
import models.Cities
import models.City
import okhttp3.Callback
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import java.io.IOException
import java.util.ArrayList


class KvartirkaDetectCityService(private val apiClient: IKvartirkaAPI) {

    fun startGetCity(ltd: Double, lng: Double,getCityCallback: (city: City?)->Unit) {
        val call = apiClient.getCity(true, ltd, lng)
        call.enqueue(object : retrofit2.Callback<Cities> {
            override fun onResponse(
                call: Call<Cities>,
                response: Response<Cities>
            ) {
                val city = response.body()
                city?.let {
                    if (!it.cities.isEmpty()) {
                        getCityCallback(it.cities[0])
                    }
                    else getCityCallback(null)
                }
            }

            override fun onFailure(
                call: Call<Cities>,
                t: Throwable
            ) {
            }
        })
    }

    fun startGetAllCities(getCitiesListCallback: (cities: Cities)->Unit) {
        val call = apiClient.getCities(true)
        call.enqueue(object : retrofit2.Callback<Cities> {
            override fun onResponse(
                call: Call<Cities>,
                response: Response<Cities>
            ) {
                val cities = response.body()
                getCitiesListCallback(cities)
            }

            override fun onFailure(
                call: Call<Cities>,
                t: Throwable
            ) {
            }
        })

    }
}