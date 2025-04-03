package br.com.velha.tech.firebase.to

import br.com.velha.tech.firebase.enums.EnumDifficultLevel
import br.com.velha.tech.firebase.enums.EnumGameStatus
import java.time.LocalDateTime

data class TOGameHistory(
    val id: String,
    val dateGameEnd: LocalDateTime,
    val roomName: String,
    val roundsCount: Int,
    val difficultLevel: EnumDifficultLevel,
    val adversaryName: String,
    val gameStatus: EnumGameStatus
)
