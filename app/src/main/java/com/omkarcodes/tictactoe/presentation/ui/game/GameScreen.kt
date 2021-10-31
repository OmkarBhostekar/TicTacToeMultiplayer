package com.omkarcodes.tictactoe.presentation.ui.game

import android.annotation.SuppressLint
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.omkarcodes.tictactoe.R
import com.omkarcodes.tictactoe.presentation.theme.lightBlue
import com.omkarcodes.tictactoe.presentation.ui.HomeViewModel
import com.omkarcodes.tictactoe.presentation.ui.SelectionType
import com.omkarcodes.tictactoe.presentation.ui.UserMove
import com.omkarcodes.tictactoe.presentation.util.Constants.TYPE_CROSS
import com.omkarcodes.tictactoe.presentation.util.Constants.defaultPadding

@SuppressLint("UnrememberedMutableState")
@ExperimentalFoundationApi
@Composable
fun GameScreen(
    t: String? = TYPE_CROSS,
    viewModel: HomeViewModel = hiltViewModel(),
    navController: NavController
) {
    val state = viewModel.state.value

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(defaultPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(modifier = Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.End) {
                Image(
                    painter = painterResource(id = if (t == TYPE_CROSS) R.drawable.ic_cross else R.drawable.ic_circle),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )
            }
            Spacer(modifier = Modifier.weight(2f))
            Text(
                text = if (viewModel.timer.value < 10) "00:0${viewModel.timer.value}" else "00:${viewModel.timer.value}",
                fontSize = 22.sp,
                color = Color.Black,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .background(color = Color.White, shape = RoundedCornerShape(30.dp))
                    .padding(horizontal = defaultPadding * 3 / 2, vertical = defaultPadding * 3 / 4)
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = viewModel.instruction.value,
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.weight(4f))
            Box(
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .background(color = Color.White, shape = RoundedCornerShape(15.dp))
                    .padding(defaultPadding)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_union),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                )
                BoxLayout(moves = state.moves, onClick = {
                    if (viewModel.userMove.value)
                        viewModel.sendMove(it.boxId)
                })
            }
            Spacer(modifier = Modifier.weight(3f))
        }
        if (viewModel.result.value != ResultType.IN_PROGRESS){
            ResultDialog(
                result = viewModel.result.value,
                onPlayAgainClick = { },
                onExitClick = {
                    navController.popBackStack()
                }
            )
        }
    }


}

@ExperimentalFoundationApi
@Composable
fun BoxLayout(
    moves: List<UserMove>,
    onClick: (m: UserMove) -> Unit
) {
    LazyVerticalGrid(cells = GridCells.Fixed(3)) {

        items(moves.size) { index ->
            val move = moves[index]
            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .padding(defaultPadding)
                    .clickable {
                        if (move.type == SelectionType.NONE)
                            onClick(UserMove(move.boxId, move.userId, SelectionType.CROSS))
                    }
            ) {
                if (move.type != SelectionType.NONE)
                    Image(
                        painter = painterResource(id = if (move.type == SelectionType.CROSS) R.drawable.ic_cross else R.drawable.ic_circle),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    )
            }
        }
    }
}

@Composable
fun ResultDialog(
    result: ResultType,
    onExitClick: () -> Unit,
    onPlayAgainClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {  },
        title = {
            Text(text = if (result == ResultType.YOU_WON) "You Won" else if (result == ResultType.YOU_LOSE) "You Lost" else "No Result")
        },
        text = {
            Text(text = if (result == ResultType.YOU_WON) "Congratulations! you won the match" else if (result == ResultType.YOU_LOSE) "Oops! better luck next time" else "Play one more match to decide winner")
        },
        confirmButton = {
            Button(
                onClick = onPlayAgainClick,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = lightBlue,
                    contentColor = Color.White)
            ) {
                Text(text = "Play Again")
            }
        },
        dismissButton = {
            Button(
                onClick = onExitClick,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.White,
                    contentColor = lightBlue),
            ) {
                Text(text = "Exit")
            }
        },
        backgroundColor = Color.White,
    )
}