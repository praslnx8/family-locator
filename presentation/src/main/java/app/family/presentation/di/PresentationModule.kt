package app.family.presentation.di

import app.family.domain.usecases.FetchFamilyStatusUseCase
import app.family.domain.usecases.LoginUseCase
import app.family.domain.usecases.MyStatusUseCase
import app.family.domain.usecases.UserUseCase
import app.family.presentation.vms.FamilyStatusViewModel
import app.family.presentation.vms.LoginViewModel
import app.family.presentation.vms.MyStatusViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class PresentationModule {

    @Provides
    fun provideLoginViewModel(loginUseCase: LoginUseCase): LoginViewModel {
        return LoginViewModel(loginUseCase)
    }

    @Provides
    fun provideMyStatusViewModel(
        userUseCase: UserUseCase,
        myStatusUseCase: MyStatusUseCase
    ): MyStatusViewModel {
        return MyStatusViewModel(userUseCase, myStatusUseCase)
    }

    @Provides
    fun provideFamilyStatusViewModel(fetchFamilyStatusUseCase: FetchFamilyStatusUseCase): FamilyStatusViewModel {
        return FamilyStatusViewModel(fetchFamilyStatusUseCase)
    }
}