package br.com.velha.tech.repository

import br.com.velha.tech.core.extensions.toEpochSeconds
import br.com.velha.tech.core.extensions.toLocalDateTime
import br.com.velha.tech.firebase.enums.EnumDifficultLevel
import br.com.velha.tech.firebase.enums.EnumGameStatus
import br.com.velha.tech.firebase.models.GameHistory
import br.com.velha.tech.firebase.models.PlayerDocument
import br.com.velha.tech.firebase.models.PlayerGamesHistory
import br.com.velha.tech.firebase.models.PlayerTimerDocument
import br.com.velha.tech.firebase.models.RoomDocument
import br.com.velha.tech.firebase.models.RoundDocument
import br.com.velha.tech.firebase.models.RoundTimerDocument
import br.com.velha.tech.firebase.to.TOGameHistory
import br.com.velha.tech.firebase.to.TOPlayer
import br.com.velha.tech.firebase.to.TOPlayerGamesHistory
import br.com.velha.tech.firebase.to.TOPlayerTimer
import br.com.velha.tech.firebase.to.TORoom
import br.com.velha.tech.firebase.to.TORound
import br.com.velha.tech.firebase.to.TORoundTimer

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
        playing = playing
    )
}

fun TOPlayer.toPlayerDocument(): PlayerDocument {
    return PlayerDocument(
        userId = userId,
        name = name,
        roomOwner = roomOwner,
        figure = figure,
        playing = playing
    )
}

fun TOPlayerTimer.toPlayerTimerDocument(): PlayerTimerDocument {
    val document = PlayerTimerDocument(
        timer = timer
    )

    id?.let { document.id = it }

    return document
}

fun PlayerTimerDocument.toTOPlayerTimer(): TOPlayerTimer {
    return TOPlayerTimer(
        id = id,
        timer = timer
    )
}

fun RoundDocument.toTORound(): TORound {
    return TORound(
        id = id,
        roundNumber = roundNumber,
        winnerPlayerId = winnerPlayerId,
        playing = playing,
        finished = finished,
        preparingToStart = preparingToStart
    )
}

fun TORound.toRoundDocument(): RoundDocument {
    val document = RoundDocument(
        roundNumber = roundNumber,
        winnerPlayerId = winnerPlayerId,
        playing = playing,
        finished = finished,
        preparingToStart = preparingToStart
    )

    id?.let { document.id = it }

    return document
}

fun PlayerGamesHistory.toTOPlayerGamesHistory(): TOPlayerGamesHistory {
    return TOPlayerGamesHistory(
        userId = userId
    )
}

fun TOPlayerGamesHistory.toPlayerGamesHistory(): PlayerGamesHistory {
    return PlayerGamesHistory(
        userId = userId
    )
}

fun GameHistory.toTOGameHistory(): TOGameHistory {
    return TOGameHistory(
        id = id,
        dateGameEnd = dateGameEnd!!.toLocalDateTime(),
        roomName = roomName!!,
        roundsCount = roundsCount!!,
        difficultLevel = EnumDifficultLevel.valueOf(difficultLevel!!),
        adversaryName = adversaryName!!,
        gameStatus = EnumGameStatus.valueOf(gameStatus!!)
    )
}

fun TOGameHistory.toGameHistory(): GameHistory {
    return GameHistory(
        id = id,
        dateGameEnd = dateGameEnd.toEpochSeconds(),
        roomName = roomName,
        roundsCount = roundsCount,
        difficultLevel = difficultLevel.name,
        adversaryName = adversaryName,
        gameStatus = gameStatus.name
    )
}

fun RoundTimerDocument.toTORoundTimer(): TORoundTimer {
    return TORoundTimer(
        id = id,
        timer = timer
    )
}

fun TORoundTimer.toRoundTimerDocument(): RoundTimerDocument {
    val document = RoundTimerDocument(
        timer = timer
    )

    id?.let { document.id = it }

    return document
}
