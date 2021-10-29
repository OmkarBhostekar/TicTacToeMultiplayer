package com.omkarcodes.tictactoe.presentation.ui.home

import com.omkarcodes.tictactoe.presentation.ui.UserMove
import com.omkarcodes.tictactoe.presentation.util.Constants.defaultMoves

data class MoveState(
    val isWaiting: Boolean = false,
    var moves: List<UserMove> = defaultMoves,
    val error: String = "",
    val movesCount: Int = 0,
    val status: MatchStatus = MatchStatus.IN_PROGRESS,
    val winnerUser: Int? = null
)

enum class MatchStatus {
    IN_PROGRESS,
    COMPLETED,
    NO_RESULT
}