package com.omkarcodes.tictactoe.presentation.ui.game

import android.annotation.SuppressLint
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.omkarcodes.tictactoe.R
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
    viewModel: HomeViewModel = hiltViewModel()
) {
    val type = if (t == TYPE_CROSS) SelectionType.CROSS else SelectionType.CIRCLE
    val state = viewModel.state.value

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(defaultPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

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
                text = "Player ${if (state.movesCount % 2 == 0) 1 else 2} 's Turn",
                color = Color.White,
                fontSize = 30.sp,
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
                BoxLayout(moves = state.moves, onClick = { viewModel.setMove(it.boxId, type) })
            }
            Spacer(modifier = Modifier.weight(3f))
        }

        if (state.status == MatchStatus.COMPLETED || state.status == MatchStatus.NO_RESULT)
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = if (state.status == MatchStatus.COMPLETED) {
                        "Player ${if (state.winnerUser!! == 0) 1 else 2} Won"
                    }else {
                        "No Result"
                    },
                    color = Color.Black,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clip(RoundedCornerShape(15.dp))
                        .border(2.dp, color = Color.Black, RoundedCornerShape(15.dp))
                        .background(color = Color.White)
                        .padding(horizontal = defaultPadding * 2, vertical = defaultPadding)
                )
                Spacer(modifier = Modifier.weight(2f))
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