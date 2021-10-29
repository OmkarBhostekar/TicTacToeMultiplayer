package com.omkarcodes.tictactoe.presentation.ui.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.ankushgrover.hourglass.Hourglass
import com.omkarcodes.tictactoe.presentation.ui.SelectionType
import com.omkarcodes.tictactoe.presentation.ui.UserMove
import com.omkarcodes.tictactoe.presentation.util.Constants
import com.omkarcodes.tictactoe.presentation.util.Constants.defaultMoves
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(

) : ViewModel() {

    private val _state = mutableStateOf<MoveState>(MoveState())
    val state: State<MoveState> = _state
    private val patterns = Constants.patterns
    private val _timer = mutableStateOf(15)
    val timer: State<Int> = _timer
    private val hourglass: Hourglass = object : Hourglass(15000) {
        override fun onTimerTick(timeRemaining: Long) {
            _timer.value = (timeRemaining/1000).toInt()
        }

        override fun onTimerFinish() {
            _timer.value = 0
        }
    }

    init {
        hourglass.startTimer()
    }

    fun setMove(boxId: Int,type: SelectionType) {
        hourglass.stopTimer()
        hourglass.setTime(15000)
        hourglass.startTimer()
        _state.value = _state.value.copy(
            moves = _state.value.moves.toMutableList().also {
                it[boxId-1] = UserMove(boxId = boxId, userId = _state.value.movesCount % 2, type = getMoveType(type, _state.value.movesCount))
            },
            movesCount = _state.value.movesCount + 1
        )
        val result = checkWin()
        if (result != null){
            if (result == type){
                // player 1 won
                _state.value = _state.value.copy(
                    status = MatchStatus.COMPLETED,
                    winnerUser = 0,
                    movesCount = 0,
                    moves = defaultMoves
                )
            }else{
                // player 2 won
                _state.value = _state.value.copy(
                    status = MatchStatus.COMPLETED,
                    winnerUser = 1,
                    movesCount = 0,
                    moves = defaultMoves
                )
            }
        }else if (_state.value.movesCount >= 9){
            // draw
            _state.value = _state.value.copy(
                status = MatchStatus.NO_RESULT,
                winnerUser = 0,
                movesCount = 0,
                moves = defaultMoves
            )
        }
    }

    private fun checkWin() : SelectionType? {
        patterns.forEach { p ->
            val list = mutableListOf<SelectionType>()
            p.forEach { boxId ->
                list.add(_state.value.moves[boxId-1].type)
            }
            if (list.filter { it == SelectionType.CROSS }.size == 3 || list.filter { it == SelectionType.CIRCLE }.size == 3)
                return list.first()
        }
        return null
    }

    private fun getMoveType(type: SelectionType, moveCount: Int) : SelectionType {
        return if (moveCount % 2 == 0){
            // first player
            if (type == SelectionType.CROSS) SelectionType.CROSS else SelectionType.CIRCLE
        }else{
            if (type == SelectionType.CROSS) SelectionType.CIRCLE else SelectionType.CROSS
        }
    }

}