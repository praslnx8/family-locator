package app.family.api.di

import android.content.Context
import app.family.api.apis.LocationAPI
import app.family.api.apis.UserApi
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class APIModule {

    @Singleton
    @Provides
    fun provideLocationAPI(@ApplicationContext context: Context): LocationAPI {
        return LocationAPI(LocationServices.getFusedLocationProviderClient(context))
    }

    @Singleton
    @Provides
    fun provideUserAPI(): UserApi {
        return UserApi(FirebaseAuth.getInstance())
    }
}