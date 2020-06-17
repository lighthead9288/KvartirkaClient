package services

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitService {

    constructor() {
        val okHttpClientBuilder: OkHttpClient.Builder = okhttp3.OkHttpClient.Builder()
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        okHttpClientBuilder.addInterceptor(logging)
        val builder: Retrofit.Builder = Retrofit.Builder()
            .baseUrl("http://api.beta.kvartirka.pro/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClientBuilder.build())
        Log.d("MyLog", "Connected")
        retrofit = builder.build()
    }

    private lateinit var retrofit: Retrofit

    fun GetRetrofitEntity(): Retrofit {
        return retrofit
    }
}