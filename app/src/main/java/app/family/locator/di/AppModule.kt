package app.family.locator.di

import android.content.Context
import androidx.core.app.NotificationManagerCompat
import app.family.locator.utils.NotificationUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class AppModule {
    @Provides
    fun provideNotificationUtils(@ApplicationContext context: Context): NotificationUtils {
        return NotificationUtils(context, NotificationManagerCompat.from(context))
    }
}