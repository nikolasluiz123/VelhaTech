package br.com.velhatech.injection

import br.com.velhatech.firebase.auth.implementations.FirebaseDefaultAuthenticationService
import br.com.velhatech.firebase.auth.implementations.FirebaseGoogleAuthenticationService
import br.com.velhatech.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class SingletonRepositoryModule {

    @Provides
    fun provideUserRepository(
        firebaseDefaultAuthenticationService: FirebaseDefaultAuthenticationService,
        firebaseGoogleAuthenticationService: FirebaseGoogleAuthenticationService,
    ): UserRepository {
        return UserRepository(
            firebaseDefaultAuthenticationService = firebaseDefaultAuthenticationService,
            firebaseGoogleAuthenticationService = firebaseGoogleAuthenticationService,
        )
    }
}