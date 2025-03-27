package br.com.velha.tech.repository

import br.com.velha.tech.firebase.enums.EnumDifficultLevel
import br.com.velha.tech.firebase.models.PlayerDocument
import br.com.velha.tech.firebase.models.RoomDocument
import br.com.velha.tech.firebase.to.TOPlayer
import br.com.velha.tech.firebase.to.TORoom

fun TORoom.toRoomDocument(): RoomDocument {
    val document = RoomDocument(
        roomName = roomName,
        roundsCount = roundsCount,
        difficultLevel = difficultLevel?.name,
        password = password,
        playersCount = playersCount
    )

    id?.let { document.id = it }

    return document
}

fun RoomDocument.toTORoom(): TORoom {
    return TORoom(
        id = id,
        roomName = roomName,
        roundsCount = roundsCount,
        difficultLevel = EnumDifficultLevel.valueOf(difficultLevel!!),
        password = password,
        playersCount = playersCount,
    )
}

fun PlayerDocument.toTOPlayer(): TOPlayer {
    return TOPlayer(
        userId = userId!!,
        name = name!!
    )
}

fun TOPlayer.toPlayerDocument(): PlayerDocument {
    return PlayerDocument(
        userId = userId,
        name = name
    )
}
