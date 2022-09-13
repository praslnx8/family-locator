package app.family.api.di

import android.content.Context
import android.location.Geocoder
import android.media.AudioManager
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import app.family.api.BuildConfig
import app.family.api.apis.AuthApi
import app.family.api.apis.DeviceApi
import app.family.api.apis.FamilyApi
import app.family.api.apis.InviteApi
import app.family.api.apis.LocalityApi
import app.family.api.apis.LocationAPI
import app.family.api.apis.MyStatusApi
import app.family.api.apis.UserApi
import app.family.api.apis.WeatherApi
import app.family.api.models.StatusProto
import app.family.api.network.WeatherApiClient
import app.family.api.proto.StatusProtoSerializer
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import javax.inject.Singleton

private val Context.statusDataStore: DataStore<StatusProto> by dataStore(
    fileName = "status.pb",
    serializer = StatusProtoSerializer
)

@InstallIn(SingletonComponent::class)
@Module
class APIModule {

    @Singleton
    @Provides
    fun provideFirebaseDatabase(): FirebaseDatabase {
        val database = Firebase.database
        database.setPersistenceEnabled(true)
        return database
    }

    @Singleton
    @Provides
    fun provideLocationAPI(@ApplicationContext context: Context): LocationAPI {
        return LocationAPI(LocationServices.getFusedLocationProviderClient(context))
    }

    @Singleton
    @Provides
    fun provideAuthAPI(): AuthApi {
        return AuthApi(FirebaseAuth.getInstance())
    }

    @Singleton
    @Provides
    fun provideMyStatusApi(@ApplicationContext context: Context): MyStatusApi {
        return MyStatusApi(context.statusDataStore)
    }

    @Provides
    fun provideLocalityInfoProvider(@ApplicationContext context: Context): LocalityApi {
        return LocalityApi(Geocoder(context, Locale.getDefault()))
    }

    @Singleton
    @Provides
    fun provideDeviceApi(@ApplicationContext context: Context): DeviceApi {
        return DeviceApi(context, context.getSystemService(Context.AUDIO_SERVICE) as AudioManager)
    }

    @Singleton
    @Provides
    fun provideFamilyApi(database: FirebaseDatabase): FamilyApi {
        return FamilyApi(database.getReference("families"))
    }

    @Singleton
    @Provides
    fun provideUserApi(database: FirebaseDatabase): UserApi {
        return UserApi(database.getReference("users"))
    }

    @Singleton
    @Provides
    fun provideInviteApi(database: FirebaseDatabase): InviteApi {
        return InviteApi(database.getReference("invitations"))
    }

    @Singleton
    @Provides
    fun provideWeatherApi(): WeatherApi {
        val weatherApiClient = Retrofit.Builder()
            .baseUrl(BuildConfig.WEATHER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return WeatherApi(weatherApiClient.create(WeatherApiClient::class.java))
    }
}