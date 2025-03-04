package com.mobatia.bisad.rest

import android.content.Context
import com.google.gson.GsonBuilder
import com.mobatia.bisad.R
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.KeyStore
import java.security.cert.CertificateFactory
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

class ApiClient(private val context: Context) {
    //dev
 //   var BASE_URL = "https://delta.mobatia.in:8083/bisadv10/public/api/v1/"

    //Live
    var BASE_URL = "https://mobile.bisad.ae/api/v1/"
    //var BASE_URL = "http://beta.mobatia.in:81/bisad_demo/api/"
    // var BASE_URL ="http://192.168.0.166/bisadv8/"

    val getClient: ApiInterface
        get() {

            val gson = GsonBuilder().setLenient().create()
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            val certificatePinner = CertificatePinner.Builder()
//                .add("nas2025.mobatia.in", "")
                .add("delta.mobatia.in", "sha256/ohmltvh1K5StBefzEp0tYM2hSfbnru5lSGaCRVTHjmU=")
                .add("mobile.bisad.ae", "sha256/igyuoGmeq2uH6W7vKJHjxo1ZsVkYvm/w/9FU8+8YKEc=")
                .build()


            val client = OkHttpClient.Builder()
                .certificatePinner(certificatePinner)
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

            return retrofit.create(ApiInterface::class.java)
        }

}