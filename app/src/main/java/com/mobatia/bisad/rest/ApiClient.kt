package com.mobatia.bisad.rest

import android.content.Context
import com.google.gson.GsonBuilder
import com.mobatia.bisad.R
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
    var BASE_URL = "http://gama.mobatia.in:8080/bisadv10/public/api/v1/"

    //Live
    //var BASE_URL = "https://mobile.bisad.ae/api/v1/"
    //var BASE_URL = "http://beta.mobatia.in:81/bisad_demo/api/"
    // var BASE_URL ="http://192.168.0.166/bisadv8/"

    val getClient: ApiInterface
        get() {
            val gson = GsonBuilder()
                .setLenient()
                .create()

            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            val client = OkHttpClient.Builder()
                .apply {
                    // SSL Pinning Setup
                    try {
                        val certificateFactory = CertificateFactory.getInstance("X.509")
                        val inputStream =
                            context.resources.openRawResource(R.raw.mobatia) // Your PEM file
                        val certificate = certificateFactory.generateCertificate(inputStream)
                        inputStream.close()

                        // Create a KeyStore containing the trusted certificate
                        val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
                        keyStore.load(null)
                        keyStore.setCertificateEntry("trusted_cert", certificate)

                        // Create a TrustManager that trusts the certificate in the KeyStore
                        val trustManagerFactory = TrustManagerFactory.getInstance(
                            TrustManagerFactory.getDefaultAlgorithm()
                        )
                        trustManagerFactory.init(keyStore)

                        // Create an SSLContext that uses the TrustManager
                        val sslContext = SSLContext.getInstance("TLS")
                        sslContext.init(null, trustManagerFactory.trustManagers, null)

                        // Set the SSL socket factory with the custom certificate
                        sslSocketFactory(
                            sslContext.socketFactory,
                            trustManagerFactory.trustManagers[0] as X509TrustManager
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                        throw RuntimeException("Failed to set up SSL pinning", e)
                    }
                }
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