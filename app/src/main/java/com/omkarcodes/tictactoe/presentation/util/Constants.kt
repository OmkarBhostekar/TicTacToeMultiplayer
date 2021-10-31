package com.omkarcodes.tictactoe.presentation.util

import androidx.compose.ui.unit.dp
import com.omkarcodes.tictactoe.presentation.ui.SelectionType
import com.omkarcodes.tictactoe.presentation.ui.UserMove

object Constants {

    val defaultPadding = 16.dp

    const val TYPE_CROSS = "cross"
    const val TYPE_CIRCLE = "circle"

    val defaultMoves = mutableListOf<UserMove>(
        UserMove(boxId = 0,),
        UserMove(boxId = 1,),
        UserMove(boxId = 2,),
        UserMove(boxId = 3,),
        UserMove(boxId = 4),
        UserMove(boxId = 5,),
        UserMove(boxId = 6),
        UserMove(boxId = 7),
        UserMove(boxId = 8,),
    )
    val patterns = listOf(
        listOf(0,1,2),
        listOf(3,4,5),
        listOf(6,7,8),
        listOf(0,3,6),
        listOf(1,4,7),
        listOf(2,5,8),
        listOf(0,4,8),
        listOf(2,4,6),
    )
}