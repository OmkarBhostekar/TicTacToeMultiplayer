package com.omkarcodes.tictactoe.presentation.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ankushgrover.hourglass.Hourglass
import com.google.gson.Gson
import com.omkarcodes.tictactoe.data.model.Choice
import com.omkarcodes.tictactoe.data.model.EventType
import com.omkarcodes.tictactoe.data.model.Room
import com.omkarcodes.tictactoe.presentation.ui.game.MatchStatus
import com.omkarcodes.tictactoe.presentation.ui.game.MoveState
import com.omkarcodes.tictactoe.presentation.util.Constants
import com.omkarcodes.tictactoe.presentation.util.Constants.defaultMoves
import dagger.hilt.android.lifecycle.HiltViewModel
import io.socket.client.Socket
import kotlinx.coroutines.launch
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val socket: Socket,
) : ViewModel() {

    private val gson: Gson = Gson()
    private val _state = mutableStateOf<MoveState>(MoveState())
    val state: State<MoveState> = _state
    private val patterns = Constants.patterns
    private val _timer = mutableStateOf(15)
    val timer: State<Int> = _timer
    var socketId = ""
    private val _gameState = mutableStateOf<Room>(Room())
    val gameState: State<Room> = _gameState
    val choose = mutableStateOf(false)
    val instruction = mutableStateOf("Please wait for other player to join")

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

    fun connect() = viewModelScope.launch {
        Timber.d("connecting")
        socket.on(Socket.EVENT_CONNECT){
            socketId = socket.id()
            Timber.d("connected $socketId")
        }
        socket.on(Socket.EVENT_CONNECT_ERROR){
            Timber.d("connection error ${it[0]}")
        }
        socket.on(Socket.EVENT_DISCONNECT){
            Timber.d("disconnected")
        }
        socket.on(EventType.JOIN_ROOM.event) {
            val room = gson.fromJson(it.first().toString(),Room::class.java)
            setupLobby(room)
        }
        socket.on(EventType.MOVE.event) {

        }
        socket.on(EventType.STATUS.event) {

        }
        socket.on(EventType.NEXT_MOVE.event) {

        }
        socket.on(EventType.CHOOSE.event) {
            val choice = gson.fromJson(it.first().toString(),Choice::class.java)
            if (choice.playerId == socketId)
                instruction.value = "Please choose your side"
            choose.value = choice.playerId == socketId
        }
        socket.on(EventType.READY.event) {
            val room = gson.fromJson(it.first().toString(),Room::class.java)
            setupLobby(room)
        }
        socket.connect()
    }

    private fun setupLobby(room: Room) {
        instruction.value = getInstruction(room)
        _gameState.value = room
    }

    private fun getInstruction(room: Room): String {
        val isReady = if (room.sockets.indexOf(socketId) == 0) room.ready1 else room.ready2
        var i = ""
        i = if (room.sockets.size != 2) "Waiting for other player"
            else if (!isReady) "Please click ready to start"
            else if (!(room.ready1 && room.ready2)) "Waiting for other player to ready"
            else "Start game"
        return i
    }

    fun createRoom() = viewModelScope.launch {
        if (socket.connected()){
            socket.emit(EventType.CREATE_ROOM.event)
        }
    }

    fun joinRoom(code: String) = viewModelScope.launch {
        if (socket.connected()){
            socket.emit(EventType.JOIN_ROOM.event,JSONObject().apply {
                put("roomId",code)
            })
        }
    }

    fun ready(roomId: String) {
        if (socket.connected()){
            socket.emit(EventType.READY.event,JSONObject().apply {
                put("roomId",roomId)
            })
        }
    }

    fun choice(c: String) {
        if (socket.connected()){
            socket.emit(EventType.CHOOSE.event,JSONObject().apply {
                put("roomId",gameState.value.roomId)
                put("icon1",c)
                put("icon2",if (c == "X") "O" else "X")
            })
        }
    }

    fun disconnect() = viewModelScope.launch {
        socket.disconnect()
        socket.off()
    }
}