package com.mobatia.bisad.rest

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    //dev
 // var BASE_URL = "http://gama.mobatia.in:8080/bisadv10/public/api/v1/"
  // var BASE_URL = "http://ec2-3-85-200-232.compute-1.amazonaws.com/bisadv10/public/api/v1/"
    //var BASE_URL = "http://bisad.mobatia.in:8081/api/v1/"
//var BASE_URL = "https://stagingcms.bisad.ae/"
    //Live
  var BASE_URL = "https://mobile.bisad.ae/api/v1/"
   //var BASE_URL = "http://beta.mobatia.in:81/bisad_demo/api/"
   // var BASE_URL ="http://192.168.0.166/bisadv8/"

    val getClient: ApiInterface
        get() {
            val gson = GsonBuilder()
                .setLenient()
                .create()
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

            return retrofit.create(ApiInterface::class.java)

        }

}