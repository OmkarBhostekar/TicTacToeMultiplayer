package com.omkarcodes.tictactoe.presentation.ui.welcome

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.omkarcodes.tictactoe.R
import com.omkarcodes.tictactoe.presentation.navigation.Screen
import com.omkarcodes.tictactoe.presentation.theme.darkBlue
import com.omkarcodes.tictactoe.presentation.ui.HomeViewModel
import com.omkarcodes.tictactoe.presentation.util.Constants.TYPE_CIRCLE
import com.omkarcodes.tictactoe.presentation.util.Constants.defaultPadding

@Composable
fun WelcomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val roomId = remember { mutableStateOf("") }
    val state = viewModel.gameState
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_frame_6),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = defaultPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(2f))
            Text(
                text = "TIC-TAC-TOE",
                fontSize = 36.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.weight(3f))
            TextField(
                value = roomId.value, 
                onValueChange = { roomId.value = it },
                modifier = Modifier
                    .fillMaxWidth(),
                placeholder = {
                    Text(text = "Enter Room Code")
                },
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = darkBlue,
                    textColor = Color.White,
                    placeholderColor = Color.White.copy(alpha = 0.5f)
                ),
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters)
            )
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedButton(
                onClick = {
                    viewModel.joinRoom(roomId.value)
                    navController.navigate(Screen.LobbyScreen.route)
                },
                shape = RoundedCornerShape(28.dp),
                border = BorderStroke(1.dp,Color.White),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White,backgroundColor = Color.Transparent),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
            ) {
                Text(text = "Join Room",color = Color.White,fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedButton(
                onClick = {
                    viewModel.createRoom()
                    navController.navigate(Screen.LobbyScreen.route)
                },
                shape = RoundedCornerShape(28.dp),
                border = BorderStroke(1.dp,Color.White),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White,backgroundColor = Color.Transparent),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
            ) {
                Text(text = "Create Room",color = Color.White,fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.weight(2f))

        }
    }
}
