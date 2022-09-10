package app.family.domain.di

import app.family.api.apis.DeviceApi
import app.family.api.apis.FamilyApi
import app.family.api.apis.FamilyStatusApi
import app.family.api.apis.InviteApi
import app.family.api.apis.LocalityApi
import app.family.api.apis.LocationAPI
import app.family.api.apis.MyStatusApi
import app.family.api.apis.UserApi
import app.family.domain.usecases.FamilyInviteUseCase
import app.family.domain.usecases.LoginUseCase
import app.family.domain.usecases.MyStatusSyncUseCase
import app.family.domain.usecases.MyStatusUploadUseCase
import app.family.domain.usecases.MyStatusUseCase
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

    @Provides
    fun providePassCodeGenerator(): RandomPassCodeGenerator {
        return RandomPassCodeGenerator()
    }

    @Provides
    fun provideMyStatusUploadUseCase(
        userApi: UserApi,
        myStatusApi: MyStatusApi,
        familyApi: FamilyApi,
        familyStatusApi: FamilyStatusApi,
        randomPassCodeGenerator: RandomPassCodeGenerator
    ): MyStatusUploadUseCase {
        return MyStatusUploadUseCase(
            userApi = userApi,
            statusApi = myStatusApi,
            familyApi = familyApi,
            familyStatusApi = familyStatusApi,
            randomPassCodeGenerator = randomPassCodeGenerator
        )
    }

    @Provides
    fun provideInviteFamilyUseCase(
        userApi: UserApi,
        familyApi: FamilyApi,
        inviteApi: InviteApi,
        randomPassCodeGenerator: RandomPassCodeGenerator
    ): FamilyInviteUseCase {
        return FamilyInviteUseCase(userApi, inviteApi, familyApi, randomPassCodeGenerator)
    }
}