package br.com.velha.tech.firebase.auth.injection

import android.content.Context
import br.com.velha.tech.firebase.auth.implementations.CommonFirebaseAuthenticationService
import br.com.velha.tech.firebase.auth.implementations.FirebaseDefaultAuthenticationService
import br.com.velha.tech.firebase.auth.implementations.FirebaseGoogleAuthenticationService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class SingletonFirebaseModule {

    @Provides
    fun provideDefaultAuthenticationService(
        @ApplicationContext context: Context
    ): FirebaseDefaultAuthenticationService {
        return FirebaseDefaultAuthenticationService(
            context = context
        )
    }

    @Provides
    fun provideGoogleAuthenticationService(
        @ApplicationContext context: Context
    ): FirebaseGoogleAuthenticationService {
        return FirebaseGoogleAuthenticationService(context)
    }

    @Provides
    fun provideCommonFirebaseAuthenticationService(): CommonFirebaseAuthenticationService {
        return CommonFirebaseAuthenticationService()
    }

}