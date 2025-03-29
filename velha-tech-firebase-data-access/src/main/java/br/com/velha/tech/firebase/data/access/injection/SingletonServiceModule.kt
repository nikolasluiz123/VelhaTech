package br.com.velha.tech.firebase.data.access.injection

import br.com.velha.tech.firebase.auth.implementations.CommonFirebaseAuthenticationService
import br.com.velha.tech.firebase.data.access.service.FirestoreRoomPlayersService
import br.com.velha.tech.firebase.data.access.service.FirestoreRoomRoundService
import br.com.velha.tech.firebase.data.access.service.FirestoreRoomService
import br.com.velha.tech.firebase.data.access.service.FirestoreRoundGameBoardService
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
        )
    }

    @Provides
    fun provideFirestoreRoomPlayersService(
        commonFirebaseAuthenticationService: CommonFirebaseAuthenticationService,
        roomService: FirestoreRoomService
    ): FirestoreRoomPlayersService {
        return FirestoreRoomPlayersService(
            commonFirebaseAuthenticationService = commonFirebaseAuthenticationService,
            roomService = roomService
        )
    }

    @Provides
    fun provideFirestoreRoomRoundService(): FirestoreRoomRoundService {
        return FirestoreRoomRoundService()
    }

    @Provides
    fun provideFirestoreRoundGameBoardService(
        roomRoundService: FirestoreRoomRoundService
    ): FirestoreRoundGameBoardService {
        return FirestoreRoundGameBoardService(
            roomRoundService = roomRoundService
        )
    }
}