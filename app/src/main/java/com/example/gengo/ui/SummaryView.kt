package com.example.gengo.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.example.gengo.R

@Composable
fun SummaryView(
    pointsEarned: Int,
    pointsTotal: Int,
    modifier: Modifier = Modifier,
    onReturnClick: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxHeight(1f)
            .fillMaxWidth(1f),
    ) {
        Spacer(
            modifier = modifier
                .fillMaxHeight(0.2f)
        )
        Row(
            modifier = modifier
                .align(Alignment.CenterHorizontally)
                .padding(4.dp),
        ) {
           Text(
               text = stringResource(R.string.score),
               fontSize = 10.em,
           )
        }
        Row(
            modifier = modifier
                .align(Alignment.CenterHorizontally)
                .padding(4.dp),
        ) {
            Text(
                text = "$pointsEarned / $pointsTotal",
                fontSize = 16.em,
                fontWeight = FontWeight(500),
            )
        }
        Spacer(
            modifier = modifier
                .fillMaxHeight(0.1f)
        )
        Row(
            modifier = modifier
                .align(Alignment.CenterHorizontally)
                .padding(4.dp),
        ) {
            Button(
                onClick = {
                    onReturnClick()
                }
            ) {
                Text(
                    text = stringResource(R.string.return_),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SummaryViewPreview() {
    SummaryView(
        pointsEarned = 11,
        pointsTotal = 16,
    )
}