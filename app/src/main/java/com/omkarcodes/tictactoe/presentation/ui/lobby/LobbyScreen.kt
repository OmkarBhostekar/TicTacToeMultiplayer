package com.omkarcodes.tictactoe.presentation.ui.lobby

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.omkarcodes.tictactoe.R
import com.omkarcodes.tictactoe.data.model.Room
import com.omkarcodes.tictactoe.presentation.navigation.Screen
import com.omkarcodes.tictactoe.presentation.theme.darkBlue
import com.omkarcodes.tictactoe.presentation.theme.darkGreen
import com.omkarcodes.tictactoe.presentation.theme.lightPink
import com.omkarcodes.tictactoe.presentation.ui.HomeViewModel
import com.omkarcodes.tictactoe.presentation.util.Constants
import com.omkarcodes.tictactoe.presentation.util.Constants.defaultPadding

@Composable
fun LobbyScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state = viewModel.gameState
    val isReady = if (state.value.sockets.indexOf(viewModel.socketId) == 0) state.value.ready1 else state.value.ready2
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_frame_6),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = Constants.defaultPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        RoomIdComposable(state.value.roomId)
        Spacer(modifier = Modifier.height(defaultPadding * 2))
        PlayersSection(state,viewModel.socketId)
        Spacer(modifier = Modifier.height(defaultPadding * 3))
        InstructionText(viewModel.instruction.value)
        Spacer(modifier = Modifier.weight(1f))
        if (viewModel.choose.value)
            ChoiceSection(
                onOClick = { viewModel.choice("O")
                           navController.navigate(Screen.GameScreen.route)},
                onXClick = { viewModel.choice("X")
                    navController.navigate(Screen.GameScreen.route)}
            )
        Spacer(modifier = Modifier.weight(1f))
        if (state.value.sockets.size == 2 && !isReady)
            ReadyButton {
                viewModel.ready(state.value.roomId)
            }
        Spacer(modifier = Modifier.height(defaultPadding * 2))
    }
}

@Composable
fun ReadyButton(
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(30.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.White
        )
    ) {
        Text(text = "Ready", fontSize = 18.sp, color = darkBlue)
    }
}

@Composable
fun InstructionText(
    text: String
) {
    Text(
        text = text,
        color = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = defaultPadding),
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
fun PlayersSection(state: State<Room>, socketId: String) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        if (state.value.sockets.isNotEmpty()) {
            PlayerCard(
                name = "Player 1 ${if (state.value.sockets.first() == socketId) "(You)" else ""}",
                isReady = state.value.ready1
            )
            Spacer(modifier = Modifier.height(defaultPadding))
        }
        if (state.value.sockets.size > 1)
            PlayerCard(
                name = "Player 2 ${if (state.value.sockets[1] == socketId) "(You)" else ""}",
                isReady = state.value.ready2
            )
    }
}

@Composable
fun PlayerCard(
    name: String,
    isReady: Boolean = false,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(
                color = if (isReady) darkGreen else lightPink,
                shape = RoundedCornerShape(30.dp)
            )
            .padding(horizontal = defaultPadding, vertical = defaultPadding / 2),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = if (isReady) R.drawable.ic_check else R.drawable.ic_close),
            contentDescription = null,
            modifier = Modifier
                .fillMaxHeight()
                .padding(vertical = defaultPadding / 2)
                .aspectRatio(1f)
        )
        Text(
            text = name,
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            fontSize = 15.sp,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = defaultPadding)
        )
    }
}

@Composable
fun ChoiceSection(
    onXClick: () -> Unit,
    onOClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = defaultPadding * 2)
    ) {
        SelectionButton(
            modifier = Modifier
                .weight(1f),
            icon = R.drawable.ic_cross,
            onClick = onXClick
        )
        Spacer(modifier = Modifier.width(defaultPadding * 2))
        SelectionButton(
            modifier = Modifier
                .weight(1f),
            icon = R.drawable.ic_circle,
            onClick = onOClick
        )
    }
}

@Composable
fun RoomIdComposable(
    id: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = buildAnnotatedString {
                pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                append("Room Id: ")
                pop()
                append(id)
            },
            color = Color.White,
        )
    }
}


@Composable
fun SelectionButton(
    icon: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clickable {
                onClick()
            }
    ) {
        Image(
            painter = painterResource(id = R.drawable.rounded_rect),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .padding(defaultPadding * 3 / 2)
        )
    }
}