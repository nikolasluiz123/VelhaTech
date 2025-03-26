package br.com.velha.tech.firebase.data.access.injection

import br.com.velha.tech.firebase.auth.implementations.CommonFirebaseAuthenticationService
import br.com.velha.tech.firebase.data.access.service.FirestoreRoomService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class SingletonServiceModule {

    @Provides
    fun provideFirestoreRoomService(
        commonFirebaseAuthenticationService: CommonFirebaseAuthenticationService
    ): FirestoreRoomService {
        return FirestoreRoomService(
            commonFirebaseAuthenticationService = commonFirebaseAuthenticationService
        )
    }
}