package app.family.domain.di

import app.family.api.apis.AuthApi
import app.family.api.apis.DeviceApi
import app.family.api.apis.FamilyApi
import app.family.api.apis.InviteApi
import app.family.api.apis.LocalityApi
import app.family.api.apis.LocationAPI
import app.family.api.apis.MyStatusApi
import app.family.api.apis.UserApi
import app.family.api.apis.WeatherApi
import app.family.domain.mappers.StatusMapper
import app.family.domain.mappers.UserStatusMapper
import app.family.domain.usecases.FamilyCreateUseCase
import app.family.domain.usecases.FamilyInviteUseCase
import app.family.domain.usecases.FetchFamilyStatusUseCase
import app.family.domain.usecases.LoginUseCase
import app.family.domain.usecases.MyStatusSyncUseCase
import app.family.domain.usecases.MyStatusUseCase
import app.family.domain.usecases.UploadStatusUseCase
import app.family.domain.usecases.UserUseCase
import app.family.domain.utils.RandomPassCodeGenerator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class DomainModule {

    @Provides
    fun provideLoginUseCase(authApi: AuthApi): LoginUseCase {
        return LoginUseCase(authApi)
    }

    @Provides
    fun provideStatusMapper(): StatusMapper {
        return StatusMapper()
    }

    @Provides
    fun provideUserStatusMapper(statusMapper: StatusMapper): UserStatusMapper {
        return UserStatusMapper(statusMapper)
    }

    @Provides
    fun provideStatusSyncUseCase(
        locationAPI: LocationAPI,
        localityApi: LocalityApi,
        myStatusApi: MyStatusApi,
        deviceApi: DeviceApi,
        weatherApi: WeatherApi
    ): MyStatusSyncUseCase {
        return MyStatusSyncUseCase(locationAPI, localityApi, myStatusApi, deviceApi, weatherApi)
    }

    @Provides
    fun provideMyStatusUseCase(
        myStatusApi: MyStatusApi,
        statusMapper: StatusMapper
    ): MyStatusUseCase {
        return MyStatusUseCase(myStatusApi, statusMapper)
    }

    @Provides
    fun provideUserUseCase(authApi: AuthApi): UserUseCase {
        return UserUseCase(authApi)
    }

    @Provides
    fun familyCreateUseCase(
        authApi: AuthApi,
        userApi: UserApi,
        familyApi: FamilyApi,
        randomPassCodeGenerator: RandomPassCodeGenerator
    ): FamilyCreateUseCase {
        return FamilyCreateUseCase(authApi, userApi, familyApi, randomPassCodeGenerator)
    }

    @Provides
    fun providePassCodeGenerator(): RandomPassCodeGenerator {
        return RandomPassCodeGenerator()
    }

    @Provides
    fun provideMyStatusUploadUseCase(
        authApi: AuthApi,
        myStatusApi: MyStatusApi,
        familyApi: FamilyApi,
        familyCreateUseCase: FamilyCreateUseCase,
    ): UploadStatusUseCase {
        return UploadStatusUseCase(
            authApi = authApi,
            statusApi = myStatusApi,
            familyApi = familyApi,
            familyCreateUseCase = familyCreateUseCase
        )
    }

    @Provides
    fun provideInviteFamilyUseCase(
        authApi: AuthApi,
        userApi: UserApi,
        inviteApi: InviteApi,
        familyCreateUseCase: FamilyCreateUseCase,
        randomPassCodeGenerator: RandomPassCodeGenerator
    ): FamilyInviteUseCase {
        return FamilyInviteUseCase(
            authApi,
            userApi,
            inviteApi,
            familyCreateUseCase,
            randomPassCodeGenerator
        )
    }

    @Provides
    fun provideFetchFamilyStatusUseCase(
        authApi: AuthApi,
        userApi: UserApi,
        familyApi: FamilyApi,
        userStatusMapper: UserStatusMapper
    ): FetchFamilyStatusUseCase {
        return FetchFamilyStatusUseCase(authApi, userApi, familyApi, userStatusMapper)
    }
}