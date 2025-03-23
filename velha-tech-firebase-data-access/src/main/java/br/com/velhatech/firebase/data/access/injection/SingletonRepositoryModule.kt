package br.com.velhatech.firebase.data.access.injection

import br.com.velhatech.firebase.data.access.repository.RoomRepository
import br.com.velhatech.firebase.data.access.service.FirestoreRoomService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class SingletonRepositoryModule {

    @Provides
    fun provideRoomRepository(
        firestoreRoomService: FirestoreRoomService,
    ): RoomRepository {
        return RoomRepository(
            firestoreRoomService = firestoreRoomService,
        )
    }
}