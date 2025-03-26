package br.com.velha.tech.injection

import br.com.velha.tech.firebase.auth.implementations.FirebaseDefaultAuthenticationService
import br.com.velha.tech.firebase.auth.implementations.FirebaseGoogleAuthenticationService
import br.com.velha.tech.firebase.data.access.service.FirestoreRoomService
import br.com.velha.tech.repository.RoomRepository
import br.com.velha.tech.repository.UserRepository
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

    @Provides
    fun provideRoomRepository(
        firestoreRoomService: FirestoreRoomService,
    ): RoomRepository {
        return RoomRepository(
            firestoreRoomService = firestoreRoomService,
        )
    }
}