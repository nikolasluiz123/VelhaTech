package br.com.velha.tech.injection

import android.content.Context
import br.com.velha.tech.repository.RoomRepository
import br.com.velha.tech.repository.UserRepository
import br.com.velha.tech.usecase.DefaultLoginUseCase
import br.com.velha.tech.usecase.GoogleLoginUseCase
import br.com.velha.tech.usecase.SaveRoomUseCase
import br.com.velha.tech.usecase.SaveUserUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class SingletonUseCaseModule {

    @Provides
    fun provideSaveUserUseCase(
        @ApplicationContext context: Context,
        userRepository: UserRepository
    ): SaveUserUseCase {
        return SaveUserUseCase(
            context = context,
            userRepository = userRepository
        )
    }

    @Provides
    fun provideDefaultLoginUseCase(
        @ApplicationContext context: Context,
        userRepository: UserRepository
    ): DefaultLoginUseCase {
        return DefaultLoginUseCase(
            context = context,
            userRepository = userRepository
        )
    }

    @Provides
    fun provideGoogleLoginUseCase(
        @ApplicationContext context: Context,
        userRepository: UserRepository
    ): GoogleLoginUseCase {
        return GoogleLoginUseCase(
            context = context,
            userRepository = userRepository
        )
    }

    @Provides
    fun provideSaveRoomUseCase(
        @ApplicationContext context: Context,
        roomRepository: RoomRepository
    ): SaveRoomUseCase {
        return SaveRoomUseCase(
            context = context,
            roomRepository = roomRepository
        )
    }
}