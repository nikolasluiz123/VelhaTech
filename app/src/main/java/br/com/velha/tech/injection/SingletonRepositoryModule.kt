package br.com.velha.tech.injection

import br.com.velha.tech.firebase.auth.implementations.FirebaseDefaultAuthenticationService
import br.com.velha.tech.firebase.auth.implementations.FirebaseGoogleAuthenticationService
import br.com.velha.tech.firebase.data.access.service.FirestoreRoomPlayersService
import br.com.velha.tech.firebase.data.access.service.FirestoreRoomRoundService
import br.com.velha.tech.firebase.data.access.service.FirestoreRoomService
import br.com.velha.tech.firebase.data.access.service.FirestoreRoundGameBoardService
import br.com.velha.tech.repository.RoomPlayersRepository
import br.com.velha.tech.repository.RoomRepository
import br.com.velha.tech.repository.RoomRoundRepository
import br.com.velha.tech.repository.RoundGameBoardRepository
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

    @Provides
    fun provideRoomPlayersRepository(
        firestoreRoomPlayersService: FirestoreRoomPlayersService,
    ): RoomPlayersRepository {
        return RoomPlayersRepository(
            firestoreRoomPlayersService = firestoreRoomPlayersService,
        )
    }

    @Provides
    fun provideRoomRoundRepository(
        firestoreRoomRoundService: FirestoreRoomRoundService,
    ): RoomRoundRepository {
        return RoomRoundRepository(
            firestoreRoomRoundService = firestoreRoomRoundService,
        )
    }

    @Provides
    fun provideRoundGameBoardRepository(
        firestoreRoundGameBoardService: FirestoreRoundGameBoardService,
    ): RoundGameBoardRepository {
        return RoundGameBoardRepository(
            roundGameBoardService = firestoreRoundGameBoardService,
        )
    }

}