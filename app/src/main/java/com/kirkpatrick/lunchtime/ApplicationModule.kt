package com.kirkpatrick.lunchtime

import android.content.Context
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    fun providePlacesClient(@ApplicationContext context: Context): PlacesClient {
        return Places.createClient(context)
    }
}