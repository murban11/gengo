package com.example.gengo.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.example.gengo.R

/*
 * TODO:
 *   - Autofocus on the question, not the top bar
 *   - After user has selected an answer, change the focus to the next button
 */
@Composable
fun CardView(
    quizItem: QuizItem,
    modifier: Modifier = Modifier,
    onNextClick: (wasAnsweredCorrectly: Boolean) -> Unit = {},
) {
    val questionLabel = stringResource(R.string.question)
    val answerLabel = stringResource(R.string.answer)
    val correctLabel = stringResource(R.string.correct)
    val wrongLabel = stringResource(R.string.wrong)

    var answer by remember { mutableIntStateOf(-1) }

    Column(
        modifier = modifier
            .fillMaxHeight(1f),
    ) {
        Row(
            modifier = modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxHeight(0.3f)
        ) {
            Text(
                text = quizItem.question,
                fontSize = 16.em,
                modifier = modifier
                    .align(Alignment.CenterVertically)
                    .semantics {
                        contentDescription = "$questionLabel: ${quizItem.question}"
                    },
            )
        }
        LazyVerticalGrid(
            modifier = modifier
                .padding(4.dp)
                .fillMaxHeight(0.6f),
            columns = GridCells.Fixed(2),
        ) {
            items(quizItem.answers.size) {
                Card(
                    onClick = {
                        if (answer == -1) {
                            answer = it
                        }
                    },
                    modifier = modifier
                        .padding(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (answer > -1 && it == quizItem.indexOfCorrect) {
                            MaterialTheme.colorScheme.tertiaryContainer
                        } else if (answer == it) {
                            MaterialTheme.colorScheme.errorContainer
                        } else {
                            MaterialTheme.colorScheme.secondaryContainer
                        }
                    )
                )
                {
                    Text(
                        text = quizItem.answers[it],
                        fontSize = 16.em,
                        modifier = modifier
                            .align(Alignment.CenterHorizontally)
                            .background(
                                color = if (answer > -1 && it == quizItem.indexOfCorrect) {
                                    MaterialTheme.colorScheme.tertiaryContainer
                                } else if (answer == it) {
                                    MaterialTheme.colorScheme.errorContainer
                                } else {
                                    MaterialTheme.colorScheme.secondaryContainer
                                }
                            )
                            .semantics {
                                stateDescription =
                                    if (answer != -1 && it == quizItem.indexOfCorrect) {
                                        correctLabel
                                    } else if (answer == it) {
                                        wrongLabel
                                    } else {
                                        ""
                                    }
                                contentDescription = "$answerLabel: ${quizItem.answers[it]}"
                            },
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                    )
                }
            }
        }
        Row (
            modifier = modifier
                .padding(8.dp)
                .fillMaxWidth(1f),
        ) {
            Button(
                onClick = {
                    onNextClick(answer == quizItem.indexOfCorrect)
                    answer = -1
                },
                modifier = modifier
                    .fillMaxWidth(1f)
                    .alpha(if (answer > -1) 1f else 0f)
            ) {
                Text(
                    text = stringResource(R.string.next),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CardViewPreview() {
    CardView(
        quizItem = QuizItem(
            question = "a",
            answers = arrayOf("え", "う", "あ", "お"),
            indexOfCorrect = 2,
        )
    )
}