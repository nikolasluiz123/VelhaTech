package br.com.velhatech.firebase.data.access.injection

import br.com.velhatech.firebase.auth.implementations.CommonFirebaseAuthenticationService
import br.com.velhatech.firebase.data.access.service.FirestoreRoomService
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