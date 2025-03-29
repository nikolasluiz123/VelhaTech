package br.com.velha.tech.repository

import br.com.velha.tech.firebase.enums.EnumDifficultLevel
import br.com.velha.tech.firebase.models.PlayerDocument
import br.com.velha.tech.firebase.models.RoomDocument
import br.com.velha.tech.firebase.models.RoundDocument
import br.com.velha.tech.firebase.to.TOPlayer
import br.com.velha.tech.firebase.to.TORoom
import br.com.velha.tech.firebase.to.TORound

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
        name = name!!,
        roomOwner = roomOwner,
        figure = figure,
        timer = timer
    )
}

fun TOPlayer.toPlayerDocument(): PlayerDocument {
    return PlayerDocument(
        userId = userId,
        name = name,
        roomOwner = roomOwner,
        figure = figure,
        timer = timer
    )
}


fun RoundDocument.toTORound(): TORound {
    return TORound(
        id = id,
        roundNumber = roundNumber,
        winnerName = winnerName,
        preparingToStart = preparingToStart,
        timerToStart = timerToStart,
        playing = playing
    )
}

fun TORound.toRoundDocument(): RoundDocument {
    val document = RoundDocument(
        roundNumber = roundNumber,
        winnerName = winnerName,
        preparingToStart = preparingToStart,
        timerToStart = timerToStart,
        playing = playing
    )

    id?.let { document.id = it }

    return document
}
