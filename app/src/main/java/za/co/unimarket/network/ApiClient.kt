package za.co.unimarket.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Singleton network client manager providing Retrofit instances and authentication header configuration.
 */
object ApiClient {

    private var baseUrl: String = "http://172.16.36.188:5076/"
    private var authToken: String? = null

    fun setAuthToken(token: String?) {
        this.authToken = token
    }

    fun getAuthToken(): String? = authToken

    fun setBaseUrl(url: String) {
        this.baseUrl = if (url.endsWith("/")) url else "$url/"
        retrofitInstance = createRetrofit()
    }

    private val authInterceptor = Interceptor { chain ->
        val original = chain.request()
        val builder = original.newBuilder()
            .header("Content-Type", "application/json")
            .header("Accept", "application/json")

        authToken?.let { token ->
            builder.header("Authorization", if (token.startsWith("Bearer ")) token else "Bearer $token")
        }

        chain.proceed(builder.build())
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private var retrofitInstance: Retrofit = createRetrofit()

    val apiService: UniMarketApiService
        get() = retrofitInstance.create(UniMarketApiService::class.java)
}
