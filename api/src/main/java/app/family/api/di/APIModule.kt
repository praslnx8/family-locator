package app.family.api.di

import android.content.Context
import android.location.Geocoder
import android.media.AudioManager
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import app.family.api.apis.DeviceApi
import app.family.api.apis.LocalityApi
import app.family.api.apis.LocationAPI
import app.family.api.apis.MyStatusApi
import app.family.api.apis.UserApi
import app.family.api.models.StatusProto
import app.family.api.proto.StatusProtoSerializer
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
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
    fun provideLocationAPI(@ApplicationContext context: Context): LocationAPI {
        return LocationAPI(LocationServices.getFusedLocationProviderClient(context))
    }

    @Singleton
    @Provides
    fun provideUserAPI(): UserApi {
        return UserApi(FirebaseAuth.getInstance())
    }

    @Singleton
    @Provides
    fun provideMyStatusApi(@ApplicationContext context: Context): MyStatusApi {
        return MyStatusApi(context.statusDataStore)
    }

    @Singleton
    @Provides
    fun provideLocalityInfoProvider(@ApplicationContext context: Context): LocalityApi {
        return LocalityApi(Geocoder(context, Locale.getDefault()))
    }

    @Singleton
    @Provides
    fun provideDeviceApi(@ApplicationContext context: Context): DeviceApi {
        return DeviceApi(context, context.getSystemService(Context.AUDIO_SERVICE) as AudioManager)
    }
}