package com.omkarcodes.tictactoe.presentation.ui.welcome

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.omkarcodes.tictactoe.R
import com.omkarcodes.tictactoe.presentation.navigation.Screen
import com.omkarcodes.tictactoe.presentation.util.Constants.TYPE_CIRCLE
import com.omkarcodes.tictactoe.presentation.util.Constants.TYPE_CROSS
import com.omkarcodes.tictactoe.presentation.util.Constants.defaultPadding

@Composable
fun WelcomeScreen(navController: NavController) {
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
            modifier = Modifier.fillMaxSize()
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
            OutlinedButton(
                onClick = { },
                shape = RoundedCornerShape(28.dp),
                border = BorderStroke(1.dp,Color.White),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White,backgroundColor = Color.Transparent),
                modifier = Modifier.fillMaxWidth()
                    .height(56.dp),
            ) {
                Text(text = "Join Room",color = Color.White,fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedButton(
                onClick = { },
                shape = RoundedCornerShape(28.dp),
                border = BorderStroke(1.dp,Color.White),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White,backgroundColor = Color.Transparent),
                modifier = Modifier.fillMaxWidth()
                    .height(56.dp),
            ) {
                Text(text = "Create Room",color = Color.White,fontSize = 16.sp)
            }
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = defaultPadding * 2)
//            ) {
//                SelectionButton(
//                    modifier = Modifier
//                        .weight(1f),
//                    icon = R.drawable.ic_cross,
//                    onClick = {
//                        navController.navigate(Screen.HomeScreen.route+"/$TYPE_CROSS")
//                    }
//                )
//                Spacer(modifier = Modifier.width(defaultPadding * 2))
//                SelectionButton(
//                    modifier = Modifier
//                        .weight(1f),
//                    icon = R.drawable.ic_circle,
//                    onClick = {
//                        navController.navigate(Screen.HomeScreen.route+"/$TYPE_CIRCLE")
//                    }
//                )
//            }
            Spacer(modifier = Modifier.weight(2f))

        }
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