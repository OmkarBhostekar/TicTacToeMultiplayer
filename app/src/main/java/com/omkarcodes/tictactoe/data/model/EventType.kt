package com.omkarcodes.tictactoe.data.model

sealed class EventType(val event: String){
    object MOVE : EventType("move")
    object JOIN_ROOM : EventType("joinRoom")
    object CREATE_ROOM : EventType("createRoom")
    object READY : EventType("ready")
    object STATUS : EventType("status")
    object CHOOSE : EventType("choose")
    object NEXT_MOVE : EventType("nextMove")

}
