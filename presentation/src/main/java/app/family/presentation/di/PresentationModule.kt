package app.family.presentation.di

import app.family.domain.usecases.FetchFamilyStatusUseCase
import app.family.domain.usecases.LoginUseCase
import app.family.domain.usecases.MessageUseCase
import app.family.domain.usecases.MyStatusUseCase
import app.family.domain.usecases.UploadStatusUseCase
import app.family.domain.usecases.UserUseCase
import app.family.presentation.vms.ChatViewModel
import app.family.presentation.vms.FamilyStatusViewModel
import app.family.presentation.vms.HomeViewModel
import app.family.presentation.vms.LoginViewModel
import app.family.presentation.vms.MapScreenViewModel
import app.family.presentation.vms.MyStatusViewModel
import app.family.presentation.vms.ProfileViewModel
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

    @Provides
    fun provideHomeViewModel(uploadStatusUseCase: UploadStatusUseCase): HomeViewModel {
        return HomeViewModel(uploadStatusUseCase)
    }

    @Provides
    fun provideChatViewModel(
        messageUseCase: MessageUseCase,
        userUseCase: UserUseCase
    ): ChatViewModel {
        return ChatViewModel(messageUseCase, userUseCase)
    }

    @Provides
    fun provideProfileViewModel(userUseCase: UserUseCase): ProfileViewModel {
        return ProfileViewModel(userUseCase)
    }

    @Provides
    fun provideMapViewModel(
        userUseCase: UserUseCase,
        myStatusUseCase: MyStatusUseCase,
        familyStatusUseCase: FetchFamilyStatusUseCase
    ): MapScreenViewModel {
        return MapScreenViewModel(userUseCase, myStatusUseCase, familyStatusUseCase)
    }
}