package com.omkarcodes.tictactoe.di

import com.omkarcodes.tictactoe.comman.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.engineio.client.transports.WebSocket
import java.net.URI
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSocketIO() : Socket {
        val opts = IO.Options()
        opts.transports = arrayOf(WebSocket.NAME)
        return IO.socket(Constants.SOCKET_URL, opts)
    }

}
