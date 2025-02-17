package com.kirkpatrick.lunchtime

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.kirkpatrick.lunchtime.network.DefaultPlacesRepository
import com.kirkpatrick.lunchtime.network.PlacesApi
import com.kirkpatrick.lunchtime.network.PlacesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    fun provideOkHttpClient() : OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        //TODO Move to configuration object
        val fields = listOf("places.id", "places.displayName", "places.rating",
            "places.user_rating_count", "places.price_level", "places.location",
            "places.formattedAddress", "places.editorialSummary").joinToString(",")

        return OkHttpClient.Builder()
            .addInterceptor(Interceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("X-Goog-FieldMask", fields)
                    .addHeader("X-Goog-Api-Key", BuildConfig.PLACES_API_KEY)
                    //.addHeader("X-Goog-Api-Key", "AIzaSyC8jnuXp0CJr8FzYMg_mG-d7__1xUdfiQw") //Uncomment if you want to use my key and remove line above
                    .build()
                chain.proceed(request)
            })
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    fun provideGson() : Gson {
        return GsonBuilder().setLenient().create()
    }

    @Provides
    fun providePlacesRepository(okHttpClient: OkHttpClient, gson: Gson) : PlacesRepository {

        val retrofit = Retrofit.Builder()
            .baseUrl("https://places.googleapis.com")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()

        return DefaultPlacesRepository(retrofit.create(PlacesApi::class.java))
    }
}