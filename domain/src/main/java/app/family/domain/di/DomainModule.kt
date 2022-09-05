package app.family.domain.di

import app.family.api.apis.UserApi
import app.family.domain.usecases.LoginUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DomainModule {

    @Singleton
    @Provides
    fun provideLoginUseCase(userApi: UserApi): LoginUseCase {
        return LoginUseCase(userApi)
    }
}