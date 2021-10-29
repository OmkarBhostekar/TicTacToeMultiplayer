package com.omkarcodes.tictactoe.presentation.ui

data class UserMove(
    val boxId: Int,
    val userId: Int = 1,
    val type: SelectionType = SelectionType.NONE,
)
