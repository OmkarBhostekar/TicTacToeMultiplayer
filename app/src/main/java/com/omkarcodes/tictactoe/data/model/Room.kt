package com.omkarcodes.tictactoe.data.model

data class Room(
    val roomId: String = "",
    val sockets: List<String> = emptyList(),
    val ready1: Boolean = false,
    val ready2: Boolean = false,
    val moves: List<String> = emptyList(),
    val nextMove: Int = -1,
    val player1Icon: String = "",
    val player2Icon: String = "",
)
