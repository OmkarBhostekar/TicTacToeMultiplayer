package com.omkarcodes.tictactoe.presentation.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ankushgrover.hourglass.Hourglass
import com.google.gson.Gson
import com.omkarcodes.tictactoe.data.model.*
import com.omkarcodes.tictactoe.presentation.ui.game.MatchStatus
import com.omkarcodes.tictactoe.presentation.ui.game.MoveState
import com.omkarcodes.tictactoe.presentation.ui.game.ResultType
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
    var userIcon = ""
    private val _gameState = mutableStateOf<Room>(Room())
    val gameState: State<Room> = _gameState
    val choose = mutableStateOf(false)
    val start = mutableStateOf(false)
    val userMove = mutableStateOf(false)
    val instruction = mutableStateOf("Please wait for other player to join")
    val result = mutableStateOf(ResultType.IN_PROGRESS)

    private val hourglass: Hourglass = object : Hourglass(30000) {
        override fun onTimerTick(timeRemaining: Long) {
            _timer.value = (timeRemaining/1000).toInt()
        }

        override fun onTimerFinish() {
            _timer.value = 0
        }
    }

    private fun setMove(boxId: Int, type: SelectionType) {
        hourglass.stopTimer()
        hourglass.setTime(30000)
        hourglass.startTimer()
        _state.value = _state.value.copy(
            moves = _state.value.moves.toMutableList().also {
                it[boxId] = UserMove(boxId = boxId, userId = _state.value.movesCount % 2, type = type)
            },
            movesCount = _state.value.movesCount + 1
        )
    }

    fun connect() = viewModelScope.launch {

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
            val move = gson.fromJson(it.first().toString(),Move::class.java)
            val type = if (move.player == socketId) {
                if (userIcon == "X") SelectionType.CROSS else SelectionType.CIRCLE
            }else{
                if (userIcon == "X") SelectionType.CIRCLE else SelectionType.CROSS
            }
            setMove(move.boxId,type)
        }
        socket.on(EventType.STATUS.event) {

        }
        socket.on(EventType.NEXT_MOVE.event) {
            val next = gson.fromJson(it.first().toString(),NextMove::class.java)
            if (next.playerId == socketId)
                instruction.value = "Please select your move"
            else
                instruction.value = "Waiting for opponent's move"
            userMove.value = next.playerId == socketId
        }
        socket.on(EventType.CHOOSE.event) {
            val choice = gson.fromJson(it.first().toString(),Choice::class.java)
            if (choice.playerId == socketId)
                instruction.value = "Please choose your side"
            else
                instruction.value = "Opponent is choosing"
            choose.value = choice.playerId == socketId
        }
        socket.on(EventType.READY.event) {
            val room = gson.fromJson(it.first().toString(),Room::class.java)
            setupLobby(room)
        }
        socket.on(EventType.START.event) {
            val s = gson.fromJson(it.first().toString(), Start::class.java)
            userIcon = if (s.player1 == socketId)
                s.icon1
            else
                s.icon2
            start.value = true
            hourglass.startTimer()
        }
        socket.on(EventType.RESULT.event) {
            val r = gson.fromJson(it.first().toString(), Result::class.java)
            result.value = if (r.winner == userIcon){
                ResultType.YOU_WON
            }else if (r.winner == "no result"){
                ResultType.NO_RESULT
            }else{
                ResultType.YOU_LOSE
            }
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

    fun sendMove(boxId: Int) {
        if (socket.connected()){
            socket.emit(EventType.MOVE.event,JSONObject().apply {
                put("roomId",gameState.value.roomId)
                put("boxId",boxId)
            })
        }
    }

    fun disconnect() = viewModelScope.launch {
        socket.disconnect()
        socket.off()
    }
}