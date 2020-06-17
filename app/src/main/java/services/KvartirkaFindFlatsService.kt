package services

import models.City
import models.Flats
import retrofit2.Call
import retrofit2.Response

class KvartirkaFindFlatsService(private val apiClient: IKvartirkaAPI, private val city_id: Long) {

    fun startGetFlats(getFlatsCallback: (flats: Flats?)->Unit) {
        val call = apiClient.getFlats(city_id)
        call.enqueue(object: retrofit2.Callback<Flats> {
            override fun onResponse(call: Call<Flats>?, response: Response<Flats>) {
                val flats = response.body()
                getFlatsCallback(flats)
            }

            override fun onFailure(call: Call<Flats>?, t: Throwable?) {
            }

        })
    }
}