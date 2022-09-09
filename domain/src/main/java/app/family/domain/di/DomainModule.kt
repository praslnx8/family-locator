package app.family.domain.di

import app.family.api.apis.DeviceApi
import app.family.api.apis.LocalityApi
import app.family.api.apis.LocationAPI
import app.family.api.apis.MyStatusApi
import app.family.api.apis.UserApi
import app.family.domain.usecases.LoginUseCase
import app.family.domain.usecases.MyStatusSyncUseCase
import app.family.domain.usecases.MyStatusUseCase
import app.family.domain.usecases.UserUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DomainModule {

    @Provides
    fun provideLoginUseCase(userApi: UserApi): LoginUseCase {
        return LoginUseCase(userApi)
    }

    @Provides
    fun provideStatusSyncUseCase(
        locationAPI: LocationAPI,
        localityApi: LocalityApi,
        myStatusApi: MyStatusApi,
        deviceApi: DeviceApi
    ): MyStatusSyncUseCase {
        return MyStatusSyncUseCase(locationAPI, localityApi, myStatusApi, deviceApi)
    }

    @Provides
    fun provideMyStatusUseCase(myStatusApi: MyStatusApi): MyStatusUseCase {
        return MyStatusUseCase(myStatusApi)
    }

    @Provides
    fun provideUserUseCase(userApi: UserApi): UserUseCase {
        return UserUseCase(userApi)
    }
}