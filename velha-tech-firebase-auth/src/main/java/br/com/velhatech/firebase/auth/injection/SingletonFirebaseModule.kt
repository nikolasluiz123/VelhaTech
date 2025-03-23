package br.com.velhatech.firebase.auth.injection

import android.content.Context
import br.com.velhatech.firebase.auth.implementations.CommonFirebaseAuthenticationService
import br.com.velhatech.firebase.auth.implementations.FirebaseDefaultAuthenticationService
import br.com.velhatech.firebase.auth.implementations.FirebaseGoogleAuthenticationService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class SingletonFirebaseModule {

    @Provides
    fun provideDefaultAuthenticationService(): FirebaseDefaultAuthenticationService {
        return FirebaseDefaultAuthenticationService()
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